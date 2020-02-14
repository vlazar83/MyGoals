package com.vlazar83.mygoals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.andtinder.model.CardModel;
import com.andtinder.view.CardContainer;
import com.andtinder.view.SimpleCardStackAdapter;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private CardFactory cardFactory;
    private ArrayList<CardShape> cardShapesList;
    private ArrayList<String> cardShapesStringList;
    private ArrayAdapter<String> arrayAdapter;
    private int i;

    private CardContainer mCardContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check if this is the first run
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = prefs.getBoolean(getString(R.string.pref_previously_started), false);
        if(!previouslyStarted) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(getString(R.string.pref_previously_started), Boolean.TRUE);
            edit.commit();

            startActivity(new Intent(MainActivity.this, IntroActivity.class));
        }

        setContentView(R.layout.activity_main);


        mCardContainer = (CardContainer) findViewById(R.id.layoutview);

        Resources r = getResources();

        SimpleCardStackAdapter adapter = new SimpleCardStackAdapter(this);

        CardModel cardModel1 = new CardModel("Title1", "Description goes here1", r.getDrawable(R.drawable.picture1));
        CardModel cardModel2 = new CardModel("Title2", "Description goes here2", r.getDrawable(R.drawable.picture2));
        CardModel cardModel3 = new CardModel("Title3", "Description goes here3", r.getDrawable(R.drawable.picture3));
        CardModel cardModel4 = new CardModel("Title4", "Description goes here4", r.getDrawable(R.drawable.picture1));

        cardModel4.setOnClickListener(new CardModel.OnClickListener() {
            @Override
            public void OnClickListener() {
                Log.i("Swipeable Cards","I am pressing the card");
            }
        });

        cardModel4.setOnCardDimissedListener(new CardModel.OnCardDimissedListener() {
            @Override
            public void onLike() {
                Log.i("Swipeable Cards","I like the card");
            }

            @Override
            public void onDislike() {
                Log.i("Swipeable Cards","I dislike the card");
            }
        });

        adapter.add(cardModel1);
        adapter.add(cardModel2);
        adapter.add(cardModel3);
        adapter.add(cardModel4);

        mCardContainer.setAdapter(adapter);

        //displayTheCards();
    }
/*
    private void displayTheCards(){

        //add the view via xml or programmatically
        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        generateCards();

        //choose your favorite adapter
        cardShapesStringList = Utils.getCardGoalList(cardShapesList);
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.item, R.id.helloText, cardShapesStringList );

        //set the listener and the adapter
        flingContainer.setAdapter(arrayAdapter);

        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {

            @Override
            public void onScroll(float v) {
                // TODO
            }

            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                cardShapesList.remove(0);
                cardShapesStringList.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                Toast.makeText(MainActivity.this, "Left!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Toast.makeText(MainActivity.this, "Right!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                cardShapesList.add(cardFactory.getCardShape("RedCard", "put the trash out" + i));
                //arrayAdapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
                i++;
            }
        });

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(MainActivity.this, "Clicked!",  Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void generateCards(){
        cardFactory = new CardFactory();
        cardShapesList = new ArrayList<CardShape>();
        cardShapesList.add(cardFactory.getCardShape("RedCard", "put the trash out"));
        cardShapesList.add(cardFactory.getCardShape("BlueCard", "put the trash out2"));
        cardShapesList.add(cardFactory.getCardShape("RedCard", "put the trash out3"));
        cardShapesList.add(cardFactory.getCardShape("BlueCard", "put the trash out4"));
        cardShapesList.add(cardFactory.getCardShape("RedCard", "put the trash out5"));
    }
*/
}
