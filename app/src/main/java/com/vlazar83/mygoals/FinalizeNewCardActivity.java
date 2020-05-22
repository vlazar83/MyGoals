package com.vlazar83.mygoals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

public class FinalizeNewCardActivity extends AppCompatActivity {

    private CreatedCardSet createdCardSet;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int MY_PERMISSIONS_REQUEST_CAMERA = 10;
    private ImageView takenPicture, rotatePicture;
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
        rotatePicture = findViewById(R.id.rotate_picture);
        pictureTakenFromCamera = false;

        FloatingActionButton createNewCardFloatingButton = findViewById(R.id.create_new_card_button);
        setupFloatingActionButtonColor(createNewCardFloatingButton);

        setupCreatedCardSet();

        createNewCardFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(firstDetails.getText().toString() != null && !firstDetails.getText().toString().isEmpty() && secondDetails.getText().toString() != null && !secondDetails.getText().toString().isEmpty()){
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
                } else {
                    Toast.makeText(FinalizeNewCardActivity.this, getString(R.string.FinalizeNewCard_Toast_Message),
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(FinalizeNewCardActivity.this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(FinalizeNewCardActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);
                } else {
                    // Permission has already been granted
                    dispatchTakePictureIntent();
                }

            }
        });

        rotatePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(pictureTakenFromCamera){
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
                    // here inside the currentPhotoPath is modified!
                    fileForBitmap(rotateImage(bitmap, 90));

                    bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

                    takenPicture.setImageBitmap(bitmap);

                }

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
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    dispatchTakePictureIntent();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
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

                case "LightGreenCard":
                    createNewCardFloatingButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.light_green)));
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

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    private void fileForBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File file = null;
        try {
            file = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileOutputStream fo = new FileOutputStream(file);
            fo.write(bytes.toByteArray());
            fo.flush();
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
