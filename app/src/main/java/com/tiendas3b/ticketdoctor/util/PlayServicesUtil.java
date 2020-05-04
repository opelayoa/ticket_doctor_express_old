package com.tiendas3b.ticketdoctor.util;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.tiendas3b.ticketdoctor.activities.MainActivity;

import okhttp3.internal.Util;

/**
 * Created by dfa on 23/02/2016.
 */
public class PlayServicesUtil {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = Util.class.getName();

    public static boolean checkPlayServices(Context context) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);
        if (context instanceof MainActivity && resultCode != ConnectionResult.SUCCESS) {
            MainActivity activity = (MainActivity) context;
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(activity, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                activity.finish();
            }
            return false;
        }
        return true;
    }
}
