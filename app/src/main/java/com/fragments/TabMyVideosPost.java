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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.adapters.DownloadModel;
import com.adapters.MySavedVideosAdapter;
import com.adapters.MySavedVideosAdapter.onItemClickListener;
import com.adapters.MySavedVideosAdapter.onItemLongClickListener;
import com.adapters.SpacesItemDecoration;
import com.fury.instafull.CreatedVideoActivity;
import com.fury.instafull.DialogUtils;
import com.fury.instafull.DialogUtils.DialogBtnClickListener;
import com.fury.instafull.IFrameActivity;
import com.fury.instafull.Post_view;
import com.fury.instafull.R;
import com.google.firebase.crash.FirebaseCrash;
import com.instagram.data.Utils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class TabMyVideosPost extends Fragment implements onItemClickListener, onItemLongClickListener {
    public static TabMyVideosPost tabVideos;
    private boolean doCut;
    String filePath;
    private boolean isSelecting;
    ArrayList<DownloadModel> items;
    private MySavedVideosAdapter mAdapter;
    Context mContext;
    Menu menu;
    private RecyclerView recyclerView;
    int selectedPos;
    TextView tvLoading;
    SharedPreferences one_play_preferences;
    SharedPreferences.Editor one_play_editor;

    /* renamed from: com.fragments.TabMyVideos.1 */

    /* renamed from: com.fragments.TabMyVideos.2 */
    class C04062 extends AsyncTask<Void, Void, Void> {
        private final /* synthetic */ String val$mp3Path;

        /* renamed from: com.fragments.TabMyVideos.2.1 */
        class C04051 implements FileFilter {
            C04051() {
            }

            public boolean accept(File pathname) {
                String fileName = pathname.getName();
                return fileName.endsWith(".mp4") || fileName.endsWith(".mkv");
            }
        }

        C04062(String str) {
            this.val$mp3Path = str;
        }

        protected Void doInBackground(Void... params) {
            File f = new File(this.val$mp3Path);
            if (!f.exists()) {
                f.mkdirs();
            }
            if (f.listFiles() != null) {
                for (File path : f.listFiles(new C04051())) {
                    items.add(new DownloadModel(path.getAbsolutePath()));
                }
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (!items.isEmpty()) {
                Collections.reverse(items);
            }
            mAdapter = new MySavedVideosAdapter(getActivity(), items);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(), 2));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(TabMyVideosPost.this);
            mAdapter.setOnItemLongClickListener(TabMyVideosPost.this);
            checkListSize();
        }
    }

    /* renamed from: com.fragments.TabMyVideos.3 */
    class C04083 implements OnClickListener {
        private final /* synthetic */ int val$selectedPos;

        /* renamed from: com.fragments.TabMyVideos.3.1 */
        class C04071 implements DialogBtnClickListener {
            C04071() {
            }

            public void onPositiveClick() {
                new deleteSingleFilesAsync(selectedPos).execute(new Void[0]);
            }
        }

        C04083(int i) {
            this.val$selectedPos = i;
        }

        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case R.styleable.View_android_theme /*0*/:
                    String path = ((DownloadModel) items.get(this.val$selectedPos)).filePath;
                    if (Character.isLetter(new File(path).getName().toCharArray()[0])) {
                        Intent it = new Intent(mContext, IFrameActivity.class);
                        it.putExtra("filePath", path);
                        startActivity(it);
                        return;
                    }
                    Toast.makeText(mContext, "Details are only available for new saved items from updated app's version", Toast.LENGTH_LONG).show();
                    FirebaseCrash.log("C04083 0");
                    break;
                case R.styleable.View_android_focusable /*1*/:
                    Intent sharingIntent = new Intent("android.intent.action.SEND");
                    Uri screenshotUri = Uri.parse(((DownloadModel) items.get(this.val$selectedPos)).filePath);
                    sharingIntent.setType("video/*");
                    sharingIntent.putExtra("android.intent.extra.STREAM", screenshotUri);
                    startActivity(Intent.createChooser(sharingIntent, "Share video using"));
                    FirebaseCrash.log("C04083 1");
                    break;
                case R.styleable.View_paddingStart /*2*/:
                    try {
                        createInstagramIntent("video/*", ((DownloadModel) items.get(this.val$selectedPos)).filePath);
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Instagram is not installed on your phone to perform repost", Toast.LENGTH_LONG).show();
                    }
                    FirebaseCrash.log("C04083 2");
                    break;
                case R.styleable.View_paddingEnd /*3*/:
                    DialogUtils.showDeleteDialog(Post_view.act, new C04071());
                    FirebaseCrash.log("C04083 3");
                    break;
            }
        }
    }

    /* renamed from: com.fragments.TabMyVideos.4 */
    class C04094 implements View.OnClickListener {
        private final /* synthetic */ Dialog val$d;
        private final /* synthetic */ ArrayList val$listSelected;

        C04094(ArrayList arrayList, Dialog dialog) {
            this.val$listSelected = arrayList;
            this.val$d = dialog;
        }

        public void onClick(View rippleView) {
            doCut = true;
            Intent itShare = new Intent("ACTION_LOCK_FROM_SHARE_ACTIVITY");
            itShare.putExtra("doCut", true);
            itShare.putExtra("type", "insta_vids");
            itShare.putStringArrayListExtra("android.intent.extra.STREAM", this.val$listSelected);
            try {
               startActivityForResult(itShare, 216);
            } catch (Exception e) {
                DialogUtils.showUpdateDialog(Post_view.act);
                FirebaseCrash.report(new Exception("C04094"));
            }
            this.val$d.dismiss();
        }
    }

    /* renamed from: com.fragments.TabMyVideos.5 */
    class C04105 implements View.OnClickListener {
        private final /* synthetic */ Dialog val$d;
        private final /* synthetic */ ArrayList val$listSelected;

        C04105(ArrayList arrayList, Dialog dialog) {
            this.val$listSelected = arrayList;
            this.val$d = dialog;
        }

        public void onClick(View rippleView) {
            doCut = false;
            Intent itShare = new Intent("ACTION_LOCK_FROM_SHARE_ACTIVITY");
            itShare.putExtra("doCut", false);
            itShare.putExtra("type", "insta_vids");
            itShare.putStringArrayListExtra("android.intent.extra.STREAM", this.val$listSelected);
            try {
               startActivityForResult(itShare, 216);
            } catch (Exception e) {
                DialogUtils.showUpdateDialog(Post_view.act);
                FirebaseCrash.report(new Exception("C04105"));
            }
            this.val$d.dismiss();
        }
    }

    /* renamed from: com.fragments.TabMyVideos.6 */
    class C04116 implements OnKeyListener {
        C04116() {
        }

        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() != 1 || keyCode != 4 || !isSelecting) {
                return false;
            }
            isSelecting = false;
            mAdapter.resetAdapter();
            return true;
        }
    }

    /* renamed from: com.fragments.TabMyVideos.7 */
    class C04127 implements DialogBtnClickListener {
        private final /* synthetic */ ArrayList val$listElements;

        C04127(ArrayList arrayList) {
            this.val$listElements = arrayList;
        }

        public void onPositiveClick() {
            new deleteFilesAsync(this.val$listElements).execute(new Void[0]);
        }
    }

    class deleteFilesAsync extends AsyncTask<Void, Integer, Void> {
        ArrayList<DownloadModel> listItems;
        ProgressDialog pd;

        public deleteFilesAsync(ArrayList<DownloadModel> listItems) {
            this.listItems = listItems;
        }

        protected void onPreExecute() {
            this.pd = new ProgressDialog(Post_view.act);
            this.pd.setTitle("Deleting");
            this.pd.setMessage("Deleting videos...");
            this.pd.setCancelable(false);
            this.pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.pd.show();
            this.pd.setMax(this.listItems.size());
            super.onPreExecute();
        }

        protected Void doInBackground(Void... arg0) {
            int value = 0;
            Iterator it = this.listItems.iterator();
            while (it.hasNext()) {
                DownloadModel model = (DownloadModel) it.next();
                File f = new File(model.filePath);
                if (f.delete()) {
                    items.remove(model);
                    Utils.scanDeletedMedia(mContext, f, "video/*");
                }
                value++;
                publishProgress(new Integer[]{Integer.valueOf(value)});
            }
            return null;
        }

        protected void onProgressUpdate(Integer... values) {
            this.pd.setProgress(values[0].intValue());
            super.onProgressUpdate(values);
        }

        protected void onPostExecute(Void result) {
            try {
                if (this.pd != null && this.pd.isShowing()) {
                    this.pd.dismiss();
                }
                isSelecting = false;
                checkListSize();
                mAdapter.removeItems(this.listItems);
                mAdapter.resetAdapter();
            } catch (Exception e) {
                FirebaseCrash.report(new Exception("r 1"));
            }
            super.onPostExecute(result);
        }
    }

    class deleteSingleFilesAsync extends AsyncTask<Void, Integer, Void> {
        ProgressDialog pd;
        int pos;

        public deleteSingleFilesAsync(int pos) {
            this.pos = pos;
        }

        protected void onPreExecute() {
            this.pd = new ProgressDialog(getActivity());
            this.pd.setMessage("deleting..");
            this.pd.setCancelable(false);
            this.pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.pd.show();
            this.pd.setMax(1);
            super.onPreExecute();
        }

        protected Void doInBackground(Void... arg0) {
            DownloadModel model = (DownloadModel) items.get(this.pos);
            if (new File(model.filePath).delete()) {
                Utils.scanDeletedMedia(getActivity(), new File(model.filePath), "video/*");
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            try {
                if (this.pd != null && this.pd.isShowing()) {
                    this.pd.dismiss();
                }
                if (mAdapter != null) {
                    mAdapter.removeItem(this.pos);
                }
                checkListSize();
            } catch (Exception e) {
                FirebaseCrash.report(new Exception("r 2"));
            }
            super.onPostExecute(result);
        }
    }

    public TabMyVideosPost() {
        this.items = new ArrayList();
    }

    public void onCreate(Bundle savedInstanceState) {
        tabVideos = this;
        setHasOptionsMenu(true);
        this.mContext = getActivity().getApplicationContext();
        one_play_preferences = getContext().getApplicationContext().getSharedPreferences("PROJECT_NAME", android.content.Context.MODE_PRIVATE);
        one_play_editor = one_play_preferences.edit();
        super.onCreate(savedInstanceState);
        FirebaseCrash.log("log 1");
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_myvideos, container, false);
        String name_video = one_play_preferences.getString("VIDEO_POST", "InstaFull/Video");
        this.tvLoading = (TextView) v.findViewById(R.id.tvLoading);
        this.tvLoading.setTypeface(Utils.tf);
        String mp3Path = Environment.getExternalStorageDirectory().toString() + "/" + name_video;
        this.recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        this.recyclerView.addItemDecoration(new SpacesItemDecoration(1));
        new C04062(mp3Path).execute(new Void[0]);
        return v;
    }

    private void checkListSize() {
        if (this.items.size() > 0) {
            this.tvLoading.setVisibility(View.GONE);
            return;
        }
        this.tvLoading.setText("هیچ ویدیویی وجود ندارد");
        this.tvLoading.setVisibility(View.VISIBLE);
    }

    public void onItemClick(int position) {
        this.selectedPos = position;
        String path = ((DownloadModel) this.items.get(position)).filePath;
        Intent itVideo = new Intent(getActivity(), CreatedVideoActivity.class);
        itVideo.putExtra("videoPath", path);
        itVideo.putExtra("index", position);
        startActivity(itVideo);
        FirebaseCrash.log("log 2");
    }

    public void onItemLongClick(int position) {
        this.selectedPos = position;
        showDialog(position);
        FirebaseCrash.log("log 3");
    }

    private void showDialog(int selectedPos) {
        CharSequence[] colors = new CharSequence[]{"جزئیات", "اشتراک گذاری", "پست مجدد", "حذف"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("امکانات");
        builder.setItems(colors, new C04083(selectedPos));
        builder.show();
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
        tv4.setText("choose option for your selected video(s)");
        d.setContentView(v2);
        ((TextView) v2.findViewById(R.id.tvDelete)).setText("MOVE");
        ((TextView) v2.findViewById(R.id.tvCancel)).setText("KEEP A COPY");
        v2.findViewById(R.id.rlDelete).setOnClickListener(new C04094(listSelected, d));
        v2.findViewById(R.id.rlCancel).setOnClickListener(new C04105(listSelected, d));
        d.show();
    }

    public void removeVideoFromList() {
        if (this.mAdapter != null) {
            this.mAdapter.removeItem(this.selectedPos);
        }
    }

    private void createInstagramIntent(String type, String mediaPath) throws Exception {
        Intent share = new Intent("android.intent.action.SEND");
        share.setType(type);
        Uri uri = Uri.fromFile(new File(mediaPath));
        share.setPackage("com.instagram.android");
        share.putExtra("android.intent.extra.STREAM", uri);
        startActivity(share);
        FirebaseCrash.log("log 5");
    }

    public void onResume() {
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new C04116());
        super.onResume();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 216 && resultCode == -1) {
            if (this.doCut) {
                ArrayList<DownloadModel> listSelected = this.mAdapter.getSelectedModel();
                if (listSelected.size() > 0) {
                    this.items.removeAll(listSelected);
                } else {
                    this.items.remove(this.selectedPos);
                }
            }
            this.isSelecting = false;
            this.mAdapter.resetAdapter();
            checkListSize();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
