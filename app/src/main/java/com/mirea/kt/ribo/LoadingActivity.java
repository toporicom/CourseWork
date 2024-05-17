package com.mirea.kt.ribo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import java.util.List;

public class LoadingActivity extends AppCompatActivity {
    private final String server = "https://news.rambler.ru/rss/";
    private TextView tv;

    private boolean loadTheme() {
        SharedPreferences preferences = getSharedPreferences("ThemeSwitcher", MODE_PRIVATE);
        return preferences.getBoolean("isDarkTheme", false);
    }

    private static List<String> getJSONObject(String server, String serverPath) {
        try {

            HTTPRunnableRambler httpRunnable = new HTTPRunnableRambler(server + serverPath);
            Thread th = new Thread(httpRunnable);
            th.start();
            th.join();
            return httpRunnable.getResult();

        } catch (InterruptedException e) {
            Log.i("Error", "Error!");
            throw new RuntimeException(e);
        }
    }

    private void writeToDb(List<News> resultArrOfNews, String tableName) {
        SQLiteDatabase db = getApplicationContext().openOrCreateDatabase("news.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + tableName + " (title TEXT, link TEXT, date TEXT, description TEXT, category TEXT, img TEXT, id INTEGER PRIMARY KEY AUTOINCREMENT)");
        for (News news : resultArrOfNews) {
            db.execSQL("INSERT OR IGNORE INTO " + tableName + " VALUES ("
                    + "'" + news.getTitle() + "',"
                    + "'" + news.getLink() + "',"
                    + "'" + news.getDate() + "',"
                    + "'" + news.getDescription() + "',"
                    + "'" + news.getCategory() + "',"
                    + "'" + news.getImg() + "',"
                    + "'" + news.getId() + "'" +
                    ");");
        }
        db.close();
    }

    private void createSortThread(String serverPath, String tableName) {
        SQLRunnable sqlRunnable = new SQLRunnable(getJSONObject(server, serverPath));
        Thread th = new Thread(sqlRunnable);
        th.start();
        try {
            th.join();
            List<News> resultArrOfNews = sqlRunnable.getResult();
            writeToDb(resultArrOfNews, tableName);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (loadTheme()) {
            setTheme(R.style.Theme_ThemeSwitcher_Dark);
        } else {
            setTheme(R.style.Theme_ThemeSwitcher_Light);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        createSortThread("incidents", "INCIDENTS");
        createSortThread("community", "COMMUNITY");
        createSortThread("moscow_city", "MOSCOW");
        createSortThread("politics", "POLITICS");
        tv = findViewById(R.id.loadingError);
        tv.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        Handler handler = new Handler();
        tv.setText("Loading...");
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent homePageIntent = new Intent(getApplicationContext(), HomePage.class);
                startActivity(homePageIntent);
            }
        }, 2000);
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}