package com.mirea.kt.ribo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class VPAdapter extends FragmentPagerAdapter {
    private List<Fragment> pages = new ArrayList<>();
    private List<String> tables = new ArrayList<String>(){{
        add(0, "Новости Москвы");
        add(1, "Политика");
        add(2, "Общество");
        add(3, "Проишествия");
    }};

    public VPAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tables.get(position);
    }

    public void setPages(List<Fragment> pages){
        this.pages = pages;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return pages.get(position);
    }
}
