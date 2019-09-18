package no.hartvigor.s306386mappe1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startGameActivity(View v){
        Intent intent=new Intent(this,GameActivity.class);
        startActivity(intent);
    }

    public void showPreferences(View v){
        Intent intent=new Intent(this,SetPreferencesActivity.class);
        startActivity(intent);
    }

    public void showStatistic(View v){
        Intent intent=new Intent(this,StatisticActivity.class);
        startActivity(intent);
    }


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
