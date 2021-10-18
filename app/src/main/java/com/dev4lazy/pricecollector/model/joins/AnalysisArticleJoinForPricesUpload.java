package com.dev4lazy.pricecollector.model.joins;

public class AnalysisArticleJoinForPricesUpload {

    private int remote_analysis_id;
    private String article_ownCode;
    private String competitor_company_name;
    private Double competitor_store_price;

    public int getRemote_analysis_id() {
        return remote_analysis_id;
    }

    public void setRemote_analysis_id(int remote_analysis_id) {
        this.remote_analysis_id = remote_analysis_id;
    }

    public String getArticle_ownCode() {
        return article_ownCode;
    }

    public void setArticle_ownCode(String article_ownCode) {
        this.article_ownCode = article_ownCode;
    }

    public String getCompetitor_company_name() {
        return competitor_company_name;
    }

    public void setCompetitor_company_name(String competitor_company_name) {
        this.competitor_company_name = competitor_company_name;
    }

    public Double getCompetitor_store_price() {
        return competitor_store_price;
    }

    public void setCompetitor_store_price(Double competitor_store_price) {
        this.competitor_store_price = competitor_store_price;
    }
}
