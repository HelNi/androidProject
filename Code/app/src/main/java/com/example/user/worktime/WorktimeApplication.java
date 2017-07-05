package com.example.user.worktime;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.LocaleList;
import android.text.TextUtils;

import java.util.Locale;

/**
 * Created by User on 04.07.2017.
 */

public class WorktimeApplication extends Application {
    // The Android Test devices are (by default) configured as english.
    // This is a workaround to make them display German instead.

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
