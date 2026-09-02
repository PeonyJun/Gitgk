

package com.github.peonyking.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.github.peonyking.inject.component.AppComponent;
import com.github.peonyking.inject.component.DaggerActivityComponent;
import com.github.peonyking.inject.module.ActivityModule;
import com.github.peonyking.mvp.contract.ISplashContract;
import com.github.peonyking.mvp.presenter.SplashPresenter;
import com.github.peonyking.ui.activity.base.BaseActivity;

/**
 * Created on 2017/7/12.
 *
 * @author ThirtyDegreesRay
 */
public class SplashActivity extends BaseActivity<SplashPresenter> implements ISplashContract.View {

    private final String TAG = "SplashActivity";

//    private final int REQUEST_ACCESS_TOKEN = 1;

    @Override
    public void showMainPage() {
        delayFinish();
        Uri dataUri = getIntent().getData();
        if (dataUri == null) {
            startActivity(new Intent(getActivity(), MainActivity.class));
        } else {
            BrowserFilterActivity.handleBrowserUri(getActivity(), dataUri);
        }
    }

    @Override
    public void showLoginPage() {
        delayFinish();
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    /**
     * 依赖注入的入口
     *
     * @param appComponent appComponent
     */
    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerActivityComponent.builder()
                .appComponent(appComponent)
                .activityModule(new ActivityModule(getActivity()))
                .build()
                .inject(this);
    }

    /**
     * 获取ContentView id
     *
     * @return
     */
    @Override
    protected int getContentView() {
        return 0;
    }

    /**
     * 初始化activity
     */
    @Override
    protected void initActivity() {
        super.initActivity();
        mPresenter.getUser();
    }

    /**
     * 初始化view
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
    }

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
//            case REQUEST_ACCESS_TOKEN:
//                if(resultCode == RESULT_OK){
//                    showMainPage();
//                }
//                break;
            default:
                break;
        }
    }

}
