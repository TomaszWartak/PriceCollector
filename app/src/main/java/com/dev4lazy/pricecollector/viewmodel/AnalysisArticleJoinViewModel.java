package com.dev4lazy.pricecollector.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;

public class AnalysisArticleJoinViewModel extends AndroidViewModel {

    private AnalysisArticleJoin analysisArticleJoin;
    private AnalysisArticleJoinValuesStateHolder valuesStateHolder;
    private AnalysisArticleJoin analysisArticleJoinForRestore;
    private boolean toRestoreAfterEanValueDupliaction;
    private int positionOnList;
    private boolean savingDataInProgress; // todo - czy to jest wykorzystywane

    public AnalysisArticleJoinViewModel(Application application) {
        super(application);
        valuesStateHolder = new AnalysisArticleJoinValuesStateHolder();
        analysisArticleJoinForRestore = new AnalysisArticleJoin();
        toRestoreAfterEanValueDupliaction = false;
        savingDataInProgress = false;
    }

    public void setAnalysisArticleJoin( AnalysisArticleJoin analysisArticleJoin ) {
        this.analysisArticleJoin = analysisArticleJoin;
    }

    public AnalysisArticleJoin getAnalysisArticleJoin() {
        return analysisArticleJoin;
    }

    public void setValuesStateHolder( AnalysisArticleJoinValuesStateHolder aAJVSH ) {
        this.valuesStateHolder = aAJVSH;
    }

    public AnalysisArticleJoinValuesStateHolder getValuesStateHolder() {
        return valuesStateHolder;
    }

    /**/
    public AnalysisArticleJoinValuesStateHolder getCopyOfValuesStateHolder() {
        AnalysisArticleJoinValuesStateHolder copyOfAnalysisArticleJoinValuesStateHolder =
                new AnalysisArticleJoinValuesStateHolder();
        copyOfAnalysisArticleJoinValuesStateHolder.setFlagNeedToSave(
                valuesStateHolder.isNeedToSaveFlagSet()
        );
        copyOfAnalysisArticleJoinValuesStateHolder.setFlagPriceChanged(
                valuesStateHolder.isPriceChangedFlagSet()
        );
        copyOfAnalysisArticleJoinValuesStateHolder.setFlagCommentsChanged(
                valuesStateHolder.isCommentsChangedFlagSet()
        );
        copyOfAnalysisArticleJoinValuesStateHolder.setFlagReferenceArticleChanged(
                valuesStateHolder. isReferenceArticleChangedFlagSet()
        );
        copyOfAnalysisArticleJoinValuesStateHolder.setFlagReferenceArticleEanChanged(
                valuesStateHolder.isReferenceArticleEanChangedFlagSet()
        );
        return copyOfAnalysisArticleJoinValuesStateHolder;
    }
    /**/

    public void setAnalysisArticleJoinForRestore(AnalysisArticleJoin analysisArticleJoin) {
        /* Ze względu na wydajność zostawiłem kopiowanie tylko niezbędnych danych.
           "Niezbędnych" = tych, które podlegają edycji.
        analysisArticleJoinForRestore.setAnalysisArticleId( analysisArticleJoin.getAnalysisArticleId() );
        analysisArticleJoinForRestore.setAnalysisId( analysisArticleJoin.getAnalysisId() );
        analysisArticleJoinForRestore.setArticleId( analysisArticleJoin.getArticleId() );
        analysisArticleJoinForRestore.setOwnArticleInfoId( analysisArticleJoin.getOwnArticleInfoId() );
        analysisArticleJoinForRestore.setArticleStorePrice( analysisArticleJoin.getArticleStorePrice() );
        analysisArticleJoinForRestore.setArticleRefPrice( analysisArticleJoin.getArticleRefPrice() );
        analysisArticleJoinForRestore.setArticleNewPrice( analysisArticleJoin.getArticleNewPrice() );
        analysisArticleJoinForRestore.setCompetitorStoreId( analysisArticleJoin.getCompetitorStoreId() );
        analysisArticleJoinForRestore.setCompetitorStorePriceId( analysisArticleJoin.getCompetitorStorePriceId() );
         */
        analysisArticleJoinForRestore.setCompetitorStorePrice( analysisArticleJoin.getCompetitorStorePrice() );
        /*
        analysisArticleJoinForRestore.setReferenceArticleId( analysisArticleJoin.getReferenceArticleId() );
        */
        analysisArticleJoinForRestore.setComments( analysisArticleJoin.getComments() );
        /*
        analysisArticleJoinForRestore.setArticleName( analysisArticleJoin.getArticleName() );
        analysisArticleJoinForRestore.setOwnCode( analysisArticleJoin.getOwnCode() );
        analysisArticleJoinForRestore.setEanCode( analysisArticleJoin.getEanCode() );
        */
        analysisArticleJoinForRestore.setReferenceArticleName( analysisArticleJoin.getReferenceArticleName() );
        /*
        analysisArticleJoinForRestore.setReferenceArticleEanCodeId( analysisArticleJoin.getReferenceArticleEanCodeId() );
        */
        analysisArticleJoinForRestore.setReferenceArticleEanCodeValue( analysisArticleJoin.getReferenceArticleEanCodeValue() );
        analysisArticleJoinForRestore.setReferenceArticleDescription( analysisArticleJoin.getReferenceArticleDescription() );
    }

