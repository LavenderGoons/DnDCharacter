package com.lavendergoons.dndcharacter.di.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.lavendergoons.dndcharacter.data.DBAdapter;
import com.lavendergoons.dndcharacter.di.scope.DataScope;
import com.lavendergoons.dndcharacter.utils.CharacterManager2;

import dagger.Module;
import dagger.Provides;

@Module(includes = {AppModule.class})
public class DataModule {

    @Provides
    @DataScope
    public DBAdapter dbAdapter(Context context) {
        return new DBAdapter(context);
    }

    @Provides
    @DataScope
    public CharacterManager2 characterManager2(DBAdapter dbAdapter) {
        return new CharacterManager2(dbAdapter);
    }

    @Provides
    @DataScope
    public SharedPreferences sharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
