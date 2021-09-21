package com.dev4lazy.pricecollector.model.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "competitors_prices",
        foreignKeys = {
                @ForeignKey( entity = Analysis.class, parentColumns = "id", childColumns = "analysis_id" ),
                @ForeignKey( entity = Store.class, parentColumns = "id", childColumns = "competitor_store_id" )
        },
        indices = {
                @Index( value = "analysis_id" ),
                @Index( value = "competitor_store_id" )
        }
)
public class CompetitorPrice {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "analysis_id")
    private int analysisId;

    @ColumnInfo(name = "analysis_article_id")
    private int analysisArticleId;

    @ColumnInfo(name = "own_article_info_id")
    private int ownArticleInfoId;

    @ColumnInfo(name = "competitor_store_id")
    private int competitorStoreId;

    @ColumnInfo(name = "competitor_store_price")
    private double competitorStorePrice;

    //  TODO Musi być w CompetitorPrice, bo w każdym sklepie konkurenta, może byc inny art ref
    @ColumnInfo(name = "reference_article_id")
    private int referenceArticleId;

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

    public int getAnalysisArticleId() {
        return analysisArticleId;
    }

    public void setAnalysisArticleId(int analysisArticleId) {
        this.analysisArticleId = analysisArticleId;
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

    public double getCompetitorStorePrice() {
        return competitorStorePrice;
    }

    public void setCompetitorStorePrice(double competitorStorePrice) {
        this.competitorStorePrice = competitorStorePrice;
    }

    public int getReferenceArticleId() {
        return referenceArticleId;
    }

    public void setReferenceArticleId(int referenceArticleId) {
        this.referenceArticleId = referenceArticleId;
    }

}
