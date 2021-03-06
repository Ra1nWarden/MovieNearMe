package com.project.movienearme.data;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.movienearme.R;

/**
 * A list adapter for displaying available time slots.
 */
public final class TimeListAdapter extends CursorAdapter {

    private MovieManager manager;

    public TimeListAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        manager = new MovieManager(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.list_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder();
        viewHolder.timeView = (TextView) v.findViewById(R.id.item_title);
        viewHolder.seatCountView = (TextView) v.findViewById(R.id.item_value);
        v.setTag(viewHolder);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        int time = cursor.getInt(cursor.getColumnIndex("show_time"));
        int listingId = cursor.getInt(cursor.getColumnIndex("_id"));
        viewHolder.timeView.setText(String.format("%02d:%02d", time / 60, time % 60));
        viewHolder.listId = listingId;
        viewHolder.seatCountView.setText(String.format("剩下%d张", manager.countMissingSeatForId
                (listingId)));
    }

    public static class ViewHolder {
        public int listId;
        public TextView timeView;
        public TextView seatCountView;
    }
}
