package com.example.panacea.ui.helpline;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HelplineViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HelplineViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Helpline fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}