package com.vlazar83.mygoals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CardStackListener {

    private CardStackLayoutManager manager;
    private CardStackView cardStackView;
    private CardStackAdapter adapter;
    private DrawerLayout drawerLayout;
    private StatisticsHolder statisticsHolder;

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

        manager = new CardStackLayoutManager(this, this);
        cardStackView = findViewById(R.id.card_stack_view);
        adapter = new CardStackAdapter();
        adapter.setCards(generateCards());
        drawerLayout = findViewById(R.id.drawer_layout);

        setupNavigation();
        setupCardStackView();
        setupButton();
        reloadCardsFromSharedPreferences();
        setupStatistics();
        reloadStatisticsFromSharedPreferences();
    }

    @Override
    protected void onStop() {
        super.onStop();

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

        // save statistics to SharedPreferences
        builder = new GsonBuilder();
        builder.registerTypeAdapter(Statistics.class, new StatisticsAdapter());
        gson = builder.create();
        //Let's serialize our array
        typeOfSrc = new TypeToken<Collection<Statistics>>(){}.getType();
        String statisticsJsonFormat = gson.toJson(StatisticsHolder.getInstance().getStatisticsList(), typeOfSrc);
        Log.w("Statistics in Json Format:", statisticsJsonFormat);
        Utils.saveStatisticsToSharedPreferences(statisticsJsonFormat);
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

            reloadCardsFromActualSet();

            /*
            SwipeAnimationSetting.Builder builder = new SwipeAnimationSetting.Builder();
            SwipeAnimationSetting setting = builder.setDirection(Direction.Bottom)
                    .setDuration(Duration.Normal.duration)
                    .setInterpolator(new DecelerateInterpolator())
                    .build();
            manager.setSwipeAnimationSetting(setting);
            cardStackView.swipe();*/
        });

        FloatingActionButton like = findViewById(R.id.like_button);
        like.setOnClickListener(v -> {

            // if there is a card left
            if(adapter.getCard(manager.getTopPosition()) != null){
                addToStatistics(adapter.getCard(manager.getTopPosition()));
            }

            SwipeAnimationSetting.Builder builder = new SwipeAnimationSetting.Builder();
            SwipeAnimationSetting setting = builder.setDirection(Direction.Right)
                    .setDuration(Duration.Normal.duration)
                    .setInterpolator(new AccelerateInterpolator())
                    .build();
            manager.setSwipeAnimationSetting(setting);
            cardStackView.swipe();
        });

    }

    private void setupNavigation(){
        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // DrawerLayout
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        actionBarDrawerToggle.syncState();
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        // NavigationView
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                // Handle navigation view item clicks here.
                int id = item.getItemId();

                switch(id){
                    case R.id.checkStatistics:
                        checkStatistics();
                        break;

                    case R.id.add_spot_to_first:
                        break;

                    case R.id.plan_the_card_set:
                        planTheCardSet();
                        break;
                }

                    drawerLayout.closeDrawers();



                return true;
            }
        });
    }

    private void planTheCardSet(){
        Intent planTheWeekIntent = new Intent(MainActivity.this, PlanTheCardSetActivity.class);
        startActivity(planTheWeekIntent);
    }

    private void checkStatistics(){
        Intent statisticsIntent = new Intent(MainActivity.this, StatisticsActivity.class);
        startActivity(statisticsIntent);
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

        ArrayList<CardShape> cardShapesList = new ArrayList<CardShape>();
        cardShapesList.addAll(ActualCardSet.getInstance().getCardShapeList());
        return cardShapesList;

    }

    private void reloadCardsFromActualSet(){

        List<CardShape> newList = ActualCardSet.getInstance().getCardShapeList();
        adapter.setCards(newList);
        adapter.notifyDataSetChanged();
    }

    private void reloadCardsFromSharedPreferences(){

        String cardsJsonFormat = Utils.loadSharedPreferences();
        if(!cardsJsonFormat.equalsIgnoreCase("[]") && !cardsJsonFormat.equalsIgnoreCase("") ){
            //Create our gson instance
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(CardShape.class, new InterfaceAdapter());
            Gson gson = builder.create();

            Type typeOfSrc = new TypeToken<Collection<CardShape>>(){}.getType();
            ArrayList<CardShape> carJsonArray = gson.fromJson(cardsJsonFormat, typeOfSrc);
            adapter.setCards(carJsonArray);
            adapter.notifyDataSetChanged();

            ActualCardSet.getInstance().setCardShapeList(carJsonArray);

        }
    }

    private void reloadStatisticsFromSharedPreferences(){

        String statisticsJsonFormat = Utils.loadStatisticsFromSharedPreferences();
        if(!statisticsJsonFormat.equalsIgnoreCase("{\"CLASSNAME_FOR_STATISTICS\":\"java.util.ArrayList\",\"DATA_FOR_STATISTICS\":[]}") && !statisticsJsonFormat.equalsIgnoreCase("") ){
            //Create our gson instance
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Statistics.class, new StatisticsAdapter());
            Gson gson = builder.create();

            Type typeOfSrc = new TypeToken<Collection<Statistics>>(){}.getType();
            ArrayList<Statistics> statisticsArray = gson.fromJson(statisticsJsonFormat, typeOfSrc);
            StatisticsHolder.getInstance().setStatisticsArray(statisticsArray);

        }
    }

    private void setupStatistics(){
        statisticsHolder = StatisticsHolder.getInstance();
    }

    private void addToStatistics(CardShape card){

        // get the statistic for today
        int today = LocalDate.now().getDayOfYear();
        Statistics todayStatistics = statisticsHolder.getStatistic(today);

        switch (card.getCardClass()){
            case "BlueCard":
                todayStatistics.incrementBlueCardCount();
                break;
            case "GreenCard":
                todayStatistics.incrementGreenCardCount();
                break;
            case "RedCard":
                todayStatistics.incrementRedCardCount();
                break;
        }

        statisticsHolder.addStatistic(todayStatistics, today);
    }


}
