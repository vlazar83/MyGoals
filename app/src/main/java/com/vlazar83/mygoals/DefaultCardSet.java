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
        addCard(cardFactory.getCardShape("RedCard", "Istentisztelet", "Vasarnaponkent", getUriToResource(MyGoals.getAppContext(),R.drawable.istentisztelet_szines_600_800).toString()));
        addCard(cardFactory.getCardShape("BlueCard", "Olvasas", "Regenyeket", getUriToResource(MyGoals.getAppContext(),R.drawable.olvasas_szines_600_800).toString()));
        addCard(cardFactory.getCardShape("GreenCard", "Tarsasjatek", "Monopoly", getUriToResource(MyGoals.getAppContext(),R.drawable.tarsasjatek_szines_800_600).toString()));
        addCard(cardFactory.getCardShape("LightGreenCard", "LightGreenCard example", "Kyoto", "https://source.unsplash.com/buF62ewDLcQ/600x800"));
        addCard(cardFactory.getCardShape("RedCard", "put the trash out4", "New York", "https://source.unsplash.com/THozNzxEP3g/600x800"));
        addCard(cardFactory.getCardShape("GreenCard", "put the trash out5", "New York", "https://source.unsplash.com/USrZRcRS2Lw/600x800"));
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
