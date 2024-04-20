package com.example.safecity;

import android.widget.Filter;

import java.util.ArrayList;

public class FilterAuth extends Filter {
    private AdapterAuthority adapter;
    private ArrayList<AuthorityClass> filterlist;

    public FilterAuth(AdapterAuthority adapter, ArrayList<AuthorityClass> filterlist) {
        this.adapter = adapter;
        this.filterlist = filterlist;
    }


    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if (constraint != null && constraint.length() > 0) {
            constraint = constraint.toString().toUpperCase(); // make case insensitive for query
            ArrayList<AuthorityClass> filtermodel = new ArrayList<>();

            for (int i = 0; i < filterlist.size(); i++) {
                // Add a null check for getCity(), getCategory(), getDate(), getLocality()
                if (filterlist.get(i).getName() != null && filterlist.get(i).getName().toUpperCase().contains(constraint) ||
                        filterlist.get(i).getCity() != null && filterlist.get(i).getCity().toUpperCase().contains(constraint)) {
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
        adapter.photoarray=(ArrayList<AuthorityClass>)filterResults.values;
        adapter.notifyDataSetChanged();

    }
}
