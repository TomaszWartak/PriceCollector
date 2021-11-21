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
import com.dev4lazy.pricecollector.model.logic.AnalysisDataDownloader_2;
import com.dev4lazy.pricecollector.model.utils.DateConverter;
import com.dev4lazy.pricecollector.utils.AppUtils;
import com.dev4lazy.pricecollector.view.utils.ProgressBarWrapper;
import com.dev4lazy.pricecollector.view.utils.ProgressPresenter;
import com.dev4lazy.pricecollector.view.utils.ProgressPresentingManager;
import com.dev4lazy.pricecollector.view.utils.TextViewMessageWrapper;
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

import static com.dev4lazy.pricecollector.view.utils.ProgressPresenter.DATA_SIZE_UNKNOWN;
import static com.dev4lazy.pricecollector.view.utils.ProgressPresenter.DONT_HIDE_WHEN_FINISHED;

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
                holder.bind( analysis, position );
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

            private final TextView textViewAnalysisHeader;
            private final TextView textViewAnalysisCreationDate;
            private final TextView textViewAnalysisDueDate;
            private final TextView textViewAnalysisLastDataSentDate;
            // TODO XXX private final TextView textViewAnalysisConfirmationDate;
            private final TextView textViewAnalysisDataReadyToDownload;

            public AnalysisViewHolder( View view ) {
                super(view);
                textViewAnalysisHeader = view.findViewById( R.id.analysis_item__header );
                setLabelsMaxWidth(view);
                textViewAnalysisCreationDate = view.findViewById( R.id.analysis_item__creation_date);
                textViewAnalysisDueDate = view.findViewById( R.id.analysis_Item__due_date);
                textViewAnalysisLastDataSentDate = view.findViewById( R.id.analysis_item__finish_date);
                // TODO XXX textViewAnalysisConfirmationDate = view.findViewById( R.id.analysis_item__confirmation_date );
                textViewAnalysisDataReadyToDownload = view.findViewById(R.id.analysis_item__data_to_download );
            }

            public void setLabelsMaxWidth(View view) {
                int labelMaxWidth = (int)Math.round( Resources.getSystem().getDisplayMetrics().widthPixels*0.8);
                setLabelMaxWidth( view.findViewById( R.id.analysis_item__label_creation_date ), labelMaxWidth );
                setLabelMaxWidth( view.findViewById( R.id.analysis_item__label_due_date ), labelMaxWidth );
                setLabelMaxWidth( view.findViewById( R.id.analysis_item__label_finish_date ), labelMaxWidth );
            }

            public void setLabelMaxWidth( TextView textViewLabel, int maxWidth ) {
                textViewLabel.setMaxWidth( maxWidth );
            }

            protected void bind( Analysis analysis, int positionInAdapter) {
                setHeaderAndCreationDateText( analysis.getCreationDate() );
                setDueDateText( analysis.getDueDate( ) );
                setLastDataSentDateText( analysis.getFinishDate() );
                setAnalysisDataReadyToDownload( analysis, positionInAdapter);
            }

            private void setHeaderAndCreationDateText(Date date) {
                if ( dateIsCorrect( date ) ) {
                    textViewAnalysisHeader.setText( dateConverter.date2StringWithFormat( date, "yyyy-MM"));
                    textViewAnalysisCreationDate.setText( dateConverter.date2String( date ) );
                }
            }

            private void setDueDateText(Date date) {
                if ( dateIsCorrect(date) ) {
                    if (date.before( new Date() )) {
                        textViewAnalysisDueDate.setTextColor( ContextCompat.getColor( getContext(), R.color.colorWarning) );
                    }
                    textViewAnalysisDueDate.setText( dateConverter.date2String(date) );
                }
            }

            private void setLastDataSentDateText(Date date) {
                if ( dateIsCorrect( date ) ) {
                    textViewAnalysisLastDataSentDate.setText( dateConverter.date2String( date ) );
                }
            }

            private void setAnalysisDataReadyToDownload( Analysis analysis, int positionInAdapter) {
                int dataReadyToDownloadVisibility = textViewAnalysisDataReadyToDownload.getVisibility();
                if ( analysis.isDataNotDownloaded() ) {
                    if (dataReadyToDownloadVisibility != VISIBLE) {
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
                                    PagedList<Analysis> analyzesList = getCurrentList();
                                    Analysis analysisToInvalidate = analyzesList.get( positionInAdapter );
                                    analysisToInvalidate.setDataDownloaded( true );
                                    analyzesList.getDataSource().invalidate();
                                    notifyItemChanged( positionInAdapter );
                                }
                            }
                        };
                        finalResult.observeForever(resultObserver);
                        updateArticlesAllData(
                                analysis,
                                finalResult,
                                new ProgressPresentingManager(
                                    new ProgressPresenter(
                                        new ProgressBarWrapper(
                                            itemView.findViewById(R.id.analysis_item__progressBar)
                                        ),
                                        DATA_SIZE_UNKNOWN,
                                        DONT_HIDE_WHEN_FINISHED
                                    ),
                                    new TextViewMessageWrapper(
                                        itemView.findViewById(R.id.analysis_item__progress_message)
                                    ),
                                    null
                                )
                        );
                    });
                } else {
                    if (dataReadyToDownloadVisibility != GONE) {
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

            public void updateArticlesAllData(
                    Analysis analysis,
                    MutableLiveData<Boolean> finalResult,
                    ProgressPresentingManager progressPresentingManager ) {
                // TODO XXX AnalysisDataDownloader.getInstance().insertArticles( analysis, finalResult, progressPresenter );
                new AnalysisDataDownloader_2().downloadData( analysis, finalResult, progressPresentingManager );
            }

            private void openCompetitorSlots( View view, Analysis analysis ) {
                // TODO XXX sloty muszą się otworzyć dla konkretnej analizy, więc jakoś (ViewModel) trzeba przekazać info o analizie
                AnalyzesListViewModel analyzesListViewModel = new ViewModelProvider( AppUtils.getActivity( getContext() ) ).get( AnalyzesListViewModel.class );
                analyzesListViewModel.setChosenAnalysis( analysis );
                Navigation.findNavController( view ).navigate(R.id.action_analyzesListFragment_to_analysisCompetitorsFragment);
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

