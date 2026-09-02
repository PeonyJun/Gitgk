package com.github.peonyking.ui.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;

import com.github.peonyking.R;
import com.github.peonyking.inject.component.AppComponent;
import com.github.peonyking.inject.component.DaggerFragmentComponent;
import com.github.peonyking.inject.module.FragmentModule;
import com.github.peonyking.mvp.contract.ILabelManageContract;
import com.github.peonyking.mvp.model.Label;
import com.github.peonyking.mvp.presenter.LabelManagePresenter;
import com.github.peonyking.ui.adapter.LabelManageAdapter;
import com.github.peonyking.ui.fragment.base.ListFragment;
import com.github.peonyking.ui.widget.EditLabelDialog;
import com.github.peonyking.util.BundleHelper;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2018/1/11 11:01:38
 */

public class LabelManageFragment extends ListFragment<LabelManagePresenter, LabelManageAdapter>
        implements ILabelManageContract.View, EditLabelDialog.EditLabelListener{

    public static LabelManageFragment create(@NonNull String owner, @NonNull String repo){
        LabelManageFragment fragment = new LabelManageFragment();
        fragment.setArguments(BundleHelper.builder().put("owner", owner).put("repo", repo).build());
        return fragment;
    }

    @Override
    protected void initFragment(Bundle savedInstanceState) {
        super.initFragment(savedInstanceState);
        setLoadMoreEnable(false);
        addVerticalDivider();
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
    protected void onReLoadData() {
        mPresenter.loadLabels(true);
    }

    @Override
    protected String getEmptyTip() {
        return null;
    }

    @Override
    public void showLabels(ArrayList<Label> labels) {
        adapter.setData(labels);
        postNotifyDataSetChanged();
    }

    @Override
    public void notifyItemInserted(int position) {
        adapter.notifyItemInserted(position);
    }

    @Override
    public void notifyItemRemoved(int position) {
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void notifyItemChanged(int position) {
        adapter.notifyItemChanged(position);
    }

    @Override
    public void onItemClick(int position, @NonNull View view) {
        super.onItemClick(position, view);
        new EditLabelDialog(getActivity(), this, adapter.getData().get(position)).show();
    }

    @Override
    public void onUpdateLabel(@NonNull Label oriLabel, @NonNull Label newLabel) {
        mPresenter.updateLabel(oriLabel, newLabel);
    }

    @Override
    public void onCreateLabel(@NonNull Label label) {
        mPresenter.createLabel(label);
    }

    @Override
    public void onDeleteLabel(@NonNull Label label) {
        mPresenter.deleteLabel(label);
    }

    public void onCreateLabelClick(){
        new EditLabelDialog(getActivity(), this).show();
    }
}
