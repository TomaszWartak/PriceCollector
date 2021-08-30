package com.dev4lazy.pricecollector.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;

public class SearchArticlesViewModel extends AndroidViewModel {

    private boolean onChangedReactionAllowed;
    private String articleName;
    private String articleEANE;
    private String articleSKUE;
    private String articleAnyText;


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

    // TODO XXX
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
        return articleEANE;
    }

    public void setArticleEANE(String articleEANE) {
        this.articleEANE = articleEANE;
    }

    public String getArticleSKU() {
        return articleSKUE;
    }

    public void setArticleSKUE(String articleSKUE) {
        this.articleSKUE = articleSKUE;
    }

    public String getArticleAnyText() {
        return articleAnyText;
    }

    public void setArticleAnyText(String articleAnyText) {
        this.articleAnyText = articleAnyText;
    }
}
