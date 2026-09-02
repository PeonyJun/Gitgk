

package com.github.peonyking.mvp.presenter;

import androidx.annotation.NonNull;

import com.github.peonyking.AppData;
import com.github.peonyking.dao.AuthUser;
import com.github.peonyking.dao.AuthUserDao;
import com.github.peonyking.dao.DaoSession;
import com.github.peonyking.http.core.HttpObserver;
import com.github.peonyking.http.core.HttpResponse;
import com.github.peonyking.mvp.contract.ISplashContract;
import com.github.peonyking.mvp.model.User;
import com.github.peonyking.mvp.presenter.base.BasePresenter;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created on 2017/7/12.
 *
 * @author ThirtyDegreesRay
 */

public class SplashPresenter extends BasePresenter<ISplashContract.View>
        implements ISplashContract.Presenter{

    private final String TAG = "SplashPresenter";

    private AuthUser authUser;
    private boolean isMainPageShowwed = false;

    @Inject
    public SplashPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void getUser() {
        AuthUserDao authUserDao = daoSession.getAuthUserDao();

        List<AuthUser> users = authUserDao.queryBuilder()
                .where(AuthUserDao.Properties.Selected.eq(true))
                .limit(1)
                .list();

        AuthUser selectedUser = users != null && users.size() > 0 ? users.get(0) : null;

        //if none selected, choose first account
        if(selectedUser == null){
            List<AuthUser> firstAccount = authUserDao.queryBuilder()
                    .limit(1)
                    .list();
            selectedUser = firstAccount != null && firstAccount.size() > 0 ? firstAccount.get(0) : null;
        }

        if (selectedUser != null && isExpired(selectedUser)) {
            authUserDao.delete(selectedUser);
            selectedUser = null;
        }

        if (selectedUser != null) {
            AppData.INSTANCE.setAuthUser(selectedUser);
            getUserInfo(selectedUser.getAccessToken());
        } else {
            mView.showLoginPage();
        }

    }

    @Override
    public void saveAccessToken(String accessToken, String scope, int expireIn) {
        AuthUser authUser = new AuthUser();
        authUser.setSelected(true);
        authUser.setScope(scope);
        authUser.setExpireIn(expireIn);
        authUser.setAuthTime(new Date());
        authUser.setAccessToken(accessToken);
        daoSession.getAuthUserDao().insert(authUser);
        this.authUser = authUser;
    }

    private void getUserInfo(final String accessToken) {

        HttpObserver<User> httpObserver = new HttpObserver<User>() {
            @Override
            public void onError(@NonNull Throwable error) {
                daoSession.getAuthUserDao().delete(AppData.INSTANCE.getAuthUser());
                AppData.INSTANCE.setAuthUser(null);
                mView.showErrorToast(getErrorTip(error));
                mView.showLoginPage();
            }

            @Override
            public void onSuccess(@NonNull HttpResponse<User> response) {
                AppData.INSTANCE.setLoggedUser(response.body());
                if (authUser != null) {
                    authUser.setLoginId(response.body().getLogin());
                    daoSession.getAuthUserDao().update(authUser);
                }
                if(!isMainPageShowwed) {
                    isMainPageShowwed = true;
                    mView.showMainPage();
                }
            }
        };

        generalRxHttpExecute(new IObservableCreator<User>() {
            @Override
            public Observable<Response<User>> createObservable(boolean forceNetWork) {
                return getUserService().getPersonInfo(forceNetWork);
            }
        }, httpObserver, true);

    }

    private boolean isExpired(AuthUser selectedUser){
        return selectedUser.getAuthTime().getTime() + selectedUser.getExpireIn() * 1000
                < System.currentTimeMillis();
    }

}
