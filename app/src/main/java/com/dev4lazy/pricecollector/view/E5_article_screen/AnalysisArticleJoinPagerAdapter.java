package com.dev4lazy.pricecollector.view.E5_article_screen;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;
import com.dev4lazy.pricecollector.view.E4_analysis_articles_list_screen.AnalysisArticleJoinDiffCallback;
import com.dev4lazy.pricecollector.viewmodel.AnalysisArticleJoinViewModel;

public class AnalysisArticleJoinPagerAdapter
        extends
            PagedListAdapter<AnalysisArticleJoin,
            AnalysisArticleJoinPagerAdapter.AnalysisArticleJoinPagerViewHolder>
        /* TODO XXX implements
            AnalysisArticlesPagerFragment.ArticleAddedDataCleaner */ {

    private AnalysisArticleJoinViewModel analysisArticleJoinViewModel;
    private AnalysisArticleJoinPagerViewHolder holder;

    public AnalysisArticleJoinPagerAdapter(
            AnalysisArticleJoinDiffCallback analysisArticleJoinDiffCallback,
            AnalysisArticleJoinViewModel analysisArticleJoinViewModel
        ) {
        super(analysisArticleJoinDiffCallback);
        this.analysisArticleJoinViewModel = analysisArticleJoinViewModel;
    }

    @NonNull
    @Override
    public AnalysisArticleJoinPagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.analysis_article, parent, false);
        holder = new AnalysisArticleJoinPagerViewHolder( view );
        return holder;
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

    /* TODO XXX @Override */
    public void cleanArticleAddedData() {
        holder.cleanArticleAddedData();
    }

    class AnalysisArticleJoinPagerViewHolder
            extends RecyclerView.ViewHolder {

        // Own Article
        // todo view.findViewById( R.id.analysisArticleFragment_imageArticle );
        private TextView ownCodeTextView;
        private TextView eanCodeTextView;
        private EditText competitorPriceEditText;
        private EditText articleCommentEditText;
        // Ref Article
        // todo view.findViewById( R.id.analysisArticleFragment_imageRefArticle );
        private EditText competitorArticleNameEditText;
        private EditText competitorArticleEANEditText;
        private EditText competitorArticleCommentEditText;

        public AnalysisArticleJoinPagerViewHolder(View view ) {
            super(view);
            setView( view );
        }

        private void setView( View view ) {
            // Own Article
            // todo view.findViewById( R.id.analysisArticleFragment_imageArticle );
            ownCodeTextView = view.findViewById( R.id.analysis_article_OwnCode_editText );
            eanCodeTextView = view.findViewById( R.id.analysis_article_EAN_editText );
            competitorPriceEditText = view.findViewById( R.id.analysis_article_CompetitorPrice_editText );
            competitorPriceEditText.addTextChangedListener( new CompetitorPriceEditTextWatcher() );
            articleCommentEditText = view.findViewById( R.id.analysis_article_ArticleComment_editText );
            articleCommentEditText.addTextChangedListener( new ArticleCommentEditTextWatcher() );
            // Ref Article
            // todo view.findViewById( R.id.analysisArticleFragment_imageRefArticle );
            competitorArticleNameEditText = view.findViewById( R.id.analysis_article_refArticleName_editText );
            competitorArticleEANEditText = view.findViewById( R.id.analysis_article_refArticleEAN_editText );
            competitorArticleCommentEditText = view.findViewById( R.id.analysis_article_refArticleComment_editText );
        }

            class CompetitorPriceEditTextWatcher implements TextWatcher  {
                @Override
                public void beforeTextChanged (CharSequence s,int start, int count, int after){
                }

                @Override
                public void onTextChanged (CharSequence charSequence,int start, int before, int count){
                    Double priceFromInput = null;
                    if (charSequence.length()!=0) {
                        priceFromInput = Double.parseDouble(competitorPriceEditText.getText().toString());
                        if (priceFromInput.intValue()==0) {
                            priceFromInput = null;
                        }
                    }
                    AnalysisArticleJoin analysisArticleJoin = getItem( getAbsoluteAdapterPosition() );
                    Double priceFromAnalysisArticleJoin = analysisArticleJoin.getCompetitorStorePrice();
                    if (arePricesNotEqual( priceFromInput, priceFromAnalysisArticleJoin )) {
                        analysisArticleJoinViewModel.getChangeInformer().setCompetitorStorePrice(priceFromInput);
                        //analysisArticleJoin.getCh setCompetitorStorePrice(priceFromInput);
                        //analysisArticleJoinViewModel.setNeedToSave(true);
                        // TODO czy to niżej jest potrzebne do czegoś?
                        analysisArticleJoinViewModel.setAnalysisArticleJoin(analysisArticleJoin);
                    }
                }

                private boolean arePricesNotEqual( Double price1, Double price2 ) {
                    if (price1==null) {
                        // TODO czy nie miałobyc tak że jak jest pusty stringi null to sa równe?
                        return !(price2==null);
                    }
                    if (price2==null) {
                        return true;
                    }
                    return !(price1.equals(price2));
                }

                @Override
                public void afterTextChanged (Editable s){
                }
            }

            class ArticleCommentEditTextWatcher implements TextWatcher  {
                @Override
                public void beforeTextChanged (CharSequence s,int start, int count, int after){
                }

                @Override
                public void onTextChanged (CharSequence charSequence,int start, int before, int count){
                    String commentsFromInput = articleCommentEditText.getText().toString();
                    AnalysisArticleJoin analysisArticleJoin = getItem( getAbsoluteAdapterPosition() );
                    String commentsFromAnalysisArticleJoin = analysisArticleJoin.getComments();
                    /* TODO test
                    boolean result = isEmptyOrNull( null ); // true
                    result = isEmptyOrNull( "" ); // true
                    result = isEmptyOrNull( "1" ); // false
                    result = areCommentsNotEqual( null, null ); // false
                    result = areCommentsNotEqual( null, "" ); // false
                    result = areCommentsNotEqual( "", null ); // false
                    result = areCommentsNotEqual( "", "" ); // false
                    result = areCommentsNotEqual( null, "1" ); // true
                    result = areCommentsNotEqual( "1", null ); // true
                    result = areCommentsNotEqual( "", "1" ); // true
                    result = areCommentsNotEqual( "1", "" ); // true
                    result = areCommentsNotEqual( "1", "1" ); // false
                    */
                    if (areCommentsNotEqual( commentsFromInput, commentsFromAnalysisArticleJoin )) {
                        analysisArticleJoinViewModel.getChangeInformer().setComments( commentsFromInput );
                        // TODO XXX analysisArticleJoin.setComments(commentsFromInput);
                        // TODO XXX  analysisArticleJoinViewModel.getChangeInformer().setFlagNeedToSave(true);
                        // TODO czy wiersz niżej jest potrzebne do czegoś?
                        analysisArticleJoinViewModel.setAnalysisArticleJoin(analysisArticleJoin);
                    }
                }

                private boolean areCommentsNotEqual(String comments1, String comments2 ) {
                    if (isEmptyOrNull( comments1 )) {
                        return isNotEmptyOrNotNull( comments2 );
                    }
                    return !(comments1.equals(comments2));
                }

                private boolean isEmptyOrNull( String string ) {
                    return (string==null) || (string.isEmpty());
                }

                private boolean isNotEmptyOrNotNull(String string ) {
                    return !isEmptyOrNull(string);
                }

                @Override
                public void afterTextChanged (Editable s){
                }
            }

        protected void bind( AnalysisArticleJoin analysisArticleJoin ) {
            // Own Article
            // todo view.findViewById( R.id.analysisArticleFragment_imageArticle );
            ownCodeTextView.setText( analysisArticleJoin.getOwnCode() );
            eanCodeTextView.setText( analysisArticleJoin.getEanCode() );
            Double competitorStorePrice = analysisArticleJoin.getCompetitorStorePrice();
            if (competitorStorePrice==null) {
                competitorPriceEditText.setText( null );
            } else {
                competitorPriceEditText.setText( competitorStorePrice.toString() );
            }
            articleCommentEditText.setText( analysisArticleJoin.getComments() );
            // Ref Article
            // todo view.findViewById( R.id.analysisArticleFragment_imageRefArticle );
            competitorArticleNameEditText.setText( analysisArticleJoin.getReferenceArticleName() );
            competitorArticleEANEditText.setText( analysisArticleJoin.getReferenceArticleEan() );
            competitorArticleCommentEditText.setText( analysisArticleJoin.getReferenceArticleDescription() );
        }

        protected void clear() {
            // Own Article
            // todo view.findViewById( R.id.analysisArticleFragment_imageArticle );
            ownCodeTextView.setText( null );
            eanCodeTextView.setText( null );
            competitorPriceEditText.setText( null );
            articleCommentEditText.setText( null );
            // Ref Article
            // todo view.findViewById( R.id.analysisArticleFragment_imageRefArticle );
            competitorArticleNameEditText.setText( null );
            competitorArticleEANEditText.setText( null );
            competitorArticleCommentEditText.setText( null );
        }

        protected void cleanArticleAddedData() {
            // Own Article
            // todo view.findViewById( R.id.analysisArticleFragment_imageArticle );
            competitorPriceEditText.setText( "" );
            articleCommentEditText.setText( "" );
            // Ref Article
            // todo view.findViewById( R.id.analysisArticleFragment_imageRefArticle );
            competitorArticleNameEditText.setText( "" );
            competitorArticleEANEditText.setText( "" );
            competitorArticleCommentEditText.setText( "" );
            notifyDataSetChanged();
            // Own Article
            AnalysisArticleJoin analysisArticleJoin = analysisArticleJoinViewModel.getAnalysisArticleJoin();
            analysisArticleJoin.setComments( "" );
            analysisArticleJoin.setCompetitorStorePrice( null );
            // Ref Article
            analysisArticleJoin.setReferenceArticleName( "" );
            analysisArticleJoin.setReferenceArticleEan( "" );
            analysisArticleJoin.setReferenceArticleDescription( "" );
        }
    }

}

