package com.keithmaher.autismfriendlylocations.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.keithmaher.autismfriendlylocations.R;
import com.keithmaher.autismfriendlylocations.models.Comment;

import java.util.List;

public class CommentListAdapter extends ArrayAdapter<Comment> {

    private Context context;
    public List<Comment> commentList;

    public CommentListAdapter(Context context, List<Comment> commentList)
    {
        super(context, R.layout.commentrow, commentList);

        this.context = context;
        this.commentList = commentList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        CommentItem item = new CommentItem(context, parent, commentList.get(position));
        return item.view;
    }

    @Override
    public int getCount()
    {

        return commentList.size();
    }

    @Override
    public Comment getItem(int position) {
        return commentList.get(position);
    }
}
