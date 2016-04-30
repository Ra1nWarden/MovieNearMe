package com.project.movienearme.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A class for managing movies.
 */
public final class MovieManager {

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

    public int getTotalSeatForCinema(int listingId) {
        Cursor cursor = database.getReadableDatabase().rawQuery("select * from listings where _id" +
                " = ?", new String[]{Integer.toString(listingId)});
        cursor.moveToFirst();
        int cinemaId = cursor.getInt(cursor.getColumnIndex("cinema_id"));
        cursor.close();
        cursor = database.getReadableDatabase().rawQuery("select * from cinema where " +
                "cinema_id = ?", new String[]{Integer.toString(cinemaId)});
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex("total_seats"));
    }

    public int countMissingSeatForId(int listingId) {
        int total = getTotalSeatForCinema(listingId);
        Cursor cursor = database.getReadableDatabase().rawQuery("select count(*) as total from " +
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

    public int getCinemaIdForListing(int listingId) {
        Cursor cursor = database.getReadableDatabase().rawQuery("select * from listings where _id" +
                " = ?", new String[]{Integer.toString(listingId)});
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex("cinema_id"));
    }

    public int getMovieIdForListing(int listingId) {
        Cursor cursor = database.getReadableDatabase().rawQuery("select * from listings where _id" +
                " = ?", new String[]{Integer.toString(listingId)});
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex("movie_id"));
    }

    public boolean isSeatAvailableForId(int listingId, int position) {
        Cursor cursor = database.getReadableDatabase().rawQuery("select * from orders where " +
                "listing_id = ? and seat_id = ?", new String[]{Integer.toString(listingId), Integer
                .toString(position)});
        return cursor.getCount() == 0;
    }

    public boolean reserveTicketsForListing(String loggedInUsername, List<Integer> selected, int
            listId) {
        SQLiteDatabase insertDatabase = database.getWritableDatabase();
        for (int each : selected) {
            ContentValues values = new ContentValues();
            values.put("username", loggedInUsername);
            values.put("listing_id", listId);
            values.put("seat_id", each);
            long id = insertDatabase.insert("orders", null, values);
            if (id == -1) {
                return false;
            }
        }
        return true;
    }

    public Cursor getOrdersForUsername(String username) {
        return database.getReadableDatabase().rawQuery("select distinct listing_id as _id, " +
                "movie_id, cinema_id, show_time from orders join listings on orders.listing_id = " +
                "listings._id where username = ?", new String[]{username});
    }

    public Cursor getAllCinema() {
        return database.getReadableDatabase().rawQuery("select cinema_id as _id, cinema_name from" +
                " cinema", new String[]{});
    }

    public List<String> getSeatsForUsernameAndListingId(String username, int listingId) {
        List<String> ret = new ArrayList<>();
        Cursor cursor = database.getReadableDatabase().rawQuery("select * from orders where " +
                "username = ? and listing_id = ?", new String[]{username, Integer.toString
                (listingId)});
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                int seatId = cursor.getInt(cursor.getColumnIndex("seat_id"));
                ret.add(String.format("%d排%d号", seatId / 10 + 1, seatId % 10 + 1));
                cursor.moveToNext();
            }

        }
        return ret;
    }

    public boolean addMovieWithSections(String movieName, String movieDescription, String movieCast,
                                        List<SectionListAdapter.Section> inputSections) {
        ContentValues movie = new ContentValues();
        movie.put("movie_name", movieName);
        movie.put("movie_description", movieDescription);
        movie.put("cast", movieCast);
        long movieId = database.getWritableDatabase().insert("movies", null, movie);
        if (movieId == -1) {
            return false;
        }
        for (SectionListAdapter.Section each : inputSections) {
            ContentValues listing = new ContentValues();
            listing.put("cinema_id", each.cinemaId);
            listing.put("show_time", each.showTime);
            listing.put("movie_id", movieId);
            long listingId = database.getWritableDatabase().insert("listings", null, listing);
            if (listingId == -1) {
                return false;
            }
        }

        return true;
    }
}
