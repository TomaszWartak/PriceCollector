package com.dev4lazy.pricecollector.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.entities.Analysis;
import com.dev4lazy.pricecollector.model.logic.AnalysisDataUpdater;
import com.dev4lazy.pricecollector.model.utils.DateConverter;
import com.dev4lazy.pricecollector.utils.AppHandle;

import java.util.Date;

import static com.dev4lazy.pricecollector.view.ProgressPresenter.DATA_SIZE_UNKNOWN;

public class AnalysisAdapter extends PagedListAdapter<Analysis, AnalysisAdapter.AnalysisViewHolder> {

    private AnalysisDiffCallback analysisDiffCallback = null; // todo
    private final DateConverter dateConverter;

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

        private Analysis analysis;
        private final TextView textViewAnalysisCreationDate;
        private final TextView textViewAnalysisDueDate;
        private final TextView textViewAnalysisFinishDate;
        private final TextView textViewAnalysisConfirmationDate;
        private final TextView textViewAnalysisDataReadyToDownload;

        public AnalysisViewHolder( View view ) {
            super(view);
            textViewAnalysisCreationDate = view.findViewById( R.id.analysis_item__creation_date);
            textViewAnalysisDueDate = view.findViewById( R.id.analysis_Item__due_date);
            textViewAnalysisFinishDate = view.findViewById( R.id.analysis_item__finish_date);
            textViewAnalysisConfirmationDate = view.findViewById( R.id.analysis_item__confirmation_date );
            textViewAnalysisDataReadyToDownload = view.findViewById(R.id.analysis_item__data_to_download );
        }

        private void openCompetitorSlots( View view) {
            // TODO sloty muszą się otworzyć dla konkretnej analizy, więc jakoś (ViewModel) trzeba przekazać info o analizie
            Navigation.findNavController( view ).navigate(R.id.action_analyzesListFragment_to_analysisCompetitorsFragment);
        }

        protected void bind( Analysis analysis ) {
            this.analysis = analysis;
            Date date = analysis.getCreationDate();
            if ( dateIsCorrect( date ) ) {
                textViewAnalysisCreationDate.setText( dateConverter.date2String( date ) );
            }
            date = analysis.getDueDate();
            if ( dateIsCorrect( date ) ) {
                textViewAnalysisDueDate.setText( dateConverter.date2String( date ) );
            }
            date = analysis.getFinishDate();
            if ( dateIsCorrect( date ) ) {
                textViewAnalysisFinishDate.setText( dateConverter.date2String( date ) );
            }
            date = analysis.getConfirmationDate();
            if ( dateIsCorrect( date ) ) {
                textViewAnalysisConfirmationDate.setText( dateConverter.date2String( date ) );
            }
            int visibility = textViewAnalysisDataReadyToDownload.getVisibility();
            if ( analysis.isDataNotDownloaded() ) {
                if (visibility!=View.VISIBLE) {
                    textViewAnalysisDataReadyToDownload.setVisibility(View.VISIBLE);
                }
                itemView.setOnClickListener( (View v) -> {
                    textViewAnalysisDataReadyToDownload.setVisibility(View.GONE);
                    MutableLiveData<Boolean> finalResult = new MutableLiveData<>();
                    Observer<Boolean> resultObserver = new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean analysisDataDownloaded) {
                            if (analysisDataDownloaded != null) {
                                finalResult.removeObserver(this); // this = observer...
                                AppHandle.getHandle().getRepository().getLocalDataRepository().updateAnalysis( analysis, null );
                                notifyDataSetChanged();
                            }
                        }
                    };
                    finalResult.observeForever(resultObserver);
                    updateArticlesAllData(
                            finalResult,
                            new ProgressBarPresenter(
                                    itemView.findViewById(R.id.analysis_item__progressBar),
                                    DATA_SIZE_UNKNOWN ) );
                });
            } else {
                if (visibility!=View.GONE) {
                    textViewAnalysisDataReadyToDownload.setVisibility(View.GONE);
                }
                itemView.setOnClickListener( (View v) -> {
                    openCompetitorSlots( v );
                });
            }
        }

        private boolean dateIsCorrect( Date date ) {
            return (date!=null)&&(dateConverter.date2Long(date)!=0L);
        }

        public void updateArticlesAllData( MutableLiveData<Boolean> finalResult, ProgressPresenter progressPresenter ) {
            AnalysisDataUpdater.getInstance().insertArticles( analysis, finalResult, progressPresenter );
        }

        protected void clear() {
            textViewAnalysisCreationDate.setText( null );
            textViewAnalysisDueDate.setText( null );
            textViewAnalysisFinishDate.setText( null );
            textViewAnalysisConfirmationDate.setText( null );
            textViewAnalysisDataReadyToDownload.setText( null );
        }

    }
}
