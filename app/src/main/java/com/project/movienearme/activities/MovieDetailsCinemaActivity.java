package com.project.movienearme.activities;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.project.movienearme.R;
import com.project.movienearme.data.CinemaListAdapter;
import com.project.movienearme.data.MovieManager;
import com.project.movienearme.ui.CinemaListFragment;

/**
 * An activity that displays details of a movie.
 */
public final class MovieDetailsCinemaActivity extends AppCompatActivity {

    public static final String MOVIE_ID_KEY = "movieId";
    public static final String LAT_KEY = "latitude";
    public static final String LON_KEY = "longitude";
    private int movieId;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.cinema_selection);
        CinemaListFragment fragment = (CinemaListFragment) getSupportFragmentManager()
                .findFragmentByTag("cinema_list");
        MovieManager manager = new MovieManager(this);
        Bundle arguments = getIntent().getExtras();
        movieId = arguments.getInt(MOVIE_ID_KEY);
        TextView titleView = (TextView) findViewById(R.id.title_box);
        titleView.setText(manager.getMovieTitleForId(movieId));
        TextView contentView = (TextView) findViewById(R.id.content_box);
        contentView.setText(manager.getMovieDescriptionForId(movieId));
        CinemaListAdapter adapter;
        if (arguments.containsKey(LAT_KEY) && arguments.containsKey(LON_KEY)) {
            Location location = new Location("");
            location.setLatitude(arguments.getDouble(LAT_KEY));
            location.setLongitude(arguments.getDouble(LON_KEY));
            adapter = new CinemaListAdapter(this, manager.getAllShowingCinemaCursorSortedForId
                    (movieId,
                            arguments.getDouble(LAT_KEY), arguments.getDouble(LON_KEY)), location);
        } else {
            adapter = new CinemaListAdapter(this, manager.getAllShowingCinemaCursorForId(movieId)
                    , null);
        }
        fragment.setListAdapter(adapter);
        setUpActionBar();
    }

    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        LayoutInflater inflater = LayoutInflater.from(this);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        View actionBarView = inflater.inflate(R.layout.action_bar, null);
        ImageButton leftButton = (ImageButton) actionBarView.findViewById(R.id.left_button);
        leftButton.setImageResource(R.drawable.ic_keyboard_arrow_left);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieDetailsCinemaActivity.this.finish();
            }
        });
        ImageButton rightButton = (ImageButton) actionBarView.findViewById(R.id.right_button);
        rightButton.setEnabled(false);
        rightButton.setVisibility(View.INVISIBLE);
        TextView titleView = (TextView) actionBarView.findViewById(R.id.title_text);
        titleView.setText(R.string.select_cinema);
        actionBar.setCustomView(actionBarView);
        actionBar.setDisplayShowCustomEnabled(true);
    }

    public int getMovieId() {
        return movieId;
    }

}
