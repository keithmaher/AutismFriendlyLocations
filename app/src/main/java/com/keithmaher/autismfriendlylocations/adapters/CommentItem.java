package com.keithmaher.autismfriendlylocations.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.keithmaher.autismfriendlylocations.R;
import com.keithmaher.autismfriendlylocations.models.Comment;

public class CommentItem {

    View view;

    public CommentItem(Context context, ViewGroup parent, Comment comment)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.commentrow, parent, false);
        view.setTag(comment.commentId);

        updateControls(comment);
    }

    private void updateControls(Comment comment) {
        ((TextView) view.findViewById(R.id.newsUserName)).setText(comment.commentName);
        ((TextView) view.findViewById(R.id.newsUserDate)).setText(comment.commentDate);
    }
}
