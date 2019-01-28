package com.ffm.network;

import com.google.gson.JsonArray;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;



public interface RestServices {

    /*@POST("/cattle/login")
    Call<Void> signIn(@Body SignInRequest signInRequest);

    @POST("/cattle/login")
    Call<Void> renew(@Body SignInRequest signInRequest);

    @GET("/cattle/device/user")
    Call<JsonArray> getDevices();

    @GET("/cattle/device")
    Call<JsonArray> getUnmappedDevices();


    @POST("/cattle/device/user")
    Call<Void> addDevices(@Body List<Device> devices);

    @GET("/cattle/device/summary/{device}")
    Call<JsonArray> getDeviceStats(@Path("device") String device, @Query("date") String date);*/
}