package com.example.panacea.ui.motivation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MotivationViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MotivationViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is motivation fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}