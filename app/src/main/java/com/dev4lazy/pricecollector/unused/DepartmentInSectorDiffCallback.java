package com.dev4lazy.pricecollector.unused;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.dev4lazy.pricecollector.model.entities.DepartmentInSector;

public class DepartmentInSectorDiffCallback extends DiffUtil.ItemCallback<DepartmentInSector>{

    @Override
    public boolean areItemsTheSame(@NonNull DepartmentInSector oldItem, @NonNull DepartmentInSector newItem) {
        return (oldItem.getId() == newItem.getId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull DepartmentInSector oldItem, @NonNull DepartmentInSector newItem) {
        return (oldItem.equals(newItem));
    }
}
