package com.centa.aplusframework.api;

import com.centa.centacore.http.okhttpclient.OkHttpClient4Api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by yanwenqiang on 17/6/23.
 * <p>
 * 描述:创建api
 */
public final class ApiCreator {

    private final OkHttpClient mOkHttpClient;

    private ApiCreator() {
        // Utility Class

        mOkHttpClient = OkHttpClient4Api.getInstance().getOkHttpClient();
    }


    /**
     * app版本更新
     */
    public static PermissionApi userPermission() {
        return retrofit("http://gzapi.centaline.com.cn:8080/api/").create(PermissionApi.class);
    }

    /**
     * 获取 Retrofit
     *
     * @param baseUrl host地址
     * @return Retrofit
     */
    private static Retrofit retrofit(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(OkHttpClient4Api.getInstance().getOkHttpClient())
                .build();

    }
}
