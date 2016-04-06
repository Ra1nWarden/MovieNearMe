package com.project.movienearme.ui;

import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.project.movienearme.data.SeatListAdapter;

/**
 * A list of available seats for selection.
 */
public final class SeatSelectionListFragment extends ListFragment {

    @Override
    public void onListItemClick(ListView l, View view, int position, long id) {
        super.onListItemClick(l, view, position, id);
        SeatListAdapter adapter = (SeatListAdapter) getListAdapter();
        adapter.toggle(position);
    }

}
