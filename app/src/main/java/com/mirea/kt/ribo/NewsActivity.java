package com.mirea.kt.ribo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class NewsActivity extends AppCompatActivity {

    private boolean loadTheme() {
        SharedPreferences preferences = getSharedPreferences("ThemeSwitcher", MODE_PRIVATE);
        return preferences.getBoolean("isDarkTheme", false);
    }
    private void saveTheme(boolean isDarkTheme) {
        SharedPreferences preferences = getSharedPreferences("ThemeSwitcher", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isDarkTheme", isDarkTheme);
        editor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (loadTheme()) {
            setTheme(R.style.Dark);
        } else {
            setTheme(R.style.Light);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        TextView tvTitle, tvDescription, tvDate, tvLink, tvShare;
        ImageView imageView;
        imageView = findViewById(R.id.imgViewNews);
        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);
        tvDate = findViewById(R.id.tvDate);
        tvLink = findViewById(R.id.tvLink);
        tvShare = findViewById(R.id.tvShare);

        String link = getIntent().getStringExtra("link");
        tvTitle.setText(getIntent().getStringExtra("title"));
        tvDescription.setText(getIntent().getStringExtra("description"));
        tvDate.setText(getIntent().getStringExtra("date"));
        tvLink.setText(link);
        tvLink.setMovementMethod(LinkMovementMethod.getInstance());
        String img = getIntent().getStringExtra("img");

        LoadingFromNet lfn = new LoadingFromNet(img.substring(img.lastIndexOf("/") + 1));
        Thread th = new Thread(lfn);
        th.start();
        try {
            th.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Drawable drawable = lfn.result();
        imageView.setImageDrawable(drawable);

        Toolbar tb = findViewById(R.id.toolBar);
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(androidx.constraintlayout.widget.R.attr.colorPrimary, typedValue, true);
        tb.setBackgroundColor(typedValue.data);
        setSupportActionBar(tb);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String body = "Поделиться новостью";
                intent.putExtra(Intent.EXTRA_TEXT, body);
                intent.putExtra(Intent.EXTRA_TEXT, link);
                startActivity(Intent.createChooser(intent, "Share using"));
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuabout, menu);
        if (loadTheme()) {
            menu.findItem(R.id.Action_theme).setIcon(R.drawable.half_moon_189162);
        } else {
            menu.findItem(R.id.Action_theme).setIcon(R.drawable.sun_869869);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.Action_theme) {
            if (loadTheme()) {
                item.setIcon(R.drawable.half_moon_189162);
                setTheme(R.style.Light);
                saveTheme(false);
            } else {
                item.setIcon(R.drawable.sun_869869);
                setTheme(R.style.Dark);
                saveTheme(true);
            }
            recreate();
        }
        if (item.getItemId() == android.R.id.home){
            Intent intent = new Intent(this, HomePage.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}