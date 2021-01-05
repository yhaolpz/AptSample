package com.wyh.aptsample.dataStore;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * @author WangYingHao
 */
public class BookDataStore {

    private MutableLiveData<BookData> mBookData = new MutableLiveData<>();

    public void loadBookData() {
        mBookData.postValue(new BookData());
    }

    public LiveData<BookData> getBookData() {
        return mBookData;
    }
}
