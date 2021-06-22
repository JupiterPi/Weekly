package de.jupiterpi.weekly;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import de.jupiterpi.kaye.orders.weekly.R;
import de.jupiterpi.weekly.data.DataService;
import de.jupiterpi.weekly.data.HistoryEntry;

public class LastWeekActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_week);

        initializeOptionsMenu();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initializeDataService();
    }

    @Override
    protected void onStop() {
        if (connection != null) unbindService(connection);
        super.onStop();
    }

    /* options menu / toolbar */

    public void initializeOptionsMenu() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle(R.string.last_week_title);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /* service */

    private ServiceConnection connection = null;
    private DataService dataService = null;

    public void initializeDataService() {
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
    }
}