package com.vlazar83.mygoals;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

public class PlanTheWeekActivity extends AppCompatActivity implements CardStackListener {

    private CardFactory cardFactory;
    private CardStackLayoutManager manager;
    private CardStackView cardStackView;
    private CardStackAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_the_week);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
            SwipeAnimationSetting.Builder builder = new SwipeAnimationSetting.Builder();
            SwipeAnimationSetting setting = builder.setDirection(Direction.Bottom)
                    .setDuration(Duration.Normal.duration)
                    .setInterpolator(new DecelerateInterpolator())
                    .build();
            manager.setSwipeAnimationSetting(setting);
            cardStackView.swipe();
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

        cardFactory = new CardFactory();
        ArrayList<CardShape> cardShapesList = new ArrayList<CardShape>();
        cardShapesList.add(cardFactory.getCardShape("RedCard", "put the trash out"));
        cardShapesList.add(cardFactory.getCardShape("BlueCard", "put the trash out2"));
        cardShapesList.add(cardFactory.getCardShape("RedCard", "put the trash out3"));
        cardShapesList.add(cardFactory.getCardShape("BlueCard", "put the trash out4"));
        cardShapesList.add(cardFactory.getCardShape("GreenCard", "put the trash out5"));

        return cardShapesList;

    }

}
