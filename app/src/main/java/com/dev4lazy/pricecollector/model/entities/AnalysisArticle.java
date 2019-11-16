package com.dev4lazy.pricecollector.model.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.dev4lazy.pricecollector.model.utils.DateConverter;

@Entity(tableName = "analysis_articles" ) // w bazie zdalnej nie ma takiej tabeli
@TypeConverters({
        DateConverter.class
})
public class AnalysisArticle {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "analysis_id")
    private int analysisId;
    @ColumnInfo(name = "article_id")
    private int articleId;
    @ColumnInfo(name = "own_article_info_id")
    private int ownArticleInfoId;
    @ColumnInfo(name = "competitor_store_id")
    private int competitorStoreId; // todo?? usuń z bazy bo jest CompetitorPrice
    @ColumnInfo(name = "competitor_store_price")
    private Double competitorStorePrice; // todo?? usuń z bazy bo jest CompetitorPrice
    @ColumnInfo(name = "article_store_price")
    private Double articleStorePrice;
    @ColumnInfo(name = "article_ref_price")
    private Double articleRefPrice;
    @ColumnInfo(name = "article_new_price")
    private Double articleNewPrice;
    @ColumnInfo(name = "reference_article_id")
    private int referenceArticleId; // todo?? usuń z bazy bo jest CompetitorPrice

    private String comments;

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

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public int getOwnArticleInfoId() {
        return ownArticleInfoId;
    }

    public void setOwnArticleInfoId(int ownArticleInfoId) {
        this.ownArticleInfoId = ownArticleInfoId;
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

    public int getReferenceArticleId() {
        return referenceArticleId;
    }

    public void setReferenceArticleId(int referenceArticleId) {
        this.referenceArticleId = referenceArticleId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnalysisArticle)) return false;
        AnalysisArticle that = (AnalysisArticle) o;
        return id == that.id;
    }
}
