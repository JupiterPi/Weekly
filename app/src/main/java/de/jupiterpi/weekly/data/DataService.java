package de.jupiterpi.weekly.data;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.jupiterpi.weekly.data.legacy_history.LegacyHistoryEntry;
import jupiterpi.tools.files.TextFile;
import jupiterpi.tools.files.csv.CSVObjectsFile;

import static de.jupiterpi.weekly.data.HistoryEntry.Type;

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

    /* lifecycle */

    @Override
    public void onCreate() {
        super.onCreate();
        read();
    }

    @Override
    public void onDestroy() {
        write();
        super.onDestroy();
    }

    /* ----- */

    private final String SHARED_PREFERENCES_NAME = "signature";
    private final String SHARED_PREFERENCES_VERSION_KEY = "version";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private void read() {
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        int version = sharedPreferences.getInt(SHARED_PREFERENCES_VERSION_KEY, 0);

        if (version == 0) {
            SharedPreferences sp = getSharedPreferences("general_data", Context.MODE_PRIVATE);
            timeLeft = sp.getInt("time_left", 25200);

            File path = new File(getFilesDir(), "history.csv");
            CSVObjectsFile<LegacyHistoryEntry> file = new CSVObjectsFile<>(new TextFile(path), LegacyHistoryEntry.class);
            for (LegacyHistoryEntry entry : file.getObjects()) {
                entries.add(new HistoryEntry(entry.getTime(), Type.REMOVED, entry.getSeconds()));
            }
        }

        if (version == 1) {
            timeLeft = sharedPreferences.getInt("time_left", -1);
            if (timeLeft == -1) {
                editor.putInt("time_left", 25200);
                timeLeft = 25200;
            }

            File path = new File(getFilesDir(), "history.csv");
            CSVObjectsFile<HistoryEntry> file = new CSVObjectsFile<>(new TextFile(path), HistoryEntry.class);
            entries.addAll(file.getObjects());
        }
    }

    private void write() {
        editor.putInt("time_left", timeLeft);
        editor.apply();

        File path = new File(getFilesDir(), "history.csv");
        CSVObjectsFile<HistoryEntry> file = new CSVObjectsFile<>(new TextFile(path), HistoryEntry.class);
        file.writeObjects(entries);
    }

    /* data */

    private int timeLeft;
    private List<HistoryEntry> entries = new ArrayList<>();

    public int getTimeLeft() {
        return timeLeft;
    }

    public List<HistoryEntry> getEntries() {
        return entries;
    }
}