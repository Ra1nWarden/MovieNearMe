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
import com.project.movienearme.data.SeatListAdapter;
import com.project.movienearme.ui.SeatSelectionListFragment;

/**
 * An activity for selecting seats.
 */
public final class SeatSelectionActivity extends AppCompatActivity {

    public static String LISTING_ID_KEY = "listingId";
    private int listId;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.seat_selection);
        listId = getIntent().getExtras().getInt(LISTING_ID_KEY);
        SeatSelectionListFragment fragment = (SeatSelectionListFragment)
                getSupportFragmentManager().findFragmentByTag("seat_list");
        MovieManager manager = new MovieManager(this);
        SeatListAdapter adapter = new SeatListAdapter(this, manager.getTotalSeatForCinema(listId)
                , listId);
        fragment.setListAdapter(adapter);
        setUpActionBar();
        TextView cinemaField = (TextView) findViewById(R.id.cinema_name_field);
        cinemaField.setText(manager.getCinemaName(manager.getCinemaIdForListing(listId)));
        TextView movieNameField = (TextView) findViewById(R.id.movie_name_field);
        movieNameField.setText(manager.getMovieTitleForId(manager.getMovieIdForListing(listId)));
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
                SeatSelectionActivity.this.finish();
            }
        });
        ImageButton rightButton = (ImageButton) actionBarView.findViewById(R.id.right_button);
        rightButton.setImageResource(R.drawable.ic_done);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        TextView titleView = (TextView) actionBarView.findViewById(R.id.title_text);
        titleView.setText(R.string.select_seat);
        actionBar.setCustomView(actionBarView);
        actionBar.setDisplayShowCustomEnabled(true);

    }

}
