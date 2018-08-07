package com.example.searchphone.mvp;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public interface MvpMainView extends MvpLoadingView{
    void showToast(String msg);
    void updateView();
}
