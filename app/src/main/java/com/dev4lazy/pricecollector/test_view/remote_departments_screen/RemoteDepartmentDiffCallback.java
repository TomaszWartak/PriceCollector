package com.dev4lazy.pricecollector.test_view.remote_departments_screen;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.dev4lazy.pricecollector.remote_model.enities.RemoteDepartment;

public class RemoteDepartmentDiffCallback extends DiffUtil.ItemCallback<RemoteDepartment>{

    @Override
    public boolean areItemsTheSame(@NonNull RemoteDepartment oldItem, @NonNull RemoteDepartment newItem) {
        return (oldItem.getId() == newItem.getId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull RemoteDepartment oldItem, @NonNull RemoteDepartment newItem) {
        return (oldItem.equals(newItem));
    }
}