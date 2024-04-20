package com.example.safecity;

import android.annotation.SuppressLint;
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
import java.util.List;

public class AdapterCity extends RecyclerView.Adapter <AdapterCity.HolderProblem> implements Filterable{
    private Context context;
    public ArrayList<String> photoarray,filterlist;
    private FilterProblem fild;
    String area,city,date,status,imagepath;
    private HolderProblem holder;
    private int position;

    public AdapterCity(Context context, ArrayList<String> photoarray) {
        this.context = context;
        this.photoarray = photoarray;
        this.filterlist= photoarray;
    }

    @NonNull
    @Override
    public AdapterCity.HolderProblem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.row_city,parent,false);

        return new HolderProblem(view);

    }



    @Override
    public void onBindViewHolder(@NonNull HolderProblem holder, @SuppressLint("RecyclerView") int position) {
        this.holder = holder;
        this.position = position;


        String userPhoto=photoarray.get(position);
        area=userPhoto;


        holder.holderarea.setText(area);



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                String selectedCity = photoarray.get(pos);

                Intent intent = new Intent(context, CategoryActivity2.class);
                intent.putExtra("SELECTED_CITY", selectedCity);
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
        return null;
    }

   /* @Override
    public Filter getFilter() {
        if(fild==null){
            fild= new FilterProblem(this,filterlist);

        }
        return fild;

    } */

    public class HolderProblem extends RecyclerView.ViewHolder {

        private TextView holderarea,holdercity,holderDate,holderstatus;
        private ImageView holderimg;
        public HolderProblem(@NonNull View itemView) {
            super(itemView);

            holderarea=itemView.findViewById(R.id.txtviewArea);

        }


    }



}
