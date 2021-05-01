package com.dev4lazy.pricecollector.view.E5_article_screen;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.viewmodel.AnalysisArticleJoinViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnalysisArticleFragment extends Fragment {

    private AnalysisArticleJoinViewModel analysisArticleJoinViewModel;

    public AnalysisArticleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        analysisArticleJoinViewModel = ViewModelProviders.of( getActivity() ).get( AnalysisArticleJoinViewModel.class );
        View view = inflater.inflate(R.layout.analysis_article_fragment, container, false);
        setView( view );
        return view;
    }

    private void setView( View view ) {
        // todo view.findViewById( R.id.analysisArticleFragment_imageArticle );
        TextView textViewArticleName  = view.findViewById( R.id.analysisArticleFragment_articleName );
        textViewArticleName.setText( analysisArticleJoinViewModel.getAnalysisArticleJoin().getArticleName() );
        TextView textViewOwnCode = view.findViewById( R.id.analysisArticleFragment_ownCode );
        textViewOwnCode.setText( analysisArticleJoinViewModel.getAnalysisArticleJoin().getOwnCode() );
        TextView textViewEanCode  = view.findViewById( R.id.analysisArticleFragment_eanCode );
        textViewEanCode.setText( analysisArticleJoinViewModel.getAnalysisArticleJoin().getEanCode() );
        TextView textViewArticleComment  = view.findViewById( R.id.analysisArticleFragment_articleComment );
        textViewArticleComment.setText( analysisArticleJoinViewModel.getAnalysisArticleJoin().getComments() );
        TextView textViewCompetitorPrice  = view.findViewById( R.id.analysisArticleFragment_competitorPrice );
        Double competitorStorePrice = analysisArticleJoinViewModel.getAnalysisArticleJoin().getCompetitorStorePrice();
        if (competitorStorePrice==null) {
            textViewCompetitorPrice.setText( "?" );
        } else {
            textViewCompetitorPrice.setText( competitorStorePrice.toString() );
        }
        // todo view.findViewById( R.id.analysisArticleFragment_imageRefArticle );
        // todo TextView textView = view.findViewById( R.id.analysisArticleFragment_refArticleComment );
        // todo textView.setText( analysisArticleJoinViewModel.getAnalysisArticleJoin().get
    }

}
