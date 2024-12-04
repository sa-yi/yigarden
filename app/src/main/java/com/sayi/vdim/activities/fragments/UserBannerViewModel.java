package com.sayi.vdim.activities.fragments;

import androidx.lifecycle.*;

public class UserBannerViewModel extends ViewModel {

    private final MutableLiveData<String> userName = new MutableLiveData<>();
    private final MutableLiveData<String> sendTime = new MutableLiveData<>();

    public LiveData<String> getUserName() {
        return userName;
    }

    public void setUserName(String name) {
        userName.setValue(name);
    }

    public LiveData<String> getSendTime() {
        return sendTime;
    }

    public void setSendTime(String time) {
        sendTime.setValue(time);
    }

    public void fetchUserData(int id) {
    }
}
