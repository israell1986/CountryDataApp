package com.example.israellorenz.countrydata;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.israellorenz.countryData.R;

import java.util.ArrayList;

/**
 * Created by israel.lorenz on 26/10/2017.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private ArrayList<String> originNames;
    private CountriesListAdapterListener listener;

    interface CountriesListAdapterListener{
        void onItemClicked(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView originName;
        int position;

        public ViewHolder(View itemView) {
            super(itemView);
            originName = (TextView) itemView.findViewById(R.id.originName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClicked(position);
            }
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }

    public ListAdapter(ArrayList<String> originNames, CountriesListAdapterListener listener){
        this.originNames = originNames;
        this.listener = listener;
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.basic_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListAdapter.ViewHolder holder, int position) {
        holder.originName.setText(originNames.get(position));
        holder.setPosition(position);
    }

    @Override
    public int getItemCount() {
        return originNames.size();
    }
}
