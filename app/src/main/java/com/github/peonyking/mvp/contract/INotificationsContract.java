

package com.github.peonyking.mvp.contract;

import androidx.annotation.NonNull;

import com.github.peonyking.mvp.contract.base.IBaseContract;
import com.github.peonyking.mvp.contract.base.IBaseListContract;
import com.github.peonyking.mvp.contract.base.IBasePagerContract;
import com.github.peonyking.mvp.model.Notification;
import com.github.peonyking.mvp.model.Repository;
import com.github.peonyking.ui.adapter.base.DoubleTypesModel;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/11/6 17:44:57
 */

public interface INotificationsContract {

    interface View extends IBaseContract.View, IBasePagerContract.View, IBaseListContract.View{
        void showNotifications(ArrayList<DoubleTypesModel<Repository, Notification>> notifications);
    }

    interface Presenter extends IBasePagerContract.Presenter<INotificationsContract.View> {
        void loadNotifications(int page, boolean isReload);
        void markNotificationAsRead(String threadId);
        void markAllNotificationsAsRead();
        boolean isNotificationsAllRead();
        void markRepoNotificationsAsRead(@NonNull Repository repository);
    }

}
