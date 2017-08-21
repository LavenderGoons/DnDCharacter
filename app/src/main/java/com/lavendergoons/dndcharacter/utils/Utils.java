package com.lavendergoons.dndcharacter.utils;

import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.crash.FirebaseCrash;

/**
 * General Utils
 */

public class Utils {
    /**
     * If string is empty returns true
     * @param string
     * @return boolean
     */
    public static boolean isStringEmpty(String string) {
        return (string.isEmpty() || string.trim().isEmpty());
    }

    /**
     * If string is empty returns true
     * @param array
     * @return
     */
    public static boolean isStringArrayEmpty(String[] array) {
        for (String s : array) {
            if (s.isEmpty() || s.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Computes mod value
     * @param mod
     * @return
     */
    public static int modValue(int mod) {
        return (mod%2==0)?(mod - 10)/2:(mod - 11) / 2;
    }

    public static int checkInputNotNull(TextView view) {
        int value = 0;
        if (view != null) {
            if (!isStringEmpty(view.getText().toString())) {
                try {
                    value = Integer.parseInt(view.getText().toString());
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    FirebaseCrash.log("checkInputNotNull view: "+view.toString()
                            +" \n view text: "+view.getText().toString()
                            +" exception: "+e.toString());
                    value = 0;
                }
            }
        }
        return value;
    }

    public static int checkInputNotNull(EditText view) {
        int value = 0;
        if (view != null) {
            if (!isStringEmpty(view.getText().toString())) {
                try {
                    value = Integer.parseInt(view.getText().toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    FirebaseCrash.log("checkInputNotNull view: "+view.toString()
                            +" \n view text: "+view.getText().toString()
                            +" exception: "+e.toString());
                    value = 0;
                }
            }
        }
        return value;
    }
}
