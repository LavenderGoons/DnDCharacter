package com.lavendergoons.dndcharacter.ui.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.crash.FirebaseCrash;
import com.lavendergoons.dndcharacter.BuildConfig;
import com.lavendergoons.dndcharacter.DndApplication;
import com.lavendergoons.dndcharacter.ui.dialogs.ACDialog;
import com.lavendergoons.dndcharacter.ui.dialogs.SavesDialog;
import com.lavendergoons.dndcharacter.ui.dialogs.ScoresDialog;
import com.lavendergoons.dndcharacter.models.Abilities;
import com.lavendergoons.dndcharacter.models.SimpleCharacter;
import com.lavendergoons.dndcharacter.R;
import com.lavendergoons.dndcharacter.utils.CharacterManager2;
import com.lavendergoons.dndcharacter.utils.Constants;


import java.util.Arrays;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static java.lang.Integer.parseInt;
import static com.lavendergoons.dndcharacter.utils.Utils.checkInputNotNull;


/**
 * Abilities Fragment
 */
public class AbilitiesFragment extends BaseFragment implements ACDialog.ACDialogListener, SavesDialog.SavesDialogListener, ScoresDialog.ScoresDialogListener {

    public static final String TAG = "ABILITIES_FRAG";

    @Inject
    CharacterManager2 characterManager;

    // Butterknife view unbinder
    private Unbinder unbinder;

    private SimpleCharacter simpleCharacter;
    private Abilities abilities;
    private long characterId = -1;

    @BindView(R.id.savesEditBtn) Button savesEditBtn;
    @BindView(R.id.acEditBtn) Button acEditBtn;
    @BindView(R.id.scoresEditBtn) Button scoresEditBtn;

    @BindView(R.id.saveFortValue) TextView saveFortValue;
    @BindView(R.id.saveReflexValue) TextView saveReflexValue;
    @BindView(R.id.saveWillValue) TextView saveWillValue;
    @BindView(R.id.acGenValue) TextView acGenValue;
    @BindView(R.id.acTouchValue) TextView acTouchValue;
    @BindView(R.id.acFlatFootValue) TextView acFlatFootValue;

    @BindView(R.id.abilityStrScoreEdit) EditText abilityStrScoreEdit;
    @BindView(R.id.abilityDexScoreEdit) EditText abilityDexScoreEdit;
    @BindView(R.id.abilityConScoreEdit) EditText abilityConScoreEdit;
    @BindView(R.id.abilityIntScoreEdit) EditText abilityIntScoreEdit;
    @BindView(R.id.abilityWisScoreEdit) EditText abilityWisScoreEdit;
    @BindView(R.id.abilityChaScoreEdit) EditText abilityChaScoreEdit;
    @BindView(R.id.abilityStrModEdit) EditText abilityStrModEdit;
    @BindView(R.id.abilityDexModEdit) EditText abilityDexModEdit;
    @BindView(R.id.abilityConModEdit) EditText abilityConModEdit;
    @BindView(R.id.abilityIntModEdit) EditText abilityIntModEdit;
    @BindView(R.id.abilityWisModEdit) EditText abilityWisModEdit;
    @BindView(R.id.abilityChaModEdit) EditText abilityChaModEdit;

    @BindView(R.id.abilityHpEdit) EditText abilityHpEdit;
    @BindView(R.id.abilityNonLethalEdit) EditText abilityNonLethalEdit;
    @BindView(R.id.abilityBaseAtkEdit) EditText abilityBaseAtkEdit;
    @BindView(R.id.abilitySpellResEdit) EditText abilitySpellResEdit;
    @BindView(R.id.abilityInitiativeEdit) EditText abilityInitiativeEdit;
    @BindView(R.id.abilitySpeedEdit) EditText abilitySpeedEdit;

    @BindView(R.id.grappleBaseAttackEdit) EditText grappleBaseAttackEdit;
    @BindView(R.id.grappleStrModEdit) EditText grappleStrModEdit;
    @BindView(R.id.grappleSizeModEdit) EditText grappleSizeModEdit;
    @BindView(R.id.grappleMiscModEdit) EditText grappleMiscModEdit;
    @BindView(R.id.grappleTotalEdit) EditText grappleTotalEdit;

