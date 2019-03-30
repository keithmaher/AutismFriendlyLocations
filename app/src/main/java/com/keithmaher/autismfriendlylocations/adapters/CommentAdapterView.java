package com.keithmaher.autismfriendlylocations.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.keithmaher.autismfriendlylocations.R;
import com.keithmaher.autismfriendlylocations.models.Comment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapterView extends RecyclerView.Adapter<CommentAdapterView.myViewHolder>{


    public List<Comment> commentList;
    FragmentActivity activity;


    public CommentAdapterView(ArrayList<Comment> commentList, FragmentActivity activity) {
        this.commentList = commentList;
        this.activity = activity;


    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.commentrow, viewGroup, false);
        return new CommentAdapterView.myViewHolder(v);

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull myViewHolder viewHolder, int i) {

        final Comment comment = commentList.get(i);
        viewHolder.name.setText(comment.getCommentName());
        viewHolder.date.setText(comment.getCommentDate());

        if (comment.getCommentUserImageURL() != null) {
            Picasso.get().load(comment.getCommentUserImageURL()).fit().into(viewHolder.image);
        }
        viewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(activity)
                        .setTitle(comment.getCommentName())
                        .setMessage(comment.getCommentMain()
                                + "\n\n"
                                + "Date added: "+ comment.getCommentDate())
                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }


    class myViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView date;
        private CircleImageView image;
        private CardView card;

        public myViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.cardId);
            name = itemView.findViewById(R.id.newsUserName);
            date = itemView.findViewById(R.id.newsUserDate);
            image = itemView.findViewById(R.id.commentProfileImage);


        }
    }

}