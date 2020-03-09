package com.vlazar83.mygoals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CreateNewCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_card);

        Toolbar toolbar = findViewById(R.id.toolbar_on_crete_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToPlan();
            }
        });

    }

    private void backToPlan(){
        Intent backToPlanIntent = new Intent(CreateNewCardActivity.this, PlanTheCardSetActivity.class);
        startActivity(backToPlanIntent);

    }

}
