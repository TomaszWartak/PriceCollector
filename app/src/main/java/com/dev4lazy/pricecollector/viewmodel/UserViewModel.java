package com.dev4lazy.pricecollector.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.dev4lazy.pricecollector.model.logic.User;

public class UserViewModel extends AndroidViewModel {

    private User user;

    public UserViewModel(@NonNull Application application, User user) {
        super(application);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
