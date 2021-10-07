package com.fury.instafull;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.view.ViewPager.PageTransformer;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adapters.DownloadModel_Post;
import com.bumptech.glide.Glide;
import com.customphotoview.PhotoView;
import com.fragments.TabMyImagesPost;
import com.google.firebase.crash.FirebaseCrash;
import com.instagram.data.Utils;
import com.zoom.imageview.ZoomImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

@TargetApi(21)
public class ViewImageActivityPost extends AppCompatActivity implements OnPageChangeListener, OnClickListener {
    public static ViewImageActivityPost act;
    MyPagerAdapter adapter;
    FrameLayout btnSlide;
    private boolean doCut;
    int f128h;
    int index;
    int interval;
    boolean isShuufle;
    ArrayList<DownloadModel_Post> items;
    TimerTask mTask;
    Timer mTimer;
    Random random;
    RelativeLayout rlBottom;
    RelativeLayout tb;
    int totalSlides;
    PageTransformer transformerAlpha;
    ViewPager viewPager;
    TextView text_name;
    ImageView action_close,action_repost;
    int f129w;

    /* renamed from: shirazwebdevelopers.ehsankarimi.instaautodownloader.ViewImageActivity.1 */
    class C16141 implements PageTransformer {
        private float MinAlpha;

        C16141() {
            this.MinAlpha = 0.3f;
        }

        public void transformPage(View view, float position) {
            if (position > ZoomImageView.DEFAULT_MIN_SCALE) {
                view.setAlpha(0.0f);
                return;
            }
            view.setAlpha(this.MinAlpha + ((ZoomImageView.DEFAULT_MIN_SCALE - this.MinAlpha) * (ZoomImageView.DEFAULT_MIN_SCALE - Math.abs(position))));
        }
    }


    /* renamed from: shirazwebdevelopers.ehsankarimi.instaautodownloader.ViewImageActivity.7 */
    class C16217 implements OnClickListener {
        private final /* synthetic */ Dialog val$d;
        private final /* synthetic */ ArrayList val$listSelected;

        C16217(ArrayList arrayList, Dialog dialog) {
            this.val$listSelected = arrayList;
            this.val$d = dialog;
        }

        public void onClick(View rippleView) {
            doCut = true;
            Intent itShare = new Intent("ACTION_LOCK_FROM_SHARE_ACTIVITY");
            itShare.putExtra("doCut", true);
            itShare.putExtra("type", "insta_pics");
            itShare.putStringArrayListExtra("android.intent.extra.STREAM",val$listSelected);
            try {
                startActivityForResult(itShare, 215);
            } catch (Exception e) {
                DialogUtils.showUpdateDialog(ViewImageActivityPost.act);
                FirebaseCrash.report(new Exception("C16217"));
            }
            this.val$d.dismiss();
        }
    }

    /* renamed from: shirazwebdevelopers.ehsankarimi.instaautodownloader.ViewImageActivity.8 */
    class C16228 implements OnClickListener {
        private final /* synthetic */ Dialog val$d;
        private final /* synthetic */ ArrayList val$listSelected;

        C16228(ArrayList arrayList, Dialog dialog) {
            this.val$listSelected = arrayList;
            this.val$d = dialog;
        }

        public void onClick(View rippleView) {
            doCut = false;
            Intent itShare = new Intent("ACTION_LOCK_FROM_SHARE_ACTIVITY");
            itShare.putExtra("doCut", false);
            itShare.putExtra("type", "insta_pics");
            itShare.putStringArrayListExtra("android.intent.extra.STREAM",val$listSelected);
            try {
               startActivityForResult(itShare, 215);
            } catch (Exception e) {
                DialogUtils.showUpdateDialog(ViewImageActivityPost.act);
                FirebaseCrash.report(new Exception("C16228"));
            }
            this.val$d.dismiss();
        }
    }

    private class MyPagerAdapter extends PagerAdapter {
        public MyPagerAdapter(Context context) {
        }

        public void destroyItem(View view, int arg1, Object object) {
            ((ViewPager) view).removeView((ImageView) object);
        }

        public int getCount() {
            return items.size();
        }

