package com.example.safecity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterProblem extends RecyclerView.Adapter <AdapterProblem.HolderProblem> implements Filterable{
    private Context context;
    public ArrayList<UserPhoto> photoarray,filterlist;
    private FilterProblem fild;
    String area,city,date,status,imagepath;

    public AdapterProblem(Context context, ArrayList<UserPhoto> photoarray) {
        this.context = context;
        this.photoarray = photoarray;
        this.filterlist= photoarray;
    }

    @NonNull
    @Override
    public AdapterProblem.HolderProblem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.row_problem1,parent,false);

        return new HolderProblem(view);

    }

    @Override
    public void onBindViewHolder(@NonNull HolderProblem holder, int position) {


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
        try
        {
            // Picasso.with(context).load(imagepath).placeholder(R.drawable.city).into((Target) holder.holderimg);

            Picasso.with(context)
                    .load(imagepath)           // Load the image from the URL or file path
                    .placeholder(R.drawable.city) // Placeholder image while loading
                    .error(R.drawable.city) // Error image to display on failure
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
                String category=hm.getCategory();


                Intent intent=new Intent(context,ProblemDetailsActivity.class);
                intent.putExtra("PHOTOID",photoID1);
                intent.putExtra("CATEGORY",category);

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
            fild= new FilterProblem(this,filterlist);

        }
        return fild;

    }

    public class HolderProblem extends RecyclerView.ViewHolder {

        private TextView holderarea,holdercity,holderDate,holderstatus;
        private ImageView holderimg;
        public HolderProblem(@NonNull View itemView) {
            super(itemView);

            holderarea=itemView.findViewById(R.id.txtviewArea);
            holdercity=itemView.findViewById(R.id.txtviewCity);
            holderDate=itemView.findViewById(R.id.txtviewDate);
            holderstatus=itemView.findViewById(R.id.txtviewStatus);
            holderimg=itemView.findViewById(R.id.imageviewMLImage);
        }


    }



}
