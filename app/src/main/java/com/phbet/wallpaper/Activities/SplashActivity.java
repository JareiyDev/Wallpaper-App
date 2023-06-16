package com.phbet.wallpaper.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.bumptech.glide.util.Util;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.phbet.wallpaper.R;
import com.phbet.wallpaper.Utils;
import com.phbet.wallpaper.WebviewActivity;

public class SplashActivity extends AppCompatActivity {

    private AdView mAdView;
    private View overlay;
    private ConstraintLayout viewAlert;
    private TextView retry;
    private TextView close;
    private TextView description;
    String link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //init view
        mAdView = findViewById(R.id.adView);
        description = findViewById(R.id.description);
        overlay = (View) findViewById(R.id.overlay);
        viewAlert = (ConstraintLayout) findViewById(R.id.viewAlert);
        retry = (TextView) findViewById(R.id.retry);
        close = (TextView) findViewById(R.id.close);
        Utils.getRealeaseKey(this);
        // check condition
        if (Utils.checkNetWork(SplashActivity.this) == 1) {
            overlay.setVisibility(View.GONE);
            viewAlert.setVisibility(View.GONE);
            if (Utils.checkIPAdress(SplashActivity.this) == 1) {
                getData();
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 3500);
            }
        } else {
            overlay.setVisibility(View.VISIBLE);
            viewAlert.setVisibility(View.VISIBLE);
        }

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.checkNetWork(SplashActivity.this) == 1) {
                    overlay.setVisibility(View.GONE);
                    viewAlert.setVisibility(View.GONE);
                    if (Utils.checkIPAdress(SplashActivity.this) == 1) {
                        getData();
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }, 3500);
                    }
                } else {
                    description.setText("Sorry, unable to load data. Please check your internet connection and try again later.");
                }
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        adsListener();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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

    private void getData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //here you will get the data
                        link = dataSnapshot.child("wallpaper").getValue().toString();
                        if (link.isEmpty()) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }, 3500);

                        } else {
                            try {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("link",link);
                                        Intent intent = new Intent(SplashActivity.this, WebviewActivity.class);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                        finish();
                                    }
                                }, 3500);
                            } catch (Exception exception) {

                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("TAG", "onCancelled: " + databaseError.getDetails());
                    }
                });
    }
}