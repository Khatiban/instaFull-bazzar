package com.fury.instafull;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fury.instafull.PageStart.PageStart;
import com.downloadme.service.ClipboardListenerService;
import com.farsitel.bazaar.IUpdateCheckService;
import com.google.firebase.crash.FirebaseCrash;
import com.instagram.data.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    TextView on_app, toast_on_or_off, CoinSingle, off_app;

    RelativeLayout back_on, open_instagram, rel_1, toast_view, back_off;

    ImageView home, profile, image, story, setting, instagram_FURY, Telegram_FURY;

    SharedPreferences one_play_preferences;
    SharedPreferences.Editor one_play_editor;

    Boolean on_or_off, auto_on, one_play, coin_play, coin_alfa,one_play_again;

    String coin;
    int anim_start;
    int coinint, outs;

    public static MainActivity act;
    File root, root1, root2, root3, root4, root5, root6, root7, root8, root9, root10, root11;

    int FreeN = 0;

    IUpdateCheckService service;
    UpdateServiceConnection connection;
    private static final String TAG = "UpdateCheck";
    String s64 = "01001101010010010100100001001110010011010100000100110000010001110100001101010011011100010100011101010011010010010110001000110011010001000101000101000101010000100100000101010001010101010100000101000001001101000100011100110111010000010100010001000011010000100111010001110111010010110100001001110010011101110100010001100010011011110011011101110000011100100100010101111010011010100011000101001101011100000110111101110000011001110111011001101001011101010111000100110000011001100111101000111001011010100101010001010100001010110100011100110000011011000101000101000100010100000111000101100101010011110101011000110001011101000110110101101100011001000100111100110110001100110110011101111001011101010110000101010101010010100110000101100111011100010110010001100010010101100110001100110010010101110101010001100011011010000110100001111001011101010010101101110001010100100111001101011001001100000110100001001111010001010100111101011000010101110100001001101110011110010100111101101010011011110101000001000011011000010100000101101101011100110111001101010010010110000110010001100100011101110110111001001100010001110100011101001110011110010101101000110001010001010011100101000011011001000111011101001001011011100101101001100110001110000111010001100100010100010111101001000001010101100110101001100010010011100110101001110000010001000101000100110011011001100100001000110000001110000101001101000001011001010110100001100100010000110100111001110110001011110101001001101000010011010101010101001001010011110111011001101110010011010011100001110111010010010110111100110111011011110100100101001000010101100110011001100010011100110100111100111001010000010101000101101010011000010011011101111001001101000111011100110011010010010110010101101000011100010010101101111000010110010011001001111010011100100110101001100110011101000110111001111000011110100100001001101100010100000100111101101110010001000111010001111010001101110100111100111001010001000011100000101011001100010100010101101111011000110100011000110010011001010011100000110111011011000101010100110010011001100110100001011010011010100110111001101000001101110111010000101111011000010101011101101101011011000110101001101100010011010100001001010101010000110100000101110111010001010100000101000001010100010011110100111101";

    ScheduledThreadPoolExecutor mDialogDaemon_time, mDialogDaemon_coin;


    class UpdateServiceConnection implements ServiceConnection {
        public void onServiceConnected(ComponentName name, IBinder boundService) {
            service = IUpdateCheckService.Stub
                    .asInterface((IBinder) boundService);
            try {
                long vCode = service.getVersionCode("com.fury.instafull");
                if (vCode == 1) {
                    (new DialogUpdate(MainActivity.this)).showDialog();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void onServiceDisconnected(ComponentName name) {
            service = null;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        one_play_preferences = getApplicationContext().getSharedPreferences("PROJECT_NAME", android.content.Context.MODE_PRIVATE);
        one_play_editor = one_play_preferences.edit();

        on_or_off = one_play_preferences.getBoolean("on_or_off_app", false);
        auto_on = one_play_preferences.getBoolean("AUTO_ON", false);
        one_play = one_play_preferences.getBoolean("one_play", false);
        coin_play = one_play_preferences.getBoolean("coin_play", false);
        coin_alfa = one_play_preferences.getBoolean("COIN_Alfa", false);
        one_play_again = one_play_preferences.getBoolean("one_play_again", false);
        coinint = one_play_preferences.getInt("COIN", 0);
        coin = String.valueOf(coinint);
        if (auto_on) {

            one_play_editor.putBoolean("on_or_off_app", true);
            one_play_editor.apply();

            Intent startIntent = new Intent(MainActivity.this, ForegroundService.class);
            startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
            startService(startIntent);
            startService(new Intent(getApplicationContext(), ClipboardListenerService.class));
            openApplication("com.instagram.android");
            finish();

        }

        if (coin_play) {

            (new DialogCoinStart(MainActivity.this)).showDialog();

            one_play_editor.putBoolean("coin_play", false);
            one_play_editor.apply();
        }

        if (!one_play) {

            one_play_editor.putString("S64", s64);
            one_play_editor.putBoolean("one_play_again", true);
            one_play_editor.putBoolean("one_play", true);
            one_play_editor.apply();

            Intent start = new Intent(this, PageStart.class);
            startActivity(start);
            overridePendingTransition(0, 0);
            finish();

        }

        if (one_play_again){

            root2 = new File(Environment.getExternalStorageDirectory(), "InstaFull/Log/log_app_instafull");
            root3 = new File(Environment.getExternalStorageDirectory(), "InstaFull/Log");
            root4 = new File(Environment.getExternalStorageDirectory(), "Android/data/com.android.fury.instafull/log_app_instafull");
            root5 = new File(Environment.getExternalStorageDirectory(), "Android/data/com.android.fury.instafull");
            root6 = new File(Environment.getExternalStorageDirectory(), "InstaFull/Log/a000");
            root7 = new File(Environment.getExternalStorageDirectory(), "InstaFull/Log/b000");
            root8 = new File(Environment.getExternalStorageDirectory(), "InstaFull/Log/c000");
            root9 = new File(Environment.getExternalStorageDirectory(), "InstaFull/Log/d000");
            root10 = new File(Environment.getExternalStorageDirectory(), "InstaFull/Log/ab000");
            root11 = new File(Environment.getExternalStorageDirectory(), "InstaFull/Log/c0000");

            if (!root4.exists() && !root5.exists() && !root3.exists() && !root2.exists()) {
                FreeN = 1;
            }


            if (!root2.exists()) {
                if (!root3.exists()) {
                    generateNoteOnSD(getApplicationContext(), "log_app_instafull", "junhn s;i 0");
                } else {
                    ///0
                }
            } else {
                ///0
            }

            if (!root4.exists()) {
                if (!root5.exists()) {
                    generateNoteOnSD2(getApplicationContext(), "log_app_instafull", "junhn s;i 0");
                } else {
                    ///0
                }
            } else {
                ///0
            }

            if (root11.exists()) {
                FreeN = 2;
                root11.delete();
            } else if (root10.exists()) {
                FreeN = 3;
                root10.delete();
            } else if (root9.exists()) {
                FreeN = 4;
                root9.delete();
            } else if (root8.exists()) {
                FreeN = 5;
                root8.delete();
            } else if (root7.exists()) {
                FreeN = 6;
                root7.delete();
            } else if (root6.exists()) {
                FreeN = 7;
                root6.delete();
            }
        }

        setContentView(R.layout.activity_main);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Utils.f100w = dm.widthPixels;
        Utils.f99h = dm.heightPixels;

        initService();

        on_app = (TextView) findViewById(R.id.on_app_insta);
        off_app = (TextView) findViewById(R.id.off_app_insta);
        CoinSingle = (TextView) findViewById(R.id.CoinSingle);
        toast_on_or_off = (TextView) findViewById(R.id.toast_on_or_off);
        back_on = (RelativeLayout) findViewById(R.id.back_on);
        back_off = (RelativeLayout) findViewById(R.id.back_off);
        open_instagram = (RelativeLayout) findViewById(R.id.open_instagram);
        toast_view = (RelativeLayout) findViewById(R.id.toast_view);
        rel_1 = (RelativeLayout) findViewById(R.id.rel_1);
        home = (ImageView) findViewById(R.id.home_page);
        profile = (ImageView) findViewById(R.id.profile);
        image = (ImageView) findViewById(R.id.image_page);
        story = (ImageView) findViewById(R.id.story);
        setting = (ImageView) findViewById(R.id.setting);
        instagram_FURY = (ImageView) findViewById(R.id.instagram_FURY);
        Telegram_FURY = (ImageView) findViewById(R.id.Telegram_FURY);

        int min = 1;
        int max = 3;
        Random r = new Random();
        int khodkar_1 = r.nextInt(max - min + 1) + min;
        if (khodkar_1 == 1){
            open_instagram.setBackgroundResource(R.drawable.instagram_back_1);
        }else if (khodkar_1 == 2){
            open_instagram.setBackgroundResource(R.drawable.instagram_back_1);
        }else if (khodkar_1 == 3){
            open_instagram.setBackgroundResource(R.drawable.instagram);
        }

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/A-Chamran.ttf");

        on_app.setTypeface(face);
        toast_on_or_off.setTypeface(face);
        off_app.setTypeface(face);

        if (coin_alfa) {
            CoinSingle.setText("بی نهایت");
        } else {

            if (mDialogDaemon_time != null) {
                try {
                    mDialogDaemon_time.shutdown();
                    mDialogDaemon_time = null;
                } catch (Exception e) {
                    FirebaseCrash.report(new Exception("12"));
                }
            }
            try {
                mDialogDaemon_time = new ScheduledThreadPoolExecutor(1);
            } catch (Exception e) {
                FirebaseCrash.report(new Exception("13"));
            }
            try {
                mDialogDaemon_time.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                try{
                                    coinint = one_play_preferences.getInt("COIN", 0);
                                    coin = String.valueOf(coinint);
                                    CoinSingle.setText(coin);
                                }catch (Exception e){
                                    mDialogDaemon_time.shutdown();
                                    mDialogDaemon_time = null;
                                    FirebaseCrash.report(new Exception("14"));
                                }

                            }
                        });
                    }
                }, 0L, 2000L, TimeUnit.MILLISECONDS);

            } catch (Exception e) {
                mDialogDaemon_time.shutdown();
                mDialogDaemon_time = null;
                FirebaseCrash.report(new Exception("14"));
            }

        }

        if (FreeN == 1) {
            anim_coin(2000);
            one_play_editor.putInt("COIN", 2000);
            one_play_editor.apply();
        }

        if (FreeN == 2) {
            one_play_editor.putBoolean("COIN_Alfa", true);
            one_play_editor.putInt("COIN", 5000000);
            one_play_editor.apply();
        } else if (FreeN == 3) {
            anim_coin(5000000);
            one_play_editor.putInt("COIN", 5000000);
            one_play_editor.apply();
        } else if (FreeN == 4) {
            anim_coin(2000000);
            one_play_editor.putInt("COIN", 2000000);
            one_play_editor.apply();
        } else if (FreeN == 5) {
            anim_coin(500000);
            one_play_editor.putInt("COIN", 500000);
            one_play_editor.apply();
        } else if (FreeN == 6) {
            anim_coin(200000);
            one_play_editor.putInt("COIN", 200000);
            one_play_editor.apply();
        } else if (FreeN == 7) {
            anim_coin(20000);
            one_play_editor.putInt("COIN", 20000);
            one_play_editor.apply();
        }

        if (Build.VERSION.SDK_INT >= 22) {
            new Handler().postDelayed(new Thread() {
                @Override
                public void run() {
                    super.run();
                    checkPermissions();
                }
            }, 1000);
        }


        home.setImageResource(R.drawable.dock_home_whiteout);

        open_instagram.setVisibility(View.INVISIBLE);

        if (on_or_off) {
            if (app_net.getInstance(this).isOnline()) {

                back_off.setVisibility(View.GONE);
                back_on.setVisibility(View.VISIBLE);
                Intent startIntent = new Intent(MainActivity.this, ForegroundService.class);
                startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                startService(startIntent);
                startService(new Intent(getApplicationContext(), ClipboardListenerService.class));
                card_start();
                toast_on_or_off.setBackgroundResource(R.color.color_toast_green);
                toast_on_or_off.setText("اینستافول فعال است!");

                if (anim_start == 0) {
                    animtoast();
                } else {
                    new Handler().postDelayed(new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            animtoast();
                        }
                    }, 2000);
                }
            } else {

                back_on.setVisibility(View.GONE);
                back_off.setVisibility(View.VISIBLE);
                sendBroadcast(new Intent(Constants.ACTION.STOPFOREGROUND_ACTION));
                stopService(new Intent(getApplicationContext(), ClipboardListenerService.class));
                card_end();

            }
        }

        back_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                one_play_editor.putBoolean("on_or_off_app", false);
                one_play_editor.apply();

                back_on.setVisibility(View.GONE);
                back_off.setVisibility(View.VISIBLE);
                sendBroadcast(new Intent(Constants.ACTION.STOPFOREGROUND_ACTION));
                stopService(new Intent(getApplicationContext(), ClipboardListenerService.class));
                card_end();


                toast_on_or_off.setBackgroundResource(R.color.color_toast_red);
                toast_on_or_off.setText("اینستافول غیرفعال شد!");

                if (anim_start == 0) {
                    animtoast();
                } else {
                    new Handler().postDelayed(new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            animtoast();
                        }
                    }, 2000);
                }

            }
        });

        back_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (app_net.getInstance(MainActivity.this).isOnline()) {
                    one_play_editor.putBoolean("on_or_off_app", true);
                    one_play_editor.apply();

                    back_off.setVisibility(View.GONE);
                    back_on.setVisibility(View.VISIBLE);
                    Intent startIntent = new Intent(MainActivity.this, ForegroundService.class);
                    startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                    startService(startIntent);
                    startService(new Intent(getApplicationContext(), ClipboardListenerService.class));
                    card_start();


                    toast_on_or_off.setBackgroundResource(R.color.color_toast_green);
                    toast_on_or_off.setText("اینستافول فعال شد!");

                    if (anim_start == 0) {
                        animtoast();
                    } else {
                        new Handler().postDelayed(new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                animtoast();
                            }
                        }, 2000);
                    }

                } else {

                    toast_on_or_off.setBackgroundResource(R.color.color_toast_blue);
                    toast_on_or_off.setText("دسترسی به اینترنت قطع است!");
                    if (anim_start == 0) {
                        animtoast();
                    } else {
                        new Handler().postDelayed(new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                animtoast();
                            }
                        }, 2000);
                    }

                }
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent profile = new Intent(MainActivity.this, Profile_page.class);
                startActivity(profile);
                overridePendingTransition(0, 0);

            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent post = new Intent(MainActivity.this, Post_view.class);
                startActivity(post);
                overridePendingTransition(0, 0);

            }
        });

        story.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent post = new Intent(MainActivity.this, Story_view.class);
                startActivity(post);
                overridePendingTransition(0, 0);

            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent setting = new Intent(MainActivity.this, Setting.class);
                startActivity(setting);
                overridePendingTransition(0, 0);

            }
        });


        instagram_FURY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://instagram.com/_u/fury_studio_ir");
                Intent inestagram = new Intent(Intent.ACTION_VIEW, uri);
                inestagram.setPackage("com.instagram.android");
                try {
                    startActivity(inestagram);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/fury_studio_ir")));
                }
            }
        });

        Telegram_FURY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://t.me/Fury_studio");
                Intent inestagram = new Intent(Intent.ACTION_VIEW, uri);
                inestagram.setPackage("org.telegram.messenger");
                try {
                    startActivity(inestagram);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://t.me/Fury_studio")));
                }
            }
        });

        open_instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openApplication("com.instagram.android");
            }
        });


        FirebaseCrash.log("log 1");
        System.gc();

    }

    private void initService() {
        connection = new UpdateServiceConnection();
        Intent i = new Intent(
                "com.farsitel.bazaar.service.UpdateCheckService.BIND");
        i.setPackage("com.farsitel.bazaar");
        boolean ret = bindService(i, connection, Context.BIND_AUTO_CREATE);
    }

    public void card_start() {

        try {
            open_instagram.setVisibility(View.VISIBLE);
            open_instagram.setAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.in));
        } catch (Exception e) {
            FirebaseCrash.report(new Exception("card_start"));
        }
    }

    public void card_end() {

        try {
            open_instagram.setAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.out));
            open_instagram.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
            FirebaseCrash.report(new Exception("card_start"));
        }
    }

    public void openApplication(String packageN) {
        Intent i = getPackageManager().getLaunchIntentForPackage(packageN);
        if (i != null) {
            startActivity(i);
            return;
        }
        try {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + packageN)));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + packageN)));
            FirebaseCrash.report(new Exception("open instagram go to google play"));
        }
    }


    //other


    public void addImageToList(String filePath) {
        //null
    }

    public void generateNoteOnSD(Context context, String sFileName, String sBody) {
        try {

            root = new File(Environment.getExternalStorageDirectory(), "InstaFull/Log");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            FirebaseCrash.report(new Exception("save log name : generateNoteOnSD"));
        }
    }

    public void generateNoteOnSD2(Context context, String sFileName, String sBody) {
        try {

            root1 = new File(Environment.getExternalStorageDirectory(), "Android/data/com.android.fury.instafull");
            if (!root1.exists()) {
                root1.mkdirs();
            }
            File gpxfile = new File(root1, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            FirebaseCrash.report(new Exception("save log name : generateNoteOnSD2"));
        }
    }

    public void animtoast() {
        anim_start = 1;
        toast_on_or_off.setVisibility(View.VISIBLE);
        toast_on_or_off.setAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.in_toast));
        new Handler().postDelayed(new Thread() {

            @Override
            public void run() {
                super.run();
                anim_start = 0;
                toast_on_or_off.setAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.out_toast));
                toast_on_or_off.setVisibility(View.INVISIBLE);
            }
        }, 3000);

        FirebaseCrash.log("log animtoast");
    }


    public void anim_coin(final int a) {
        int b;

        if (a >= 20000 && a <= 200000) {
            b = 10;
        } else if (a >= 200000 && a <= 500000) {
            b = 80;
        } else if (a >= 500000 && a <= 2000000) {
            b = 280;
        } else if (a >= 2000000 && a <= 5000000) {
            b = 2860;
        } else if (a >= 5000000) {
            b = 5860;
        } else {
            b = 1;
        }
        outs = 1;

        if (mDialogDaemon_coin != null) {
            try {
                mDialogDaemon_coin.shutdown();
                mDialogDaemon_coin = null;
            } catch (Exception e) {
                FirebaseCrash.report(new Exception("12"));
            }
        }
        try {
            mDialogDaemon_coin = new ScheduledThreadPoolExecutor(1);
        } catch (Exception e) {
            FirebaseCrash.report(new Exception("13"));
        }
        try {
            final int finalB = b;
            mDialogDaemon_coin.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                if (outs < a) {
                                    outs = outs + finalB;
                                    final int finalS = outs;
                                    String n = String.valueOf(finalS);
                                    CoinSingle.setText(n);
                                } else {
                                    mDialogDaemon_coin.shutdown();
                                    mDialogDaemon_coin = null;
                                }
                            } catch (Exception e) {
                                String n = String.valueOf(a);
                                CoinSingle.setText(n);
                            }

                        }
                    });
                }
            }, 0L, 1L, TimeUnit.MILLISECONDS);

        } catch (Exception e) {
            String n = String.valueOf(a);
            CoinSingle.setText(n);
            FirebaseCrash.report(new Exception("14"));
        }
    }


    private void checkPermissions(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            new PermissionHandler().checkPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE, new PermissionHandler.OnPermissionResponse() {
                @Override
                public void onPermissionGranted() {
                    // permission granted
                    // your code
                }

                @Override
                public void onPermissionDenied() {
                    // User canceled permission
                    Toast.makeText(MainActivity.this,"در صورت نپذیرفتن درخواست ها برنامه با مشکل مواجه می شود!", Toast.LENGTH_LONG).show();
                }
            });
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            new PermissionHandler().checkPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionHandler.OnPermissionResponse() {
                @Override
                public void onPermissionGranted() {
                    // permission granted
                    // your code
                }

                @Override
                public void onPermissionDenied() {
                    // User canceled permission
                    Toast.makeText(MainActivity.this,"در صورت نپذیرفتن درخواست ها برنامه با مشکل مواجه می شود!", Toast.LENGTH_LONG).show();
                }
            });
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            new PermissionHandler().checkPermission(this, android.Manifest.permission.READ_PHONE_STATE, new PermissionHandler.OnPermissionResponse() {
                @Override
                public void onPermissionGranted() {
                    // permission granted
                    // your code
                }

                @Override
                public void onPermissionDenied() {
                    // User canceled permission
                    Toast.makeText(MainActivity.this,"در صورت نپذیرفتن درخواست ها برنامه با مشکل مواجه می شود!", Toast.LENGTH_LONG).show();
                }
            });
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            new PermissionHandler().checkPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION, new PermissionHandler.OnPermissionResponse() {
                @Override
                public void onPermissionGranted() {
                    // permission granted
                    // your code
                }

                @Override
                public void onPermissionDenied() {
                    // User canceled permission
                    Toast.makeText(MainActivity.this,"در صورت نپذیرفتن درخواست ها برنامه با مشکل مواجه می شود!", Toast.LENGTH_LONG).show();
                }
            });
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Intent intent = new Intent("PERMISSION_RECEIVER");
        intent.putExtra("requestCode",requestCode);
        intent.putExtra("permissions",permissions);
        intent.putExtra("grantResults",grantResults);
        sendBroadcast(intent);
    }

}






