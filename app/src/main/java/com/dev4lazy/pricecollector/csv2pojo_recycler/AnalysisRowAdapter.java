package com.dev4lazy.pricecollector.csv2pojo_recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.csv2pojo.AnalysisRow;

public class AnalysisRowAdapter extends PagedListAdapter<AnalysisRow, AnalysisRowAdapter.AnalysisRowViewHolder> {

    private AnalysisRowDiffCalback analysisRowDiffCalback = null;

    public AnalysisRowAdapter( AnalysisRowDiffCalback analysisRowDiffCalback){
        super(analysisRowDiffCalback);
        this.analysisRowDiffCalback = analysisRowDiffCalback;

    }

    @Override
    public void onBindViewHolder(@NonNull AnalysisRowViewHolder holder, int position) {
        AnalysisRow analysisRow = getItem(position);
        if (analysisRow == null) {
            holder.clear();
        } else {
            holder.bind(analysisRow);
        }
    }

    @NonNull
    @Override
    public AnalysisRowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_analysis_row,parent, false);
        return new AnalysisRowViewHolder( view );
    }

    class AnalysisRowViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewArticleName;

        public AnalysisRowViewHolder( View view ) {
            super(view);
            textViewArticleName = view.findViewById(R.id.article_name);
        }

        protected void bind(AnalysisRow analysisRow) {
            textViewArticleName.setText(analysisRow.getArticleName());
        }

        protected void clear() {
            textViewArticleName.setText(null);
        }

    }
}

