package com.dev4lazy.pricecollector.test_view.remote_users_screen;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.dev4lazy.pricecollector.remote_model.enities.RemoteUser;


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
