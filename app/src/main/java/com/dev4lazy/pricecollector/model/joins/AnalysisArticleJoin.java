package com.dev4lazy.pricecollector.model.joins;

public class AnalysisArticleJoin {
    private int id; // from AnalysisArticle.
    private int analysisId; // from AnalysisArticle.
    private int article_Id; // from AnalysisArticle.
    private int competitorStoreId; // from AnalysisArticle.
    private Double competitorStorePrice; // from AnalysisArticle.
    private int referenceArticleId; // from AnalysisArticle.
    private String comments; // from AnalysisArticle.

    private String articleName; // from Article.name by AnalysisArticle.article_Id
    private String ownCode; // kod casto from OwnArticleInfo. by AnalysisArticle.article_Id
    private String eanCode; // from OwnArticleInfo. by AnalysisArticle.article_Id
    private String referenceArticleName; // from Article. by AnalysisArticle.referenceArticleId
    private String referenceArticleEan; // from Article. by AnalysisArticle.referenceArticleId

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(int analysisId) {
        this.analysisId = analysisId;
    }

    public int getArticle_Id() {
        return article_Id;
    }

    public void setArticle_Id(int article_Id) {
        this.article_Id = article_Id;
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
}
