package com.project.movienearme.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.project.movienearme.R;
import com.project.movienearme.data.MovieManager;
import com.project.movienearme.data.SeatListAdapter;
import com.project.movienearme.data.UserManager;
import com.project.movienearme.ui.SeatSelectionListFragment;

import java.util.List;

/**
 * An activity for selecting seats.
 */
public final class SeatSelectionActivity extends AppCompatActivity {

    public static String LISTING_ID_KEY = "listingId";
    private int listId;
    private SeatListAdapter adapter;
    private MovieManager movieManager;
    private UserManager userManger;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.seat_selection);
        listId = getIntent().getExtras().getInt(LISTING_ID_KEY);
        SeatSelectionListFragment fragment = (SeatSelectionListFragment)
                getSupportFragmentManager().findFragmentByTag("seat_list");
        movieManager = new MovieManager(this);
        userManger = new UserManager(this);
        adapter = new SeatListAdapter(this, movieManager.getTotalSeatForCinema(listId)
                , listId);
        fragment.setListAdapter(adapter);
        setUpActionBar();
        TextView cinemaField = (TextView) findViewById(R.id.cinema_name_field);
        cinemaField.setText(movieManager.getCinemaName(movieManager.getCinemaIdForListing(listId)));
        TextView movieNameField = (TextView) findViewById(R.id.movie_name_field);
        movieNameField.setText(movieManager.getMovieTitleForId(movieManager.getMovieIdForListing
                (listId)));
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
                final List<Integer> selected = adapter.getSelectedSeats();
                AlertDialog.Builder builder = new AlertDialog.Builder(SeatSelectionActivity.this)
                        .setTitle(R.string.submit_order)
                        .setMessage(String.format("确认预定%d张？", selected.size()))
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (movieManager.reserveTicketsForListing(userManger
                                        .getLoggedInUsername(), selected, listId)) {
                                    String successText = getResources().getString(R.string
                                            .submit_success);
                                    Toast.makeText(SeatSelectionActivity.this, successText, Toast
                                            .LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    Intent homeIntent = new Intent(SeatSelectionActivity.this,
                                            HomeActivity.class);
                                    homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(homeIntent);
                                } else {
                                    String failedText = getResources().getString(R.string
                                            .submit_fail);
                                    Toast.makeText(SeatSelectionActivity.this, failedText, Toast
                                            .LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    ;
                                }
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
            }
        });
        TextView titleView = (TextView) actionBarView.findViewById(R.id.title_text);
        titleView.setText(R.string.select_seat);
        actionBar.setCustomView(actionBarView);
        actionBar.setDisplayShowCustomEnabled(true);

    }

}
