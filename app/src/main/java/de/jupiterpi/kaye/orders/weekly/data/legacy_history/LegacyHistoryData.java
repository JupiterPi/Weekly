package de.jupiterpi.kaye.orders.weekly.data.legacy_history;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jupiterpi.tools.files.TextFile;
import jupiterpi.tools.files.csv.CSVObjectsFile;
import jupiterpi.tools.util.ToolsUtil;

public class LegacyHistoryData {
    private final String HISTORY_FILENAME = "history.csv";

    private CSVObjectsFile<LegacyHistoryEntry> file;

    public LegacyHistoryData(Context context) {
        File path = new File(context.getFilesDir(), HISTORY_FILENAME);
        file = new CSVObjectsFile<>(new TextFile(path), LegacyHistoryEntry.class);
    }

    public List<LegacyHistoryEntry> getEntries() {
        return file.getObjects();
    }

    public String getEntriesToString() {
        List<String> entries = new ArrayList<>();
        for (LegacyHistoryEntry entry : getEntries()) {
            entries.add(entry.toString());
        }
        return ToolsUtil.appendWithSeparator(entries, "\n");
    }

    public void addEntry(int seconds) {
        LegacyHistoryEntry entry = new LegacyHistoryEntry(seconds);
        List<LegacyHistoryEntry> entries = getEntries();
        entries.add(entry);
        file.writeObjects(entries);
    }
}