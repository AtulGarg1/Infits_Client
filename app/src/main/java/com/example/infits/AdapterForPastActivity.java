package com.example.infits;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import java.util.ArrayList;

public class AdapterForPastActivity extends RecyclerView.Adapter<AdapterForPastActivity.PastVH> {

    Context context;
    ArrayList<String> dates = new ArrayList<>();
    ArrayList<String> datas = new ArrayList<>();

    AdapterForPastActivity(Context context,ArrayList<String> dates,ArrayList<String> datas){
        this.context = context;
        this.datas = datas;
        this.dates = dates;
    }

    @NonNull
    @Override
    public PastVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater la = LayoutInflater.from(context);
        View view = la.inflate(R.layout.past_activity_layout,parent,false);

        return new PastVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PastVH holder, int position) {
                holder.steps.setText(datas.get(position));
                holder.date.setText(dates.get(position));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class PastVH extends RecyclerView.ViewHolder {
        TextView steps,date;
        public PastVH(@NonNull View itemView) {
            super(itemView);
            steps = itemView.findViewById(R.id.steps_count_pa);
            date = itemView.findViewById(R.id.date_pa);
        }
    }
}
