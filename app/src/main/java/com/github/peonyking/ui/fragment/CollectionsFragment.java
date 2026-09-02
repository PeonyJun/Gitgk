package com.github.peonyking.ui.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.View;

import com.github.peonyking.R;
import com.github.peonyking.inject.component.AppComponent;
import com.github.peonyking.inject.component.DaggerFragmentComponent;
import com.github.peonyking.inject.module.FragmentModule;
import com.github.peonyking.mvp.contract.ICollectionsContract;
import com.github.peonyking.mvp.model.Collection;
import com.github.peonyking.mvp.presenter.CollectionsPresenter;
import com.github.peonyking.ui.activity.RepoListActivity;
import com.github.peonyking.ui.adapter.CollectionAdapter;
import com.github.peonyking.ui.fragment.base.ListFragment;
import com.github.peonyking.util.PrefUtils;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/12/25 15:17:38
 */

public class CollectionsFragment extends ListFragment<CollectionsPresenter, CollectionAdapter>
        implements ICollectionsContract.View {

    public static Fragment create(){
        return new CollectionsFragment();
    }

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
        setLoadMoreEnable(false);
    }

    @Override
    protected void onReLoadData() {
        mPresenter.loadCollections(true);
    }

    @Override
    protected String getEmptyTip() {
        return getString(R.string.no_repo_collections);
    }

    @Override
    public void showCollections(ArrayList<Collection> collections) {
        adapter.setData(collections);
        postNotifyDataSetChanged();
        if(collections != null && collections.size() > 0 && PrefUtils.isCollectionsTipAble()){
            showOperationTip(R.string.collections_tip);
            PrefUtils.set(PrefUtils.COLLECTIONS_TIP_ABLE, false);
        }
    }

    @Override
    public void onItemClick(int position, @NonNull View view) {
        super.onItemClick(position, view);
        RepoListActivity.showCollection(getActivity(), adapter.getData().get(position));
    }
}
