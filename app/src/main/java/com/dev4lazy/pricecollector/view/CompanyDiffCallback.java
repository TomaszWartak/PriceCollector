package com.dev4lazy.pricecollector.view;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.dev4lazy.pricecollector.model.entities.Company;
import com.dev4lazy.pricecollector.model.entities.Store;


public class CompanyDiffCallback extends DiffUtil.ItemCallback<Company>{

    @Override
    public boolean areItemsTheSame(@NonNull Company oldItem, @NonNull Company newItem) {
        return (oldItem.getId() == newItem.getId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Company oldItem, @NonNull Company newItem) {
        return (oldItem.equals(newItem));
    }
}
