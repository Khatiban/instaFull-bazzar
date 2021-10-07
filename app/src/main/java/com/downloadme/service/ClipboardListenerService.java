package com.downloadme.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipboardManager;
import android.content.ClipboardManager.OnPrimaryClipChangedListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fury.instafull.AdVideo;
import com.fury.instafull.BuildConfig;
import com.fury.instafull.Constants;
import com.fury.instafull.MyDatabaseHelper_url;
import com.fury.instafull.R;
import com.fury.instafull.Store;
import com.fury.instafull.StoryDownloader;
import com.fury.instafull.Ulits.Utils;
import com.downloadme.p060e.C1572b;
import com.google.firebase.crash.FirebaseCrash;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class ClipboardListenerService extends Service {
    private ClipboardManager f4995a;
    private C1572b f4996b;
    private String f4998d;
    private HashMap<Integer, Integer> f4999e;
    private HashMap<Integer, Object> f5000f;
    private HashMap<Integer, Integer> f5001g;
    private NotificationCompat f5002h;
    private Bitmap pb;
    String id_org;
    private NotificationCompat.Builder mBuilder, mBuilder1;
    private NotificationManager mNotifyManager, mNotifyManager1;
    SharedPreferences one_play_preferences;
    SharedPreferences.Editor one_play_editor;
    int coin;
    Boolean coin_alfa;
    MyDatabaseHelper_url key_url;
    String output1;
    ScheduledThreadPoolExecutor mDialogDaemon_time;

    //profile download new in instaPP
    class getStory extends AsyncTask<Void, Void, String> {

        String url;

        getStory(String s) {
            url = s;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {

            return Utils.getData(url);
        }

        @Override
        protected void onPostExecute(String jsonStr) {
            if (jsonStr != null) {

                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    String msg = jsonObject.getString("msg");

                    if (Objects.equals(msg, "done")) {

                        JSONArray item = jsonObject.getJSONArray("items");

                        for (int i = 0; i < item.length(); i++) {

                            JSONObject t = item.getJSONObject(i);
                            String tvp = t.getString("t");
                            String l = t.getString("l");

                            new StoryDownloader(getApplicationContext()).download(l, tvp, "_" + i + "_" +id_org);

                            int jj = item.length();
                            jj--;
                            if (jj == i) {
                                mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                mBuilder = new NotificationCompat.Builder(getApplicationContext());
                                mBuilder.setContentTitle("استوری دانلود شد!");
                                mBuilder.setSmallIcon(R.drawable.notif_tick);
                                mBuilder.setContentText("دانلود استوری" + " " + id_org + " " + "با موفقیت انجام شد");
                                final int id = (int) System.currentTimeMillis();
                                mNotifyManager.notify(id, mBuilder.build());
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "استوری وجود ندارد", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    FirebaseCrash.report(new Exception("bad as get jsonStr"));
                }

            } else {
                Toast.makeText(getApplicationContext(), "ارتباط با سرور برقرار نشد", Toast.LENGTH_LONG).show();
            }
        }
    }


    //profile download new in instaPP
    class getPb extends AsyncTask<String, Void, Bitmap> {
        getPb() {
        }

        protected Bitmap doInBackground(String... params) {
            Bitmap Pb = getBitmapFromURL(params[0]);
            return Pb;
        }

        protected void onPostExecute(Bitmap result) {
            pb = result;
            downloadprofile();
        }
    }

    //PP
    class getPbLink extends AsyncTask<String, Void, String> {
        getPbLink() {
        }

        protected void onPreExecute() {
        }

        protected String doInBackground(String... params) {
            String htmlString = null;
            try {
                htmlString = Jsoup.connect(params[0]).timeout(25000).get().toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return filterString(htmlString, "\"og:image\" content=\"", "jpg");
        }

        protected void onPostExecute(String result) {
            if (result == "abab") {
                Toast.makeText(getApplicationContext(), "لطفا مجددا تلاش کنید", Toast.LENGTH_SHORT).show();
                return;
            }
            new getPb().execute(new String[]{result});
        }
    }

    //PP
    public void downloadprofile() {
        if (pb == null) {
            Toast.makeText(getApplicationContext(), "عکس پروفایل پیدا نشد!", Toast.LENGTH_SHORT).show();
        } else {
            SaveImage(pb);
            mNotifyManager1 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mBuilder1 = new NotificationCompat.Builder(getApplicationContext());
            mBuilder1.setContentTitle("پروفایل دانلود شد!");
            mBuilder1.setSmallIcon(R.drawable.notif_tick);
            mBuilder1.setContentText("عکس پروفایل دانلود شد!");
            int id = (int) System.currentTimeMillis();
            mNotifyManager1.notify(id, mBuilder1.build());
        }
        FirebaseCrash.log("log 1");
    }

    //PP
    public static String getSubString(String mainString, String lastString, String startString) {
        String endString = BuildConfig.FLAVOR;
        int endIndex = mainString.indexOf(lastString);
        int startIndex = mainString.indexOf(startString);
        return mainString.substring(startIndex, endIndex);
    }

    //PP
    public static String filterString(String originalString, String startString, String endString) {
        if (originalString == null || originalString.indexOf(startString) == -1 || originalString.indexOf(startString) >= originalString.indexOf(endString)) {
            return "abab";
        }
        return (getSubString(originalString, endString, startString) + "jpg").replace("\"og:image\" content=\"", BuildConfig.FLAVOR).replace("/s150x150/", "/");
    }

    //PP
    public static Bitmap getBitmapFromURL(String src) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(src).openConnection();
            connection.setConnectTimeout(25000);
            connection.setDoInput(true);
            connection.connect();
            return BitmapFactory.decodeStream(connection.getInputStream());
        } catch (IOException e) {
            FirebaseCrash.report(new Exception("getBitmapFromURL"));
            return null;
        }
    }

    //PP
    private void SaveImage(Bitmap finalBitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        String name_profile = one_play_preferences.getString("PROFILE", "InstaFull/Profile");
        File myDir = new File(root + "/" + name_profile);
        myDir.mkdirs();
        String timeStamp = new SimpleDateFormat("ss_mm_HH_dd_MM_yyyy").format(new Date()) + ".jpg";
        File file = new File(myDir, timeStamp);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        mediaScanIntent.setData(Uri.fromFile(new File(root + "/" + name_profile + "/" + timeStamp)));
        sendBroadcast(mediaScanIntent);
        FirebaseCrash.log("log SaveImage");
    }

    /* renamed from: com.downloadme.service.ClipboardListenerService.1 */
    class C16431 implements OnPrimaryClipChangedListener {
        final /* synthetic */ ClipboardListenerService f4967a;

        C16431(ClipboardListenerService clipboardListenerService) {
            this.f4967a = clipboardListenerService;
        }

        public void onPrimaryClipChanged() {
            this.f4967a.f4998d = this.f4967a.f4996b.m7638a(this.f4967a.getApplicationContext());
            int a = this.f4967a.f4996b.m7636a(this.f4967a.f4998d);
            if (a == 0) {
                this.f4967a.m7899a(0);
            } else if (a == 1) {
                this.f4967a.m7899a(1);
            } else if (a == 2) {
                Toast.makeText(getApplicationContext(), "اینکه اینستاگرام نیست!", Toast.LENGTH_SHORT).show();
            }
            FirebaseCrash.log("log onPrimaryClipChanged");
        }
    }


    /* renamed from: com.downloadme.service.ClipboardListenerService.5 */
    class C16485 implements OnTouchListener {
        final /* synthetic */ WindowManager f4977a;
        final /* synthetic */ View f4978b;
        final /* synthetic */ ClipboardListenerService f4979c;

        C16485(ClipboardListenerService clipboardListenerService, WindowManager windowManager, View view) {
            this.f4979c = clipboardListenerService;
            this.f4977a = windowManager;
            this.f4978b = view;
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == 4) {
                this.f4977a.removeView(this.f4978b);
            }
            return true;
        }
    }

    /* renamed from: com.downloadme.service.ClipboardListenerService.6 */
    class C16496 implements OnTouchListener {
        final /* synthetic */ WindowManager f4980a;
        final /* synthetic */ View f4981b;
        final /* synthetic */ ClipboardListenerService f4982c;

        C16496(ClipboardListenerService clipboardListenerService, WindowManager windowManager, View view) {
            this.f4982c = clipboardListenerService;
            this.f4980a = windowManager;
            this.f4981b = view;
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == 4) {
                this.f4980a.removeView(this.f4981b);
            }
            return true;
        }
    }

    public ClipboardListenerService() {

        this.f4996b = C1572b.m7634a();
        this.f4999e = new HashMap();
        this.f5000f = new HashMap();
        this.f5001g = new HashMap();
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        super.onStartCommand(intent, i, i2);

        key_url = new MyDatabaseHelper_url(this);
        StringBuffer dadeha = new StringBuffer();
        Cursor res_2 = key_url.showAllData();
        while (res_2.moveToNext()) {
            dadeha.append(res_2.getString(1));
        }
        String b64key = dadeha.toString();
        String output = "";
        for (int o = 0; o <= b64key.length() - 8; o += 8) {
            int k = Integer.parseInt(b64key.substring(o, o + 8), 2);
            output += (char) k;
        }
        output1 = output;
        one_play_preferences = getApplicationContext().getSharedPreferences("PROJECT_NAME", android.content.Context.MODE_PRIVATE);
        one_play_editor = one_play_preferences.edit();

        coin_alfa = one_play_preferences.getBoolean("COIN_Alfa", false);
        coin = one_play_preferences.getInt("COIN", 0);


        this.f4995a = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        this.f4995a.addPrimaryClipChangedListener(new C16431(this));
        return 1;
    }

    private void m7899a(int i) {
        LayoutParams layoutParams2 = new WindowManager.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_TOAST, 262184, -3);
        final WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        if (i == 0) {
            return;
        }
        final View inflate3 = layoutInflater.inflate(R.layout.story_widget, null);
        inflate3.setOnTouchListener(new C16485(this, windowManager, inflate3));
        RelativeLayout s1 = (RelativeLayout) inflate3.findViewById(R.id.btnGetStory);
        s1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                m7902b(1);
                windowManager.removeView(inflate3);
            }
        });
        RelativeLayout s2 = (RelativeLayout) inflate3.findViewById(R.id.btnGetProfile);
        s2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                m7902b(2);
                windowManager.removeView(inflate3);
            }
        });
        windowManager.addView(inflate3, layoutParams2);
        FirebaseCrash.log("log m7899a");
    }

    private void clickm66() {

        Intent notificationIntent = new Intent(this, Store.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(268468224);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }

    }

    private void clickm66_video() {

        Intent notificationIntent = new Intent(this, AdVideo.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(268468224);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }

    }

    private void m66() {

        LayoutParams layoutParams33 = new WindowManager.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.TYPE_TOAST, 262184, -3);
        final WindowManager windowManager2 = (WindowManager) getSystemService(WINDOW_SERVICE);
        LayoutInflater layoutInflater2 = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View inflate33 = layoutInflater2.inflate(R.layout.dialog_universal_not_coin, null);
        inflate33.setOnTouchListener(new C16485(this, windowManager2, inflate33));
        TextView s11 = (TextView) inflate33.findViewById(R.id.dialog_universal_info_cancel_2);
        TextView s22 = (TextView) inflate33.findViewById(R.id.dialog_universal_info_video_2);
        TextView s33 = (TextView) inflate33.findViewById(R.id.dialog_universal_info_store_2);

        s11.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                windowManager2.removeView(inflate33);
            }
        });

        s22.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clickm66_video();
                windowManager2.removeView(inflate33);
            }
        });

        s33.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clickm66();
                windowManager2.removeView(inflate33);
            }
        });

        windowManager2.addView(inflate33, layoutParams33);
    }

    private void clickm68() {

        try {
            Intent notificationIntent = new Intent(Intent.ACTION_EDIT);
            notificationIntent.setData(Uri.parse("bazaar://details?id=" + "com.fury.instafull"));
            notificationIntent.setPackage("com.farsitel.bazaar");
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
            pendingIntent.send();
            coin = coin + 2000;
            one_play_editor.putInt("COIN", coin);
            one_play_editor.apply();
        } catch (Exception e) {
            FirebaseCrash.report(new Exception("star in bazzar"));
        }

    }

    private void m68() {

        LayoutParams layoutParams43 = new WindowManager.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.TYPE_TOAST, 262184, -3);
        final WindowManager windowManager4 = (WindowManager) getSystemService(WINDOW_SERVICE);
        LayoutInflater layoutInflater4 = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View inflate34 = layoutInflater4.inflate(R.layout.dialog_universal_coment, null);
        inflate34.setOnTouchListener(new C16485(this, windowManager4, inflate34));
        RelativeLayout s14 = (RelativeLayout) inflate34.findViewById(R.id.btnGetComment);

        s14.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clickm68();
                windowManager4.removeView(inflate34);
            }
        });

        windowManager4.addView(inflate34, layoutParams43);
    }

    private void m7902b(final int i) {
        Boolean start = one_play_preferences.getBoolean("TOAST_START", true);
        if (i == 1) {
            coin = one_play_preferences.getInt("COIN", 0);
            if (coin_alfa) {
                String name_video = one_play_preferences.getString("STORY_VIDEO", "InstaFull/Story/Video");
                String name_image = one_play_preferences.getString("STORY_IMAGE", "InstaFull/Story/Image");
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/" + name_video);
                if (!myDir.exists()) {
                    myDir.mkdirs();
                }
                File myDir2 = new File(root + "/" + name_image);
                if (!myDir2.exists()) {
                    myDir2.mkdirs();
                }
                if (start) {
                    Toast.makeText(getApplicationContext(), "دانلود استوری شروع شد", Toast.LENGTH_SHORT).show();
                }
                C03611();


            } else if (coin >= 1000) {

                coin = coin - 1000;
                one_play_editor.putInt("COIN", coin);
                one_play_editor.apply();
                if (coin <= 990) {
                    Boolean one_riz = one_play_preferences.getBoolean("Comment_Bazzar", true);
                    if (one_riz) {
                        one_play_editor.putBoolean("Comment_Bazzar", false);
                        one_play_editor.apply();
                        m68();
                    }
                }
                String name_video = one_play_preferences.getString("STORY_VIDEO", "InstaFull/Story/Video");
                String name_image = one_play_preferences.getString("STORY_IMAGE", "InstaFull/Story/Image");
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/" + name_video);
                if (!myDir.exists()) {
                    myDir.mkdirs();
                }
                File myDir2 = new File(root + "/" + name_image);
                if (!myDir2.exists()) {
                    myDir2.mkdirs();
                }

                if (start) {
                    Toast.makeText(getApplicationContext(), "دانلود استوری شروع شد", Toast.LENGTH_SHORT).show();
                }
                C03611();

            } else {
                m66();
            }

        } else if (i == 2) {
            coin = one_play_preferences.getInt("COIN", 0);
            if (coin_alfa) {
                if (start) {
                    Toast.makeText(getApplicationContext(), "دانلود پروفایل شروع شد", Toast.LENGTH_SHORT).show();
                }
                new getPbLink().execute(new String[]{f4998d});
            } else if (coin >= 1000) {
                coin = coin - 1000;
                one_play_editor.putInt("COIN", coin);
                one_play_editor.apply();
                if (coin <= 990) {
                    Boolean one_riz = one_play_preferences.getBoolean("Comment_Bazzar", true);
                    if (one_riz) {
                        one_play_editor.putBoolean("Comment_Bazzar", false);
                        one_play_editor.apply();
                        m68();
                    }
                }
                if (start) {
                    Toast.makeText(getApplicationContext(), "دانلود پروفایل شروع شد", Toast.LENGTH_SHORT).show();
                }
                new getPbLink().execute(new String[]{f4998d});
            } else {
                m66();
            }
        }
        FirebaseCrash.log("log m7902b");
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy() {
        super.onDestroy();
        sendBroadcast(new Intent("RestartSensor"));
    }

    public static String getStringFilter(String org, String start, String end) {
        String endst = "";
        int endindex = org.indexOf(end);
        int startindex = org.indexOf(start);
        endst = org.substring(startindex, endindex);
        return endst;
    }

    public static String getStringFilterEnd(String org, int start, String end) {
        String endst = "";
        int endindex = org.indexOf(end);
        endst = org.substring(start, endindex);
        return endst;
    }

    //insta story as iSave
    public void C03611() {
        String add_1 = f4998d + ")";
        String add2 = add_1.replace("/", "");
        String id_1 = getStringFilter(add2, "o", ")");
        String add_2 = id_1 + ")";
        String id_2 = getStringFilter(add_2, "m", ")");
        String add_3 = id_2 + ")";
        String id_3 = getStringFilterEnd(add_3, 1, ")");
        id_org = id_3;
        try {

            new getStory(output1 + id_org).execute();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "خطا در دانلود استوری!", Toast.LENGTH_SHORT).show();
            FirebaseCrash.report(new Exception("C03611 error download"));
        }

        FirebaseCrash.log("log C03611");
    }

}
