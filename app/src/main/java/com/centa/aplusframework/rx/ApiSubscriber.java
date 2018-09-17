package com.centa.aplusframework.rx;

import com.centa.centacore.http.exception.ApiException;

public abstract class ApiSubscriber<T> extends APlusSubscriber<T> {
    @Override
    protected void error(ApiException e) {
        onResult(false, null, e.message);
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onNext(T t) {
        onResult(true, t, "");
    }

    /**
     *
     * @param isResult 是否是接口正常返回的结果，true(状态码：200，返回tf数据) false(网络 或者 其他异常导致 返回 msg数据);
     * @param tf
     * @param msg
     */
    public abstract void onResult (boolean isResult, T tf, String msg);
}
