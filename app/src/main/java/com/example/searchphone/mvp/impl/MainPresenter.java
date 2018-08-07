package com.example.searchphone.mvp.impl;

import com.example.searchphone.business.HttpUtils;
import com.example.searchphone.model.Phone;
import com.example.searchphone.mvp.MvpMainView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainPresenter extends BasePresenter {
    String mUrl = "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm";
    MvpMainView mvpMainView;
    Phone mPhone;
    public Phone getPhoneInfo(){
        return mPhone;
    }
    public MainPresenter(MvpMainView mainView) {
        mvpMainView = mainView;
    }

    public void searchPhoneInfo(String phone) {
        if (phone.length() != 11) {
            mvpMainView.showToast("请输入正确的手机号");
            return;
        }
        mvpMainView.showLoading();
        // 写上http请求处理逻辑
        sendHttp(phone);
    }

    private void sendHttp(String phone) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("tel", phone);
        HttpUtils httpUtils = new HttpUtils(new HttpUtils.HttpResponse() {
            @Override
            public void onSuccess(Object object) {
                String json = object.toString();
                int index = json.indexOf("{");
                json = json.substring(index, json.length());
                // 使用JSONObject
                mPhone = parseModelWithOrgJson(json);
                // Gson
                mPhone = parseModelWithGson(json);
                // FastJson
                mPhone = parseModelWithFastJson(json);
                mvpMainView.hidenLoading();
                mvpMainView.updateView();
            }

            @Override
            public void onFail(String error) {
                mvpMainView.showToast(error);
                mvpMainView.hidenLoading();
            }
        });
        httpUtils.sendGetHttp(mUrl, map);
    }

    private Phone parseModelWithOrgJson(String json) {
        Phone phone = new Phone();
        try {
            JSONObject jsonObject = new JSONObject(json);
            String value = jsonObject.getString("telString");
            phone.setTelString(value);

            value = jsonObject.getString("province");
            phone.setProvince(value);

            value = jsonObject.getString("catName");
            phone.setCatName(value);

            value = jsonObject.getString("carrier");
            phone.setCarrier(value);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return phone;
    }
    private Phone parseModelWithGson(String json){
        Gson gson = new Gson();
        Phone phone =  gson.fromJson(json, Phone.class);
        return phone;
    }
    private Phone parseModelWithFastJson(String json){
        Phone phone = com.alibaba.fastjson.JSONObject.parseObject(json, Phone.class);
        return phone;
    }
}
