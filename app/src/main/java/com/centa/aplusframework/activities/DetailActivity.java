package com.centa.aplusframework.activities;

import android.view.View;

import com.centa.aplusframework.R;
import com.centa.aplusframework.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yanwenqiang on 2017/7/7.
 * <p>
 * 描述:详情页面
 */
public class DetailActivity extends BaseActivity<Object> {
    @Override
    protected int layoutResId() {
        return R.layout.act_detail;
    }

    @Override
    protected void findViews() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {
        String title = getIntent().getExtras().getString(TITLE_ACTIVITY);
        snack(title);
        setUniversalToolbar(title);
    }

    @Override
    protected void initComplete() {

    }

    @OnClick({R.id.fab})
    public void fabOnClick(View view) {
        snack("你好，刘德华！");
    }

    @Override
    public void updateData(Object o) {

    }
}
