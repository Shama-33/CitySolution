package com.example.usersafecity;

import android.widget.Filter;

import java.util.ArrayList;

public class FilterHistory extends Filter {
    private AdapterHistory adapter;
    private ArrayList<UserPhoto> filterlist;

    public FilterHistory(AdapterHistory adapter, ArrayList<UserPhoto> filterlist) {
        this.adapter = adapter;
        this.filterlist = filterlist;
    }


    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results= new FilterResults();
        if(constraint!=null && constraint.length()>0) {
            constraint = constraint.toString().toUpperCase();//make case insensitive for query
            ArrayList<UserPhoto>filtermodel= new ArrayList<>();

            /* for(int i=0;i<filterlist.size();i++)
            {
                if (filterlist.get(i).getCategory().toUpperCase().contains(constraint) ||
                        filterlist.get(i).getLocality().toUpperCase().contains(constraint)||
                        filterlist.get(i).getCity().toUpperCase().contains(constraint) ||
                filterlist.get(i).getDate().toUpperCase().contains(constraint)||
                filterlist.get(i).getStatus().toUpperCase().contains(constraint))
                {
                    filtermodel.add(filterlist.get(i));
                }

            } */
            for (int i = 0; i < filterlist.size(); i++) {
                UserPhoto currentPhoto = filterlist.get(i);

                // Check for null values before performing operations
                String category = currentPhoto.getCategory();
                String locality = currentPhoto.getLocality();
                String city = currentPhoto.getCity();
                String date = currentPhoto.getDate();
                String status = currentPhoto.getStatus();

                if ((category != null && category.toUpperCase().contains(constraint)) ||
                        (locality != null && locality.toUpperCase().contains(constraint)) ||
                        (city != null && city.toUpperCase().contains(constraint)) ||
                        (date != null && date.toUpperCase().contains(constraint)) ||
                        (status != null && status.toUpperCase().contains(constraint))) {
                    filtermodel.add(currentPhoto);
                }
            }
            results.count= filtermodel.size();
            results.values=filtermodel;
        }

        else
        {
            results.count= filterlist.size();
            results.values=filterlist;

        }

        return results;


    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        adapter.photoarray=(ArrayList<UserPhoto>)filterResults.values;
        adapter.notifyDataSetChanged();

    }
}
