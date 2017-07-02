package com.centa.aplusframework.contracts.base;

import android.content.Context;
import android.support.annotation.StringRes;

/**
 * Created by yanwenqiang on 2017/6/28.
 * <p>
 * 描述:MVP-View基类
 */
public interface BaseView {
    /**
     * 得到Activity的上下文
     *
     * @return 上下文
     */
    Context getContext();

    /**
     * Toast统一显示入口
     */
    void toast(@StringRes int id);

    /**
     * Toast统一显示入口
     */
    void toast(String text);
}
