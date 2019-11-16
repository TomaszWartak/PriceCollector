package com.dev4lazy.pricecollector.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.entities.Article;

public class ArticleAdapter extends PagedListAdapter<Article, ArticleAdapter.ArticleViewHolder> {

    private ArticleDiffCalback articleDiffCalback = null;

    public ArticleAdapter(ArticleDiffCalback articleDiffCalback){
        super(articleDiffCalback);
        this.articleDiffCalback = articleDiffCalback;

    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = getItem(position);
        if (article == null) {
            holder.clear();
        } else {
            holder.bind(article);
        }
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.analysis_article_row, parent, false);
        return new ArticleViewHolder( view );
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewArticleName;

        public ArticleViewHolder(View view ) {
            super(view);
            textViewArticleName = view.findViewById(R.id.article_name);
        }

        protected void bind(Article article) {
            textViewArticleName.setText( String.valueOf( article.getId() ) );
        }

        protected void clear() {
            textViewArticleName.setText(null);
        }

    }
}

