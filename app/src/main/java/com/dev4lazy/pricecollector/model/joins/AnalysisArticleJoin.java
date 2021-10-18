package com.dev4lazy.pricecollector.model.joins;

import androidx.room.ColumnInfo;

import java.util.Objects;

public class AnalysisArticleJoin {
    @ColumnInfo(name = "id")
    private int analysisArticleId; // from AnalysisArticle.
    @ColumnInfo(name = "analysis_id")
    private int analysisId; // from AnalysisArticle.
    @ColumnInfo(name = "article_id")
    private int articleId; // from AnalysisArticle.
    @ColumnInfo(name = "own_article_info_id")
    private int ownArticleInfoId; // from AnalysisArticle.
    @ColumnInfo(name = "article_store_price")
    private Double articleStorePrice; // from AnalysisArticle.
    @ColumnInfo(name = "article_ref_price")
    private Double articleRefPrice; // from AnalysisArticle.
    @ColumnInfo(name = "article_new_price")
    private Double articleNewPrice; // from AnalysisArticle.
    @ColumnInfo(name = "competitor_store_id")
    private Integer competitorStoreId; // from CompetitorPrice.
    @ColumnInfo(name = "competitor_store_price_id")
    private Integer competitorStorePriceId; // from CompetitorPrice.
    @ColumnInfo(name = "competitor_store_price")
    private Double competitorStorePrice; // from CompetitorPrice.
    @ColumnInfo(name = "reference_article_id")
    private Integer referenceArticleId; // from CompetitorPrice.
    private String comments; // from AnalysisArticle.
    @ColumnInfo(name = "name")
    private String articleName; // from Article.name by AnalysisArticle.articleId
    private String ownCode; // kod briko from OwnArticleInfo. by AnalysisArticle.articleId
    @ColumnInfo(name = "value")
    private String eanCode; // from OwnArticleInfo. by AnalysisArticle.articleId
    private String referenceArticleName; // from Article by CompetitorPrice.referenceArticleId
    @ColumnInfo(name = "reference_article_ean_id")
    private Integer referenceArticleEanCodeId; // id Ean from EanCode by CompetitorPrice.referenceArticleEanCodeId
    private String referenceArticleEanCodeValue; // from EanCode by CompetitorPrice.referenceArticleId
    @ColumnInfo(name = "description")
    private String referenceArticleDescription; // from Article by CompetitorPrice.referenceArticleId

    /* TODO XXX
    public AnalysisArticleJoin() {
        competitorStoreId = -1;
        competitorStorePriceId=-1;
        referenceArticleId=-1;
    }*/

    public int getAnalysisArticleId() {
        return analysisArticleId;
    }

    public void setAnalysisArticleId(int id) {
        this.analysisArticleId = id;
    }

    public int getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(int analysisId) {
        this.analysisId = analysisId;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int article_Id) {
        this.articleId = article_Id;
    }

    public int getOwnArticleInfoId() {
        return ownArticleInfoId;
    }

    public void setOwnArticleInfoId(int ownArticleInfoId) {
        this.ownArticleInfoId = ownArticleInfoId;
    }

    public Double getArticleStorePrice() {
        return articleStorePrice;
    }

    public void setArticleStorePrice(Double articleStorePrice) {
        this.articleStorePrice = articleStorePrice;
    }

    public Double getArticleRefPrice() {
        return articleRefPrice;
    }

    public void setArticleRefPrice(Double articleRefPrice) {
        this.articleRefPrice = articleRefPrice;
    }

    public Double getArticleNewPrice() {
        return articleNewPrice;
    }

    public void setArticleNewPrice(Double articleNewPrice) {
        this.articleNewPrice = articleNewPrice;
    }

    public Integer getCompetitorStoreId() {
        return competitorStoreId;
    }

    public int getCompetitorStoreIdInt() {
        if (competitorStoreId==null) {
            return 0;
        }
        return competitorStoreId.intValue();
    }

    public boolean isCompetitorStoreIdNotSet() {
        if (competitorStoreId==null) {
            return true;
        }
        return competitorStoreId<1;
    }

