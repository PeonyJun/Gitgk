package com.github.peonyking.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.button.MaterialButton;
import com.github.peonyking.R;
import com.github.peonyking.inject.component.AppComponent;
import com.github.peonyking.inject.component.DaggerActivityComponent;
import com.github.peonyking.inject.module.ActivityModule;
import com.github.peonyking.mvp.contract.ILoginContract;
import com.github.peonyking.mvp.model.BasicToken;
import com.github.peonyking.mvp.presenter.LoginPresenter;
import com.github.peonyking.ui.activity.base.BaseActivity;
import com.github.peonyking.util.AppOpener;
import com.github.peonyking.util.CrashHandler;
import com.github.peonyking.util.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

/**
 * Created on 2017/7/12.
 *
 * @author ThirtyDegreesRay
 */

public class LoginActivity extends BaseActivity<LoginPresenter>
        implements ILoginContract.View {

    private final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.login_bn) MaterialButton loginBn;
    @BindView(R.id.token_et) TextInputEditText tokenEt;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mPresenter.handleOauth(intent);
        setIntent(null);
    }

    @Override
    public void onGetTokenSuccess(BasicToken basicToken) {
        mPresenter.getUserInfo(basicToken);
    }

    @Override
    public void onGetTokenError(String errorMsg) {
        if (StringUtils.isBlank(errorMsg)) {
            errorMsg = getString(R.string.auth_error);
        }
        Toasty.error(getApplicationContext(), errorMsg).show();
    }

    @Override
    public void onLoginComplete() {
        delayFinish();
        startActivity(new Intent(getActivity(), MainActivity.class));
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerActivityComponent.builder()
                .appComponent(appComponent)
                .activityModule(new ActivityModule(getActivity()))
                .build()
                .inject(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_login_new;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        CrashHandler.checkAndShowPendingReport(getActivity());
    }

    @OnClick(R.id.login_bn)
    public void onOauthLoginClick(){
        AppOpener.openInCustomTabsOrBrowser(getActivity(), mPresenter.getOAuth2Url());
    }

    @OnClick(R.id.token_login_bn)
    public void onTokenLoginClick(){
        String token = tokenEt.getText() == null ? "" : tokenEt.getText().toString();
        if (StringUtils.isBlank(token)) {
            Toasty.error(getApplicationContext(), getString(R.string.token_cannot_empty)).show();
            return;
        }
        tokenEt.setEnabled(false);
        loginBn.setEnabled(false);
        mPresenter.tokenLogin(token);
    }

}