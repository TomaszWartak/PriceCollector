package com.dev4lazy.pricecollector.view.E2_analyzes_list_screen;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.entities.Analysis;
import com.dev4lazy.pricecollector.model.logic.AnalysisDataDownloader;
import com.dev4lazy.pricecollector.model.utils.DateConverter;
import com.dev4lazy.pricecollector.AppHandle;
import com.dev4lazy.pricecollector.utils.AppUtils;
import com.dev4lazy.pricecollector.view.ProgressBarPresenter;
import com.dev4lazy.pricecollector.view.ProgressPresenter;
import com.dev4lazy.pricecollector.viewmodel.AnalyzesListViewModel;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.dev4lazy.pricecollector.view.ProgressPresenter.DATA_SIZE_UNKNOWN;

public class AnalyzesRecyclerView extends RecyclerView {

    public AnalyzesRecyclerView(@NonNull Context context) {
        super(context);
    }

    public AnalyzesRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super( context, attrs );
    }

    public void setup() {
        setLayoutManager( new LinearLayoutManager(getContext()));
        setAdapter( new AnalyzesRecyclerView.AnalysisAdapter(new AnalysisDiffCallback()) );
    }

    public void submitAnalyzesList( PagedList<Analysis> analyzesList ) {
        ((AnalyzesRecyclerView.AnalysisAdapter)getAdapter()).submitList(analyzesList);
    }

    public void refresh() {
        getAdapter().notifyDataSetChanged();
    }

    public class AnalysisAdapter extends PagedListAdapter<Analysis, AnalysisAdapter.AnalysisViewHolder> {

        private final DateConverter dateConverter;

        public AnalysisAdapter( AnalysisDiffCallback analysisDiffCalback){
            super( analysisDiffCalback );
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

        class AnalysisViewHolder extends ViewHolder {
            // TODO sprawdź, czy tu nie jest potrzebny VieMOdel, czy to się nie zgubi przy obrocie

            private Analysis analysis;
            private final TextView textViewAnalysisCreationDate;
            private final TextView textViewAnalysisDueDate;
            private final TextView textViewAnalysisLastDataSentDate;
            // TODO XXX private final TextView textViewAnalysisConfirmationDate;
            private final TextView textViewAnalysisDataReadyToDownload;

            public AnalysisViewHolder( View view ) {
                super(view);
                textViewAnalysisCreationDate = view.findViewById( R.id.analysis_item__creation_date);
                textViewAnalysisDueDate = view.findViewById( R.id.analysis_Item__due_date);
                textViewAnalysisLastDataSentDate = view.findViewById( R.id.analysis_item__finish_date);
                // TODO XXX textViewAnalysisConfirmationDate = view.findViewById( R.id.analysis_item__confirmation_date );
                textViewAnalysisDataReadyToDownload = view.findViewById(R.id.analysis_item__data_to_download );
            }

            private void openCompetitorSlots( View view, Analysis analysis ) {
                // TODO XXX sloty muszą się otworzyć dla konkretnej analizy, więc jakoś (ViewModel) trzeba przekazać info o analizie
                AnalyzesListViewModel analyzesListViewModel = new ViewModelProvider( AppUtils.getActivity( getContext() ) ).get( AnalyzesListViewModel.class );
                analyzesListViewModel.setChosenAnalysis( analysis );
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
                    if (date.before( new Date() )) {
                        textViewAnalysisDueDate.setTextColor( ContextCompat.getColor( getContext(), R.color.colorWarning) );
                    }
                    textViewAnalysisDueDate.setText( dateConverter.date2String( date ) );
                }
                date = analysis.getFinishDate();
                if ( dateIsCorrect( date ) ) {
                    textViewAnalysisLastDataSentDate.setText( dateConverter.date2String( date ) );
                }
                /* TODO XXX
                date = analysis.getConfirmationDate();
                if ( dateIsCorrect( date ) ) {
                    textViewAnalysisConfirmationDate.setText( dateConverter.date2String( date ) );
                }
                */
                int visibility = textViewAnalysisDataReadyToDownload.getVisibility();
                if ( analysis.isDataNotDownloaded() ) {
                    if (visibility!= VISIBLE) {
                        textViewAnalysisDataReadyToDownload.setVisibility(VISIBLE);
                    }
                    itemView.setOnClickListener( (View v) -> {
                        textViewAnalysisDataReadyToDownload.setVisibility(GONE);
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
                    if (visibility!= GONE) {
                        textViewAnalysisDataReadyToDownload.setVisibility(GONE);
                    }
                    itemView.setOnClickListener( (View v) -> {
                        openCompetitorSlots( v, analysis );
                    });
                }
            }

            private boolean dateIsCorrect( Date date ) {
                return (date!=null)&&(dateConverter.date2Long(date)!=0L);
            }

            public void updateArticlesAllData( MutableLiveData<Boolean> finalResult, ProgressPresenter progressPresenter ) {
                AnalysisDataDownloader.getInstance().insertArticles( analysis, finalResult, progressPresenter );
            }

            protected void clear() {
                textViewAnalysisCreationDate.setText( null );
                textViewAnalysisDueDate.setText( null );
                textViewAnalysisLastDataSentDate.setText( null );
                // TODO XXX textViewAnalysisConfirmationDate.setText( null );
                textViewAnalysisDataReadyToDownload.setText( null );
            }

        }
    }
}

