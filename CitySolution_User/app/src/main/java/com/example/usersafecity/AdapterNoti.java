package com.example.usersafecity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterNoti extends RecyclerView.Adapter <AdapterNoti.HolderNoti>{

    private Context context;
    public ArrayList<notification_class> notarray;
    String time,line;

    public AdapterNoti(Context context, ArrayList<notification_class> notarray) {
        this.context = context;
        this.notarray = notarray;
    }

    @NonNull
    @Override
    public HolderNoti onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_notification,parent,false);

        return new AdapterNoti.HolderNoti(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderNoti holder, int position) {

        notification_class userPhoto=notarray.get(position);
       time=userPhoto.getDate_time();
        line=userPhoto.getFeedback();

        holder.holderarea.setText(time);

        if ("Alert!!We found your problem to be a false claim".equals(line)) {

            holder.holdercity.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
        }
        holder.holdercity.setText(line);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos=holder.getAdapterPosition();
                notification_class hm=notarray.get(pos);
                String photoID1=hm.getPid();

                DatabaseReference countRef = FirebaseDatabase.getInstance().getReference("notification")
                        .child(FirebaseAuth.getInstance().getUid())
                        .child(photoID1)
                        .child("count");


                countRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            int currentCount = snapshot.getValue(Integer.class);
                            // Update the count to 1
                            countRef.setValue(1);

                            // Handle the rest of your onClick logic here

                            Intent intent = new Intent(context, DetailHistoryActivity.class);
                            intent.putExtra("PHOTOID", hm.getPid());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });





                /*
                Intent intent=new Intent(context,DetailHistoryActivity.class);
                intent.putExtra("PHOTOID",photoID1);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);*/

            }
        });

    }

    @Override
    public int getItemCount() {
        {
            return notarray.size();
        }
    }

    public class HolderNoti extends RecyclerView.ViewHolder {

        private TextView holderarea,holdercity;
        private ImageView holderimg;
        public HolderNoti(@NonNull View itemView) {
            super(itemView);

            holderarea=itemView.findViewById(R.id.textTime);
            holdercity=itemView.findViewById(R.id.textFeedback);

        }


    }

}

