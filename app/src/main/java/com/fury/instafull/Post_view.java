package com.fury.instafull;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.adapters.ViewPagerPost;
import com.appodeal.ads.Appodeal;
import com.appodeal.ads.InterstitialCallbacks;
import com.google.firebase.crash.FirebaseCrash;

public class Post_view extends AppCompatActivity {

    ImageView home, profile, image, story, setting;

    ViewPager pager_post;

    ViewPagerPost adapter;

    CharSequence[] Titles;

    public static Post_view act;

    SharedPreferences one_play_preferences;
    SharedPreferences.Editor one_play_editor;

    TabLayout tl;

    boolean play1 = false;
    int iads;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_post_view);

        act = this;

        one_play_preferences = getApplicationContext().getSharedPreferences("PROJECT_NAME", android.content.Context.MODE_PRIVATE);
        one_play_editor = one_play_preferences.edit();

        iads = one_play_preferences.getInt("IAds", 1);

        if (iads == 1){
            iads = iads + 1;
            one_play_editor.putInt("IAds", iads);
            one_play_editor.apply();
        }else if (iads == 2){
            iads = iads + 1;
            one_play_editor.putInt("IAds", iads);
            one_play_editor.apply();
        }else if (iads == 3){
            iads = iads + 1;
            one_play_editor.putInt("IAds", iads);
            one_play_editor.apply();
        }else if (iads == 4){
            iads = iads + 1;
            one_play_editor.putInt("IAds", iads);
            one_play_editor.apply();
        }else if (iads == 5){
            iads = 1;
            one_play_editor.putInt("IAds", iads);
            one_play_editor.apply();

            String appKey = "79e2a2830ef44210ecf09dc0f39d99e4f9864c224ca68d45";
            Appodeal.initialize(this, appKey, Appodeal.INTERSTITIAL );

            Appodeal.setInterstitialCallbacks(new InterstitialCallbacks() {
                @Override
                public void onInterstitialLoaded(boolean isPrecache) {
                    if (!play1) {
                        play1 = true;
                        Appodeal.show(Post_view.this, Appodeal.INTERSTITIAL);
                        Appodeal.isLoaded(Appodeal.INTERSTITIAL);
                    }
                    FirebaseCrash.report(new Exception("Show Post Interstitial"));
                    Log.d("Appodeal", "onInterstitialLoaded");
                }
                @Override
                public void onInterstitialFailedToLoad() {
                    Log.d("Appodeal", "onInterstitialFailedToLoad");
                }
                @Override
                public void onInterstitialShown() {
                    Log.d("Appodeal", "onInterstitialShown");
                }
                @Override
                public void onInterstitialClicked() {
                    FirebaseCrash.report(new Exception("Interstitial Clicked Post"));
                    Log.d("Appodeal", "onInterstitialClicked");
                }
                @Override
                public void onInterstitialClosed() {
                    Log.d("Appodeal", "onInterstitialClosed");
                }
            });

        }

        home = (ImageView) findViewById(R.id.home_page);
        profile = (ImageView) findViewById(R.id.profile);
        image = (ImageView) findViewById(R.id.image_page);
        story = (ImageView) findViewById(R.id.story);
        setting = (ImageView) findViewById(R.id.setting);
        pager_post = (ViewPager) findViewById(R.id.pager_post);
        tl = (TabLayout)findViewById(R.id.tl);

        image.setImageResource(R.drawable.bugreporter_category_capturephoto_on);

        Titles = new CharSequence[]{"تصاویر", "ویدیو"};

        adapter = new ViewPagerPost(getSupportFragmentManager(), Titles, Titles.length);
        pager_post.setAdapter(this.adapter);
        tl.setupWithViewPager(pager_post);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                overridePendingTransition(R.anim.hold, R.anim.in_insta);

            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent profile = new Intent(Post_view.this,Profile_page.class);
                startActivity(profile);
                overridePendingTransition(0, 0);
                finish();

            }
        });

        story.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent story = new Intent(Post_view.this,Story_view.class);
                startActivity(story);
                overridePendingTransition(0, 0);
                finish();

            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent setting = new Intent(Post_view.this,Setting.class);
                startActivity(setting);
                overridePendingTransition(0, 0);
                finish();

            }
        });

        FirebaseCrash.log("log 1");
        System.gc();
    }

}
