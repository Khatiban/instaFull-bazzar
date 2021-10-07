package com.fury.instafull;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat.Builder;
import android.widget.Toast;

import com.adapters.ImageModel;
import com.fury.instafull.Constants.ACTION;
import com.google.firebase.crash.FirebaseCrash;
import com.instagram.data.DownloadFileFromService;
import com.instagram.data.DownloadFileFromService.FileDownloadFromServiceListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ForegroundService extends Service {
    public static boolean IS_SERVICE_RUNNING;
    int fileType;
    String lastPath;
    BroadcastReceiver mReciever;
    Timer f115t;
    TimerTask tt;
    SharedPreferences one_play_preferences;
    SharedPreferences.Editor one_play_editor;

    /* renamed from: shirazwebdevelopers.ehsankarimi.instaautodownloader.ForegroundService.1 */
    class C15431 extends BroadcastReceiver {
        C15431() {
        }

        public void onReceive(Context context, Intent intent) {
            if (!intent.getAction().equals(ACTION.STARTFOREGROUND_ACTION) && intent.getAction().equals(ACTION.STOPFOREGROUND_ACTION)) {
                Editor edit1 = PreferenceManager.getDefaultSharedPreferences(ForegroundService.this.getApplicationContext()).edit();
                edit1.putBoolean("isEnabled", false);
                edit1.commit();
                Intent uiIntent2 = new Intent(Constants.ACTION_WIDGET_UPDATE_FROM_ACTIVITY);
                uiIntent2.putExtra("isEnabled", false);
                ForegroundService.this.sendBroadcast(uiIntent2);
                ForegroundService.this.doStopSelf();
            }
        }
    }

    /* renamed from: shirazwebdevelopers.ehsankarimi.instaautodownloader.ForegroundService.2 */
    class C15442 extends TimerTask {
        private final /* synthetic */ Editor val$edit;
        private final /* synthetic */ ClipboardManager val$myClipboard;
        private final /* synthetic */ SharedPreferences val$prefs;

        C15442(ClipboardManager clipboardManager, SharedPreferences sharedPreferences, Editor editor) {
            this.val$myClipboard = clipboardManager;
            this.val$prefs = sharedPreferences;
            this.val$edit = editor;
        }

        public void run() {
            ClipData cp = this.val$myClipboard.getPrimaryClip();
            if (cp != null) {
                try {
                    String curPath = cp.getItemAt(0).getText().toString();
                    ForegroundService.this.lastPath = this.val$prefs.getString("lastPath", "");
                    if (!ForegroundService.this.lastPath.equals(curPath)) {
                        this.val$edit.putString("lastPath", curPath);
                        this.val$edit.commit();
                        new fetchUrl().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{curPath});
                    }
                } catch (Exception e) {
                    FirebaseCrash.report(new Exception("error 1"));
                }
            }
        }
    }

    private class fetchUrl extends AsyncTask<String, Void, String> {
        String extension;
        String fileName;
        private int id;
        private Builder mBuilder;
        private NotificationManager mNotifyManager;

        /* renamed from: shirazwebdevelopers.ehsankarimi.instaautodownloader.ForegroundService.fetchUrl.1 */
        class C15451 implements FileDownloadFromServiceListener {
            C15451() {
            }

            public void onFileDownloadComplete(int NotifId) {
                Boolean End = one_play_preferences.getBoolean("TOAST_END", true);
                if (End){
                    Toast.makeText(ForegroundService.this.getApplicationContext(), new StringBuilder(String.valueOf(ForegroundService.this.fileType == 0 ? "عکس" : "ویدیو")).append(" ذخیره شد!").toString(), Toast.LENGTH_SHORT).show();
                }
                fetchUrl.this.mBuilder.setContentTitle("دانلود شد!");
                fetchUrl.this.mBuilder.setSmallIcon(R.drawable.notif_tick);
                fetchUrl.this.mBuilder.setContentText("دانلود با موفقیت انجام شد");
                fetchUrl.this.mBuilder.setProgress(0, 0, false);
                fetchUrl.this.mNotifyManager.notify(NotifId, fetchUrl.this.mBuilder.build());
            }

            public void onConnectionError(int NotifId) {
                Toast.makeText(ForegroundService.this.getApplicationContext(), "دسترسی به اینترنت قطع است!", Toast.LENGTH_SHORT).show();
                fetchUrl.this.mNotifyManager.cancel(NotifId);
            }

            public void onProgressUpdate(int NotifId, int progress) {
                fetchUrl.this.mBuilder.setProgress(100, progress, false);
                fetchUrl.this.mNotifyManager.notify(NotifId, fetchUrl.this.mBuilder.build());
            }
        }

        private fetchUrl() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {
            try {
                this.fileName = new File(params[0]).getName();
                Document document = Jsoup.connect(params[0]).get();
                String srcType = document.select("meta[property=og:type]").attr("content");
                Elements mElement = null;
                if (srcType.contains("photo")) {
                    mElement = document.select("meta[property=og:image]");
                } else if (srcType.contains("video")) {
                    mElement = document.select("meta[property=og:video]");
                }
                if (mElement != null) {
                    return mElement.attr("content");
                }
                return null;
            } catch (Exception e) {
                FirebaseCrash.report(new Exception("null error"));
                return null;
            }
        }

        protected void onPostExecute(String url) {

            if (url == null) {
                return;
            }
            if (url.length() < 10) {
                Toast.makeText(ForegroundService.this.getApplicationContext(), "صفحه کاربر private است!", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                this.extension = ForegroundService.this.getFileExt(new File(url).getName());
            } catch (Exception e) {
                this.extension = "jpg";
            }
            ArrayList<ImageModel> list = new ArrayList();
            try {
                String fileName = ForegroundService.this.getFileExt(new File(url).getName()).toLowerCase();
                if (fileName.equals("jpg") || fileName.equals("png") || fileName.equals("gif") || fileName.equals("jpeg")) {
                    ForegroundService.this.fileType = 0;
                } else {
                    ForegroundService.this.fileType = 1;
                }
                this.mNotifyManager = (NotificationManager) ForegroundService.this.getSystemService(NOTIFICATION_SERVICE);
                Intent notificationIntent = new Intent(ForegroundService.this.getApplicationContext(), MainActivity.class);
                notificationIntent.setAction(ACTION.MAIN_ACTION);
                notificationIntent.setFlags(268468224);
                PendingIntent pendingIntent = PendingIntent.getActivity(ForegroundService.this.getApplicationContext(), 0, notificationIntent, 0);
                this.mBuilder = new Builder(ForegroundService.this.getApplicationContext());
                this.mBuilder.setContentTitle("در حال دانلود ...").setContentIntent(pendingIntent).setContentText("در حال دریافت "+ (ForegroundService.this.fileType == 0 ? "عکس": "ویدیو")).setSmallIcon(R.drawable.download_icon);
                this.mBuilder.setProgress(100, 0, true);
                this.id = (int) System.currentTimeMillis();
                this.mNotifyManager.notify(this.id, this.mBuilder.build());
                list.add(new ImageModel(url, "", new File(url).getName(), ForegroundService.this.fileType));
                new DownloadFileFromService(ForegroundService.this.getApplicationContext(), this.fileName, this.extension, new C15451(), this.id).doDownloadFile(list);
            } catch (Exception e2) {
                Toast.makeText(ForegroundService.this.getApplicationContext(), "خطایی در دانلود رخ داده است!", Toast.LENGTH_SHORT).show();
                FirebaseCrash.report(new Exception("khata dar download 1"));
            }
        }
    }

    public ForegroundService() {
        this.fileType = 0;
        this.lastPath = "";
    }

    static {
        IS_SERVICE_RUNNING = false;
    }

    public void onCreate() {
        super.onCreate();

        one_play_preferences = getApplicationContext().getSharedPreferences("PROJECT_NAME", android.content.Context.MODE_PRIVATE);
        one_play_editor = one_play_preferences.edit();

        ClipboardManager myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Editor edit = prefs.edit();
        showNotification();
        this.mReciever = new C15431();
        IntentFilter filter = new IntentFilter(ACTION.STARTFOREGROUND_ACTION);
        filter.addAction(ACTION.STOPFOREGROUND_ACTION);
        registerReceiver(this.mReciever, filter);
        this.tt = new C15442(myClipboard, prefs, edit);
        this.f115t = new Timer();
        this.f115t.schedule(this.tt, 1000, 1000);


        FirebaseCrash.log("log 1");
        System.gc();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return 1;
    }

    private void showNotification() {

        Boolean show = one_play_preferences.getBoolean("NOTIFICATION_SHOW", true);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(ACTION.MAIN_ACTION);
        notificationIntent.setFlags(268468224);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        if (show){
            startForeground(R.styleable.AppCompatTheme_buttonStyleSmall, new Builder(this).setContentTitle("اینستافول فعال است").setTicker("وقت دانلوده!").setContentText("دانلود کن سریع و راحت!").setSmallIcon(R.drawable.notif_icon).setLargeIcon(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher), 60, 60, false)).setContentIntent(pendingIntent).setOngoing(true).addAction(R.drawable.stop, "اینستافول غیرفعال شد!", PendingIntent.getBroadcast(this, 0, new Intent(ACTION.STOPFOREGROUND_ACTION), 0)).build());
        }

        FirebaseCrash.log("log 2");
    }

    public void onDestroy() {
        super.onDestroy();
        try {
            this.f115t.cancel();
            this.tt.cancel();
        } catch (Exception e) {
            FirebaseCrash.report(new Exception("onDestroy"));
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    private void doStopSelf() {
        unregisterReceiver(this.mReciever);
        stopForeground(true);
        stopSelf();
    }

    public String getFileExt(String fileName) throws Exception {
        return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
    }
}
