package com.meest;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import android.os.Bundle;

public class AppLifecycleHandler implements
        Application.ActivityLifecycleCallbacks, ComponentCallbacks2 {

    LifeCycleDelegate lifeCycleDelegate;
    boolean appInForeground = false;

    public AppLifecycleHandler(LifeCycleDelegate lifeCycleDelegate) {
        this.lifeCycleDelegate = lifeCycleDelegate;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (!appInForeground) {
            appInForeground = true;
            lifeCycleDelegate.onAppForegrounded();
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    @Override
    public void onTrimMemory(int level) {
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            // lifecycleDelegate instance was passed in on the constructor
            appInForeground = false;
            lifeCycleDelegate.onAppBackgrounded();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

    }

    @Override
    public void onLowMemory() {

    }
}
