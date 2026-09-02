

package com.github.peonyking.ui.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.github.peonyking.R;
import com.github.peonyking.inject.component.AppComponent;
import com.github.peonyking.inject.component.DaggerFragmentComponent;
import com.github.peonyking.inject.module.FragmentModule;
import com.github.peonyking.mvp.contract.INotificationsContract;
import com.github.peonyking.mvp.model.Notification;
import com.github.peonyking.mvp.model.NotificationSubject;
import com.github.peonyking.mvp.model.Repository;
import com.github.peonyking.mvp.presenter.NotificationsPresenter;
import com.github.peonyking.ui.activity.CommitDetailActivity;
import com.github.peonyking.ui.activity.IssueDetailActivity;
import com.github.peonyking.ui.activity.RepositoryActivity;
import com.github.peonyking.ui.adapter.NotificationsAdapter;
import com.github.peonyking.ui.adapter.base.DoubleTypesModel;
import com.github.peonyking.ui.fragment.base.ListFragment;
import com.github.peonyking.util.BundleHelper;

import java.util.ArrayList;

/**
 * Created by ThirtyDegreesRay on 2017/11/6 20:54:31
 */

public class NotificationsFragment extends ListFragment<NotificationsPresenter, NotificationsAdapter>
        implements INotificationsContract.View, NotificationsAdapter.NotificationAdapterListener {

    public enum NotificationsType{
        Unread, Participating, All
    }

    public static Fragment create(NotificationsType type){
        NotificationsFragment fragment = new NotificationsFragment();
        fragment.setArguments(BundleHelper.builder().put("type", type).build());
        return fragment;
    }

    @Override
    protected void initFragment(Bundle savedInstanceState) {
        super.initFragment(savedInstanceState);
        setLoadMoreEnable(true);
        setHasOptionsMenu(NotificationsType.Unread.equals(mPresenter.getType()));
        adapter.setListener(this);
    }

    @Override
    public void showNotifications(ArrayList<DoubleTypesModel<Repository, Notification>> notifications) {
        adapter.setData(notifications);
        postNotifyDataSetChanged();
        getActivity().invalidateOptionsMenu();
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
        mPresenter.loadNotifications(1, true);
    }

    @Override
    protected String getEmptyTip() {
        return getString(R.string.no_notifications_tip);
    }

    @Override
    public void onFragmentShowed() {
        super.onFragmentShowed();
        if(mPresenter != null) mPresenter.prepareLoadData();
    }

    @Override
    public void onItemClick(int position, @NonNull View view) {
        super.onItemClick(position, view);
        Notification notification = adapter.getData().get(position).getM2();
        Repository repository = adapter.getData().get(position).getM1();
        if(adapter.getItemViewType(position) == 0){
            RepositoryActivity.show(getActivity(), repository.getOwner().getLogin(), repository.getName());
        }else{
            String url = notification.getSubject().getUrl();
            switch (notification.getSubject().getType()){
                case Issue:
                    IssueDetailActivity.show(getActivity(), url);
                    break;
                case Commit:
                    CommitDetailActivity.show(getActivity(), url);
                    break;
                case PullRequest:
                    showInfoToast(getString(R.string.developing));
                    break;
            }

            if(notification.isUnread() &&
                    !notification.getSubject().getType().equals(NotificationSubject.Type.PullRequest)){
                mPresenter.markNotificationAsRead(notification.getId());
                notification.setUnread(false);
                adapter.notifyItemChanged(position);
                getActivity().invalidateOptionsMenu();
            }

        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if(!mPresenter.isNotificationsAllRead()){
            inflater.inflate(R.menu.menu_notifications, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_mark_all_as_read){
            mPresenter.markAllNotificationsAsRead();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRepoMarkAsReadClicked(@NonNull Repository repository) {
        mPresenter.markRepoNotificationsAsRead(repository);
    }

}
