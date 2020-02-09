package com.anime.cloud.Utils;

import com.google.firebase.database.FirebaseDatabase;

public class FireBaseSing {

    private static FirebaseDatabase mDatabase;

    public static synchronized FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }

        return mDatabase;
    }
}
