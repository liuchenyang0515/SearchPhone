package com.example.searchphone;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.searchphone.model.Phone;
import com.example.searchphone.mvp.MvpMainView;
import com.example.searchphone.mvp.impl.MainPresenter;

public class MainActivity extends AppCompatActivity implements MvpMainView {

    EditText input_phone;
    Button btn_search;
    TextView result_phone;
    TextView result_province;
    TextView result_type;
    TextView result_carrier;
    MainPresenter mainPresenter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input_phone = findViewById(R.id.input_phone);
        btn_search = findViewById(R.id.btn_search);
        result_phone = findViewById(R.id.result_phone);
        result_province = findViewById(R.id.result_province);
        result_type = findViewById(R.id.result_type);
        result_carrier = findViewById(R.id.result_carrier);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPresenter.searchPhoneInfo(input_phone.getText().toString());
            }
        });
        mainPresenter = new MainPresenter(this);
        mainPresenter.attach(this);
    }

    // mvpMainView接口的方法
    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateView() {
        Phone phone = mainPresenter.getPhoneInfo();
        result_phone.setText("手机号码：" + phone.getTelString());
        result_province.setText("省份：" + phone.getProvince());
        result_type.setText("运营商：" + phone.getCatName());
        result_carrier.setText("归属地运营商:" + phone.getCarrier());
    }

    @Override
    public void showLoading() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog.show(this, "", "正在加载...", true, false);
        } else if (progressDialog.isShowing()) {
            progressDialog.setTitle("");
            progressDialog.setMessage("正在加载...");
        }
        progressDialog.show();
    }

    @Override
    public void hidenLoading() {
        if (progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
}
