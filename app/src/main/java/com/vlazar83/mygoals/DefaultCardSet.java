package com.vlazar83.mygoals;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;

import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class DefaultCardSet {

    private static DefaultCardSet uniqueInstance = null;
    private ArrayList<CardShape> cardShapesList;

    private DefaultCardSet(){
        cardShapesList = new ArrayList<CardShape>();
        fillWithDefaultSet();
    }

    public static synchronized DefaultCardSet getInstance(){

        if(uniqueInstance == null){
            uniqueInstance = new DefaultCardSet();
        }

        return uniqueInstance;

    }

    private void fillWithDefaultSet(){
        CardFactory cardFactory = new CardFactory();
        addCard(cardFactory.getCardShape("RedCard", "Work", "worships on Sundays", getUriToResource(MyGoals.getAppContext(),R.drawable.red_card_1).toString()));
        addCard(cardFactory.getCardShape("RedCard", "Work", "teaching about God's love", getUriToResource(MyGoals.getAppContext(),R.drawable.red_card_2).toString()));
        addCard(cardFactory.getCardShape("RedCard", "Work", "preparations...", getUriToResource(MyGoals.getAppContext(),R.drawable.red_card_3).toString()));
        addCard(cardFactory.getCardShape("RedCard", "Work", "other work related activities", getUriToResource(MyGoals.getAppContext(),R.drawable.red_card_generic).toString()));

        addCard(cardFactory.getCardShape("BlueCard", "Family", "visit your family", getUriToResource(MyGoals.getAppContext(),R.drawable.blue_card_1).toString()));
        addCard(cardFactory.getCardShape("BlueCard", "Family", "family trip in the woods", getUriToResource(MyGoals.getAppContext(),R.drawable.blue_card_2).toString()));
        addCard(cardFactory.getCardShape("BlueCard", "Family", "other activities with your loved ones", getUriToResource(MyGoals.getAppContext(),R.drawable.blue_card_generic).toString()));

        addCard(cardFactory.getCardShape("LightGreenCard", "Self-Time", "reading a book", getUriToResource(MyGoals.getAppContext(),R.drawable.green_card_introvert_1).toString()));
        addCard(cardFactory.getCardShape("GreenCard", "Self-Time", "board game night with the family", getUriToResource(MyGoals.getAppContext(),R.drawable.green_card_extrovert_1).toString()));
        addCard(cardFactory.getCardShape("LightGreenCard", "Self-Time", "pray", getUriToResource(MyGoals.getAppContext(),R.drawable.green_card_introvert_2).toString()));
        addCard(cardFactory.getCardShape("GreenCard", "Self-Time", "football with friends", getUriToResource(MyGoals.getAppContext(),R.drawable.green_card_extrovert_2).toString()));
        addCard(cardFactory.getCardShape("GreenCard", "Self-Time", "other activities for yourself", getUriToResource(MyGoals.getAppContext(),R.drawable.green_card_generic).toString()));

    }

    public void addCard(CardShape cardShape){
        cardShapesList.add(cardShape);
    }

    public List<CardShape> getCardShapeList(){
        return cardShapesList;
    }

    public static final Uri getUriToResource(@NonNull Context context,
                                             @AnyRes int resId)
            throws Resources.NotFoundException {
        /** Return a Resources instance for your application's package. */
        Resources res = context.getResources();
        /**
         * Creates a Uri which parses the given encoded URI string.
         * @param uriString an RFC 2396-compliant, encoded URI
         * @throws NullPointerException if uriString is null
         * @return Uri for this given uri string
         */
        Uri resUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + res.getResourcePackageName(resId)
                + '/' + res.getResourceTypeName(resId)
                + '/' + res.getResourceEntryName(resId));
        /** return uri */
        return resUri;
    }

}