        public Object instantiateItem(View view, int position) {
            String imgPath = ((DownloadModel_Post) items.get(position)).filePath;
            PhotoView iv = new PhotoView(ViewImageActivityPost.this);
            Glide.with(act).load(new File(imgPath)).override(f129w, f128h ).centerCrop().fitCenter().into(iv);
            ((ViewPager) view).addView(iv);
            return iv;
        }

        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public Parcelable saveState() {
            return null;
        }
    }

    public class deleteSingleFilesAsync extends AsyncTask<Void, Void, Void> {
        String model;
        ProgressDialog pd;

        protected void onPreExecute() {
            this.pd = new ProgressDialog(ViewImageActivityPost.this);
            this.pd.setMessage("deleting..");
            this.pd.setCancelable(false);
            this.pd.setProgressStyle(1);
            this.pd.show();
            super.onPreExecute();
        }

        public deleteSingleFilesAsync(String model) {
            this.model = model;
        }

        protected Void doInBackground(Void... arg0) {
            new File(this.model).delete();
            Utils.scanDeletedMedia(getApplicationContext(), new File(model), "image/*");
            return null;
        }

        protected void onPostExecute(Void result) {
            try {
                if (this.pd != null && this.pd.isShowing()) {
                    this.pd.dismiss();
                }
            } catch (Exception e) {
                FirebaseCrash.report(new Exception("onPostExecute"));
            }
            TabMyImagesPost.act.removeSingleImageFromViewer(index);
            items.remove(model);
            finish();
            super.onPostExecute(result);
        }
    }

    public ViewImageActivityPost() {
    }

