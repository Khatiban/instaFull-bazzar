package com.fury.instafull.PageStart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.fury.instafull.MainActivity;
import com.fury.instafull.MyDatabaseHelper;
import com.fury.instafull.MyDatabaseHelper_url;
import com.fury.instafull.PermissionHandler;
import com.fury.instafull.R;
import com.google.firebase.crash.FirebaseCrash;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fury on 1/28/2017.
 */
public class PageStart extends AhoyOnboarderActivity {

    SharedPreferences one_play_preferences;
    SharedPreferences.Editor one_play_editor;

    String SCheck;

    MyDatabaseHelper S64_data;
    MyDatabaseHelper_url url_data;

    String b64 = "01";
    String b64_1 = "001101010010010100100001001110010011010100000100110000";///
    String b64_2 = "0100011101000011010100110111000101000111010100110100100101100010001100110100010001010001010001010100001001";
    String b64_3 = "000001010100010101010101000001010000010011010001000111001101110100000101000100010000110100001001110100";//
    String b64_4 = "011101110100101101000010011100100111011101000100011000100";
    String b64_5 = "11011110011011101110000011100";
    String b64_6 = "100100010101111010011010100011000101001101";
    String b64_7 = "1100000110111101110000011001110111011001101001011101010111000100110000011001100111101000111001";
    String b64_8 = "011010100101010001010100001010110100011100110000011011000101000101000100010100000111000101100101010011110101011000110001011";
    String b64_9 = "101000110110101101100011001000100111100110110001100110110011101111001011101010110000101010101";
    String b64_10 = "01001010011000010110011101110001011001000110001001010110011000110011001";
    String b64_11 = "001010111010101000110001101101000011010000111100101110101001";
    String b64_12 = "010110111000101010010011100110101100100110000011010000100111101000101010011110101100001010111010000100";
    String b64_13 = "110111001111001010011110110101001101111010100000100001101100";
    String b64_14 = "00101000001011011010111001101110";
    String b64_15 = "0110101001001011000011";
    String b64_16 = "001000110010001110111011011100100110001000111010001110100111001111001010110100011000101000101";
    String b64_17 = "00111001010000110110010001110111010010010110111001011010011001100";
    String b64_18 = "0111000011101000110010001010001011110100100000101010110011010100110001001001110011010100111000001000100010100010011001101100";
    String b64_19 = "11001000010001100000011100001010011010000010110010101101000011001000100001101001110011101100";
    String b64_20 = "0101111010100100110100001001101010101010100100101001111011101100110111001001101";
    String b64_21 = "0011100001110111010010010110111100110111011011110100100101";
    String b64_22 = "001000010101100110011001100010011100";
    String b64_23 = "110100111100111001010000010";
    String b64_24 = "101000101101010011000010011011101111001001101000111011100110011010010";
    String b64_25 = "010110010101101000011100010010101101111000010110010011001001111010011100100110101001100";
    String b64_26 = "1100111010001101110011110000111101001000010011011000101000001001111011011100100010001110100011110100";
    String b64_27 = "011011101001111001110010100010000111000001010110011000101000101011011110110001101000110001100100110010100111000001101110110110001010";
    String b64_28 = "10100110010011001100110100001011010011010100110111001101000001101110111010000101111011000010101011101101101011";
    String b64_29 = "01100011010100110110001001101010";
    String b64_30 = "000100";
    String b64_31 = "1010101010000110100000101110111010";
    String b64_32 = "00101";
    String b64_33 = "01000001";
    String b64_34 = "0100000101";
    String b64_35 = "0";
    String b64_36 = "0010011";
    String b64_37 = "110100111101";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        b64_1 = b64 + b64_1;
        b64_7 = b64 + b64_7;
        b64_35 = b64 + b64_35;

        S64_data = new MyDatabaseHelper(this);
        url_data = new MyDatabaseHelper_url(this);

