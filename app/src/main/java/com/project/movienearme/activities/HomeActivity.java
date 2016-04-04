package com.project.movienearme.activities;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.project.movienearme.R;
import com.project.movienearme.data.NavigationListAdapter;
import com.project.movienearme.data.UserManager;
import com.project.movienearme.ui.UserLoginFragment;

/**
 * Home activity for the app.
 */
public final class HomeActivity extends AppCompatActivity {

    private ListView navigationList;
    private DrawerLayout drawer;
    private UserManager userManager;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.home);
        setUpActionBar();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationList = (ListView) findViewById(R.id.left_drawer);
        userManager = new UserManager(this);
        NavigationListAdapter adapter = new NavigationListAdapter(this);
        navigationList.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!userManager.userLoggedIn()) {
            UserLoginFragment fragment = new UserLoginFragment();
            fragment.show(getSupportFragmentManager(), UserLoginFragment.TAG);
        }
    }

    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        LayoutInflater inflater = LayoutInflater.from(this);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        View actionBarView = inflater.inflate(R.layout.action_bar, null);
        actionBar.setCustomView(actionBarView);
        actionBar.setDisplayShowCustomEnabled(true);
    }

    public void toggleDrawer(View v) {
        if (drawer.isDrawerOpen(navigationList)) {
            drawer.closeDrawer(navigationList);
        } else {
            drawer.openDrawer(navigationList);
        }
    }

    public void add(View v) {

    }
}
