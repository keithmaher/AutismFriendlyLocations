package com.keithmaher.autismfriendlylocations.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.keithmaher.autismfriendlylocations.R;
import com.keithmaher.autismfriendlylocations.Utils.TinyDB;
import com.keithmaher.autismfriendlylocations.fragments.BaseFragment;
import com.keithmaher.autismfriendlylocations.models.Comment;
import com.keithmaher.autismfriendlylocations.models.Location;
import com.keithmaher.autismfriendlylocations.models.News;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewsAdapterView extends RecyclerView.Adapter<NewsAdapterView.myViewHolder>{


    public List<News> locationList;
    FragmentActivity activity;


    public NewsAdapterView(ArrayList<News> locationList, FragmentActivity activity) {
        this.locationList = locationList;
        this.activity = activity;


    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.newsrow, viewGroup, false);
        return new NewsAdapterView.myViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder viewHolder, int i) {

        final News news = locationList.get(i);
        viewHolder.name.setText(news.getNewsName());
        viewHolder.date.setText("Date added: "+news.getNewsDate());
        viewHolder.location.setText("Commented on "+news.getNewsLocation());

        if (news.getNewsImg() != null) {
            Picasso.get().load(news.getNewsImg()).fit().into(viewHolder.image);
        }


        String dateStart = news.getNewsDate();
        Date cDate = new Date();
        String dateStop = new SimpleDateFormat("dd-MM-yyyy").format(cDate);

        //HH converts hour in 24 hours format (0-23), day calculation
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);

            if(diffDays<1){
                viewHolder.time.setText("Today");
            }else{
                viewHolder.time.setText(diffDays+" days ago");
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }


    class myViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView date;
        private TextView location;
        private CircleImageView image;
        private CardView card;
        private TextView time;

        public myViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.newsIcon);
            name = itemView.findViewById(R.id.newsUserName);
            date = itemView.findViewById(R.id.newsUserDate);
            card = itemView.findViewById(R.id.cardId);
            location = itemView.findViewById(R.id.newsLocation);
            time = itemView.findViewById(R.id.timeDays);
            card = itemView.findViewById(R.id.cardId);

        }
    }

}