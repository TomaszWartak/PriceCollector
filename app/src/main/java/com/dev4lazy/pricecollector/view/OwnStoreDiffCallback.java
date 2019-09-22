package com.dev4lazy.pricecollector.view;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.dev4lazy.pricecollector.model.entities.OwnStore;
import com.dev4lazy.pricecollector.model.entities.Store;


public class OwnStoreDiffCallback extends DiffUtil.ItemCallback<OwnStore>{

    @Override
    public boolean areItemsTheSame(@NonNull OwnStore oldItem, @NonNull OwnStore newItem) {
        return (oldItem.getId() == newItem.getId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull OwnStore oldItem, @NonNull OwnStore newItem) {
        return (oldItem.equals(newItem));
    }
}
