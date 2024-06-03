package com.mirea.kt.ribo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AboutMe extends AppCompatActivity {
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
        setContentView(R.layout.activity_about_me);
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