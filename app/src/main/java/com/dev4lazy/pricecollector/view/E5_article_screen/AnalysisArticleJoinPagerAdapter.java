package com.dev4lazy.pricecollector.view.E5_article_screen;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;
import com.dev4lazy.pricecollector.view.E4_analysis_articles_list_screen.AnalysisArticleJoinDiffCallback;
import com.dev4lazy.pricecollector.viewmodel.AnalysisArticleJoinViewModel;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        private char decimalSeparator;
        private DecimalFormat decimalFormat;
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
            setDecimalFormatInformation();
            setView( view );
        }

        public void setDecimalFormatInformation() {
            decimalSeparator = DecimalFormatSymbols.getInstance().getDecimalSeparator();
            decimalFormat = new DecimalFormat("0.00" );
        }

        private void setView( View view ) {
            // Own Article
            // todo view.findViewById( R.id.analysisArticleFragment_imageArticle );
            ownCodeTextView = view.findViewById( R.id.analysis_article_OwnCode_editText );
            eanCodeTextView = view.findViewById( R.id.analysis_article_EAN_editText );

            competitorPriceEditText = view.findViewById( R.id.analysis_article_CompetitorPrice_editText );
            competitorPriceEditText.addTextChangedListener( new CompetitorPriceEditTextWatcher( competitorPriceEditText ) );
            competitorPriceEditText.setKeyListener( DigitsKeyListener.getInstance("0123456789"+decimalSeparator) );
            // TODO XXX competitorPriceEditText.setFilters(new InputFilter[]{new DecimalDigitsInputFilter()});

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

                private EditText watchedEditText;
                private int cursorPosition = 0;
                public CompetitorPriceEditTextWatcher( EditText watchedEditText ) {
                    this.watchedEditText = watchedEditText;
                }

                @Override
                public void beforeTextChanged (CharSequence charSequence, int start, int count, int after){
                    cursorPosition = watchedEditText.getSelectionStart();
                }

                @Override
                public void onTextChanged (CharSequence charSequence, int start, int before, int count){
                    charSequence = correctDecimalPlaces( charSequence );
                    Double priceFromInput = extractPriceFromInput( charSequence );
                    Double priceFromAnalysisArticleJoin = getPriceFromItem();
                    if (arePricesNotEqual( priceFromInput, priceFromAnalysisArticleJoin )) {
                        analysisArticleJoinViewModel.getValuesStateHolder().setCompetitorStorePrice(priceFromInput);
                        // TODO czy to niżej jest potrzebne do czegoś? No chyba tak, bo powyżej jest
                        //  pobranie analysisArticleJoin z adaptera i zmiana wartości ceny
                        // // TODO XXX  analysisArticleJoinViewModel.setAnalysisArticleJoin(analysisArticleJoin);
                    }
                }

                @NonNull
                public CharSequence correctDecimalPlaces( CharSequence charSequence ) {
                    if (charSequence.length()>0 ) {
                        String charSequenceStr = charSequence.toString().replace( decimalSeparator, '.');
                        if (areDecimalSeparatorsMoreThanOne(charSequenceStr)) {
                            charSequenceStr = makeMultipleDecimalSeparatorsCorrection( charSequenceStr );
                            makeCorrectionInEditText( charSequenceStr );
                        }
                        charSequence = decimalFormat.format( Double.parseDouble( charSequenceStr ) );
                        if (areDecimalPlacesMoreThanTwo( charSequence, charSequenceStr ) ) {
                            makeCorrectionInEditText( charSequence );
                        }
                    }
                    return charSequence;
                }

                private boolean areDecimalSeparatorsMoreThanOne(String inputText) {
                    // TODO XXX long lo = inputText.chars().filter(ch -> ch == '.' ).count();
                    return (inputText.chars().filter(ch -> ch == '.' ).count()) > 1;
                }

                private String makeMultipleDecimalSeparatorsCorrection( String inputText ) {
                    inputText = new StringBuffer(inputText).reverse().toString();
                    int decimalSeparatorPosition = inputText.indexOf( '.' );
                    inputText = inputText.replace( String.valueOf( '.'), "");
                    return new StringBuffer(inputText).insert( decimalSeparatorPosition, '.' ).reverse().toString();
                }

                private boolean areDecimalPlacesMoreThanTwo(CharSequence charSequence, String charSequenceStr) {
                    return charSequence.length() != charSequenceStr.length();
                }

                private void makeCorrectionInEditText(CharSequence charSequence) {
                    watchedEditText.removeTextChangedListener(this );
                    // TODO XXX int cursorPosition = watchedEditText.getSelectionStart();
                    if (isCursorPositionOutOfCharSequence(charSequence, cursorPosition)) {
                        cursorPosition = charSequence.length();
                    }
                    watchedEditText.setText( charSequence );
                    watchedEditText.setSelection(cursorPosition); // charSequence.length()
                    watchedEditText.addTextChangedListener(this );
                }

                private boolean isCursorPositionOutOfCharSequence(CharSequence charSequence, int cursorPosition) {
                    return cursorPosition > charSequence.length();
                }

                @Nullable
                private Double extractPriceFromInput(CharSequence charSequence) {
                    Double priceFromInput = null;
                    if (charSequence.length()>0) {
                        String charSequenceStr = charSequence.toString().replace( decimalSeparator, '.');
                        priceFromInput = Double.parseDouble( charSequenceStr );
                        if (priceFromInput.intValue()==0) {
                            priceFromInput = null;
                        }
                    }
                    return priceFromInput;
                }

                private Double getPriceFromItem() {
                    AnalysisArticleJoin analysisArticleJoin = getItem( getAbsoluteAdapterPosition() );
                    return analysisArticleJoin.getCompetitorStorePrice();
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
                public void afterTextChanged (Editable s) {
                    /*
                    String s1 = s.toString();
                    s1.length();
                    */
                }

            }

        class ArticleCommentEditTextWatcher implements TextWatcher  {
                @Override
                public void beforeTextChanged (CharSequence charSequence, int start, int count, int after){
                }

                @Override
                public void onTextChanged (CharSequence charSequence,int start, int before, int count){
                    // TODO !!! commentsFromInput to raczej z charSequence trzeba pobrać...
                    //  i w pozostłaych TextWatcherach też...
                    String commentsFromInput = charSequence.toString();
                    AnalysisArticleJoin analysisArticleJoin = getItem( getAbsoluteAdapterPosition() );
                    String commentsFromAnalysisArticleJoin = analysisArticleJoin.getComments();
                    if (areTextsNotEqual( commentsFromInput, commentsFromAnalysisArticleJoin )) {
                        analysisArticleJoinViewModel.getValuesStateHolder().setComments( commentsFromInput );
                        // TODO XXX analysisArticleJoin.setComments(commentsFromInput);
                        // TODO XXX  analysisArticleJoinViewModel.getChangeInformer().setFlagNeedToSave(true);
                        // TODO czy wiersz niżej jest potrzebne do czegoś?
                        // TODO XXX  analysisArticleJoinViewModel.setAnalysisArticleJoin(analysisArticleJoin);
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
                String nameFromInput = charSequence.toString();
                AnalysisArticleJoin analysisArticleJoin = getItem( getAbsoluteAdapterPosition() );
                String nameFromAnalysisArticleJoin = analysisArticleJoin.getReferenceArticleName();
                if (areTextsNotEqual( nameFromInput, nameFromAnalysisArticleJoin )) {
                    analysisArticleJoinViewModel.getValuesStateHolder().setReferenceArticleName( nameFromInput );
                    // TODO czy wiersz niżej jest potrzebne do czegoś?
                    // TODO XXX analysisArticleJoinViewModel.setAnalysisArticleJoin(analysisArticleJoin);
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
                String eanFromInput = charSequence.toString();
                AnalysisArticleJoin analysisArticleJoin = getItem( getAbsoluteAdapterPosition() );
                String eanFromAnalysisArticleJoin = analysisArticleJoin.getReferenceArticleEanCodeValue();
                if (areTextsNotEqual( eanFromInput, eanFromAnalysisArticleJoin )) {
                    analysisArticleJoinViewModel.getValuesStateHolder().setReferenceArticleEan( eanFromInput );
                    // TODO czy wiersz niżej jest potrzebne do czegoś?
                    // TODO XXX analysisArticleJoinViewModel.setAnalysisArticleJoin(analysisArticleJoin);
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
                String descriptionFromInput = charSequence.toString();
                AnalysisArticleJoin analysisArticleJoin = getItem( getAbsoluteAdapterPosition() );
                String descriptionFromAnalysisArticleJoin = analysisArticleJoin.getReferenceArticleDescription();
                if (areTextsNotEqual( descriptionFromInput, descriptionFromAnalysisArticleJoin )) {
                    analysisArticleJoinViewModel.getValuesStateHolder().setReferenceArticleDescription( descriptionFromInput );
                    // TODO czy wiersz niżej jest potrzebne do czegoś?
                    // TODO XXX analysisArticleJoinViewModel.setAnalysisArticleJoin(analysisArticleJoin);
                }
            }

            @Override
            public void afterTextChanged (Editable s){
            }
        }

        private boolean areTextsNotEqual(String text1, String text2 ) {
            if (isStringEmptyOrNull( text1 )) {
                return isStringNotEmptyOrNotNull( text2 );
            }
            return !(text1.equals(text2));
        }

        private boolean isStringEmptyOrNull(String string ) {
            return (string==null) || (string.isEmpty());
        }

        private boolean isStringNotEmptyOrNotNull(String string ) {
            return !isStringEmptyOrNull(string);
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
                String formattedString = decimalFormat.format( competitorStorePrice );
                /* TODO XXX formattedString = competitorStorePrice.toString(); */
                // TODO XXX formattedString.replace(',', '.');
                competitorPriceEditText.setText( formattedString );
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

    // TODO ???
    public class DecimalDigitsInputFilter implements InputFilter {

        Pattern mPattern;

        public DecimalDigitsInputFilter() {
            mPattern = Pattern.compile("[0-9]*+((\\.[0-9]?)?)||(\\.)?");
        }

        @Override
        public CharSequence filter(
                CharSequence source,
                int start,
                int end,
                Spanned dest,
                int dstart,
                int dend ) {
            Matcher matcher = mPattern.matcher(dest);
            if (!matcher.matches())
                return "";
            return null;
        }
    }

}

