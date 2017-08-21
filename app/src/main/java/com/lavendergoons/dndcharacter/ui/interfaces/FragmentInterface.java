package com.lavendergoons.dndcharacter.ui.interfaces;


public interface FragmentInterface {
    // Return the ID to avoid fragment not attached exception
    int getTitle();
    String getFragmentTag();
}
