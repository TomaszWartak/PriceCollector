package com.dev4lazy.pricecollector.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.dev4lazy.pricecollector.model.entities.Store;

public class StoreViewModel extends AndroidViewModel {

    private boolean onChangedReactionAllowed;
    private Store temporary;
    private MutableLiveData<Store> selected = new MutableLiveData<>();

    public StoreViewModel(Application application) {
        super(application);
        setOnChangedReactionNotAllowed();
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

    public void setStore(Store store) {
        selected.setValue(store);
    }

    public Store getStore() {
        return selected.getValue();
    }

    public MutableLiveData<Store> getData() {
        return selected;
    }

}
