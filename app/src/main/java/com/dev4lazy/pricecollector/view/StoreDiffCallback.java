package com.dev4lazy.pricecollector.view;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.dev4lazy.pricecollector.model.entities.Store;


public class StoreDiffCallback extends DiffUtil.ItemCallback<Store>{

    @Override
    public boolean areItemsTheSame(@NonNull Store oldItem, @NonNull Store newItem) {
        return (oldItem.getId() == newItem.getId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Store oldItem, @NonNull Store newItem) {
        return (oldItem.equals(newItem));
    }
}
