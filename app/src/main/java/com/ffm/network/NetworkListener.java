package com.ffm.network;

import java.util.List;
import java.util.Map;



public interface NetworkListener<T> {

    void success(T t);

    void headers(Map<String, String> header);

    void fail(int code, List<NetworkError> networkErrors);

    void failure();
}
