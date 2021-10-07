package com.fury.instafull.Suport;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.fury.instafull.Loading.BernoullisProgressView;
import com.fury.instafull.PageStart.views.FlowingGradientClass;
import com.fury.instafull.R;
import com.fury.instafull.app_net;
import com.google.firebase.crash.FirebaseCrash;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fury on 1/29/2017.
 */
public class Suport_me extends Activity {

    String mobile, email, name_user, text, text_kol;

    File root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.suport_me);


        RelativeLayout rl_suport = (RelativeLayout) findViewById(R.id.rl_suport);
        ScrollView sc_suport = (ScrollView) findViewById(R.id.sc_suport);
        final CardView btn_send = (CardView) findViewById(R.id.btn_send);
        final EditText s_phone = (EditText) findViewById(R.id.s_phone);
        final EditText s_email = (EditText) findViewById(R.id.s_email);
        final EditText s_name = (EditText) findViewById(R.id.s_name);
        final EditText payam = (EditText) findViewById(R.id.payam);
        final BernoullisProgressView progress_support = (BernoullisProgressView) findViewById(R.id.progress_support);

        try {
            sc_suport.setVerticalScrollBarEnabled(false);
            sc_suport.setHorizontalScrollBarEnabled(false);
        } catch (Exception e) {
            FirebaseCrash.report(new Exception("2"));
        }


        try {
            Typeface face = Typeface.createFromAsset(getAssets(), "fonts/A-Chamran.ttf");
            s_phone.setTypeface(face);
            s_email.setTypeface(face);
            s_name.setTypeface(face);
            payam.setTypeface(face);
        } catch (Exception e) {
            FirebaseCrash.report(new Exception("3"));
        }


        FlowingGradientClass grad = new FlowingGradientClass();
        grad.setBackgroundResource(R.drawable.translate)
                .onRelativeLayout(rl_suport)
                .setTransitionDuration(4000)
                .start();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd");
        Date now = new Date();
        String hours = String.valueOf(now.getHours());
        String minutes = String.valueOf(now.getMinutes());
        String seconds = String.valueOf(now.getSeconds());
        String fileName = formatter.format(now);
        final String name_now = fileName + "_" + hours + "_" + minutes + "_" + seconds;


        btn_send.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                try {
                    new Handler().postDelayed(new Thread() {

                        @Override
                        public void run() {
                            super.run();
                            btn_send.setCardElevation(20);
                        }
                    }, 200L);
                    btn_send.setCardElevation(15);
                } catch (Exception e) {
                    FirebaseCrash.report(new Exception("on touch . elevation"));
                }
                return false;
            }
        });


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    mobile = s_phone.getText().toString();
                    email = s_email.getText().toString();
                    name_user = s_name.getText().toString();
                    text = payam.getText().toString();
                } catch (Exception e) {
                    FirebaseCrash.report(new Exception("4"));
                    finish();
                }


                if (mobile.length() != 11 && email.length() == 0) {

                    try {
                        s_phone.setTextColor(getResources().getColor(R.color.colorPrimary));
                        s_phone.setHintTextColor(getResources().getColor(R.color.colorPrimary));
                        s_email.setTextColor(getResources().getColor(R.color.colorPrimary));
                        s_email.setHintTextColor(getResources().getColor(R.color.colorPrimary));
                    } catch (Exception e) {
                        FirebaseCrash.report(new Exception("5"));
                        finish();
                    }

                    try {
                        Toast.makeText(getApplicationContext(), "لطفا اطلاعات خود را وارد کنید", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        FirebaseCrash.report(new Exception("6"));
                    }

                } else if (name_user.length() == 0) {
                    try {
                        s_name.setTextColor(getResources().getColor(R.color.colorPrimary));
                        s_name.setHintTextColor(getResources().getColor(R.color.colorPrimary));
                    } catch (Exception e) {
                        FirebaseCrash.report(new Exception("7"));
                    }

                    try {
                        Toast.makeText(getApplicationContext(), "اسم خود را وارد کنید", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        FirebaseCrash.report(new Exception("8"));
                    }

                } else if (text.length() == 0) {
                    try {
                        payam.setTextColor(getResources().getColor(R.color.colorPrimary));
                        payam.setHintTextColor(getResources().getColor(R.color.colorPrimary));
                    } catch (Exception e) {
                        FirebaseCrash.report(new Exception("9"));
                    }

                    try {
                        Toast.makeText(getApplicationContext(), "لطفا پیام خود را وارد کنید", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        FirebaseCrash.report(new Exception("10"));
                    }

                } else {

                    try {
                        StringBuilder result = new StringBuilder();
                        if (email.length() > 0) {
                            result.append(email.charAt(0));
                            for (int i = 1; i < email.length(); i++) {
                                result.append(" ");
                                result.append(email.charAt(i));
                            }
                            email = result.toString();
                        }
                    } catch (Exception e) {
                        FirebaseCrash.report(new Exception("chart email"));
                    }


                    try {
                        text_kol = mobile + " /// " + "=" + email + "=" + " /// " + name_user + " /// " + " /// " + text;
                    } catch (Exception e) {
                        FirebaseCrash.report(new Exception("11"));
                        finish();
                    }

                    try {
                        generateNoteOnSD(getApplicationContext(), name_now, text_kol);
                    } catch (Exception e) {
                        FirebaseCrash.report(new Exception("12"));
                        finish();
                    }

                    progress_support.setVisibility(View.VISIBLE);
                    btn_send.setVisibility(View.GONE);

                    FirebaseCrash.report(new Exception("Messege : " + text_kol));

                    try {
                        new Handler().postDelayed(new Thread() {

                            @Override
                            public void run() {
                                super.run();
                                if (app_net.getInstance(Suport_me.this).isOnline()) {
                                    progress_support.setVisibility(View.GONE);
                                    btn_send.setVisibility(View.VISIBLE);
                                    Toast.makeText(getApplicationContext(), "ارسال شد", Toast.LENGTH_LONG).show();
                                }else {
                                    progress_support.setVisibility(View.GONE);
                                    btn_send.setVisibility(View.VISIBLE);
                                    Toast.makeText(getApplicationContext(), "پیام ذخیره شد بعد از وصل کردن اینترنت خودکار ارسال می شود", Toast.LENGTH_LONG).show();
                                }
                            }
                        }, 2000);
                    } catch (Exception e) {
                        progress_support.setVisibility(View.VISIBLE);
                        FirebaseCrash.log("log 13");
                    }

                }
                FirebaseCrash.log("log 2");
            }
        });

        FirebaseCrash.log("log 1");
        System.gc();
    }


    public void generateNoteOnSD(Context context, String sFileName, String sBody) {
        try {

            root = new File(Environment.getExternalStorageDirectory(), "InstaFull/Support");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            FirebaseCrash.report(new Exception("4"));
        }
    }

}
