package no.hartvigor.s306386mappe1;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.MissingResourceException;

public class GameActivity extends AppCompatActivity {

    private ArrayList<GameItem> gameItems = new ArrayList<>();
    private int array_position;

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
        outState.putSerializable("Holder", Holder);
        String currentAnswer = ((TextView)findViewById(R.id.game_answer_field)).getText().toString();
        outState.putString("currentAnswer", currentAnswer);
    }

    /**
     * her hentes lagret fra onSaveIS og restores
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState.containsKey("Holder")) {
            GameHolder restoreHolder = (GameHolder) savedInstanceState.getSerializable("Holder");
            if (restoreHolder != null) {
                gameItems = restoreHolder.getItems();
            }
        }
        if(savedInstanceState.containsKey("currentAnswer")){
            ((TextView)findViewById(R.id.game_answer_field)).setText(savedInstanceState.getString("currentAnswer"));
        }
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


        //TextView txt_question = findViewById(R.id.editText_question);

        if(ql.length == qa.length){
            for(int i = 0; i < qa.length; i++){
                gameItems.add(new GameItem(ql[i], qa[i]));
                if(gameItems.size() == max)
                    break;
            }
        }
        else{
            throw new MissingResourceException("Resource Array Length is not the same size", "GameActivity", null);
        }
        Log.e("Objekt listen", "Objetktet:"+ Arrays.toString(new ArrayList[]{gameItems}));

        Collections.shuffle(gameItems);

        for(int i = 0; i < gameItems.size(); i++){
            gameItems.get(i).setId(i+1);
        }
        //for()
        //txt_question.setText();
    }

    public void nextQuestion(){

    }

    private void onCheckAnswer (){
        //TextView Question = findViewById(R.id.game_math_question);
        String Question = "1";
        TextView Attempt = findViewById(R.id.game_answer_field);
        String current = Attempt.getText().toString();


        if(Question.equals(current)){
            Log.e("Melding.","R.string.string_correct");
        }
        else{
            Log.e("Melding.", "R.string.string_wrong");
        }
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






