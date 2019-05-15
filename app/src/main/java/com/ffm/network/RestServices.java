package com.ffm.network;

import com.ffm.viewmodels.request.ComplaintStatus;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.internal.http.RetryAndFollowUpInterceptor;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface RestServices {

    //    @POST("/cattle/login")
//    Call<Void> signIn(@Body SignInRequest signInRequest);
//
//    @POST("/cattle/login")
//    Call<Void> renew(@Body SignInRequest signInRequest);
//
    @GET("resources/fieldforce/GetAssignedComplaintsByEmployee")
    Call<JsonArray> getComplaintsByEmployee(@Query("employeeID") String employeeID, @Query("statusType") String statusType);

    @Multipart
    @POST("resources/fieldforce/UpdateEmployeeStatus")
    Call<Void> updateIssueDetails(@Part("issueID") RequestBody issueID,
                                  @Part("employeeID") RequestBody employeeID,
                                  @Part("issueStatus") RequestBody issueStatus,
                                  @Part("latitude") RequestBody latitude,
                                  @Part("longitude") RequestBody longitude,
                                  @Part ("file") RequestBody file);

    @GET("{path}")
    Call<ResponseBody> getImage(@Path(value = "path", encoded = true) String path);

    @GET("resources/fieldforce/GetIssueHistory")
    Call<JsonArray> getIssueHistory(@Query("issueID") int issueID);
//
//    @GET("/cattle/device")
//    Call<JsonArray> getUnmappedDevices();
//
//
//    @POST("/cattle/device/user")
//    Call<Void> addDevices(@Body List<Device> devices);
//
//    @GET("/cattle/device/summary/{device}")
//    Call<JsonArray> getDeviceStats(@Path("device") String device, @Query("date") String date);
}