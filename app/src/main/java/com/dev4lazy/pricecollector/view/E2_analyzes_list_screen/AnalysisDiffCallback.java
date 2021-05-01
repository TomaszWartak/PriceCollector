package com.dev4lazy.pricecollector.view.E2_analyzes_list_screen;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.dev4lazy.pricecollector.model.entities.Analysis;

public class AnalysisDiffCallback extends DiffUtil.ItemCallback<Analysis>{

    @Override
    public boolean areItemsTheSame(@NonNull Analysis oldItem, @NonNull Analysis newItem) {
        return (oldItem.getId() == newItem.getId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Analysis oldItem, @NonNull Analysis newItem) {
        return (oldItem.equals(newItem));
    }
}
