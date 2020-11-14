package com.chad.wallpaperapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.chad.wallpaperapp.Adapter.WallpaperAdapter;
import com.chad.wallpaperapp.Model.Wallpaper;
import com.chad.wallpaperapp.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WallpaperAdapter wallpaperAdapter;
    private List<Wallpaper> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }

    private void initialize() {

        recyclerView = findViewById(R.id.recyclerView);
        list = new ArrayList<>();
        wallpaperAdapter = new WallpaperAdapter(this, list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);

        recyclerView.setAdapter(wallpaperAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);
    }
}