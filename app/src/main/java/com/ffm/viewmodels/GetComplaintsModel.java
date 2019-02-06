package com.ffm.viewmodels;

import android.content.Context;
import android.os.Bundle;

import com.ffm.db.room.entity.Complaint;
import com.ffm.db.room.handlers.DataHandler;
import com.ffm.network.NetworkError;
import com.ffm.network.NetworkListener;
import com.ffm.network.RestCall;
import com.ffm.preference.AppPrefConstants;
import com.ffm.preference.AppPreference;
import com.ffm.util.Trace;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetComplaintsModel extends  BaseAndroidViewModel<Integer, JsonArray, String, GetComplaintsModel> {
    public GetComplaintsModel(int errorCode) {
        super(false, errorCode);
    }

    @Override
    public GetComplaintsModel run(Context context,  String statusType) {
        restCall = new RestCall<>(context, true);
        restCall.execute(restServices.getComplaintsByEmployee(AppPreference.getInstance().getString(AppPrefConstants.USER_ID), statusType), 2, new NetworkListener<JsonArray>() {
            @Override
            public void success(JsonArray jsonArray) {
                if (jsonArray != null) {
                    Type type = new TypeToken<List<Complaint>>() {
                    }.getType();
                    try {
                        Trace.i("Complaints from server: " +jsonArray.toString());
                        ArrayList<Complaint> complaints = new Gson().fromJson(jsonArray, type);
                        DataHandler.getInstance().addComplaintsToDb(complaints);
                        data.postValue(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    data.postValue(errorCode);
                }
            }

            @Override
            public void headers(Map<String, String> header) {

            }

            @Override
            public void fail(int code, List<NetworkError> networkErrors) {
                if (code == 404) {
                    data.postValue(0);
                } else {
                    data.postValue(errorCode);
                }
            }

            @Override
            public void failure() {
                data.postValue(errorCode);
            }
        });
        return this;
    }
}
