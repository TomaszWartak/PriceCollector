package com.dev4lazy.pricecollector.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dev4lazy.pricecollector.model.entities.Store;

public class StoreViewModel extends AndroidViewModel {

    private final MutableLiveData<Store> selected = new MutableLiveData<>();

    public StoreViewModel(Application application) {
        super(application);
    }

    public MutableLiveData<Store> getData() {
        return selected;
    }

    public void setData(Store store) {
        selected.setValue(store);
    }

}
