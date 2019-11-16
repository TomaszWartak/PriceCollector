package com.dev4lazy.pricecollector.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;

public class AnalysisArticleJoinAdapter extends PagedListAdapter<AnalysisArticleJoin, AnalysisArticleJoinAdapter.AnalysisArticleViewHolder> {

    private AnalysisArticleJoinDiffCalback analysisArticleJoinDiffCalback = null;

    public AnalysisArticleJoinAdapter(AnalysisArticleJoinDiffCalback analysisArticleJoinDiffCalback){
        super(analysisArticleJoinDiffCalback);
        this.analysisArticleJoinDiffCalback = analysisArticleJoinDiffCalback;

    }

    @Override
    public void onBindViewHolder(@NonNull AnalysisArticleViewHolder holder, int position) {
        AnalysisArticleJoin analysisArticleJoin = getItem(position);
        if (analysisArticleJoin == null) {
            holder.clear();
        } else {
            holder.bind(analysisArticleJoin);
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
            textViewArticleName = view.findViewById(R.id.article_name);
        }

        protected void bind(AnalysisArticleJoin analysisArticleJoin) {
            textViewArticleName.setText(analysisArticleJoin.getArticleId());
        }

        protected void clear() {
            textViewArticleName.setText(null);
        }

    }
}

