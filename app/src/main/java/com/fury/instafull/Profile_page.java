package com.fury.instafull;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.adapters.ViewPagerAdapter;
import com.google.firebase.crash.FirebaseCrash;

public class Profile_page extends AppCompatActivity {

    ImageView home, profile, image, story, setting,go_shop;

    ViewPager pager_profile;

    public static Profile_page act;

    ViewPagerAdapter adapter;

    TextView CoinSingle;

    Boolean coin_alfa;

    String coin;

    SharedPreferences one_play_preferences;
    SharedPreferences.Editor one_play_editor;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_profile_page);

        act = this;

        home = (ImageView) findViewById(R.id.home_page);
        profile = (ImageView) findViewById(R.id.profile);
        image = (ImageView) findViewById(R.id.image_page);
        story = (ImageView) findViewById(R.id.story);
        setting = (ImageView) findViewById(R.id.setting);
        go_shop = (ImageView) findViewById(R.id.go_shop);
        pager_profile = (ViewPager) findViewById(R.id.pager_profile);
        CoinSingle = (TextView) findViewById(R.id.CoinSingle);

        one_play_preferences = getApplicationContext().getSharedPreferences("PROJECT_NAME", android.content.Context.MODE_PRIVATE);
        one_play_editor = one_play_preferences.edit();

        int coinint = one_play_preferences.getInt("COIN", 0);
        coin = String.valueOf(coinint);
        coin_alfa = one_play_preferences.getBoolean("COIN_Alfa", false);

        if (coin_alfa) {
            CoinSingle.setText("بی نهایت");
        }else {
            CoinSingle.setText(coin);
        }

        profile.setImageResource(R.drawable.dock_profile_whiteout);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        pager_profile.setAdapter(this.adapter);

        go_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent s = new Intent(Profile_page.this, Store.class);
                startActivity(s);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                overridePendingTransition(0, 0);

            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent image = new Intent(Profile_page.this,Post_view.class);
                startActivity(image);
                overridePendingTransition(0, 0);
                finish();

            }
        });

        story.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent story = new Intent(Profile_page.this,Story_view.class);
                startActivity(story);
                overridePendingTransition(0, 0);
                finish();

            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent setting = new Intent(Profile_page.this,Setting.class);
                startActivity(setting);
                overridePendingTransition(0, 0);
                finish();

            }
        });

        FirebaseCrash.log("log 1");
        System.gc();
    }

}
