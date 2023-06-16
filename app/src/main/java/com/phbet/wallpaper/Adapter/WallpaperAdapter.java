package com.phbet.wallpaper.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.phbet.wallpaper.Activities.FullWallpaperActivity;
import com.phbet.wallpaper.Constants.Constants;
import com.phbet.wallpaper.Model.Wallpaper;
import com.phbet.wallpaper.R;

import java.util.List;

public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperAdapter.ViewHolder> {

    private Context context;
    private List<Wallpaper> list;

    public WallpaperAdapter(Context context, List<Wallpaper> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.wallpaper_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getMediumUrl()).into(holder.wallpaperImage);
        holder.wallpaperImage.setOnClickListener(v -> {
            Intent intent = new Intent(context, FullWallpaperActivity.class);
            intent.putExtra(Constants.ORIGINAL_URL, list.get(position).getOriginalUrl());
            System.out.println("Thắng 123 \b" +list.get(position).getOriginalUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView wallpaperImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            wallpaperImage = itemView.findViewById(R.id.wallpaperImage);
        }
    }
}