        String input = b64_1 + b64_2 + b64_3 + b64_4 + b64_5 + b64_6 + b64_7 + b64_8 + b64_9 + b64_10 + b64_11 + b64_12 + b64_13 + b64_14 +
                b64_15 + b64_16 + b64_17 + b64_18 + b64_19 + b64_20 + b64_21 + b64_22 + b64_23 + b64_24 + b64_25 + b64_26 + b64_27 + b64_28 +
                b64_29 + b64_30 + b64_31 + b64_32 + b64_33 + b64_34 + b64_35 + b64_36 + b64_37 ;


        S64_data.insertData(input, "1");
        url_data.insertData("011010000111010001110100011100000011101000101111001011110111001101101111011010000110010101101001011011000010111001101001011011110010111101101001011100110110000101110110011001010010111101110110001100100010111101110011011101000110111101110010011110010010111001110000011010000111000000111111011000010011110101101100001001100111010100111101", "1");

        one_play_preferences = getApplicationContext().getSharedPreferences("PROJECT_NAME", android.content.Context.MODE_PRIVATE);
        one_play_editor = one_play_preferences.edit();

        one_play_editor.putBoolean("coin_play", true);
        one_play_editor.apply();


        String page_1_1 = "خوش آمدی";
        String page_1_2 = "به دنیای اینستافول خوش آمدید!";
        String page_2_1 = "چرا اینستافول ?";
        String page_2_2 = "رایگان ، سرعت بالا ، راحت";
        String page_3_1 = "دانلود پست";
        String page_3_2 = "بعد از فعال کردن اینستافول با کپی کردن آدرس پست در اینستاگرام دانلود خودکار شروع میشود ( کافیه روی Copy Share URL کلیک کنی )";
        String page_4_1 = "تماشا تصاویر و ویدیوها";
        String page_4_2 = "برای تماشا کردن تصاویر و ویدیو های دانلود کرده داخل برنامه بر روی اون دکمه بالا کلیک کنید";
        String page_5_1 = "دانلود پروفایل";
        String page_5_2 = "بعد از فعال کردن اینستافول با کپی کردن آدرس پروفایل در اینستاگرام دانلود خودکار شروع می شود ( کافیه بر روی Copy Profile URL کلیک کنی )";
        String page_6_1 = "تماشا عکس پروفایل";
        String page_6_2 = "برای تماشا کردن عکس پروفایل های دانلود شده داخل برنامه بر روی اون دکمه بالا کلیک کنید";
        String page_7_1 = "دانلود استوری ها";
        String page_7_2 = "بعد از فعال کردن اینستافول با کپی کردن آدرس پروفایل در اینستاگرام دانلود خودکار تمام استوری شروع می شود ( کافیه بر روی Copy Profile URL کلیک کنی )";
        String page_8_1 = "تماشا استوری ها";
        String page_8_2 = "برای تماشا تصاویر و ویدیو های استوری دانلود شده داخل برنامه بر روی اون دکمه بالا کلیک کنید";
        String page_9_1 = "تنظیمات";
        String page_9_2 = "تنظیمات پیشرفته خودکار شدن برنامه ، نمایش فعال بودن برنامه ، تغییر محل ذخیره سازی و ...";
        String page_10_1 = "سکه";
        String page_10_2 = "به شما 2000 سکه رایگان تعلق گرفت!";
        String page_11_1 = "فروشگاه";
        String page_11_2 = "تمام امکانات اینستافول از دانلود تا تنظیمات رایگان است شما می توانید با استفاده از فروشگاه سکه رایگان و یا سکه بخرید برای بخش هایی که به سکه نیاز دارد";

