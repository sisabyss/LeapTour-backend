package org.example.LeapTour.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * API调用工具类
 * 目前未用到
 * v1版本的API接口对外部API进行了调用
 * 但目前v1版本API接口已废用 因此该工具类也没有被使用
 */
public class OkHttpApi {
    OkHttpClient client = new OkHttpClient();

    public String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}
