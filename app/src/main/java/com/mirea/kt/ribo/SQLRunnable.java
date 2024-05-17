package com.mirea.kt.ribo;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SQLRunnable implements Runnable {

    private List<String> source;
    private List<News> result = new ArrayList<>();
    private int id = 1;


    public SQLRunnable(List<String> source) {
        this.source = source;
    }

    public List<News> getResult() {
        return result;
    }

    @Override
    public void run() {
        Iterator<String> iterator = source.listIterator();
        int i = 0;
        while (iterator.hasNext()) {
            iterator.next();
            if (i <= 12) {
                iterator.remove();
            }
            i++;
        }

        int firstIndex, secondIndex;
        String title = "", link = "", date = "", description = "", category = "", img, element = "";
        for (String key : source) {
            firstIndex = key.indexOf(">");
            secondIndex = key.lastIndexOf("<");
            if (firstIndex < secondIndex || key.startsWith("enc", 1)) {
                if (firstIndex < secondIndex) element = key.substring(firstIndex + 1, secondIndex);
                if (key.startsWith("tit", 1)) title = element;
                if (key.startsWith("lin", 1)) link = element;
                if (key.startsWith("pub", 1)) date = element;
                if (key.startsWith("des", 1)) description = element;
                if (key.startsWith("cat", 1)) category = element;
                if (key.startsWith("enc", 1)){
                    img = key.substring(key.indexOf("url=") + 5, key.indexOf("width") - 2);
                    News news = new News(title, link, date, description, category, img, id);
                    id++;
                    result.add(news);

                }
            }

        }
    }


}
