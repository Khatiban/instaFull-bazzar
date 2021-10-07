package com.fury.instafull;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.ThumbnailUtils;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.fragments.TabMyVideosStory;
import com.google.firebase.crash.FirebaseCrash;
import com.instagram.data.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class CreatedVideoActivity_Story extends AppCompatActivity implements OnSeekBarChangeListener {
    public static CreatedVideoActivity_Story act;
    ImageButton btnPlayVideo;
    private boolean doCut;
    int duration;
    String fileName;
    RelativeLayout flVideoView;
    String format;
    Handler handler;
    int index;
    MenuItem infoItem;
    boolean isFirstPlay;
    boolean isPlay;
    ImageView ivScreen,back_page_video;
    OnClickListener onclickplayvideo;
    ProgressDialog pd;
    Runnable runnable;
    SeekBar seekVideo;
    Runnable seekrunnable;
    TextView tvEndVideo;
    TextView tvStartVideo;
    String videoPath;
    VideoView videoview;


    /* renamed from: shirazwebdevelopers.ehsankarimi.instaautodownloader.CreatedVideoActivity.11 */
    class AnonymousClass11 implements OnClickListener {
        private final /* synthetic */ Dialog val$d;
        private final /* synthetic */ ArrayList val$listSelected;

        AnonymousClass11(ArrayList arrayList, Dialog dialog) {
            this.val$listSelected = arrayList;
            this.val$d = dialog;
        }

        public void onClick(View rippleView) {
            CreatedVideoActivity_Story.this.doCut = true;
            Intent itShare = new Intent("ACTION_LOCK_FROM_SHARE_ACTIVITY");
            itShare.putExtra("doCut", true);
            itShare.putExtra("type", "insta_vids");
            itShare.putStringArrayListExtra("android.intent.extra.STREAM", this.val$listSelected);
            try {
                CreatedVideoActivity_Story.this.startActivityForResult(itShare, 216);
            } catch (Exception e) {
                DialogUtils.showUpdateDialog(CreatedVideoActivity_Story.act);
                FirebaseCrash.report(new Exception("r 3"));
            }
            this.val$d.dismiss();
        }
    }

    /* renamed from: shirazwebdevelopers.ehsankarimi.instaautodownloader.CreatedVideoActivity.12 */
    class AnonymousClass12 implements OnClickListener {
        private final /* synthetic */ Dialog val$d;
        private final /* synthetic */ ArrayList val$listSelected;

        AnonymousClass12(ArrayList arrayList, Dialog dialog) {
            this.val$listSelected = arrayList;
            this.val$d = dialog;
        }

        public void onClick(View rippleView) {
            CreatedVideoActivity_Story.this.doCut = false;
            Intent itShare = new Intent("ACTION_LOCK_FROM_SHARE_ACTIVITY");
            itShare.putExtra("doCut", false);
            itShare.putExtra("type", "insta_vids");
            itShare.putStringArrayListExtra("android.intent.extra.STREAM", this.val$listSelected);
            try {
                CreatedVideoActivity_Story.this.startActivityForResult(itShare, 216);
            } catch (Exception e) {
                DialogUtils.showUpdateDialog(CreatedVideoActivity_Story.act);
            }
            this.val$d.dismiss();
        }
    }

    /* renamed from: shirazwebdevelopers.ehsankarimi.instaautodownloader.CreatedVideoActivity.1 */
    class C15121 implements OnClickListener {
        C15121() {
        }

        public void onClick(View v) {
            boolean z = false;
            if (CreatedVideoActivity_Story.this.isFirstPlay) {
                CreatedVideoActivity_Story.this.ivScreen.setVisibility(View.GONE);
                CreatedVideoActivity_Story.this.videoview.setVisibility(View.VISIBLE);
                CreatedVideoActivity_Story.this.isFirstPlay = false;
            }
            if (CreatedVideoActivity_Story.this.isPlay) {
                CreatedVideoActivity_Story.this.videoview.pause();
                CreatedVideoActivity_Story.this.handler.removeCallbacks(CreatedVideoActivity_Story.this.seekrunnable);
                CreatedVideoActivity_Story.this.btnPlayVideo.setVisibility(View.VISIBLE);
            } else {
                CreatedVideoActivity_Story.this.videoview.seekTo(CreatedVideoActivity_Story.this.seekVideo.getProgress());
                CreatedVideoActivity_Story.this.videoview.start();
                CreatedVideoActivity_Story.this.handler.postDelayed(CreatedVideoActivity_Story.this.seekrunnable, 500);
                CreatedVideoActivity_Story.this.btnPlayVideo.setVisibility(View.GONE);
            }
            CreatedVideoActivity_Story createdVideoActivity = CreatedVideoActivity_Story.this;
            if (!CreatedVideoActivity_Story.this.isPlay) {
                z = true;
            }
            createdVideoActivity.isPlay = z;
        }
    }

    /* renamed from: shirazwebdevelopers.ehsankarimi.instaautodownloader.CreatedVideoActivity.2 */
    class C15132 implements Runnable {
        C15132() {
        }

        public void run() {
            CreatedVideoActivity_Story.this.handler.removeCallbacks(CreatedVideoActivity_Story.this.runnable);
        }
    }

    /* renamed from: shirazwebdevelopers.ehsankarimi.instaautodownloader.CreatedVideoActivity.3 */
    class C15143 implements Runnable {
        C15143() {
        }

        public void run() {
            if (CreatedVideoActivity_Story.this.videoview.isPlaying()) {
                int curPos = CreatedVideoActivity_Story.this.videoview.getCurrentPosition();
                CreatedVideoActivity_Story.this.seekVideo.setProgress(curPos);
                try {
                    CreatedVideoActivity_Story.this.tvStartVideo.setText(CreatedVideoActivity_Story.formatTimeUnit((long) curPos));
                } catch (ParseException e) {
                    FirebaseCrash.report(new Exception("r 2"));
                }
                if (curPos == CreatedVideoActivity_Story.this.duration) {
                    CreatedVideoActivity_Story.this.seekVideo.setProgress(0);
                    CreatedVideoActivity_Story.this.tvStartVideo.setText("00:00");
                    CreatedVideoActivity_Story.this.handler.removeCallbacks(CreatedVideoActivity_Story.this.seekrunnable);
                    return;
                }
                CreatedVideoActivity_Story.this.handler.postDelayed(CreatedVideoActivity_Story.this.seekrunnable, 500);
                return;
            }
            CreatedVideoActivity_Story.this.seekVideo.setProgress(CreatedVideoActivity_Story.this.duration);
            try {
                CreatedVideoActivity_Story.this.tvStartVideo.setText(CreatedVideoActivity_Story.formatTimeUnit((long) CreatedVideoActivity_Story.this.duration));
            } catch (ParseException e2) {
                FirebaseCrash.report(new Exception("r 1"));
            }
            CreatedVideoActivity_Story.this.handler.removeCallbacks(CreatedVideoActivity_Story.this.seekrunnable);
        }
    }

    /* renamed from: shirazwebdevelopers.ehsankarimi.instaautodownloader.CreatedVideoActivity.4 */


    /* renamed from: shirazwebdevelopers.ehsankarimi.instaautodownloader.CreatedVideoActivity.5 */
    class C15185 implements OnErrorListener {

        /* renamed from: shirazwebdevelopers.ehsankarimi.instaautodownloader.CreatedVideoActivity.5.1 */
        class C15161 implements OnClickListener {
            private final /* synthetic */ Dialog val$d;

            C15161(Dialog dialog) {
                this.val$d = dialog;
            }

            public void onClick(View rippleView) {
                this.val$d.dismiss();
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(CreatedVideoActivity_Story.this.videoPath));
                intent.setDataAndType(Uri.parse(CreatedVideoActivity_Story.this.videoPath), "video/" + CreatedVideoActivity_Story.this.format);
                CreatedVideoActivity_Story.this.startActivity(intent);
            }
        }

        /* renamed from: shirazwebdevelopers.ehsankarimi.instaautodownloader.CreatedVideoActivity.5.2 */
        class C15172 implements OnClickListener {
            private final /* synthetic */ Dialog val$d;

            C15172(Dialog dialog) {
                this.val$d = dialog;
            }

            public void onClick(View rippleView) {
                this.val$d.dismiss();
            }
        }

        C15185() {
        }

        public boolean onError(MediaPlayer mp, int what, int extra) {
            Toast.makeText(getApplicationContext(), "ویدیو متوقف شد!", Toast.LENGTH_SHORT).show();
            View v2 = getLayoutInflater().inflate(R.layout.player_dialog, null);
            TextView tvDialogText = (TextView) v2.findViewById(R.id.tv_dialogText);
            ((TextView) v2.findViewById(R.id.textView1)).setTypeface(Utils.tf);
            ((TextView) v2.findViewById(R.id.tvCancel)).setTypeface(Utils.tf);
            ((TextView) v2.findViewById(R.id.tvDelete)).setTypeface(Utils.tf);
            tvDialogText.setTypeface(Utils.tf);
            Dialog d = new Dialog(CreatedVideoActivity_Story.this, R.style.CustomDialogTheme);
            d.setContentView(v2);
            v2.findViewById(R.id.rlIntent).setOnClickListener(new C15161(d));
            v2.findViewById(R.id.rlCancel).setOnClickListener(new C15172(d));
            d.show();
            FirebaseCrash.report(new Exception("onError"));
            return true;
        }
    }

    /* renamed from: shirazwebdevelopers.ehsankarimi.instaautodownloader.CreatedVideoActivity.6 */
    class C15196 implements OnPreparedListener {
        C15196() {
        }

        public void onPrepared(MediaPlayer mp) {
            CreatedVideoActivity_Story.this.duration = CreatedVideoActivity_Story.this.videoview.getDuration();
            CreatedVideoActivity_Story.this.seekVideo.setMax(CreatedVideoActivity_Story.this.duration);
            CreatedVideoActivity_Story.this.tvStartVideo.setText("00:00");
            try {
                CreatedVideoActivity_Story.this.tvEndVideo.setText(CreatedVideoActivity_Story.formatTimeUnit((long) CreatedVideoActivity_Story.this.duration));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: shirazwebdevelopers.ehsankarimi.instaautodownloader.CreatedVideoActivity.7 */
    class C15207 implements OnCompletionListener {
        C15207() {
        }

        public void onCompletion(MediaPlayer mp) {
            CreatedVideoActivity_Story.this.btnPlayVideo.setVisibility(View.VISIBLE);
            CreatedVideoActivity_Story.this.seekVideo.setProgress(0);
            CreatedVideoActivity_Story.this.tvStartVideo.setText("00:00");
            CreatedVideoActivity_Story.this.handler.removeCallbacks(CreatedVideoActivity_Story.this.seekrunnable);
            CreatedVideoActivity_Story.this.isPlay = false;
            CreatedVideoActivity_Story.this.ivScreen.setVisibility(View.VISIBLE);
            CreatedVideoActivity_Story.this.videoview.setVisibility(View.GONE);
            CreatedVideoActivity_Story.this.isFirstPlay = true;
        }
    }

    /* renamed from: shirazwebdevelopers.ehsankarimi.instaautodownloader.CreatedVideoActivity.8 */
    class C15218 implements OnClickListener {
        C15218() {
        }

        public void onClick(View arg0) {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(CreatedVideoActivity_Story.this.videoPath));
            intent.setDataAndType(Uri.fromFile(new File(CreatedVideoActivity_Story.this.videoPath)), "video/mp4");
            CreatedVideoActivity_Story.this.startActivity(intent);
        }
    }

    /* renamed from: shirazwebdevelopers.ehsankarimi.instaautodownloader.CreatedVideoActivity.9 */
    class C15229 implements OnClickListener {
        private final /* synthetic */ Dialog val$d;

        C15229(Dialog dialog) {
            this.val$d = dialog;
        }

        public void onClick(View rippleView) {
            this.val$d.dismiss();
            File f = new File(CreatedVideoActivity_Story.this.videoPath);
            f.delete();
            Utils.scanDeletedMedia(CreatedVideoActivity_Story.act, f, "video/*");
            if (TabMyVideosStory.tabVideos != null) {
                TabMyVideosStory.tabVideos.removeVideoFromList();
            }
            CreatedVideoActivity_Story.this.finish();
        }
    }

    public class LoadVideoThumbnail extends AsyncTask<String, Object, Bitmap> {
        protected Bitmap doInBackground(String... objectURL) {
            return ThumbnailUtils.createVideoThumbnail(CreatedVideoActivity_Story.this.videoPath, 1);
        }

        protected void onPostExecute(Bitmap result) {
            CreatedVideoActivity_Story.this.ivScreen.setImageBitmap(result);
        }
    }

    public CreatedVideoActivity_Story() {
        this.videoPath = "";
        this.isPlay = false;
        this.duration = 0;
        this.handler = new Handler();
        this.isFirstPlay = true;
        this.onclickplayvideo = new C15121();
        this.runnable = new C15132();
        this.seekrunnable = new C15143();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.created_video_layout);
        act = this;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Utils.f100w = dm.widthPixels;
        Utils.f99h = dm.heightPixels;
        Intent intent = getIntent();
        this.videoPath = intent.getStringExtra("videoPath");
        this.index = intent.getIntExtra("index", 0);
        FindbyID();
        this.format = this.videoPath.substring(this.videoPath.lastIndexOf(".") + 1);
        this.isFirstPlay = true;
        new LoadVideoThumbnail().execute(new String[0]);
        this.videoview.setVideoPath(this.videoPath);
        this.seekVideo.setProgress(0);
        this.videoview.setOnErrorListener(new C15185());
        this.videoview.setOnPreparedListener(new C15196());
        this.videoview.setOnCompletionListener(new C15207());
        findViewById(R.id.btnPlayWith).setOnClickListener(new C15218());
        back_page_video.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        FirebaseCrash.log("log 1");
        System.gc();
    }

    private void FindbyID() {
        this.videoview = (VideoView) findViewById(R.id.vvScreen);
        this.ivScreen = (ImageView) findViewById(R.id.ivScreen);
        this.back_page_video = (ImageView) findViewById(R.id.back_page_video);
        this.seekVideo = (SeekBar) findViewById(R.id.sbVideo);
        this.seekVideo.setOnSeekBarChangeListener(this);
        this.tvStartVideo = (TextView) findViewById(R.id.tvStartVideo);
        this.tvEndVideo = (TextView) findViewById(R.id.tvEndVideo);
        this.flVideoView = (RelativeLayout) findViewById(R.id.flVideoView);
        this.flVideoView.setOnClickListener(this.onclickplayvideo);
        this.btnPlayVideo = (ImageButton) findViewById(R.id.btnPlayVideo);
        this.btnPlayVideo.setOnClickListener(this.onclickplayvideo);
        this.videoPath = getIntent().getStringExtra("videoPath");
        File f = new File(this.videoPath);
        if (f.exists()) {
            String name = f.getName();
            this.fileName = name;
            String ext = name.substring(name.lastIndexOf("."), name.length());
            if (name.length() > 15) {
                name = name.substring(0, 15) + ".." + ext;
            }
            this.fileName = this.fileName.substring(0, this.fileName.lastIndexOf("."));
        }

        FirebaseCrash.log("log 2 FindbyID");
    }

    @SuppressLint({"NewApi", "DefaultLocale"})
    @TargetApi(9)
    public static String formatTimeUnit(long millis) throws ParseException {
        return String.format("%02d:%02d", new Object[]{Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(millis)), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)))});
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = seekBar.getProgress();
        this.videoview.seekTo(progress);
        try {
            this.tvStartVideo.setText(formatTimeUnit((long) progress));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (this.isPlay) {
            this.videoview.start();
        }
    }

    public void onBackPressed() {
        if (this.videoview != null && this.videoview.isPlaying()) {
            this.videoview.pause();
        }
        super.onBackPressed();
    }

    @SuppressLint({"NewApi"})
    protected void onDestroy() {
        if (this.videoview.isActivated()) {
            this.videoview.suspend();
            this.videoview.destroyDrawingCache();
        }
        super.onDestroy();
    }

    private void showDeleteDialog() {
        final Dialog d = new Dialog(this, R.style.CustomDialogTheme);
        View v2 = getLayoutInflater().inflate(R.layout.delete_dialog, null);
        ((TextView) v2.findViewById(R.id.textView1)).setTypeface(Utils.tf);
        ((TextView) v2.findViewById(R.id.tv_dialogText)).setTypeface(Utils.tf);
        d.setContentView(v2);
        v2.findViewById(R.id.rlDelete).setOnClickListener(new C15229(d));
        v2.findViewById(R.id.rlCancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });
        d.show();
    }

    private void showShareDialog() {
        Intent sharingIntent = new Intent("android.intent.action.SEND");
        Uri screenshotUri = Uri.parse(this.videoPath);
        sharingIntent.setType("video/*");
        sharingIntent.putExtra("android.intent.extra.STREAM", screenshotUri);
        startActivity(Intent.createChooser(sharingIntent, "Share Video using"));
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
        tv2.setText("HIDE OPTIONS");
        TextView tv4 = (TextView) v2.findViewById(R.id.tv_dialogText);
        tv4.setText("choose option to hide this video");
        d.setContentView(v2);
        ((TextView) v2.findViewById(R.id.tvDelete)).setText("MOVE");
        ((TextView) v2.findViewById(R.id.tvCancel)).setText("KEEP A COPY");
        v2.findViewById(R.id.rlDelete).setOnClickListener(new AnonymousClass11(listSelected, d));
        v2.findViewById(R.id.rlCancel).setOnClickListener(new AnonymousClass12(listSelected, d));
        d.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 216 && resultCode == -1 && this.doCut) {
            TabMyVideosStory.tabVideos.removeVideoFromList();
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
