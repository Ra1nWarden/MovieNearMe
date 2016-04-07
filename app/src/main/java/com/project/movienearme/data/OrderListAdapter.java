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
 * A list adapter for displaying orders.
 */
public final class OrderListAdapter extends CursorAdapter {

    private MovieManager manager;

    public OrderListAdapter(Context context, Cursor c) {
        super(context, c, 0);
        manager = new MovieManager(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.list_item, parent, false);
        final OrderListAdapter.ViewHolder viewHolder = new OrderListAdapter.ViewHolder();
        viewHolder.titleView = (TextView) v.findViewById(R.id.item_title);
        viewHolder.contentView = (TextView) v.findViewById(R.id.item_value);
        v.setTag(viewHolder);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        OrderListAdapter.ViewHolder holder = (OrderListAdapter.ViewHolder) view.getTag();
        int listingId = cursor.getInt(cursor.getColumnIndex("_id"));
        holder.listingId = listingId;
        int time = cursor.getInt(cursor.getColumnIndex("show_time"));
        holder.showTime = time;
        holder.titleView.setText(manager.getMovieTitleForId(manager.getMovieIdForListing
                (listingId)));
        holder.contentView.setText(manager.getCinemaName(manager.getCinemaIdForListing(listingId)
        ) + String.format(" （%02d:%02d）", time / 60, time % 60));
    }

    public static class ViewHolder {
        public TextView titleView;
        public TextView contentView;
        public int listingId;
        public int showTime;
    }
}
