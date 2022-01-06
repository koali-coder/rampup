package com.example.demo.utils;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;

/**
 * @author zhouyw
 * @date 2021-11-24
 */
@Slf4j
public class OKHttpUtil {

    /**
     * get请求
     * @param url
     * @return
     */
    public static String httpGet(String url) {
        log.info("http  get  url --> {}", url);
        String result = null;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.debug("http  get  result --> {}", result);
        return result;
    }

    /**
     * post请求
     * @param url
     * @param data  提交的参数为key=value&key1=value1的形式
     */
    public static String httpPost(String url, Object data) {
        log.info("http  post  url --> {}   data --> {}", url, new Gson().toJson(data));
        String result = null;
        OkHttpClient httpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(data));
        Request request = new Request.Builder().url(url)
                .addHeader("Content-type", "application/json")
                .post(requestBody).build();
        try {
            Response response = httpClient.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.debug("http  post  result --> {}", result);
        return result;
    }
}
