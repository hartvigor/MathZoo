package no.hartvigor.s306386mappe1;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class StatisticActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Integer score = sharedPreferences.getInt("score", 0);
        Integer total_score = sharedPreferences.getInt("score_total", 0);
        Integer total_games = sharedPreferences.getInt("sum_total_games", 0);

        TextView setScoreView = findViewById(R.id.last_game_score_statistic);
        setScoreView.setText(score + "/" + sharedPreferences.getString("number_of_questions", "5"));

        TextView setTotalView = findViewById(R.id.total_score_statistic);
        setTotalView.setText(total_score + "/" + total_games);

        button_input_listener();
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

    public void clearStatistic() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.clear();
        edit.commit();
        recreate();
    }

    public void button_input_listener() {
        findViewById(R.id.deleteStatistic).setOnClickListener(view -> clearStatistic());
    }
}
