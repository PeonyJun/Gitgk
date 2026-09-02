package com.github.peonyking.mvp.presenter;

import com.github.peonyking.dao.DaoSession;
import com.github.peonyking.mvp.contract.IIssuesActContract;
import com.github.peonyking.mvp.presenter.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created by ThirtyDegreesRay on 2017/9/20 17:22:16
 */

public class IssuesActPresenter extends BasePresenter<IIssuesActContract.View>
        implements IIssuesActContract.Presenter{

    @Inject
    public IssuesActPresenter(DaoSession daoSession) {
        super(daoSession);
    }

}
