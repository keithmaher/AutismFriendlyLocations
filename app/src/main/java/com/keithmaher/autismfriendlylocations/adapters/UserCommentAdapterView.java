package com.keithmaher.autismfriendlylocations.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.keithmaher.autismfriendlylocations.R;
import com.keithmaher.autismfriendlylocations.fragments.BaseFragment;
import com.keithmaher.autismfriendlylocations.models.Comment;
import com.keithmaher.autismfriendlylocations.models.Location;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserCommentAdapterView extends RecyclerView.Adapter<UserCommentAdapterView.myViewHolder>{


    public List<Comment> commentList;
    FragmentActivity activity;
    DatabaseReference mDatabase;


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
    public void onBindViewHolder(@NonNull final myViewHolder viewHolder, final int i) {

        final Comment commentTest = commentList.get(i);
        viewHolder.name.setText(commentTest.getCommentLocationName());
        viewHolder.date.setText(commentTest.getCommentDate());
        viewHolder.comment.setText(commentTest.getCommentMain());
        if (commentTest.getCommentUserImageURL() != null) {
            Picasso.get().load(commentTest.getCommentUserImageURL()).fit().into(viewHolder.image);
        }

        viewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                v = activity.getLayoutInflater().inflate(R.layout.usercommentupdatebox, null);
                alertDialog.setView(v);
                final Button button1 = v.findViewById(R.id.button);
                Button button2 = v.findViewById(R.id.button2);
                alertDialog.setTitle("What do you want to do")
                        .setMessage("Please Select the following"
                                + "\n\n"
                                + "Edit & Delete");
                alertDialog.setView(v);
                final AlertDialog dialog1 = alertDialog.create();
                dialog1.show();

                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                        v = activity.getLayoutInflater().inflate(R.layout.commentalertbox, null);
                        alertDialog.setView(v);
                        final EditText editCommentMain = v.findViewById(R.id.editTextComment);
                        TextView editCommentName = v.findViewById(R.id.editTextName);
                        editCommentName.setText(commentTest.getCommentName());
                        editCommentMain.setText(commentTest.getCommentMain());
                        alertDialog.setTitle("Editing Comment")
                                .setMessage("Sorry that you made a mistake"
                                        + "\n\n"
                                        + "Don't let this happen again")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(final DialogInterface dialog, int which) {

                                        final String updatedCommentMain = editCommentMain.getText().toString();

                                        if (updatedCommentMain.isEmpty()) {
                                            Toast.makeText(activity, "This can not be blank", Toast.LENGTH_SHORT).show();
                                        } else {
                                            mDatabase = FirebaseDatabase.getInstance().getReference("Comments");
                                            ChildEventListener childEventListener = new ChildEventListener() {
                                                @Override
                                                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                                                    final Iterable<DataSnapshot> contactChildren = dataSnapshot.getChildren();

                                                    for (DataSnapshot contact : contactChildren) {
                                                        Comment comment = contact.getValue(Comment.class);

                                                        if (comment.getCommentMain().equals(commentTest.getCommentMain())) {
                                                            mDatabase.child(dataSnapshot.getKey()).child(contact.getKey()).child("commentMain").setValue(updatedCommentMain);
                                                            dialog.dismiss();
                                                            BaseFragment.userLocationFragment(activity);
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                                }

                                                @Override
                                                public void onChildRemoved(DataSnapshot dataSnapshot) {
                                                }

                                                @Override
                                                public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                }
                                            };

                                            mDatabase.addChildEventListener(childEventListener);
                                        }
                                    }
                                })
                                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                        alertDialog.setView(v);
                        AlertDialog dialog = alertDialog.create();
                        dialog.show();
                    }
                });

                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();

                        new AlertDialog.Builder(activity)
                                .setTitle("Are You Sure")
                                .setMessage("This can not be undone")
                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(final DialogInterface dialog, int which) {

                                        mDatabase = FirebaseDatabase.getInstance().getReference("Comments");
                                        ChildEventListener childEventListener = new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                                                final Iterable<DataSnapshot> contactChildren = dataSnapshot.getChildren();

                                                for (DataSnapshot contact : contactChildren) {
                                                    Comment comment = contact.getValue(Comment.class);

                                                    if (comment.getCommentMain().equals(commentTest.getCommentMain())) {
                                                        mDatabase.child(dataSnapshot.getKey()).child(contact.getKey()).removeValue();
                                                        dialog.dismiss();
                                                        BaseFragment.userLocationFragment(activity);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                            }

                                            @Override
                                            public void onChildRemoved(DataSnapshot dataSnapshot) {
                                            }

                                            @Override
                                            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                            }
                                        };

                                        mDatabase.addChildEventListener(childEventListener);
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }
                });
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