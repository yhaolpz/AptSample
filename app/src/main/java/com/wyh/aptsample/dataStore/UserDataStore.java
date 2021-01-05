package com.wyh.aptsample.dataStore;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * @author WangYingHao
 */
public class UserDataStore {

    private MutableLiveData<UserData> mUserData = new MutableLiveData<>();

    public void loadUserData() {
        mUserData.postValue(new UserData());
    }

    public LiveData<UserData> getUserData() {
        return mUserData;
    }
}
