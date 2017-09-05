package com.lavendergoons.dndcharacter.ui.fragments;

import android.os.BadParcelableException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.crash.FirebaseCrash;
import com.lavendergoons.dndcharacter.models.Armor;
import com.lavendergoons.dndcharacter.R;
import com.lavendergoons.dndcharacter.utils.CharacterManager;
import com.lavendergoons.dndcharacter.utils.Utils;

import java.util.ArrayList;


public class ArmorFragment extends BaseFragment {

    public static final String TAG = ArmorFragment.class.getCanonicalName();

    private CharacterManager characterManager;
    private ArrayList<Armor> armorList = new ArrayList<>();
    private Armor armor;
    private int index = -1;
    private EditText armorNameEdit, armorTypeEdit, armorACEdit, armorDexEdit, armorCheckEdit, armorSpellEdit, armorSpeedEdit, armorWeightEdit, armorPropertiesEdit, armorQuantityEdit;

    public ArmorFragment() {
        // Required empty public constructor
    }

    public static ArmorFragment newInstance(Armor armor, int index) {
        ArmorFragment frag = new ArmorFragment();
        if (armor == null) {
            armor = new Armor();
        }
        Bundle args = new Bundle();
        args.putParcelable("ARMOR", armor);
        args.putInt("INDEX", index);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            armor = getArguments() != null ? (Armor) getArguments().getParcelable("ARMOR") : new Armor();
            index = getArguments().getInt("INDEX");
        }catch(BadParcelableException ex) {
            Log.e("PARSE", "Bad Parcelable in ArmorFragment");
        }
        characterManager = CharacterManager.getInstance();
        //armorList = characterManager.getCharacterArmor();
    }

    @Override
    public int getTitle() {
        return R.string.title_fragment_armor;
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_armor2, container, false);
        armorNameEdit = (EditText) rootView.findViewById(R.id.armorNameEdit);
        armorTypeEdit = (EditText) rootView.findViewById(R.id.armorTypeEdit);
        armorACEdit = (EditText) rootView.findViewById(R.id.armorACEdit);
        armorDexEdit = (EditText) rootView.findViewById(R.id.armorDexEdit);
        armorCheckEdit = (EditText) rootView.findViewById(R.id.armorCheckEdit);
        armorSpellEdit = (EditText) rootView.findViewById(R.id.armorSpellEdit);
        armorSpeedEdit = (EditText) rootView.findViewById(R.id.armorSpeedEdit);
        armorWeightEdit = (EditText) rootView.findViewById(R.id.armorWeightEdit);
        armorPropertiesEdit = (EditText) rootView.findViewById(R.id.armorPropertiesEdit);
        armorQuantityEdit = (EditText) rootView.findViewById(R.id.armorQuantityEdit);
        getValues();
        return rootView;
    }

    // Set the Armor object with new values
    private void setValues() {
        try {
            armor.setName(armorNameEdit.getText().toString());
            armor.setType(armorTypeEdit.getText().toString());
            armor.setAcBonus(Utils.checkIntNotNull(armorACEdit));
            armor.setMaxDex(Utils.checkIntNotNull(armorDexEdit));
            armor.setCheckPenalty(Utils.checkIntNotNull(armorCheckEdit));
            armor.setSpellFailure(Utils.checkIntNotNull(armorSpellEdit));
            armor.setWeight(Utils.checkIntNotNull(armorSpeedEdit));
            armor.setSpeed(Utils.checkIntNotNull(armorWeightEdit));
            armor.setProperties(armorPropertiesEdit.getText().toString());
            armor.setQuantity(Utils.checkIntNotNull(armorQuantityEdit));
        } catch (Exception ex) {
            ex.printStackTrace();
            FirebaseCrash.log(TAG +ex.toString());
        }
    }

    // Get values from Init Armor Object
    private void getValues() {
        try {
            armorNameEdit.setText(armor.getName());
            armorTypeEdit.setText(armor.getType());
            armorACEdit.setText(String.valueOf(armor.getAcBonus()));
            armorDexEdit.setText(String.valueOf(armor.getMaxDex()));
            armorCheckEdit.setText(String.valueOf(armor.getCheckPenalty()));
            armorSpellEdit.setText(String.valueOf(armor.getSpellFailure()));
            armorSpeedEdit.setText(String.valueOf(armor.getSpeed()));
            armorWeightEdit.setText(String.valueOf(armor.getWeight()));
            armorPropertiesEdit.setText(armor.getProperties());
            armorQuantityEdit.setText(String.valueOf(armor.getQuantity()));
        } catch (Exception ex) {
            ex.printStackTrace();
            FirebaseCrash.log(TAG +ex.toString());
        }
    }

    @Override
    public void onStop() {
        setValues();
        if (index >= 0 && index < armorList.size()) {
            armorList.set(index, armor);
            //characterManager.setCharacterArmor(armorList);
        } else {
            FirebaseCrash.log(TAG + " Index out of Bounds. Index: "+index);
        }
        super.onStop();
    }
}
