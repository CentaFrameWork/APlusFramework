package com.centa.aplusframework.base;

import android.content.Context;

import com.centa.aplusframework.contracts.base.BaseView;
import com.centa.aplusframework.model.respdo.APlusRespDo;
import com.centa.aplusframework.rx.APlusTransformer;
import com.centa.centacore.base.AbsActivity;
import com.trello.rxlifecycle.android.ActivityEvent;

/**
 * Created by yanwenqiang on 2017/6/26.
 * <p>
 * 描述:业务层Activity基类
 */
public abstract class BaseActivity extends AbsActivity implements BaseView {
    // TODO: 2017/6/26 计划加入框架的内容
    // 1.热更新    ----------------------------->待认领
    // 2.EventBus   --------------------------->待认领
    // 3.添加常用的HttpTransformer   ----------->ok
    // 4.MVP项目结构    ------------------------>ok
    // 5.FakeInterceptor 的完善    ------------->燕文强
    // 6.WebViewJavaScriptBridge   ------------>待认领
    // 7.Litepal gradle 方式引入最新版本   ------>ok


    /**
     * 这个看似无价值，请不要删除，为P层getContext做引用
     * @return
     */
    @Override
    public Context getContext() {
        return getContext();
    }

    /**
     * 处理 A+ Api 错误对象整理为统一的ApiException<p>
     * 绑定生命周期<p>
     * 约束transfer回不同状态下指定完整实体
     * 默认在BaseActivity中实现，P层也调用这里
     */
    public final <T> APlusTransformer<T> bindAPlusTransformer() {
        return new APlusTransformer<>(this.<APlusRespDo<T>>bindUntilEvent(ActivityEvent.DESTROY));
    }
}
