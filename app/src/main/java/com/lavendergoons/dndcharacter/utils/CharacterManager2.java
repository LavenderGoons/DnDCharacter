package com.lavendergoons.dndcharacter.utils;



import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lavendergoons.dndcharacter.data.DBAdapter;
import com.lavendergoons.dndcharacter.data.DatabaseHelper;
import com.lavendergoons.dndcharacter.di.scope.DataScope;
import com.lavendergoons.dndcharacter.models.Abilities;
import com.lavendergoons.dndcharacter.models.Armor;
import com.lavendergoons.dndcharacter.models.Attack;
import com.lavendergoons.dndcharacter.models.Attributes;
import com.lavendergoons.dndcharacter.models.Feat;
import com.lavendergoons.dndcharacter.models.Item;
import com.lavendergoons.dndcharacter.models.Note;
import com.lavendergoons.dndcharacter.models.SimpleCharacter;
import com.lavendergoons.dndcharacter.models.Skill;
import com.lavendergoons.dndcharacter.models.Spell;

import java.lang.reflect.Type;
import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

@DataScope
public class CharacterManager2 {

    public static final String TAG = CharacterManager2.class.getCanonicalName();

    private Character2 character2;
    private DBAdapter dbAdapter;
    private Gson gson;

    @Inject
    public CharacterManager2(DBAdapter dbAdapter) {
        this.dbAdapter = dbAdapter;
        gson = new Gson();
    }

    private void checkCharacter() {
        if (character2 == null) {character2 = new Character2();}
    }

    //**********************************************************
    // Character Management Methods
    //**********************************************************

    public ArrayList<SimpleCharacter> getSimpleCharacters() {
        return dbAdapter.getSimpleCharacters();
    }

    public Observable<ArrayList<SimpleCharacter>> getSimpleCharacterObservable() {
        return dbAdapter.getSimpleCharacterObservable();
    }

    public void createCharacter(SimpleCharacter simpleCharacter) {
        String characterJson = gson.toJson(simpleCharacter);
        dbAdapter.insertRow(characterJson);
    }

    /*
    public void setSimpleCharacter(long id, SimpleCharacter simpleCharacter) {
        writeToDatabase(id, DatabaseHelper.COLUMN_CHARACTER, gson.toJson(simpleCharacter));
    }*/

    public void setSimpleCharacter(long id, SimpleCharacter simpleCharacter) {
        writeToDatabaseSingle(id, DatabaseHelper.COLUMN_CHARACTER, gson.toJson(simpleCharacter));
    }

    public void removeCharacter(SimpleCharacter simpleCharacter) {
        long index = dbAdapter.getCharacterId(simpleCharacter.getName());
        dbAdapter.deleteRow(index);
    }

    public void removeCharacter(int index) {
        dbAdapter.deleteRow((long)index);
    }

    public long getCharacterId(SimpleCharacter simpleCharacter) {
        return dbAdapter.getCharacterId(simpleCharacter.getName());
    }

    //**********************************************************
    // Character Accessor Methods
    //**********************************************************

    public Observable<String> getObservableJson(long id, String column) {
        checkCharacter();
        return dbAdapter.getObservableJson(id, column);
    }

    public void setAbilities(long id, Abilities abilities) {
        writeToDatabaseSingle(id, DatabaseHelper.COLUMN_ABILITIES, gson.toJson(abilities));
    }

    public ArrayList<Armor> getArmor(long id) {
        checkCharacter();
        if (character2.getArmorList().size() == 0) {
            Type type = new TypeToken<ArrayList<Armor>>(){}.getType();
            ArrayList<Armor> list = dbAdapter.getListColumn(id, DatabaseHelper.COLUMN_ARMOR, type);
            character2.setArmorList(list);
            list = null;
        }
        return character2.getArmorList();
    }

    public ArrayList<Attack> getAttacks(long id) {
        checkCharacter();
        if (character2.getAttackList().size() == 0) {
            Type type = new TypeToken<ArrayList<Attack>>(){}.getType();
            ArrayList<Attack> list = dbAdapter.getListColumn(id, DatabaseHelper.COLUMN_ATTACK, type);
            character2.setAttackList(list);
            list = null;
        }
        return character2.getAttackList();
    }

    public void setAttributes(long id, Attributes attributes) {
        writeToDatabaseSingle(id, DatabaseHelper.COLUMN_ATTRIBUTES, gson.toJson(attributes));
    }

