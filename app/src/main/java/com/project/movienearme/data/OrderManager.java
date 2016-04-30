package com.project.movienearme.data;

import android.content.Context;
import android.database.Cursor;

/**
 * Order manager for managing orders.
 */
public final class OrderManager {

    private DatabaseOpenHelper databaseOpenHelper;

    public OrderManager(Context context) {
        this.databaseOpenHelper = new DatabaseOpenHelper(context);
    }

    public boolean removeOrder(int listingId, String username) {
        return databaseOpenHelper.getWritableDatabase().delete("orders", "username = ? and " +
                "listing_id = ?", new String[]{username, Integer.toString(listingId)}) > 0;
    }
}
