package com.github.peonyking.mvp.contract;

import com.github.peonyking.mvp.contract.base.IBaseContract;
import com.github.peonyking.mvp.model.Release;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/9/16 11:05:13
 */

public interface IReleasesContract {

    interface View extends IBaseContract.View{
        void showReleases(ArrayList<Release> releases);
        void setCanLoadMore(boolean canLoadMore);
        void showLoadError(String error);
    }

    interface Presenter extends IBaseContract.Presenter<IReleasesContract.View>{
        void loadReleases(int page, boolean isReload);
    }

}
