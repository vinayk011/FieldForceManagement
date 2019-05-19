package com.ffm.viewmodels;

import android.content.Context;

import com.ffm.db.paper.PaperConstants;
import com.ffm.db.paper.PaperDB;
import com.ffm.db.room.entity.Complaint;
import com.ffm.db.room.handlers.DataHandler;
import com.ffm.model.IssueHistory;
import com.ffm.network.NetworkError;
import com.ffm.network.NetworkListener;
import com.ffm.network.RestCall;
import com.ffm.preference.AppPrefConstants;
import com.ffm.preference.AppPreference;
import com.ffm.util.DateUtil;
import com.ffm.util.Trace;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class GetIssueHistory extends BaseAndroidViewModel<Integer, JsonArray, Integer, GetIssueHistory> {
    public GetIssueHistory(int errorCode) {
        super(false, errorCode);
    }

    @Override
    public GetIssueHistory run(Context context, Integer issueId) {
        restCall = new RestCall<>(context, true);
        restCall.execute(restServices.getIssueHistory(issueId), 2, new NetworkListener<JsonArray>() {
            @Override
            public void success(JsonArray jsonArray) {
                if (jsonArray != null) {
                    Trace.i("issue history: " + jsonArray.toString());
                    Type type = new TypeToken<List<IssueHistory>>() {
                    }.getType();
                    try {
                        ArrayList<IssueHistory> issueHistories = new Gson().fromJson(jsonArray, type);
                        TreeMap<Long, IssueHistory> issuesMap = new TreeMap<>();
                        for (IssueHistory issueHistory : issueHistories) {
                            if (issueHistory.getUpdatedDate() != null) {
                                issuesMap.put(DateUtil.convertStringToTimeInMills(issueHistory.getUpdatedDate()), issueHistory);
                            }
                        }
                        PaperDB.getInstance().write(PaperConstants.COMPLETED_ISSUE_DETAILS, issuesMap.entrySet());
                        Trace.i("IssueHistory from server: " + issuesMap.size());
                        for (long key : issuesMap.keySet()) {
                            Trace.i("i1: " + issuesMap.get(key).toString());
                        }
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

