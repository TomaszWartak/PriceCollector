package com.dev4lazy.pricecollector.test_view.articles_screen;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.dev4lazy.pricecollector.model.entities.Article;

public class ArticleDiffCallback extends DiffUtil.ItemCallback<Article>{

    @Override
    public boolean areItemsTheSame(@NonNull Article oldItem, @NonNull Article newItem) {
        return (oldItem.getId() == newItem.getId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull Article oldItem, @NonNull Article newItem) {
        return (oldItem.equals(newItem));
    }
}
