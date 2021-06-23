package de.jupiterpi.weekly.data;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.jupiterpi.kaye.orders.weekly.R;
import jupiterpi.tools.files.csv.CSVCastable;
import jupiterpi.tools.util.TimeUtils;
import jupiterpi.tools.util.ToolsUtil;

public class HistoryEntry implements CSVCastable {
    public Date time;
    public Type type;
    public Map<String, String> extra = new HashMap<>();

    public enum Type {
        REMOVED, NEW_WEEK
    }

    public HistoryEntry(Type type) {
        this.time = new Date();
        this.type = type;
    }

    public HistoryEntry(Type type, Map<String, String> extra) {
        this.time = new Date();
        this.type = type;
        this.extra = extra;
    }

    public HistoryEntry(Date time, Type type, Map<String, String> extra) {
        this.time = time;
        this.type = type;
        this.extra = extra;
    }

    private String extraToString() {
        List<String> elements = new ArrayList<>();
        for (String key : extra.keySet()) {
            String value = extra.get(key);
            elements.add(key + "=" + value);
        }
        return ToolsUtil.appendWithSeparator(elements, ",");
    }

    @Override
    public String toString() {
        return String.format("HistoryEntry{time=%s,type=%s%s", TimeUtils.DF_DISPLAY_FULL.format(time), type.toString(), extra.isEmpty() ? "}" : ","+extraToString()+"}");
    }

    public String toStringDisplay(Context context) {
        String str = "";
        str += TimeUtils.DF_DISPLAY_FULL.format(time);
        str += " - ";

        String action = "error";
        switch (type) {
            case REMOVED: action = context.getText(R.string.history_entry_removed).toString(); break;
            case NEW_WEEK: action = context.getText(R.string.history_entry_new_week).toString(); break;
        }
        for (String key : extra.keySet()) {
            System.out.println(" ------------------------------------------------------- " + key + " = " + extra.get(key));
            int time = Integer.parseInt(extra.get(key));
            action = action.replaceAll("\\{" + key + "\\}", TimeUtils.formatDuration(time));
        }
        str += action;

        return str + "\n";
    }

    /* csv */

    public HistoryEntry(String[] f) {
        time = new Date(Long.parseLong(f[0]));
        type = Type.valueOf(f[1]);
        for (String element : f[2].split(",")) {
            String[] parts = element.split("=");
            extra.put(parts[0], parts[1]);
        }
    }

    @Override
    public String[] toCSV() {
        return new String[]{
                Long.toString(time.getTime()),
                type.toString(),
                extraToString()
        };
    }
}