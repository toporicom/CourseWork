package com.mirea.kt.ribo;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<News> news;
    private Context context;

    public RecyclerViewAdapter(List<News> news) {
        this.news = news;
    }
    private boolean isLightTheme(Context context){
        return context.getTheme().toString().contains("Theme.Material3.Light");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new RecyclerView.ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.news, parent, false)
        ) {
        };
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FrameLayout frameLayout = holder.itemView.findViewById(R.id.frameLayout);
        TextView tvTitle = holder.itemView.findViewById(R.id.tvTitle);
        TextView tvDate = holder.itemView.findViewById(R.id.tvDate);
        ImageView imgView = holder.itemView.findViewById(R.id.imgView);
        ImageView backgroundImg = holder.itemView.findViewById(R.id.backgroundImg);
        if (isLightTheme(frameLayout.getContext())) {
            imgView.setImageResource(R.drawable.day_img);
        } else {
            imgView.setImageResource(R.drawable.night_img);
        }
        tvTitle.setText(news.get(position).getTitle());
        tvDate.setText(news.get(position).getDate().substring(4, news.get(position).getDate().length() - 6));
        LoadingFromNet lfn = new LoadingFromNet(news.get(position).getImg().substring(news.get(position).getImg().lastIndexOf("/") + 1));
        Thread th = new Thread(lfn);
        th.start();
        try {
            th.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Drawable drawable = lfn.result();
        backgroundImg.setImageDrawable(drawable);
        frameLayout.setOnClickListener(v -> {
            Intent intent = new Intent(context, NewsActivity.class);
            intent.putExtra("title", news.get(holder.getAdapterPosition()).getTitle());
            intent.putExtra("description", news.get(holder.getAdapterPosition()).getDescription());
            intent.putExtra("date", news.get(holder.getAdapterPosition()).getDate());
            intent.putExtra("link", news.get(holder.getAdapterPosition()).getLink());
            intent.putExtra("img", news.get(holder.getAdapterPosition()).getImg());
            startActivity(context, intent, null);
        });

    }
    @Override
    public int getItemCount() {
        return this.news.size();
    }

}
