package com.dev4lazy.pricecollector.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.entities.AnalysisArticle;

public class AnalysisArticleAdapter extends PagedListAdapter<AnalysisArticle, AnalysisArticleAdapter.AnalysisArticleViewHolder> {

    private AnalysisArticleDiffCalback analysisArticleDiffCalback = null;

    public AnalysisArticleAdapter(AnalysisArticleDiffCalback analysisArticleDiffCalback){
        super(analysisArticleDiffCalback);
        this.analysisArticleDiffCalback = analysisArticleDiffCalback;

    }

    @Override
    public void onBindViewHolder(@NonNull AnalysisArticleViewHolder holder, int position) {
        AnalysisArticle analysisArticle = getItem(position);
        if (analysisArticle == null) {
            holder.clear();
        } else {
            holder.bind(analysisArticle);
        }
    }

    @NonNull
    @Override
    public AnalysisArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.analysis_article_row, parent, false);
        return new AnalysisArticleViewHolder( view );
    }

    class AnalysisArticleViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewArticleName;

        public AnalysisArticleViewHolder(View view ) {
            super(view);
            textViewArticleName = view.findViewById(R.id.remote_article_name);
        }

        protected void bind(AnalysisArticle analysisArticle) {
            textViewArticleName.setText(analysisArticle.getArticleId());
        }

        protected void clear() {
            textViewArticleName.setText(null);
        }

    }
}

