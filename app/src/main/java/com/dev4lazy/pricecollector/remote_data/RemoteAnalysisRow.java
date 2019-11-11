package com.dev4lazy.pricecollector.remote_data;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "analysis_rows", // w bazie zdalnej nie ma takiej tabeli
        indices = {
            @Index(
                name = "index_analysis_rows_articleCode",
                value = "articleCode",
                unique = true
            )
        }
)
// todo
//  sprawdź dla dużej ilości wierszy (1000?) jaki wpływ na czas wyświetlenie (będzie jakieś wyświetlanie?),
//  będzie miał dodanie indeksu po kodzie casto
public class RemoteAnalysisRow { //[dbo].[PRC_CompetitorPrice]

    /*
    [SKU_Id] [int] NOT NULL,
    [DEP_Id] [int] NOT NULL,
	[IMP_Date] [date] NOT NULL,
	[CYC_Date] [date] NOT NULL,
	[SHO_Id] [int] NOT NULL,
	[SKU_ShopPrice] [money] NULL,
    [SKU_ShopPriceDate] [date] NULL,
    [SKU_ShopCost] [money] NULL,
    [SKU_ShopCostDate] [date] NULL,
    [SKU_PMMPrice] [money] NULL,
    [SKU_PMMPriceDate] [date] NULL,
    [SKU_CastoChangePrice] [money] NULL,
    [SKU_LMPrice] [money] NULL,
    [SKU_OBIPrice] [money] NULL,
    [SKU_BricomanPrice] [money] NULL,
    [SKU_LocalCompetitorPrice1] [money] NULL,
    [SKU_LocalCompetitorPrice2] [money] NULL,        */


    @PrimaryKey(autoGenerate = true)
    private int id;
    private Integer analysisId;
    private Integer articleCode; // kod casto
    private Integer store;
    private String articleName;
    private Double articleStorePrice;
    private Double articleRefPrice;
    private Double articleNewPrice;
    private Double articleNewMarginPercent;
    private Double articleLmPrice;
    private Double articleObiPrice;
    private Double articleBricomanPrice;
    private Double articleLocalCompetitor1Price;
    private Double articleLocalCompetitor2Price;
    private String department;
    private String sector;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId( Integer analysisId) {
        this.analysisId = analysisId;
    }

    public Integer getArticleCode() {
        return articleCode;
    }

    public void setArticleCode( Integer articleCode) {
        this.articleCode = articleCode;
    }

    public Integer getStore() {
        return store;
    }

    public void setStore(Integer store) {
        this.store = store;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
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

    public Double getArticleNewMarginPercent() {
        return articleNewMarginPercent;
    }

    public void setArticleNewMarginPercent(Double articleNewMarginPercent) {
        this.articleNewMarginPercent = articleNewMarginPercent;
    }

    public Double getArticleLmPrice() {
        return articleLmPrice;
    }

    public void setArticleLmPrice(Double articleLmPrice) {
        this.articleLmPrice = articleLmPrice;
    }

    public Double getArticleObiPrice() {
        return articleObiPrice;
    }

    public void setArticleObiPrice(Double articleObiPrice) {
        this.articleObiPrice = articleObiPrice;
    }

    public Double getArticleBricomanPrice() {
        return articleBricomanPrice;
    }

    public void setArticleBricomanPrice(Double articleBricomanPrice) {
        this.articleBricomanPrice = articleBricomanPrice;
    }

    public Double getArticleLocalCompetitor1Price() {
        return articleLocalCompetitor1Price;
    }

    public void setArticleLocalCompetitor1Price(Double articleLocalCompetitor1Price) {
        this.articleLocalCompetitor1Price = articleLocalCompetitor1Price;
    }

    public Double getArticleLocalCompetitor2Price() {
        return articleLocalCompetitor2Price;
    }

    public void setArticleLocalCompetitor2Price(Double articleLocalCompetitor2Price) {
        this.articleLocalCompetitor2Price = articleLocalCompetitor2Price;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RemoteAnalysisRow)) return false;
        RemoteAnalysisRow that = (RemoteAnalysisRow) o;
        return id == that.id;
    }

    public static class AnalysisRowBuilder {

        private RemoteAnalysisRow remoteAnalysisRow = new RemoteAnalysisRow();

        public AnalysisRowBuilder analysisId( Integer id ) {
            remoteAnalysisRow.id = id;
            return this;
        }

        public AnalysisRowBuilder store(Integer store ){
            remoteAnalysisRow.store = store;
            return this;
        }

        public AnalysisRowBuilder articleCode(Integer articleCode ){
            remoteAnalysisRow.articleCode = articleCode;
            return this;
        }

        public AnalysisRowBuilder articleName(String articleName){
            remoteAnalysisRow.articleName = articleName;
            return this;
        }

        public AnalysisRowBuilder articleStorePrice(Double articleStorePrice ){
            remoteAnalysisRow.articleStorePrice = articleStorePrice;
            return this;
        }

        public AnalysisRowBuilder articleRefPrice(Double articleRefPrice ){
            remoteAnalysisRow.articleRefPrice = articleRefPrice;
            return this;
        }

        public AnalysisRowBuilder articleNewPrice(Double articleNewPrice ){
            remoteAnalysisRow.articleNewPrice = articleNewPrice;
            return this;
        }

        public AnalysisRowBuilder articleNewMarginPercent(Double articleNewMarginPercent ){
            remoteAnalysisRow.articleNewMarginPercent = articleNewMarginPercent;
            return this;
        }

        public AnalysisRowBuilder articleLmPrice(Double articleLmPrice ){
            remoteAnalysisRow.articleLmPrice = articleLmPrice;
            return this;
        }

        public AnalysisRowBuilder artlcleObiPrice(Double articleObiPrice ){
            remoteAnalysisRow.articleObiPrice = articleObiPrice;
            return this;
        }

        public AnalysisRowBuilder artlcleBricomanPrice(Double articleBricomanPrice ){
            remoteAnalysisRow.articleBricomanPrice = articleBricomanPrice;
            return this;
        }

        public AnalysisRowBuilder articleLocalCompetitor1Price(Double articleLocalCompetitor1Price ){
            remoteAnalysisRow.articleLocalCompetitor1Price = articleLocalCompetitor1Price;
            return this;
        }

        public AnalysisRowBuilder articleLocalCompetitor2Price(Double articleLocalCompetitor2Price ){
            remoteAnalysisRow.articleLocalCompetitor2Price = articleLocalCompetitor2Price;
            return this;
        }

        public AnalysisRowBuilder department( String department ){
            remoteAnalysisRow.department = department;
            return this;
        }

        public AnalysisRowBuilder sector( String sector ){
            remoteAnalysisRow.sector = sector;
            return this;
        }

        public RemoteAnalysisRow build() {
            return remoteAnalysisRow;
        }
    }
}
