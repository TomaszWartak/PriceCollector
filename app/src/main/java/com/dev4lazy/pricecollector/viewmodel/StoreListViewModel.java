package com.dev4lazy.pricecollector.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.dev4lazy.pricecollector.model.db.StoreDao;
import com.dev4lazy.pricecollector.model.entities.Store;
import com.dev4lazy.pricecollector.utils.AppHandle;

public class StoreListViewModel extends AndroidViewModel {

    private LiveData<PagedList<Store>> storesLiveData;

    public StoreListViewModel(Application application) {
        super(application);
        StoreDao storeDao = AppHandle.getHandle().getLocalDatabase().storeDao();
        DataSource.Factory<Integer, Store>  factory = storeDao.getAllPaged();
        LivePagedListBuilder<Integer, Store> pagedListBuilder = new LivePagedListBuilder<Integer, Store>(factory, 50);
        storesLiveData = pagedListBuilder.build();
    }

    public LiveData<PagedList<Store>> getStoresLiveData() {
        return storesLiveData;
    }
}
