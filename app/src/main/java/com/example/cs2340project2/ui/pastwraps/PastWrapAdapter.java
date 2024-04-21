package com.example.cs2340project2.ui.pastwraps;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs2340project2.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PastWrapAdapter extends RecyclerView.Adapter<PastWrapAdapter.ClassViewHolder> {
    private final PastWrapRecyclerViewInterface pastWrapRecyclerViewInterface;

    private final List<PastWrapItem> wrapItemList;

    public PastWrapAdapter(List<PastWrapItem> wrapItemList, PastWrapRecyclerViewInterface pastWrapRecyclerViewInterface) {
        this.pastWrapRecyclerViewInterface = pastWrapRecyclerViewInterface;
        this.wrapItemList = wrapItemList;
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pastwrap,
                parent, false);
        return new ClassViewHolder(itemView, pastWrapRecyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        PastWrapItem pastWrapItem = wrapItemList.get(position);
        Picasso.get().load(pastWrapItem.getImageURL()).into(holder.summary);
        holder.wrapDate.setText(pastWrapItem.getDate());

        switch (pastWrapItem.getTime()) {
            case "short_term":
                holder.wrapTime.setText("Past Month");
                break;
            case "medium_term":
                holder.wrapTime.setText("Past 6 Months");
                break;
            default:
                holder.wrapTime.setText("Past Year");
        }

    }

    @Override
    public int getItemCount() {
        return wrapItemList.size();
    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder {
        TextView wrapDate;
        TextView wrapTime;
        ImageView summary;

        public ClassViewHolder(@NonNull View itemView, PastWrapRecyclerViewInterface pastWrapRecyclerViewInterface) {
            super(itemView);
            summary = itemView.findViewById(R.id.summary_img);
            wrapDate = itemView.findViewById(R.id.wrapDate);
            wrapTime = itemView.findViewById(R.id.wrapTime);

            itemView.setOnClickListener(view -> {
                if (pastWrapRecyclerViewInterface != null) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        pastWrapRecyclerViewInterface.onItemClick(position);
                    }
                }
            });
        }
    }

}
