package com.project.movienearme.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.project.movienearme.activities.MovieDetailsCinemaActivity;
import com.project.movienearme.activities.MovieDetailsTimeActivity;
import com.project.movienearme.data.CinemaListAdapter;

/**
 * A fragment for listing available cinema.
 */
public final class CinemaListFragment extends ListFragment {

    @Override
    public void onListItemClick(ListView l, View view, int position, long id) {
        super.onListItemClick(l, view, position, id);
        Intent intent = new Intent(getActivity(), MovieDetailsTimeActivity.class);
        Bundle args = new Bundle();
        args.putInt(MovieDetailsCinemaActivity.MOVIE_ID_KEY, ((MovieDetailsCinemaActivity)
                getActivity()).getMovieId());
        CinemaListAdapter.ViewHolder viewHolder = (CinemaListAdapter.ViewHolder) view.getTag();
        args.putInt(MovieDetailsTimeActivity.CINEMA_ID_KEY, viewHolder.cinemaId);
        intent.putExtras(args);
        startActivity(intent);
    }
}
