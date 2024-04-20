package com.example.usersafecity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class NetworkChangeReceiver extends BroadcastReceiver {

    public static final String NETWORK_CHANGE_ACTION = "com.example.usersafecity.NETWORK_CHANGE_ACTION";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!isNetworkConnected(context)) {
            //Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();
            Intent networkIntent = new Intent(NETWORK_CHANGE_ACTION);
            networkIntent.putExtra("isConnected", true);
            LocalBroadcastManager.getInstance(context).sendBroadcast(networkIntent);
        }
        else
        {
           // Toast.makeText(context, "Internet is Connected", Toast.LENGTH_LONG).show();
            Intent networkIntent = new Intent(NETWORK_CHANGE_ACTION);
            networkIntent.putExtra("isConnected", false);
            LocalBroadcastManager.getInstance(context).sendBroadcast(networkIntent);
        }
    }

    private boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
        return false;
    }
}
