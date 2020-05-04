package com.tiendas3b.ticketdoctor.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.tiendas3b.ticketdoctor.R;
import com.tiendas3b.ticketdoctor.activities.MainActivity;
import com.tiendas3b.ticketdoctor.util.FileUtil;

/**
 * Created by dfa on 23/02/2016.
 */
public class GcmListenerService extends com.google.android.gms.gcm.GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {
        Log.d(TAG, "From: " + from);

        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            handleMessage(data);
        }
    }

    private void handleMessage(Bundle data) {
        String message = data.getString(GcmConstants.DATA_cambiarnombre);
        String phone = data.getString(GcmConstants.DATA_cambiarnombre2);
        FileUtil.writeFile(phone + ": " + message, "cambiar a nombre de archivo de log!!!");
        Log.d(TAG, "Message: " + message);
        Log.d(TAG, "Phone: " + phone);

//        SMS sms = new SMS(phone, message);
////        sms.setTelephoneNumber(phone);
//        Context ctx = getApplicationContext();
//        sms = SmsUtil.saveSMS(ctx, sms);
//        SmsUtil smsUtil = new SmsUtil(ctx);
//        if(sms != null){
//            smsUtil.send(sms);
//        }


        sendNotification(message);
    }

    private void sendNotification(String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_menu_gallery)
                .setContentTitle("Mensaje")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
