package com.vlazar83.mygoals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.List;

public class CreateNewCardActivity extends AppCompatActivity implements CardStackListener {

    private CardStackLayoutManager manager;
    private CardStackView cardStackView;
    private CardStackAdapter adapter;
    private CreationCardSetHolder creationCardSetHolder;
    public static CardShape cardToPassOnForCreation;

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

        manager = new CardStackLayoutManager(this, this);
        cardStackView = findViewById(R.id.card_stack_view);
        adapter = new CardStackAdapter();
        adapter.setCards(generateCards());

        setupCardStackView();
        setupButton();

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

        FloatingActionButton skip = findViewById(R.id.skip_button);
        skip.setOnClickListener(v -> {

            //removeFirst();

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

            createCardBasedOnTopPicture();

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

    private void createCardBasedOnTopPicture(){
        if (adapter.getCards().isEmpty()) {
            return;
        }

        if(manager.getTopView() == null){
            return;
        }

        // manager.getTopPosition() provides the card on the top
        //createdCardSet.addCard(adapter.getCard(manager.getTopPosition()));

        // Here we need to navigate further to the second step to type in the 2 details for the choosen picture
        // only after that can we create the actual card.

        Intent goToFinalizeIntent = new Intent(CreateNewCardActivity.this, FinalizeNewCardActivity.class);
        // forward the card to the next step
        cardToPassOnForCreation = adapter.getCard(manager.getTopPosition());
        startActivity(goToFinalizeIntent);

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

    private void backToPlan(){
        Intent backToPlanIntent = new Intent(CreateNewCardActivity.this, PlanTheCardSetActivity.class);
        startActivity(backToPlanIntent);

    }

    private List<CardShape> generateCards(){
        creationCardSetHolder = CreationCardSetHolder.getInstance();
        Utils.scanForHighestCardIds(creationCardSetHolder.getCardShapeList());
        return creationCardSetHolder.getCardShapeList();
    }

}
