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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.keithmaher.autismfriendlylocations.R;
import com.keithmaher.autismfriendlylocations.models.Comment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserCommentAdapterView extends RecyclerView.Adapter<UserCommentAdapterView.myViewHolder>{


    public List<Comment> commentList;
    FragmentActivity activity;


    public UserCommentAdapterView(ArrayList<Comment> commentList, FragmentActivity activity) {
        this.commentList = commentList;
        this.activity = activity;


    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.usercommentrow, viewGroup, false);
        return new UserCommentAdapterView.myViewHolder(v);

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull final myViewHolder viewHolder, int i) {

        final Comment comment = commentList.get(i);
        viewHolder.name.setText(comment.getCommentName());
        viewHolder.date.setText(comment.getCommentDate());
        viewHolder.comment.setText(comment.getCommentMain());
        viewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                v = activity.getLayoutInflater().inflate(R.layout.usercommentupdatebox, null);
                alertDialog.setView(v);
                final Button button1 = v.findViewById(R.id.button);
                Button button2 = v.findViewById(R.id.button2);
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(activity, "Testing", Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialog.setTitle("Registration")
                        .setMessage("Please complete the following"
                                + "\n\n"
                                + "Email & Password")
                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.setView(v);
                AlertDialog dialog = alertDialog.create();
                dialog.show();
            }
        });


        if (comment.getCommentUserImageURL() != null) {
            Picasso.get().load(comment.getCommentUserImageURL()).fit().into(viewHolder.image);
        }


    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }


    class myViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView date;
        private TextView comment;
        private CircleImageView image;
        private CardView card;

        public myViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.cardId);
            name = itemView.findViewById(R.id.newsUserName);
            date = itemView.findViewById(R.id.newsUserDate);
            comment = itemView.findViewById(R.id.comment);
            image = itemView.findViewById(R.id.commentProfileImage);


        }
    }

}