package com.lavendergoons.dndcharacter.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;
import com.lavendergoons.dndcharacter.di.scope.DataScope;
import com.lavendergoons.dndcharacter.models.SimpleCharacter;
import com.lavendergoons.dndcharacter.utils.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;


import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

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

    public static final String TAG = DBAdapter.class.getCanonicalName();

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase mDatabase;
    private Gson gson = new Gson();

    @Inject
    public DBAdapter(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    private void checkDatabase() {
        if (mDatabase == null) {mDatabase = databaseHelper.getWritableDatabase();}
    }


    public ArrayList<SimpleCharacter> getSimpleCharacters() {
        checkDatabase();
        Cursor c = mDatabase.query(true, TABLE_CHARACTERS, new String[]{COLUMN_CHARACTER}, null, null, null, null, null, null);

        ArrayList<SimpleCharacter> simpleCharacters = new ArrayList<>();
        if (c != null) {
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                String json = c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_CHARACTER));
                Log.d("JSON", "SimpleCharacter json string "+json);
                SimpleCharacter simpleCharacter = gson.fromJson(json, SimpleCharacter.class);
                simpleCharacters.add(simpleCharacter);
            }
            c.close();
            return simpleCharacters;
        }
        return null;
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public Observable<ArrayList<SimpleCharacter>> getSimpleCharacterObservable() {
        checkDatabase();
        return Observable.create(new ObservableOnSubscribe<ArrayList<SimpleCharacter>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<ArrayList<SimpleCharacter>> e) throws Exception {
                Cursor cursor = null;
                try {
                    cursor = mDatabase.query(true, TABLE_CHARACTERS, new String[]{COLUMN_CHARACTER}, null, null, null, null, null, null);
                    // TODO Maybe Observable of String, and emit string onNext, with a Map to simplecharacter

                    ArrayList<SimpleCharacter> simpleCharacters = new ArrayList<>();
                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                        String json = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CHARACTER));

                        SimpleCharacter simpleCharacter = gson.fromJson(json, SimpleCharacter.class);
                        simpleCharacters.add(simpleCharacter);
                    }
                    e.onNext(simpleCharacters);
                    e.onComplete();
                } catch (SQLiteException ex) {
                    Utils.logError(ex, TAG);
                    e.onError(ex);
                } catch (IllegalArgumentException ex1) {
                    Utils.logError(ex1, TAG);
                    e.onError(ex1);
                } finally {
                    if (cursor != null) {cursor.close();}
                }
            }
        }).subscribeOn(Schedulers.computation());
    }

    public long getCharacterId(String name) {
        checkDatabase();
        Cursor c = mDatabase.query(true, TABLE_CHARACTERS, new String[]{COLUMN_ID, COLUMN_CHARACTER}, null, null, null, null, null, null);
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
        }
        return id;
    }

    public boolean insertRow(String simpleCharacterJson) {
        if (!Utils.isStringEmpty(simpleCharacterJson)) {
            checkDatabase();
            ContentValues values = new ContentValues();
            values = fillBlankColumns(values);
            values.put(COLUMN_CHARACTER, simpleCharacterJson);

            long id = mDatabase.insert(TABLE_CHARACTERS, null, values);
            Log.d(TAG, "Insert Row ID "+id+" for character "+simpleCharacterJson);
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
            checkDatabase();
            Cursor cursor = mDatabase.query(true, TABLE_CHARACTERS, new String[]{COLUMN_ID, column}, COLUMN_ID+ " = ?", new String[]{String.valueOf(id)}, null, null, null, null);
            cursor.moveToFirst();
            String json = cursor.getString(cursor.getColumnIndex(column));

            if (json != null && !Utils.isStringEmpty(json) && !json.equals("[]") && !json.equals("[ ]")) {
                ArrayList<T> list = gson.fromJson(json, jsonType);
                cursor.close();
                return list;
            }
        }
        return null;
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    public Observable<String> getObservableJson(final long id, final String column) {
        checkDatabase();
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                Cursor cursor = null;
                try {
                    cursor = mDatabase.query(true, TABLE_CHARACTERS, new String[]{COLUMN_ID, column}, COLUMN_ID+ " = ?",
                            new String[]{String.valueOf(id)}, null, null, null, null);
                    cursor.moveToFirst();
                    String json = cursor.getString(cursor.getColumnIndexOrThrow(column));
                    Log.d(TAG, "getObservableJson: Thread "+Thread.currentThread().getName());
                    e.onNext(json);
                    e.onComplete();
                } catch (SQLiteException ex) {
                    ex.printStackTrace();
                    FirebaseCrash.log(ex.toString());
                    e.onError(ex);
                } catch (IllegalArgumentException ex1) {
                    ex1.printStackTrace();
                    FirebaseCrash.log(ex1.toString());
                    e.onError(ex1);
                } finally {
                    if (cursor != null ) {cursor.close();}
                }
            }
        });
    }

    public <T> T getObjectColumn(long id, String column, Type type) {
        if (!Utils.isStringEmpty(column)) {
            checkDatabase();
            Cursor cursor = mDatabase.query(true, TABLE_CHARACTERS, new String[]{COLUMN_ID, column}, COLUMN_ID+ " = ?", new String[]{String.valueOf(id)}, null, null, null, null);
            cursor.moveToFirst();
            String json = cursor.getString(cursor.getColumnIndexOrThrow(column));

            if (json != null && !Utils.isStringEmpty(json)) {
                T item = gson.fromJson(json, type);
                cursor.close();
                return item;
            }
        }
        return null;
    }

    public String getJsonString(long id, String column) {
        if (!Utils.isStringEmpty(column)) {
            checkDatabase();
            Cursor cursor = mDatabase.query(true, TABLE_CHARACTERS, new String[]{COLUMN_ID, column}, COLUMN_ID+ " = ?", new String[]{String.valueOf(id)}, null, null, null, null);
            String json = cursor.getString(cursor.getColumnIndex(column));

            cursor.close();
            return json;
        }
        return null;
    }

    public synchronized boolean fillColumn(long id, String col, String value) {
        if (!Utils.isStringEmpty(value) && !Utils.isStringEmpty(col)) {
            checkDatabase();
            ContentValues values = new ContentValues();
            values.put(col, value);
            // Update TABLE with values WHERE COLUMN_ID = id
            mDatabase.update(TABLE_CHARACTERS, values, COLUMN_ID+" = ?", new String[]{String.valueOf(id)});
            return true;
        }
        return false;
    }


    public int deleteRow(long id) {
        checkDatabase();
        String where = COLUMN_ID + " = ?";
        return mDatabase.delete(TABLE_CHARACTERS, where, new String[]{String.valueOf(id)});
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
