package com.vlazar83.mygoals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    private ToggleButton toggleButton, toggleButton_isExtrovert, toggleButton_isOwl;
    private EditText ageNumber;
    private ListView listView;
    private Button redCardsTargetButton,greenCardsTargetButton,blueCardsTargetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_in_settings_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        redCardsTargetButton = findViewById(R.id.red_cards_target_button);
        redCardsTargetButton.setText(String.valueOf(Settings.getInstance().getWeekly_target_red_card()));
        greenCardsTargetButton = findViewById(R.id.green_cards_target_button);
        greenCardsTargetButton.setText(String.valueOf(Settings.getInstance().getWeekly_target_green_card()));
        blueCardsTargetButton = findViewById(R.id.blue_cards_target_button);
        blueCardsTargetButton.setText(String.valueOf(Settings.getInstance().getWeekly_target_blue_card()));

        toggleButton = findViewById(R.id.toggle_button);
        toggleButton_isExtrovert = findViewById(R.id.toggle_button_introvert_or_extrovert);
        toggleButton_isOwl = findViewById(R.id.toggle_button_owl_or_lark);
        ageNumber = findViewById(R.id.age_number);
        listView=findViewById(R.id.listView_for_goldenSentences);
        FloatingActionButton addNewGoldenSentenceFloatingButton = findViewById(R.id.add_golden_sentence_fab);

        loadSettingsFromSharedPreferences();

        ArrayList<GoldenSentence> arrayOfGoldenSentences = new ArrayList<GoldenSentence>();
        GoldenSentenceAdapter adapter = new GoldenSentenceAdapter(this, arrayOfGoldenSentences);
        listView.setAdapter(adapter);
        for(int i =0; i<Settings.getInstance().getGoldenSentences().size();i++){
            adapter.add(new GoldenSentence(Settings.getInstance().getGoldenSentence(i)));
        }

        listView.setOnItemClickListener((parent, view, position, id) -> {

            if(Settings.getInstance().getGoldenSentences().size() > 0 && position < Settings.getInstance().getGoldenSentences().size()) {
                Settings.getInstance().dropGoldenSentence(position);

                adapter.clear();
                for(int i =0; i<Settings.getInstance().getGoldenSentences().size();i++){
                    adapter.add(new GoldenSentence(Settings.getInstance().getGoldenSentence(i)));
                }
                adapter.notifyDataSetChanged();

                saveSettingsToSharedPreferences();
                Toast.makeText(SettingsActivity.this, getApplicationContext().getString(R.string.GoldenSentencesDeleted_ToastMessage), Toast.LENGTH_LONG).show();
            }

        });

        addNewGoldenSentenceFloatingButton.setOnClickListener(v -> goToCreateNewGoldenSentence());

        redCardsTargetButton.setOnClickListener(v -> {
            if(Settings.getInstance().getWeekly_target_red_card() == 10){
                Settings.getInstance().setWeekly_target_red_card(1);
                redCardsTargetButton.setText(String.valueOf(Settings.getInstance().getWeekly_target_red_card()));
            } else {
                Settings.getInstance().setWeekly_target_red_card(Settings.getInstance().getWeekly_target_red_card()+1);
                redCardsTargetButton.setText(String.valueOf(Settings.getInstance().getWeekly_target_red_card()));
            }
        });

        greenCardsTargetButton.setOnClickListener(v -> {
            if(Settings.getInstance().getWeekly_target_green_card() == 10){
                Settings.getInstance().setWeekly_target_green_card(1);
                greenCardsTargetButton.setText(String.valueOf(Settings.getInstance().getWeekly_target_green_card()));
            } else {
                Settings.getInstance().setWeekly_target_green_card(Settings.getInstance().getWeekly_target_green_card()+1);
                greenCardsTargetButton.setText(String.valueOf(Settings.getInstance().getWeekly_target_green_card()));
            }
        });

        blueCardsTargetButton.setOnClickListener(v -> {
            if(Settings.getInstance().getWeekly_target_blue_card() == 10){
                Settings.getInstance().setWeekly_target_blue_card(1);
                blueCardsTargetButton.setText(String.valueOf(Settings.getInstance().getWeekly_target_blue_card()));
            } else {
                Settings.getInstance().setWeekly_target_blue_card(Settings.getInstance().getWeekly_target_blue_card()+1);
                blueCardsTargetButton.setText(String.valueOf(Settings.getInstance().getWeekly_target_blue_card()));
            }
        });


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
            Settings.getInstance().setGoldenSentences(settings.getGoldenSentences());
            Settings.getInstance().setWeekly_target_red_card(settings.getWeekly_target_red_card());
            Settings.getInstance().setWeekly_target_green_card(settings.getWeekly_target_green_card());
            Settings.getInstance().setWeekly_target_blue_card(settings.getWeekly_target_blue_card());

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

    private void goToCreateNewGoldenSentence(){
        Intent intent = new Intent(SettingsActivity.this, AddGoldenSentenceActivity.class);
        startActivity(intent);

    }

    public class GoldenSentence {
        public String goldenSentence;

        public GoldenSentence(String goldenSentence) {
            this.goldenSentence = goldenSentence;
        }
    }

    public class GoldenSentenceAdapter extends ArrayAdapter<GoldenSentence> {
        public GoldenSentenceAdapter(Context context, ArrayList<GoldenSentence> goldenSentences) {
            super(context, 0, goldenSentences);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            GoldenSentence goldenSentence = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_golden_sentence, parent, false);
            }
            TextView tvName = (TextView) convertView.findViewById(R.id.goldenSentence);
            tvName.setText(goldenSentence.goldenSentence);
            return convertView;
        }
    }

}