package com.lavendergoons.dndcharacter.utils;

import android.support.annotation.NonNull;
import android.util.Log;
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
    public static boolean isStringEmpty(@NonNull String string) {
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

    /**
     * Gets the text from a view and parseInt
     * @param view TextView
     * @return Value of text in view
     */
    public static int checkIntNotNull(TextView view) {
        int value = 0;
        if (view != null) {
            if (!isStringEmpty(view.getText().toString())) {
                try {
                    value = Integer.parseInt(view.getText().toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    FirebaseCrash.log("checkIntNotNull view: "+view.toString()
                            +" \n view text: "+view.getText().toString()
                            +" exception: "+e.toString());
                    value = 0;
                }
            }
        }
        return value;
    }

    /**
     * Gets the text from a view and parseInt
     * @param view EditText
     * @return Value of text in view
     */
    public static int checkIntNotNull(EditText view) {
        int value = 0;
        if (view != null) {
            if (!isStringEmpty(view.getText().toString())) {
                try {
                    value = Integer.parseInt(view.getText().toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    FirebaseCrash.log("checkIntNotNull view: "+view.toString()
                            +" \n view text: "+view.getText().toString()
                            +" exception: "+e.toString());
                    value = 0;
                }
            }
        }
        return value;
    }

    /**
     * Parses String to Int.
     * Has default value 0 if failure
     * @param  input
     * @return Parsed or Failed Int Value
     */
    public static int stringToInt(String input) {
        int value = 0;
        if (!isStringEmpty(input)) {
            try {
               value = Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                FirebaseCrash.log("exception: "+ex.toString());
                value = 0;
            }
        }
        return value;
    }

    /**
     * Parses String to Float.
     * Has default value 0 if failure
     * @param input
     * @return Parsed or Failed Float Value
     */
    public static float stringToFloat(String input) {
        float value = 0;
        if (!isStringEmpty(input)) {
            try {
                value = Float.parseFloat(input);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                FirebaseCrash.log("exception: "+ex.toString());
                value = 0;
            }
        }
        return value;
    }

    public static void logError(Exception ex, String TAG, String msg) {
        ex.printStackTrace();
        String error = (msg.isEmpty()) ? ex.toString() : "Message: "+msg+"\nException:"+ex.toString();
        Log.e(TAG, error);
        FirebaseCrash.log(ex.toString());
    }

    public static void logError(Exception ex, String TAG) {
        logError(ex, TAG, "");
    }
}
