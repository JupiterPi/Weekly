package de.jupiterpi.weekly;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesData {
    private final String SHARED_PREFERENCES_NAME = "general_data";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPreferencesData(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    /* time left */

    private final String SHARED_PREFERENCES_TIME_LEFT_KEY = "time_left";
    private final int STANDARD_TIME_LEFT = 7 *60*60 + 0 *60;

    public int readTimeLeft() {
        int timeLeft = sharedPreferences.getInt(SHARED_PREFERENCES_TIME_LEFT_KEY, -1);
        if (timeLeft == -1) {
            timeLeft = STANDARD_TIME_LEFT;
            editor.putInt(SHARED_PREFERENCES_TIME_LEFT_KEY, timeLeft);
            editor.apply();
        }
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        editor.putInt(SHARED_PREFERENCES_TIME_LEFT_KEY, timeLeft);
        editor.apply();
    }
}