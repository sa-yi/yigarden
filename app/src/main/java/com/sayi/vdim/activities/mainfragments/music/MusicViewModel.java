package com.sayi.vdim.activities.mainfragments.music;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MusicViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MusicViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is music fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}