    public ArrayList<Feat> getFeats(long id) {
        checkCharacter();
        if (character2.getFeatList().size() == 0) {
            Type type = new TypeToken<ArrayList<Feat>>(){}.getType();
            ArrayList<Feat> list = dbAdapter.getListColumn(id, DatabaseHelper.COLUMN_FEATS, type);
            character2.setFeatList(list);
            list = null;
        }
        return character2.getFeatList();
    }

    public ArrayList<Item> getItems(long id) {
        checkCharacter();
        if (character2.getItemList().size() == 0) {
            Type type = new TypeToken<ArrayList<Item>>(){}.getType();
            ArrayList<Item> list = dbAdapter.getListColumn(id, DatabaseHelper.COLUMN_ITEM_GENERAL, type);
            character2.setItemList(list);
            list = null;
        }
        return character2.getItemList();
    }

    public ArrayList<Note> getNotes(long id) {
        checkCharacter();
        if (character2.getNotesList().size() == 0) {
            Type type = new TypeToken<ArrayList<Note>>(){}.getType();
            ArrayList<Note> list = dbAdapter.getListColumn(id, DatabaseHelper.COLUMN_NOTES, type);
            character2.setNotesList(list);
            list = null;
        }
        return character2.getNotesList();
    }

    public void setSkills(final long id, ArrayList<Skill> skills) {
        writeToDatabaseSingle(id, DatabaseHelper.COLUMN_SKILL, gson.toJson(skills));
    }

    public ArrayList<Spell> getSpell(long id) {
        checkCharacter();
        if (character2.getSpellList().size() == 0) {
            Type type = new TypeToken<ArrayList<Spell>>(){}.getType();
            ArrayList<Spell> list = dbAdapter.getListColumn(id, DatabaseHelper.COLUMN_SPELL, type);
            character2.setSpellList(list);
            list = null;
        }
        return character2.getSpellList();
    }

    private void writeToDatabase(long id, String column, String json) {
        dbAdapter.fillColumn(id, column, json);

    }

    private void writeToDatabaseSingle(final long id, final String column, String json) {
        Single.just(json)
                .subscribeOn(Schedulers.computation())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        dbAdapter.fillColumn(id, column, s);
                    }
                });
    }

    //**********************************************************
    // Private Character Object
    //**********************************************************

    private class Character2 {
        private String name;
        private int level;

        private Abilities abilities;
        private ArrayList<Armor> armorList = new ArrayList<>();
        private ArrayList<Attack> attackList = new ArrayList<>();
        private ArrayList<String> attributesList = new ArrayList<>(Constants.ATTRIBUTES.length);
        private ArrayList<Feat> featList = new ArrayList<>();
        private ArrayList<Item> itemList = new ArrayList<>();
        private ArrayList<Note> notesList = new ArrayList<>();
        private ArrayList<Skill> skillsList = new ArrayList<>();
        private ArrayList<Spell> spellList = new ArrayList<>();

        private Character2() {

        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        Abilities getAbilities() {
            return abilities;
        }

        void setAbilities(Abilities abilities) {
            this.abilities = abilities;
        }

        ArrayList<Armor> getArmorList() {
            return armorList;
        }

        void setArmorList(ArrayList<Armor> armorList) {
            this.armorList = armorList;
        }

        ArrayList<Attack> getAttackList() {
            return attackList;
        }

        void setAttackList(ArrayList<Attack> attackList) {
            this.attackList = attackList;
        }

        ArrayList<String> getAttributesList() {
            return attributesList;
        }

        void setAttributesList(ArrayList<String> attributesList) {
            this.attributesList = attributesList;
        }

        ArrayList<Feat> getFeatList() {
            return featList;
        }

        void setFeatList(ArrayList<Feat> featList) {
            this.featList = featList;
        }

        ArrayList<Item> getItemList() {
            return itemList;
        }

        void setItemList(ArrayList<Item> itemList) {
            this.itemList = itemList;
        }

        ArrayList<Note> getNotesList() {
            return notesList;
        }

        void setNotesList(ArrayList<Note> notesList) {
            this.notesList = notesList;
        }

        ArrayList<Skill> getSkillsList() {
            return skillsList;
        }

        void setSkillsList(ArrayList<Skill> skillsList) {
            this.skillsList = skillsList;
        }

        public ArrayList<Spell> getSpellList() {
            return spellList;
        }

        void setSpellList(ArrayList<Spell> spellList) {
            this.spellList = spellList;
        }
    }
}