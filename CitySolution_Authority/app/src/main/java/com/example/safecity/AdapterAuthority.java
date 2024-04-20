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
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class AdapterAuthority extends RecyclerView.Adapter <AdapterAuthority.HolderAuthority> implements Filterable{
    private Context context;
    public ArrayList<AuthorityClass> photoarray,filterlist;
    private FilterAuth fild;
    String area,city,date,status,imagepath,name,email,employeeid,phone,userid;

    public AdapterAuthority(Context context, ArrayList<AuthorityClass> photoarray) {
        this.context = context;
        this.photoarray = photoarray;
        this.filterlist= photoarray;
    }

    @NonNull
    @Override
    public AdapterAuthority.HolderAuthority onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.row_authority,parent,false);

        return new HolderAuthority(view);

    }

    @Override
    public void onBindViewHolder(@NonNull HolderAuthority holder, int position) {


        AuthorityClass userPhoto=photoarray.get(position);
        name=userPhoto.getName();
        city=userPhoto.getCity();
        email=userPhoto.getEmail();
        employeeid=userPhoto.getEmployeeid();
        phone=userPhoto.getPhone();
        userid=userPhoto.getUserid();

        imagepath=userPhoto.getImagepath();


        holder.holdercity.setText(city);
        holder.holderName.setText(name);
        try
        {
             //Picasso.with(context).load(imagepath).placeholder(R.drawable.city).into((Target) holder.holderimg);

            Picasso.with(context)
                    .load(imagepath)           // Load the image from the URL or file path
                    .placeholder(R.drawable.logo) // Placeholder image while loading
                    .error(R.drawable.logo) // Error image to display on failure
                    .into(holder.holderimg);


        }catch (Exception e){

            holder.holderimg.setImageResource(R.drawable.logo);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos=holder.getAdapterPosition();
                AuthorityClass hm=photoarray.get(pos);
                String photoID1=hm.getUserid();


                Intent intent=new Intent(context,AuthDetails.class);
                intent.putExtra("AUTHID",photoID1);


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
            fild= new FilterAuth(this,filterlist);

        }
        return fild;

    }

    public class HolderAuthority extends RecyclerView.ViewHolder {

        private TextView holderarea,holdercity,holderDate,holderstatus,holderName;
        private ImageView holderimg;
        public HolderAuthority(@NonNull View itemView) {
            super(itemView);


            holdercity=itemView.findViewById(R.id.textCity);
            holderName=itemView.findViewById(R.id.textName);
            holderimg=itemView.findViewById(R.id.imageView);

        }


    }



}
