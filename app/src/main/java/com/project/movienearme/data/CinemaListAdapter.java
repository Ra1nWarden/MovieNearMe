package com.project.movienearme.data;

import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.movienearme.R;

/**
 * A list adapter for displaying list of cinema for a particular movie.
 */
public final class CinemaListAdapter extends CursorAdapter {

    private final Location userLocation;

    public CinemaListAdapter(Context context, Cursor c, Location location) {
        super(context, c, 0);
        this.userLocation = location;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.list_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder();
        viewHolder.cinemaNameText = (TextView) v.findViewById(R.id.item_title);
        viewHolder.distanceText = (TextView) v.findViewById(R.id.item_value);
        v.setTag(viewHolder);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.cinemaId = cursor.getInt(cursor.getColumnIndex("_id"));
        holder.cinemaNameText.setText(cursor.getString(cursor.getColumnIndex("cinema_name")));
        if (userLocation == null) {
            holder.distanceText.setText(R.string.unknown_distance);
        } else {
            Location dest = new Location("");
            dest.setLatitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
            dest.setLongitude(cursor.getDouble(cursor.getColumnIndex("longitude")));
            holder.distanceText.setText(Float.toString(userLocation.distanceTo(dest)) + "米");
        }
    }

    public static class ViewHolder {
        public TextView cinemaNameText;
        public TextView distanceText;
        public int cinemaId;
    }
}
