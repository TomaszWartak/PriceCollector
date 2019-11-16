package com.dev4lazy.pricecollector.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.entities.Analysis;
import com.dev4lazy.pricecollector.model.utils.DateConverter;

public class AnalysisAdapter extends PagedListAdapter<Analysis, AnalysisAdapter.AnalysisViewHolder> {

    private AnalysisDiffCallback analysisDiffCallback = null; // todo
    private DateConverter dateConverter;

    public AnalysisAdapter( AnalysisDiffCallback analysisDiffCalback){
        super( analysisDiffCalback );
        this.analysisDiffCallback = analysisDiffCalback;
        dateConverter = new DateConverter();
    }

    @Override
    public void onBindViewHolder(@NonNull AnalysisViewHolder holder, int position) {
        Analysis analysis = getItem(position);
        if ( analysis == null) {
            holder.clear();
        } else {
            holder.bind( analysis );
        }
    }

    @NonNull
    @Override
    public AnalysisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.analysis_item, parent, false);
        return new AnalysisViewHolder( view );
    }

    class AnalysisViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewAnalysisCreationDate;
        private TextView textViewAnalysisDueDate;
        private TextView textViewAnalysisFinishDate;
        private TextView textViewAnalysisConfirmationDate;

        public AnalysisViewHolder( View view ) {
            super(view);
            textViewAnalysisCreationDate = view.findViewById( R.id.analysisItem_creationDate );
            textViewAnalysisDueDate = view.findViewById( R.id.analysisItem_dueDate );
            textViewAnalysisFinishDate = view.findViewById( R.id.analysisItem_finishDate );
            textViewAnalysisConfirmationDate = view.findViewById( R.id.analysisItem_confirmationDate );
        }

        protected void bind( Analysis analysis ) {
            textViewAnalysisCreationDate.setText( dateConverter.date2String( analysis.getCreationDate() ) );
            textViewAnalysisDueDate.setText( dateConverter.date2String( analysis.getDueDate() ) );
            textViewAnalysisFinishDate.setText( dateConverter.date2String( analysis.getFinishDate() ) );
            textViewAnalysisConfirmationDate.setText( dateConverter.date2String( analysis.getConfirmationDate() ) );
        }

        protected void clear() {
            textViewAnalysisCreationDate.setText( null );
            textViewAnalysisDueDate.setText( null );
            textViewAnalysisFinishDate.setText( null );
            textViewAnalysisConfirmationDate.setText( null );
        }

    }
}
