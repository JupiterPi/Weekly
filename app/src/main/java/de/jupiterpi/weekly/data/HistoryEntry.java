package de.jupiterpi.weekly.data;

import java.util.Date;

import jupiterpi.tools.files.csv.CSVCastable;
import jupiterpi.tools.util.TimeUtils;

public class HistoryEntry implements CSVCastable {
    public Date time;
    public Type type;
    public int extra;

    public enum Type {
        REMOVED, ADDED, NEW_WEEK, TRANSFERRED_FROM_LAST_WEEK
    }

    public HistoryEntry(Type type) {
        this.time = new Date();
        this.type = type;
    }

    public HistoryEntry(Type type, int extra) {
        this.time = new Date();
        this.type = type;
        this.extra = extra;
    }

    public HistoryEntry(Date time, Type type, int extra) {
        this.time = time;
        this.type = type;
        this.extra = extra;
    }

    @Override
    public String toString() {
        return String.format("HistoryEntry{time=%s,type=%s%s", TimeUtils.DF_DISPLAY_FULL.format(time), type.toString(), extra > 0 ? ","+extra : "");
    }

    /* csv */

    public HistoryEntry(String[] f) {
        time = new Date(Long.parseLong(f[0]));
        type = Type.valueOf(f[1]);
        extra = Integer.parseInt(f[2]);
    }

    @Override
    public String[] toCSV() {
        return new String[]{
                Long.toString(time.getTime()),
                type.toString(),
                Integer.toString(extra)
        };
    }
}