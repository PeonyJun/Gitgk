

package com.github.peonyking.mvp.contract;

import com.github.peonyking.mvp.contract.base.IBaseContract;

/**
 * Created on 2017/8/1.
 *
 * @author ThirtyDegreesRay
 */

public interface ISettingsContract {

    interface View extends IBaseContract.View{

    }

    interface Presenter extends IBaseContract.Presenter<ISettingsContract.View>{

        void logout();

    }

}
