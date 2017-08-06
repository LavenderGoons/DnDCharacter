package com.lavendergoons.dndcharacter.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.google.firebase.crash.FirebaseCrash;
import com.lavendergoons.dndcharacter.DndApplication;
import com.lavendergoons.dndcharacter.ui.fragments.AboutFragment;
import com.lavendergoons.dndcharacter.ui.fragments.CharacterListFragment;
import com.lavendergoons.dndcharacter.models.SimpleCharacter;
import com.lavendergoons.dndcharacter.R;
import com.lavendergoons.dndcharacter.utils.Constants;

import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

import javax.inject.Inject;


/**
  * Initial Activity to hold list of SimpleCharacters
  * Add and delete SimpleCharacters.
  * Selecting SimpleCharacter will launch CharacterNavDrawerActivity,
  * to show all SimpleCharacter info.
 */

public class CharacterListActivity extends AppCompatActivity implements
        CharacterListFragment.OnCharacterClickListener{

    public static final String TAG = "CHARACTER_LIST";
    private static final String FIRST_OPEN = "FIRST_OPEN";
    public static final String VERSION_CODE = "VERSION_CODE";

    Toolbar mToolbar;

    boolean isFirstOpen = true;
    private int storedVersion = -1;

    @Inject
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_list);
        DndApplication.get(this).getAppComponent().inject(this);

        createView();
        checkAppVersion();
    }

    private void createView() {
        mToolbar = (Toolbar) findViewById(R.id.character_list_toolbar);
        mToolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(mToolbar);

        FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
        fragTransaction.replace(R.id.content_character_list, CharacterListFragment.newInstance(), CharacterListFragment.TAG).commit();
    }

    private void checkAppVersion() {
        SharedPreferences.Editor sharedEditor = sharedPreferences.edit();
        isFirstOpen = sharedPreferences.getBoolean(FIRST_OPEN, true);
        storedVersion = sharedPreferences.getInt(VERSION_CODE, 0);

        int versionCode = -1;
        String versionName = "";
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionCode = packageInfo.versionCode;
            versionName = packageInfo.versionName;
        } catch (Exception ex) {
            ex.printStackTrace();
            FirebaseCrash.log(ex.toString());
        }

        Log.d(TAG, "VersionCode: "+versionCode+" StoredVersion: "+storedVersion);
        if ((versionCode != -1 && versionCode != storedVersion) || isFirstOpen) {
            showChangeDialog(false);
        }

        sharedEditor.putInt(VERSION_CODE, versionCode);
        sharedEditor.putBoolean(FIRST_OPEN, false);
        sharedEditor.apply();
    }

    @Override
    public void onFragmentCharacterClick(SimpleCharacter simpleCharacter, long id) {
        Intent intent = new Intent(this, CharacterNavDrawerActivity.class);
        intent.putExtra(Constants.CHARACTER_KEY, simpleCharacter);
        intent.putExtra(Constants.CHARACTER_ID, id);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (isCurrentFragment(AboutFragment.TAG)) {
            // Set the title back to the app name once leaving About page
            mToolbar.setTitle(getString(R.string.app_name));
        }
        super.onBackPressed();
    }

    private boolean isCurrentFragment(String tag) {
        Fragment frag = getSupportFragmentManager().findFragmentByTag(tag);
        return frag != null && frag.isVisible();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionAbout:
                mToolbar.setTitle(getString(R.string.title_fragment_about));
                FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
                fragTransaction.replace(R.id.content_character_list, AboutFragment.newInstance(), AboutFragment.TAG).addToBackStack(AboutFragment.TAG).commit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void showChangeDialog(boolean isAnnouncment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LinearLayout dialogLayout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        final WebView webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setBackgroundColor(Color.TRANSPARENT);
        if (isAnnouncment) {
            webView.loadUrl("file:///android_asset/firstOpen.html");
        } else {
            webView.loadUrl("file:///android_asset/info.html");
        }


        dialogLayout.setLayoutParams(params);
        dialogLayout.addView(webView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        //TODO Change
        if (isAnnouncment) {
            builder.setTitle(getString(R.string.announcement_title));
        } else {
            builder.setTitle(getString(R.string.announcement_changelog));
        }
        builder.setView(dialogLayout)
                .setPositiveButton(R.string.ok, null)
                .create().show();
    }
}
