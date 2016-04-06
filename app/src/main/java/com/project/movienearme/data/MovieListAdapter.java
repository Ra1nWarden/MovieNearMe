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
 * A list adapter for showing all movies.
 */
public final class MovieListAdapter extends CursorAdapter {

    public MovieListAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.list_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder();
        viewHolder.movieNameText = (TextView) v.findViewById(R.id.item_title);
        viewHolder.movieCastText = (TextView) v.findViewById(R.id.item_value);
        v.setTag(viewHolder);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.movieId = cursor.getInt(cursor.getColumnIndex("_id"));
        holder.movieCastText.setText(cursor.getString(cursor.getColumnIndex("cast")));
        holder.movieNameText.setText(cursor.getString(cursor.getColumnIndex("movie_name")));
    }

    public static class ViewHolder {
        public TextView movieNameText;
        public TextView movieCastText;
        public int movieId;
    }
}
