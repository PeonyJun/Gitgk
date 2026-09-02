

package com.github.peonyking.inject.component;

import com.github.peonyking.inject.FragmentScope;
import com.github.peonyking.inject.module.FragmentModule;
import com.github.peonyking.ui.fragment.CollectionsFragment;
import com.github.peonyking.ui.fragment.LabelManageFragment;
import com.github.peonyking.ui.fragment.LanguagesEditorFragment;
import com.github.peonyking.ui.fragment.ActivityFragment;
import com.github.peonyking.ui.fragment.BookmarksFragment;
import com.github.peonyking.ui.fragment.CommitFilesFragment;
import com.github.peonyking.ui.fragment.CommitsFragment;
import com.github.peonyking.ui.fragment.IssueTimelineFragment;
import com.github.peonyking.ui.fragment.IssuesFragment;
import com.github.peonyking.ui.fragment.NotificationsFragment;
import com.github.peonyking.ui.fragment.ProfileInfoFragment;
import com.github.peonyking.ui.fragment.ReleasesFragment;
import com.github.peonyking.ui.fragment.RepoFilesFragment;
import com.github.peonyking.ui.fragment.RepoInfoFragment;
import com.github.peonyking.ui.fragment.RepositoriesFragment;
import com.github.peonyking.ui.fragment.TopicsFragment;
import com.github.peonyking.ui.fragment.TraceFragment;
import com.github.peonyking.ui.fragment.UserListFragment;
import com.github.peonyking.ui.fragment.ViewerFragment;
import com.github.peonyking.ui.fragment.WikiFragment;

import dagger.Component;

/**
 * Created on 2017/7/18.
 *
 * @author ThirtyDegreesRay
 */

@FragmentScope
@Component(modules = FragmentModule.class, dependencies = AppComponent.class)
public interface FragmentComponent {
    void inject(RepositoriesFragment fragment);
    void inject(RepoInfoFragment fragment);
    void inject(RepoFilesFragment fragment);
    void inject(UserListFragment fragment);
    void inject(ViewerFragment fragment);
    void inject(ProfileInfoFragment fragment);
    void inject(ActivityFragment fragment);
    void inject(ReleasesFragment fragment);
    void inject(IssuesFragment fragment);
    void inject(IssueTimelineFragment fragment);
    void inject(CommitsFragment fragment);
    void inject(CommitFilesFragment fragment);
    void inject(NotificationsFragment fragment);
    void inject(BookmarksFragment fragment);
    void inject(TraceFragment fragment);
    void inject(LanguagesEditorFragment fragment);
    void inject(WikiFragment fragment);
    void inject(CollectionsFragment fragment);
    void inject(TopicsFragment fragment);
    void inject(LabelManageFragment fragment);
}
