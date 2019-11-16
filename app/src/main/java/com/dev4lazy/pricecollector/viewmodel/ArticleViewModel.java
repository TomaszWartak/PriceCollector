package com.dev4lazy.pricecollector.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.dev4lazy.pricecollector.model.db.LocalDatabase;
import com.dev4lazy.pricecollector.model.entities.Article;

public class ArticleViewModel extends AndroidViewModel {

    private LiveData<PagedList<Article>> articlesLiveData;

    public ArticleViewModel(Application application) {
        super(application);
        DataSource.Factory<Integer, Article> factory = LocalDatabase.getInstance().articleDao().getAllPaged();
        LivePagedListBuilder<Integer, Article> pagedListBuilder = new LivePagedListBuilder<Integer, Article>(factory, 50);
        articlesLiveData = pagedListBuilder.build();
    }

    public LiveData<PagedList<Article>> getArticlesLiveDataPaged() {
        return articlesLiveData;
    }

}