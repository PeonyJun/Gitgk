

package com.github.peonyking.inject.module;

import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.NonNull;

import com.github.peonyking.AppApplication;
import com.github.peonyking.AppConfig;
import com.github.peonyking.dao.DBOpenHelper;
import com.github.peonyking.dao.DaoMaster;
import com.github.peonyking.dao.DaoSession;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * AppModule
 * Created by ThirtyDegreesRay on 2016/8/30 13:52
 */
@Module
public class AppModule {

    private AppApplication application;

    public AppModule(AppApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public AppApplication provideApplication() {
        return application;
    }

    @NonNull
    @Provides
    @Singleton
    public DaoSession provideDaoSession() {
        DBOpenHelper helper = new DBOpenHelper(application, AppConfig.DB_NAME, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        return daoMaster.newSession();
    }


}
