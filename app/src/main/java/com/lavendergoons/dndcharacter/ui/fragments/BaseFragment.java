package com.lavendergoons.dndcharacter.ui.fragments;

import android.support.v4.app.Fragment;

import com.lavendergoons.dndcharacter.R;
import com.lavendergoons.dndcharacter.ui.interfaces.FragmentInterface;

public class BaseFragment extends Fragment implements FragmentInterface{

    public static final String TAG = BaseFragment.class.getCanonicalName();

    public int getTitle() {
        return R.string.app_name;
    }

    public String getFragmentTag() {
        return TAG;
    }
}
