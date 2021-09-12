package com.dev4lazy.pricecollector.view.E5_article_screen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;
import com.dev4lazy.pricecollector.view.E4_analysis_articles_list_screen.AnalysisArticleJoinDiffCallback;
import com.dev4lazy.pricecollector.viewmodel.AnalysisArticleJoinViewModel;

public class AnalysisArticleJoinPagerAdapter extends PagedListAdapter<AnalysisArticleJoin, AnalysisArticleJoinPagerAdapter.AnalysisArticleJoinPagerViewHolder> {

    public AnalysisArticleJoinPagerAdapter(AnalysisArticleJoinDiffCallback analysisArticleJoinDiffCallback){
        super(analysisArticleJoinDiffCallback);
    }

    @NonNull
    @Override
    public AnalysisArticleJoinPagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.analysis_article, parent, false);
        return new AnalysisArticleJoinPagerViewHolder( view );
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

    class AnalysisArticleJoinPagerViewHolder extends RecyclerView.ViewHolder {

        // todo XXX usuń AnalysisArticleJoin analysisArticleJoin;
        private AnalysisArticleJoinViewModel analysisArticleJoinViewModel; // todo xxx ???
        private TextView textViewArticleName;
        private TextView textViewOwnCode;
        private TextView textViewEanCode;
        private TextView textViewArticleComment;
        private TextView textViewCompetitorPrice;
        private TextView textViewCompetitorArticleComment;

        public AnalysisArticleJoinPagerViewHolder(View view ) {
            super(view);
            setView( view );
        }

        private void setView( View view ) {
            // Own Article
            // todo view.findViewById( R.id.analysisArticleFragment_imageArticle );
            textViewArticleName  = view.findViewById( R.id.analysisArticleFragment_articleName );
            textViewOwnCode = view.findViewById( R.id.analysisArticleFragment_ownCode );
            textViewEanCode  = view.findViewById( R.id.analysisArticleFragment_eanCode );
            textViewArticleComment  = view.findViewById( R.id.analysisArticleFragment_articleComment );
            textViewCompetitorPrice  = view.findViewById( R.id.analysisArticleFragment_competitorPrice );
            // Ref Article
            // todo view.findViewById( R.id.analysisArticleFragment_imageRefArticle );
            textViewCompetitorArticleComment = view.findViewById( R.id.analysisArticleFragment_refArticleComment );

        }

        protected void bind( AnalysisArticleJoin analysisArticleJoin ) {
            // Own Article
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
            // Ref Article
            textViewCompetitorArticleComment.setText( analysisArticleJoin.getRefArticleComment() );
        }

        protected void clear() {
            // Own Article
            textViewArticleName.setText( null );
            textViewOwnCode.setText( null );
            textViewEanCode.setText( null );
            textViewArticleComment.setText( null );
            textViewCompetitorPrice.setText( null );
            // Ref Article
       }

    }
}

