package com.fury.instafull;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.com.example.android.trivialdrivesample.util.IabHelper;
import com.com.example.android.trivialdrivesample.util.IabResult;
import com.com.example.android.trivialdrivesample.util.Inventory;
import com.com.example.android.trivialdrivesample.util.Purchase;
import com.google.firebase.crash.FirebaseCrash;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Store extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout card_free_coin_insta,ads_video, card_coin_telegram, card_coin_1000, card_coin_2000, card_coin_5000, card_coin_9000, card_coin_16000, card_coin_8;
    SharedPreferences one_play_preferences;
    SharedPreferences.Editor one_play_editor;

    int coin_plus, outs;
    String tedad_coin;

    ImageView back_page_video;

    Boolean telegram_save, instagram_save, coin_alfa,click,start,log,not_one_ads;

    TextView off_6, off_5, off_4, off_3, off_2, off_1, CoinSingle, text_2, text_1, text_22, text_11, text_222, text_111, text_coin_number_1, text_money_2, text_coin_number_2, text_money_1, text_coin_number_3, text_money_3, text_coin_number_4, text_money_4, text_coin_number_5, text_money_5, text_coin_number_6, text_money_6;

    // Debug tag, for logging
    // Debug tag, for logging

    ScrollView scrollView;

    // SKUs for our products: the premium upgrade (non-consumable)
    static final String SKU_PREMIUM = "bhsce";
    static final String SKU_PREMIUM2 = "dhsce";
    static final String SKU_PREMIUM3 = "dmsce";
    static final String SKU_PREMIUM4 = "phsce";
    static final String SKU_PREMIUM5 = "pmsce";
    static final String SKU_PREMIUM6 = "benhayat";

    static String SKU ,SCheck ,b64key;

    // Does the user have the premium upgrade?
    boolean mIsPremium = false;

    int coinint,ads_plus;

    // The helper object
    IabHelper mHelper;

    int nov = 0;

    File root, root1, root2, root3, root4, root5;

    private ScheduledThreadPoolExecutor mDialogDaemon_time,mDialogDaemon_coin;

    MyDatabaseHelper key_64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        key_64 = new MyDatabaseHelper(this);
        StringBuffer dadeha = new StringBuffer();
        Cursor res_2 = key_64.showAllData();
        while (res_2.moveToNext()) {
            dadeha.append(res_2.getString(1));
        }
        b64key = dadeha.toString();
        String output = "";
        for (int i = 0; i <= b64key.length() - 8 ; i+=8){
            int k = Integer.parseInt(b64key.substring(i,i+8),2);
            output+=(char)k;
        }
        mHelper = new IabHelper(this, output);
        setContentView(R.layout.content_store);

        root = new File(Environment.getExternalStorageDirectory(), "InstaFull/Log/a000");
        root1 = new File(Environment.getExternalStorageDirectory(), "InstaFull/Log/b000");
        root2 = new File(Environment.getExternalStorageDirectory(), "InstaFull/Log/c000");
        root3 = new File(Environment.getExternalStorageDirectory(), "InstaFull/Log/d000");
        root4 = new File(Environment.getExternalStorageDirectory(), "InstaFull/Log/ab000");
        root5 = new File(Environment.getExternalStorageDirectory(), "InstaFull/Log/c0000");

        one_play_preferences = getApplicationContext().getSharedPreferences("PROJECT_NAME", android.content.Context.MODE_PRIVATE);
        one_play_editor = one_play_preferences.edit();
        SCheck = one_play_preferences.getString("S64", "");

        click = false;
        start = false;
        log = false;
        not_one_ads = false;

        card_free_coin_insta = (RelativeLayout) findViewById(R.id.card_free_coin_insta);
        ads_video = (RelativeLayout) findViewById(R.id.ads_video);
        card_coin_telegram = (RelativeLayout) findViewById(R.id.card_coin_telegram);
        card_coin_1000 = (RelativeLayout) findViewById(R.id.card_coin_1000);
        card_coin_2000 = (RelativeLayout) findViewById(R.id.card_coin_2000);
        card_coin_5000 = (RelativeLayout) findViewById(R.id.card_coin_5000);
        card_coin_9000 = (RelativeLayout) findViewById(R.id.card_coin_9000);
        card_coin_16000 = (RelativeLayout) findViewById(R.id.card_coin_16000);
        card_coin_8 = (RelativeLayout) findViewById(R.id.card_coin_8);
        CoinSingle = (TextView) findViewById(R.id.CoinSingle);
        text_2 = (TextView) findViewById(R.id.text_2);
        text_1 = (TextView) findViewById(R.id.text_1);
        text_22 = (TextView) findViewById(R.id.text_22);
        text_11 = (TextView) findViewById(R.id.text_11);
        text_222 = (TextView) findViewById(R.id.text_222);
        text_111 = (TextView) findViewById(R.id.text_111);
        text_coin_number_1 = (TextView) findViewById(R.id.text_coin_number_1);
        text_coin_number_2 = (TextView) findViewById(R.id.text_coin_number_2);
        text_money_1 = (TextView) findViewById(R.id.text_money_1);
        text_money_2 = (TextView) findViewById(R.id.text_money_2);
        off_6 = (TextView) findViewById(R.id.off_6);
        off_5 = (TextView) findViewById(R.id.off_5);
        off_4 = (TextView) findViewById(R.id.off_4);
        off_3 = (TextView) findViewById(R.id.off_3);
        off_2 = (TextView) findViewById(R.id.off_2);
        off_1 = (TextView) findViewById(R.id.off_1);
        text_coin_number_3 = (TextView) findViewById(R.id.text_coin_number_3);
        text_money_3 = (TextView) findViewById(R.id.text_money_3);
        text_coin_number_4 = (TextView) findViewById(R.id.text_coin_number_4);
        text_money_4 = (TextView) findViewById(R.id.text_money_4);
        text_coin_number_5 = (TextView) findViewById(R.id.text_coin_number_5);
        text_money_5 = (TextView) findViewById(R.id.text_money_5);
        text_coin_number_6 = (TextView) findViewById(R.id.text_coin_number_6);
        text_money_6 = (TextView) findViewById(R.id.text_money_6);
        back_page_video = (ImageView) findViewById(R.id.back_page_video);
        scrollView = (ScrollView) findViewById(R.id.scroll);

        card_coin_1000.setOnClickListener(this);
        card_coin_2000.setOnClickListener(this);
        card_coin_5000.setOnClickListener(this);
        card_coin_9000.setOnClickListener(this);
        card_coin_16000.setOnClickListener(this);
        card_coin_8.setOnClickListener(this);
        card_coin_telegram.setOnClickListener(this);
        card_free_coin_insta.setOnClickListener(this);
        ads_video.setOnClickListener(this);
        back_page_video.setOnClickListener(this);

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/A-Chamran.ttf");
        text_2.setTypeface(face);
        text_1.setTypeface(face);
        text_22.setTypeface(face);
        text_11.setTypeface(face);
        text_222.setTypeface(face);
        text_111.setTypeface(face);
        text_coin_number_1.setTypeface(face);
        text_money_2.setTypeface(face);
        text_coin_number_2.setTypeface(face);
        text_money_1.setTypeface(face);
        text_coin_number_3.setTypeface(face);
        text_money_3.setTypeface(face);
        text_coin_number_4.setTypeface(face);
        text_money_4.setTypeface(face);
        text_coin_number_5.setTypeface(face);
        text_money_5.setTypeface(face);
        text_coin_number_6.setTypeface(face);
        text_money_6.setTypeface(face);

        off_6.setVisibility(View.GONE);
        off_5.setVisibility(View.GONE);
        off_4.setVisibility(View.GONE);
        off_3.setVisibility(View.GONE);
        off_2.setVisibility(View.GONE);
        off_1.setVisibility(View.GONE);

        coinint = one_play_preferences.getInt("COIN", 0);

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

                            coinint = one_play_preferences.getInt("COIN", 0);
                            tedad_coin = String.valueOf(coinint);
                            CoinSingle.setText(tedad_coin);

                        }
                    });
                }
            }, 0L, 2000L, TimeUnit.MILLISECONDS);

        } catch (Exception e) {
            FirebaseCrash.report(new Exception("14"));
        }
        check_key(dadeha.toString());
        String output2 = "";
        for (int i = 0; i <= SCheck.length() - 8 ; i+=8){
            int k = Integer.parseInt(SCheck.substring(i,i+8),2);
            output2+=(char)k;
        }
        check_64(output2,output);
        tedad_coin = String.valueOf(coinint);
        telegram_save = one_play_preferences.getBoolean("telegram_save", false);
        instagram_save = one_play_preferences.getBoolean("instagram_save", false);
        coin_alfa = one_play_preferences.getBoolean("COIN_Alfa", false);



        if (coin_alfa) {
            CoinSingle.setText("بی نهایت");
        } else {
            CoinSingle.setText(tedad_coin);
        }

        if (instagram_save) {
            card_free_coin_insta.setVisibility(View.GONE);
        }

        if (telegram_save) {
            card_coin_telegram.setVisibility(View.GONE);
        }


