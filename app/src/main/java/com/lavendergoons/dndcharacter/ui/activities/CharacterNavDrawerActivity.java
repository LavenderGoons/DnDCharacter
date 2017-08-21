package com.lavendergoons.dndcharacter.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lavendergoons.dndcharacter.data.DBAdapter;
import com.lavendergoons.dndcharacter.ui.fragments.AbilitiesFragment;
import com.lavendergoons.dndcharacter.ui.fragments.AboutFragment;
import com.lavendergoons.dndcharacter.ui.fragments.ArmorFragment;
import com.lavendergoons.dndcharacter.ui.fragments.ArmorListFragment;
import com.lavendergoons.dndcharacter.ui.fragments.AttacksFragment;
import com.lavendergoons.dndcharacter.ui.fragments.AttributesFragment;
import com.lavendergoons.dndcharacter.ui.fragments.BaseFragment;
import com.lavendergoons.dndcharacter.ui.fragments.FeatsFragment;
import com.lavendergoons.dndcharacter.ui.fragments.ItemsGeneralFragment;
import com.lavendergoons.dndcharacter.ui.fragments.NoteFragment;
import com.lavendergoons.dndcharacter.ui.fragments.NotesListFragment;
import com.lavendergoons.dndcharacter.ui.fragments.SkillsFragment;
import com.lavendergoons.dndcharacter.ui.fragments.SpellFragment;
import com.lavendergoons.dndcharacter.ui.fragments.SpellListFragment;
import com.lavendergoons.dndcharacter.models.SimpleCharacter;
import com.lavendergoons.dndcharacter.R;
import com.lavendergoons.dndcharacter.utils.CharacterManager;
import com.lavendergoons.dndcharacter.utils.Constants;

/**
 * Nav Drawer Activity to display fragments,
 * with all SimpleCharacter info.
 */

public class CharacterNavDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentManager.OnBackStackChangedListener {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;

    public static final String TAG = "CHARACTER_NAV";

    private boolean toolbarListenerRegister = false;

    private DBAdapter dbAdapter;
    private SimpleCharacter simpleCharacter;
    private CharacterManager characterManager;
    private long characterId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_nav);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            simpleCharacter = (extras.get(Constants.CHARACTER_KEY) instanceof SimpleCharacter)? (SimpleCharacter) extras.get(Constants.CHARACTER_KEY) : new SimpleCharacter();
            characterId = extras.getLong(Constants.CHARACTER_ID);
        }

        mToolbar = (Toolbar) findViewById(R.id.character_nav_toolbar);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().addOnBackStackChangedListener(this);

        if (savedInstanceState == null) {
            loadDefaultFragment();
        }
        mToolbar.setTitle(getString(R.string.title_fragment_attributes));
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void showBackButton(boolean enabled) {
        try {
            if (enabled) {
                mDrawerToggle.setDrawerIndicatorEnabled(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                if (!toolbarListenerRegister) {
                    mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onBackPressed();
                        }
                    });
                }
                toolbarListenerRegister = true;
            } else {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                mDrawerToggle.setDrawerIndicatorEnabled(true);
                mDrawerToggle.setToolbarNavigationClickListener(null);
                toolbarListenerRegister = false;
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setDrawerLock(boolean locked) {
        if (locked) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            mDrawerToggle.onDrawerStateChanged(DrawerLayout.STATE_IDLE);
            mDrawerToggle.syncState();
        } else {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            mDrawerToggle.onDrawerStateChanged(DrawerLayout.STATE_IDLE);
            mDrawerToggle.syncState();
        }
    }

    private boolean isCurrentFragment(String tag) {
        Fragment frag = getSupportFragmentManager().findFragmentByTag(tag);
        return frag != null && frag.isVisible();
    }

    @Override
    public void onBackStackChanged() {
        if (isCurrentFragment(SpellFragment.TAG) || isCurrentFragment(ArmorFragment.TAG)
                || isCurrentFragment(AboutFragment.TAG) || isCurrentFragment(NoteFragment.TAG)) {
            showBackButton(true);
            setDrawerLock(true);
        } else {
            showBackButton(false);
            setDrawerLock(false);
        }
        mToolbar.setTitle(getCurrentFragmentTitle());
    }

    private String getCurrentFragmentTitle() {
        Fragment frag = getSupportFragmentManager().findFragmentById(R.id.content_character_nav);
        if (frag != null && frag instanceof BaseFragment) {
            return getString(((BaseFragment) frag).getTitle());
        }
        return getString(R.string.app_name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.actionAbout) {
            mToolbar.setTitle(getString(R.string.title_fragment_about));
            FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
            fragTransaction.replace(R.id.content_character_nav, AboutFragment.newInstance(), AboutFragment.TAG).addToBackStack(AboutFragment.TAG).commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadDefaultFragment() {
        FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
        fragTransaction.replace(R.id.content_character_nav, AttributesFragment.newInstance(simpleCharacter, characterId),  AttributesFragment.TAG).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        BaseFragment fragment = null;
        switch (item.getItemId()) {
            case R.id.nav_characters:
                // Stop Fragment to Write Changes
                Fragment frag = getSupportFragmentManager().findFragmentById(R.id.content_character_nav);
                Log.d(TAG, frag.toString());
                frag.onStop();
                Intent intent = new Intent(this, CharacterListActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.nav_attributes:
                fragment = AttributesFragment.newInstance(simpleCharacter, characterId);
                break;
            case R.id.nav_abilities:
                fragment = AbilitiesFragment.newInstance(simpleCharacter, characterId);
                break;
            case R.id.nav_feats:
                fragment = FeatsFragment.newInstance(simpleCharacter, characterId);
                break;
            case R.id.nav_skills:
                fragment = SkillsFragment.newInstance(simpleCharacter, characterId);
                break;
            case R.id.nav_attacks:
                fragment = AttacksFragment.newInstance(simpleCharacter, characterId);
                break;
            case R.id.nav_items:
                fragment = ItemsGeneralFragment.newInstance(simpleCharacter, characterId);
                break;
            case R.id.nav_armor:
                fragment = ArmorListFragment.newInstance(simpleCharacter, characterId);
                break;
            case R.id.nav_spells:
                fragment = SpellListFragment.newInstance(simpleCharacter, characterId);
                break;
            case R.id.nav_notes:
                fragment = NotesListFragment.newInstance(simpleCharacter, characterId);
                break;
        }


        mDrawerLayout.closeDrawer(GravityCompat.START);
        if (fragment != null && !isCurrentFragment(fragment.getTag())) {
            mToolbar.setTitle(getString(fragment.getTitle()));
            FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
            fragTransaction.replace(R.id.content_character_nav, fragment, fragment.getTag()).commit();
        }
        return true;
    }
}
