package com.example.imageresize;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    EditText imageHeight;
    EditText imageWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        imageHeight = findViewById(R.id.editHeight);
        imageWidth = findViewById(R.id.editWidth);

        //Request permission from user to write to the storage of the device
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    //The method that handles the open camera intent
    public void onCameraButtonPressed(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        PackageManager packageManager = getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            startActivityForResult(intent, 1);
        }
    }

    //Callback method when the camera user has taken the photo and the actity gets a result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == MainActivity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(bitmap);

            //Gets the height and width of the new imageview that was set from the photo and saves it in variables
            String height = Integer.toString(imageView.getMeasuredHeight());
            String width = Integer.toString(imageView.getMeasuredWidth());

            //sets the edittext fields from the new variables
            imageHeight.setText(height);
            imageWidth.setText(width);
        }
    }

    //gets height and width that the user put into the edittext fields and saves them in the variables, then get the original image and creates a new resized image from that
    public void onResizeButtonPressed(View view) {
        int newHeight = Integer.parseInt(imageHeight.getText().toString());
        int newWidth = Integer.parseInt(imageWidth.getText().toString());

        //orinal
        Bitmap originalImage = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

        //new resized
        Bitmap resized = Bitmap.createScaledBitmap(originalImage, newWidth, newHeight, true);

        //imageview is set from the resized
        imageView.setImageBitmap(resized);
    }

    //Saves the imageview to the storage of the device
    public void onSaveButtonPressed(View view) {
        imageView.setDrawingCacheEnabled(true);
        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Photo", "Description");

        //Clear the fields after the save
        imageWidth.setText("");
        imageHeight.setText("");
        imageView.setImageResource(0);
    }

}
