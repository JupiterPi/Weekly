package de.jupiterpi.weekly;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;

import de.jupiterpi.kaye.orders.weekly.R;
import de.jupiterpi.weekly.data.legacy_history.LegacyHistoryData;

public class HistoryActivity extends AppCompatActivity {
    private LegacyHistoryData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle(R.string.history_title);

        data = new LegacyHistoryData(this);
        TextView text = findViewById(R.id.history_text);
        String entriesStr = data.getEntriesToString();
        if (!entriesStr.isEmpty()) text.setText(entriesStr);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}