package com.vlazar83.mygoals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.gson.Gson;

public class SettingsActivity extends AppCompatActivity {

    private ToggleButton toggleButton, toggleButton_isExtrovert, toggleButton_isOwl;
    private EditText ageNumber;
    private ListView listView;
    private TextView textView;
    private String[] listItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_in_settings_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toggleButton = findViewById(R.id.toggle_button);
        toggleButton_isExtrovert = findViewById(R.id.toggle_button_introvert_or_extrovert);
        toggleButton_isOwl = findViewById(R.id.toggle_button_owl_or_lark);
        ageNumber = findViewById(R.id.age_number);
        listView=findViewById(R.id.listView_for_goldenSentences);
        textView=findViewById(R.id.textView_for_goldenSentences);

        loadSettingsFromSharedPreferences();

        listItem = Settings.getInstance().getGoldenSentences().stream().toArray(String[]::new);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, listItem);
        listView.setAdapter(adapter);


    }

    @Override
    protected void onPause() {
        super.onPause();

        Settings.getInstance().setInFamily(toggleButton.isChecked());
        Settings.getInstance().setIsExtrovert(toggleButton_isExtrovert.isChecked());
        Settings.getInstance().setIsOwl(toggleButton_isOwl.isChecked());
        if(ageNumber.getText().toString().equalsIgnoreCase("")){
            Settings.getInstance().setAge(Settings.default_age);
        } else {
            Settings.getInstance().setAge(Integer.parseInt(ageNumber.getText().toString()));
        }

        saveSettingsToSharedPreferences();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Utils.cancelAllAlarms();
        if(Settings.getInstance().getIsOwl()){
            Utils.scheduleAlarm(19,15,86400000, ReminderBroadcast.class);
        } else {
            Utils.scheduleAlarm(10,15,86400000, ReminderBroadcast.class);
        }

    }

    private void loadSettingsFromSharedPreferences(){

        Gson gson = new Gson();
        String settingsJsonFormat = Utils.loadSettingsFromSharedPreferences();
        Settings settings = gson.fromJson(settingsJsonFormat, Settings.class);

        // set it up in singleton Settings
        if(settings != null) {
            Settings.getInstance().setInFamily(settings.isInFamily());
            Settings.getInstance().setAge(settings.getAge());
            Settings.getInstance().setIsExtrovert(settings.getIsExtrovert());
            Settings.getInstance().setIsOwl(settings.getIsOwl());

            setupValuesOnUi();

        }

    }

    private void saveSettingsToSharedPreferences(){

        Gson gson = new Gson();
        Settings.getInstance().setInFamily(toggleButton.isChecked());
        Settings.getInstance().setIsExtrovert(toggleButton_isExtrovert.isChecked());
        Settings.getInstance().setIsOwl(toggleButton_isOwl.isChecked());

        if(ageNumber.getText().toString().equalsIgnoreCase("")){
            Settings.getInstance().setAge(Settings.default_age);
        } else {
            Settings.getInstance().setAge(Integer.parseInt(ageNumber.getText().toString()));
        }

        String settingsJsonFormat = gson.toJson(Settings.getInstance());
        Utils.saveSettingsToSharedPreferences(settingsJsonFormat);

    }

    private void setupValuesOnUi(){

        toggleButton.setChecked(Settings.getInstance().isInFamily());
        toggleButton_isExtrovert.setChecked(Settings.getInstance().getIsExtrovert());
        toggleButton_isOwl.setChecked(Settings.getInstance().getIsOwl());
        ageNumber.setText(Integer.toString(Settings.getInstance().getAge()));

    }

}
