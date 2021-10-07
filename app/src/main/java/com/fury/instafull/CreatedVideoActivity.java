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

import com.fragments.TabMyVideosPost;
import com.google.firebase.crash.FirebaseCrash;
import com.instagram.data.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class CreatedVideoActivity extends AppCompatActivity implements OnSeekBarChangeListener {
    public static CreatedVideoActivity act;
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
            doCut = true;
            Intent itShare = new Intent("ACTION_LOCK_FROM_SHARE_ACTIVITY");
            itShare.putExtra("doCut", true);
            itShare.putExtra("type", "insta_vids");
            itShare.putStringArrayListExtra("android.intent.extra.STREAM", this.val$listSelected);
            try {
                CreatedVideoActivity.this.startActivityForResult(itShare, 216);
            } catch (Exception e) {
                DialogUtils.showUpdateDialog(CreatedVideoActivity.act);
                FirebaseCrash.report(new Exception("r 1"));
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
            CreatedVideoActivity.this.doCut = false;
            Intent itShare = new Intent("ACTION_LOCK_FROM_SHARE_ACTIVITY");
            itShare.putExtra("doCut", false);
            itShare.putExtra("type", "insta_vids");
            itShare.putStringArrayListExtra("android.intent.extra.STREAM", this.val$listSelected);
            try {
                CreatedVideoActivity.this.startActivityForResult(itShare, 216);
            } catch (Exception e) {
                DialogUtils.showUpdateDialog(CreatedVideoActivity.act);
                FirebaseCrash.report(new Exception("r 2"));
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
            if (isFirstPlay) {
                ivScreen.setVisibility(View.GONE);
                videoview.setVisibility(View.VISIBLE);
                isFirstPlay = false;
            }
            if (isPlay) {
                videoview.pause();
                handler.removeCallbacks(CreatedVideoActivity.this.seekrunnable);
                btnPlayVideo.setVisibility(View.VISIBLE);
            } else {
                videoview.seekTo(CreatedVideoActivity.this.seekVideo.getProgress());
                videoview.start();
                handler.postDelayed(CreatedVideoActivity.this.seekrunnable, 500);
                btnPlayVideo.setVisibility(View.GONE);
            }
            CreatedVideoActivity createdVideoActivity = CreatedVideoActivity.this;
            if (!CreatedVideoActivity.this.isPlay) {
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
            CreatedVideoActivity.this.handler.removeCallbacks(CreatedVideoActivity.this.runnable);
        }
    }

    /* renamed from: shirazwebdevelopers.ehsankarimi.instaautodownloader.CreatedVideoActivity.3 */
    class C15143 implements Runnable {
        C15143() {
        }

        public void run() {
            if (videoview.isPlaying()) {
                int curPos = videoview.getCurrentPosition();
                seekVideo.setProgress(curPos);
                try {
                    CreatedVideoActivity.this.tvStartVideo.setText(CreatedVideoActivity.formatTimeUnit((long) curPos));
                } catch (ParseException e) {
                    FirebaseCrash.report(new Exception("r 3"));
                }
                if (curPos == duration) {
                    seekVideo.setProgress(0);
                    tvStartVideo.setText("00:00");
                    handler.removeCallbacks(seekrunnable);
                    return;
                }
                handler.postDelayed(seekrunnable, 500);
                return;
            }
            seekVideo.setProgress(duration);
            try {
                tvStartVideo.setText(CreatedVideoActivity.formatTimeUnit((long) CreatedVideoActivity.this.duration));
            } catch (ParseException e2) {
                FirebaseCrash.report(new Exception("r 4"));
            }
            CreatedVideoActivity.this.handler.removeCallbacks(CreatedVideoActivity.this.seekrunnable);
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
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(videoPath));
                intent.setDataAndType(Uri.parse(videoPath), "video/" + format);
                startActivity(intent);
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
            Dialog d = new Dialog(CreatedVideoActivity.this, R.style.CustomDialogTheme);
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
            duration = videoview.getDuration();
            seekVideo.setMax(duration);
            tvStartVideo.setText("00:00");
            try {
                tvEndVideo.setText(CreatedVideoActivity.formatTimeUnit((long) duration));
            } catch (ParseException e) {
                FirebaseCrash.report(new Exception("r 5"));
            }
        }
    }

    /* renamed from: shirazwebdevelopers.ehsankarimi.instaautodownloader.CreatedVideoActivity.7 */
    class C15207 implements OnCompletionListener {
        C15207() {
        }

        public void onCompletion(MediaPlayer mp) {
            btnPlayVideo.setVisibility(View.VISIBLE);
            seekVideo.setProgress(0);
            tvStartVideo.setText("00:00");
            handler.removeCallbacks(seekrunnable);
            isPlay = false;
            ivScreen.setVisibility(View.VISIBLE);
            videoview.setVisibility(View.GONE);
            isFirstPlay = true;
        }
    }

    /* renamed from: shirazwebdevelopers.ehsankarimi.instaautodownloader.CreatedVideoActivity.8 */
    class C15218 implements OnClickListener {
        C15218() {
        }

        public void onClick(View arg0) {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(CreatedVideoActivity.this.videoPath));
            intent.setDataAndType(Uri.fromFile(new File(CreatedVideoActivity.this.videoPath)), "video/mp4");
            startActivity(intent);
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
            File f = new File(CreatedVideoActivity.this.videoPath);
            f.delete();
            Utils.scanDeletedMedia(CreatedVideoActivity.act, f, "video/*");
            if (TabMyVideosPost.tabVideos != null) {
                TabMyVideosPost.tabVideos.removeVideoFromList();
            }
            finish();
        }
    }

    public class LoadVideoThumbnail extends AsyncTask<String, Object, Bitmap> {
        protected Bitmap doInBackground(String... objectURL) {
            return ThumbnailUtils.createVideoThumbnail(videoPath, 1);
        }

        protected void onPostExecute(Bitmap result) {
            ivScreen.setImageBitmap(result);
        }
    }

    public CreatedVideoActivity() {
        videoPath = "";
        isPlay = false;
        duration = 0;
        handler = new Handler();
        isFirstPlay = true;
        onclickplayvideo = new C15121();
        runnable = new C15132();
        seekrunnable = new C15143();
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
        FirebaseCrash.log("log 2");
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
            FirebaseCrash.report(new Exception("r 6"));
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
            TabMyVideosPost.tabVideos.removeVideoFromList();
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
