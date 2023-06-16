package com.phbet.wallpaper.Activities;

import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.phbet.wallpaper.Constants.Constants;
import com.phbet.wallpaper.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;

public class FullWallpaperActivity extends AppCompatActivity {
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_wallpaper);

       // Objects.requireNonNull(getSupportActionBar()).hide();
        setBarColors();
        initialize();
    }

    private void initialize() {

        PhotoView photoView = findViewById(R.id.imageFullWallpaper);
        MaterialButton buttonSetWallpaper = findViewById(R.id.buttonSetWallpaper);
        MaterialButton buttonDownloadImage = findViewById(R.id.buttonDownloadWallpaper);
        mAdView = findViewById(R.id.adView);
        String originalUrl;

        originalUrl = getIntent().getStringExtra(Constants.ORIGINAL_URL);
        Glide.with(this).load(originalUrl).placeholder(R.drawable.placeholder).into(photoView);

        buttonDownloadImage.setOnClickListener(v -> {
            DownloadManager downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(originalUrl);
            DownloadManager.Request request = new DownloadManager.Request(uri);

            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            downloadManager.enqueue(request);

            Toast.makeText(this, "Download started!", Toast.LENGTH_SHORT).show();
        });
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
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        adsListener();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void setBarColors() {
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
    }
    private void adsListener() {
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        });
    }
}