package com.vlazar83.mygoals;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LeadingIdeaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leading_idea);

        EditText editText_Idea = findViewById(R.id.editText_Idea);

        Button button = findViewById(R.id.update_button);
        button.setOnClickListener(v -> {

            // delete the leading idea card first
            ActualCardSet.getInstance().removeCardAtFront();

            // then add a new one to it.
            CardFactory cardFactory = new CardFactory();
            ActualCardSet.getInstance().addCardToFront(cardFactory.getCardShape("RedCard", getString(R.string.LeadingIdea_cardLabel), editText_Idea.getText().toString(), "https://source.unsplash.com/Xq1ntWruZQI/600x800"));

        });

    }
}
