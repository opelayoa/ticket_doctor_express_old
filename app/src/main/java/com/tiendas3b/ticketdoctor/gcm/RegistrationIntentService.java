package com.tiendas3b.ticketdoctor.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.tiendas3b.ticketdoctor.R;
import com.tiendas3b.ticketdoctor.http.HttpAsyncTask;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by dfa on 23/02/2016.
 */
public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};//podrian ser por zonas, regiones, etc

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            Log.d(TAG, "onHandleIntent");
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
//            sendRegistrationToServer(token);

            // Subscribe to topic channels
//            subscribeTopics(token);
            Log.d(TAG, "token:" + token);

            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            sharedPreferences.edit().putBoolean(GcmConstants.SENT_TOKEN_TO_SERVER, true).apply();
            sharedPreferences.edit().putString(GcmConstants.TOKEN, token).apply();
            // [END register_for_gcm]
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            sharedPreferences.edit().putBoolean(GcmConstants.SENT_TOKEN_TO_SERVER, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(GcmConstants.REGISTRATION_COMPLETE);
        Log.d(TAG, "onHandleIntent3");
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(String token) {
        new SendGcmToServerTask(token).execute();
    }

    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
    // [END subscribe_topics]

    private class SendGcmToServerTask extends HttpAsyncTask {

        private final String token;

        public SendGcmToServerTask(String token) {
            this.token = token;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
//                Utils util = new Utils();
                JSONObject data = new JSONObject();
                data.put("token", token);

                //sin SLL
//                URL url = new URL("https://dominio.com");
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //sin SLL

                //SSL beg
                URL urlS = new URL(getString(R.string.ws_borrame_URL, getString(R.string.host)));
                HttpsURLConnection conn = (HttpsURLConnection) urlS.openConnection();
                //SSL end
                OutputStream outputStream = createOutputStream(conn);
                outputStream.write(data.toString().getBytes());

                // Read response.
                InputStream inputStream = conn.getInputStream();
                String resp = IOUtils.toString(inputStream);
                System.out.println(resp);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            status = 1;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            switch (status) {
                case OK:
                    Log.i(TAG, "ok");
                    break;
                case ERROR:
                    Log.i(TAG, "error");
                    break;

                default:
                    break;
            }
        }
    }
}
