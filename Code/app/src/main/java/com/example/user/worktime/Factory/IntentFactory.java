package com.example.user.worktime.Factory;

import android.content.Intent;
import android.net.Uri;

import com.example.user.worktime.LoginActivity;
import com.example.user.worktime.MainActivity;
import com.example.user.worktime.R;
import com.example.user.worktime.TimeTableEntryCreationActivity;

/**
 * Class for creating Intents
 */

public class IntentFactory {

    private static final String GIT_HUB_LINK = ""; // missing 'http://' will cause a crash!
    private static final String IMPRINT_LINK = "https://www.grumpycats.com/about"; // missing 'http://' will cause a crash!

    public static Intent createNewEntryCreationIntent(MainActivity mainActivity) {
        Intent intent = new Intent(mainActivity, TimeTableEntryCreationActivity.class);
        mainActivity.startActivity(intent);
        return intent;
    }

    /**
     * This method is used by the about.xml and opens the URL with the example-imprint in the default web-browser.
     * @param mainActivity urls.xml is only accessible from the MainActivity.
     * @return
     */
    public static Intent createGitHubIntent(MainActivity mainActivity) {
        Uri uri = Uri.parse(mainActivity.getString(R.string.github_link));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        return intent;
    }

    /**
     * This method is used by the about.xml and opens the URL with the example-imprint.
     * @param mainActivity urls.xml is only accessible from the MainActivity.
     * @return
     */
    public static Intent createImprintIntent(MainActivity mainActivity) {
        Uri uri = Uri.parse(mainActivity.getString(R.string.imprint_link));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        return intent;
    }

    public static Intent createLogoutIntent(MainActivity mainActivity) {
        Intent intent = new Intent(mainActivity, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }
}