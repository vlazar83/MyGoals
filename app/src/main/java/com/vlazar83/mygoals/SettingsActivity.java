package com.vlazar83.mygoals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

public class SettingsActivity extends AppCompatActivity {

    private ToggleButton toggleButton;
    private EditText ageNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_in_settings_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toggleButton = findViewById(R.id.toggle_button);
        ageNumber = findViewById(R.id.age_number);

        loadSettingsFromSharedPreferences();

    }

    @Override
    protected void onPause() {
        super.onPause();

        Settings.getInstance().setMale(toggleButton.isChecked());
        if(ageNumber.getText().toString().equalsIgnoreCase("")){
            Settings.getInstance().setAge(Settings.default_age);
        } else {
            Settings.getInstance().setAge(Integer.parseInt(ageNumber.getText().toString()));
        }

        saveSettingsToSharedPreferences();
    }

    private void loadSettingsFromSharedPreferences(){

        Gson gson = new Gson();
        String settingsJsonFormat = Utils.loadSettingsFromSharedPreferences();
        Settings settings = gson.fromJson(settingsJsonFormat, Settings.class);

        // set it up in singleton Settings
        if(settings != null) {
            Settings.getInstance().setMale(settings.isMale());
            Settings.getInstance().setAge(settings.getAge());

            setupValuesOnUi();

        }

    }

    private void saveSettingsToSharedPreferences(){

        Gson gson = new Gson();
        Settings.getInstance().setMale(toggleButton.isChecked());

        if(ageNumber.getText().toString().equalsIgnoreCase("")){
            Settings.getInstance().setAge(Settings.default_age);
        } else {
            Settings.getInstance().setAge(Integer.parseInt(ageNumber.getText().toString()));
        }

        String settingsJsonFormat = gson.toJson(Settings.getInstance());
        Utils.saveSettingsToSharedPreferences(settingsJsonFormat);

    }

    private void setupValuesOnUi(){

        toggleButton.setChecked(Settings.getInstance().isMale());
        ageNumber.setText(Integer.toString(Settings.getInstance().getAge()));

    }

}
