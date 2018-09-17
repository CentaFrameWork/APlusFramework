package com.centa.aplusframework.base;

import android.content.Context;
import android.support.annotation.StringRes;

import com.centa.aplusframework.contracts.base.BaseView;
import com.centa.aplusframework.rx.APlusTransformer;
import com.centa.centacore.base.AbsFragment;
import com.trello.rxlifecycle.android.FragmentEvent;

/**
 * Created by yanwenqiang on 2017/7/2.
 * <p>
 * 描述:BaseFragment
 */
public abstract class BaseFragment<E> extends AbsFragment implements BaseView<E> {

    /**
     * 这个看似无价值，请不要删除，为P层getContext做引用
     * @return
     */
    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void toast(@StringRes int id) {
        super.toast(id);
    }

    @Override
    public void toast(String text) {
        super.toast(text);
    }

    @Override
    public void cancelLoadingDialog() {
        super.cancelLoadingDialog();
    }

    /**
     * 处理 A+ Api 错误对象整理为统一的ApiException<p>
     * 绑定生命周期<p>
     * 约束transfer回不同状态下指定完整实体
     * 默认在BaseFragment中实现，P层也调用这里
     */
    public final <T> APlusTransformer<T> bindAPlusTransformer() {
        return new APlusTransformer<>(this.<T>bindUntilEvent(FragmentEvent.DESTROY));
    }
}
