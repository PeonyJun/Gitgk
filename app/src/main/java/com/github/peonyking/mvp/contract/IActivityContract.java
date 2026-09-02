

package com.github.peonyking.mvp.contract;

import androidx.annotation.NonNull;

import com.github.peonyking.mvp.contract.base.IBaseContract;
import com.github.peonyking.mvp.contract.base.IBaseListContract;
import com.github.peonyking.mvp.contract.base.IBasePagerContract;
import com.github.peonyking.mvp.model.ActivityRedirectionModel;
import com.github.peonyking.mvp.model.Event;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/8/23 21:51:44
 */

public interface IActivityContract {

    interface View extends IBaseContract.View, IBasePagerContract.View, IBaseListContract.View {
        void showEvents(ArrayList<Event> events);
    }

    interface Presenter extends IBasePagerContract.Presenter<IActivityContract.View>{
        void loadEvents(boolean isReload, int page);
        ArrayList<ActivityRedirectionModel> getRedirectionList(@NonNull Event event);
    }

}
