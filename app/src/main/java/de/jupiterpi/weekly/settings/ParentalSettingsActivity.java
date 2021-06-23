package de.jupiterpi.weekly.settings;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import de.jupiterpi.kaye.orders.weekly.R;

public class ParentalSettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parental_settings);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_preferences_fragment_view, new SettingsFragment())
                .commit();
        initializeOptionsMenu();
    }

    /* options menu / toolbar */

    public void initializeOptionsMenu() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle(R.string.settings_title);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}