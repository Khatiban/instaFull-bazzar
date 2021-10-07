package com.fury.instafull;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fury.instafull.Suport.Suport_me;
import com.google.firebase.crash.FirebaseCrash;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Setting extends AppCompatActivity {

    ImageView home, profile, image, story, setting, go_shop;

    TextView about_me, CoinSingle, support, name_folder_image, name_folder_image_name, name_folder_video, name_folder_video_name, name_folder_profile, name_folder_profile_name, name_folder_story_image, name_folder_story_image_name, name_folder_story_video, name_folder_story_video_name;

    SharedPreferences one_play_preferences;
    SharedPreferences.Editor one_play_editor;

    Boolean CHECKED_STATE_3, CHECKED_STATE_2, CHECKED_STATE_1, CHECKED_STATE_4, coin_alfa;
    SwitchCompat auto_on, notification_show, Toast_end, Toast_start;

    String coinint;
    String name_image;
    String name_image_full;
    String name_video;
    String name_video_full;
    String name_profile;
    String name_profile_full;
    String name_story_image;
    String name_story_image_full;
    String name_story_video;
    String name_story_video_full;

    ScheduledThreadPoolExecutor mDialogDaemon_time;

    int coin;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_setting);

        one_play_preferences = getApplicationContext().getSharedPreferences("PROJECT_NAME", android.content.Context.MODE_PRIVATE);
        one_play_editor = one_play_preferences.edit();


        home = (ImageView) findViewById(R.id.home_page);
        profile = (ImageView) findViewById(R.id.profile);
        go_shop = (ImageView) findViewById(R.id.go_shop);
        image = (ImageView) findViewById(R.id.image_page);
        story = (ImageView) findViewById(R.id.story);
        setting = (ImageView) findViewById(R.id.setting);
        auto_on = (SwitchCompat) findViewById(R.id.auto_on);
        notification_show = (SwitchCompat) findViewById(R.id.notificaton_bar);
        Toast_end = (SwitchCompat) findViewById(R.id.Toast_end);
        Toast_start = (SwitchCompat) findViewById(R.id.Toast_start);
        name_folder_image = (TextView) findViewById(R.id.name_folder_image);
        name_folder_image_name = (TextView) findViewById(R.id.name_folder_image_name);
        name_folder_video = (TextView) findViewById(R.id.name_folder_video);
        name_folder_video_name = (TextView) findViewById(R.id.name_folder_video_name);
        name_folder_profile = (TextView) findViewById(R.id.name_folder_porfile);
        name_folder_profile_name = (TextView) findViewById(R.id.name_folder_porfile_name);
        name_folder_story_image = (TextView) findViewById(R.id.name_folder_story_pic);
        name_folder_story_image_name = (TextView) findViewById(R.id.name_folder_story_pic_name);
        name_folder_story_video = (TextView) findViewById(R.id.name_folder_story_video);
        name_folder_story_video_name = (TextView) findViewById(R.id.name_folder_story_video_name);
        about_me = (TextView) findViewById(R.id.about_me);
        support = (TextView) findViewById(R.id.support);
        CoinSingle = (TextView) findViewById(R.id.CoinSingle);

        setting.setImageResource(R.drawable.bar_button_settings);

        coin = one_play_preferences.getInt("COIN", 0);
        coinint = String.valueOf(coin);
        coin_alfa = one_play_preferences.getBoolean("COIN_Alfa", false);

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
                                try {
                                    coin = one_play_preferences.getInt("COIN", 0);
                                    coinint = String.valueOf(coin);
                                    CoinSingle.setText(coinint);
                                } catch (Exception e) {
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


        CHECKED_STATE_3 = one_play_preferences.getBoolean("AUTO_ON", false);
        auto_on.setChecked(CHECKED_STATE_3);
        auto_on.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    if (coin_alfa){
                        (new DialogCoinSettingCoin(Setting.this, "توجه", "توجه توجه", "بعد از فعال کردن دیگر نمی توانید از امکانات داخلی اپ مثل تماشا کردن ویدیو و عکس دسترسی پیدا کنید و همیشه قبل از باز شدن اپلیکیشن اینستاگرام باز میشود و مانع باز شدن اینستافول می شود (اینستافول فعال می شود و دانلود انجام می شود اما صفحات اپلیکیشن نمایش داده نمی شود", "روشن", "لغو", true, 1000)).showDialog();
                    }else if (coin >= 5000) {
                        (new DialogCoinSettingCoin(Setting.this, "توجه", "توجه توجه", "بعد از فعال کردن دیگر نمی توانید از امکانات داخلی اپ مثل تماشا کردن ویدیو و عکس دسترسی پیدا کنید و همیشه قبل از باز شدن اپلیکیشن اینستاگرام باز میشود و مانع باز شدن اینستافول می شود (اینستافول فعال می شود و دانلود انجام می شود اما صفحات اپلیکیشن نمایش داده نمی شود", "روشن", "لغو", true, 1)).showDialog();
                    } else {
                        (new DialogCoinSettingCoin(Setting.this, "سکه کافی نیست", "برای فعال کردن 5000 سکه میخواد", "برای گرفتن سکه به فروشگاه یه سر بزن", "فروشگاه", "باشه", false, 1)).showDialog();
                        auto_on.setChecked(false);
                    }
                } else {
                    one_play_editor.putBoolean("AUTO_ON", b);
                    one_play_editor.apply();
                }

            }
        });

        CHECKED_STATE_2 = one_play_preferences.getBoolean("NOTIFICATION_SHOW", true);
        notification_show.setChecked(CHECKED_STATE_2);
        notification_show.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                one_play_editor.putBoolean("NOTIFICATION_SHOW", b);
                one_play_editor.apply();

            }
        });

        CHECKED_STATE_1 = one_play_preferences.getBoolean("TOAST_END", true);
        Toast_end.setChecked(CHECKED_STATE_1);
        Toast_end.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    one_play_editor.putBoolean("TOAST_END", b);
                    one_play_editor.apply();
                } else {
                    if (coin_alfa){
                        one_play_editor.putBoolean("TOAST_END", b);
                        one_play_editor.commit();
                    }else if (coin >= 500) {
                        coin = coin - 500;
                        coinint = String.valueOf(coin);
                        CoinSingle.setText(coinint);
                        one_play_editor.putInt("COIN", coin);
                        one_play_editor.putBoolean("TOAST_END", b);
                        one_play_editor.commit();
                    } else {
                        (new DialogCoinSettingCoin(Setting.this, "سکه کافی نیست", "برای فعال کردن 500 سکه میخواد", "برای گرفتن سکه به فروشگاه یه سر بزن", "فروشگاه", "باشه", false, 0)).showDialog();
                        Toast_end.setChecked(true);
                    }
                }


            }
        });

        CHECKED_STATE_4 = one_play_preferences.getBoolean("TOAST_START", true);
        Toast_start.setChecked(CHECKED_STATE_4);
        Toast_start.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    one_play_editor.putBoolean("TOAST_START", b);
                    one_play_editor.apply();
                } else {
                    if (coin_alfa){
                        one_play_editor.putBoolean("TOAST_START", b);
                        one_play_editor.apply();
                    }else if (coin >= 500) {
                        coin = coin - 500;
                        coinint = String.valueOf(coin);
                        CoinSingle.setText(coinint);
                        one_play_editor.putInt("COIN", coin);
                        one_play_editor.putBoolean("TOAST_START", b);
                        one_play_editor.apply();
                    } else {
                        (new DialogCoinSettingCoin(Setting.this, "سکه کافی نیست", "برای فعال کردن 500 سکه میخواد", "برای گرفتن سکه به فروشگاه یه سر بزن", "فروشگاه", "باشه", false, 0)).showDialog();
                        Toast_start.setChecked(true);
                    }
                }

            }
        });


        go_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent s = new Intent(Setting.this, Store.class);
                startActivity(s);
            }
        });

        about_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                (new DialogAbout(Setting.this)).showDialog();
            }
        });

        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent s = new Intent(Setting.this, Suport_me.class);
                startActivity(s);
            }
        });

        name_image = one_play_preferences.getString("IMAGE_POST", "InstaFull/Pictures");
        name_image_full = "/حافظه داخلی/" + name_image + "/";
        name_folder_image_name.setText(name_image_full);
        name_folder_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (coin_alfa){
                    try{
                        (new DialogUniversalInfoUtils(Setting.this, name_image, 1)).showDialog();
                    }catch (Exception e){
                        FirebaseCrash.report(new Exception("chenge name file 1"));
                    }
                }else if (coin >= 2000) {
                    try{
                        coin = coin - 2000;
                        coinint = String.valueOf(coin);
                        CoinSingle.setText(coinint);
                        one_play_editor.putInt("COIN", coin);
                        one_play_editor.apply();
                        (new DialogUniversalInfoUtils(Setting.this, name_image, 1)).showDialog();
                    }catch (Exception e){
                        FirebaseCrash.report(new Exception("chenge name file 1"));
                    }
                } else {
                    (new DialogCoinSettingCoin(Setting.this, "سکه کافی نیست", "برای فعال کردن به 2000 سکه میخواد", "برای گرفتن سکه به فروشگاه یه سر بزن", "فروشگاه", "باشه", false, 0)).showDialog();
                }
            }
        });
        name_video = one_play_preferences.getString("VIDEO_POST", "InstaFull/Video");
        name_video_full = "/حافظه داخلی/" + name_video + "/";
        name_folder_video_name.setText(name_video_full);
        name_folder_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (coin_alfa){
                    try{
                        (new DialogUniversalInfoUtils(Setting.this, name_video, 2)).showDialog();
                    }catch (Exception e){
                        FirebaseCrash.report(new Exception("chenge name file 1"));
                    }
                }else if (coin >= 2000) {
                    try{
                        coin = coin - 2000;
                        coinint = String.valueOf(coin);
                        CoinSingle.setText(coinint);
                        one_play_editor.putInt("COIN", coin);
                        one_play_editor.apply();
                        (new DialogUniversalInfoUtils(Setting.this, name_video, 2)).showDialog();
                    }catch (Exception e){
                        FirebaseCrash.report(new Exception("chenge name file 1"));
                    }
                } else {
                    (new DialogCoinSettingCoin(Setting.this, "سکه کافی نیست", "برای فعال کردن به 2000 سکه میخواد", "برای گرفتن سکه به فروشگاه یه سر بزن", "فروشگاه", "باشه", false, 0)).showDialog();
                }
            }
        });

        name_profile = one_play_preferences.getString("PROFILE", "InstaFull/Profile");
        name_profile_full = "/حافظه داخلی/" + name_profile + "/";
        name_folder_profile_name.setText(name_profile_full);
        name_folder_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (coin_alfa){
                    try{
                        (new DialogUniversalInfoUtils(Setting.this, name_profile, 3)).showDialog();
                    }catch (Exception e){
                        FirebaseCrash.report(new Exception("chenge name file 1"));
                    }
                }else if (coin >= 2000) {
                    try{
                        coin = coin - 2000;
                        coinint = String.valueOf(coin);
                        CoinSingle.setText(coinint);
                        one_play_editor.putInt("COIN", coin);
                        one_play_editor.apply();
                        (new DialogUniversalInfoUtils(Setting.this, name_profile, 3)).showDialog();
                    }catch (Exception e){
                        FirebaseCrash.report(new Exception("chenge name file 1"));
                    }
                } else {
                    (new DialogCoinSettingCoin(Setting.this, "سکه کافی نیست", "برای فعال کردن به 2000 سکه میخواد", "برای گرفتن سکه به فروشگاه یه سر بزن", "فروشگاه", "باشه", false, 0)).showDialog();
                }
            }
        });

        name_story_image = one_play_preferences.getString("STORY_IMAGE", "InstaFull/Story/Image");
        name_story_image_full = "/حافظه داخلی/" + name_story_image + "/";
        name_folder_story_image_name.setText(name_story_image_full);
        name_folder_story_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (coin_alfa){
                    try{
                        (new DialogUniversalInfoUtils(Setting.this, name_story_image, 4)).showDialog();
                    }catch (Exception e){
                        FirebaseCrash.report(new Exception("chenge name file 1"));
                    }
                }else if (coin >= 2000) {
                    try{
                        coin = coin - 2000;
                        coinint = String.valueOf(coin);
                        CoinSingle.setText(coinint);
                        one_play_editor.putInt("COIN", coin);
                        one_play_editor.apply();
                        (new DialogUniversalInfoUtils(Setting.this, name_story_image, 4)).showDialog();
                    }catch (Exception e){
                        FirebaseCrash.report(new Exception("chenge name file 1"));
                    }
                } else {
                    (new DialogCoinSettingCoin(Setting.this, "سکه کافی نیست", "برای فعال کردن به 2000 سکه میخواد", "برای گرفتن سکه به فروشگاه یه سر بزن", "فروشگاه", "باشه", false, 0)).showDialog();
                }
            }
        });

        name_story_video = one_play_preferences.getString("STORY_VIDEO", "InstaFull/Story/Video");
        name_story_video_full = "/حافظه داخلی/" + name_story_video + "/";
        name_folder_story_video_name.setText(name_story_video_full);
        name_folder_story_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (coin_alfa){
                    try{
                        (new DialogUniversalInfoUtils(Setting.this, name_story_video, 5)).showDialog();
                    }catch (Exception e){
                        FirebaseCrash.report(new Exception("chenge name file 1"));
                    }
                }else if (coin >= 2000) {
                    try{
                        coin = coin - 2000;
                        coinint = String.valueOf(coin);
                        CoinSingle.setText(coinint);
                        one_play_editor.putInt("COIN", coin);
                        one_play_editor.apply();
                        (new DialogUniversalInfoUtils(Setting.this, name_story_video, 5)).showDialog();
                    }catch (Exception e){
                        FirebaseCrash.report(new Exception("chenge name file 1"));
                    }
                } else {
                    (new DialogCoinSettingCoin(Setting.this, "سکه کافی نیست", "برای فعال کردن به 2000 سکه میخواد", "برای گرفتن سکه به فروشگاه یه سر بزن", "فروشگاه", "باشه", false, 0)).showDialog();
                }
            }
        });


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                overridePendingTransition(0, 0);

            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent profile = new Intent(Setting.this, Profile_page.class);
                startActivity(profile);
                overridePendingTransition(0, 0);
                finish();

            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent image = new Intent(Setting.this, Post_view.class);
                startActivity(image);
                overridePendingTransition(0, 0);
                finish();

            }
        });

        story.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent story = new Intent(Setting.this, Story_view.class);
                startActivity(story);
                overridePendingTransition(0, 0);
                finish();

            }
        });

        FirebaseCrash.log("log 1");
        System.gc();
    }

}
