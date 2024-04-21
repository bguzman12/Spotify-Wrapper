package com.example.cs2340project2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PastWrapAdapter extends RecyclerView.Adapter<PastWrapAdapter.ClassViewHolder> {
    private final PastWrapRecyclerViewInterface pastWrapRecyclerViewInterface;

    private List<PastWrapItem> wrapItemList;

    public PastWrapAdapter(List<PastWrapItem> wrapItemList, PastWrapRecyclerViewInterface pastWrapRecyclerViewInterface) {
        this.wrapItemList = wrapItemList;
        this.pastWrapRecyclerViewInterface = pastWrapRecyclerViewInterface;
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pastwrap_item,
                parent, false);
        return new ClassViewHolder(itemView, pastWrapRecyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        PastWrapItem pastWrapItem = wrapItemList.get(position);
        holder.summary.setImageBitmap(pastWrapItem.getSummaryBitmap());
//        holder.wrapDate.setText(pastWrapItem.getDate());
//        holder.wrapTime.setText(pastWrapItem.getTime());
    }

    @Override
    public int getItemCount() {
        return wrapItemList.size();
    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder {
//        TextView wrapDate;
//        TextView wrapTime;
        ImageView summary;

        public ClassViewHolder(@NonNull View itemView, PastWrapRecyclerViewInterface classRecyclerViewInterface) {
            super(itemView);
            summary = itemView.findViewById(R.id.summary_img);
//            wrapDate = itemView.findViewById(R.id.wrapDate);
//            wrapTime = itemView.findViewById(R.id.wrapTime);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (classRecyclerViewInterface != null) {
//                        int position = getAdapterPosition();
//
//                        if (position != RecyclerView.NO_POSITION) {
//                            classRecyclerViewInterface.onItemClick(position);
//                        }
//                    }
//                }
//            });
        }
    }

}
