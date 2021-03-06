package com.example.user.worktime;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.example.user.worktime.Backend.BackendApiTokenManager;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.Locale;

/**
 * Created by User on 04.07.2017.
 */

public class WorktimeApplication extends Application {
    // The Android Test devices are (by default) configured as english.
    // This is a workaround to make them display German instead.


    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
        BackendApiTokenManager.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        Resources res = base.getResources();
        Configuration configuration = res.getConfiguration();

        Locale l = new Locale("de", "de");
        l.setDefault(Locale.GERMANY);
        configuration.setLocale(l);

        Context ct = base.createConfigurationContext(configuration);
        res.updateConfiguration(configuration, res.getDisplayMetrics());
        super.attachBaseContext(new ContextWrapper(ct));
    }
}
