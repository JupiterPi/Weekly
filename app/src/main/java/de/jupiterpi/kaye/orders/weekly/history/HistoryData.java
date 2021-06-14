package de.jupiterpi.kaye.orders.weekly.history;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jupiterpi.tools.files.TextFile;
import jupiterpi.tools.files.csv.CSVObjectsFile;
import jupiterpi.tools.util.ToolsUtil;

public class HistoryData {
    private final String HISTORY_FILENAME = "history.csv";

    private CSVObjectsFile<HistoryEntry> file;

    public HistoryData(Context context) {
        File path = new File(context.getFilesDir(), HISTORY_FILENAME);
        file = new CSVObjectsFile<>(new TextFile(path), HistoryEntry.class);
    }

    public List<HistoryEntry> getEntries() {
        return file.getObjects();
    }

    public String getEntriesToString() {
        List<String> entries = new ArrayList<>();
        for (HistoryEntry entry : getEntries()) {
            entries.add(entry.toString());
        }
        return ToolsUtil.appendWithSeparator(entries, "\n");
    }

    public void addEntry(int seconds) {
        HistoryEntry entry = new HistoryEntry(seconds);
        List<HistoryEntry> entries = getEntries();
        entries.add(entry);
        file.writeObjects(entries);
    }
}