package com.mirea.kt.ribo;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PageFragment extends Fragment {
    int pageNumber;
    private static final String positionKey = "POSITION_KEY";

    private List<String> tables = new ArrayList<String>() {{
        add(0, "MOSCOW");
        add(1, "POLITICS");
        add(2, "COMMUNITY");
        add(3, "INCIDENTS");
    }};

    public static PageFragment getNewInstance(int position, boolean isDarkTheme) {
        PageFragment pageFragment = new PageFragment();
        Bundle args = new Bundle();
        args.putBoolean("isDarkTheme", isDarkTheme);
        args.putInt(positionKey, position);
        pageFragment.setArguments(args);
        return pageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private List<News> takeFromDb(String tableName, Context context) {
        SQLiteDatabase db = context.openOrCreateDatabase("news.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS " + tableName + " (title TEXT, link TEXT, date TEXT, description TEXT, category TEXT, img TEXT, id INTEGER PRIMARY KEY AUTOINCREMENT)");
        String title, link, date, description, category, img;
        List<News> news = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName + " ORDER BY date DESC", null);
        int index = 1;
        while (cursor.moveToNext()) {
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
        cursor.close();
        db.close();

        return news;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        pageNumber =  requireArguments().getInt(positionKey);
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        List<News> news = takeFromDb(tables.get(pageNumber - 1), getLayoutInflater().getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        RecyclerViewAdapter rvAdapter = new RecyclerViewAdapter(news);
        recyclerView.setAdapter(rvAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