    @TargetApi(21)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_pager);

        this.mTimer = new Timer();
        this.items = TabMyImagesPost.items;
        this.transformerAlpha = new C16141();
        act = this;
        tb = (RelativeLayout) findViewById(R.id.rltop);
        random = new Random();
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        f129w = outMetrics.widthPixels;
        f128h = outMetrics.heightPixels;
        findViewById(R.id.rlNext).setOnClickListener(this);
        findViewById(R.id.rlPrevious).setOnClickListener(this);
        findViewById(R.id.rlExport).setOnClickListener(this);
        findViewById(R.id.rlDelete2).setOnClickListener(this);
        action_close = (ImageView) findViewById(R.id.action_close);
        action_close.setOnClickListener(this);
        action_repost = (ImageView) findViewById(R.id.action_repost);
        action_repost.setOnClickListener(this);
        text_name = (TextView) findViewById(R.id.text_name);
        totalSlides = items.size();
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOnPageChangeListener(this);
        viewPager.setPageTransformer(true, this.transformerAlpha);
        adapter = new MyPagerAdapter(this);
        viewPager.setAdapter(this.adapter);
        index = getIntent().getIntExtra("position", 0);
        viewPager.setCurrentItem(this.index);
        rlBottom = (RelativeLayout) findViewById(R.id.rlBottom);
        try {
            CharSequence fileName = new File(((DownloadModel_Post) this.items.get(this.index)).filePath).getName();
            text_name.setText(fileName);
        } catch (Exception e2) {
            FirebaseCrash.report(new Exception("text_name"));
            text_name.setText("");;
        }
        if (getIntent().getBooleanExtra("play", false)) {
            onClick(this.btnSlide);
        }
        FirebaseCrash.log("log 1");
        System.gc();
    }

    public void onPageScrollStateChanged(int arg0) {
    }

    public void onPageScrolled(int arg0, float arg1, int arg2) {
        this.index = arg0;
        try {
            CharSequence fileName = new File(((DownloadModel_Post) items.get(index)).filePath).getName();
            text_name.setText(fileName);
        } catch (Exception e) {
            FirebaseCrash.report(new Exception("onPageScrolled"));
            text_name.setText("");
        }
    }

    public void onPageSelected(int arg0) {
    }

    public static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;
            while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.rlPrevious /*2131165358*/:
                if (this.isShuufle) {
                    this.index = this.random.nextInt(this.totalSlides);
                } else if (this.index > 0) {
                    this.index--;
                } else {
                    this.index = this.totalSlides - 1;
                }
                this.viewPager.setCurrentItem(this.index);
                break;
            case R.id.rlNext /*2131165361*/:
                if (this.isShuufle) {
                    this.index = this.random.nextInt(this.totalSlides);
                } else if (this.index < this.totalSlides - 1) {
                    this.index++;
                } else {
                    this.index = 0;
                }
                this.viewPager.setCurrentItem(this.index, true);
                break;
            case R.id.rlExport /*2131165362*/:
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("image/*");
                intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(((DownloadModel_Post) this.items.get(this.index)).filePath)));
                startActivity(Intent.createChooser(intent, "Share Image"));
                break;
            case R.id.rlDelete2 /*2131165363*/:
                final Dialog d = new Dialog(this, R.style.CustomDialogTheme);
                View v2 = getLayoutInflater().inflate(R.layout.delete_dialog, null);
                ((TextView) v2.findViewById(R.id.textView2)).setTypeface(Utils.tf);
                ((TextView) v2.findViewById(R.id.tv_dialogText)).setTypeface(Utils.tf);
                d.setContentView(v2);
                v2.findViewById(R.id.rlDelete).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View rippleView) {
                        new deleteSingleFilesAsync(((DownloadModel_Post) ViewImageActivityPost.this.items.get(ViewImageActivityPost.this.index)).filePath).execute();
                        d.dismiss();
                    }
                });
                v2.findViewById(R.id.rlCancel).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                    }
                });
                d.show();
                break;
            case R.id.action_close:
                finish();
                break;
            case R.id.action_repost /*2131165479*/:
                try {
                    createInstagramIntent("image/*", ((DownloadModel_Post) this.items.get(this.index)).filePath);
                    break;
                } catch (Exception e) {
                    Toast.makeText(this, "اول اینستاگرام نصب کن!", Toast.LENGTH_LONG).show();
                    break;
                }
        }
    }

    public void toggleFullView() {
        Animation var1 = AnimationUtils.loadAnimation(act, android.R.anim.fade_out);
        Animation var2 = AnimationUtils.loadAnimation(act, android.R.anim.fade_in);
        var1.setFillAfter(true);
        var2.setFillAfter(true);
        var1.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation var1) {
                rlBottom.setVisibility(View.GONE);
            }
            public void onAnimationRepeat(Animation var1) {
            }
            public void onAnimationStart(Animation var1) {
            }
        });
        var2.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation var1) {
                rlBottom.setVisibility(View.VISIBLE);
            }
            public void onAnimationRepeat(Animation var1) {
            }
            public void onAnimationStart(Animation var1) {
            }
        });
        if(this.rlBottom.getVisibility() == View.VISIBLE) {
            this.rlBottom.startAnimation(var1);
            this.tb.startAnimation(var1);
        } else {
            this.rlBottom.startAnimation(var2);
            this.tb.startAnimation(var2);
        }
    }



    private void createInstagramIntent(String type, String mediaPath) throws Exception {
        Intent share = new Intent("android.intent.action.SEND");
        share.setType(type);
        Uri uri = Uri.fromFile(new File(mediaPath));
        share.setPackage("com.instagram.android");
        share.putExtra("android.intent.extra.STREAM", uri);
        startActivity(share);
    }

    private void showHideDialog(ArrayList<String> listSelected) {
        Dialog d = new Dialog(act, R.style.CustomDialogTheme);
        View v2 = act.getLayoutInflater().inflate(R.layout.delete_dialog, null);
        TextView tv2 = (TextView) v2.findViewById(R.id.textView1);
        tv2.setTypeface(Utils.tf);
        tv2.setText("HIDE OPTIONS");
        TextView tv4 = (TextView) v2.findViewById(R.id.tv_dialogText);
        tv4.setTypeface(Utils.tf);
        tv4.setText("choose option for your selected picture(s)");
        d.setContentView(v2);
        ((TextView) v2.findViewById(R.id.tvDelete)).setText("MOVE");
        ((TextView) v2.findViewById(R.id.tvCancel)).setText("KEEP A COPY");
        v2.findViewById(R.id.rlDelete).setOnClickListener(new C16217(listSelected, d));
        v2.findViewById(R.id.rlCancel).setOnClickListener(new C16228(listSelected, d));
        d.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 215 && resultCode == RESULT_OK && this.doCut) {
            TabMyImagesPost.act.removeSingleImageFromViewer(this.index);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
