package de.jupiterpi.weekly.settings;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import de.jupiterpi.kaye.orders.weekly.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.parental_settings, rootKey);
    }
}