package com.dev4lazy.pricecollector.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.dev4lazy.pricecollector.model.entities.Store;
import com.dev4lazy.pricecollector.utils.Action;

public class StoreViewModel extends AndroidViewModel {

    private boolean onChangedReactionAllowed;
    private Store temporary;
    private MutableLiveData<Store> selected = new MutableLiveData<>();
    private Action actionToDo;

    public StoreViewModel(Application application) {
        super(application);
        setOnChangedReactionNotAllowed();
        // TODO XXX resetData();
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

    // TODO XXX
    public void setTemporaryData(Store temporary) {
        this.temporary = temporary;
    }

    // TODO XXX
    public Store getTemporaryData() {
        return temporary;
    }

    // TODO XXX
    public void clearTemporaryData() {
        setTemporaryData(null);
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

    // TODO XXX
    public void resetData() {
        selected = new MutableLiveData<>();
    }

    public Action getActionToDo() {
        return actionToDo;
    }

    public void setActionToDo(Action actionToDo) {
        this.actionToDo = actionToDo;
    }
}
