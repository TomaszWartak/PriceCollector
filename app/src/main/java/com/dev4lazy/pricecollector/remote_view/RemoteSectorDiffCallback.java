package com.dev4lazy.pricecollector.remote_view;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.dev4lazy.pricecollector.remote_data.RemoteSector;


public class RemoteSectorDiffCallback extends DiffUtil.ItemCallback<RemoteSector>{

    @Override
    public boolean areItemsTheSame(@NonNull RemoteSector oldItem, @NonNull RemoteSector newItem) {
        return (oldItem.getId() == newItem.getId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull RemoteSector oldItem, @NonNull RemoteSector newItem) {
        return (oldItem.equals(newItem));
    }
}
