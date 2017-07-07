package com.example.user.worktime;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.transition.Explode;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.user.worktime.Classes.User.User;

import org.joda.time.LocalDate;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String FRAGMENT_PROFILE = "frag_profile";
    private static final String FRAGMENT_TIME_TABLE = "frag_time_table";
    private static final String FRAGMENT_ABOUT = "frag_about";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

//        TimeTablePagerFragment initialFragment = new TimeTablePagerFragment();
//        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, initialFragment).commit();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        onNavigationItemSelected(navigationView.getMenu().getItem(1));
        navigationView.getMenu().getItem(1).setChecked(true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment;
        String fragmentName;
        boolean displayFab = false;

        switch (id) {
            case R.id.nav_profile:
                // TODO: This is a temp user; Get the real user from the API later!
                User user = new User(1, "Herr", null, "Peter", "Lustig", "Peter@Baerstadt.de", new LocalDate());

                fragment = ProfileFragment.newInstance(user);
                fragmentName = FRAGMENT_PROFILE;
                break;
            case R.id.nav_time_table:
                fragment = new TimeTablePagerFragment();
                fragmentName = FRAGMENT_TIME_TABLE;
                displayFab = true;
                break;
            case R.id.nav_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return false;
            case R.id.nav_logout:
                return false;
            case R.id.nav_about:
                fragment = new AboutFragment();
                fragmentName = FRAGMENT_ABOUT;
                break;
            default:
                Log.e("Error", "onNavigationItemSelected: unknown ID " + id);
                return false;
        }

        if (getSupportFragmentManager().findFragmentByTag(fragmentName) != null) {
            // Fragment is already selected- we don't need to transition twice.
            return false;
        }

        fragment.setEnterTransition(new Fade());
        fragment.setExitTransition(new Fade());
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        if (displayFab) {
            if (!(fragment instanceof View.OnClickListener))
                throw new AssertionError(String.format("Fragment %s must implement OnClickListener or not show The FAB", fragment.getClass().getName()));

            fab.setOnClickListener((View.OnClickListener) fragment);
            fab.show();
        }
        else
    {
        fab.hide();
    }

    getSupportFragmentManager().

    beginTransaction().

    replace(R.id.main_layout, fragment, fragmentName).

    commit();

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
}

    public void logout(MenuItem item) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /**
     * This method is used by the about.xml and opens the URL with the example-imprint.
     * @param view the view handed over by the button.
     */
    public void openImprint(View view) {
        Uri uri = Uri.parse("https://www.grumpycats.com/about"); // missing 'http://' will cause a crash!
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    /**
     * This method is used by the about.xml and opens the URL with the example-imprint.
     * @param view the view handed over by the button.
     */
    public void openGitHub(View view) {
        Uri uri = Uri.parse("https://github.com/HelNi/androidProject.git"); // missing 'http://' will cause a crash!
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
