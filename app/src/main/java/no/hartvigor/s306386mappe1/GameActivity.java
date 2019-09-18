package no.hartvigor.s306386mappe1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.MissingResourceException;

public class GameActivity extends AppCompatActivity {

    private ArrayList<GameItem> gameItems = new ArrayList<>();
    /**
     * -1 fordi den ikke er tatt i bruk altså blir 0 ved bruk
     */
    private int array_position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int conf = getResources().getConfiguration().orientation;
        if (conf == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_game_horizontal);
        } else if (conf == Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.activity_game);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        /**
         * starte metode for laging av spm. if spill er igang er ikke saveIS null
         */
        if(savedInstanceState == null)
            createRandomMathQuestions();

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
        if(savedInstanceState.containsKey("position"))
            array_position = savedInstanceState.getInt("position");
        restoreQuestion();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int conf = getResources().getConfiguration().orientation;
        if (conf == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_game_horizontal);
        } else if (conf == Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.activity_game);
        }
        keyboard_input_listener();
    }

    /**
     * laging av spm
     */
    public void createRandomMathQuestions() {
        String[] ql = getResources().getStringArray(R.array.math_questions);
        String[] qa = getResources().getStringArray(R.array.math_answers);

        Log.e("array listen:", "arrayet"+ Arrays.toString(ql));
        Log.e("array listen:", "arrayet"+ Arrays.toString(qa));

        /**
         * henting av valgt antall spm
         */
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String desiredPreference =  sharedPreferences.getString("number_of_questions", "5");
        /**
         * gjør string til int
         */
        int max = Integer.valueOf(desiredPreference);

        ArrayList<GameItem> temp = new ArrayList<GameItem>();

        if(ql.length == qa.length){
            for(int i = 0; i < qa.length; i++){
                temp.add(new GameItem(i, ql[i], qa[i]));
            }
        }
        else{
            throw new MissingResourceException("Resource Array Length is not the same size", "GameActivity", null);
        }
        Log.e("Objekt listen", "Objetktet:"+ Arrays.toString(new ArrayList[]{gameItems}));

        Collections.shuffle(gameItems);
        for(GameItem item : temp){
            gameItems.add(item);
            if(gameItems.size() == max)
                break;
        }
        nextQuestion();

    }

    /**
     * calles ved gjennomoppretting av aktivitet(for eks horizontalt endring)
     */
    public void restoreQuestion() {
        GameItem restore = gameItems.get(array_position);

        ((TextView)findViewById(R.id.game_math_question)).setText(restore.getQuestion());
    }

    public void nextQuestion(){
        array_position++;
        ((TextView)findViewById(R.id.game_math_question)).setText(gameItems.get(array_position).getQuestion());
        ((TextView)findViewById(R.id.game_answer_field)).setText("");
    }

    private void onCheckAnswer (){
        gameItems.get(array_position).setAnswered(true);

        TextView Attempt = findViewById(R.id.game_answer_field);
        String current = Attempt.getText().toString();


        if(gameItems.get(array_position).getAnswer().equals(current)){
            Log.e("Melding.","Riktig svar");
            Toast.makeText(this, getString(R.string.string_correct), Toast.LENGTH_SHORT).show();
        }
        else{
            Log.e("Melding.", "Ikke riktig");
            Toast.makeText(this, getString(R.string.string_wrong), Toast.LENGTH_SHORT).show();
        }

        if(gameItems.size()-1 == array_position && gameItems.get(array_position).isAnswered())
            gameCompleted();
        else
            nextQuestion();

    }

    private void gameCompleted(){
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setPositiveButton(getResources().getString(R.string.string_finished), (dialogInterface, i) -> {
            finish();
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


}