    @BindView(R.id.abilityPlatinumEdit) EditText platinumEdit;
    @BindView(R.id.abilityGoldEdit) EditText goldEdit;
    @BindView(R.id.abilitySilverEdit) EditText silverEdit;
    @BindView(R.id.abilityCopperEdit) EditText copperEdit;

    public AbilitiesFragment() {
        // Required empty public constructor
    }

    public static AbilitiesFragment newInstance(SimpleCharacter charIn, long characterId) {
        AbilitiesFragment frag = new AbilitiesFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.CHARACTER_KEY, charIn);
        args.putLong(Constants.CHARACTER_ID, characterId);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            characterId = getArguments().getLong(Constants.CHARACTER_ID);
            simpleCharacter = getArguments().getParcelable(Constants.CHARACTER_KEY);
        }
        DndApplication.get(this).getAppComponent().inject(this);
        abilities = characterManager.getAbilities(characterId);
        FirebaseCrash.log("Abilities: "+abilities);
    }

    @Override
    public int getTitle() {
        return R.string.title_fragment_abilities;
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_abilities, container, false);
        unbinder = ButterKnife.bind(this, rootView);


        abilityStrScoreEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                abilityStrModEdit.setText(modValue(charSequence));
                grappleStrModEdit.setText(modValue(charSequence));
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });


        abilityDexScoreEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                abilityDexModEdit.setText(modValue(charSequence));
                // Set value of AC DEX, SAVE BASE
                try {
                    abilities.setAC(Integer.parseInt(modValue(charSequence)), Abilities.AC_DEX);
                    abilities.setReflex(Integer.parseInt(modValue(charSequence)), Abilities.SAVE_BASE);
                    setACValues();
                    setSaveValues();
                    abilities.setAC(Integer.parseInt(acGenValue.getText().toString()), Abilities.AC_TOTAL);
                    abilities.setReflex(Integer.parseInt(saveReflexValue.getText().toString()), Abilities.SAVE_TOTAL);
                }catch (Exception ex) {
                    ex.printStackTrace();
                    FirebaseCrash.log(TAG +ex.toString());
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        abilityConScoreEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                abilityConModEdit.setText(modValue(charSequence));
                // Set value of SAVE BASE
                try {
                    abilities.setFort(Integer.parseInt(modValue(charSequence)), Abilities.SAVE_BASE);
                    setSaveValues();
                }catch (Exception ex) {
                    ex.printStackTrace();
                    FirebaseCrash.log(TAG +ex.toString());
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        abilityIntScoreEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                abilityIntModEdit.setText(modValue(charSequence));
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        abilityWisScoreEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                abilityWisModEdit.setText(modValue(charSequence));
                // Set value of SAVE BASE
                try {
                    abilities.setWill(Integer.parseInt(modValue(charSequence)), Abilities.SAVE_BASE);
                    setSaveValues();
                }catch (Exception ex) {
                    ex.printStackTrace();
                    FirebaseCrash.log(TAG +ex.toString());
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        abilityChaScoreEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                abilityChaModEdit.setText(modValue(charSequence));
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // Set Grapple TextWatchers
        grappleBaseAttackEdit.addTextChangedListener(new GrappleTextWatcher());
        grappleStrModEdit.addTextChangedListener(new GrappleTextWatcher());
        grappleSizeModEdit.addTextChangedListener(new GrappleTextWatcher());
        grappleMiscModEdit.addTextChangedListener(new GrappleTextWatcher());

        setValues();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //Butterknife view unbinder
        unbinder.unbind();
    }

    @OnClick({R.id.savesEditBtn, R.id.acEditBtn, R.id.scoresEditBtn})
    public void onClick(View view) {
        writeAbilities();
        switch (view.getId()) {
            case R.id.acEditBtn: {
                new ACDialog(this.getActivity(), this, abilities).showDialog();
                break;
            }
            case R.id.savesEditBtn: {
                new SavesDialog(this.getActivity(), this, abilities).showDialog();
                break;
            }
            case R.id.scoresEditBtn: {
                new ScoresDialog(this.getActivity(), this, abilities).showDialog();
                break;
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        writeAbilities();
    }

    private void writeAbilities() {
        readAbilityGeneralValues();
        readGrappleValues();
        readScoreModValues();
        readMoneyValues();
        if (abilities != null) {
            characterManager.setAbilities(characterId, abilities);
        } else {
            FirebaseCrash.log(TAG+" Abilities Null writeAbilities");
        }
    }

    //**********************************************************
    // Listeners
    //**********************************************************

    @Override
    public void OnACPositive(Abilities abilities) {
        if (abilities == null) {
            FirebaseCrash.log("Abilities Null From OnACPositive");
        } else {
            this.abilities = abilities;
            Log.d(TAG, Arrays.toString(abilities.getACArray()));
        }
        setACValues();
    }

    @Override
    public void OnSavesPositive(Abilities abilities) {
        if (abilities == null) {
            FirebaseCrash.log("Abilities Null From OnSavesPositive");
        } else {
            this.abilities = abilities;
        }
        setSaveValues();
    }

    @Override
    public void OnScoresPositive(Abilities abilities) {
        if (abilities == null) {
            FirebaseCrash.log("Abilities Null From OnScoresPositive");
        } else {
            this.abilities = abilities;
        }
        setScoreModValues();
    }


    //**********************************************************
    // Set Values To EditTexts
    //**********************************************************

    private void setValues() {
        if (abilities != null) {
            setACValues();
            setSaveValues();
            setScoreModValues();
            setAbilityGeneralValues();
            setGrappleValues();
            setMoneyValues();
        }
    }

    private void setACValues() {
        try {
            //acGenValue.setText(String.valueOf(abilities.getAC(Abilities.AC_TOTAL)));
            acGenValue.setText(String.valueOf(abilities.getACTotal()));
            acTouchValue.setText(String.valueOf(abilities.getAcTouch()));
            acFlatFootValue.setText(String.valueOf(abilities.getAcFlatFoot()));
        }catch (Exception ex) {
            ex.printStackTrace();
            FirebaseCrash.log(TAG +ex.toString());
            if (!BuildConfig.DEBUG) {
                FirebaseCrash.report(ex);
            }
        }
    }

    private void setSaveValues() {
        try {
            saveFortValue.setText(String.valueOf(abilities.getFortTotal()));
            saveReflexValue.setText(String.valueOf(abilities.getReflexTotal()));
            saveWillValue.setText(String.valueOf(abilities.getWillTotal()));
        }catch (Exception ex) {
            ex.printStackTrace();
            FirebaseCrash.log(TAG +ex.toString());
            if (!BuildConfig.DEBUG) {
                FirebaseCrash.report(ex);
            }
        }
    }

    private void setScoreModValues() {
        try {
            abilityStrScoreEdit.setText(String.valueOf(abilities.getScore(Abilities.STR)));
            abilityDexScoreEdit.setText(String.valueOf(abilities.getScore(Abilities.DEX)));
            abilityConScoreEdit.setText(String.valueOf(abilities.getScore(Abilities.CON)));
            abilityIntScoreEdit.setText(String.valueOf(abilities.getScore(Abilities.INT)));
            abilityWisScoreEdit.setText(String.valueOf(abilities.getScore(Abilities.WIS)));
            abilityChaScoreEdit.setText(String.valueOf(abilities.getScore(Abilities.CHA)));

            abilityStrModEdit.setText(String.valueOf(abilities.getMod(Abilities.STR)));
            abilityDexModEdit.setText(String.valueOf(abilities.getMod(Abilities.DEX)));
            abilityConModEdit.setText(String.valueOf(abilities.getMod(Abilities.CON)));
            abilityIntModEdit.setText(String.valueOf(abilities.getMod(Abilities.INT)));
            abilityWisModEdit.setText(String.valueOf(abilities.getMod(Abilities.WIS)));
            abilityChaModEdit.setText(String.valueOf(abilities.getMod(Abilities.CHA)));
            //checkAndSetTempValue();
        }catch (Exception ex) {
            ex.printStackTrace();
            FirebaseCrash.log(TAG +ex.toString());
            FirebaseCrash.log("Scores Array: "+Arrays.toString(abilities.getScoreArray()));
            FirebaseCrash.log("Scores Array: "+Arrays.toString(abilities.getModArray()));
            if (!BuildConfig.DEBUG) {
                FirebaseCrash.report(ex);
            }
        }
    }

    private void checkAndSetTempValue() {
        for (int i=0;i<abilities.getScoreTempArray().length;i++) {
            if (abilities.getScoreTempArray()[i] != 0) {
                EditText editText = scoreToEditText(i);
                editText.setText(String.valueOf(abilities.getScoreTempArray()[i]));
            }
        }
        /*for (int i=0;i<abilities.getModTempArray().length;i++) {
            if (abilities.getModTempArray()[i] != 0) {
                EditText editText = modToEditText(i);
                editText.setText(String.valueOf(abilities.getModTempArray()[i]));
            }
        }*/
    }

    //TODO Try and make it less gross
    private EditText scoreToEditText(int i) {
        EditText edit = null;
        switch (i) {
            case Abilities.STR:
                edit = abilityStrScoreEdit;
                break;
            case Abilities.DEX:
                edit = abilityDexScoreEdit;
                break;
            case Abilities.CON:
                edit = abilityConScoreEdit;
                break;
            case Abilities.INT:
                edit = abilityIntScoreEdit;
                break;
            case Abilities.WIS:
                edit = abilityWisScoreEdit;
                break;
            case Abilities.CHA:
                edit = abilityChaScoreEdit;
                break;
        }
        return edit;
    }

    private EditText modToEditText(int i) {
        EditText edit = null;
        switch (i) {
            case Abilities.STR:
                edit = abilityStrModEdit;
                break;
            case Abilities.DEX:
                edit = abilityDexModEdit;
                break;
            case Abilities.CON:
                edit = abilityConModEdit;
                break;
            case Abilities.INT:
                edit = abilityIntModEdit;
                break;
            case Abilities.WIS:
                edit = abilityWisModEdit;
                break;
            case Abilities.CHA:
                edit = abilityChaModEdit;
                break;
        }
        return edit;
    }

    private void setAbilityGeneralValues() {
        try {
            abilityHpEdit.setText(String.valueOf(abilities.getHp()));
            abilityNonLethalEdit.setText(String.valueOf(abilities.getNonLethal()));
            abilityBaseAtkEdit.setText(String.valueOf(abilities.getBaseAtk()));
            abilitySpellResEdit.setText(String.valueOf(abilities.getSpellRes()));
            abilityInitiativeEdit.setText(String.valueOf(abilities.getInitiative()));
            abilitySpeedEdit.setText(String.valueOf(abilities.getSpeed()));
        }catch (Exception ex) {
            ex.printStackTrace();
            FirebaseCrash.log(TAG +ex.toString());
            if (!BuildConfig.DEBUG) {
                FirebaseCrash.report(ex);
            }
        }
    }

    private void setGrappleValues() {
        try {
            grappleBaseAttackEdit.setText(String.valueOf(abilities.getGrapple(Abilities.GRAPPLE_BASE)));
            grappleStrModEdit.setText(String.valueOf(abilities.getGrapple(Abilities.GRAPPLE_STR)));
            grappleSizeModEdit.setText(String.valueOf(abilities.getGrapple(Abilities.GRAPPLE_SIZE)));
            grappleMiscModEdit.setText(String.valueOf(abilities.getGrapple(Abilities.GRAPPLE_MISC)));
            grappleTotalEdit.setText(String.valueOf(abilities.getGrapple(Abilities.GRAPPLE_TOTAL)));
        }catch (Exception ex) {
            ex.printStackTrace();
            FirebaseCrash.log(TAG +ex.toString());
            if (!BuildConfig.DEBUG) {
                FirebaseCrash.report(ex);
            }
        }
    }

    private void setMoneyValues() {
        try {
            platinumEdit.setText(String.valueOf(abilities.getPlatinum()));
            goldEdit.setText(String.valueOf(abilities.getGold()));
            silverEdit.setText(String.valueOf(abilities.getSilver()));
            copperEdit.setText(String.valueOf(abilities.getCopper()));
        }catch (Exception ex) {
            ex.printStackTrace();
            FirebaseCrash.log(TAG +ex.toString());
            if (!BuildConfig.DEBUG) {
                FirebaseCrash.report(ex);
            }
        }
    }

    //**********************************************************
    // Read Values From EditTexts
    //**********************************************************

    private void readAbilityGeneralValues() {
        abilities.setHp(checkInputNotNull(abilityHpEdit));
        abilities.setNonLethal(checkInputNotNull(abilityNonLethalEdit));
        abilities.setBaseAtk(checkInputNotNull(abilityBaseAtkEdit));
        abilities.setSpellRes(checkInputNotNull(abilitySpellResEdit));
        abilities.setInitiative(checkInputNotNull(abilityInitiativeEdit));
        abilities.setSpeed(checkInputNotNull(abilitySpeedEdit));

        abilities.setFort(checkInputNotNull(saveFortValue), Abilities.SAVE_TOTAL);
        abilities.setReflex(checkInputNotNull(saveReflexValue), Abilities.SAVE_TOTAL);
        abilities.setWill(checkInputNotNull(saveWillValue), Abilities.SAVE_TOTAL);

        abilities.setAC(checkInputNotNull(acGenValue), Abilities.AC_TOTAL);
    }

    private void readGrappleValues() {
        int[] array = new int[]{0,0,0,0,0};
        array[Abilities.GRAPPLE_BASE] = checkInputNotNull(grappleBaseAttackEdit);
        array[Abilities.GRAPPLE_STR] = checkInputNotNull(grappleStrModEdit);
        array[Abilities.GRAPPLE_SIZE] = checkInputNotNull(grappleSizeModEdit);
        array[Abilities.GRAPPLE_MISC] = checkInputNotNull(grappleMiscModEdit);
        array[Abilities.GRAPPLE_TOTAL] = checkInputNotNull(grappleTotalEdit);
        abilities.setGrappleArray(array);
    }

    private void readMoneyValues() {
        abilities.setPlatinum(checkInputNotNull(platinumEdit));
        abilities.setGold(checkInputNotNull(goldEdit));
        abilities.setSilver(checkInputNotNull(silverEdit));
        abilities.setCopper(checkInputNotNull(copperEdit));
    }

    private void readScoreModValues() {
        int[] scores = new int[]{0,0,0,0,0,0};
        int[] mods = new int[]{0,0,0,0,0,0};

        scores[Abilities.STR] = checkInputNotNull(abilityStrScoreEdit);
        scores[Abilities.DEX] = checkInputNotNull(abilityDexScoreEdit);
        scores[Abilities.CON] = checkInputNotNull(abilityConScoreEdit);
        scores[Abilities.INT] = checkInputNotNull(abilityIntScoreEdit);
        scores[Abilities.WIS] = checkInputNotNull(abilityWisScoreEdit);
        scores[Abilities.CHA] = checkInputNotNull(abilityChaScoreEdit);

        mods[Abilities.STR] = checkInputNotNull(abilityStrModEdit);
        mods[Abilities.DEX] = checkInputNotNull(abilityDexModEdit);
        mods[Abilities.CON] = checkInputNotNull(abilityConModEdit);
        mods[Abilities.INT] = checkInputNotNull(abilityIntModEdit);
        mods[Abilities.WIS] = checkInputNotNull(abilityWisModEdit);
        mods[Abilities.CHA] = checkInputNotNull(abilityChaModEdit);

        abilities.setScoreArray(scores);
        abilities.setModArray(mods);
    }

    private String modValue(CharSequence score) {
        int mod = 0;
        if (score.length() <= 0) {
            return String.valueOf(mod);
        }
        try {
            mod = parseInt(score.toString());
        }catch (NumberFormatException ex) {
            ex.printStackTrace();
            FirebaseCrash.log(TAG +ex.toString());
        }
        mod = (mod%2==0)? (mod - 10)/2 : (mod - 11)/2;
        return String.valueOf(mod);
    }

    //**********************************************************
    // TextChange Listeners
    //**********************************************************

    private class GrappleTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            int base=0, str=0, size=0, misc=0;
            try {
                base = Integer.parseInt(grappleBaseAttackEdit.getText().toString());
                str = Integer.parseInt(grappleStrModEdit.getText().toString());
                size = Integer.parseInt(grappleSizeModEdit.getText().toString());
                misc = Integer.parseInt(grappleMiscModEdit.getText().toString());
            }catch (Exception ex) {
                ex.printStackTrace();
                FirebaseCrash.log(TAG +ex.toString());
            }
            grappleTotalEdit.setText(String.valueOf(base + str + size + misc));
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    }
}
