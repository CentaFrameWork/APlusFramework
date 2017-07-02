package com.centa.aplusframework.rx;

import com.centa.centacore.http.exception.ApiException;

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
        } else {
            error(new ApiException(1000, e.getMessage()));
        }
    }

    protected abstract void error(ApiException e);
}
