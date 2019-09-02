package com.dev4lazy.pricecollector.csv2pojo_recycler;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.dev4lazy.pricecollector.csv2pojo.AnalysisRow;

public class AnalysisRowDiffCalback extends DiffUtil.ItemCallback<AnalysisRow>{

    @Override
    public boolean areItemsTheSame(@NonNull AnalysisRow oldItem, @NonNull AnalysisRow newItem) {
        return (oldItem.getId() == newItem.getId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull AnalysisRow oldItem, @NonNull AnalysisRow newItem) {
        return (oldItem.equals(newItem));
    }
}
