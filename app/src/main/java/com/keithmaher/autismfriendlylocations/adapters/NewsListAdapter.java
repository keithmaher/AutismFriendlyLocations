package com.keithmaher.autismfriendlylocations.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.keithmaher.autismfriendlylocations.R;
import com.keithmaher.autismfriendlylocations.models.Comment;
import com.keithmaher.autismfriendlylocations.models.News;

import java.util.List;

public class NewsListAdapter extends ArrayAdapter<News> {

    private Context context;
    public List<News> newsList;

    public NewsListAdapter(Context context, List<News> newsList)
    {
        super(context, R.layout.newsrow, newsList);

        this.context = context;
        this.newsList = newsList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        NewsItem item = new NewsItem(context, parent, newsList.get(position));
        return item.view;
    }

    @Override
    public int getCount()
    {

        return newsList.size();
    }

    @Override
    public News getItem(int position) {
        return newsList.get(position);
    }
}
