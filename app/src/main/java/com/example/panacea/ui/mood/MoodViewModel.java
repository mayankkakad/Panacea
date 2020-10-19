package com.example.panacea.ui.mood;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MoodViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MoodViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is mood based content fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}