package com.niza.app.givder.Utils;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetUnits {
    public static boolean IsNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
