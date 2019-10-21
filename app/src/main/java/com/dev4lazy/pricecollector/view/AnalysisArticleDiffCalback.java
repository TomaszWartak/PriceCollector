package com.dev4lazy.pricecollector.view;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.dev4lazy.pricecollector.model.entities.AnalysisArticle;

public class AnalysisArticleDiffCalback extends DiffUtil.ItemCallback<AnalysisArticle>{

    @Override
    public boolean areItemsTheSame(@NonNull AnalysisArticle oldItem, @NonNull AnalysisArticle newItem) {
        return (oldItem.getId() == newItem.getId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull AnalysisArticle oldItem, @NonNull AnalysisArticle newItem) {
        return (oldItem.equals(newItem));
    }
}
