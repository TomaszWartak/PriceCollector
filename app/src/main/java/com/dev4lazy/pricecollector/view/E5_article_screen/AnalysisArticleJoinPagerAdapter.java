package com.dev4lazy.pricecollector.view.E5_article_screen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
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

        private AnalysisArticleJoinViewModel analysisArticleJoinViewModel; // todo xxx ???
        // Own Article
        // todo XXX to ma byc w toolbarze -> private TextView textViewArticleName;
        private TextView textViewOwnCode;
        private TextView textViewEanCode;
        private TextView textViewArticleComment;
        private TextView textViewCompetitorPrice;
        // Ref Article
        private TextView textViewCompetitorArticleName;
        private TextView textViewCompetitorArticleEAN;
        private TextView textViewCompetitorArticleComment;

        public AnalysisArticleJoinPagerViewHolder(View view ) {
            super(view);
            setView( view );
        }

        private void setView( View view ) {
            // Own Article
            // todo view.findViewById( R.id.analysisArticleFragment_imageArticle );
            // todo XXX to ma byc w toolbarze -> textViewArticleName  = view.findViewById( R.id.ana );
            textViewOwnCode = view.findViewById( R.id.analysis_article_OwnCode_editText );
            textViewEanCode = view.findViewById( R.id.analysis_article_EAN_editText );
            textViewArticleComment = view.findViewById( R.id.analysis_article_ArticleComment_editText );
            textViewCompetitorPrice = view.findViewById( R.id.analysis_article_CompetitorPrice_editText );
                // Ref Article
            // todo view.findViewById( R.id.analysisArticleFragment_imageRefArticle );
            textViewCompetitorArticleName = view.findViewById( R.id.analysis_article_refArticleName_editText );
            textViewCompetitorArticleEAN = view.findViewById( R.id.analysis_article_refArticleEAN_editText );
            textViewCompetitorArticleComment = view.findViewById( R.id.analysis_article_refArticleComment_editText );

        }

        protected void bind( AnalysisArticleJoin analysisArticleJoin ) {
            // Own Article
            // todo XXX to ma byc w toolbarze -> textViewArticleName.setText( analysisArticleJoin.getArticleName() );
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
            textViewCompetitorArticleName.setText( analysisArticleJoin.getReferenceArticleName() );
            textViewCompetitorArticleEAN.setText( analysisArticleJoin.getReferenceArticleEan() );
            textViewCompetitorArticleComment.setText( analysisArticleJoin.getReferenceArticleDescription() );
        }

        protected void clear() {
            // Own Article
            // todo XXX to ma byc w toolbarze -> textViewArticleName.setText( null );
            textViewOwnCode.setText( null );
            textViewEanCode.setText( null );
            textViewArticleComment.setText( null );
            textViewCompetitorPrice.setText( null );
            // Ref Article
            textViewCompetitorArticleName.setText( null );
            textViewCompetitorArticleEAN.setText( null );
            textViewCompetitorArticleComment.setText( null );
        }

    }
}

