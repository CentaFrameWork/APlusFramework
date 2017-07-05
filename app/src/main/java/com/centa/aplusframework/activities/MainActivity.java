package com.centa.aplusframework.activities;

import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.centa.aplusframework.R;
import com.centa.aplusframework.base.BaseActivity;
import com.centa.aplusframework.contracts.MainContract;
import com.centa.aplusframework.presenters.MainPresenter;
import com.centa.aplusframework.repository.MainModel;
import com.centa.centacore.interfaces.ISingleRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A login screen that offers login via email/password.
 */
public class MainActivity extends BaseActivity implements ISingleRequest, MainContract.View {
    // UI references.
    @BindView(R.id.actv_account)
    AutoCompleteTextView mAccountView;
    @BindView(R.id.et_password)
    EditText mPasswordView;
    @BindView(R.id.btn_sign_in)
    Button mSignInBtn;
    @BindView(R.id.btn_eventbus)
    Button mEventbusBtn;

    private MainContract.Presenter presenter;

    @Override
    protected int layoutResId() {
        return R.layout.activity_login;
    }

    @Override
    protected void findViews() {
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initViews() {
        presenter = new MainPresenter(this, new MainModel());
    }


    @OnClick({R.id.btn_sign_in, R.id.btn_eventbus})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //  登录点击事件
            case R.id.btn_sign_in:
                loadingDialog("我的数据已提交，请耐心等待...");
                mSignInBtn.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        presenter.login();
                    }
                }, 1000);
                break;
            //  测试EventBus
            case R.id.btn_eventbus:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        presenter.testEventBus();
                    }
                }).start();
                break;
        }
    }


    @Override
    protected void initComplete() {

    }


    @Override
    public String getStaffNo() {
        String account = mAccountView.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            return "Ceshigzywq";
        } else {
            return account;
        }
        
    }

    @Override
    public String getPwd() {
        String pwd = mPasswordView.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            return "111222";
        } else {
            return pwd;
        }
    }

    @Override
    public void showUser(String userName) {
        cancelLoadingDialog();
        toast(userName);
    }


    @Override
    public void request() {
        presenter.login();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showEventBusInfo(String info) {
        toast(info);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}