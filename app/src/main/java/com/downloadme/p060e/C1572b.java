package com.downloadme.p060e;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

/* renamed from: com.downloadme.e.b */
public class C1572b {
    private static C1572b f4727a;

    static {
        f4727a = new C1572b();
    }

    private C1572b() {
    }

    public static C1572b m7634a() {
        return f4727a;
    }

    public String m7638a(Context context) {
        ClipData primaryClip = ((ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE)).getPrimaryClip();
        if (primaryClip != null) {
            return primaryClip.getItemAt(0).getText().toString();
        }
        return "";
    }

    public int m7636a(String str) {
        if (str.startsWith("http://www.instagram.com/p/") || str.startsWith("https://www.instagram.com/p/")) {
            return 0;
        }
        if (str.startsWith("http://www.instagram.com/") || str.startsWith("https://www.instagram.com/")) {
            return 1;
        }
        if (str.startsWith("http") || str.startsWith("https")) {
            return 2;
        }
        return 3;
    }
}
