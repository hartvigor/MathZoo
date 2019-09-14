package no.hartvigor.s306386mappe1;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class SetPreferencesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content,new
                PrefsFragment()).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.registerOnSharedPreferenceChangeListener(myPreferenceListener);
    }

    public void settland(String landskode){
        Resources res=getResources();
        DisplayMetrics dm=res.getDisplayMetrics();
        Configuration cf=res.getConfiguration();
        cf.setLocale(new Locale(landskode));
        res.updateConfiguration(cf,dm);
    }

    SharedPreferences.OnSharedPreferenceChangeListener myPreferenceListener = (sharedPreferences, s) -> {
        Log.e("SharedPreferenceValue", s);
        String val = sharedPreferences.getString(s, "");

        switch (s)
        {
            case "languages":
            {
                String langCode = sharedPreferences.getString(s, "no");
                settland(langCode);
                recreate();
                break;
            }
        }


/*
        Log.e("SharedPref VALUE", val);
        switch(val) {
            case "no": {
                settland("no");
                recreate();

                // Launch Langauge manipulator
                Log.i("National New", "Sexy time is now");
                break;
            }

            case "de": {
                settland("de");
                recreate();
            }
        }
*/

    };

    public static class PrefsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }





}
