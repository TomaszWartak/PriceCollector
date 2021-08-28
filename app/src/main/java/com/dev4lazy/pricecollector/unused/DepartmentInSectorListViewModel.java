package com.dev4lazy.pricecollector.unused;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.dev4lazy.pricecollector.model.db.DepartmentInSectorDao;
import com.dev4lazy.pricecollector.model.entities.DepartmentInSector;
import com.dev4lazy.pricecollector.AppHandle;

public class DepartmentInSectorListViewModel extends AndroidViewModel {

    private LiveData<PagedList<DepartmentInSector>> departmentInSectorsLiveData;

    public DepartmentInSectorListViewModel(Application application) {
        super(application);
        DepartmentInSectorDao departmentInSectorDao = AppHandle.getHandle().getLocalDatabase().departmentInSectorDao();
        DataSource.Factory<Integer, DepartmentInSector>  factory = departmentInSectorDao.getAllPaged();
        LivePagedListBuilder<Integer, DepartmentInSector> pagedListBuilder = new LivePagedListBuilder<Integer, DepartmentInSector>(factory, 50);
        departmentInSectorsLiveData = pagedListBuilder.build();
    }

    public LiveData<PagedList<DepartmentInSector>> getDepartmentInSectorsLiveData() {
        return departmentInSectorsLiveData;
    }
}
