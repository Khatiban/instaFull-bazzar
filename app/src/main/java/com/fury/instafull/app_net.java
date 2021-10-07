package com.fury.instafull;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class app_net {

    private static final String TAG = app_net.class.getSimpleName();
    static Context context;

    public static app_net getInstance(Context ctx) {
        context = ctx;
        return null;
    }

    public static boolean isOnline()
    {
        NetworkInfo info = (NetworkInfo) ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info == null)
        {
            return false;
        }
        else
        {
            if(info.isConnected())
            {
                return true;
            }
            else
            {
                return true;
            }

        }
    }
}