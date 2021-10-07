package com.fury.instafull;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.adapters.ViewPagerStory;
import com.google.firebase.crash.FirebaseCrash;

public class Story_view extends AppCompatActivity {

    ImageView home, profile, image, story, setting;

    ViewPager pager_post;

    ViewPagerStory adapter;

    CharSequence[] Titles;

    public static Story_view act;

    TabLayout tl;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_story_view);

        act = this;

        home = (ImageView) findViewById(R.id.home_page);
        profile = (ImageView) findViewById(R.id.profile);
        image = (ImageView) findViewById(R.id.image_page);
        story = (ImageView) findViewById(R.id.story);
        setting = (ImageView) findViewById(R.id.setting);
        pager_post = (ViewPager) findViewById(R.id.pager_story);
        tl = (TabLayout)findViewById(R.id.story_tl);

        story.setImageResource(R.drawable.profile_on);

        Titles = new CharSequence[]{"تصاویر", "ویدیو"};

        adapter = new ViewPagerStory(getSupportFragmentManager(), Titles, Titles.length);
        pager_post.setAdapter(this.adapter);
        tl.setupWithViewPager(pager_post);

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

                Intent profile = new Intent(Story_view.this,Profile_page.class);
                startActivity(profile);
                overridePendingTransition(0, 0);
                finish();

            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent image = new Intent(Story_view.this,Post_view.class);
                startActivity(image);
                overridePendingTransition(0, 0);
                finish();

            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent setting = new Intent(Story_view.this,Setting.class);
                startActivity(setting);
                overridePendingTransition(0, 0);
                finish();

            }
        });

        FirebaseCrash.log("log 1");
        System.gc();
    }

}
