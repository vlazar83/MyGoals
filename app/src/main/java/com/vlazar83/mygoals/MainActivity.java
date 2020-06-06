package com.vlazar83.mygoals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import org.threeten.bp.LocalDate;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements CardStackListener {

    private CardStackLayoutManager manager;
    private CardStackView cardStackView;
    private CardStackAdapter adapter;
    private DrawerLayout drawerLayout;
    private StatisticsHolder statisticsHolder;
    private CardShape swipedCard;  // for the case if we select the card using swipe gesture
    private Boolean swipedUsingButton = false;

    @Override
    protected void onResume() {
        super.onResume();
        reloadCardsFromActualSet();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidThreeTen.init(this);

        // check if this is the first run
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = prefs.getBoolean(getString(R.string.pref_previously_started), false);
        if(!previouslyStarted) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(getString(R.string.pref_previously_started), Boolean.TRUE);
            edit.commit();
            createNotificationChannel();

            startActivity(new Intent(MainActivity.this, IntroActivity.class));
        }

        setContentView(R.layout.activity_main);

        manager = new CardStackLayoutManager(this, this);
        cardStackView = findViewById(R.id.card_stack_view);
        adapter = new CardStackAdapter();

        drawerLayout = findViewById(R.id.drawer_layout);

        setupNavigation();
        setupCardStackView();
        setupButton();
        reloadCardsFromSharedPreferences();
        reloadCreatedCardsFromSharedPreferences();

        adapter.setCards(generateCards());

        setupStatistics();

        startToastIfInFamily();

        // each time drop all alarms and re-schedule it. Just to make sure it is properly setup even after settings changed.
        Utils.cancelAllAlarms();
        AlarmScheduler.scheduleAllAlarms();

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
        if(direction == Direction.Right && swipedCard != null && !swipedUsingButton){

            // only then add it to statistics if it is not the Leading Idea card
            if(!(swipedCard.getCardClass().equalsIgnoreCase("RedCard") && swipedCard.getCardGoal().equalsIgnoreCase(getString(R.string.LeadingIdea_cardLabel)))){
                addToStatistics(swipedCard);
            }
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

    private void startToastIfInFamily(){

        if(Settings.getInstance().isInFamily() && Integer.valueOf(Utils.getBlueCardsCountFromWeek(statisticsHolder)) < 1 && Calendar.DAY_OF_WEEK > 4){
            Random rand = new Random();
            int value = rand.nextInt(100);
            if(value<50){
                Toast.makeText(MainActivity.this, MyGoals.getAppContext().getString(R.string.MainActivity_Toast_inFamily1), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, MyGoals.getAppContext().getString(R.string.MainActivity_Toast_inFamily2), Toast.LENGTH_LONG).show();
            }
        }

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
                // only then add it to statistics if it is not the Leading Idea card

                CardShape cardShape = adapter.getCard(manager.getTopPosition());

                if(!(cardShape.getCardClass().equalsIgnoreCase("RedCard") && cardShape.getCardGoal().equalsIgnoreCase(getString(R.string.LeadingIdea_cardLabel)))){
                    addToStatistics(adapter.getCard(manager.getTopPosition()));
                }

            }

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

                    case R.id.own_cards:
                        checkOwnCards();
                        break;

                    case R.id.plan_the_card_set:
                        planTheCardSet();
                        break;

                    case R.id.leadingIdea:
                        planTheLeadingIdea();
                        break;

                    case R.id.settings:
                        settings();
                        break;

                    case R.id.about:
                        about();
                        break;
                }

                    drawerLayout.closeDrawers();



                return true;
            }
        });
    }

    private void planTheLeadingIdea(){
        Intent planTheLeadingIdea = new Intent(MainActivity.this, LeadingIdeaActivity.class);
        startActivity(planTheLeadingIdea);
    }

    private void planTheCardSet(){
        Intent planTheWeekIntent = new Intent(MainActivity.this, PlanTheCardSetActivity.class);
        startActivity(planTheWeekIntent);
    }

    private void checkStatistics(){
        Intent statisticsIntent = new Intent(MainActivity.this, StatisticsActivity.class);
        startActivity(statisticsIntent);
    }

    private void checkOwnCards(){
        Intent ownCardsIntent = new Intent(MainActivity.this, CreatedCardsActivity.class);
        startActivity(ownCardsIntent);
    }

    private void settings(){
        Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(settingsIntent);
    }

    private void about(){
        Intent aboutIntent = new Intent(MainActivity.this, InfoActivity.class);
        startActivity(aboutIntent);
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

        //The leading Idea card comes always to the front
        //check if we have a leading Idea card on front, if not add it.
        if(ActualCardSet.getInstance().getCardShapeList().size() == 0){
            String leadingIdea = Utils.loadLeadingIdeaFromSharedPreferences();
            if(leadingIdea != null && !leadingIdea.equalsIgnoreCase("") ){

                ActualCardSet.getInstance().addCardToFront(generateLeadingIdeaCard(leadingIdea));

            } else {

                ActualCardSet.getInstance().addCardToFront(generateLeadingIdeaCard(null));

            }
        } else if(!(ActualCardSet.getInstance().getCardShapeList().get(0).getCardClass().equalsIgnoreCase("RedCard") && ActualCardSet.getInstance().getCardShapeList().get(0).getCardGoal().equalsIgnoreCase(getString(R.string.LeadingIdea_cardLabel)))){
            String leadingIdea = Utils.loadLeadingIdeaFromSharedPreferences();
            if(leadingIdea != null && !leadingIdea.equalsIgnoreCase("") ){

                ActualCardSet.getInstance().addCardToFront(generateLeadingIdeaCard(leadingIdea));

            } else {

                ActualCardSet.getInstance().addCardToFront(generateLeadingIdeaCard(null));

            }
        }







        cardShapesList.addAll(ActualCardSet.getInstance().getCardShapeList());
        return cardShapesList;

    }

    private CardShape generateLeadingIdeaCard(String idea){

        CardFactory cardFactory = new CardFactory();

        if(idea != null && !idea.equalsIgnoreCase("") ){

            return cardFactory.getCardShape("RedCard", getString(R.string.LeadingIdea_cardLabel), idea, "https://source.unsplash.com/Xq1ntWruZQI/600x800");

        } else {
            return cardFactory.getCardShape("RedCard", getString(R.string.LeadingIdea_cardLabel), getString(R.string.LeadingIdeaActivity_default_entry), "https://source.unsplash.com/Xq1ntWruZQI/600x800");
        }

/*
        if(LeadingIdeaActivity.leadingIdeaDetail!= null && !LeadingIdeaActivity.leadingIdeaDetail.isEmpty()){
            return cardFactory.getCardShape("RedCard", getString(R.string.LeadingIdea_cardLabel), LeadingIdeaActivity.leadingIdeaDetail, "https://source.unsplash.com/Xq1ntWruZQI/600x800");
        } else {
            return cardFactory.getCardShape("RedCard", getString(R.string.LeadingIdea_cardLabel), getString(R.string.LeadingIdeaActivity_default_entry), "https://source.unsplash.com/Xq1ntWruZQI/600x800");
        }*/

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
            ArrayList<CardShape> cardJsonArray = gson.fromJson(cardsJsonFormat, typeOfSrc);
            adapter.setCards(cardJsonArray);
            adapter.notifyDataSetChanged();

            ActualCardSet.getInstance().setCardShapeList(cardJsonArray);

        }
    }

    private void reloadCreatedCardsFromSharedPreferences(){
        Utils.loadCreatedCardsFromSharedPreferencesNew();
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
        int statisticsYear = Utils.loadStatisticsYearFromSharedPreferences();

        // meaning no previous saving was done.
        if(statisticsYear == Utils.STATISTICS_YEAR_DEFAULT){
            statisticsHolder = StatisticsHolder.getInstance();
        } else if (statisticsYear != Calendar.getInstance().get(Calendar.YEAR)){
            // start new yer
            StatisticsHolder.resetStatisticsHolder();
            statisticsHolder = StatisticsHolder.getInstance();
        } else {
            // we are still in the same year, reload data from SharedPreferences
            statisticsHolder = StatisticsHolder.getInstance();
            reloadStatisticsFromSharedPreferences();
        }

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
            case "LightGreenCard":
                todayStatistics.incrementLightGreenCardCount();
                break;
            case "RedCard":
                todayStatistics.incrementRedCardCount();
                break;
        }

        statisticsHolder.addStatistic(todayStatistics, today);
    }

    private void createNotificationChannel(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "MyGoalReminderChannel";
            String description = "Channel for MyGoal Reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyMyGoals", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }

    }
}