    public void setAnalysisArticleJoinForRestore2(AnalysisArticleJoin analysisArticleJoin) {
        Double dummyDouble;
        /* Ze względu na wydajność zostawiłem kopiowanie tylko niezbędnych danych.
           "Niezbędnych" = tych, które podlegają edycji.
        analysisArticleJoinForRestore.setAnalysisArticleId( analysisArticleJoin.getAnalysisArticleId() );
        analysisArticleJoinForRestore.setAnalysisId( analysisArticleJoin.getAnalysisId() );
        analysisArticleJoinForRestore.setArticleId( analysisArticleJoin.getArticleId() );
        analysisArticleJoinForRestore.setOwnArticleInfoId( analysisArticleJoin.getOwnArticleInfoId() );
        Double dummyDouble = analysisArticleJoin.getArticleStorePrice();
        if (dummyDouble ==null) {
            analysisArticleJoinForRestore.setArticleStorePrice( null );
        } else {
            analysisArticleJoinForRestore.setArticleStorePrice( dummyDouble.doubleValue() );
        }
        dummyDouble = analysisArticleJoin.getArticleRefPrice();
        if (dummyDouble ==null) {
            analysisArticleJoinForRestore.setArticleRefPrice( null );
        } else {
            analysisArticleJoinForRestore.setArticleRefPrice( dummyDouble.doubleValue() );
        }
        dummyDouble = analysisArticleJoin.getArticleNewPrice();
        if (dummyDouble ==null) {
            analysisArticleJoinForRestore.setArticleNewPrice( null );
        } else {
            analysisArticleJoinForRestore.setArticleNewPrice( dummyDouble.doubleValue() );
        }
        Integer dummyInteger = analysisArticleJoin.getCompetitorStoreId();
        if (dummyInteger==null) {
            analysisArticleJoinForRestore.setCompetitorStoreId( null );
        } else {
            analysisArticleJoinForRestore.setCompetitorStoreId( new Integer( dummyInteger ) );
        }
        dummyInteger = analysisArticleJoin.getCompetitorStorePriceId();
        if (dummyInteger==null) {
            analysisArticleJoinForRestore.setCompetitorStorePriceId( null );
        } else {
            analysisArticleJoinForRestore.setCompetitorStorePriceId( new Integer( dummyInteger ) );
        }
         */
        dummyDouble = analysisArticleJoin.getCompetitorStorePrice();
        if (dummyDouble ==null) {
            analysisArticleJoinForRestore.setCompetitorStorePrice( null );
        } else {
            analysisArticleJoinForRestore.setCompetitorStorePrice( dummyDouble.doubleValue() );
        }
        /*
        dummyInteger = analysisArticleJoin.getReferenceArticleId();
        if (dummyInteger==null) {
            analysisArticleJoinForRestore.setReferenceArticleId( null );
        } else {
            analysisArticleJoinForRestore.setReferenceArticleId( new Integer( dummyInteger ) );
        }
        */
        analysisArticleJoinForRestore.setComments( analysisArticleJoin.getComments() );
        /*
        analysisArticleJoinForRestore.setArticleName( analysisArticleJoin.getArticleName() );
        analysisArticleJoinForRestore.setOwnCode( analysisArticleJoin.getOwnCode() );
        analysisArticleJoinForRestore.setEanCode( analysisArticleJoin.getEanCode() );
        */
        analysisArticleJoinForRestore.setReferenceArticleName( analysisArticleJoin.getReferenceArticleName() );
        /*
        dummyInteger = analysisArticleJoin.getReferenceArticleEanCodeId();
        if (dummyInteger==null) {
            analysisArticleJoinForRestore.setReferenceArticleEanCodeId( null );
        } else {
            analysisArticleJoinForRestore.setReferenceArticleEanCodeId( new Integer( dummyInteger ) );
        }
        */
        analysisArticleJoinForRestore.setReferenceArticleEanCodeValue( analysisArticleJoin.getReferenceArticleEanCodeValue() );
        analysisArticleJoinForRestore.setReferenceArticleDescription( analysisArticleJoin.getReferenceArticleDescription() );
    }

