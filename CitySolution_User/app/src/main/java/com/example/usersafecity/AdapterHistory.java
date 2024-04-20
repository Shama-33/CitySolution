package com.example.usersafecity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class AdapterHistory extends RecyclerView.Adapter <AdapterHistory.HolderHistory> implements Filterable {


    private Context context;
    public ArrayList<UserPhoto> photoarray,filterlist;
    private FilterHistory fild;
    String area,city,date,status,imagepath;

    public AdapterHistory(Context context, ArrayList<UserPhoto> photoarray) {
        this.context = context;
        this.photoarray = photoarray;
        this.filterlist= photoarray;
    }

    @NonNull
    @Override
    public AdapterHistory.HolderHistory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.row_history,parent,false);

        return new HolderHistory(view);

    }

    @Override
    public void onBindViewHolder(@NonNull HolderHistory holder, int position) {


        UserPhoto userPhoto=photoarray.get(position);
        area=userPhoto.getLocality();
        city=userPhoto.getCity();
        date=userPhoto.getDate();
        status=userPhoto.getStatus();
        imagepath=userPhoto.getImagepath();

        holder.holderarea.setText(area);
        holder.holdercity.setText(city);
        holder.holderDate.setText(date);
        holder.holderstatus.setText(status);
        if(status.equalsIgnoreCase("pending"))
        {
            holder.holderstatus.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
        }
        if(status.equalsIgnoreCase("acknowledged"))
        {
            holder.holderstatus.setText("processing");
            holder.holderstatus.setTextColor(ContextCompat.getColor(context, R.color.colorAddCart));
        }
        if(status.equalsIgnoreCase("solved"))
        {
            holder.holderstatus.setTextColor(ContextCompat.getColor(context, R.color.teal_700));
        }
        try
        {
           // Picasso.with(context).load(imagepath).placeholder(R.drawable.city).into((Target) holder.holderimg);

            Picasso.with(context)
                    .load(imagepath)
                    .placeholder(R.drawable.city)
                    .error(R.drawable.city)
                    .into(holder.holderimg);


        }catch (Exception e){

            holder.holderimg.setImageResource(R.drawable.city);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos=holder.getAdapterPosition();
                UserPhoto hm=photoarray.get(pos);
                String photoID1=hm.getPhotoid();


                Intent intent=new Intent(context,DetailHistoryActivity.class);
                intent.putExtra("PHOTOID",photoID1);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return photoarray.size();
    }

    @Override
    public Filter getFilter() {
        if(fild==null){
            fild= new FilterHistory(this,filterlist);

        }
        return fild;

    }

    public class HolderHistory extends RecyclerView.ViewHolder {

        private TextView holderarea,holdercity,holderDate,holderstatus;
        private ImageView holderimg;
        public HolderHistory(@NonNull View itemView) {
            super(itemView);

            holderarea=itemView.findViewById(R.id.txtviewArea);
            holdercity=itemView.findViewById(R.id.txtviewCity);
            holderDate=itemView.findViewById(R.id.txtviewDate);
            holderstatus=itemView.findViewById(R.id.txtviewStatus);
            holderimg=itemView.findViewById(R.id.imageviewMLImage);
        }


    }
}
