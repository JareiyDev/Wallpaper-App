package com.chad.wallpaperapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.chad.wallpaperapp.Constants.Constants;
import com.chad.wallpaperapp.R;
import com.github.chrisbanes.photoview.PhotoView;

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

        String originalUrl = getIntent().getStringExtra(Constants.ORIGINAL_URL);
        Glide.with(this).load(originalUrl).into(photoView);
    }

    private void setBarColors() {
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
    }

}