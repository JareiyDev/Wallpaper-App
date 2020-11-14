package com.chad.wallpaperapp.Activities;

import android.os.Bundle;
import android.view.View;
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
import com.chad.wallpaperapp.Adapter.WallpaperAdapter;
import com.chad.wallpaperapp.Constants.Constants;
import com.chad.wallpaperapp.Model.Wallpaper;
import com.chad.wallpaperapp.R;

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

        setBarColors();
        initialize();
        fetchWallpaper();
        searchWallpaper();
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
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
    }


}