    public void restoreReferenceArticleEanCodeValue() {
        analysisArticleJoin.setReferenceArticleEanCodeValue( analysisArticleJoinForRestore.getReferenceArticleEanCodeValue() );
    }

    public void restoreAnalysisArticleJoin() {
        /* Ze względu na wydajność zostawiłem kopiowanie tylko niezbędnych danych.
           "Niezbędnych" = tych, które podlegają edycji.
        analysisArticleJoin.setAnalysisArticleId( analysisArticleJoinForRestore.getAnalysisArticleId() );
        analysisArticleJoin.setAnalysisId( analysisArticleJoinForRestore.getAnalysisId() );
        analysisArticleJoin.setArticleId( analysisArticleJoinForRestore.getArticleId() );
        analysisArticleJoin.setOwnArticleInfoId( analysisArticleJoinForRestore.getOwnArticleInfoId() );
        analysisArticleJoin.setArticleStorePrice( analysisArticleJoinForRestore.getArticleStorePrice() );
        analysisArticleJoin.setArticleRefPrice( analysisArticleJoinForRestore.getArticleRefPrice() );
        analysisArticleJoin.setArticleNewPrice( analysisArticleJoinForRestore.getArticleNewPrice() );
        analysisArticleJoin.setCompetitorStoreId( analysisArticleJoinForRestore.getCompetitorStoreId() );
        analysisArticleJoin.setCompetitorStorePriceId( analysisArticleJoinForRestore.getCompetitorStorePriceId() );
         */
        if ( valuesStateHolder.isPriceChangedFlagSet() ) {
            analysisArticleJoin.setCompetitorStorePrice( analysisArticleJoinForRestore.getCompetitorStorePrice() );
        }
        /*
        analysisArticleJoin.setReferenceArticleId( analysisArticleJoinForRestore.getReferenceArticleId() );
        */
        if ( valuesStateHolder.isCommentsChangedFlagSet() ) {
            analysisArticleJoin.setComments( analysisArticleJoinForRestore.getComments() );
        }
        /*
        analysisArticleJoin.setArticleName( analysisArticleJoinForRestore.getArticleName() );
        analysisArticleJoin.setOwnCode( analysisArticleJoinForRestore.getOwnCode() );
        analysisArticleJoin.setEanCode( analysisArticleJoinForRestore.getEanCode() );
        */
        if ( valuesStateHolder.isReferenceArticleNameChangedFlagSet() ) {
            analysisArticleJoin.setReferenceArticleName( analysisArticleJoinForRestore.getReferenceArticleName() );
        }
        /*
        analysisArticleJoin.setReferenceArticleEanCodeId( analysisArticleJoinForRestore.getReferenceArticleEanCodeId() );
        */
        if ( valuesStateHolder.isReferenceArticleEanChangedFlagSet() ) {
            analysisArticleJoin.setReferenceArticleEanCodeValue( analysisArticleJoinForRestore.getReferenceArticleEanCodeValue() );
        }
        if ( valuesStateHolder.isReferenceArticleDescriptionChangedFlagSet() ) {
            analysisArticleJoin.setReferenceArticleDescription( analysisArticleJoinForRestore.getReferenceArticleDescription() );
        }
    }

