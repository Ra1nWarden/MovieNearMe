package com.project.movienearme.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.project.movienearme.activities.SeatSelectionActivity;
import com.project.movienearme.data.TimeListAdapter;

/**
 * A fragment for listing show times.
 */
public final class TimeListFragment extends ListFragment {

    @Override
    public void onListItemClick(ListView l, View view, int position, long id) {
        super.onListItemClick(l, view, position, id);
        TimeListAdapter.ViewHolder viewHolder = (TimeListAdapter.ViewHolder) view.getTag();
        Bundle args = new Bundle();
        args.putInt(SeatSelectionActivity.LISTING_ID_KEY, viewHolder.listId);
        Intent intent = new Intent(getActivity(), SeatSelectionActivity.class);
        intent.putExtras(args);
        startActivity(intent);
    }
}
