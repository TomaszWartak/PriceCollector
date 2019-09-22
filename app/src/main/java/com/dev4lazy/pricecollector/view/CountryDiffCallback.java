package com.dev4lazy.pricecollector.view;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.dev4lazy.pricecollector.model.entities.Country;
import com.dev4lazy.pricecollector.model.entities.Store;


public class CountryDiffCallback extends DiffUtil.ItemCallback<Country>{

    @Override
    public boolean areItemsTheSame(@NonNull Country oldItem, @NonNull Country newItem) {
        return (oldItem.getId() == newItem.getId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Country oldItem, @NonNull Country newItem) {
        return (oldItem.equals(newItem));
    }
}
