package com.dev4lazy.pricecollector.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;

public class AnalysisArticleJoinViewModel extends AndroidViewModel {

    private int AnalysisArticleJoinsRecyclerViewPosition;
    private AnalysisArticleJoin analysisArticleJoin;
    private ChangeInformer changeInformer;
    private boolean needToSave;
    private boolean priceChanged;
    private boolean commentsChanged;
    private boolean referenceArticleChanged;

    public AnalysisArticleJoinViewModel(Application application) {
        super(application);
        changeInformer = new ChangeInformer();
    }

    public void setAnalysisArticleJoin( AnalysisArticleJoin analysisArticleJoin ) {
        this.analysisArticleJoin = analysisArticleJoin;
    }

    public AnalysisArticleJoin getAnalysisArticleJoin() {
        return analysisArticleJoin;
    }

    public void setAnalysisArticleJoinsRecyclerViewPosition(int analysisArticleJoinsRecyclerViewPosition) {
        this.AnalysisArticleJoinsRecyclerViewPosition = analysisArticleJoinsRecyclerViewPosition;
    }

    public int getAnalysisArticleJoinsRecyclerViewPosition() {
        return AnalysisArticleJoinsRecyclerViewPosition;
    }

    public boolean isAnalysisArticleJoinNotModified() {
        return changeInformer.isNotAnyValueChanged();
    }

    public void setNeedToSave( boolean valueToSet ) {
        changeInformer.setNeedToSaveFlag( valueToSet );
    }

    public boolean isNeedToSave() {
        return changeInformer.isAnyFlagSet();
    }

    public ChangeInformer getChangeInformer() {
        return changeInformer;
    }

    class ChangeInformer {

        ChangeInformer() {
            clearFlags();
        }

        private void clearFlags() {
            needToSave = false;
            priceChanged = false;
            commentsChanged = false;
            referenceArticleChanged = false;
        };

        private void setNeedToSaveFlag(boolean valueToSet ) {
            needToSave = valueToSet;
        }

        private boolean isAnyFlagSet() {
            return needToSave;
        }

        private boolean isNotAnyFlagSet() {
            return needToSave;
        }

        private void setFlagPriceChanged( boolean valueToSet ) {
            priceChanged = valueToSet;
        }

        private boolean isPriceChangedFlagSet() {
            return priceChanged;
        }

        private boolean isPriceChangedFlagNotSet() {
            return !priceChanged;
        }

        private void setPrice( Double price ) {
            analysisArticleJoin.setCompetitorStorePrice( price );
            if (isNotAnyFlagSet()) {
                setNeedToSaveFlag( false );
            }
        }

        private void setFlagComments( boolean valueToSet ) {
            commentsChanged = valueToSet;
        }

        private boolean isCommentsChangedFlagSet() {
            return commentsChanged;
        }

        private boolean isCommentsChangedFlagNotSet() {
            return !commentsChanged;
        }

        private void setFlagReferenceArticleChanged( boolean valueToSet ) {
            referenceArticleChanged = valueToSet;
        }

        private boolean isReferenceArticleChangedFlagSet() {
            return referenceArticleChanged;
        }

        private boolean isReferenceArticleChangedFlagNotSet() {
            return !referenceArticleChanged;
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