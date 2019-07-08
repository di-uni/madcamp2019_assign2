package com.example.zebal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

public class ImageActivity extends AppCompatActivity {
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image);

        image = findViewById(R.id.full_image_view);

        Intent intent = getIntent();

        image.setImageURI(Uri.parse(intent.getExtras().getString("image")));

    }
}
