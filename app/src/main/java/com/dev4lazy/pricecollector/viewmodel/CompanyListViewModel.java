package com.dev4lazy.pricecollector.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.dev4lazy.pricecollector.model.db.CompanyDao;
import com.dev4lazy.pricecollector.model.entities.Company;
import com.dev4lazy.pricecollector.utils.AppHandle;

public class CompanyListViewModel extends AndroidViewModel {

    private LiveData<PagedList<Company>> companiesLiveData;

    public CompanyListViewModel(Application application) {
        super(application);
        CompanyDao companyDao = AppHandle.getHandle().getLocalDatabase().companyDao();
        DataSource.Factory<Integer, Company>  factory = companyDao.getAllPaged();
        LivePagedListBuilder<Integer, Company> pagedListBuilder = new LivePagedListBuilder<Integer, Company>(factory, 50);
        companiesLiveData = pagedListBuilder.build();
    }

    public LiveData<PagedList<Company>> getCompaniesLiveData() {
        return companiesLiveData;
    }
}
