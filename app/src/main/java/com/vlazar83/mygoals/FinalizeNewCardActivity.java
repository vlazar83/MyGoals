package com.vlazar83.mygoals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FinalizeNewCardActivity extends AppCompatActivity {

    private CreatedCardSet createdCardSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalize_new_card);
        Toolbar toolbar = findViewById(R.id.toolbar_for_finalize_new_card);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EditText firstDetails = findViewById(R.id.enter_first_details);
        EditText secondDetails = findViewById(R.id.enter_second_details);

        FloatingActionButton createNewCardFloatingButton = findViewById(R.id.create_new_card_button);
        setupFloatingActionButtonColor(createNewCardFloatingButton);

        setupCreatedCardSet();

        createNewCardFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(CreateNewCardActivity.cardToPassOnForCreation != null){

                    CardShape selectedSampleCard = CreateNewCardActivity.cardToPassOnForCreation;
                    CardShape newCard = createTheNewCard(selectedSampleCard.getCardClass(), firstDetails.getText().toString(), secondDetails.getText().toString(), selectedSampleCard.getCardUrl());
                    createdCardSet.addCard(newCard);
                }

                backToNewCardCreation();
            }
        });


    }

    private void backToNewCardCreation(){
        Intent backToNewCardIntent = new Intent(FinalizeNewCardActivity.this, CreateNewCardActivity.class);
        startActivity(backToNewCardIntent);
    }

    private void setupCreatedCardSet(){
        createdCardSet = CreatedCardSet.getInstance();
        //createdCardSet.emptyList();
    }

    private void setupFloatingActionButtonColor(FloatingActionButton createNewCardFloatingButton){
        if(CreateNewCardActivity.cardToPassOnForCreation != null){
            switch(CreateNewCardActivity.cardToPassOnForCreation.getCardClass()){
                case "BlueCard":
                    createNewCardFloatingButton.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
                    break;

                case "RedCard":
                    createNewCardFloatingButton.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    break;

                case "GreenCard":
                    createNewCardFloatingButton.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
            }
        }
    }

    private CardShape createTheNewCard(String cardClass, String cardGoal, String cardCity, String cardUrl){
        CardFactory cardFactory = new CardFactory();
        return cardFactory.getCardShape(cardClass, cardGoal, cardCity, cardUrl);

    }


}
