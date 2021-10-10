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
            holder.bind( analysisArticleJoin );
        }
    }

    /* TODO XXX @Override */
    public void clearCompetitorArticleDataOnScreen() {
        holder.clearCompetitorArticleDataOnScreen();
    }

    class AnalysisArticleJoinPagerViewHolder extends RecyclerView.ViewHolder {

        // Own Article
        // todo view.findViewById( R.id.analysisArticleFragment_imageArticle );
        private TextView ownCodeTextView;
        private TextView eanCodeTextView;
        private EditText competitorPriceEditText;
        private EditText articleCommentEditText;
        // Ref Article
        // todo view.findViewById( R.id.analysisArticleFragment_imageRefArticle );
        private EditText referenceArticleNameEditText;
        private EditText referenceArticleEANEditText;
        private EditText referenceArticleDescriptionEditText;

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
            referenceArticleNameEditText = view.findViewById( R.id.analysis_article_refArticleName_editText );
            referenceArticleNameEditText.addTextChangedListener( new ReferenceArticleNameEditTextWatcher() );

            referenceArticleEANEditText = view.findViewById( R.id.analysis_article_refArticleEAN_editText );
            referenceArticleEANEditText.addTextChangedListener( new ReferenceArticleEanEditTextWatcher() );

            referenceArticleDescriptionEditText = view.findViewById( R.id.analysis_article_refArticleComment_editText );
            referenceArticleDescriptionEditText.addTextChangedListener( new ReferenceArticleDescriptionEditTextWatcher() );
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
                        analysisArticleJoinViewModel.getValuesStateHolder().setCompetitorStorePrice(priceFromInput);
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
                    if (areTextsNotEqual( commentsFromInput, commentsFromAnalysisArticleJoin )) {
                        analysisArticleJoinViewModel.getValuesStateHolder().setComments( commentsFromInput );
                        // TODO XXX analysisArticleJoin.setComments(commentsFromInput);
                        // TODO XXX  analysisArticleJoinViewModel.getChangeInformer().setFlagNeedToSave(true);
                        // TODO czy wiersz niżej jest potrzebne do czegoś?
                        analysisArticleJoinViewModel.setAnalysisArticleJoin(analysisArticleJoin);
                    }
                }

                @Override
                public void afterTextChanged (Editable s){
                }
            }

        class ReferenceArticleNameEditTextWatcher implements TextWatcher  {
            @Override
            public void beforeTextChanged (CharSequence s,int start, int count, int after){
            }

            @Override
            public void onTextChanged (CharSequence charSequence,int start, int before, int count){
                String nameFromInput = referenceArticleNameEditText.getText().toString();
                AnalysisArticleJoin analysisArticleJoin = getItem( getAbsoluteAdapterPosition() );
                String nameFromAnalysisArticleJoin = analysisArticleJoin.getReferenceArticleName();
                    /* TODO test
                    boolean result = isEmptyOrNull( null ); // true
                    result = isEmptyOrNull( "" ); // true
                    result = isEmptyOrNull( "1" ); // false
                    result = areNamesNotEqual( null, null ); // false
                    result = areNamesNotEqual( null, "" ); // false
                    result = areNamesNotEqual( "", null ); // false
                    result = areNamesNotEqual( "", "" ); // false
                    result = areNamesNotEqual( null, "1" ); // true
                    result = areNamesNotEqual( "1", null ); // true
                    result = areNamesNotEqual( "", "1" ); // true
                    result = areNamesNotEqual( "1", "" ); // true
                    result = areNamesNotEqual( "1", "1" ); // false
                    /*/
                if (areTextsNotEqual( nameFromInput, nameFromAnalysisArticleJoin )) {
                    analysisArticleJoinViewModel.getValuesStateHolder().setReferenceArticleName( nameFromInput );
                    // TODO czy wiersz niżej jest potrzebne do czegoś?
                    analysisArticleJoinViewModel.setAnalysisArticleJoin(analysisArticleJoin);
                }
            }

            @Override
            public void afterTextChanged (Editable s){
            }
        }

        class ReferenceArticleEanEditTextWatcher implements TextWatcher  {
            @Override
            public void beforeTextChanged (CharSequence s,int start, int count, int after){
            }

            @Override
            public void onTextChanged (CharSequence charSequence,int start, int before, int count){
                String eanFromInput = referenceArticleEANEditText.getText().toString();
                AnalysisArticleJoin analysisArticleJoin = getItem( getAbsoluteAdapterPosition() );
                String eanFromAnalysisArticleJoin = analysisArticleJoin.getReferenceArticleEanCodeValue();
                    /* TODO test
                    boolean result = isEmptyOrNull( null ); // true
                    result = isEmptyOrNull( "" ); // true
                    result = isEmptyOrNull( "1" ); // false
                    result = areNamesNotEqual( null, null ); // false
                    result = areNamesNotEqual( null, "" ); // false
                    result = areNamesNotEqual( "", null ); // false
                    result = areNamesNotEqual( "", "" ); // false
                    result = areNamesNotEqual( null, "1" ); // true
                    result = areNamesNotEqual( "1", null ); // true
                    result = areNamesNotEqual( "", "1" ); // true
                    result = areNamesNotEqual( "1", "" ); // true
                    result = areNamesNotEqual( "1", "1" ); // false
                    /*/
                if (areTextsNotEqual( eanFromInput, eanFromAnalysisArticleJoin )) {
                    analysisArticleJoinViewModel.getValuesStateHolder().setReferenceArticleEan( eanFromInput );
                    // TODO czy wiersz niżej jest potrzebne do czegoś?
                    // analysisArticleJoinViewModel.setAnalysisArticleJoin(analysisArticleJoin);
                }
            }

            @Override
            public void afterTextChanged (Editable s){
            }
        }

        class ReferenceArticleDescriptionEditTextWatcher implements TextWatcher  {
            @Override
            public void beforeTextChanged (CharSequence s,int start, int count, int after){
            }

            @Override
            public void onTextChanged (CharSequence charSequence,int start, int before, int count){
                String descriptionFromInput = referenceArticleDescriptionEditText.getText().toString();
                AnalysisArticleJoin analysisArticleJoin = getItem( getAbsoluteAdapterPosition() );
                String descriptionFromAnalysisArticleJoin = analysisArticleJoin.getReferenceArticleDescription();
                    /* TODO test
                    boolean result = isEmptyOrNull( null ); // true
                    result = isEmptyOrNull( "" ); // true
                    result = isEmptyOrNull( "1" ); // false
                    result = areNamesNotEqual( null, null ); // false
                    result = areNamesNotEqual( null, "" ); // false
                    result = areNamesNotEqual( "", null ); // false
                    result = areNamesNotEqual( "", "" ); // false
                    result = areNamesNotEqual( null, "1" ); // true
                    result = areNamesNotEqual( "1", null ); // true
                    result = areNamesNotEqual( "", "1" ); // true
                    result = areNamesNotEqual( "1", "" ); // true
                    result = areNamesNotEqual( "1", "1" ); // false
                    /*/
                if (areTextsNotEqual( descriptionFromInput, descriptionFromAnalysisArticleJoin )) {
                    analysisArticleJoinViewModel.getValuesStateHolder().setReferenceArticleDescription( descriptionFromInput );
                    // TODO czy wiersz niżej jest potrzebne do czegoś?
                    analysisArticleJoinViewModel.setAnalysisArticleJoin(analysisArticleJoin);
                }
            }

            @Override
            public void afterTextChanged (Editable s){
            }
        }

        private boolean areTextsNotEqual(String text1, String text2 ) {
            if (isEmptyOrNull( text1 )) {
                return isNotEmptyOrNotNull( text2 );
            }
            return !(text1.equals(text2));
        }

        private boolean isEmptyOrNull( String string ) {
            return (string==null) || (string.isEmpty());
        }

        private boolean isNotEmptyOrNotNull(String string ) {
            return !isEmptyOrNull(string);
        }

        protected void bind(AnalysisArticleJoin analysisArticleJoin ) {
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
            referenceArticleNameEditText.setText( analysisArticleJoin.getReferenceArticleName() );
            referenceArticleEANEditText.setText( analysisArticleJoin.getReferenceArticleEanCodeValue() );
            referenceArticleDescriptionEditText.setText( analysisArticleJoin.getReferenceArticleDescription() );
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
            referenceArticleNameEditText.setText( null );
            referenceArticleEANEditText.setText( null );
            referenceArticleDescriptionEditText.setText( null );
        }

        protected void clearCompetitorArticleDataOnScreen() {
            // Own Article
            // todo view.findViewById( R.id.analysisArticleFragment_imageArticle );
            competitorPriceEditText.setText( "" );
            articleCommentEditText.setText( "" );
            // Ref Article
            // todo view.findViewById( R.id.analysisArticleFragment_imageRefArticle );
            referenceArticleNameEditText.setText( "" );
            referenceArticleEANEditText.setText( "" );
            referenceArticleDescriptionEditText.setText( "" );
            notifyDataSetChanged();
        }

    }

}

