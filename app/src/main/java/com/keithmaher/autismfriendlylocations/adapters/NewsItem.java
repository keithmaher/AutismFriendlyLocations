package com.keithmaher.autismfriendlylocations.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.keithmaher.autismfriendlylocations.R;
import com.keithmaher.autismfriendlylocations.models.News;
import com.squareup.picasso.Picasso;

public class NewsItem {

    View view;
    private ImageView image;

    public NewsItem(Context context, ViewGroup parent, News news)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.newsrow, parent, false);
        view.setTag(news.newsId);

        updateControls(news);
    }

    private void updateControls(News news) {

        String title = news.newsName+" added a comment to \n\n"+news.newsMain;
        ((TextView) view.findViewById(R.id.newsUserName)).setText(title);
        String newDate = "Date added: "+news.newsDate;
        ((TextView) view.findViewById(R.id.newsUserDate)).setText(newDate);
        image = view.findViewById(R.id.newsIcon);
        Picasso.get().load(news.newsImg).into(image);
    }
}
