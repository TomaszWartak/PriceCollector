package com.dev4lazy.pricecollector.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;

// TODO XXX to chyba do nczego nie jest potrzebne
public class SearchArticlesCriteriaViewModel extends AndroidViewModel {

    private boolean onChangedReactionAllowed;
    private boolean filterSet;
    private String articleName;
    private String articleEAN;
    private String articleSKU;
    private String articleAnyText;
    private int articleSectorId;
    private int articleDepartmentId;


    public SearchArticlesCriteriaViewModel(Application application) {
        super(application);
        setOnChangedReactionNotAllowed();
        clearAllData();
    }

    public boolean isOnChangedReactionAllowed() {
        return onChangedReactionAllowed;
    }

    public void setOnChangedReactionAllowed() {
        this.onChangedReactionAllowed = true;
    }

    public void setOnChangedReactionNotAllowed() {
        this.onChangedReactionAllowed = false;
    }

    public void clearAllData() {
        setArticleName("");
        setArticleEAN("");
        setArticleSKU("");
        setArticleAnyText("");
        setArticleSectorId( 0 );
        setArticleDepartmentId( 0 );
    }

    public boolean isFilterSet() {
        return filterSet;
    }

    public void setFilterSet(boolean filterSet) {
        this.filterSet = filterSet;
    }

    public boolean areCriteriaSet() {
        boolean result =
            ( getArticleName()!="") &&
            ( getArticleEAN()!="" ) &&
            ( getArticleSKU()!="" ) &&
            ( getArticleAnyText()!="" ) &&
            ( getArticleSectorId()!=0 ) &&
            ( getArticleDepartmentId()!=0 );
        return result;
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
