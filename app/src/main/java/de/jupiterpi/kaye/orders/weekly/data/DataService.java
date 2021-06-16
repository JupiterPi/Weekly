package de.jupiterpi.kaye.orders.weekly.data;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;

public class DataService extends Service {
    private IBinder binder = new DataBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class DataBinder extends Binder {
        public DataService getService() {
            return DataService.this;
        }
    }

    /* ----- */

    private final String SHARED_PREFERENCES_NAME = "signature";
    private final String SHARED_PREFERENCES_VERSION_KEY = "version";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreate() {
        super.onCreate();

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        int version = sharedPreferences.getInt(SHARED_PREFERENCES_VERSION_KEY, 0);
        System.out.println("--------------- created service (version: " + version + ") ---------------");
    }
}