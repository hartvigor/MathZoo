package no.hartvigor.s306386mappe1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.MissingResourceException;

public class GameActivity extends AppCompatActivity {

    private ArrayList<GameItem> gameItems = new ArrayList<>();
    /**
     * array_position -1 fordi den ikke er tatt i bruk altså blir 0 ved bruk
     */
    private int array_position = -1;
    /**
     * variabler for statestikk
     */
    private int score_number;
    private int sum_total_games;
    private int score_total_history;
    private int sum_total_history;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);
        /*
          starte metode for laging av spm. if spill er igang er ikke saveIS null
         */
        //Hvis ikke det er lagret spill i InstanceState lag nytt spill
        if(savedInstanceState == null) {
            createRandomMathQuestions();
        }

        keyboard_input_listener();
    }

    /**
     * Ved endring for eks horizontalt (config) lagres aktivit i objektet i en bundle(objekt)
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        GameHolder Holder = new GameHolder(gameItems);
        outState.putSerializable("holder", Holder);
        String currentAnswer = ((TextView)findViewById(R.id.game_answer_field)).getText().toString();
        outState.putString("currentAnswer", currentAnswer);
        outState.putInt("position", array_position);
        outState.putInt("score", score_number);

        String language = PreferenceManager.getDefaultSharedPreferences(this).getString("languages", "default");
        Configuration config = getResources().getConfiguration();
        if( language.equals("default") ) language = Locale.getDefault().getLanguage();
        config.locale = new Locale(language);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        super.onSaveInstanceState(outState);
    }

    /**
     * her hentes lagret fra onSaveIS og restores
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState.containsKey("holder")) {
            GameHolder restoreHolder = (GameHolder) savedInstanceState.getSerializable("holder");
            if (restoreHolder != null) {
                gameItems = restoreHolder.getItems();
            }
        }

        if(savedInstanceState.containsKey("currentAnswer")){
            ((TextView)findViewById(R.id.game_answer_field)).setText(savedInstanceState.getString("currentAnswer"));
        }

        if(savedInstanceState.containsKey("position")){
            array_position = savedInstanceState.getInt("position");
        }

        if(savedInstanceState.containsKey("score")){
            score_number = savedInstanceState.getInt("score");
        }

        restoreQuestion();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_game);
    }

    /**
     * laging av spm
     */
    public void createRandomMathQuestions() {


        //??
        String[] ql = getResources().getStringArray(R.array.math_questions);
        String[] qa = getResources().getStringArray(R.array.math_answers);


         //henting av valgt antall spm
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String desiredPreference =  sharedPreferences.getString("number_of_questions", "5");

        //Gjør string til int
        int max = Integer.valueOf(desiredPreference);

        ArrayList<GameItem> temp = new ArrayList<GameItem>();

        if(ql.length == qa.length){
            for(int i = 0; i < qa.length; i++){
                temp.add(new GameItem(i, ql[i], qa[i]));
            }
        }
        else{
            throw new MissingResourceException("Length in Array not the same", "GameActivity", null);
        }
        Log.e("Objekt listen", "Objetktet:"+ Arrays.toString(new ArrayList[]{gameItems}));

        Collections.shuffle(temp);
        for(GameItem item : temp){
            gameItems.add(item);
            if(gameItems.size() == max)
                break;
        }
        nextQuestion();
    }

    /**
     * metode setter score i view
     */
    public void setScoreView(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        TextView setScoreView = findViewById(R.id.game_score_view);
        setScoreView.setText(score_number + "/" + sharedPreferences.getString("number_of_questions", "5"));
    }

    /**
     * calles ved gjennomoppretting av aktivitet(for eks horizontalt endring)
     */
    public void restoreQuestion() {
        GameItem restore = gameItems.get(array_position);
        setScoreView();
        ((TextView)findViewById(R.id.game_math_question)).setText(restore.getQuestion());
    }

    public void nextQuestion(){
        //legge til i variabel for totalt spillte spill
        sum_total_games++;


        setScoreView();
        array_position++;
        ((TextView)findViewById(R.id.game_math_question)).setText(gameItems.get(array_position).getQuestion());
        ((TextView)findViewById(R.id.game_answer_field)).setText("");
    }

    private void onCheckAnswer (){
        gameItems.get(array_position).setAnswered(true);

        TextView Attempt = findViewById(R.id.game_answer_field);
        String current = Attempt.getText().toString();

        if(gameItems.get(array_position).getAnswer().equals(current)){
            score_number++;
            gameItems.get(array_position).setCorrect(true);
            toastHelper(getString(R.string.string_correct));
        }
        else{
            toastHelper(getString(R.string.string_wrong));
        }

        if(gameItems.size()-1 == array_position && gameItems.get(array_position).isAnswered()){
            gameCompleted();
        }
        else{
            nextQuestion();
        }
    }

    //Hjelper for å plassere toast melding riktig posisjon på skjermen
    private void toastHelper(String text_for_toast){
        Toast correct_toast = Toast.makeText(this, text_for_toast, Toast.LENGTH_SHORT);

        View toastStyle = correct_toast.getView();

        if(text_for_toast.equals(getString(R.string.string_correct))){
            toastStyle.setBackgroundResource(R.drawable.toast_styling);
        }
        else{
            toastStyle.setBackgroundResource(R.drawable.toast_styling_alt);
        }

        int orientation = getResources().getConfiguration().orientation;

        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            correct_toast.setGravity(Gravity.CENTER,-200,10);
        }
        else{
            correct_toast.setGravity(Gravity.CENTER,0,-80);
        }

        correct_toast.show();
    }

    //ved avsluttning
    private void statisticHelper(){
        /**
         * ved avslutting av spill lagres score i shared preferences
         */
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        score_total_history =  sharedPreferences.getInt("score_total", 0);
        sum_total_history =  sharedPreferences.getInt("sum_total_games", 0);
        score_total_history += score_number;
        sum_total_history += sum_total_games;
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //siste spill score
        editor.putInt("score", score_number);
        editor.apply();

        //totalt riktig score
        editor.putInt("score_total", score_total_history);
        editor.apply();

        //totalt antall spill
        editor.putInt("sum_total_games", sum_total_history);
        editor.apply();

    }

    private void gameCompleted(){
        setScoreView();
        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        final View customLayout = getLayoutInflater().inflate(R.layout.game_activity_dialog_alert, null);
        adb.setView(customLayout);

        adb.setPositiveButton(getResources().getString(R.string.string_finished), (dialogInterface, i) -> {
            statisticHelper();
            finish();
        });

        adb.setNegativeButton(getResources().getString(R.string.string_new_game), (dialogInterface, i) -> {
            sum_total_games++;
            gameItems.clear();
            array_position = -1;
            score_number = 0;
            createRandomMathQuestions();
        });

        adb.create().show();
    }



    /**
     * listener som aktiverer metode fra trykk på keyboard
     */
    private void keyboard_input_listener(){
        findViewById(R.id.button_0).setOnClickListener(view -> keyboard_set_input(getString(R.string.string_number_zero)));
        findViewById(R.id.button_1).setOnClickListener(view -> keyboard_set_input(getString(R.string.string_number_one)));
        findViewById(R.id.button_2).setOnClickListener(view -> keyboard_set_input(getString(R.string.string_number_two)));
        findViewById(R.id.button_3).setOnClickListener(view -> keyboard_set_input(getString(R.string.string_number_three)));
        findViewById(R.id.button_4).setOnClickListener(view -> keyboard_set_input(getString(R.string.string_number_four)));
        findViewById(R.id.button_5).setOnClickListener(view -> keyboard_set_input(getString(R.string.string_number_five)));
        findViewById(R.id.button_6).setOnClickListener(view -> keyboard_set_input(getString(R.string.string_number_six)));
        findViewById(R.id.button_7).setOnClickListener(view -> keyboard_set_input(getString(R.string.string_number_seven)));
        findViewById(R.id.button_8).setOnClickListener(view -> keyboard_set_input(getString(R.string.string_number_eight)));
        findViewById(R.id.button_9).setOnClickListener(view -> keyboard_set_input(getString(R.string.string_number_nine)));
        findViewById(R.id.button_check_answer).setOnClickListener(view -> onCheckAnswer());
        findViewById(R.id.button_delete).setOnClickListener(view -> deleteInput());
    }

    /**
     * legger inn innput fra keybord og lager string av inputten
     * @param v
     */
    private void keyboard_set_input(String v){
        TextView Answer = findViewById(R.id.game_answer_field);
        Answer.append(v);
    }

    /**
     * fjerner siste bokstav fra input fra keyboard
     */
    private void deleteInput(){
        TextView Answer = findViewById(R.id.game_answer_field);
        if (Answer != null) {
            String v = Answer.getText().toString();
            if (v.length() > 0) {
                v = v.substring(0, v.length() -1);
                Answer.setText(v);
            }
        }
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle(R.string.string_leaving_game);

        adb.setPositiveButton(getResources().getString(R.string.string_exit), ((dialogInterface, i) -> {
            finish();
        }));

        adb.setNegativeButton(getResources().getString(R.string.string_cancell), ((dialogInterface, i) -> {
            dialogInterface.dismiss();
        }));
        adb.create().show();
    }

}






