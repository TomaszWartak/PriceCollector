package com.dev4lazy.pricecollector.test_view.remote_articles_screen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteAnalysisRowJoin;

public class RemoteAnalysisRowJoinAdapter extends PagedListAdapter<RemoteAnalysisRowJoin, RemoteAnalysisRowJoinAdapter.RemoteAnalysisRowJoinViewHolder> {

    private RemoteAnalysisRowJoinDiffCalback remoteAnalysisRowJoinDiffCalback = null;

    public RemoteAnalysisRowJoinAdapter(RemoteAnalysisRowJoinDiffCalback remoteAnalysisRowJoinDiffCalback){
        super(remoteAnalysisRowJoinDiffCalback);
        this.remoteAnalysisRowJoinDiffCalback = remoteAnalysisRowJoinDiffCalback;
    }

    @Override
    public void onBindViewHolder(@NonNull RemoteAnalysisRowJoinViewHolder holder, int position) {
        RemoteAnalysisRowJoin remoteAnalysisRowJoin = getItem(position);
        if (remoteAnalysisRowJoin == null) {
            holder.clear();
        } else {
            holder.bind(remoteAnalysisRowJoin);
        }
    }

    @NonNull
    @Override
    public RemoteAnalysisRowJoinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.remote_analysis_row_join,parent, false);
        return new RemoteAnalysisRowJoinViewHolder( view );
    }

    class RemoteAnalysisRowJoinViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewArticleName;
        private TextView textViewArticleCode;
        private TextView textViewArticleEan;

        public RemoteAnalysisRowJoinViewHolder(View view ) {
            super(view);
            textViewArticleName = view.findViewById(R.id.remote_article_name);
            textViewArticleCode = view.findViewById(R.id.remote_article_code);
            textViewArticleEan = view.findViewById(R.id.remote_article_ean);
        }

        protected void bind(RemoteAnalysisRowJoin remoteAnalysisRowJoin) {
            textViewArticleName.setText(remoteAnalysisRowJoin.getRemoteAnalysisRowArticleName());
            textViewArticleCode.setText(String.valueOf(remoteAnalysisRowJoin.getRemoteAnalysisRowArticleCode()));
            textViewArticleEan.setText(remoteAnalysisRowJoin.getRemoteEanCodeValue());
        }

        protected void clear() {
            textViewArticleName.setText(null);
            textViewArticleCode.setText(null);
            textViewArticleEan.setText(null);
        }

    }
}

