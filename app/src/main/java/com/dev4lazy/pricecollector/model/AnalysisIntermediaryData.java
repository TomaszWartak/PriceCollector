package com.dev4lazy.pricecollector.model;

public class AnalysisIntermediaryData { //[dbo].[PRC_CompetitorPrice]

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


    private Integer articleId;
    private Integer storeId;
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

    public static class AnalisisInputBuilder {

        private AnalysisIntermediaryData analysisIntermediaryData = new AnalysisIntermediaryData();

        public AnalisisInputBuilder store(Integer store ){
            analysisIntermediaryData.storeId = store;
            return this;
        }

        public AnalisisInputBuilder articleCode(Integer articleCode ){
            analysisIntermediaryData.articleId = articleCode;
            return this;
        }

        public AnalisisInputBuilder articleName(String articleName){
            analysisIntermediaryData.articleName = articleName;
            return this;
        }

        public AnalisisInputBuilder articleStorePrice(Double articleStorePrice ){
            analysisIntermediaryData.articleStorePrice = articleStorePrice;
            return this;
        }

        public AnalisisInputBuilder articleRefPrice(Double articleRefPrice ){
            analysisIntermediaryData.articleRefPrice = articleRefPrice;
            return this;
        }

        public AnalisisInputBuilder articleNewPrice(Double articleNewPrice ){
            analysisIntermediaryData.articleNewPrice = articleNewPrice;
            return this;
        }

        public AnalisisInputBuilder articleNewMarginPercent(Double articleNewMarginPercent ){
            analysisIntermediaryData.articleNewMarginPercent = articleNewMarginPercent;
            return this;
        }

        public AnalisisInputBuilder articleLmPrice(Double articleLmPrice ){
            analysisIntermediaryData.articleLmPrice = articleLmPrice;
            return this;
        }

        public AnalisisInputBuilder artlcleObiPrice(Double articleObiPrice ){
            analysisIntermediaryData.articleObiPrice = articleObiPrice;
            return this;
        }

        public AnalisisInputBuilder artlcleBricomanPrice(Double articleBricomanPrice ){
            analysisIntermediaryData.articleBricomanPrice = articleBricomanPrice;
            return this;
        }

        public AnalisisInputBuilder articleLocalCompetitor1Price(Double articleLocalCompetitor1Price ){
            analysisIntermediaryData.articleLocalCompetitor1Price = articleLocalCompetitor1Price;
            return this;
        }

        public AnalisisInputBuilder articleLocalCompetitor2Price(Double articleLocalCompetitor2Price ){
            analysisIntermediaryData.articleLocalCompetitor2Price = articleLocalCompetitor2Price;
            return this;
        }

        public AnalysisIntermediaryData build() {
            return analysisIntermediaryData;
        }
    }
}
