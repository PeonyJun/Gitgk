package com.github.peonyking.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.View;

import com.github.peonyking.R;
import com.github.peonyking.inject.component.AppComponent;
import com.github.peonyking.ui.activity.base.PagerActivity;
import com.github.peonyking.ui.adapter.base.FragmentPagerModel;
import com.github.peonyking.ui.adapter.base.FragmentViewPagerAdapter;
import com.github.peonyking.ui.fragment.RepositoriesFragment;
import com.github.peonyking.ui.fragment.UserListFragment;

/**
 * Created by ThirtyDegreesRay on 2017/11/13 11:35:50
 */

public class TraceActivity extends PagerActivity {

    public static void show(@NonNull Activity activity){
        Intent intent = new Intent(activity, TraceActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void initActivity() {
        super.initActivity();
        pagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager());
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_view_pager;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setToolbarScrollAble(true);
        setToolbarBackEnable();
        setToolbarTitle(getString(R.string.trace));
        pagerAdapter.setPagerList(FragmentPagerModel.createTracePagerList(getActivity(), getFragments()));
        tabLayout.setVisibility(View.VISIBLE);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(pagerAdapter);
        showFirstPager();
    }

    @Override
    public int getPagerSize() {
        return 2;
    }

    @Override
    protected int getFragmentPosition(Fragment fragment) {
        if(fragment instanceof RepositoriesFragment){
            return 0;
        } else if(fragment instanceof UserListFragment){
            return 1;
        } else
            return -1;
    }
}
