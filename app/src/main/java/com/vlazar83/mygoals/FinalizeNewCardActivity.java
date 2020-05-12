package com.vlazar83.mygoals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

public class FinalizeNewCardActivity extends AppCompatActivity {

    private CreatedCardSet createdCardSet;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView takenPicture;
    private String currentPhotoPath;
    private Boolean pictureTakenFromCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalize_new_card);
        Toolbar toolbar = findViewById(R.id.toolbar_for_finalize_new_card);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EditText firstDetails = findViewById(R.id.enter_first_details);
        EditText secondDetails = findViewById(R.id.enter_second_details);
        ImageView takePictureButton = findViewById(R.id.take_picture);
        takenPicture = findViewById(R.id.taken_picture);
        pictureTakenFromCamera = false;

        FloatingActionButton createNewCardFloatingButton = findViewById(R.id.create_new_card_button);
        setupFloatingActionButtonColor(createNewCardFloatingButton);

        setupCreatedCardSet();

        createNewCardFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(CreateNewCardActivity.cardToPassOnForCreation != null && !pictureTakenFromCamera){

                    CardShape selectedSampleCard = CreateNewCardActivity.cardToPassOnForCreation;
                    CardShape newCard = createTheNewCard(selectedSampleCard.getCardClass(), firstDetails.getText().toString(), secondDetails.getText().toString(), selectedSampleCard.getCardUrl());
                    createdCardSet.addCard(newCard);
                    saveCreatedCardsToSharedPreferences();
                } else if (CreateNewCardActivity.cardToPassOnForCreation != null && pictureTakenFromCamera){
                    CardShape selectedSampleCard = CreateNewCardActivity.cardToPassOnForCreation;
                    CardShape newCard = createTheNewCard(selectedSampleCard.getCardClass(), firstDetails.getText().toString(), secondDetails.getText().toString(), currentPhotoPath);
                    createdCardSet.addCard(newCard);
                    saveCreatedCardsToSharedPreferences();
                }

                backToNewCardCreation();
            }
        });

        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });


    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.vlazar83.mygoals.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Get the dimensions of the View
            int targetW = takenPicture.getWidth();
            int targetH = takenPicture.getHeight();

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;

            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
            takenPicture.setImageBitmap(bitmap);
            pictureTakenFromCamera = true;
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void backToNewCardCreation(){
        Intent backToNewCardIntent = new Intent(FinalizeNewCardActivity.this, CreateNewCardActivity.class);
        startActivity(backToNewCardIntent);
    }

    private void setupCreatedCardSet(){
        createdCardSet = CreatedCardSet.getInstance();
        //createdCardSet.emptyList();
    }

    private void setupFloatingActionButtonColor(FloatingActionButton createNewCardFloatingButton){
        if(CreateNewCardActivity.cardToPassOnForCreation != null){
            switch(CreateNewCardActivity.cardToPassOnForCreation.getCardClass()){
                case "BlueCard":
                    createNewCardFloatingButton.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
                    break;

                case "RedCard":
                    createNewCardFloatingButton.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    break;

                case "GreenCard":
                    createNewCardFloatingButton.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
            }
        }
    }

    private CardShape createTheNewCard(String cardClass, String cardGoal, String cardCity, String cardUrl){
        CardFactory cardFactory = new CardFactory();
        return cardFactory.getCardShape(cardClass, cardGoal, cardCity, cardUrl);

    }

    private void saveCreatedCardsToSharedPreferences(){
        //Create our gson instance
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(CardShape.class, new InterfaceAdapter());
        Gson gson = builder.create();
        //Let's serialize our array
        Type typeOfSrc = new TypeToken<Collection<CardShape>>(){}.getType();
        String cardsJsonFormat = gson.toJson(CreatedCardSet.getInstance().getCardShapeList(), typeOfSrc);
        Log.w("Cards in Json Format:", cardsJsonFormat);

        // save actual card set to SharedPreferences
        Utils.saveCreatedCardsToSharedPreferences(cardsJsonFormat);
    }


}
