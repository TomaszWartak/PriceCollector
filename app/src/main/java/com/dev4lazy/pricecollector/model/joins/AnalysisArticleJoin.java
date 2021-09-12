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
    @ColumnInfo(name = "article_store_price")
    private Double articleStorePrice; // from AnalysisArticle.
    @ColumnInfo(name = "article_ref_price")
    private Double articleRefPrice; // from AnalysisArticle.
    @ColumnInfo(name = "article_new_price")
    private Double articleNewPrice; // from AnalysisArticle.
    @ColumnInfo(name = "competitor_store_id")
    private String comments; // from AnalysisArticle.
    private int competitorStoreId; // from CompetitorPrice.
    @ColumnInfo(name = "competitor_store_price")
    private Double competitorStorePrice; // from CompetitorPrice.
    @ColumnInfo(name = "reference_article_id")
    private int referenceArticleId; // from CompetitorPrice.
    @ColumnInfo(name = "description")
    private String refArticleComment; // from Article.description
    @ColumnInfo(name = "name")
    private String articleName; // from Article.name by AnalysisArticle.articleId
    private String ownCode; // kod briko from OwnArticleInfo. by AnalysisArticle.articleId
    @ColumnInfo(name = "value")
    private String eanCode; // from OwnArticleInfo. by AnalysisArticle.articleId
    private String referenceArticleName; // from Article. by AnalysisArticle.referenceArticleId
    private String referenceArticleEan; // from Article. by AnalysisArticle.referenceArticleId

    /*
    QUERY:
    +id,
    analysis_id,
    article_id,
    competitor_store_id,
    competitor_store_price,
    +article_store_price,
    +article_ref_price,
    +article_new_price,
    reference_article_id,
    comments,
    name,
    ownCode,
    -id,
    -remote_id,
    -value,
    article_id.

    Fields in com.dev4lazy.pricecollector.model.joins.AnalysisArticleJoin:
    -analysis_articles.id,
    analysis_id,
    article_id,
    competitor_store_id,
    competitor_store_price,
    reference_article_id,
    comments,
    name,
    ownCode,
    -eanCode,
    -referenceArticleName,
    -referenceArticleEan
     */

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

    public int getCompetitorStoreId() {
        return competitorStoreId;
    }

    public void setCompetitorStoreId(int competitorStoreId) {
        this.competitorStoreId = competitorStoreId;
    }

    public Double getCompetitorStorePrice() {
        return competitorStorePrice;
    }

    public void setCompetitorStorePrice(Double competitorStorePrice) {
        this.competitorStorePrice = competitorStorePrice;
    }

    public int getReferenceArticleId() {
        return referenceArticleId;
    }

    public void setReferenceArticleId(int referenceArticleId) {
        this.referenceArticleId = referenceArticleId;
    }

    public String getRefArticleComment() {
        return refArticleComment;
    }

    public void setRefArticleComment(String refArticleComment) {
        this.refArticleComment = refArticleComment;
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

    public String getReferenceArticleName() {
        return referenceArticleName;
    }

    public void setReferenceArticleName(String referenceArticleName) {
        this.referenceArticleName = referenceArticleName;
    }

    public String getReferenceArticleEan() {
        return referenceArticleEan;
    }

    public void setReferenceArticleEan(String referenceArticleEan) {
        this.referenceArticleEan = referenceArticleEan;
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
