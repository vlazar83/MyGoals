package com.vlazar83.mygoals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DiffUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CreatedCardsActivity extends AppCompatActivity implements CardStackListener {

    private CardStackLayoutManager manager;
    private CardStackView cardStackView;
    private CardStackAdapter adapter;
    private ImageView deleteCard;
    private CreatedCardSet createdCardSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_cards);
        Toolbar toolbar = findViewById(R.id.toolbar_on_own_cards_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        deleteCard = findViewById(R.id.delete_card);

        manager = new CardStackLayoutManager(this, this);
        cardStackView = findViewById(R.id.card_stack_view);
        adapter = new CardStackAdapter();
        adapter.setCards(generateCards());

        setupCardStackView();
        setupButton();
        loadCreatedCardsFromSharedPreferences();

    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {

    }

    @Override
    public void onCardSwiped(Direction direction) {

    }

    @Override
    public void onCardRewound() {

    }

    @Override
    public void onCardCanceled() {

    }

    @Override
    public void onCardAppeared(View view, int position) {

    }

    @Override
    public void onCardDisappeared(View view, int position) {

    }

    private void setupButton(){

        deleteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                removeFirst();

            }
        });

        FloatingActionButton skip = findViewById(R.id.skip_button);
        skip.setOnClickListener(v -> {

            SwipeAnimationSetting.Builder builder = new SwipeAnimationSetting.Builder();
            SwipeAnimationSetting setting = builder.setDirection(Direction.Left)
                    .setDuration(Duration.Normal.duration)
                    .setInterpolator(new AccelerateInterpolator())
                    .build();
            manager.setSwipeAnimationSetting(setting);
            cardStackView.swipe();
        });

        FloatingActionButton rewind = findViewById(R.id.rewind_button);
        rewind.setOnClickListener(v -> {

            reloadCardsFromCreatedCardSet();

        });

        FloatingActionButton like = findViewById(R.id.like_button);
        like.setOnClickListener(v -> {

            SwipeAnimationSetting.Builder builder = new SwipeAnimationSetting.Builder();
            SwipeAnimationSetting setting = builder.setDirection(Direction.Right)
                    .setDuration(Duration.Normal.duration)
                    .setInterpolator(new AccelerateInterpolator())
                    .build();
            manager.setSwipeAnimationSetting(setting);
            cardStackView.swipe();
        });

    }

    private void setupCardStackView(){
        initialize();
    }

    private void initialize(){
        manager.setStackFrom(StackFrom.None);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(Direction.HORIZONTAL);
        manager.setCanScrollHorizontal(true);
        manager.setCanScrollVertical(true);
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);
        manager.setOverlayInterpolator(new LinearInterpolator());

        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
        cardStackView.getItemAnimator();
    }

    private List<CardShape> generateCards(){
        createdCardSet = CreatedCardSet.getInstance();
        return createdCardSet.getCardShapeList();
    }

    private void reloadCardsFromCreatedCardSet(){

        List<CardShape> newList = CreatedCardSet.getInstance().getCardShapeList();
        adapter.setCards(newList);
        adapter.notifyDataSetChanged();
    }

    private void removeFirst(){
        if (adapter.getCards().isEmpty()) {
            return;
        }

        // if there is nothing on top, do nothing.
        if(adapter.getCard(manager.getTopPosition()) == null){
            return;
        }

        List<CardShape> old = adapter.getCards();
        List<CardShape> newList = new ArrayList<CardShape>();

        CardShape cardToBeDeleted = old.get(manager.getTopPosition());
        newList.addAll(old);
        newList.remove(manager.getTopPosition());

        DiffUtil.Callback callback = new SpotDiffCallback(old,newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback);
        adapter.setCards(newList);
        diffResult.dispatchUpdatesTo(adapter);

        createdCardSet.setCardShapeList(newList);

        Toast.makeText(CreatedCardsActivity.this, "Card deleted!", Toast.LENGTH_LONG).show();

        removeCardFromActualCardSet(cardToBeDeleted);
        removeCardFromPlanTheCardSet(cardToBeDeleted);
        saveCreatedCardsToSharedPreferences();

    }

    private void removeCardFromActualCardSet(CardShape cardToBeDeleted){

        // reload data from SharedPreferences
        reloadActualCardsFromSharedPreferences();

        // delete it from list
        List<CardShape> newList = deleteFromList(cardToBeDeleted, ActualCardSet.getInstance().getCardShapeList());
        ActualCardSet.getInstance().setCardShapeList(newList);

        //save data to SharedPreferences
        saveActualCardsToSharedPreferences();
    }


    private void reloadActualCardsFromSharedPreferences(){

        String cardsJsonFormat = Utils.loadSharedPreferences();
        if(!cardsJsonFormat.equalsIgnoreCase("[]") && !cardsJsonFormat.equalsIgnoreCase("") ){
            //Create our gson instance
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(CardShape.class, new InterfaceAdapter());
            Gson gson = builder.create();

            Type typeOfSrc = new TypeToken<Collection<CardShape>>(){}.getType();
            ArrayList<CardShape> cardJsonArray = gson.fromJson(cardsJsonFormat, typeOfSrc);

            ActualCardSet.getInstance().setCardShapeList(cardJsonArray);

        }
    }

    private void saveActualCardsToSharedPreferences(){
        //Create our gson instance
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(CardShape.class, new InterfaceAdapter());
        Gson gson = builder.create();
        //Let's serialize our array
        Type typeOfSrc = new TypeToken<Collection<CardShape>>(){}.getType();
        String cardsJsonFormat = gson.toJson(ActualCardSet.getInstance().getCardShapeList(), typeOfSrc);
        //Log.w("Cards in Json Format:", cardsJsonFormat);

        // save actual card set to SharedPreferences
        Utils.saveSharedPreferences(cardsJsonFormat);
    }

    private void removeCardFromPlanTheCardSet(CardShape cardToBeDeleted){

        // delete it from list
        List<CardShape> newList = deleteFromList(cardToBeDeleted, CardSetHolder.getInstance().getCardShapeList());
        CardSetHolder.getInstance().setupCardList(newList);

    }

    private List<CardShape> deleteFromList(CardShape cardToBeDeleted, List<CardShape> old){

        List<CardShape> newList = new ArrayList<CardShape>();
        newList.addAll(old);

        int indexOfDeletedCard = 0;
        boolean foundIt = false;
        for (CardShape cardShape : newList) {
            if(cardShape.getCardClass().equalsIgnoreCase(cardToBeDeleted.getCardClass()) && cardShape.getId() == cardToBeDeleted.getId()){
                foundIt = true;
                break;
            }
            indexOfDeletedCard += 1;
        }

        if (foundIt) newList.remove(indexOfDeletedCard);

        return newList;
    }

    private void loadCreatedCardsFromSharedPreferences(){

        String cardsJsonFormat = Utils.loadCreatedCardsFromSharedPreferences();
        if(!cardsJsonFormat.equalsIgnoreCase("[]") && !cardsJsonFormat.equalsIgnoreCase("") ){
            //Create our gson instance
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(CardShape.class, new InterfaceAdapter());
            Gson gson = builder.create();

            Type typeOfSrc = new TypeToken<Collection<CardShape>>(){}.getType();
            ArrayList<CardShape> cardJsonArray = gson.fromJson(cardsJsonFormat, typeOfSrc);
            adapter.setCards(cardJsonArray);
            adapter.notifyDataSetChanged();

            CreatedCardSet.getInstance().setCardShapeList(cardJsonArray);

        }
    }

    private void saveCreatedCardsToSharedPreferences(){
        //Create our gson instance
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(CardShape.class, new InterfaceAdapter());
        Gson gson = builder.create();
        //Let's serialize our array
        Type typeOfSrc = new TypeToken<Collection<CardShape>>(){}.getType();
        String cardsJsonFormat = gson.toJson(CreatedCardSet.getInstance().getCardShapeList(), typeOfSrc);
        //Log.w("Cards in Json Format:", cardsJsonFormat);

        // save actual card set to SharedPreferences
        Utils.saveCreatedCardsToSharedPreferences(cardsJsonFormat);
    }

}
