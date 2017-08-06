package com.lavendergoons.dndcharacter.di.component;

import android.content.SharedPreferences;

import com.lavendergoons.dndcharacter.data.DBAdapter;
import com.lavendergoons.dndcharacter.di.module.DataModule;
import com.lavendergoons.dndcharacter.di.scope.DataScope;
import com.lavendergoons.dndcharacter.models.Abilities;
import com.lavendergoons.dndcharacter.ui.activities.CharacterListActivity;
import com.lavendergoons.dndcharacter.ui.activities.CharacterNavDrawerActivity;
import com.lavendergoons.dndcharacter.ui.fragments.AbilitiesFragment;
import com.lavendergoons.dndcharacter.ui.fragments.ArmorFragment;
import com.lavendergoons.dndcharacter.ui.fragments.ArmorListFragment;
import com.lavendergoons.dndcharacter.ui.fragments.AttacksFragment;
import com.lavendergoons.dndcharacter.ui.fragments.AttributesFragment;
import com.lavendergoons.dndcharacter.ui.fragments.CharacterListFragment;
import com.lavendergoons.dndcharacter.ui.fragments.FeatsFragment;
import com.lavendergoons.dndcharacter.ui.fragments.ItemsGeneralFragment;
import com.lavendergoons.dndcharacter.ui.fragments.NoteFragment;
import com.lavendergoons.dndcharacter.ui.fragments.NotesListFragment;
import com.lavendergoons.dndcharacter.ui.fragments.SkillsFragment;
import com.lavendergoons.dndcharacter.ui.fragments.SpellFragment;
import com.lavendergoons.dndcharacter.ui.fragments.SpellListFragment;
import com.lavendergoons.dndcharacter.utils.CharacterManager2;

import dagger.Component;

@DataScope
@Component(modules = {DataModule.class})
public interface AppComponent {
    //DBAdapter getDBAdapter();
    //CharacterManager2 getCharacterManager2();
    //SharedPreferences getSharedPreferences();

    // Activity Injection
    void inject(CharacterListActivity activity);
    void inject(CharacterNavDrawerActivity activity);

    // Fragment Injection
    void inject(AbilitiesFragment fragment);
    void inject(ArmorFragment fragment);
    void inject(ArmorListFragment fragment);
    void inject(AttacksFragment fragment);
    void inject(AttributesFragment fragment);
    void inject(CharacterListFragment fragment);
    void inject(FeatsFragment fragment);
    void inject(ItemsGeneralFragment fragment);
    void inject(NoteFragment fragment);
    void inject(NotesListFragment fragment);
    void inject(SkillsFragment fragment);
    void inject(SpellFragment fragment);
    void inject(SpellListFragment fragment);
}
