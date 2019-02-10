package com.ffm.viewmodels;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ffm.db.paper.PaperDB;
import com.ffm.network.NetworkError;
import com.ffm.network.NetworkListener;
import com.ffm.network.RestCall;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;


public class GetCustomerLocationImageModel extends BaseAndroidViewModel<Integer, ResponseBody, String, GetCustomerLocationImageModel> {
    public GetCustomerLocationImageModel(int errorCode) {
        super(true, "image/jpeg");
        this.errorCode = errorCode;
    }

    @Override
    public GetCustomerLocationImageModel run(final Context context, String imagePath) {
        restCall = new RestCall<>(context, true);
        restCall.execute(restServices.getImage(imagePath), 3,
                new NetworkListener<ResponseBody>() {
                    @Override
                    public void success(ResponseBody stream) {
                        Bitmap bitmap = BitmapFactory.decodeStream(stream.byteStream());
                        if (bitmap != null) {
                            PaperDB.getInstance().saveImageBitmap(bitmap);
                        }
                        data.postValue(0);
                    }

                    @Override
                    public void headers(Map<String, String> header) {

                    }

                    @Override
                    public void fail(int code, List<NetworkError> networkErrors) {
                        data.postValue(errorCode);
                    }

                    @Override
                    public void failure() {
                        data.postValue(errorCode);
                    }
                });
        return this;
    }
}