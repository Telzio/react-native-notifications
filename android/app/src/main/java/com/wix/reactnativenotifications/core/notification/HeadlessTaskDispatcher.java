package com.wix.reactnativenotifications.core.notification;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.react.HeadlessJsTaskService;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.jstasks.HeadlessJsTaskConfig;
import com.facebook.react.ReactNativeHost;

import javax.annotation.Nullable;

import static com.wix.reactnativenotifications.Defs.LOGTAG;

public class HeadlessTaskDispatcher extends HeadlessJsTaskService {

  @Override
  protected @Nullable HeadlessJsTaskConfig getTaskConfig(Intent intent) {

    ReactNativeHost host = getReactNativeHost();
    Log.d(LOGTAG, "hasInstance(): " + host.hasInstance());

    Bundle extras = intent.getExtras();
    WritableMap args = Arguments.fromBundle(extras);
    args.putBoolean("rnn_hasRNHost", host.hasInstance());

    if (extras != null) {
      return new HeadlessJsTaskConfig(
          "RNNotificationsDataMessageReceived",
          args,
          30000, // timeout for the task
          true // optional: defines whether or not  the task is allowed in foreground. Default is false
        );
    }
    return null;
  }
}
