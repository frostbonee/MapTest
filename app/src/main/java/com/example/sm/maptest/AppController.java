package com.example.sm.maptest;

import android.app.Application;

import com.mmi.LicenceManager;

public class AppController extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LicenceManager.getInstance().setRestAPIKey("q62q6nhnkqqqt4ovt2ig3k5zg82ah557");
        LicenceManager.getInstance().setMapSDKKey("aiarhqq1th4ykrtqsni7jy7z7kln127a");
    }
}
