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
import com.dev4lazy.pricecollector.view.analysis_articles_list_screen.AnalysisArticleJoinDiffCalback;
import com.dev4lazy.pricecollector.viewmodel.AnalysisArticleJoinViewModel;

public class AnalysisArticleJoinPagerAdapter extends PagedListAdapter<AnalysisArticleJoin, AnalysisArticleJoinPagerAdapter.AnalysisArticleJoinPagerViewHolder> {

    public AnalysisArticleJoinPagerAdapter(AnalysisArticleJoinDiffCalback analysisArticleJoinDiffCalback ){
        super( analysisArticleJoinDiffCalback );
    }

    @Override
    public void onBindViewHolder(@NonNull AnalysisArticleJoinPagerViewHolder holder, int position) {
        AnalysisArticleJoin analysisArticleJoin = getItem(position);
        if (analysisArticleJoin == null) {
            holder.clear();
        } else {
            holder.bind(analysisArticleJoin);
        }
    }

    @NonNull
    @Override
    public AnalysisArticleJoinPagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.analysis_article_fragment, parent, false);
        return new AnalysisArticleJoinPagerViewHolder( view );
    }

    class AnalysisArticleJoinPagerViewHolder extends RecyclerView.ViewHolder {

        AnalysisArticleJoin analysisArticleJoin;
        private AnalysisArticleJoinViewModel analysisArticleJoinViewModel;
        private TextView textViewArticleName;
        private TextView textViewOwnCode;
        private TextView textViewEanCode;
        private TextView textViewArticleComment;
        private TextView textViewCompetitorPrice;

        public AnalysisArticleJoinPagerViewHolder(View view ) {
            super(view);
            setView( view );
        }

        private void setView( View view ) {
            // todo view.findViewById( R.id.analysisArticleFragment_imageArticle );
            textViewArticleName  = view.findViewById( R.id.analysisArticleFragment_articleName );
            textViewOwnCode = view.findViewById( R.id.analysisArticleFragment_ownCode );
            textViewEanCode  = view.findViewById( R.id.analysisArticleFragment_eanCode );
            textViewArticleComment  = view.findViewById( R.id.analysisArticleFragment_articleComment );
            textViewCompetitorPrice  = view.findViewById( R.id.analysisArticleFragment_competitorPrice );

            // todo view.findViewById( R.id.analysisArticleFragment_imageRefArticle );
            // todo TextView textView = view.findViewById( R.id.analysisArticleFragment_refArticleComment );
            // todo textView.setText( analysisArticleJoinViewModel.getAnalysisArticleJoin().get
        }

        protected void bind( AnalysisArticleJoin analysisArticleJoin ) {
            /*textViewArticleName.setText( analysisArticleJoinViewModel.getAnalysisArticleJoin().getArticleName() );
            textViewOwnCode.setText( analysisArticleJoinViewModel.getAnalysisArticleJoin().getOwnCode() );
            textViewEanCode.setText( analysisArticleJoinViewModel.getAnalysisArticleJoin().getEanCode() );
            textViewArticleComment.setText( analysisArticleJoinViewModel.getAnalysisArticleJoin().getComments() );
            Double competitorStorePrice = analysisArticleJoinViewModel.getAnalysisArticleJoin().getCompetitorStorePrice();
             */
            textViewArticleName.setText( analysisArticleJoin.getArticleName() );
            textViewOwnCode.setText( analysisArticleJoin.getOwnCode() );
            textViewEanCode.setText( analysisArticleJoin.getEanCode() );
            textViewArticleComment.setText( analysisArticleJoin.getComments() );
            Double competitorStorePrice = analysisArticleJoin.getCompetitorStorePrice();
            if (competitorStorePrice==null) {
                textViewCompetitorPrice.setText( "?" );
            } else {
                textViewCompetitorPrice.setText( competitorStorePrice.toString() );
            }
        }

        protected void clear() {
            textViewArticleName.setText( null );
            textViewOwnCode.setText( null );
            textViewEanCode.setText( null );
            textViewArticleComment.setText( null );
            textViewCompetitorPrice.setText( null );
        }

    }
}

