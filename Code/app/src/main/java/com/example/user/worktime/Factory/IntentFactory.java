package com.example.user.worktime.Factory;

import android.content.Intent;
import android.net.Uri;

import com.example.user.worktime.LoginActivity;
import com.example.user.worktime.MainActivity;
import com.example.user.worktime.TimeTableEntryCreationActivity;

/**
 * Class for creating Intents
 */

public class IntentFactory {

    private static final String GIT_HUB_LINK = "https://github.com/HelNi/androidProject.git"; // missing 'http://' will cause a crash!
    private static final String IMPRINT_LINK = "https://www.grumpycats.com/about"; // missing 'http://' will cause a crash!

    public static Intent createNewEntryCreationIntent(MainActivity mainActivity) {
        Intent intent = new Intent(mainActivity, TimeTableEntryCreationActivity.class);
        mainActivity.startActivity(intent);
        return intent;
    }

    public static Intent createGitHubIntent() {
        Uri uri = Uri.parse(GIT_HUB_LINK);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        return intent;
    }

    public static Intent createImprintIntent() {
        Uri uri = Uri.parse(IMPRINT_LINK);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        return intent;
    }

    public static Intent createLogoutIntent(MainActivity mainActivity) {
        Intent intent = new Intent(mainActivity, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }
}
