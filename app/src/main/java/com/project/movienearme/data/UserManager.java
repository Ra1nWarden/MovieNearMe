package com.project.movienearme.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * A class for managing user.
 */
public final class UserManager {

    private DatabaseOpenHelper databaseOpenHelper;
    private SharedPreferences preferences;
    private static final String USERNAME_KEY = "userId";
    private static final String ADMIN_KEY = "admin";
    private static final String NO_LOGGED_IN_USER = "";
    private static final String TAG = "UserManager";

    public UserManager(Context context) {
        this.databaseOpenHelper = new DatabaseOpenHelper(context);
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getLoggedInUsername() {
        return preferences.getString(USERNAME_KEY, NO_LOGGED_IN_USER);
    }

    public boolean getLoggedInUserAdmin() {
        return preferences.getBoolean(ADMIN_KEY, false);
    }

    public boolean userLoggedIn() {
        return !preferences.getString(USERNAME_KEY, NO_LOGGED_IN_USER).equals(NO_LOGGED_IN_USER);
    }

    public boolean logUserIn(String username, String password) {
        Cursor cursor = databaseOpenHelper.getReadableDatabase().rawQuery("select " +
                "* from users where username = ? and password = ?", new String[]{username,
                password});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(USERNAME_KEY, username);
            editor.putBoolean(ADMIN_KEY, cursor.getInt(cursor.getColumnIndex("admin")) == 1);
            editor.commit();
            return true;
        }
        return false;
    }

    public void register(String userName, String password, String realName, String phoneNumber) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", userName);
        contentValues.put("password", password);
        contentValues.put("real_name", realName);
        contentValues.put("phone_number", phoneNumber);
        databaseOpenHelper.getWritableDatabase().insert("users", null, contentValues);
    }

    public void logout() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USERNAME_KEY, NO_LOGGED_IN_USER)
                .commit();
    }

    public String getLoggedInUserField(String fieldName) {
        return getUserFieldForUsername(fieldName, getLoggedInUsername());
    }

    public String getUserFieldForUsername(String fieldName, String username) {
        SQLiteDatabase database = databaseOpenHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from users where username = ?", new
                String[]{username});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex(fieldName));
        } else {
            if (Log.isLoggable(TAG, Log.ERROR)) {
                Log.e(TAG, "No item found in database.");
            }
            return null;
        }
    }
}
