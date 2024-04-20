package com.example.safecity;

import android.widget.Filter;

import java.util.ArrayList;

public class FilterProblem extends Filter {
    private AdapterProblem adapter;
    private ArrayList<UserPhoto> filterlist;

    public FilterProblem(AdapterProblem adapter, ArrayList<UserPhoto> filterlist) {
        this.adapter = adapter;
        this.filterlist = filterlist;
    }


    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if (constraint != null && constraint.length() > 0) {
            constraint = constraint.toString().toUpperCase(); // make case insensitive for query
            ArrayList<UserPhoto> filtermodel = new ArrayList<>();

            for (int i = 0; i < filterlist.size(); i++) {
                // Add a null check for getCity(), getCategory(), getDate(), getLocality()
                if (filterlist.get(i).getCategory() != null && filterlist.get(i).getCategory().toUpperCase().contains(constraint) ||
                        filterlist.get(i).getLocality() != null && filterlist.get(i).getLocality().toUpperCase().contains(constraint) ||
                        filterlist.get(i).getCity() != null && filterlist.get(i).getCity().toUpperCase().contains(constraint) ||
                        filterlist.get(i).getDate() != null && filterlist.get(i).getDate().toUpperCase().contains(constraint)) {
                    filtermodel.add(filterlist.get(i));
                }
            }
            results.count = filtermodel.size();
            results.values = filtermodel;
        } else {
            results.count = filterlist.size();
            results.values = filterlist;
        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        adapter.photoarray=(ArrayList<UserPhoto>)filterResults.values;
        adapter.notifyDataSetChanged();

    }
}
