package com.dev4lazy.pricecollector.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;

public class SearchArticlesViewModel extends AndroidViewModel {

    private boolean onChangedReactionAllowed;
    private String articleName;
    private String articleEAN;
    private String articleSKU;
    private String articleAnyText;
    private int articleSectorId;
    private int articleDepartmentId;


    public SearchArticlesViewModel(Application application) {
        super(application);
        setOnChangedReactionNotAllowed();
    }

    public boolean isOnChangedReactionAllowed() {
        return onChangedReactionAllowed;
    }

    public void setOnChangedReactionAllowed() {
        this.onChangedReactionAllowed = true;
    }

    public void clearAllData() {
        setArticleName("");
        setArticleEAN("");
        setArticleSKU("");
        setArticleAnyText("");
        setArticleSectorId( 0 );
        setArticleDepartmentId( 0 );
    }

    public void setOnChangedReactionNotAllowed() {
        this.onChangedReactionAllowed = false;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public String getArticleEAN() {
        return articleEAN;
    }

    public void setArticleEAN(String articleEAN) {
        this.articleEAN = articleEAN;
    }

    public String getArticleSKU() {
        return articleSKU;
    }

    public void setArticleSKU(String articleSKU) {
        this.articleSKU = articleSKU;
    }

    public String getArticleAnyText() {
        return articleAnyText;
    }

    public void setArticleAnyText(String articleAnyText) {
        this.articleAnyText = articleAnyText;
    }

    public int getArticleSectorId() {
        return articleSectorId;
    }

    public void setArticleSectorId(int sectorId) {
        this.articleSectorId = sectorId;
    }

    public int getArticleDepartmentId() {
        return articleDepartmentId;
    }

    public void setArticleDepartmentId(int departmentId) {
        this.articleDepartmentId = departmentId;
    }
}
