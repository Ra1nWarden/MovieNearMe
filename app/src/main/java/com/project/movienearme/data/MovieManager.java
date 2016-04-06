package com.project.movienearme.data;

import android.content.Context;
import android.database.Cursor;

/**
 * A class for managing movies.
 */
public final class MovieManager {

    private static final String TAG = "MovieManager";
    private DatabaseOpenHelper database;

    public MovieManager(Context context) {
        database = new DatabaseOpenHelper(context);
    }

    public Cursor getAllMoviesCursor() {
        return database.getReadableDatabase().rawQuery("select * from movies", new String[]{});
    }

    public Cursor getAllShowingCinemaCursorSortedForId(int movieId, double latitude, double
            longitude) {
        return database.getReadableDatabase().rawQuery("select distinct cinema.cinema_id as _id, " +
                "cinema.cinema_name, cinema.latitude, cinema.longitude from listings join cinema " +
                "on listings.cinema_id = cinema.cinema_id where listings.movie_id = ? order by " +
                "(cinema.latitude - ?) * (cinema.latitude - ?) + (cinema.longitude - ?) * (cinema" +
                ".longitude - ?) asc", new String[]{Integer.toString(movieId), Double.toString
                (latitude), Double.toString(latitude), Double.toString(longitude), Double
                .toString(longitude)});
    }

    public Cursor getAllShowingCinemaCursorForId(int movieId) {
        return database.getReadableDatabase().rawQuery("select distinct cinema.cinema_id as _id, " +
                "cinema.cinema_name, cinema.latitude, cinema.longitude from listings join cinema " +
                "on listings.cinema_id = cinema.cinema_id where listings.movie_id = ?", new
                String[]{Integer.toString(movieId)});
    }

    public String getMovieTitleForId(int id) {
        Cursor cursor = database.getReadableDatabase().rawQuery("select * from movies where _id =" +
                " ?", new String[]{Integer.toString(id)});
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex("movie_name"));
    }

    public String getMovieDescriptionForId(int id) {
        Cursor cursor = database.getReadableDatabase().rawQuery("select * from movies where _id =" +
                " ?", new String[]{Integer.toString(id)});
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex("movie_description"));
    }

    public String getCinemaName(int id) {
        Cursor cursor = database.getReadableDatabase().rawQuery("select * from cinema where " +
                "cinema_id = ?", new String[]{Integer.toString(id)});
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex("cinema_name"));
    }

    public String getCinemaAddress(int id) {
        Cursor cursor = database.getReadableDatabase().rawQuery("select * from cinema where " +
                "cinema_id = ?", new String[]{Integer.toString(id)});
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex("cinema_address"));
    }

    public int countMissingSeatForId(int listingId) {
        Cursor cursor = database.getReadableDatabase().rawQuery("select * from listings where _id" +
                " = ?", new String[]{Integer.toString(listingId)});
        cursor.moveToFirst();
        int cinemaId = cursor.getInt(cursor.getColumnIndex("cinema_id"));
        cursor.close();
        cursor = database.getReadableDatabase().rawQuery("select * from cinema where " +
                "cinema_id = ?", new String[]{Integer.toString(cinemaId)});
        cursor.moveToFirst();
        int total = cursor.getInt(cursor.getColumnIndex("total_seats"));
        cursor.close();
        cursor = database.getReadableDatabase().rawQuery("select count(*) as total from " +
                "orders where listing_id = ?", new String[]{Integer.toString(listingId)});
        cursor.moveToFirst();
        int ordered = cursor.getInt(cursor.getColumnIndex("total"));
        return total - ordered;
    }

    public Cursor getTimeForListing(int cinemaId, int movieId) {
        return database.getReadableDatabase().rawQuery("select * from listings where cinema_id" +
                " = ? and movie_id = ?", new String[]{Integer.toString(cinemaId), Integer
                .toString(movieId)});
    }
}