package com.sayi.vdim.activities.mainfragments.user;

import androidx.lifecycle.*;

import com.sayi.vdim.dz_entity.*;

public class UserViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    private final MutableLiveData<DzUser> user;

    public UserViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is user fragment");

        user = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<DzUser> getUserData() {
        return user;
    }
}