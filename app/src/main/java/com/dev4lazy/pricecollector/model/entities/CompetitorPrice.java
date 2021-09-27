package com.dev4lazy.pricecollector.model.entities;

import androidx.annotation.Nullable;
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
    private Double competitorStorePrice;

    // Musi być w CompetitorPrice, bo w każdym sklepie konkurenta, może byc inny art ref
    @ColumnInfo(name = "reference_article_id")
    private int referenceArticleId;

    @ColumnInfo(name = "reference_article_ean_id")
    @Nullable
    private int referenceArticleEanCodeId;

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

    public int getReferenceArticleEanCodeId() {
        return referenceArticleEanCodeId;
    }

    public void setReferenceArticleEanCodeId(int referenceArticleEanCodeId) {
        this.referenceArticleEanCodeId = referenceArticleEanCodeId;
    }
}
