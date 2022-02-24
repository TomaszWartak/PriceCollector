package com.dev4lazy.pricecollector.view.E5_article_screen;

import android.content.Context;
import android.content.res.Configuration;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev4lazy.pricecollector.AppHandle;
import com.dev4lazy.pricecollector.R;
import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;
import com.dev4lazy.pricecollector.view.E4_analysis_articles_list_screen.AnalysisArticleJoinDiffCallback;
import com.dev4lazy.pricecollector.view.utils.PopupWindowWrapper;
import com.dev4lazy.pricecollector.viewmodel.AnalysisArticleJoinViewModel;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

public class AnalysisArticleJoinPagerAdapter // OK
        extends
            PagedListAdapter<AnalysisArticleJoin,
            AnalysisArticleJoinPagerAdapter.AnalysisArticleJoinPagerViewHolder> {

    private AnalysisArticleJoinViewModel analysisArticleJoinViewModel;
    private AnalysisArticleJoinPagerViewHolder holder;
    int statusbarHeight;
    private int toolbarHeight;

    public AnalysisArticleJoinPagerAdapter(
            ViewPager2 analysisArticlesViewPager,
            AnalysisArticleJoinDiffCallback analysisArticleJoinDiffCallback,
            AnalysisArticleJoinViewModel analysisArticleJoinViewModel,
            int statusbarHeight,
            int toolbarHeight
        ) {
        super(analysisArticleJoinDiffCallback);
        this.analysisArticleJoinViewModel = analysisArticleJoinViewModel;
        this.statusbarHeight = statusbarHeight;
        this.toolbarHeight = toolbarHeight;
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

    public void clearCompetitorArticleDataOnScreen() {
        holder.clearCompetitorArticleDataOnScreen();
    }

    class AnalysisArticleJoinPagerViewHolder extends RecyclerView.ViewHolder {

        private char decimalSeparator;
        private DecimalFormat decimalFormat;
        private ImageView ownArticleImageView;
        private TextView ownCodeTextView;
        private TextView eanCodeTextView;
        private EditText competitorPriceEditText;
        private EditText articleCommentEditText;
        // Ref Article
        // todo ok: private ImageView refArticleImageView;
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
            ownArticleImageView = view.findViewById( R.id.analysisArticleFragment_imageArticle );
            ownCodeTextView = view.findViewById( R.id.analysis_article_OwnCode_editText );
            eanCodeTextView = view.findViewById( R.id.analysis_article_EAN_editText );

            competitorPriceEditText = view.findViewById( R.id.analysis_article_CompetitorPrice_editText );
            competitorPriceEditText.addTextChangedListener( new CompetitorPriceEditTextWatcher( competitorPriceEditText ) );
            competitorPriceEditText.setKeyListener( DigitsKeyListener.getInstance("0123456789"+decimalSeparator) );

            articleCommentEditText = view.findViewById( R.id.analysis_article_ArticleComment_editText );
            articleCommentEditText.addTextChangedListener( new ArticleCommentEditTextWatcher() );

            // Ref Article
            // todo ok: view.findViewById( R.id.analysisArticleFragment_imageRefArticle );
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
                private boolean watchedEditTextWasEmpty = true;

                public CompetitorPriceEditTextWatcher( EditText watchedEditText ) {
                    this.watchedEditText = watchedEditText;
                }

                @Override
                public void beforeTextChanged (CharSequence charSequence, int start, int count, int after){
                    cursorPosition = watchedEditText.getSelectionStart();
                    watchedEditTextWasEmpty = watchedEditText.getText().toString().isEmpty();
                }

                @Override
                public void onTextChanged (CharSequence charSequence, int start, int before, int count){
                    charSequence = correctDecimalPlaces( charSequence );
                    Double priceFromInput = extractPriceFromInput( charSequence );
                    Double priceFromAnalysisArticleJoin = getPriceFromItem();
                    if (arePricesNotEqual( priceFromInput, priceFromAnalysisArticleJoin )) {
                        analysisArticleJoinViewModel.getValuesStateHolder().setCompetitorStorePrice(priceFromInput);
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
                        if (areDecimalPlacesDiffThanTwo( charSequence, charSequenceStr ) ) {
                            makeCorrectionInEditText( charSequence );
                        }
                    }
                    return charSequence;
                }

                private boolean areDecimalSeparatorsMoreThanOne(String inputText) {
                    return (inputText.chars().filter(ch -> ch == '.' ).count()) > 1;
                }

                private String makeMultipleDecimalSeparatorsCorrection( String inputText ) {
                    inputText = new StringBuffer(inputText).reverse().toString();
                    int decimalSeparatorPosition = inputText.indexOf( '.' );
                    inputText = inputText.replace( String.valueOf( '.'), "");
                    return new StringBuffer(inputText).insert( decimalSeparatorPosition, '.' ).reverse().toString();
                }

                private boolean areDecimalPlacesDiffThanTwo(CharSequence charSequence, String charSequenceStr) {
                    return charSequence.length() != charSequenceStr.length();
                }

                private void makeCorrectionInEditText(CharSequence charSequence) {
                    watchedEditText.removeTextChangedListener(this );
                    if (isCursorPositionOutOfCharSequence( charSequence, cursorPosition)) {
                        cursorPosition = charSequence.length();
                    }
                    if (watchedEditTextWasEmpty) {
                        cursorPosition = 1;
                    }
                    watchedEditText.setText( charSequence );
                    watchedEditText.setSelection( cursorPosition ); // charSequence.length()
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
                    String commentsFromInput = charSequence.toString();
                    AnalysisArticleJoin analysisArticleJoin = getItem( getAbsoluteAdapterPosition() );
                    String commentsFromAnalysisArticleJoin = analysisArticleJoin.getComments();
                    if (areTextsNotEqual( commentsFromInput, commentsFromAnalysisArticleJoin )) {
                        analysisArticleJoinViewModel.getValuesStateHolder().setComments( commentsFromInput );
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
                    analysisArticleJoinViewModel.getValuesStateHolder().setReferenceArticleEanCodeValue( eanFromInput );
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
            int ownArticleImageResId = getOwnArticleImageResId( analysisArticleJoin);
            if (ownArticleImageResId!=0) {
                ownArticleImageView.setImageResource( ownArticleImageResId );
                ownArticleImageView.setOnClickListener(
                        new OwnArticleImageViewOnClickListener( analysisArticleJoin )
                );
            } else {
                ownArticleImageView.setImageResource( R.drawable.common_google_signin_btn_icon_dark );
                ownArticleImageView.setOnClickListener( null );
            }
            ownCodeTextView.setText( analysisArticleJoin.getOwnCode() );
            eanCodeTextView.setText( analysisArticleJoin.getEanCode() );
            Double competitorStorePrice = analysisArticleJoin.getCompetitorStorePrice();
            if (competitorStorePrice==null) {
                competitorPriceEditText.setText( null );
            } else {
                String formattedString = decimalFormat.format( competitorStorePrice );
                competitorPriceEditText.setText( formattedString );
            }
            articleCommentEditText.setText( analysisArticleJoin.getComments() );
            // Ref Article
            // todo ok: view.findViewById( R.id.analysisArticleFragment_imageRefArticle );
            referenceArticleNameEditText.setText( analysisArticleJoin.getReferenceArticleName() );
            referenceArticleEANEditText.setText( analysisArticleJoin.getReferenceArticleEanCodeValue() );
            referenceArticleDescriptionEditText.setText( analysisArticleJoin.getReferenceArticleDescription() );
        }


            private int getOwnArticleImageResId( AnalysisArticleJoin analysisArticleJoin ) {
                Context applicationContext = AppHandle.getHandle();
                int resId = applicationContext.getResources().getIdentifier(
                        getOwnArticleImageName( analysisArticleJoin ),
                        "drawable",
                        applicationContext.getPackageName()
                );
                return resId;
            }

            private String getOwnArticleImageName( AnalysisArticleJoin analysisArticleJoin ) {
                return "i"+analysisArticleJoin.getOwnCode();
            }

            private class OwnArticleImageViewOnClickListener implements View.OnClickListener {
                private AnalysisArticleJoin analysisArticleJoin;
                private PopupWindowWrapper populatingDataPopupWindowWrapper;

                public OwnArticleImageViewOnClickListener( AnalysisArticleJoin analysisArticleJoin ) {
                    super();
                    this.analysisArticleJoin = analysisArticleJoin;;
                }

                @Override
                public void onClick(View articleImageView) {
                    Context context = articleImageView.getContext();
                    int screenOrientation = context.getResources().getConfiguration().orientation;
                    int width;
                    if (screenOrientation== Configuration.ORIENTATION_PORTRAIT) {
                        width = ViewGroup.LayoutParams.MATCH_PARENT;
                    } else {
                        width = ViewGroup.LayoutParams.WRAP_CONTENT;
                    }
                    populatingDataPopupWindowWrapper =
                            new PopupWindowWrapper(
                                    articleImageView,
                                    R.layout.article_image_greater_popup_window ).
                                    setWidth( width ).
                                    setHeight( ViewGroup.LayoutParams.WRAP_CONTENT ).
                                    setGravity( Gravity.TOP ).
                                    setOutsideTouchable( false ).
                                    setFocusable( true );

                    ImageView articleGreaterImageView = getArticleGreaterImageView();
                    articleGreaterImageView.setImageDrawable( ((ImageView)articleImageView).getDrawable() );
                    articleGreaterImageView.getLayoutParams().width =  ViewGroup.LayoutParams.MATCH_PARENT;
                    articleGreaterImageView.setOnClickListener( new OwnArticleGreaterImageViewOnClickListener() );

                    TextView articleEANTextView = getArticleEANTextView();
                    articleEANTextView.setText( analysisArticleJoin.getEanCode() );

                    int marginTop = itemView.getContext().getResources().getDimensionPixelSize( R.dimen.margin );
                    populatingDataPopupWindowWrapper.show(0 , statusbarHeight+toolbarHeight+marginTop );
                }

                private ImageView getArticleGreaterImageView() {
                    return populatingDataPopupWindowWrapper.getPopupView().findViewById(R.id.articleImageGreater_imageArticle);
                }

                private TextView getArticleEANTextView() {
                    return populatingDataPopupWindowWrapper.getPopupView().findViewById(R.id.articleImageGreater_EANArticle);
                }

                class OwnArticleGreaterImageViewOnClickListener implements View.OnClickListener {
                    @Override
                    public void onClick(View v) {
                        populatingDataPopupWindowWrapper.close();
                    }
                }

            }

        protected void clear() {
            // Own Article
            // ownArticleImageView. TODO ???
            ownCodeTextView.setText( null );
            eanCodeTextView.setText( null );
            competitorPriceEditText.setText( null );
            articleCommentEditText.setText( null );
            // Ref Article
            // todo ok: view.findViewById( R.id.analysisArticleFragment_imageRefArticle );
            referenceArticleNameEditText.setText( null );
            referenceArticleEANEditText.setText( null );
            referenceArticleDescriptionEditText.setText( null );
        }

        protected void clearCompetitorArticleDataOnScreen() {
            // Own Article
            // todo ok: view.findViewById( R.id.analysisArticleFragment_imageArticle );
            competitorPriceEditText.setText( "" );
            articleCommentEditText.setText( "" );
            // Ref Article
            // todo ok: view.findViewById( R.id.analysisArticleFragment_imageRefArticle );
            referenceArticleNameEditText.setText( "" );
            referenceArticleEANEditText.setText( "" );
            referenceArticleDescriptionEditText.setText( "" );
            notifyDataSetChanged();
        }

    }

}

