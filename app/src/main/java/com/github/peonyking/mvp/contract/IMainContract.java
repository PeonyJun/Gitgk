

package com.github.peonyking.mvp.contract;

import androidx.annotation.NonNull;

import com.github.peonyking.dao.AuthUser;
import com.github.peonyking.mvp.contract.base.IBaseContract;

import java.util.List;

/**
 * Created on 2017/7/18.
 *
 * @author ThirtyDegreesRay
 */

public interface IMainContract {

    interface View extends IBaseContract.View{
        void restartApp();
    }

    interface Presenter extends IBaseContract.Presenter<IMainContract.View>{
        boolean isFirstUseAndNoNewsUser();
        List<AuthUser> getLoggedUserList();
        void toggleAccount(@NonNull String loginId);
        void logout();
    }

}
