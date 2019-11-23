package com.dev4lazy.pricecollector.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.entities.Analysis;
import com.dev4lazy.pricecollector.model.logic.AnalysisDataUpdater;
import com.dev4lazy.pricecollector.model.utils.DateConverter;

import static com.dev4lazy.pricecollector.view.ProgressPresenter.DATA_SIZE_UNKNOWN;

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
        // TODO sprawdź, czy tu nie jest potrzebny VieMOdel, czy to się nie zgubi przy obrocie

        private TextView textViewAnalysisCreationDate;
        private TextView textViewAnalysisDueDate;
        private TextView textViewAnalysisFinishDate;
        private TextView textViewAnalysisConfirmationDate;

        public AnalysisViewHolder( View view ) {
            super(view);
            textViewAnalysisCreationDate = view.findViewById( R.id.analysis_item__creation_date);
            textViewAnalysisDueDate = view.findViewById( R.id.analysis_Item__due_date);
            textViewAnalysisFinishDate = view.findViewById( R.id.analysis_item__finish_date);
            textViewAnalysisConfirmationDate = view.findViewById( R.id.analysis_item__confirmation_date );
            view.setOnClickListener( (View v) -> {
                openCompetitorSlots( v );
            });
            view.findViewById(R.id.button_analysis_articles_create).setOnClickListener( (View v) -> {
                updateArticlesAllData( new ProgressBarPresenter( view.findViewById(R.id.analysis_item__progressBar), DATA_SIZE_UNKNOWN ) );
            });
        }

        private void openCompetitorSlots( View view) {
            // TODO sloty muszą się otworzyć dla konkretnej analizy, więc jakoś (ViewModel) trzeba przekazać info o analizie
            Navigation.findNavController( view ).navigate(R.id.action_mainFragment_to_analysisCompetitorsFragment);
        }

        public void updateArticlesAllData( ProgressPresenter progressPresenter ) {
            AnalysisDataUpdater.getInstance().insertArticles( progressPresenter );
        }

        protected void bind( Analysis analysis ) {
            if (analysis.getCreationDate()!=null) {
                textViewAnalysisCreationDate.setText(dateConverter.date2String( analysis.getCreationDate() ) );
            }
            if (analysis.getDueDate()!=null) {
                textViewAnalysisDueDate.setText( dateConverter.date2String( analysis.getDueDate() ) );
            }
            if (analysis.getFinishDate()!=null) {
                textViewAnalysisFinishDate.setText( dateConverter.date2String( analysis.getFinishDate() ) );
            }
            if (analysis.getConfirmationDate()!=null) {
                textViewAnalysisConfirmationDate.setText( dateConverter.date2String( analysis.getConfirmationDate() ) );
            }
        }

        protected void clear() {
            textViewAnalysisCreationDate.setText( null );
            textViewAnalysisDueDate.setText( null );
            textViewAnalysisFinishDate.setText( null );
            textViewAnalysisConfirmationDate.setText( null );
        }

    }
}
