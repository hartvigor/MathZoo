package no.hartvigor.s306386mappe1;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;

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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.registerOnSharedPreferenceChangeListener(myPreferenceListener);
    }

    //method for changing language
    public void settland(String landskode){
        Resources res=getResources();
        DisplayMetrics dm=res.getDisplayMetrics();
        Configuration cf=res.getConfiguration();
        cf.setLocale(new Locale(landskode));
        res.updateConfiguration(cf,dm);
    }

    //listener for change in preferences activity
    SharedPreferences.OnSharedPreferenceChangeListener myPreferenceListener = (sharedPreferences, s) -> {
        Log.e("SharedPreferenceValue", s);
        String val = sharedPreferences.getString(s, "");

        switch (s) {
            case "languages": {
                String langCode = sharedPreferences.getString(s, "no");
                settland(langCode);
                recreate();
                break;
            }
        }
    };

    public static class PrefsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }

    /*
    Saves set language when rotation is changed
    source: https://stackoverflow.com/questions/19919654/why-locale-settings-are-overriden-on-preferenceactivity-when-orientation-changes
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        String language = PreferenceManager.getDefaultSharedPreferences(this).getString("languages", "default");
        Configuration config = getResources().getConfiguration();
        if( language.equals("default") ) language = Locale.getDefault().getLanguage();
        config.locale = new Locale(language);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        super.onSaveInstanceState(outState);
    }




}
