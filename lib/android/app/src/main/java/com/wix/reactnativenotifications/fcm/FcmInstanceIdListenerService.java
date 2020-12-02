package com.wix.reactnativenotifications.fcm;

import android.os.Bundle;
import android.util.Log;
import android.content.Intent;

import java.util.Map;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.wix.reactnativenotifications.BuildConfig;
import com.wix.reactnativenotifications.core.notification.IPushNotification;
import com.wix.reactnativenotifications.core.notification.PushNotification;
import com.wix.reactnativenotifications.core.notification.HeadlessTaskDispatcher;

import static com.wix.reactnativenotifications.Defs.LOGTAG;

/**
 * Instance-ID + token refreshing handling service. Contacts the FCM to fetch the updated token.
 *
 * @author amitd
 */
public class FcmInstanceIdListenerService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage message){
        Bundle bundle = message.toIntent().getExtras();
        if(BuildConfig.DEBUG) Log.d(LOGTAG, "New message from FCM: " + bundle);

        if(message.getNotification() == null && !message.getData().isEmpty()) {
            //This is a data only message, dispatch it to a headless task
            if(BuildConfig.DEBUG) Log.d(LOGTAG, "Message is data only: " + bundle);

            Intent service = new Intent(getApplicationContext(), HeadlessTaskDispatcher.class);
            service.putExtras(bundle);
            getApplicationContext().startService(service);
        }
        else {
            try {
                final IPushNotification notification = PushNotification.get(getApplicationContext(), bundle);
                notification.onReceived();
            } catch (IPushNotification.InvalidNotificationException e) {
                // An FCM message, yes - but not the kind we know how to work with.
                if(BuildConfig.DEBUG) Log.v(LOGTAG, "FCM message handling aborted", e);
            }
        }
    }
}
