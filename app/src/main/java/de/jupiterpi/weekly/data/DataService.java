package de.jupiterpi.weekly.data;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.util.TimeUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        checkNewWeek();
    }

    @Override
    public void onDestroy() {
        System.out.println("--------------------------------------- destroyed");
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

        System.out.println("------------------------------------ version: " + version);

        if (version == 0) {
            SharedPreferences sp = getSharedPreferences("general_data", Context.MODE_PRIVATE);
            timeLeft = sp.getInt("time_left", 25200);

            File path = new File(getFilesDir(), "history.csv");
            CSVObjectsFile<LegacyHistoryEntry> file = new CSVObjectsFile<>(new TextFile(path), LegacyHistoryEntry.class);
            for (LegacyHistoryEntry entry : file.getObjects()) {
                Map<String, String> extra = new HashMap<>();
                extra.put("amount", Integer.toString(entry.getSeconds()));
                entries.add(new HistoryEntry(entry.getTime(), Type.REMOVED, extra));
            }
        }

        if (version == 1) {
            timeLeft = sharedPreferences.getInt("time_left", -1);
            if (timeLeft <= 0) {
                editor.putInt("time_left", 25200);
                timeLeft = 25200;
            }

            File path = new File(getFilesDir(), "history.csv");
            CSVObjectsFile<HistoryEntry> file = new CSVObjectsFile<>(new TextFile(path), HistoryEntry.class);
            for (String[] parts : file.get()) {
                entries.add(new HistoryEntry(new String[]{
                        parts[0], parts[1], "amount=" + parts[2]
                }));
            }
        }

        if (version == 2) {
            timeLeft = sharedPreferences.getInt("time_left", -1);
            if (timeLeft <= 0) {
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
        editor.putInt("version", 2);
        editor.apply();

        File path = new File(getFilesDir(), "history.csv");
        CSVObjectsFile<HistoryEntry> file = new CSVObjectsFile<>(new TextFile(path), HistoryEntry.class);
        file.writeObjects(entries);
    }

    /* data */

    private int timeLeft;
    private List<HistoryEntry> entries = new ArrayList<>();

    private void checkNewWeek() {
        HistoryEntry lastEntry = entries.get(entries.size()-1);
        int lastDay = Integer.parseInt(new SimpleDateFormat("u").format(lastEntry.time.getTime()));
        int day = Integer.parseInt(new SimpleDateFormat("u").format(new Date()));
        if (day < lastDay) {
            Map<String, String> extra = new HashMap<>();
            extra.put("last_week_time_left", Integer.toString(timeLeft));
            extra.put("new_week_time_left", Integer.toString(25200));
            entries.add(new HistoryEntry(Type.NEW_WEEK, extra));

            timeLeft = 25200;
        }
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public List<HistoryEntry> getEntries() {
        return entries;
    }

    public int remove(int seconds) {
        timeLeft = getTimeLeft() - seconds;

        Map<String, String> extra = new HashMap<>();
        extra.put("amount", Integer.toString(seconds));
        entries.add(new HistoryEntry(Type.REMOVED, extra));

        return getTimeLeft();
    }
}