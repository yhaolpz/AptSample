package com.wyh.aptsample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wyh.aptsample.dataStore.BookDataStore;
import com.wyh.aptsample.dataStore.UserDataStore;
import com.wyh.viewmodel.annotation.ViewModelAutoGen;

@ViewModelAutoGen({
        UserDataStore.class
})
public class UserFragment extends Fragment {

    public static UserFragment newInstance() {
        return new UserFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.blank_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}