    public void setCompetitorStoreId(Integer competitorStoreId) {
        this.competitorStoreId = competitorStoreId;
    }

    public Integer getCompetitorStorePriceId() {
        return competitorStorePriceId;
    }

    public int getCompetitorStorePriceIdInt() {
        if (competitorStorePriceId==null) {
            return 0;
        }
        return competitorStorePriceId.intValue();
    }

    public void setCompetitorStorePriceId(Integer competitorStorePriceId) {
        this.competitorStorePriceId = competitorStorePriceId;
    }

    public Double getCompetitorStorePrice() {
        return competitorStorePrice;
    }

    public void setCompetitorStorePrice(Double competitorStorePrice) {
        this.competitorStorePrice = competitorStorePrice;
    }
    /*  TODO XXX
    public AnalysisArticleJoin setCompetitorStorePrice(Double competitorStorePrice) {
        this.competitorStorePrice = competitorStorePrice;
        return this;
    }

     */

    public boolean isCompetitorStorePriceSet() {
        Double price = getCompetitorStorePrice();
        if (price==null) {
            return false;
        }
        return (0.0-price)<0;
    }

    public Integer getReferenceArticleId() {
        return referenceArticleId;
    }

    public int getReferenceArticleIdInt() {
        if (referenceArticleId==null) {
            return 0;
        }
        return referenceArticleId.intValue();
    }

    public void setReferenceArticleId(Integer referenceArticleId) {
        this.referenceArticleId = referenceArticleId;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public String getOwnCode() {
        return ownCode;
    }

    public void setOwnCode(String ownCode) {
        this.ownCode = ownCode;
    }

    public String getEanCode() {
        return eanCode;
    }

    public void setEanCode(String eanCode) {
        this.eanCode = eanCode;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean areCommentsSet() {
        if (getComments()==null)
            return false;
        return !getComments().isEmpty();
    }

    public String getReferenceArticleName() {
        return referenceArticleName;
    }

    public void setReferenceArticleName(String referenceArticleName) {
        this.referenceArticleName = referenceArticleName;
    }

    public boolean isReferenceArticleNameSet() {
        if (getReferenceArticleName()==null)
            return false;
        return !getReferenceArticleName().isEmpty();
    }

    public Integer getReferenceArticleEanCodeId() {
        return referenceArticleEanCodeId;
    }

    public int getReferenceArticleEanCodeIdInt() {
        if (referenceArticleEanCodeId==null) {
            return 0;
        }
        return referenceArticleEanCodeId.intValue();
    }

    public void setReferenceArticleEanCodeId(Integer referenceArticleEanCodeId) {
        this.referenceArticleEanCodeId = referenceArticleEanCodeId;
    }

    public String getReferenceArticleEanCodeValue() {
        return referenceArticleEanCodeValue;
    }

    public void setReferenceArticleEanCodeValue(String referenceArticleEanCodeValue) {
        this.referenceArticleEanCodeValue = referenceArticleEanCodeValue;
    }

    public boolean isReferenceArticleEanSet() {
        if (getReferenceArticleEanCodeValue()==null)
            return false;
        return !getReferenceArticleEanCodeValue().isEmpty();
    }

    public boolean isReferenceArticleEanNotSet() {
        return !isReferenceArticleEanSet();
    }

    public String getReferenceArticleDescription() {
        return referenceArticleDescription;
    }

    public void setReferenceArticleDescription(String referenceArticleDescription) {
        this.referenceArticleDescription = referenceArticleDescription;
    }

    public boolean isReferenceArticleDescriptionSet() {
        if (getReferenceArticleDescription()==null)
            return false;
        return !getReferenceArticleDescription().isEmpty();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnalysisArticleJoin)) return false;
        AnalysisArticleJoin that = (AnalysisArticleJoin) o;
        return analysisArticleId == that.analysisArticleId &&
                analysisId == that.analysisId &&
                articleId == that.articleId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(analysisArticleId, analysisId, articleId);
    }
}
