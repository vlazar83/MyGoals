package com.vlazar83.mygoals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

public class LeadingIdeaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leading_idea);
        Toolbar toolbar = findViewById(R.id.toolbar_on_leading_idea);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EditText editText_Idea = findViewById(R.id.editText_Idea);
        editText_Idea.setText(reloadLeadingIdeaFromSharedPreferences());

        Button button = findViewById(R.id.update_button);
        button.setOnClickListener(v -> {

            // delete the leading idea card first
            ActualCardSet.getInstance().removeCardAtFront();

            // then add a new one to it.
            CardFactory cardFactory = new CardFactory();
            String leadingIdeaDetail = "";
            leadingIdeaDetail = editText_Idea.getText().toString();
            //ActualCardSet.getInstance().addCardToFront(cardFactory.getCardShape("RedCard", getString(R.string.LeadingIdea_cardLabel), leadingIdeaDetail, "https://source.unsplash.com/Xq1ntWruZQI/600x800"));

            //Create our gson instance
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(CardShape.class, new InterfaceAdapter());
            Gson gson = builder.create();
            //Let's serialize our array
            Type typeOfSrc = new TypeToken<Collection<CardShape>>(){}.getType();
            String cardsJsonFormat = gson.toJson(ActualCardSet.getInstance().getCardShapeList(), typeOfSrc);
            Log.w("Cards in Json Format:", cardsJsonFormat);

            // save actual card set to SharedPreferences
            Utils.saveSharedPreferences(cardsJsonFormat);
            Utils.saveLeadingIdeaToSharedPreferences(leadingIdeaDetail);

            backToMain();
        });

    }

    private void backToMain(){
        Intent backToMainIntent = new Intent(LeadingIdeaActivity.this, MainActivity.class);
        startActivity(backToMainIntent);

    }

    private String reloadLeadingIdeaFromSharedPreferences(){

        String leadingIdea = Utils.loadLeadingIdeaFromSharedPreferences();
        if(leadingIdea != null && !leadingIdea.equalsIgnoreCase("") ){

            return leadingIdea;

        } else return getString(R.string.LeadingIdeaActivity_default_entry);
    }

}
