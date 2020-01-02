package com.alinz.parkerdan.shareextension;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import java.io.InputStream;


public class ShareModule extends ReactContextBaseJavaModule implements LifecycleEventListener {


  public ShareModule(ReactApplicationContext reactContext) {
    super(reactContext);
    reactContext.addLifecycleEventListener(this);
    Log.d("RNShareExtention", "create ShareModule: " + reactContext);
  }

  @Override
  public String getName() {
    return "ReactNativeShareExtension";
  }

  @ReactMethod
  public void close() {
    getCurrentActivity().finish();
  }

  @ReactMethod
  public void data(Promise promise) {
    promise.resolve(processIntent());
  }

  public WritableMap processIntent() {
    WritableMap map = Arguments.createMap();

    String value = "";
    String type = "";
    String action = "";

    Activity currentActivity = getCurrentActivity();
    Context currentContext = getReactApplicationContext();
    Log.d("RNShareExtention", "currentActivity: " + currentActivity);
    Log.d("RNShareExtention", "currentContext: " + currentContext);

    if (currentActivity != null) {
      Intent intent = currentActivity.getIntent();
      action = intent.getAction();
      type = intent.getType();

      // log intent
      Log.d("RNShareExtention", "LOG intent extras");
      Log.e("RNShareExtention", "LOG intent extras");
      Log.d("RNShareExtension", "intent action: " + action);
      Log.d("RNShareExtension", "intent type: " + type);
      Bundle bundle = intent.getExtras();
      if (bundle != null) {
        for (String key : bundle.keySet()) {
          Log.e("RNShareExtention", key + " : " + (bundle.get(key) != null ? bundle.get(key) : "NULL"));
        }
      }
      // END OF log intent

      if (type == null) {
        type = "";
      }
      if (Intent.ACTION_SEND.equals(action) && "text/plain".equals(type)) {
        value = intent.getStringExtra(Intent.EXTRA_TEXT);
      }
      else if (Intent.ACTION_SEND.equals(action) && ("image/*".equals(type) || "image/jpeg".equals(type) || "image/png".equals(type) || "image/jpg".equals(type) ) ) {
        Uri uri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        value = "file://" + RealPathUtil.getRealPathFromURI(currentActivity, uri);
      } else {
        value = "";
      }
    } else {
      value = "";
      type = "";
    }

    map.putString("type", type);
    map.putString("value",value);

    return map;
  }

  // Implement LifecycleEventListener

  @Override
  public void onHostResume() {
    Activity currentActivity = getCurrentActivity();
    Context currentContext = getReactApplicationContext();
    Log.d("RNShareExtention", "onHostResume.currentActivity: " + currentActivity);
    Log.d("RNShareExtention", "onHostResume.currentContext: " + currentContext);
  }

  @Override
  public void onHostPause() {

  }

  @Override
  public void onHostDestroy() {

  }
}
