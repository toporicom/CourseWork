package com.mirea.kt.ribo;

import androidx.annotation.NonNull;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;

public class News {
    String title, link, date, description, category, img;
    int id;

    public News(String title, String link, String date, String description, String category, String img, int id) {
        this.title = title;
        this.link = link;
        this.date = date;
        this.description = description;
        this.category = category;
        this.img = img;
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    @NonNull
    @Override
    public String toString() {
        return "News{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", date='" + date + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
