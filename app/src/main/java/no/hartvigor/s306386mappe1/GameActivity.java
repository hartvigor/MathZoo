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
         * starte metode for laging av spm
         */
        createRandomMathQuestions();
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

        //gjør om string til tall
        int max = Integer.valueOf(desiredPreference);


        TextView txt_question = findViewById(R.id.editText_question);

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

        //for()
        //txt_question.setText();
    }


    public void onCheckAnswer (View v){
        TextView Answer = findViewById(R.id.game_math_question);
        TextView Attempt = findViewById(R.id.game_answer_field);

        if(Attempt.equals(Answer)){
            Answer.setText("@string/string_correct");
        }
        else{
            Answer.setText("@string/string_wrong");
        }
    }

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
        findViewById(R.id.button_check_answer).setOnClickListener(view -> keyboard_set_input(getString(R.string.string_check_answer)));
        findViewById(R.id.button_delete).setOnClickListener(view -> keyboard_set_input(getString(R.string.string_delete_button)));
    }

    private void keyboard_set_input(String v){
        TextView Answer = findViewById(R.id.game_answer_field);
        Answer.append(v);
    }

    /*velge tekst
    public void onSubmitClick (View view){
        TextView Answer = findViewById(R.id.Answer);
        EditText Attempt = findViewById(R.id.Attempt);
        int userAnswer = Integer.parseInt(Attempt.getText().toString());
        if(userAnswer == value1+value2) {
            Answer.setText("Correct!");

        } else {
            Answer.setText("Wrong, the correct answer was: " + (value1+value2));
        }

    }*/

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


}






