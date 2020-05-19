package com.vlazar83.mygoals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;

public class AddGoldenSentenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_golden_sentence);
        Toolbar toolbar = findViewById(R.id.toolbar_on_golden_sentence_creation);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        EditText editText_AddGoldenSentence = findViewById(R.id.editText_AddGoldenSentence);

        Button button = findViewById(R.id.AddGoldenSentence_update_button);
        button.setOnClickListener(v -> {

            Settings.getInstance().addGoldenSentence(editText_AddGoldenSentence.getText().toString());

            saveSettingsToSharedPreferences();
            backToSettings();
        });

    }

    private void backToSettings(){
        Intent backToSettingsIntent = new Intent(AddGoldenSentenceActivity.this, SettingsActivity.class);
        startActivity(backToSettingsIntent);

    }

    private void saveSettingsToSharedPreferences(){

        Gson gson = new Gson();
        String settingsJsonFormat = gson.toJson(Settings.getInstance());
        Utils.saveSettingsToSharedPreferences(settingsJsonFormat);

    }

}
