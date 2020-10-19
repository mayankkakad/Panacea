package com.example.panacea.ui.faq;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FaqViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public FaqViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is FAQ fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}