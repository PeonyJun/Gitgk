

package com.github.peonyking.mvp.contract;

import androidx.annotation.NonNull;

import com.github.peonyking.mvp.contract.base.IBaseContract;
import com.github.peonyking.mvp.model.SearchModel;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/8/25 14:55:29
 */

public interface ISearchContract {

    interface View extends IBaseContract.View{
        void showSearches(ArrayList<SearchModel> searchModels);
    }

    interface Presenter extends IBaseContract.Presenter<ISearchContract.View>{
        ArrayList<SearchModel> getQueryModels(@NonNull String query);
        SearchModel getSortModel(int page, int sortId);
        @NonNull ArrayList<String> getSearchRecordList();
        void addSearchRecord(@NonNull String record);
    }

}
