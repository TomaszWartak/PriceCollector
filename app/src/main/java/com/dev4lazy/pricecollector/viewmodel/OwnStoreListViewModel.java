package com.dev4lazy.pricecollector.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.dev4lazy.pricecollector.model.db.OwnStoreDao;
import com.dev4lazy.pricecollector.model.entities.OwnStore;
import com.dev4lazy.pricecollector.utils.AppHandle;

public class OwnStoreListViewModel extends AndroidViewModel {

    private LiveData<PagedList<OwnStore>> ownStoresLiveData;

    public OwnStoreListViewModel(Application application) {
        super(application);
        OwnStoreDao ownStoreDao = AppHandle.getHandle().getLocalDatabase().ownStoreDao();
        DataSource.Factory<Integer, OwnStore>  factory = ownStoreDao.getAllOwnStoresPaged();
        LivePagedListBuilder<Integer, OwnStore> pagedListBuilder = new LivePagedListBuilder<Integer, OwnStore>(factory, 50);
        ownStoresLiveData = pagedListBuilder.build();
    }

    public LiveData<PagedList<OwnStore>> getOwnStoresLiveData() {
        return ownStoresLiveData;
    }
}
