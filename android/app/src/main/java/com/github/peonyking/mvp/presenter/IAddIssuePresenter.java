package com.github.peonyking.mvp.presenter;

import com.github.peonyking.dao.DaoSession;
import com.github.peonyking.mvp.contract.IAddIssueContract;
import com.github.peonyking.mvp.presenter.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created by ThirtyDegreesRay on 2017/9/26 16:56:35
 */

public class IAddIssuePresenter extends BasePresenter<IAddIssueContract.View>
        implements IAddIssueContract.Presenter{

    @Inject
    public IAddIssuePresenter(DaoSession daoSession) {
        super(daoSession);
    }

}
