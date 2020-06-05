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
        addCard(cardFactory.getCardShape("RedCard", MyGoals.getAppContext().getString(R.string.DefaultCard_red_goal), MyGoals.getAppContext().getString(R.string.DefaultCard_red_geneic), getUriToResource(MyGoals.getAppContext(),R.drawable.red_card_generic).toString()));
        addCard(cardFactory.getCardShape("RedCard", MyGoals.getAppContext().getString(R.string.DefaultCard_red_goal), MyGoals.getAppContext().getString(R.string.DefaultCard_red1), getUriToResource(MyGoals.getAppContext(),R.drawable.red_card_1).toString()));
        addCard(cardFactory.getCardShape("RedCard", MyGoals.getAppContext().getString(R.string.DefaultCard_red_goal), MyGoals.getAppContext().getString(R.string.DefaultCard_red2), getUriToResource(MyGoals.getAppContext(),R.drawable.red_card_2).toString()));
        addCard(cardFactory.getCardShape("RedCard", MyGoals.getAppContext().getString(R.string.DefaultCard_red_goal), MyGoals.getAppContext().getString(R.string.DefaultCard_red3), getUriToResource(MyGoals.getAppContext(),R.drawable.red_card_3).toString()));

        addCard(cardFactory.getCardShape("BlueCard", MyGoals.getAppContext().getString(R.string.DefaultCard_blue_goal), MyGoals.getAppContext().getString(R.string.DefaultCard_blue_geneic), getUriToResource(MyGoals.getAppContext(),R.drawable.blue_card_generic).toString()));
        addCard(cardFactory.getCardShape("BlueCard", MyGoals.getAppContext().getString(R.string.DefaultCard_blue_goal), MyGoals.getAppContext().getString(R.string.DefaultCard_blue1), getUriToResource(MyGoals.getAppContext(),R.drawable.blue_card_1).toString()));
        addCard(cardFactory.getCardShape("BlueCard", MyGoals.getAppContext().getString(R.string.DefaultCard_blue_goal), MyGoals.getAppContext().getString(R.string.DefaultCard_blue2), getUriToResource(MyGoals.getAppContext(),R.drawable.blue_card_2).toString()));

        addCard(cardFactory.getCardShape("GreenCard", MyGoals.getAppContext().getString(R.string.DefaultCard_green_goal), MyGoals.getAppContext().getString(R.string.DefaultCard_green_geneic), getUriToResource(MyGoals.getAppContext(),R.drawable.green_card_generic).toString()));
        addCard(cardFactory.getCardShape("LightGreenCard", MyGoals.getAppContext().getString(R.string.DefaultCard_green_goal), MyGoals.getAppContext().getString(R.string.DefaultCard_green_card_introvert_1), getUriToResource(MyGoals.getAppContext(),R.drawable.green_card_introvert_1).toString()));
        addCard(cardFactory.getCardShape("GreenCard", MyGoals.getAppContext().getString(R.string.DefaultCard_green_goal), MyGoals.getAppContext().getString(R.string.DefaultCard_green_card_extrovert_1), getUriToResource(MyGoals.getAppContext(),R.drawable.green_card_extrovert_1).toString()));
        addCard(cardFactory.getCardShape("LightGreenCard", MyGoals.getAppContext().getString(R.string.DefaultCard_green_goal), MyGoals.getAppContext().getString(R.string.DefaultCard_green_card_introvert_2), getUriToResource(MyGoals.getAppContext(),R.drawable.green_card_introvert_2).toString()));
        addCard(cardFactory.getCardShape("GreenCard", MyGoals.getAppContext().getString(R.string.DefaultCard_green_goal), MyGoals.getAppContext().getString(R.string.DefaultCard_green_card_extrovert_2), getUriToResource(MyGoals.getAppContext(),R.drawable.green_card_extrovert_2).toString()));


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
