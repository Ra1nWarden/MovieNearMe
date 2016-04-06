package com.project.movienearme.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.project.movienearme.R;
import com.project.movienearme.data.MovieManager;
import com.project.movienearme.data.TimeListAdapter;
import com.project.movienearme.ui.TimeListFragment;

/**
 * Activity for selecting showing time for a movie.
 */
public final class MovieDetailsTimeActivity extends AppCompatActivity {

    public static String CINEMA_ID_KEY = "cinemaId";
    private int movieId;
    private int cinemaId;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.time_selection);
        setUpActionBar();
        MovieManager manager = new MovieManager(this);
        Bundle args = getIntent().getExtras();
        movieId = args.getInt(MovieDetailsCinemaActivity.MOVIE_ID_KEY);
        cinemaId = args.getInt(MovieDetailsTimeActivity.CINEMA_ID_KEY);
        TextView titleView = (TextView) findViewById(R.id.title_box);
        titleView.setText(manager.getMovieTitleForId(movieId));
        TextView contentView = (TextView) findViewById(R.id.content_box);
        contentView.setText(manager.getMovieDescriptionForId(movieId));
        TextView cinemaNameView = (TextView) findViewById(R.id.cinema_name_field);
        cinemaNameView.setText(manager.getCinemaName(cinemaId));
        TextView cinemaAddressView = (TextView) findViewById(R.id.cinema_address_field);
        cinemaAddressView.setText(manager.getCinemaAddress(cinemaId));

        TimeListFragment listFragment = (TimeListFragment) getSupportFragmentManager()
                .findFragmentByTag("time_list");
        TimeListAdapter adapter = new TimeListAdapter(this, manager.getTimeForListing(cinemaId,
                movieId));
        listFragment.setListAdapter(adapter);
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
                MovieDetailsTimeActivity.this.finish();
            }
        });
        ImageButton rightButton = (ImageButton) actionBarView.findViewById(R.id.right_button);
        rightButton.setEnabled(false);
        rightButton.setVisibility(View.INVISIBLE);
        TextView titleView = (TextView) actionBarView.findViewById(R.id.title_text);
        titleView.setText(R.string.select_time);
        actionBar.setCustomView(actionBarView);
        actionBar.setDisplayShowCustomEnabled(true);

    }

}
