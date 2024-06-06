package com.mirea.kt.ribo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {
    private void saveTheme(boolean isDarkTheme) {
        SharedPreferences preferences = getSharedPreferences("ThemeSwitcher", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isDarkTheme", isDarkTheme);
        editor.apply();
    }

    private boolean loadTheme() {
        SharedPreferences preferences = getSharedPreferences("ThemeSwitcher", MODE_PRIVATE);
        boolean theme = preferences.getBoolean("isDarkTheme", false);
        Log.i("theme_main", (theme) ? "Dark" : "Light");
        return theme;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (loadTheme()) {
            setTheme(R.style.Dark);
        } else {
            setTheme(R.style.Light);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        List<Fragment> pages = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            PageFragment fragment = PageFragment.getNewInstance(i, loadTheme());
            pages.add(fragment);
        }


        PagerTabStrip pagerTabStrip = findViewById(R.id.pagerTabStrip);
        pagerTabStrip.setTextSize(TypedValue.COMPLEX_UNIT_PT, 9);

        ViewPager viewPager = findViewById(R.id.vpMain);
        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager());
        viewPager.setAdapter(vpAdapter);
        vpAdapter.setPages(pages);

        Toolbar tb = findViewById(R.id.toolBar);
        setSupportActionBar(tb);
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(androidx.constraintlayout.widget.R.attr.colorPrimary, typedValue, true);
        tb.setBackgroundColor(typedValue.data);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuhome, menu);

        if (loadTheme()) {
            menu.findItem(R.id.Action_theme).setIcon(R.drawable.half_moon_189162);
        } else {
            menu.findItem(R.id.Action_theme).setIcon(R.drawable.sun_869869);
        }

        MenuItem searchItem = menu.findItem(R.id.Action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(getApplicationContext(), SearchingResultActivity.class);
                intent.putExtra("query", query);
                startActivity(intent);
                Log.i("Searching intent", "Searching intent, " + query);
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                searchItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
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
        if (item.getItemId() == R.id.Action_about){
            Intent aboutMeIntent = new Intent(this, AboutMe.class);
            startActivity(aboutMeIntent);
        }
        if (item.getItemId() == android.R.id.home){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}