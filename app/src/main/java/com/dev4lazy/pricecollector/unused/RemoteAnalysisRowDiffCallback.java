package com.dev4lazy.pricecollector.unused;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.dev4lazy.pricecollector.remote_model.enities.RemoteAnalysisRow;

public class RemoteAnalysisRowDiffCallback extends DiffUtil.ItemCallback<RemoteAnalysisRow>{

    @Override
    public boolean areItemsTheSame(@NonNull RemoteAnalysisRow oldItem, @NonNull RemoteAnalysisRow newItem) {
        return (oldItem.getId() == newItem.getId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull RemoteAnalysisRow oldItem, @NonNull RemoteAnalysisRow newItem) {
        return (oldItem.equals(newItem));
    }
}
