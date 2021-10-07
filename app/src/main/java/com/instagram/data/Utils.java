package com.instagram.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build.VERSION;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video.Media;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.widget.Toast;

import com.fury.instafull.MainActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class Utils {
    public static int TYPE = 0;
    public static final int TYPE_IMAGE = 0;
    public static final int TYPE_VIDEO = 1;
    public static int f99h;
    public static Typeface tf;
    public static int f100w;

    public static void CopyStream(InputStream is, OutputStream os) {
        try {
            byte[] bytes = new byte[AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT];
            while (true) {
                int count = is.read(bytes, TYPE_IMAGE, AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
                if (count != -1) {
                    os.write(bytes, TYPE_IMAGE, count);
                } else {
                    return;
                }
            }
        } catch (Exception e) {
        }
    }

    public static String streamToString(InputStream is) throws IOException {
        String str = "";
        if (is == null) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line);
            }
            reader.close();
            return sb.toString();
        } finally {
            is.close();
        }
    }

    public static void scanMedia(Context mContext, File f, String type) {
        if (VERSION.SDK_INT >= 20) {
            ContentValues values;
            if (type.equals("video/*")) {
                values = new ContentValues();
                values.put("_data", f.getAbsolutePath());
                values.put("mime_type", type);
                values.put("date_added", Long.valueOf(System.currentTimeMillis()));
                mContext.getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, values);
            } else if (type.equals("image/*")) {
                values = new ContentValues();
                values.put("_data", f.getAbsolutePath());
                values.put("mime_type", type);
                values.put("date_added", Long.valueOf(System.currentTimeMillis()));
                mContext.getContentResolver().insert(Images.Media.EXTERNAL_CONTENT_URI, values);
                if (MainActivity.act != null) {
                    MainActivity.act.addImageToList(f.getAbsolutePath());
                }
            }
        } else if (VERSION.SDK_INT >= 19) {
            Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
            mediaScanIntent.setData(Uri.fromFile(f));
            mContext.sendBroadcast(mediaScanIntent);
            if (type.equals("image/*") && MainActivity.act != null) {
                MainActivity.act.addImageToList(f.getAbsolutePath());
            }
        } else {
            mContext.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(f)));
            if (type.equals("image/*") && MainActivity.act != null) {
                MainActivity.act.addImageToList(f.getAbsolutePath());
            }
        }
    }

    public static void scanDeletedMedia(Context c, File f, String type) {
        if (VERSION.SDK_INT >= 20) {
            String[] strArr;
            if (type.equals("video/*")) {
                strArr = new String[TYPE_VIDEO];
                strArr[TYPE_IMAGE] = f.getAbsolutePath();
                c.getContentResolver().delete(Media.EXTERNAL_CONTENT_URI, "_data= ?", strArr);
                return;
            }
            strArr = new String[TYPE_VIDEO];
            strArr[TYPE_IMAGE] = f.getAbsolutePath();
            c.getContentResolver().delete(Images.Media.EXTERNAL_CONTENT_URI, "_data= ?", strArr);
        } else if (VERSION.SDK_INT >= 19) {
            Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
            mediaScanIntent.setData(Uri.fromFile(f));
            c.sendBroadcast(mediaScanIntent);
        } else {
            c.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED", Uri.fromFile(f)));
        }
    }

    public static String getFileExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + TYPE_VIDEO, fileName.length());
    }

    public static void showToast(Context mContext, String string) {
        Toast.makeText(mContext, string, Toast.LENGTH_SHORT).show();
    }

    public static float convertDpToPixel(float dp, Context context) {
        return dp * (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f);
    }
}
