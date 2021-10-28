package com.example.covidvaccinetrack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Adaptor extends ArrayAdapter<Modal> {

    private Context context;
    private List<Modal> modalList;
    private List<Modal> modalListFilter;


    public Adaptor(@NonNull Context context, List<Modal> list) {
        super(context, R.layout.list_item, list);

        this.context = context;
        this.modalList = list;
        this.modalListFilter = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null, true);
        TextView countName = view.findViewById(R.id.country_name);
        ImageView countImg = view.findViewById(R.id.country_img);

        countName.setText(modalListFilter.get(position).getCountry());
        Glide.with(context).load(modalListFilter.get(position).getFlag()).into(countImg);

        return view;
    }

    @Override
    public int getCount() {
        return modalListFilter.size();
    }

    @Nullable
    @Override
    public Modal getItem(int position) {
        return modalListFilter.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter filer = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    filterResults.count = modalList.size();
                    filterResults.values = modalList;
                } else {
                    List<Modal> modals = new ArrayList<>();
                    String searchStr = constraint.toString().toLowerCase();

                    for (Modal item : modalList) {
                        if (item.getCountry().toLowerCase().contains(searchStr))
                            modals.add(item);
                    }
                    filterResults.count = modals.size();
                    filterResults.values = modals;
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                modalListFilter = (List<Modal>) results.values;
                AffecCountry.modalList = (List<Modal>) results.values;
                notifyDataSetChanged();
            }
        };
        return filer;
    }
}

