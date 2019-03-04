package com.ffm.viewmodels;

import android.content.Context;
import android.os.Bundle;

import com.ffm.network.NetworkError;
import com.ffm.network.NetworkListener;
import com.ffm.network.RestCall;
import com.ffm.preference.AppPrefConstants;
import com.ffm.preference.AppPreference;
import com.ffm.util.Trace;
import com.ffm.viewmodels.request.ComplaintStatus;
import com.google.gson.JsonArray;

import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UpdateIssueDetailsModel extends BaseAndroidViewModel<Integer, Void, ComplaintStatus, UpdateIssueDetailsModel> {
    public UpdateIssueDetailsModel(int errorCode) {
        super(false, errorCode);
    }

    @Override
    public UpdateIssueDetailsModel run(Context context, ComplaintStatus complaintStatus) {
//        MultipartBody.Part part = null;
        RequestBody file= null;
        if (complaintStatus.getImagePath() != null) {
            file = RequestBody.create(MediaType.parse("image/*"), new File(complaintStatus.getImagePath()));
//            part = MultipartBody.Part.createFormData("uploadedFile", complaintStatus.getImagePath(), requestBody);
        }
        MediaType text = MediaType.parse("text/plain");
        RequestBody issueID = RequestBody.create(text, String.valueOf(complaintStatus.getIssueId()));
        RequestBody employeeID = RequestBody.create(text, complaintStatus.getEmployeeId());
        RequestBody issueStatus = RequestBody.create(text, complaintStatus.getIssueStatus());
        RequestBody latitude = RequestBody.create(text, String.valueOf(complaintStatus.getLocationInfo().getLatitude()));
        RequestBody longitude = RequestBody.create(text, String.valueOf(complaintStatus.getLocationInfo().getLongitude()));
        //RequestBody file = RequestBody.create(MediaType.parse("image/*"), new File(complaintStatus.getImagePath()));
        restCall = new RestCall<>(context, true);
        restCall.execute(restServices.updateIssueDetails(issueID, employeeID, issueStatus, latitude, longitude, file), 2, new NetworkListener<Void>() {
            @Override
            public void success(Void response) {
                //Trace.i("Response:" + response);
                data.postValue(0);
            }

            @Override
            public void headers(Map<String, String> header) {

            }

            @Override
            public void fail(int code, List<NetworkError> networkErrors) {
                Trace.i("" + code);
                if (code == 404) {
                    data.postValue(0);
                } else {
                    data.postValue(errorCode);
                }
            }

            @Override
            public void failure() {
                Trace.i("");
                data.postValue(errorCode);
            }
        });
        return this;
    }
}
