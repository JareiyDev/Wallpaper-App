package com.chad.wallpaperapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.wallpaperapp.Constants.Constants;
import com.chad.wallpaperapp.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.Objects;

public class FullWallpaperActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_wallpaper);

        Objects.requireNonNull(getSupportActionBar()).hide();
        setBarColors();
        initialize();
    }

    private void initialize() {

        PhotoView photoView = findViewById(R.id.imageFullWallpaper);
        MaterialButton buttonSetWallpaper = findViewById(R.id.buttonSetWallpaper);

        buttonSetWallpaper.setOnClickListener(v -> {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(FullWallpaperActivity.this);

            Bitmap bitmap = ((BitmapDrawable)photoView.getDrawable()).getBitmap();

            try {
                wallpaperManager.setBitmap(bitmap);
                Toast.makeText(this, "Wallpaper Set", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(FullWallpaperActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });

        String originalUrl = getIntent().getStringExtra(Constants.ORIGINAL_URL);
        Glide.with(this).load(originalUrl).into(photoView);
    }

    private void setBarColors() {
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
    }

}