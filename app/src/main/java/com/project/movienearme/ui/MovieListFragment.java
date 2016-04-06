package com.project.movienearme.ui;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.project.movienearme.activities.HomeActivity;
import com.project.movienearme.activities.MovieDetailsCinemaActivity;
import com.project.movienearme.data.MovieListAdapter;

/**
 * A fragment for displaying list of movie.
 */
public final class MovieListFragment extends ListFragment {

    @Override
    public void onListItemClick(ListView l, View view, int position, long id) {
        super.onListItemClick(l, view, position, id);
        MovieListAdapter.ViewHolder viewHolder = (MovieListAdapter.ViewHolder) view.getTag();
        int movieId = viewHolder.movieId;
        Location location = ((HomeActivity) getActivity()).getLastLocation();
        Intent movieDetails = new Intent(getActivity(), MovieDetailsCinemaActivity.class);
        Bundle args = new Bundle();
        args.putInt(MovieDetailsCinemaActivity.MOVIE_ID_KEY, movieId);
        if (location != null) {
            args.putDouble(MovieDetailsCinemaActivity.LAT_KEY, location.getLatitude());
            args.putDouble(MovieDetailsCinemaActivity.LON_KEY, location.getLongitude());
        }
        movieDetails.putExtras(args);
        startActivity(movieDetails);
    }
}
