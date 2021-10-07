package com.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.adapters.DownloadModel;
import com.adapters.MySavedImagesAdapter;
import com.adapters.MySavedImagesAdapter.onItemClickListener;
import com.adapters.MySavedImagesAdapter.onItemLongClickListener;
import com.adapters.SpacesItemDecoration;
import com.fury.instafull.DialogUtils;
import com.fury.instafull.DialogUtils.DialogBtnClickListener;
import com.fury.instafull.Profile_page;
import com.fury.instafull.R;
import com.fury.instafull.ViewImageActivity;
import com.google.firebase.crash.FirebaseCrash;
import com.instagram.data.Utils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class TabMyImages extends Fragment implements onItemLongClickListener, onItemClickListener {
    public static TabMyImages act;
    public static ArrayList<DownloadModel> items;
    private String currentPath;
    private boolean doCut;
    private boolean isSelecting;
    private MySavedImagesAdapter mAdapter;
    Context mContext;
    Menu menu;
    private RecyclerView recyclerView;
    private int selectedPos;
    TextView tvLoading;
    SharedPreferences one_play_preferences;
    SharedPreferences.Editor one_play_editor;

    /* renamed from: com.fragments.TabMyImages.2 */
    class C03962 extends AsyncTask<Void, Void, Void> {

        /* renamed from: com.fragments.TabMyImages.2.1 */
        class C03941 implements FileFilter {
            C03941() {
            }

            public boolean accept(File pathname) {
                String fileName = pathname.getName();
                return fileName.endsWith(".jpg") || fileName.endsWith(".png");
            }
        }

        /* renamed from: com.fragments.TabMyImages.2.2 */
        class C03952 implements Comparator<File> {
            C03952() {
            }

            public int compare(File f1, File f2) {
                return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
            }
        }

        C03962() {
        }

        protected Void doInBackground(Void... arg0) {
            File[] files = new File(currentPath).listFiles(new C03941());
            if (files != null) {
                Arrays.sort(files, new C03952());
            }
            if (files != null) {
                for (File path : files) {
                    items.add(new DownloadModel(path.getAbsolutePath()));
                }
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            if (!items.isEmpty()) {
                Collections.reverse(items);
            }
            mAdapter = new MySavedImagesAdapter(getActivity(),items);
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(TabMyImages.this);
            mAdapter.setOnItemLongClickListener(TabMyImages.this);
            checkListSize();
            super.onPostExecute(result);
        }
    }

    /* renamed from: com.fragments.TabMyImages.4 */
    class C04004 implements OnClickListener {
        private final /* synthetic */ int val$position;

        /* renamed from: com.fragments.TabMyImages.4.1 */
        class C03981 implements DialogBtnClickListener {
            private final /* synthetic */ int val$position;

            C03981(int i) {
                this.val$position = i;
            }

            public void onPositiveClick() {
                new deleteSingleFilesAsync(this.val$position).execute();
            }
        }

        C04004(int i) {
            this.val$position = i;
        }

        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case R.styleable.View_android_theme /*0*/:
                    Intent share = new Intent("android.intent.action.SEND");
                    share.setType("image/*");
                    share.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(((DownloadModel) TabMyImages.items.get(this.val$position)).filePath)));
                    TabMyImages.this.startActivity(Intent.createChooser(share, "Share Picture Via.."));
                    FirebaseCrash.log("C04004 0");
                    break;
                case R.styleable.View_android_focusable /*1*/:
                    try {
                        createInstagramIntent("image/*", ((DownloadModel) TabMyImages.items.get(this.val$position)).filePath);
                    } catch (Exception e) {
                        Toast.makeText(TabMyImages.this.getActivity(), "بدون اینستاگرام چجوری دانلود کردی؟", Toast.LENGTH_LONG).show();
                    }
                    FirebaseCrash.log("C04004 1");
                    break;
                case R.styleable.View_paddingStart /*2*/:
                    DialogUtils.showDeleteDialog(Profile_page.act, new C03981(this.val$position));
                    FirebaseCrash.log("C04004 2");
                    break;
            }
        }
    }

    /* renamed from: com.fragments.TabMyImages.6 */
    class C04026 implements View.OnClickListener {
        private final /* synthetic */ Dialog val$d;
        private final /* synthetic */ ArrayList val$listSelected;

        C04026(ArrayList arrayList, Dialog dialog) {
            this.val$listSelected = arrayList;
            this.val$d = dialog;
        }

        public void onClick(View rippleView) {
            doCut = true;
            Intent itShare = new Intent("ACTION_LOCK_FROM_SHARE_ACTIVITY");
            itShare.putExtra("doCut", true);
            itShare.putExtra("type", "insta_pics");
            itShare.putStringArrayListExtra("android.intent.extra.STREAM", this.val$listSelected);
            try {
                startActivityForResult(itShare, 215);
            } catch (Exception e) {
                DialogUtils.showUpdateDialog(Profile_page.act);
                FirebaseCrash.report(new Exception("r 1"));
            }
            this.val$d.dismiss();
        }
    }

    /* renamed from: com.fragments.TabMyImages.7 */
    class C04037 implements View.OnClickListener {
        private final /* synthetic */ Dialog val$d;
        private final /* synthetic */ ArrayList val$listSelected;

        C04037(ArrayList arrayList, Dialog dialog) {
            val$listSelected = arrayList;
            val$d = dialog;
        }

        public void onClick(View rippleView) {
            doCut = false;
            Intent itShare = new Intent("ACTION_LOCK_FROM_SHARE_ACTIVITY");
            itShare.putExtra("doCut", false);
            itShare.putExtra("type", "insta_pics");
            itShare.putStringArrayListExtra("android.intent.extra.STREAM", this.val$listSelected);
            try {
                startActivityForResult(itShare, 215);
            } catch (Exception e) {
                DialogUtils.showUpdateDialog(Profile_page.act);
                FirebaseCrash.report(new Exception("r 2"));
            }
            this.val$d.dismiss();
        }
    }

    class deleteSingleFilesAsync extends AsyncTask<Void, Integer, Void> {
        ProgressDialog pd;
        int pos;

        public deleteSingleFilesAsync(int pos) {
            this.pos = pos;
        }

        protected void onPreExecute() {
            this.pd = new ProgressDialog(TabMyImages.this.getActivity());
            this.pd.setMessage("deleting..");
            this.pd.setCancelable(false);
            this.pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.pd.show();
            this.pd.setMax(1);
            super.onPreExecute();
        }

        protected Void doInBackground(Void... arg0) {
            DownloadModel model = (DownloadModel) TabMyImages.items.get(this.pos);
            if (new File(model.filePath).delete()) {
                Utils.scanDeletedMedia(TabMyImages.this.getActivity(), new File(model.filePath), "image/*");
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            try {
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
                if (mAdapter != null) {
                    mAdapter.removeItem(pos);
                }
                checkListSize();
            } catch (Exception e) {
                FirebaseCrash.report(new Exception("r 3"));
            }
            super.onPostExecute(result);
        }
    }

    static {
        items = new ArrayList();
    }

    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        mContext = getActivity().getApplicationContext();
        one_play_preferences = getContext().getApplicationContext().getSharedPreferences("PROJECT_NAME", android.content.Context.MODE_PRIVATE);
        one_play_editor = one_play_preferences.edit();
        super.onCreate(savedInstanceState);
        FirebaseCrash.log("log 1");
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_myimages, container, false);
        String name_profile = one_play_preferences.getString("PROFILE", "InstaFull/Profile");
        act = this;
        items.clear();
        tvLoading = (TextView) v.findViewById(R.id.tvLoading);
        currentPath = Environment.getExternalStorageDirectory().toString() + "/" + name_profile;
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(new SpacesItemDecoration(2));
        new C03962().execute();
        return v;
    }

    private void checkListSize() {
        if (items.size() > 0) {
            tvLoading.setVisibility(View.GONE);
            return;
        }
        tvLoading.setText("هنوز هیچ تصویری دانلود نکردی؟");
        tvLoading.setVisibility(View.VISIBLE);
    }

    public void onResume() {
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        super.onResume();
    }

    public void removeSingleImageFromViewer(int index) {
        mAdapter.removeItem(index);
        checkListSize();
    }

    public void onItemLongClick(int position) {
        selectedPos = position;
        CharSequence[] colors = new CharSequence[]{ "ارسال به دوستان", "ارسال به صورت پست", "حذف "};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("امکانات");
        builder.setItems(colors, new C04004(position));
        builder.show();
        FirebaseCrash.log("log 2");
    }

    private void createInstagramIntent(String type, String mediaPath) throws Exception {
        Intent share = new Intent("android.intent.action.SEND");
        share.setType(type);
        Uri uri = Uri.fromFile(new File(mediaPath));
        share.setPackage("com.instagram.android");
        share.putExtra("android.intent.extra.STREAM", uri);
        startActivity(share);
        FirebaseCrash.log("log 3");
    }

    public void onItemClick(int position) {
        Intent it = new Intent(mContext, ViewImageActivity.class);
        it.putExtra("position", position);
        startActivity(it);
        FirebaseCrash.log("log 4");
    }

    private void showHideDialog(ArrayList<String> listSelected) {
        Dialog d = new Dialog(getActivity(), R.style.CustomDialogTheme);
        View v2 = getActivity().getLayoutInflater().inflate(R.layout.delete_dialog, null);
        TextView tv2 = (TextView) v2.findViewById(R.id.textView1);
        tv2.setTypeface(Utils.tf);
        tv2.setText("HIDE OPTIONS");
        TextView tv4 = (TextView) v2.findViewById(R.id.tv_dialogText);
        tv4.setTypeface(Utils.tf);
        tv4.setText("choose option for your selected picture(s)");
        d.setContentView(v2);
        ((TextView) v2.findViewById(R.id.tvDelete)).setText("MOVE");
        ((TextView) v2.findViewById(R.id.tvCancel)).setText("KEEP A COPY");
        v2.findViewById(R.id.rlDelete).setOnClickListener(new C04026(listSelected, d));
        v2.findViewById(R.id.rlCancel).setOnClickListener(new C04037(listSelected, d));
        d.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 215 && resultCode == -1) {
            if (doCut) {
                ArrayList<DownloadModel> listSelected = mAdapter.getSelectedModel();
                if (listSelected.size() > 0) {
                    items.removeAll(listSelected);
                } else {
                    items.remove(this.selectedPos);
                }
            }
            isSelecting = false;
            mAdapter.resetAdapter();
            checkListSize();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
