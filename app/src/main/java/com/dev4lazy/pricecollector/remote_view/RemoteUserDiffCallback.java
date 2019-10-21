package com.dev4lazy.pricecollector.remote_view;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.dev4lazy.pricecollector.model.entities.Country;
import com.dev4lazy.pricecollector.remote_data.RemoteUser;


public class RemoteUserDiffCallback extends DiffUtil.ItemCallback<RemoteUser>{

    @Override
    public boolean areItemsTheSame(@NonNull RemoteUser oldItem, @NonNull RemoteUser newItem) {
        return (oldItem.getId() == newItem.getId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull RemoteUser oldItem, @NonNull RemoteUser newItem) {
        return (oldItem.equals(newItem));
    }
}
