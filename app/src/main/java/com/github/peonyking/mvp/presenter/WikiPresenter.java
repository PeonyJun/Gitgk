package com.github.peonyking.mvp.presenter;

import com.thirtydegreesray.dataautoaccess.annotation.AutoAccess;
import com.github.peonyking.dao.DaoSession;
import com.github.peonyking.http.core.HttpObserver;
import com.github.peonyking.http.core.HttpResponse;
import com.github.peonyking.mvp.contract.IWikiContract;
import com.github.peonyking.mvp.model.WikiFeedModel;
import com.github.peonyking.mvp.model.WikiModel;
import com.github.peonyking.mvp.presenter.base.BasePresenter;

import org.xmlpull.v1.XmlPullParserException;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by ThirtyDegreesRay on 2017/12/6 16:41:32
 */

public class WikiPresenter extends BasePresenter<IWikiContract.View>
        implements IWikiContract.Presenter {

    @AutoAccess String owner;
    @AutoAccess String repo;
    private ArrayList<WikiModel> wikiList;

    @Inject
    public WikiPresenter(DaoSession daoSession) {
        super(daoSession);
    }

    @Override
    public void onViewInitialized() {
        super.onViewInitialized();
        loadWiki(false);
    }

    @Override
    public void loadWiki(boolean isReload) {
        mView.showLoading();
        HttpObserver<WikiFeedModel> httpObserver = new HttpObserver<WikiFeedModel>() {
            @Override
            public void onError(Throwable error) {
                mView.hideLoading();
                if (error.getCause() != null && error.getCause() instanceof XmlPullParserException) {
                    mView.showWiki(null);
                } else {
                    mView.showLoadError(getErrorTip(error));
                }
            }

            @Override
            public void onSuccess(HttpResponse<WikiFeedModel> response) {
                mView.hideLoading();
                wikiList = response.body().getWikiList();
                mView.showWiki(wikiList);
            }
        };
        generalRxHttpExecute(forceNetWork -> getGitHubWebPageService()
                        .getWiki(forceNetWork, owner, repo)
                , httpObserver, !isReload);

    }

}
