package com.dev4lazy.pricecollector.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;

public class AnalysisArticleJoinViewModel extends AndroidViewModel {

    private int positionOnList;
    private AnalysisArticleJoin analysisArticleJoin;
    private AnalysisArticleJoinValuesStateHolder valuesStateHolder;
    private boolean needToSave;
    private boolean priceChanged;
    private boolean commentsChanged;
    private boolean referenceArticleChanged;
    private boolean referenceArticleEanChanged;

    public AnalysisArticleJoinViewModel(Application application) {
        super(application);
        valuesStateHolder = new AnalysisArticleJoinValuesStateHolder();
    }

    public void setAnalysisArticleJoin( AnalysisArticleJoin analysisArticleJoin ) {
        this.analysisArticleJoin = analysisArticleJoin;
    }

    public AnalysisArticleJoin getAnalysisArticleJoin() {
        return analysisArticleJoin;
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


    public AnalysisArticleJoinValuesStateHolder getValuesStateHolder() {
        return valuesStateHolder;
    }

    public void clearNeedToSave( ) {
        valuesStateHolder.setFlagNeedToSave( false );
    }

    public boolean isNeedToSave() {
        return valuesStateHolder.isNeedToSaveFlagSet();
    }

    public class AnalysisArticleJoinValuesStateHolder {

        AnalysisArticleJoinValuesStateHolder() {
            clearFlags();
        }

        private void clearFlags() {
            needToSave = false;
            priceChanged = false;
            commentsChanged = false;
            referenceArticleChanged = false;
            referenceArticleEanChanged = false;
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

        public void setReferenceArticleName( String name ) {
            analysisArticleJoin.setReferenceArticleName( name );
            setFlagReferenceArticleChanged(true);
            if (isNeedToSaveFlagNotSet()) {
                setFlagNeedToSave(true);
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

        public void setReferenceArticleDescription( String description ) {
            analysisArticleJoin.setReferenceArticleDescription( description );
            setFlagReferenceArticleChanged(true);
            if (isNeedToSaveFlagNotSet()) {
                setFlagNeedToSave(true);
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