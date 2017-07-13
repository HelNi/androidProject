package com.example.user.worktime;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
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

import com.example.user.worktime.Backend.BackendApiTokenManager;
import com.example.user.worktime.Classes.User.User;
import com.example.user.worktime.Factory.IntentFactory;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String FRAGMENT_PROFILE = "frag_profile";
    private static final String FRAGMENT_TIME_TABLE = "frag_time_table";
    private static final String FRAGMENT_ABOUT = "frag_about";
    boolean displayFab;

    // The Current user of the application, returned from the Login action.
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = (User) getIntent().getSerializableExtra("user");
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
        //close the drawer menu if back button is clicked
        FragmentManager fm = getSupportFragmentManager();
        int index = fm.getBackStackEntryCount() - 1;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        //Removes current fragment on back pressed. If the only fragment is the time table page then close the application.
        //frag_time_table is not added to the backStack - only profile and about.
        else if(fm.getBackStackEntryCount() != 0){
            fm.popBackStack();
        } else{
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
        displayFab = false;
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();

        switch (id) {
            case R.id.nav_profile:
                fragment = ProfileFragment.newInstance(mUser);
                fragmentName = FRAGMENT_PROFILE;
                //Add the fragment to the BackStack.
                fm.beginTransaction().replace(R.id.main_layout, fragment, fragmentName).addToBackStack("profile").commit();
                break;
            case R.id.nav_time_table:
                fragment = new TimeTablePagerFragment();
                fragmentName = FRAGMENT_TIME_TABLE;
                //FRAGMENT_TIME_TABLE is not added to the BackStack so it won't get removed onBackPressed()-method.
                fm.beginTransaction().replace(R.id.main_layout, fragment, fragmentName).commit();
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
                //Add the fragment to the BackStack.
                fm.beginTransaction().replace(R.id.main_layout, fragment, fragmentName).addToBackStack("about").commit();
                break;
            default:
                return false;
        }

        //If TIME_TABLE_FRAGMENT is clicked while being in another Activity: GOTO line 135! Otherwise the fab won't show up again.
        if (fm.findFragmentByTag(fragmentName) != null  && (fm.findFragmentByTag(fragmentName).equals(FRAGMENT_TIME_TABLE))) {
            // Fragment is already selected- we don't need to transition twice.
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return false;
        }

        fragment.setEnterTransition(new Fade());
        fragment.setExitTransition(new Fade());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (displayFab) {

            if (!(fragment instanceof View.OnClickListener))
                throw new AssertionError(String.format("Fragment %s must implement OnClickListener or not show The FAB", fragment.getClass().getName()));

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(IntentFactory.createNewEntryCreationIntent(MainActivity.this));
                }
            });
            fab.show();
        } else {
            fab.hide();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logout(MenuItem item) {
        // Clear saved API token
        BackendApiTokenManager.unsetApiToken();

        startActivity(IntentFactory.createLogoutIntent(this));
    }

    /**
     * See IntentFactory.java for further information!
     * @param view the view handed over by the button.
     */
    public void openImprint(View view) {
        startActivity(IntentFactory.createImprintIntent(this));
    }

    /**
     * See IntentFactory.java for further information!
     * @param view the view handed over by the button.
     */
    public void openGitHub(View view) {
        startActivity(IntentFactory.createGitHubIntent(this));
    }

    /**
     * This method is used when the contact-button in the about-fragment is being clicked.
     * The default mail-program will be opened by using an intent.
     * The receiver of the mail is handed over to the mail program as a parameter of the intent.
     * @param view
     */
    public void openMailProgram (View view) {
        Intent sendMail = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("mailto:workTime@example.com?subject=Anfrage%20zur%20WorkTime%20App");
        sendMail.setData(data);
        startActivity(sendMail);
    }
}