

package com.github.peonyking.inject.component;

import com.github.peonyking.inject.ActivityScope;
import com.github.peonyking.inject.module.ActivityModule;
import com.github.peonyking.ui.activity.CommitDetailActivity;
import com.github.peonyking.ui.activity.EditIssueActivity;
import com.github.peonyking.ui.activity.IssueDetailActivity;
import com.github.peonyking.ui.activity.IssuesActivity;
import com.github.peonyking.ui.activity.LoginActivity;
import com.github.peonyking.ui.activity.MainActivity;
import com.github.peonyking.ui.activity.ProfileActivity;
import com.github.peonyking.ui.activity.ReleaseInfoActivity;
import com.github.peonyking.ui.activity.RepositoryActivity;
import com.github.peonyking.ui.activity.SearchActivity;
import com.github.peonyking.ui.activity.SettingsActivity;
import com.github.peonyking.ui.activity.SplashActivity;
import com.github.peonyking.ui.activity.TrendingActivity;

import dagger.Component;

/**
 * ActivityComponent
 * Created by ThirtyDegreesRay on 2016/8/30 14:56
 */
@ActivityScope
@Component(modules = ActivityModule.class, dependencies = AppComponent.class)
public interface ActivityComponent {
    void inject(SplashActivity activity);
    void inject(LoginActivity activity);
    void inject(MainActivity activity);
    void inject(SettingsActivity activity);
    void inject(RepositoryActivity activity);
    void inject(ProfileActivity activity);
    void inject(SearchActivity activity);
    void inject(ReleaseInfoActivity activity);
    void inject(IssuesActivity activity);
    void inject(IssueDetailActivity activity);
    void inject(EditIssueActivity activity);
    void inject(CommitDetailActivity activity);
    void inject(TrendingActivity activity);
}
