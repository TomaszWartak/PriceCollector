package com.dev4lazy.pricecollector.model.logic;

import com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin;
import com.google.android.gms.common.data.FreezableUtils;

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

    public AnalysisArticleJoinValuesStateHolder setAnalysisArticleJoin( AnalysisArticleJoin analysisArticleJoin ) {
        this.analysisArticleJoin = analysisArticleJoin;
        return this;
    }

    public void clearFlags() {
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

    public AnalysisArticleJoinValuesStateHolder setCompetitorStorePrice( Double price ) {
        analysisArticleJoin.setCompetitorStorePrice( price );
        setFlagCompetitorPriceChanged(true);
        if (isNeedToSaveFlagNotSet()) {
            setFlagNeedToSave(true);
        }
        return this;
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

    public AnalysisArticleJoinValuesStateHolder setComments( String comments ) {
        analysisArticleJoin.setComments( comments );
        setFlagCommentsChanged(true);
        if (isNeedToSaveFlagNotSet()) {
            setFlagNeedToSave(true);
        }
        return this;
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

    public AnalysisArticleJoinValuesStateHolder setReferenceArticleName( String name ) {
        analysisArticleJoin.setReferenceArticleName( name );
        setFlagReferenceArticleChanged( true );
        setFlagReferenceArticleNameChanged( true );
        if (isNeedToSaveFlagNotSet()) {
            setFlagNeedToSave( true );
        }
        return this;
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

    public AnalysisArticleJoinValuesStateHolder setReferenceArticleEanCodeValue(String ean ) {
        analysisArticleJoin.setReferenceArticleEanCodeValue( ean );
        // TODO XXX setFlagReferenceArticleChanged(true);
        setFlagReferenceArticleEanChanged(true);
        if (isNeedToSaveFlagNotSet()) {
            setFlagNeedToSave(true);
        }
        return this;
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

    public AnalysisArticleJoinValuesStateHolder setReferenceArticleDescription( String description ) {
        analysisArticleJoin.setReferenceArticleDescription( description );
        setFlagReferenceArticleChanged( true );
        setFlagReferenceArticleDescriptionChanged( true );
        if (isNeedToSaveFlagNotSet()) {
            setFlagNeedToSave( true );
        }
        return this;
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
