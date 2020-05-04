package com.tiendas3b.ticketdoctor.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.tiendas3b.ticketdoctor.R;

/**
 * Created by dfa on 22/02/2016.
 */
public class NetworkUtil {

    public static boolean isConnectedWithToast(final Context context){
        final boolean isConnected = isConnected(context);
        if(!isConnected){
            Toast.makeText(context, R.string.no_internet, Toast.LENGTH_SHORT).show();
        }
        return isConnected;
    }

    public static boolean isConnected(final Context context){
        final NetworkInfo network = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        final boolean isConnected = network != null && network.isConnected();
        return isConnected;
    }
}
