package de.jupiterpi.kaye.orders.weekly;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import de.jupiterpi.kaye.orders.weekly.data.DataService;
import de.jupiterpi.kaye.orders.weekly.data.HistoryEntry;
import de.jupiterpi.kaye.orders.weekly.data.legacy_history.LegacyHistoryData;
import jupiterpi.tools.util.TimeUtils;

public class MainActivity extends AppCompatActivity {
    private SharedPreferencesData data;

    private ServiceConnection connection = null;
    private DataService dataService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                dataService = ((DataService.DataBinder)iBinder).getService();
            }
            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                dataService = null;
            }
        };
        bindService(new Intent(this, DataService.class), connection, BIND_AUTO_CREATE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        data = new SharedPreferencesData(this);
        displayTimeLeft(data.readTimeLeft());
    }

    public void test(View view) {
        System.out.println("------------------- timeLeft: " + dataService.getTimeLeft());
        for (HistoryEntry entry : dataService.getEntries()) {
            System.out.println("------------------- entry: " + entry.toString());
        }
    }

    @Override
    protected void onDestroy() {
        if (connection != null) unbindService(connection);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_history:
                Intent intent = new Intent(this, HistoryActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_last_week:
                ErrorDialog.show(this, getText(R.string.error_not_implemented).toString());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void displayTimeLeft(int timeLeft) {
        TextView timeView = findViewById(R.id.main_time_left);
        timeView.setText(String.format(getText(R.string.main_time_left).toString(), TimeUtils.formatDuration(timeLeft)));
    }

    private void remove(int seconds) {
        int timeLeft = data.readTimeLeft();
        timeLeft -= seconds;
        data.setTimeLeft(timeLeft);
        displayTimeLeft(timeLeft);

        new LegacyHistoryData(this).addEntry(seconds);
    }

    /* dialog */

    private View layout = null;
    private int chosenSeconds = 0;
    private void resetDialog() {
        layout = null;
        chosenSeconds = 0;
    }

    public void showChooseTimeDialog(View view) {
        LayoutInflater inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.layout_choose_time, null);

        new AlertDialog.Builder(this)
                .setView(layout)
                //.setTitle(R.string.chooser_title)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        remove(chosenSeconds);
                        resetDialog();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        resetDialog();
                    }
                })
                .create().show();
    }

    public void add(View view) {
        int minutes = -1;
        for (String str : ((Button) view).getText().toString().split(" ")) {
            try {
                minutes = Integer.parseInt(str);
            } catch (NumberFormatException ignored) {}
        }
        if (minutes == -1) {
            ErrorDialog.show(this);
        }
        chosenSeconds += minutes*60;

        TextView display = view.getRootView().findViewById(R.id.chooser_time_display);
        display.setText(String.format(getText(R.string.chooser_remove_display).toString(), TimeUtils.formatDuration(chosenSeconds)));
    }
}