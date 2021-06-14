package de.jupiterpi.kaye.orders.weekly.history;

import java.util.Date;

import jupiterpi.tools.files.csv.CSVCastable;
import jupiterpi.tools.util.TimeUtils;

public class HistoryEntry implements CSVCastable {
    private Date time;
    private int seconds;

    public HistoryEntry(int seconds) {
        this.time = new Date();
        this.seconds = seconds;
    }

    /* getters */

    public Date getTime() {
        return time;
    }

    public int getSeconds() {
        return seconds;
    }

    @Override
    public String toString() {
        return TimeUtils.DF_DISPLAY_FULL.format(time) + " - " + TimeUtils.formatDuration(seconds);
    }

    /* csv */

    public HistoryEntry(String[] f) {
        time = new Date(Long.parseLong(f[0]));
        seconds = Integer.parseInt(f[1]);
    }

    @Override
    public String[] toCSV() {
        return new String[]{
                Long.toString(time.getTime()),
                Integer.toString(seconds)
        };
    }
}