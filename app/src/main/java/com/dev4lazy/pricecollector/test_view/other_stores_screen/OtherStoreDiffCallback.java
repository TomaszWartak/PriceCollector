package com.dev4lazy.pricecollector.test_view.other_stores_screen;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.dev4lazy.pricecollector.model.entities.Store;


public class OtherStoreDiffCallback extends DiffUtil.ItemCallback<Store>{

    @Override
    public boolean areItemsTheSame(@NonNull Store oldItem, @NonNull Store newItem) {
        return (oldItem.getId() == newItem.getId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Store oldItem, @NonNull Store newItem) {
        return (oldItem.equals(newItem));
    }
}