        AhoyOnboarderCard ahoyOnboarderCard1 = new AhoyOnboarderCard(page_1_1, page_1_2, R.drawable.welcome);
        AhoyOnboarderCard ahoyOnboarderCard2 = new AhoyOnboarderCard(page_2_1, page_2_2, R.drawable.instafull);
        AhoyOnboarderCard ahoyOnboarderCard3 = new AhoyOnboarderCard(page_3_1, page_3_2, R.drawable.download_post);
        AhoyOnboarderCard ahoyOnboarderCard4 = new AhoyOnboarderCard(page_4_1, page_4_2, R.drawable.help_post);
        AhoyOnboarderCard ahoyOnboarderCard5 = new AhoyOnboarderCard(page_5_1, page_5_2, R.drawable.download_profile_and_story);
        AhoyOnboarderCard ahoyOnboarderCard6 = new AhoyOnboarderCard(page_6_1, page_6_2, R.drawable.help_profile);
        AhoyOnboarderCard ahoyOnboarderCard7 = new AhoyOnboarderCard(page_7_1, page_7_2, R.drawable.download_profile_and_story);
        AhoyOnboarderCard ahoyOnboarderCard8 = new AhoyOnboarderCard(page_8_1, page_8_2, R.drawable.help_story);
        AhoyOnboarderCard ahoyOnboarderCard9 = new AhoyOnboarderCard(page_9_1, page_9_2, R.drawable.help_setting);
        AhoyOnboarderCard ahoyOnboarderCard10 = new AhoyOnboarderCard(page_10_1, page_10_2, R.drawable.hedeye);
        AhoyOnboarderCard ahoyOnboarderCard11 = new AhoyOnboarderCard(page_11_1, page_11_2, R.drawable.story);

        ahoyOnboarderCard1.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard2.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard3.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard4 .setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard5 .setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard6 .setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard7 .setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard8 .setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard9 .setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard10.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard11.setBackgroundColor(R.color.black_transparent);

        List<AhoyOnboarderCard> pages = new ArrayList<>();

        pages.add(ahoyOnboarderCard1);
        pages.add(ahoyOnboarderCard2);
        pages.add(ahoyOnboarderCard3);
        pages.add(ahoyOnboarderCard4 );
        pages.add(ahoyOnboarderCard5 );
        pages.add(ahoyOnboarderCard6 );
        pages.add(ahoyOnboarderCard7 );
        pages.add(ahoyOnboarderCard8 );
        pages.add(ahoyOnboarderCard9 );
        pages.add(ahoyOnboarderCard10);
        pages.add(ahoyOnboarderCard11);

        for (AhoyOnboarderCard page : pages) {
            page.setTitleColor(R.color.colorPrimary);
            page.setDescriptionColor(R.color.colorPrimary);
        }

        setFinishButtonTitle("بزن بریم");

        showNavigationControls(true);
        setGradientBackground();

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/A-Chamran.ttf");
        setFont(face);

        setInactiveIndicatorColor(R.color.grey_app);
        setActiveIndicatorColor(R.color.colorPrimary);

        setOnboardPages(pages);

        if (Build.VERSION.SDK_INT >= 22) {
            new Handler().postDelayed(new Thread() {
                @Override
                public void run() {
                    super.run();
                    checkPermissions();
                }
            }, 1000);
        }

        FirebaseCrash.log("log 1");
        System.gc();
    }


    @Override
    public void onFinishButtonPressed() {

        Intent uou = new Intent(PageStart.this, MainActivity.class);
        startActivity(uou);
        PageStart.this.finish();

    }



    private void checkPermissions(){

        String[] per = {android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.READ_PHONE_STATE,android.Manifest.permission.ACCESS_COARSE_LOCATION};

        new PermissionHandler().checkPermission(this, per, new PermissionHandler.OnPermissionResponse() {
            @Override
            public void onPermissionGranted() {
                // permission granted
                // your code
            }

            @Override
            public void onPermissionDenied() {
                // User canceled permission
                Toast.makeText(PageStart.this,"در صورت نپذیرفتن درخواست ها برنامه با مشکل مواجه می شود!", Toast.LENGTH_LONG).show();
            }
        });


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