    public void restoreAnalysisArticleJoin2() {
        setToRestoreAfterEanValueDupliaction(false);
        Double dummyDouble;
        /* Ze względu na wydajność zostawiłem kopiowanie tylko niezbędnych danych.
           "Niezbędnych" = tych, które podlegają edycji.
        analysisArticleJoin.setAnalysisArticleId( analysisArticleJoinForRestore.getAnalysisArticleId() );
        analysisArticleJoin.setAnalysisId( analysisArticleJoinForRestore.getAnalysisId() );
        analysisArticleJoin.setArticleId( analysisArticleJoinForRestore.getArticleId() );
        analysisArticleJoin.setOwnArticleInfoId( analysisArticleJoinForRestore.getOwnArticleInfoId() );
        Double dummyDouble = analysisArticleJoinForRestore.getArticleStorePrice();
        if (dummyDouble ==null) {
            analysisArticleJoin.setArticleStorePrice( null );
        } else {
            analysisArticleJoin.setArticleStorePrice( dummyDouble.doubleValue() );
        }
        dummyDouble = analysisArticleJoinForRestore.getArticleRefPrice();
        if (dummyDouble ==null) {
            analysisArticleJoin.setArticleRefPrice( null );
        } else {
            analysisArticleJoin.setArticleRefPrice( dummyDouble.doubleValue() );
        }
        dummyDouble = analysisArticleJoinForRestore.getArticleNewPrice();
        if (dummyDouble ==null) {
            analysisArticleJoin.setArticleNewPrice( null );
        } else {
            analysisArticleJoin.setArticleNewPrice( dummyDouble.doubleValue() );
        }
        Integer dummyInteger = analysisArticleJoinForRestore.getCompetitorStoreId();
        if (dummyInteger==null) {
            analysisArticleJoin.setCompetitorStoreId( null );
        } else {
            analysisArticleJoin.setCompetitorStoreId( new Integer( dummyInteger ) );
        }
        dummyInteger = analysisArticleJoinForRestore.getCompetitorStorePriceId();
        if (dummyInteger==null) {
            analysisArticleJoin.setCompetitorStorePriceId( null );
        } else {
            analysisArticleJoin.setCompetitorStorePriceId( new Integer( dummyInteger ) );
        }
         */
        dummyDouble = analysisArticleJoinForRestore.getCompetitorStorePrice();
        if (dummyDouble ==null) {
            analysisArticleJoin.setCompetitorStorePrice( null );
        } else {
            analysisArticleJoin.setCompetitorStorePrice( dummyDouble.doubleValue() );
        }
        /*
        dummyInteger = analysisArticleJoinForRestore.getReferenceArticleId();
        if (dummyInteger==null) {
            analysisArticleJoin.setReferenceArticleId( null );
        } else {
            analysisArticleJoin.setReferenceArticleId( new Integer( dummyInteger ) );
        }
        */
        analysisArticleJoin.setComments( analysisArticleJoinForRestore.getComments() );
        /*
        analysisArticleJoin.setArticleName( analysisArticleJoinForRestore.getArticleName() );
        analysisArticleJoin.setOwnCode( analysisArticleJoinForRestore.getOwnCode() );
        analysisArticleJoin.setEanCode( analysisArticleJoinForRestore.getEanCode() );
        */
        analysisArticleJoin.setReferenceArticleName( analysisArticleJoinForRestore.getReferenceArticleName() );
        /*
        dummyInteger = analysisArticleJoinForRestore.getReferenceArticleEanCodeId();
        if (dummyInteger==null) {
            analysisArticleJoin.setReferenceArticleEanCodeId( null );
        } else {
            analysisArticleJoin.setReferenceArticleEanCodeId( new Integer( dummyInteger ) );
        }
        */
        analysisArticleJoin.setReferenceArticleEanCodeValue( analysisArticleJoinForRestore.getReferenceArticleEanCodeValue() );
        analysisArticleJoin.setReferenceArticleDescription( analysisArticleJoinForRestore.getReferenceArticleDescription() );
    }

    public AnalysisArticleJoin getAnalysisArticleJoinForRestore() {
        return analysisArticleJoinForRestore;
    }

