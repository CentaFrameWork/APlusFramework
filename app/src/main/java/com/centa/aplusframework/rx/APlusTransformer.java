package com.centa.aplusframework.rx;

import com.centa.aplusframework.model.respdo.APlusRespDo;
import com.centa.centacore.http.exception.ApiException;
import com.trello.rxlifecycle.LifecycleTransformer;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by yanwenqiang on 2017/7/2.
 * <p>
 * 描述:A+ Api统一的数据转换策略
 */
public class APlusTransformer<T> implements
        Observable.Transformer<APlusRespDo<T>, T> {

    private final LifecycleTransformer<T> mLifecycleTransformer;

    public APlusTransformer(LifecycleTransformer<T> lifecycleTransformer) {
        mLifecycleTransformer = lifecycleTransformer;
    }

    @Override
    public Observable<T> call(Observable<APlusRespDo<T>> observable) {
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<APlusRespDo<T>, T>() {
                    @Override
                    public T call(APlusRespDo<T> taPlusRespDo) {
//                        taPlusRespDo.getResult();
                        if (!taPlusRespDo.isFlag()) {
                            // TODO: 2017/7/2 这里暂时用500（服务器内部错误）因为A+的Api将返回code给屏蔽了
                            // 将来有code的时候，可以直接改成真正的response code
                            throw new ApiException(500,
                                    taPlusRespDo.getErrorMsg());
                        }
                        return taPlusRespDo.getResult();
                    }
                })
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends T>>() {
                    @Override
                    public Observable<? extends T> call(Throwable throwable) {
                        throwable.printStackTrace();
                        return Observable.error(throwable);
                    }
                })
                .compose(mLifecycleTransformer);

    }
}
