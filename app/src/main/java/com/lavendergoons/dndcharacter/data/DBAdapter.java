package com.lavendergoons.dndcharacter.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.lavendergoons.dndcharacter.di.scope.DataScope;
import com.lavendergoons.dndcharacter.models.SimpleCharacter;
import com.lavendergoons.dndcharacter.utils.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;


import javax.inject.Inject;

import static android.R.attr.type;
import static com.lavendergoons.dndcharacter.data.DatabaseHelper.COLUMN_ABILITIES;
import static com.lavendergoons.dndcharacter.data.DatabaseHelper.COLUMN_ARMOR;
import static com.lavendergoons.dndcharacter.data.DatabaseHelper.COLUMN_ATTACK;
import static com.lavendergoons.dndcharacter.data.DatabaseHelper.COLUMN_ATTRIBUTES;
import static com.lavendergoons.dndcharacter.data.DatabaseHelper.COLUMN_CHARACTER;
import static com.lavendergoons.dndcharacter.data.DatabaseHelper.COLUMN_FEATS;
import static com.lavendergoons.dndcharacter.data.DatabaseHelper.COLUMN_ID;
import static com.lavendergoons.dndcharacter.data.DatabaseHelper.COLUMN_ITEM_GENERAL;
import static com.lavendergoons.dndcharacter.data.DatabaseHelper.COLUMN_NOTES;
import static com.lavendergoons.dndcharacter.data.DatabaseHelper.COLUMN_SKILL;
import static com.lavendergoons.dndcharacter.data.DatabaseHelper.COLUMN_SPELL;
import static com.lavendergoons.dndcharacter.data.DatabaseHelper.TABLE_CHARACTERS;

@DataScope
public class DBAdapter {

    public static final String TAG = "DATABASE_ADAPTER";

    private DatabaseHelper databaseHelper;
    private Gson gson = new Gson();

    @Inject
    public DBAdapter(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }


    public ArrayList<SimpleCharacter> getSimpleCharacters() {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Cursor c = database.query(true, TABLE_CHARACTERS, new String[]{COLUMN_CHARACTER}, null, null, null, null, null, null);

        ArrayList<SimpleCharacter> simpleCharacters = new ArrayList<>();
        if (c != null) {
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                String json = c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CHARACTER));
                Log.d("JSON", "SimpleCharacter json string "+json);
                SimpleCharacter simpleCharacter = gson.fromJson(json, SimpleCharacter.class);
                simpleCharacters.add(simpleCharacter);
            }
            c.close();
            database.close();
            return simpleCharacters;
        }
        return null;
    }

    public long getCharacterId(String name) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Cursor c = database.query(true, TABLE_CHARACTERS, new String[]{COLUMN_ID, COLUMN_CHARACTER}, null, null, null, null, null, null);
        long id = -1;

        if (c != null) {
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                String json = c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CHARACTER));
                SimpleCharacter simpleCharacter = gson.fromJson(json, SimpleCharacter.class);
                if (simpleCharacter.getName().equals(name)) {
                    id = (long) c.getInt(c.getColumnIndex(DatabaseHelper.COLUMN_ID));
                    break;
                }
            }
            c.close();
            database.close();
        }
        return id;
    }

    public boolean insertRow(String simpleCharacterJson) {
        if (!Utils.isStringEmpty(simpleCharacterJson)) {
            SQLiteDatabase database = databaseHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values = fillBlankColumns(values);
            values.put(COLUMN_CHARACTER, simpleCharacterJson);

            long id = database.insert(TABLE_CHARACTERS, null, values);
            Log.d(TAG, "Insert Row ID "+id+" for character "+simpleCharacterJson);
            database.close();
            return true;
        }
        return false;
    }

    /*private Cursor getColumnCursor(String column, long id) {
        if (!Utils.isStringEmpty(column)) {
            Cursor cursor = database.query(true, TABLE_CHARACTERS, new String[]{COLUMN_ID, column}, COLUMN_ID+ " = ?", new String[]{String.valueOf(id)}, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                return cursor;
            }
        }
        return null;
    }*/

    public <T> ArrayList<T> getListColumn(long id, String column, Type jsonType) {
        if (!Utils.isStringEmpty(column)) {

            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            Cursor cursor = database.query(true, TABLE_CHARACTERS, new String[]{COLUMN_ID, column}, COLUMN_ID+ " = ?", new String[]{String.valueOf(id)}, null, null, null, null);
            String json = cursor.getString(cursor.getColumnIndex(column));

            if (json != null && !Utils.isStringEmpty(json) && !json.equals("[]") && !json.equals("[ ]")) {
                ArrayList<T> list = gson.fromJson(json, jsonType);
                cursor.close();
                database.close();
                return list;
            }
        }
        return null;
    }

    public <T> T getObjectColumn(long id, String column, Type type) {
        if (!Utils.isStringEmpty(column)) {

            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            Cursor cursor = database.query(true, TABLE_CHARACTERS, new String[]{COLUMN_ID, column}, COLUMN_ID+ " = ?", new String[]{String.valueOf(id)}, null, null, null, null);
            String json = cursor.getString(cursor.getColumnIndex(column));

            if (json != null && !Utils.isStringEmpty(json)) {
                T item = gson.fromJson(json, type);
                cursor.close();
                database.close();
                return item;
            }
        }
        return null;
    }

    public String getJsonString(long id, String column) {
        if (!Utils.isStringEmpty(column)) {

            SQLiteDatabase database = databaseHelper.getReadableDatabase();
            Cursor cursor = database.query(true, TABLE_CHARACTERS, new String[]{COLUMN_ID, column}, COLUMN_ID+ " = ?", new String[]{String.valueOf(id)}, null, null, null, null);
            String json = cursor.getString(cursor.getColumnIndex(column));

            cursor.close();
            database.close();
            return json;
        }
        return null;
    }

    public boolean fillColumn(long id, String col, String value) {
        if (!Utils.isStringEmpty(value) && !Utils.isStringEmpty(col)) {
            SQLiteDatabase database = databaseHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(col, value);
            // Update TABLE with values WHERE COLUMN_ID = id
            database.update(TABLE_CHARACTERS, values, COLUMN_ID+" = ?", new String[]{String.valueOf(id)});
            database.close();
            return true;
        }
        return false;
    }


    public int deleteRow(long id) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        String where = COLUMN_ID + " = ?";
        return database.delete(TABLE_CHARACTERS, where, new String[]{String.valueOf(id)});
    }

    private ContentValues fillBlankColumns(ContentValues values) {
        values.put(COLUMN_ABILITIES, "");
        values.put(COLUMN_ATTRIBUTES, "");
        values.put(COLUMN_ARMOR, "");
        values.put(COLUMN_ATTACK, "");
        values.put(COLUMN_ITEM_GENERAL, "");
        values.put(COLUMN_SKILL, "");
        values.put(COLUMN_SPELL, "");
        values.put(COLUMN_NOTES, "");
        values.put(COLUMN_FEATS, "");
        return values;
    }
}
