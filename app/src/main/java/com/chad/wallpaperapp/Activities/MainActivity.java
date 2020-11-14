package com.chad.wallpaperapp.Activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chad.wallpaperapp.Adapter.WallpaperAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
        fetchWallpaper();
    }

    private void initialize() {

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        list = new ArrayList<>();
        wallpaperAdapter = new WallpaperAdapter(this, list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);

        recyclerView.setAdapter(wallpaperAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    public void fetchWallpaper() {

        StringRequest request = new StringRequest(
                Request.Method.GET,
                "https://api.pexels.com/v1/curated/?page=1&per_page=80",
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

                    } catch (JSONException e) {
                        Toast.makeText(this, "JSON Error: " + e, Toast.LENGTH_SHORT).show();
                    }

                }, volleyError -> Toast.makeText(this, "API Error: " + volleyError, Toast.LENGTH_SHORT).show()){

            @Override
            public Map<String, String> getHeaders() {

                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "563492ad6f917000010000016ba5c7498c7a4751bca860431dbdc02f");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

}