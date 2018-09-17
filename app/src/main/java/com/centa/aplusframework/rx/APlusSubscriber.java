package com.centa.aplusframework.rx;

import com.centa.centacore.http.exception.ApiException;

import retrofit2.HttpException;
import rx.Subscriber;

/**
 * Created by yanwenqiang on 2017/7/2.
 * <p>
 * 描述:A+ Api的订阅基类（统一处理error）
 */
public abstract class APlusSubscriber<T> extends Subscriber<T> {
    @Override
    public void onError(Throwable e) {
        if (e instanceof ApiException) {
            error((ApiException) e);
        } else if (e instanceof HttpException) {
            HttpException exception = (HttpException) e;
            int statusCode = exception.code();
            String message = exception.getMessage();
            if (502 == statusCode || 404 == statusCode) {
                message = "服务器异常，请稍后再试";
            } else if (504 == statusCode) {
                message = "网络不给力";
            }
            error(new ApiException(statusCode, message + "\n"));
        } else {
            error(new ApiException(1000, e.getMessage()));
        }
    }

    protected abstract void error(ApiException e);
}
