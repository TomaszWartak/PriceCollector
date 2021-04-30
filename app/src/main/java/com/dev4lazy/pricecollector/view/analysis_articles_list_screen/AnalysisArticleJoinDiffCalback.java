package com.dev4lazy.pricecollector.view.analysis_articles_list_screen;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;

public class AnalysisArticleJoinDiffCalback extends DiffUtil.ItemCallback<AnalysisArticleJoin>{

    @Override
    public boolean areItemsTheSame(@NonNull AnalysisArticleJoin oldItem, @NonNull AnalysisArticleJoin newItem) {
        return (oldItem.hashCode() == newItem.hashCode());
    }

    @Override
    public boolean areContentsTheSame(@NonNull AnalysisArticleJoin oldItem, @NonNull AnalysisArticleJoin newItem) {
        return (oldItem.equals( newItem ));
    }
}