// You can find it in your Bazaar console, in the Dealers section.
// It is recommended to add more security than just pasting it in your source code;
        mHelper.enableDebugLogging(true);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                }
                // Hooray, IAB is fully set up!
                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });


        FirebaseCrash.log("log 1");
        System.gc();

    }



    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            if (result.isFailure()) {
                return;
            } else {
                // does the user have the premium upgrade?
                mIsPremium = inventory.hasPurchase(SKU_PREMIUM);
                if (mIsPremium) {
                    MasrafSeke(inventory.getPurchase(SKU_PREMIUM));
                }

                mIsPremium = inventory.hasPurchase(SKU_PREMIUM2);
                if (mIsPremium) {
                    MasrafSeke(inventory.getPurchase(SKU_PREMIUM2));
                }

                mIsPremium = inventory.hasPurchase(SKU_PREMIUM3);
                if (mIsPremium) {
                    MasrafSeke(inventory.getPurchase(SKU_PREMIUM3));
                }

                mIsPremium = inventory.hasPurchase(SKU_PREMIUM4);
                if (mIsPremium) {
                    MasrafSeke(inventory.getPurchase(SKU_PREMIUM4));
                }

                mIsPremium = inventory.hasPurchase(SKU_PREMIUM5);
                if (mIsPremium) {
                    MasrafSeke(inventory.getPurchase(SKU_PREMIUM5));
                }

                mIsPremium = inventory.hasPurchase(SKU_PREMIUM6);
                if (mIsPremium) {
                    MasrafSeke(inventory.getPurchase(SKU_PREMIUM6));
                }

                // update UI accordingly

            }

        }
    };




    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.back_page_video:
                finish();
                break;
            case R.id.ads_video:

                if (app_net.getInstance(Store.this).isOnline()) {

                    Intent adv = new Intent(this,AdVideo.class);
                    startActivity(adv);

                } else {
                    Toast.makeText(getApplicationContext(), "اینترنت قطع می باشد", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.card_free_coin_insta:

                if (app_net.getInstance(Store.this).isOnline()) {
                    Uri uri = Uri.parse("http://instagram.com/_u/fury_studio_ir");
                    Intent inestagram = new Intent(Intent.ACTION_VIEW, uri);
                    inestagram.setPackage("com.instagram.android");
                    try {
                        startActivity(inestagram);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://instagram.com/fury_studio_ir")));
                    }

                    if (!instagram_save) {
                        card_free_coin_insta.setVisibility(View.GONE);
                        coin_plus = 1000 + coinint;
                        anim_coin(coin_plus);
                        one_play_editor.putInt("COIN", coin_plus);
                        one_play_editor.putBoolean("instagram_save", true);
                        one_play_editor.apply();
                        FirebaseCrash.report(new Exception("instagram_save anjamshod"));
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "اینترنت قطع می باشد", Toast.LENGTH_LONG).show();
                }


                break;
            case R.id.card_coin_telegram:

                if (app_net.getInstance(Store.this).isOnline()) {
                    Uri uri2 = Uri.parse("http://t.me/Fury_studio");
                    Intent inestagram2 = new Intent(Intent.ACTION_VIEW, uri2);
                    inestagram2.setPackage("org.telegram.messenger");
                    try {
                        startActivity(inestagram2);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://t.me/Fury_studio")));
                    }

                    if (!telegram_save) {
                        card_coin_telegram.setVisibility(View.GONE);
                        coin_plus = 1000 + coinint;
                        anim_coin(coin_plus);
                        one_play_editor.putInt("COIN", coin_plus);
                        one_play_editor.putBoolean("telegram_save", true);
                        one_play_editor.apply();
                        FirebaseCrash.report(new Exception("telegram_save anjamshod"));
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "اینترنت قطع می باشد", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.card_coin_1000:

                if (app_net.getInstance(Store.this).isOnline()) {

                    SKU = "bhsce";
                    nov = 1;

                    try{
                        mHelper.launchPurchaseFlow(this, SKU, 1001, mPurchaseFinishedListener, "payload-string");
                    }catch (Exception e){
                        Toast.makeText(Store.this, "لطفا کمی صبر کنید و دوباره کلیک کنید", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "اینترنت قطع می باشد", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.card_coin_2000:
                if (app_net.getInstance(Store.this).isOnline()) {

                    SKU = "dhsce";
                    nov = 2;

                    try{
                        mHelper.launchPurchaseFlow(this, SKU, 1001, mPurchaseFinishedListener, "payload-string");
                    }catch (Exception e){
                        Toast.makeText(Store.this, "لطفا کمی صبر کنید و دوباره کلیک کنید", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "اینترنت قطع می باشد", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.card_coin_5000:
                if (app_net.getInstance(Store.this).isOnline()) {

                    SKU = "dmsce";
                    nov = 3;

                    try{
                        mHelper.launchPurchaseFlow(this, SKU, 1001, mPurchaseFinishedListener, "payload-string");
                    }catch (Exception e){
                        Toast.makeText(Store.this, "لطفا کمی صبر کنید و دوباره کلیک کنید", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "اینترنت قطع می باشد", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.card_coin_9000:
                if (app_net.getInstance(Store.this).isOnline()) {

                    SKU = "phsce";
                    nov = 4;

                    try{
                        mHelper.launchPurchaseFlow(this, SKU, 1001, mPurchaseFinishedListener, "payload-string");
                    }catch (Exception e){
                        Toast.makeText(Store.this, "لطفا کمی صبر کنید و دوباره کلیک کنید", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "اینترنت قطع می باشد", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.card_coin_16000:
                if (app_net.getInstance(Store.this).isOnline()) {

                    SKU = "pmsce";
                    nov = 5;

                    try{
                        mHelper.launchPurchaseFlow(this, SKU, 1001, mPurchaseFinishedListener, "payload-string");
                    }catch (Exception e){
                        Toast.makeText(Store.this, "لطفا کمی صبر کنید و دوباره کلیک کنید", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "اینترنت قطع می باشد", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.card_coin_8:
                if (app_net.getInstance(Store.this).isOnline()) {

                    SKU = "benhayat";
                    nov = 6;

                    try{
                        mHelper.launchPurchaseFlow(this, SKU, 1001, mPurchaseFinishedListener, "payload-string");
                    }catch (Exception e){
                        Toast.makeText(Store.this, "لطفا کمی صبر کنید و دوباره کلیک کنید", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "اینترنت قطع می باشد", Toast.LENGTH_LONG).show();
                }

                break;

        }
    }


    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            if (result.isFailure()) {
                return;
            } else if (purchase.getSku().equals(SKU)) {
                if (nov == 1){
                    get_coin(25000,1);
                }else if (nov == 2){
                    get_coin(250000,2);
                }else if (nov == 3){
                    get_coin(700000,3);
                }else if (nov == 4){
                    get_coin(2500000,4);
                }else if (nov == 5){
                    get_coin(7000000,5);
                }else if (nov == 6){
                    get_coin(8,6);
                }else {
                    Toast.makeText(Store.this, "خرید ناموفق بود", Toast.LENGTH_SHORT).show();
                }
                MasrafSeke(purchase);
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }


    private void MasrafSeke(Purchase kala) {
        mHelper.consumeAsync(kala, new IabHelper.OnConsumeFinishedListener() {
            @Override
            public void onConsumeFinished(Purchase purchase, IabResult result) {
            }
        });
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
        }
    }

    public void anim_coin(final int a) {

        int b ;

        if (a >= 20000 && a <= 200000){
            b = 10;
        }else if (a >= 200000 && a <= 500000) {
            b = 80;
        }else if (a >= 500000 && a <= 2000000) {
            b = 180;
        }else if (a >= 2000000 && a <= 5000000) {
            b = 1860;
        }else if (a >= 5000000) {
            b = 2860;
        }else {
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

                            try{
                                if ( outs < a ) {
                                    outs = outs + finalB;
                                    final int finalS = outs;
                                    String n = String.valueOf(finalS);
                                    CoinSingle.setText(n);
                                }else {
                                    mDialogDaemon_coin.shutdown();
                                    mDialogDaemon_coin = null;
                                }
                            }catch (Exception e){
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

    public void get_coin(int a,int u) {
        try {
            if (u == 6){
                CoinSingle.setText("بی نهایت");
                one_play_editor.putBoolean("COIN_Alfa", true);
                one_play_editor.apply();
                Toast.makeText(Store.this, "خرید موفق", Toast.LENGTH_SHORT).show();
            }else {
                coin_plus = a + coinint;
                one_play_editor.putInt("COIN", coin_plus);
                one_play_editor.apply();
                anim_coin(coin_plus);
                Toast.makeText(Store.this, "خرید موفق", Toast.LENGTH_SHORT).show();
            }

            if (u == 1){
                (new DialogCoinGift(Store.this, "شما برنده 5000 سکه رایگان شده اید!", "یعنی شما 20000 سکه خریدی 5000 دیگه هم گرفتی!")).showDialog();
                if (!root.exists()) {
                    generateNoteOnSD(getApplicationContext(), "a000", "junhn s;i 20000");
                }
                FirebaseCrash.report(new Exception("20000 T anjamshod"));
            }else if (u == 2){
                (new DialogCoinGift(Store.this, "شما برنده 50000 سکه رایگان شده اید!", "یعنی شما 200000 سکه خریدی 50000 دیگه هم گرفتی!")).showDialog();
                if (!root1.exists()) {
                    generateNoteOnSD(getApplicationContext(), "b000", "junhn s;i 200000");
                }
                FirebaseCrash.report(new Exception("200000 T anjamshod"));
            }else if (u == 3){
                (new DialogCoinGift(Store.this, "شما برنده 200000 سکه رایگان شده اید!", "یعنی شما 500000 سکه خریدی 200000 دیگه هم گرفتی!")).showDialog();
                if (!root2.exists()) {
                    generateNoteOnSD(getApplicationContext(), "c000", "junhn s;i 500000");
                }
                FirebaseCrash.report(new Exception("500000 T anjamshod"));
            }else if (u == 4){
                (new DialogCoinGift(Store.this, "شما برنده 500000 سکه رایگان شده اید!", "یعنی شما 2000000 سکه خریدی 500000 دیگه هم گرفتی!")).showDialog();
                if (!root3.exists()) {
                    generateNoteOnSD(getApplicationContext(), "d000", "junhn s;i 2000000");
                }
                FirebaseCrash.report(new Exception("2000000 T anjamshod"));
            }else if (u == 5){
                (new DialogCoinGift(Store.this, "شما برنده 2000000 سکه رایگان شده اید!", "یعنی شما 5000000 سکه خریدی 2000000 دیگه هم گرفتی!")).showDialog();
                if (!root4.exists()) {
                    generateNoteOnSD(getApplicationContext(), "ab000", "junhn s;i 5000000");
                }
                FirebaseCrash.report(new Exception("5000000 T anjamshod"));
            }else if (u == 6){
                if (!root5.exists()) {
                    generateNoteOnSD(getApplicationContext(), "c0000", "junhn s;i 8");
                }
                FirebaseCrash.report(new Exception("8 T anjamshod"));
            }

        } catch (Exception e) {
            if (u == 6){
                one_play_editor.putBoolean("COIN_Alfa", true);
                one_play_editor.apply();
            }else {
                one_play_editor.putInt("COIN", a + coinint);
                one_play_editor.apply();
                Toast.makeText(Store.this, "خرید موفق", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void check_key(String s){
        if (!Objects.equals(s, SCheck)){
            FirebaseCrash.report(new Exception("H_shod_" + getPackageName()));
            System.exit(0);
        }
    }

    public void check_64(String s,String i){
        if (!Objects.equals(s, i)){
            FirebaseCrash.report(new Exception("H_shod_" + getPackageName()));
            System.exit(0);
        }
    }

}
