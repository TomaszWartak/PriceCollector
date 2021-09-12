package com.dev4lazy.pricecollector.remote_view_viewmodel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteAnalysisRow;

public class RemoteAnalysisRowAdapter extends PagedListAdapter<RemoteAnalysisRow, RemoteAnalysisRowAdapter.RemoteAnalysisRowViewHolder> {

    private RemoteAnalysisRowDiffCallback remoteAnalysisRowDiffCallback = null;

    public RemoteAnalysisRowAdapter(RemoteAnalysisRowDiffCallback remoteAnalysisRowDiffCallback){
        super(remoteAnalysisRowDiffCallback);
        this.remoteAnalysisRowDiffCallback = remoteAnalysisRowDiffCallback;

    }

    @Override
    public void onBindViewHolder(@NonNull RemoteAnalysisRowViewHolder holder, int position) {
        RemoteAnalysisRow remoteAnalysisRow = getItem(position);
        if (remoteAnalysisRow == null) {
            holder.clear();
        } else {
            holder.bind(remoteAnalysisRow);
        }
    }

    @NonNull
    @Override
    public RemoteAnalysisRowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.remote_analysis_row,parent, false);
        return new RemoteAnalysisRowViewHolder( view );
    }

    class RemoteAnalysisRowViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewArticleName;

        public RemoteAnalysisRowViewHolder(View view ) {
            super(view);
            textViewArticleName = view.findViewById(R.id.article_name);
        }

        protected void bind(RemoteAnalysisRow remoteAnalysisRow) {
            textViewArticleName.setText(remoteAnalysisRow.getArticleName());
        }

        protected void clear() {
            textViewArticleName.setText(null);
        }

    }
}

