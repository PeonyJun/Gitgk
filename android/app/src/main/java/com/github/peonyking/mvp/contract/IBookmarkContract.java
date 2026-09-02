package com.github.peonyking.mvp.contract;

import com.github.peonyking.mvp.contract.base.IBaseContract;
import com.github.peonyking.mvp.contract.base.IBaseListContract;
import com.github.peonyking.mvp.model.BookmarkExt;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/11/22 15:58:04
 */

public interface IBookmarkContract {

    interface View extends IBaseContract.View, IBaseListContract.View{
        void showBookmarks(ArrayList<BookmarkExt> bookmarks);
        void notifyItemAdded(int position);
    }

    interface Presenter extends IBaseContract.Presenter<IBookmarkContract.View>{
        void loadBookmarks(int page);
        void removeBookmark(int position);
        void undoRemoveBookmark();
    }

}
