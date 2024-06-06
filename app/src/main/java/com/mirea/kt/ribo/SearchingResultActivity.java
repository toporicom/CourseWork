package com.mirea.kt.ribo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchingResultActivity extends AppCompatActivity {

    private final List<String> tables = new ArrayList<String>() {{
        add(0, "MOSCOW");
        add(1, "POLITICS");
        add(2, "COMMUNITY");
        add(3, "INCIDENTS");
    }};
    private boolean isDarkTheme;

    private List<News> findInDB(Context context, String query) {
        List<News> news = new ArrayList<>();
        SQLiteDatabase db = context.openOrCreateDatabase("news.db", MODE_PRIVATE, null);
        for (int i = 0; i < 4; i++) {
            String tableName = tables.get(i);
            String title, link, date, description, category, img;
            Cursor cursor = db.rawQuery("SELECT * FROM " + tableName + " ORDER BY date", null);
            int index = 1;
            while (cursor.moveToNext()) {
                if (cursor.getString(0).toLowerCase().contains(query.toLowerCase())){
                    title = cursor.getString(0);
                    link = cursor.getString(1);
                    date = cursor.getString(2);
                    description = cursor.getString(3);
                    category = cursor.getString(4);
                    img = cursor.getString(5);
                    News newsObj = new News(title, link, date, description, category, img, index);
                    news.add(index - 1, newsObj);
                    index++;
                }
            }
            Collections.reverse(news);
            cursor.close();
        }
        db.close();
        return news;
    }
    private void saveTheme(boolean isDarkTheme) {
        SharedPreferences preferences = getSharedPreferences("ThemeSwitcher", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isDarkTheme", isDarkTheme);
        editor.apply();
    }
    private boolean loadTheme() {
        SharedPreferences preferences = getSharedPreferences("ThemeSwitcher", MODE_PRIVATE);
        boolean theme = preferences.getBoolean("isDarkTheme", false);
        Log.i("theme_searching", (theme) ? "Dark" : "Light");
        return theme;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (loadTheme()) {
            setTheme(R.style.Dark);
            isDarkTheme = true;
        } else {
            setTheme(R.style.Light);
            isDarkTheme = false;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching_result);
        TextView tvRes = findViewById(R.id.searingRes);
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
        List<News> news = findInDB(getApplicationContext(), getIntent().getStringExtra("query"));
        if (!news.isEmpty()){
            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            RecyclerViewAdapter rvAdapter = new RecyclerViewAdapter(news);
            recyclerView.setAdapter(rvAdapter);
        }else {
            tvRes.setVisibility(View.VISIBLE);
        }
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