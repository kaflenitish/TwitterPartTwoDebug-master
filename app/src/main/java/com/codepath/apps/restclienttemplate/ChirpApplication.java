package com.codepath.apps.restclienttemplate;

import android.app.Application;
import android.content.Context;

import com.codepath.apps.restclienttemplate.models.MyDatabase;

import androidx.room.Room;

/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
 *
 *     ChirpClient client = ChirpApplication.getRestClient(Context context);
 *     // use client to send requests to API
 *
 */
public class ChirpApplication extends Application {

    MyDatabase myDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        // when upgrading versions, kill the original tables by using
		// fallbackToDestructiveMigration()
        myDatabase = Room.databaseBuilder(this, MyDatabase.class,
                MyDatabase.NAME).fallbackToDestructiveMigration().build();
    }

    public static ChirpClient getRestClient(Context context) {
        return (ChirpClient) ChirpClient.getInstance(ChirpClient.class, context);
    }

    public MyDatabase getMyDatabase() {
        return myDatabase;
    }
}