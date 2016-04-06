package com.project.movienearme.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.project.movienearme.R;
import com.project.movienearme.data.MovieListAdapter;
import com.project.movienearme.data.MovieManager;
import com.project.movienearme.data.NavigationListAdapter;
import com.project.movienearme.data.ProfileListAdapter;
import com.project.movienearme.data.UserManager;
import com.project.movienearme.ui.MovieListFragment;
import com.project.movienearme.ui.UserLoginFragment;

/**
 * Home activity for the app.
 */
public final class HomeActivity extends AppCompatActivity implements AdapterView
        .OnItemClickListener, LocationListener {

    private ListView navigationList;
    private DrawerLayout drawer;
    private UserManager userManager;
    private ImageButton addButton;
    private TextView titleView;
    private NavigationListAdapter adapter;
    private Location lastLocation;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.home);
        setUpActionBar();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationList = (ListView) findViewById(R.id.left_drawer);
        navigationList.setOnItemClickListener(this);
        userManager = new UserManager(this);
        adapter = new NavigationListAdapter(this);
        navigationList.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        if (locationManager != null) {
            lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        if (!userManager.userLoggedIn()) {
            UserLoginFragment fragment = new UserLoginFragment();
            fragment.show(getSupportFragmentManager(), UserLoginFragment.TAG);
        }
        goToHomeView();
    }

    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        LayoutInflater inflater = LayoutInflater.from(this);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        View actionBarView = inflater.inflate(R.layout.action_bar, null);
        actionBar.setCustomView(actionBarView);
        actionBar.setDisplayShowCustomEnabled(true);
        addButton = (ImageButton) actionBarView.findViewById(R.id.right_button);
        addButton.setImageResource(R.drawable.ic_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(HomeActivity.this, AddMovieActivity.class);
                startActivity(addIntent);
            }
        });
        ImageButton menuButton = (ImageButton) actionBarView.findViewById(R.id.left_button);
        menuButton.setImageResource(R.drawable.ic_menu);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(navigationList)) {
                    drawer.closeDrawer(navigationList);
                } else {
                    drawer.openDrawer(navigationList);
                }
            }
        });
        titleView = (TextView) actionBarView.findViewById(R.id.title_text);
    }

    public void showAddButton() {
        if (userManager.getLoggedInUserAdmin()) {
            addButton.setVisibility(View.VISIBLE);
            addButton.setEnabled(true);
        } else {
            addButton.setVisibility(View.INVISIBLE);
            addButton.setEnabled(false);
        }
    }

    public void goToHomeView() {
        MovieManager manager = new MovieManager(this);
        MovieListFragment movie_list_fragment = new MovieListFragment();
        movie_list_fragment.setListAdapter(new MovieListAdapter(this, manager
                .getAllMoviesCursor()));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, movie_list_fragment)
                .commit();
        titleView.setText((String) adapter.getItem(0));
        showAddButton();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 3) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle(R.string.log_out)
                    .setMessage(R.string.confirm_log_out)
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            userManager.logout();
                            UserLoginFragment loginFragment = new UserLoginFragment();
                            loginFragment.show(getSupportFragmentManager(), UserLoginFragment.TAG);
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else if (position == 2) {
            ListFragment profileList = new ListFragment();
            profileList.setListAdapter(new ProfileListAdapter(this));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, profileList)
                    .commit();
            titleView.setText((String) adapter.getItem(position));
            addButton.setVisibility(View.INVISIBLE);
            addButton.setEnabled(false);
        } else if (position == 0) {
            goToHomeView();
        } else if (position == 1) {
            addButton.setVisibility(View.INVISIBLE);
            addButton.setEnabled(false);
        }
        drawer.closeDrawer(navigationList);
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            lastLocation = location;
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
