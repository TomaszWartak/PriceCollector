package com.dev4lazy.pricecollector.test_view.remote_sectors_screen;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.dev4lazy.pricecollector.remote_model.enities.RemoteSector;


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