    public AnalysisArticleJoin getCopyOfAnalysisArticleJoinForRestore() {
        return getCopyOfAnalysisArticleJoin( analysisArticleJoinForRestore );
    }
    public AnalysisArticleJoin getCopyOfAnalysisArticleJoin( AnalysisArticleJoin analysisArticleJoin ) {
        AnalysisArticleJoin copyOfAnalysisArticleJoin = new AnalysisArticleJoin();
        /* Ze względu na wydajność zostawiłem kopiowanie tylko niezbędnych danych.
           "Niezbędnych" = tych, które podlegają edycji.
        copyOfAnalysisArticleJoin.setAnalysisArticleId( analysisArticleJoin.getAnalysisArticleId() );
        copyOfAnalysisArticleJoin.setAnalysisId( analysisArticleJoin.getAnalysisId() );
        copyOfAnalysisArticleJoin.setArticleId( analysisArticleJoin.getArticleId() );
        copyOfAnalysisArticleJoin.setOwnArticleInfoId( analysisArticleJoin.getOwnArticleInfoId() );
        copyOfAnalysisArticleJoin.setArticleStorePrice( analysisArticleJoin.getArticleStorePrice() );
        copyOfAnalysisArticleJoin.setArticleRefPrice( analysisArticleJoin.getArticleRefPrice() );
        copyOfAnalysisArticleJoin.setArticleNewPrice( analysisArticleJoin.getArticleNewPrice() );
        copyOfAnalysisArticleJoin.setCompetitorStoreId( analysisArticleJoin.getCompetitorStoreId() );
        copyOfAnalysisArticleJoin.setCompetitorStorePriceId( analysisArticleJoin.getCompetitorStorePriceId() );
         */
        copyOfAnalysisArticleJoin.setCompetitorStorePrice( analysisArticleJoin.getCompetitorStorePrice() );
        /*
        copyOfAnalysisArticleJoin.setReferenceArticleId( analysisArticleJoin.getReferenceArticleId() );
        */
        copyOfAnalysisArticleJoin.setComments( analysisArticleJoin.getComments() );
        /*
        copyOfAnalysisArticleJoin.setArticleName( analysisArticleJoin.getArticleName() );
        copyOfAnalysisArticleJoin.setOwnCode( analysisArticleJoin.getOwnCode() );
        copyOfAnalysisArticleJoin.setEanCode( analysisArticleJoin.getEanCode() );
        */
        copyOfAnalysisArticleJoin.setReferenceArticleName( analysisArticleJoin.getReferenceArticleName() );
        /*
        copyOfAnalysisArticleJoin.setReferenceArticleEanCodeId( analysisArticleJoin.getReferenceArticleEanCodeId() );
        */
        copyOfAnalysisArticleJoin.setReferenceArticleEanCodeValue( analysisArticleJoin.getReferenceArticleEanCodeValue() );
        copyOfAnalysisArticleJoin.setReferenceArticleDescription( analysisArticleJoin.getReferenceArticleDescription() );

        return copyOfAnalysisArticleJoin;
    }

    public boolean isToRestoreAfterEanValueDupliaction() {
        return toRestoreAfterEanValueDupliaction;
    }

    public boolean isNotToRestoreAfterEanValueDupliaction() {
        return !isToRestoreAfterEanValueDupliaction();
    }

    public void setToRestoreAfterEanValueDupliaction(boolean toRestoreAfterEanValueDupliaction) {
        this.toRestoreAfterEanValueDupliaction = toRestoreAfterEanValueDupliaction;
    }

    public boolean isSavingDataInProgress() {
        return savingDataInProgress;
    }

    public void setSavingDataInProgress(boolean savingDataInProgress) {
        this.savingDataInProgress = savingDataInProgress;
    }

    public void setPositionOnList(int positionOnList) {
        this.positionOnList = positionOnList;
    }

    public int getPositionOnList() {
        return positionOnList;
    }


    public boolean isAnalysisArticleJoinNotModified() {
        return valuesStateHolder.isNotAnyValueChanged();
    }

    public void clearNeedToSave( ) {
        valuesStateHolder.setFlagNeedToSave( false );
    }

    public boolean isNeedToSave() {
        return valuesStateHolder.isNeedToSaveFlagSet();
    }

    public class AnalysisArticleJoinValuesStateHolder {

        private boolean needToSave;
        private boolean priceChanged;
        private boolean commentsChanged;
        private boolean referenceArticleChanged;
        private boolean referenceArticleNameChanged;
        private boolean referenceArticleEanChanged;
        private boolean referenceArticleDescriptionChanged;

        AnalysisArticleJoinValuesStateHolder() {
            clearFlags();
        }

        private void clearFlags() {
            needToSave = false;
            priceChanged = false;
            commentsChanged = false;
            referenceArticleChanged = false;
            referenceArticleNameChanged = false;
            referenceArticleEanChanged = false;
            referenceArticleDescriptionChanged = false;
        };

        private void setFlagNeedToSave(boolean valueToSet ) {
            needToSave = valueToSet;
        }

        private boolean isNeedToSaveFlagSet() {
            return needToSave;
        }

        private boolean isNeedToSaveFlagNotSet() {
            return !needToSave;
        }

        public void clearFlagPriceChanged() {
            setFlagPriceChanged( false );
        }

        public void setFlagPriceChanged( boolean valueToSet ) {
            priceChanged = valueToSet;
        }

        public boolean isPriceChangedFlagSet() {
            return priceChanged;
        }

        public boolean isPriceChangedFlagNotSet() {
            return !priceChanged;
        }

