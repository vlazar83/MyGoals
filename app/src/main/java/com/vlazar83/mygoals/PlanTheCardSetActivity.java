package com.vlazar83.mygoals;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DiffUtil;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlanTheCardSetActivity extends AppCompatActivity implements CardStackListener {

    private CardStackLayoutManager manager;
    private CardStackView cardStackView;
    private CardStackAdapter adapter;
    private CardSetHolder cardSetHolder;
    private ActualCardSet actualCardSet;
    private ImageView createNewCard;
    private CreatedCardSet createdCardSet;
    private CardShape swipedCard;  // for the case if we select the card during the planning using swipe gesture
    private Boolean swipedUsingButton = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_the_card_set);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        manager = new CardStackLayoutManager(this, this);
        cardStackView = findViewById(R.id.card_stack_view);
        adapter = new CardStackAdapter();
        adapter.setCards(generateCards());

        createNewCard = findViewById(R.id.create_new_card);

        setupCardStackView();
        setupButton();

        setupActualCardSet();

    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {

    }

    @Override
    public void onCardSwiped(Direction direction) {
        if(direction == Direction.Right && swipedCard != null && !swipedUsingButton){
            addFirstThroughSwipe(swipedCard);
        }

        swipedCard = null;
        swipedUsingButton = false;
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
        swipedCard = adapter.getCard(manager.getTopPosition());
    }

    private void setupButton(){

        createNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent createNewCardIntent = new Intent(PlanTheCardSetActivity.this, CreateNewCardActivity.class);
                startActivity(createNewCardIntent);

            }
        });

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
            /*
            SwipeAnimationSetting.Builder builder = new SwipeAnimationSetting.Builder();
            SwipeAnimationSetting setting = builder.setDirection(Direction.Bottom)
                    .setDuration(Duration.Normal.duration)
                    .setInterpolator(new DecelerateInterpolator())
                    .build();
            manager.setSwipeAnimationSetting(setting);
            cardStackView.swipe();*/
            backToMain();
        });

        FloatingActionButton like = findViewById(R.id.like_button);
        like.setOnClickListener(v -> {

            addFirst();

            SwipeAnimationSetting.Builder builder = new SwipeAnimationSetting.Builder();
            SwipeAnimationSetting setting = builder.setDirection(Direction.Right)
                    .setDuration(Duration.Normal.duration)
                    .setInterpolator(new AccelerateInterpolator())
                    .build();
            manager.setSwipeAnimationSetting(setting);
            swipedUsingButton = true;
            cardStackView.swipe();
        });

    }

    private void addFirst(){
        if (adapter.getCards().isEmpty()) {
            return;
        }

        if(manager.getTopView() == null){
            return;
        }

        // manager.getTopPosition() provides the card on the top
        actualCardSet.addCard(adapter.getCard(manager.getTopPosition()));

    }

    private void addFirstThroughSwipe(CardShape card){
        actualCardSet.addCard(card);
        swipedCard = null;
    }

    private void removeFirst(){
        if (adapter.getCards().isEmpty()) {
            return;
        }

        List<CardShape> old = adapter.getCards();
        List<CardShape> newList = new ArrayList<CardShape>();

        newList.addAll(old);
        newList.remove(0);

        DiffUtil.Callback callback = new SpotDiffCallback(old,newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback);
        adapter.setCards(newList);
        diffResult.dispatchUpdatesTo(adapter);


    }

    private void setupActualCardSet(){
        actualCardSet = ActualCardSet.getInstance();
        actualCardSet.emptyList();
        //actualCardSet.setCardShapeList(adapter.getCards());

    }

    private void setupCardStackView(){
        initialize();
    }

    private void backToMain(){
        Intent backToMainIntent = new Intent(PlanTheCardSetActivity.this, MainActivity.class);
        startActivity(backToMainIntent);

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
        cardSetHolder = CardSetHolder.getInstance();
        Utils.scanForHighestCardIds((ArrayList<CardShape>)cardSetHolder.getCardShapeList());

        // add manually created cards to the list

        List<CardShape> list = new ArrayList<>();
        list.addAll(CreatedCardSet.getInstance().getCardShapeList());
        Collections.reverse(list);
        Utils.scanForHighestCardIds((ArrayList<CardShape>)list);
        list.forEach((cardShape) -> {

            // only add if it is not added before
            if(!cardSetHolder.hasCard(cardShape)) cardSetHolder.addCardToPosition(cardShape, 0);

        });

        return cardSetHolder.getCardShapeList();
    }

}
