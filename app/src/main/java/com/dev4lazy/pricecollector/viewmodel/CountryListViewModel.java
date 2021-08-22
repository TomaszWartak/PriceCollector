package com.dev4lazy.pricecollector.viewmodel;

import android.app.Application;

import com.dev4lazy.pricecollector.model.db.CountryDao;
import com.dev4lazy.pricecollector.model.entities.Country;
import com.dev4lazy.pricecollector.utils.AppHandle;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

public class CountryListViewModel extends AndroidViewModel {

    private LiveData<PagedList<Country>> countriesLiveData;

    public CountryListViewModel(Application application) {
        super(application);
        CountryDao countryDao = AppHandle.getHandle().getLocalDatabase().countryDao();
        DataSource.Factory<Integer, Country> factory = countryDao.getAllPaged();
        LivePagedListBuilder<Integer, Country> pagedListBuilder = new LivePagedListBuilder<Integer, Country>(factory, 50);
        countriesLiveData = pagedListBuilder.build();
    }

    public LiveData<PagedList<Country>> getCountriesLiveData() {
        return countriesLiveData;
    }
}
