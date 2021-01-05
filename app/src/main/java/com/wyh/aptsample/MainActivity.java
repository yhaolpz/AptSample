package com.wyh.aptsample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.wyh.aptsample.dataStore.BookDataStore;
import com.wyh.aptsample.dataStore.UserDataStore;
import com.wyh.viewmodel.annotation.ViewModelAutoGen;

@ViewModelAutoGen({
        UserDataStore.class,
        BookDataStore.class
})
public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel mViewMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}