        public void setCompetitorStorePrice( Double price ) {
            analysisArticleJoin.setCompetitorStorePrice( price );
            setFlagPriceChanged(true);
            if (isNeedToSaveFlagNotSet()) {
                setFlagNeedToSave(true);
            }
        }

        public void clearFlagCommentsChanged() {
            setFlagCommentsChanged( false );
        }

        public void setFlagCommentsChanged(boolean valueToSet ) {
            commentsChanged = valueToSet;
        }

        public boolean isCommentsChangedFlagSet() {
            return commentsChanged;
        }

        public boolean isCommentsChangedFlagNotSet() {
            return !commentsChanged;
        }

        public void setComments( String comments ) {
            analysisArticleJoin.setComments( comments );
            setFlagCommentsChanged(true);
            if (isNeedToSaveFlagNotSet()) {
                setFlagNeedToSave(true);
            }
        }

        public void clearFlagReferenceArticleChanged() {
            setFlagReferenceArticleChanged( false );
        }

        private void setFlagReferenceArticleChanged( boolean valueToSet ) {
            referenceArticleChanged = valueToSet;
        }

        public boolean isReferenceArticleChangedFlagSet() {
            return referenceArticleChanged;
        }

        public boolean isReferenceArticleChangedFlagNotSet() {
            return !referenceArticleChanged;
        }

        public void clearFlagReferenceArticleNameChanged() {
            setFlagReferenceArticleNameChanged( false );
        }

        public void setFlagReferenceArticleNameChanged( boolean valueToSet ) {
            referenceArticleNameChanged = valueToSet;
        }

        public boolean isReferenceArticleNameChangedFlagSet() {
            return referenceArticleNameChanged;
        }

        public boolean isReferenceArticleNameChangedFlagNotSet() {
            return !referenceArticleNameChanged;
        }

        public void setReferenceArticleName( String name ) {
            analysisArticleJoin.setReferenceArticleName( name );
            setFlagReferenceArticleChanged( true );
            setFlagReferenceArticleNameChanged( true );
            if (isNeedToSaveFlagNotSet()) {
                setFlagNeedToSave( true );
            }
        }

        public void setReferenceArticleEan( String ean ) {
            analysisArticleJoin.setReferenceArticleEanCodeValue( ean );
            // TODO XXX setFlagReferenceArticleChanged(true);
            setFlagReferenceArticleEanChanged(true);
            if (isNeedToSaveFlagNotSet()) {
                setFlagNeedToSave(true);
            }
        }

        public void clearFlagReferenceArticleEanChanged() {
            setFlagReferenceArticleEanChanged( false );
        }

        public void setFlagReferenceArticleEanChanged( boolean valueToSet ) {
            referenceArticleEanChanged = valueToSet;
        }


        public boolean isReferenceArticleEanChangedFlagSet() {
            return referenceArticleEanChanged;
        }

        public boolean isReferenceArticleEanChangedFlagNotSet() {
            return !referenceArticleEanChanged;
        }

        public void clearFlagReferenceArticleDescriptionChanged() {
            setFlagReferenceArticleDescriptionChanged( false );
        }

        public void setFlagReferenceArticleDescriptionChanged( boolean valueToSet ) {
            referenceArticleDescriptionChanged = valueToSet;
        }

        public boolean isReferenceArticleDescriptionChangedFlagSet() {
            return referenceArticleDescriptionChanged;
        }

        public boolean isReferenceArticleDescriptionChangedFlagNotSet() {
            return !referenceArticleDescriptionChanged;
        }

        public void setReferenceArticleDescription( String description ) {
            analysisArticleJoin.setReferenceArticleDescription( description );
            setFlagReferenceArticleChanged( true );
            setFlagReferenceArticleDescriptionChanged( true );
            if (isNeedToSaveFlagNotSet()) {
                setFlagNeedToSave( true );
            }
        }

        private boolean isAnyValueChanged() {
            return
                    analysisArticleJoin.isCompetitorStorePriceSet() ||
                    analysisArticleJoin.areCommentsSet() ||
                    isReferenceArticleDataChanged();
        }

        protected boolean isNotAnyValueChanged() {
            return !isAnyValueChanged();
        }

        private boolean isReferenceArticleDataChanged() {
            return
                    analysisArticleJoin.isReferenceArticleNameSet() ||
                    analysisArticleJoin.isReferenceArticleEanSet() ||
                    analysisArticleJoin.isReferenceArticleDescriptionSet();
        }

    }
}