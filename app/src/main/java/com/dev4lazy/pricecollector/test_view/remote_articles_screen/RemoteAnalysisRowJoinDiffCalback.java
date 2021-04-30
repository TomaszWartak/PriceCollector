package com.dev4lazy.pricecollector.test_view.remote_articles_screen;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.dev4lazy.pricecollector.remote_data.RemoteAnalysisRowJoin;

public class RemoteAnalysisRowJoinDiffCalback extends DiffUtil.ItemCallback<RemoteAnalysisRowJoin>{

    @Override
    public boolean areItemsTheSame(@NonNull RemoteAnalysisRowJoin oldItem, @NonNull RemoteAnalysisRowJoin newItem) {
        return (oldItem.getRemoteAnalysisRowId() == newItem.getRemoteAnalysisRowId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull RemoteAnalysisRowJoin oldItem, @NonNull RemoteAnalysisRowJoin newItem) {
        return (oldItem.equals(newItem));
    }
}
