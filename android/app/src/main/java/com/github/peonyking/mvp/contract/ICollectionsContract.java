package com.github.peonyking.mvp.contract;

import com.github.peonyking.mvp.contract.base.IBaseContract;
import com.github.peonyking.mvp.contract.base.IBaseListContract;
import com.github.peonyking.mvp.model.Collection;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/12/25 15:12:30
 */

public interface ICollectionsContract {

    interface View extends IBaseContract.View, IBaseListContract.View{
        void showCollections(ArrayList<Collection> collections);
    }

    interface Presenter extends IBaseContract.Presenter<ICollectionsContract.View>{
        void loadCollections(boolean isReload);
    }

}
