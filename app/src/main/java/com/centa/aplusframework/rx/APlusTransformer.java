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
        Observable.Transformer<APlusRespDo<T>, APlusRespDo<T>> {

    private final LifecycleTransformer<APlusRespDo<T>> mLifecycleTransformer;

    public APlusTransformer(LifecycleTransformer<APlusRespDo<T>> lifecycleTransformer) {
        mLifecycleTransformer = lifecycleTransformer;
    }

    @Override
    public Observable<APlusRespDo<T>> call(Observable<APlusRespDo<T>> observable) {
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<APlusRespDo<T>, APlusRespDo<T>>() {
                    @Override
                    public APlusRespDo<T> call(APlusRespDo<T> aPlusRespDo) {
                        if (!aPlusRespDo.isFlag()) {
                            // TODO: 2017/7/2 这里暂时用500（服务器内部错误）因为A+的Api将返回code给屏蔽了
                            // 将来有code的时候，可以直接改成真正的response code
                            throw new ApiException(500,
                                    aPlusRespDo.getErrorMsg());
                        }
                        return aPlusRespDo;
                    }
                })
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends APlusRespDo<T>>>() {
                    @Override
                    public Observable<? extends APlusRespDo<T>> call(Throwable throwable) {
                        throwable.printStackTrace();
                        return Observable.error(throwable);
                    }
                })
                .compose(mLifecycleTransformer);

    }
}
