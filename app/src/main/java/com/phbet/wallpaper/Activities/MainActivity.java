package com.phbet.wallpaper.Activities;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.phbet.wallpaper.Adapter.WallpaperAdapter;
import com.phbet.wallpaper.Constants.Constants;
import com.phbet.wallpaper.Model.Wallpaper;
import com.phbet.wallpaper.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private WallpaperAdapter wallpaperAdapter;
    private List<Wallpaper> list;
    private int pageNumber = 1;
    private AdView mAdView;
    ImageView imageSearch;
    EditText inputSearch;

    private String url = "https://api.pexels.com/v1/curated/?page="+pageNumber+"&per_page=80";

    private Boolean isScrolling = false;

    int currentItems;
    int totalItems;
    int scrollOutItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAdView = findViewById(R.id.adView);
        setBarColors();
        initialize();
        fetchWallpaper();
        searchWallpaper();
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        adsListener();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void initialize() {

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        list = new ArrayList<>();
        wallpaperAdapter = new WallpaperAdapter(this, list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        inputSearch = findViewById(R.id.inputSearch);
        imageSearch = findViewById(R.id.imageSearch);

        recyclerView.setAdapter(wallpaperAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems = gridLayoutManager.getChildCount();
                totalItems = gridLayoutManager.getItemCount();
                scrollOutItems = gridLayoutManager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    isScrolling = false;
                    fetchWallpaper();
                }
            }
        });
    }

    public void fetchWallpaper() {

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                response -> {

                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("photos");

                        int length = jsonArray.length();

                        for (int i = 0; i < length; i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            int id = object.getInt("id");

                            JSONObject objectImages = object.getJSONObject("src");

                            String originalUrl = objectImages.getString("original");
                            String mediumUrl = objectImages.getString("medium");

                            Wallpaper wallpaper = new Wallpaper(id, originalUrl, mediumUrl);
                            list.add(wallpaper);
                        }

                        wallpaperAdapter.notifyDataSetChanged();
                        pageNumber++;

                    } catch (JSONException e) {
                        Toast.makeText(this, "JSON Error: " + e, Toast.LENGTH_SHORT).show();
                    }

                }, volleyError -> Toast.makeText(this, "API Error: " + volleyError, Toast.LENGTH_SHORT).show()){

            @Override
            public Map<String, String> getHeaders() {

                Map<String, String> params = new HashMap<>();
                params.put("Authorization", Constants.API_KEY);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

    public void searchWallpaper() {
        imageSearch.setOnClickListener(v -> {
            String query = inputSearch.getText().toString().toLowerCase();
            url = "https://api.pexels.com/v1/search/?page="+pageNumber+"&per_page=80&query="+query;
            list.clear();
            fetchWallpaper();
        });
    }

    private void setBarColors() {
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
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