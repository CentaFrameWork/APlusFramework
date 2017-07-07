package com.centa.aplusframework.base;

import android.content.Context;
import android.support.annotation.StringRes;

import com.centa.aplusframework.contracts.base.BaseView;
import com.centa.aplusframework.model.respdo.APlusRespDo;
import com.centa.aplusframework.rx.APlusTransformer;
import com.centa.centacore.base.AbsActivity;
import com.centa.centacore.http.exception.ApiException;
import com.centa.centacore.utils.WLog;
import com.trello.rxlifecycle.android.ActivityEvent;

/**
 * Created by yanwenqiang on 2017/6/26.
 * Updated by yanwenqiang on 2017/7/2. 添加框架计划.】
 * <p>
 * 描述:业务层Activity基类
 */
public abstract class BaseActivity extends AbsActivity implements BaseView {
    // TODO: 2017/6/26 计划加入框架的内容
    // 1.热更新    ----------------------------->待认领
    // 2.EventBus   --------------------------->【ok】
    // 3.添加常用的HttpTransformer   ----------->【ok】
    // 4.MVP项目结构    ------------------------>【ok】
    // 5.FakeInterceptor 的完善    ------------->燕文强
    // 6.WebViewJavaScriptBridge   ------------>待认领
    // 7.Litepal gradle 方式引入最新版本   ------>【ok】
    // 8.极光推送（配置、gradle）    ------------>待认领
    // 9.百度地图（gradle）   ------------------>待认领
    // 10.图片选择（包括裁剪）    --------------->待认领
    // 11.每次切换环境修改大量极光工程名称问题    -->待认领
    // 12.BottomLayout

    /**
     * 这个看似无价值，请不要删除，为P层getContext做引用
     *
     * @return
     */
    @Override
    public Context getContext() {
        return getBaseContext();
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
     * A+ Api错误
     */
    public void apiError(ApiException apiException) {
        switch (apiException.code) {
            case 1000:
                WLog.e("native onNext error", apiException, 5);
                break;
            default:
                toast(apiException.message);
                break;
        }
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
