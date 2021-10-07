package com.fury.instafull;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.firebase.crash.FirebaseCrash;

import java.io.File;

public class IFrameActivity extends AppCompatActivity {
    WebView wv;

    /* renamed from: shirazwebdevelopers.ehsankarimi.instaautodownloader.IFrameActivity.1 */


    /* renamed from: shirazwebdevelopers.ehsankarimi.instaautodownloader.IFrameActivity.2 */
    class C15572 extends WebViewClient {
        C15572() {
        }

        public void onPageFinished(WebView view, String url) {
            IFrameActivity.this.findViewById(R.id.textView_web).setVisibility(View.GONE);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_iframe);
        this.wv = (WebView) findViewById(R.id.webView1);
        this.wv.getSettings().setJavaScriptEnabled(true);
        String fileName = new File(getIntent().getStringExtra("filePath")).getName();
        this.wv.loadData("<iframe style=\"background-color: #FFFFFF;\" width=\"340\" height=\"460\" src=\"http://instagram.com/p/" + fileName.substring(0, fileName.lastIndexOf(".")) + "/embed\" frameborder=\"0\"></iframe>", "text/html", null);
        this.wv.setWebViewClient(new C15572());


        FirebaseCrash.log("log 1");
        System.gc();
    }

    protected void onPause() {
        super.onPause();
    }

    @SuppressLint({"NewApi"})
    protected void onDestroy() {
        super.onDestroy();
        this.wv.goBack();
        wv.destroy();
    }

    protected void onResume() {
        super.onResume();
    }

    public void onBackPressed() {
        if (this.wv.canGoBack()) {
            this.wv.goBack();
        }
        super.onBackPressed();
    }
}
