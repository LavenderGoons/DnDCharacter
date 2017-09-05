package com.lavendergoons.dndcharacter.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TAG = DatabaseHelper.class.getCanonicalName();

    public static final String TABLE_CHARACTERS = "characters";
    private static final String DATABASE_NAME = "DnD_Characters.db";
    public static final int DATABASE_VERSION = 3;


    public static final String COLUMN_ID = "_id";

    public static final String COLUMN_CHARACTER = "character";
    public static final String COLUMN_ABILITIES = "abilities";
    public static final String COLUMN_ATTRIBUTES = "attributes";
    public static final String COLUMN_ARMOR = "armor";
    public static final String COLUMN_ATTACK = "attack";
    public static final String COLUMN_ITEM_GENERAL = "item_general";
    public static final String COLUMN_SKILL = "skill";
    public static final String COLUMN_SPELL = "spell";
    public static final String COLUMN_NOTES = "notes";
    public static final String COLUMN_FEATS = "feats";

    public static final String[] ALL_COLUMNS = {
            COLUMN_ID,
            COLUMN_CHARACTER,
            COLUMN_ABILITIES,
            COLUMN_ATTRIBUTES,
            COLUMN_ARMOR,
            COLUMN_ATTACK,
            COLUMN_ITEM_GENERAL,
            COLUMN_SKILL,
            COLUMN_SPELL,
            COLUMN_NOTES,
            COLUMN_FEATS
    };

    //*********************************************
    // PUT COMMA AFTER text not null default
    //*********************************************
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_CHARACTERS + " (" + COLUMN_ID +
                    " integer primary key autoincrement, " +
                    COLUMN_CHARACTER + " text not null default '', " +
                    COLUMN_ABILITIES + " text not null default '', " +
                    COLUMN_ATTRIBUTES + " text not null default '', " +
                    COLUMN_ARMOR + " text not null default '', " +
                    COLUMN_ATTACK + " text not null default '', " +
                    COLUMN_ITEM_GENERAL + " text not null default '', " +
                    COLUMN_SKILL + " text not null default '', " +
                    COLUMN_SPELL + " text not null default '', " +
                    COLUMN_NOTES + " text not null default '', " +
                    COLUMN_FEATS + " text not null default ''" +
                    " );";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //TODO Refactor and make this better
        Log.w(TAG, "Upgrading SQLite Table");

        // Upgrading from 1 to 2 add notes column
        if (newVersion == 2 && oldVersion == 1) {
            sqLiteDatabase.execSQL("ALTER TABLE "+TABLE_CHARACTERS+" ADD COLUMN "+COLUMN_NOTES +" text not null default ''");
        }

        // Upgrading from 2 to 3 add feats column
        if (newVersion == 3 && oldVersion == 2) {
            sqLiteDatabase.execSQL("ALTER TABLE "+TABLE_CHARACTERS+" ADD COLUMN "+COLUMN_FEATS +" text not null default ''");
        }

        // Upgrading from 1 to 3 add notes & feats column
        if (newVersion == 3 && oldVersion == 1) {
            sqLiteDatabase.execSQL("ALTER TABLE "+TABLE_CHARACTERS+" ADD COLUMN "+COLUMN_NOTES +" text not null default ''");
            sqLiteDatabase.execSQL("ALTER TABLE "+TABLE_CHARACTERS+" ADD COLUMN "+COLUMN_FEATS +" text not null default ''");
        }
    }
}
