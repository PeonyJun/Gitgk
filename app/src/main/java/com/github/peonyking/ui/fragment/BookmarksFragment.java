package com.github.peonyking.ui.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.View;

import com.github.peonyking.R;
import com.github.peonyking.inject.component.AppComponent;
import com.github.peonyking.inject.component.DaggerFragmentComponent;
import com.github.peonyking.inject.module.FragmentModule;
import com.github.peonyking.mvp.contract.IBookmarkContract;
import com.github.peonyking.mvp.model.BookmarkExt;
import com.github.peonyking.mvp.presenter.BookmarkPresenter;
import com.github.peonyking.ui.activity.ProfileActivity;
import com.github.peonyking.ui.activity.RepositoryActivity;
import com.github.peonyking.ui.adapter.BookmarksAdapter;
import com.github.peonyking.ui.adapter.base.ItemTouchHelperCallback;
import com.github.peonyking.ui.fragment.base.ListFragment;
import com.github.peonyking.util.PrefUtils;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/11/22 16:29:20
 */

public class BookmarksFragment extends ListFragment<BookmarkPresenter, BookmarksAdapter>
        implements IBookmarkContract.View, ItemTouchHelperCallback.ItemGestureListener {

    public static BookmarksFragment create(){
        return new BookmarksFragment();
    }

    private ItemTouchHelper itemTouchHelper;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list;
    }

    @Override
    protected void setupFragmentComponent(AppComponent appComponent) {
        DaggerFragmentComponent.builder()
                .appComponent(appComponent)
                .fragmentModule(new FragmentModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initFragment(Bundle savedInstanceState) {
        super.initFragment(savedInstanceState);
        setLoadMoreEnable(true);
        ItemTouchHelperCallback callback = new ItemTouchHelperCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onReLoadData() {
        mPresenter.loadBookmarks(1);
    }

    @Override
    protected String getEmptyTip() {
        return getString(R.string.no_bookmarks);
    }

    @Override
    public void onItemClick(int position, @NonNull View view) {
        super.onItemClick(position, view);
        BookmarkExt bookmark = adapter.getData().get(position);
        if("user".equals(bookmark.getType())){
            View userAvatar = view.findViewById(R.id.avatar);
            ProfileActivity.show(getActivity(), userAvatar, bookmark.getUser().getLogin(),
                    bookmark.getUser().getAvatarUrl());
        } else {
            RepositoryActivity.show(getActivity(), bookmark.getRepository().getOwner().getLogin(),
                    bookmark.getRepository().getName());
        }
    }

    @Override
    protected void onLoadMore(int page) {
        super.onLoadMore(page);
        mPresenter.loadBookmarks(page);
    }

    @Override
    public void showBookmarks(ArrayList<BookmarkExt> bookmarks) {
        adapter.setData(bookmarks);
        postNotifyDataSetChanged();

        if(bookmarks != null && bookmarks.size() > 0 && PrefUtils.isBookmarksTipAble()){
            showOperationTip(R.string.bookmarks_tip);
            PrefUtils.set(PrefUtils.BOOKMARKS_TIP_ABLE, false);
        }
    }

    @Override
    public void notifyItemAdded(int position) {
        if(adapter.getData().size() == 1){
            postNotifyDataSetChanged();
        } else {
            adapter.notifyItemInserted(position);
        }
    }

    @Override
    public boolean onItemMoved(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemSwiped(int position, int direction) {
        mPresenter.removeBookmark(position);
        if(adapter.getData().size() == 0){
            postNotifyDataSetChanged();
        } else {
            adapter.notifyItemRemoved(position);
        }
        Snackbar.make(recyclerView, R.string.bookmark_removed, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo, v -> mPresenter.undoRemoveBookmark() )
                .show();
    }

}
