package com.centa.aplusframework.presenters;

import com.centa.aplusframework.contracts.MainContract;
import com.centa.aplusframework.model.respdo.APlusRespDo;
import com.centa.aplusframework.model.respdo.PermUserInfoDo;
import com.centa.aplusframework.rx.APlusSubscriber;
import com.centa.centacore.http.exception.ApiException;
import com.centa.centacore.utils.WLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanwenqiang on 2017/6/28.
 * <p>
 * 描述:首页的Presenter
 */
public class MainPresenter extends MainContract.Presenter {
    public MainPresenter(MainContract.View view, MainContract.Model model) {
        super(view, model);
    }

    @Override
    public void login() {
        // TODO: 2017/7/2 原始方式
//        selfModel.userPermission(selfView.getStaffNo())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .compose(selfView.<APlusRespDo<ArrayList<PermUserInfoDo>>>bindUntilEvent(ActivityEvent.DESTROY))
//                .subscribe(new Subscriber<APlusRespDo<ArrayList<PermUserInfoDo>>>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(APlusRespDo<ArrayList<PermUserInfoDo>> permUserInfoDoAPlusRespDo) {
//                        List<PermUserInfoDo> permUserInfoDo = permUserInfoDoAPlusRespDo.getResult();
//                        PermUserInfoDo permUserInfoEntity = permUserInfoDo.get(0);
//                        String name = permUserInfoEntity.getIdentify().getUName();
//                        WLog.p("结果", name);
//                        selfView.showUser(name);
//                    }
//                });

        // TODO: 2017/6/28 亦可使用以下方式，改为Action1.
//        selfModel.userPermission(selfView.getStaffNo())
//                .compose(selfView.<ArrayList<PermUserInfoDo>>bindAPlusTransformer())
//                .subscribe(new Action1<APlusRespDo<ArrayList<PermUserInfoDo>>>() {
//                    @Override
//                    public void call(APlusRespDo<ArrayList<PermUserInfoDo>> arrayListAPlusRespDo) {
//                        List<PermUserInfoDo> permUserInfoDo = arrayListAPlusRespDo.getResult();
//                        PermUserInfoDo permUserInfoEntity = permUserInfoDo.get(0);
//                        String name = permUserInfoEntity.getIdentify().getUName();
//                        WLog.p("结果", name);
//                        selfView.showUser(name);
//                    }
//                });

        // TODO: 2017/7/2 A+ Api定制版本
        selfModel.userPermission(selfView.getStaffNo())
                .compose(selfView.<ArrayList<PermUserInfoDo>>bindAPlusTransformer())
                .subscribe(new APlusSubscriber<APlusRespDo<ArrayList<PermUserInfoDo>>>() {
                    @Override
                    protected void error(ApiException e) {
                        switch (e.code) {
                            case 1000:
                                // dosomething...
                                break;
                            default:
                                selfView.toast(e.message);
                                break;
                        }
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(APlusRespDo<ArrayList<PermUserInfoDo>> arrayListAPlusRespDo) {
                        List<PermUserInfoDo> permUserInfoDo = arrayListAPlusRespDo.getResult();
                        PermUserInfoDo permUserInfoEntity = permUserInfoDo.get(0);
                        String name = permUserInfoEntity.getIdentify().getUName();
                        WLog.p("结果", name);
                        selfView.showUser(name);
                    }
                });
    }
}
