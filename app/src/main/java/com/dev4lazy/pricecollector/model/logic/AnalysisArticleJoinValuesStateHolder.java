package com.dev4lazy.pricecollector.model.logic;

import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;

public class AnalysisArticleJoinValuesStateHolder {

    private AnalysisArticleJoin analysisArticleJoin;
    private boolean needToSave;
    private boolean competitorPriceChanged;
    private boolean commentsChanged;
    private boolean referenceArticleChanged;
    private boolean referenceArticleNameChanged;
    private boolean referenceArticleEanChanged;
    private boolean referenceArticleDescriptionChanged;

    public AnalysisArticleJoinValuesStateHolder( ) {
        clearFlags();
    }

    public void setAnalysisArticleJoin( AnalysisArticleJoin analysisArticleJoin ) {
        this.analysisArticleJoin = analysisArticleJoin;
    }

    private void clearFlags() {
        needToSave = false;
        competitorPriceChanged = false;
        commentsChanged = false;
        referenceArticleChanged = false;
        referenceArticleNameChanged = false;
        referenceArticleEanChanged = false;
        referenceArticleDescriptionChanged = false;
    };

    public void setFlagNeedToSave(boolean valueToSet) {
        needToSave = valueToSet;
    }

    public boolean isNeedToSaveFlagSet() {
        return needToSave;
    }

    private boolean isNeedToSaveFlagNotSet() {
        return !needToSave;
    }

    public void clearFlagCompetitorPriceChanged() {
        setFlagCompetitorPriceChanged( false );
    }

    public void setFlagCompetitorPriceChanged(boolean valueToSet ) {
        competitorPriceChanged = valueToSet;
    }

    public boolean isCompetitorPriceChangedFlagSet() {
        return competitorPriceChanged;
    }

    public boolean isCompetitorPriceChangedFlagNotSet() {
        return !competitorPriceChanged;
    }

    public void setCompetitorStorePrice( Double price ) {
        analysisArticleJoin.setCompetitorStorePrice( price );
        setFlagCompetitorPriceChanged(true);
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

    public void setFlagReferenceArticleChanged(boolean valueToSet) {
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

    public boolean isAnyDataChangedFlagSet() {
        return
                competitorPriceChanged ||
                        commentsChanged ||
                        referenceArticleNameChanged ||
                        referenceArticleEanChanged ||
                        referenceArticleDescriptionChanged;
    }

    private boolean isAnyValueSet() {
        return
                analysisArticleJoin.isCompetitorStorePriceSet() ||
                        analysisArticleJoin.areCommentsSet() ||
                        isReferenceArticleDataSet();
    }

    public boolean isNotAnyValueSet() {
        return !isAnyValueSet();
    }

    private boolean isReferenceArticleDataSet() {
        return
                analysisArticleJoin.isReferenceArticleNameSet() ||
                        analysisArticleJoin.isReferenceArticleEanSet() ||
                        analysisArticleJoin.isReferenceArticleDescriptionSet();
    }

}
