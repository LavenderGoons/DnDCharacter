package com.lavendergoons.dndcharacter.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lavendergoons.dndcharacter.DndApplication;
import com.lavendergoons.dndcharacter.data.DatabaseHelper;
import com.lavendergoons.dndcharacter.models.Attributes;
import com.lavendergoons.dndcharacter.models.SimpleCharacter;
import com.lavendergoons.dndcharacter.R;
import com.lavendergoons.dndcharacter.ui.adapters.AttributesAdapter;
import com.lavendergoons.dndcharacter.utils.CharacterManager2;
import com.lavendergoons.dndcharacter.utils.Constants;
import com.lavendergoons.dndcharacter.utils.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


public class AttributesFragment extends BaseFragment implements AttributesAdapter.AttributesAdapterListener {

    public static final String TAG = "ATTRIBUTES_FRAG";

    private CompositeDisposable disposable = new CompositeDisposable();
    private SimpleCharacter simpleCharacter;
    private Attributes attributes;

    Unbinder unbinder;
    Gson gson = new Gson();

    // General
    @BindView(R.id.attrNameEdit) EditText attrNameEdit;
    @BindView(R.id.attrLevelEdit) EditText attrLevelEdit;
    @BindView(R.id.attrClassEdit) EditText attrClazzEdit;
    @BindView(R.id.attrExperienceEdit) EditText attrXpEdit;

    // Physical
    @BindView(R.id.attrGenderEdit) EditText attrGenderEdit;
    @BindView(R.id.attrHeightEdit) EditText attrHeightEdit;
    @BindView(R.id.attrWeightEdit) EditText attrWeightEdit;
    @BindView(R.id.attrEyesEdit) EditText attrEyesEdit;
    @BindView(R.id.attrHairEdit) EditText attrHairEdit;
    @BindView(R.id.attrSkinEdit) EditText attrSkinEdit;

    // Detail
    @BindView(R.id.attrRaceEdit) EditText attrRaceEdit;
    @BindView(R.id.attrDeityEdit) EditText attrDeityEdit;
    @BindView(R.id.attrSizeEdit) EditText attrSizeEdit;
    @BindView(R.id.attrAgeEdit) EditText attrAgeEdit;
    @BindView(R.id.attrAlignEdit) EditText attrAlignEdit;


    @Inject CharacterManager2 characterManager;

    private long characterId = -1;

    public AttributesFragment() {
        // Required empty public constructor
    }

    public static AttributesFragment newInstance(SimpleCharacter charIn, long characterId) {
        AttributesFragment frag = new AttributesFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.CHARACTER_KEY, charIn);
        args.putLong(Constants.CHARACTER_ID, characterId);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DndApplication.get(this).getAppComponent().inject(this);
        if (getArguments() != null) {
            characterId = getArguments().getLong(Constants.CHARACTER_ID);
            simpleCharacter = getArguments().getParcelable(Constants.CHARACTER_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_attributes2, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Observable<String> observable = characterManager.getObservableJson(characterId, DatabaseHelper.COLUMN_ATTRIBUTES);
        Log.d(TAG, "Get Observable");
        disposable.add(observable.map(new Function<String, Attributes>() {
            @Override
            public Attributes apply(@NonNull String json) throws Exception {
                return jsonParse(json);
            }
        }).subscribe(new Consumer<Attributes>() {
            @Override
            public void accept(@NonNull Attributes attributes) throws Exception {
                setData(attributes);
                loadContentInViews();
            }
        }));
    }

    private void setData(Attributes attributes) {
        this.attributes = attributes;
    }

    @Override
    public void onStop() {
        super.onStop();
        disposable.clear();
    }

    /*
     * Get JSON String from DB and Parse it to Attributes Object
     * The String may be Arraylist of type String or Attributes Object
     */
    private Attributes jsonParse(String json) {
        Attributes attributes = new Attributes();
        Log.d(TAG, "Parsing JSON: "+json);
        boolean listException = false;
        try {
            Type listType = new TypeToken<ArrayList<String>>(){}.getType();
            ArrayList<String> list = gson.fromJson(json, listType);
            Log.d(TAG, "JSON to List: "+list.toString());
            attributes = mapAttrListToObject(list);
        } catch (JsonSyntaxException ex) {
            Log.i(TAG, "Attributes is NOT A LIST");
            listException = true;
        }
        if (listException) {
            try {
                Type attrType = new TypeToken<Attributes>(){}.getType();
                attributes = gson.fromJson(json, attrType);
            } catch (JsonSyntaxException ex) {
                Utils.logError(ex, TAG, "Parsing Error with Attributes OBJECT");
            }
        }
        return attributes;
    }

    private Attributes mapAttrListToObject(ArrayList<String> list) {
        Attributes attr = new Attributes();
        if (list.size() != 0) {
            Log.d(TAG, "List NOT empty Mapping");
            attr.setName(list.get(0));
            attr.setLevel(Utils.stringToInt(list.get(2)));
            attr.setClazz(list.get(1));
            attr.setXp(Utils.stringToInt(list.get(3)));
            attr.setRace(list.get(4));
            attr.setAlignment(list.get(5));
            attr.setDeity(list.get(6));
            attr.setSize(list.get(7));

            attr.setAge(Utils.stringToInt(list.get(8)));
            attr.setGender(list.get(9));
            attr.setHeight(Utils.stringToFloat(list.get(10)));
            attr.setWeight(Utils.stringToFloat(list.get(11)));
            attr.setEyes(list.get(12));
            attr.setHair(list.get(13));
            attr.setSkin(list.get(14));
        }
        Log.d(TAG, "Returning Attributes: "+attr.toString());
        return attr;
    }

    private void loadContentInViews() {
        attrNameEdit.setText(this.attributes.getName());
        attrLevelEdit.setText(String.valueOf(this.attributes.getLevel()));
        attrClazzEdit.setText(this.attributes.getClazz());

        attrXpEdit.setText(String.valueOf(this.attributes.getXp()));
        attrAgeEdit.setText(String.valueOf(this.attributes.getAge()));
        attrGenderEdit.setText(this.attributes.getGender());

        attrHeightEdit.setText(String.valueOf(this.attributes.getHeight()));
        attrWeightEdit.setText(String.valueOf(this.attributes.getWeight()));

        attrEyesEdit.setText(this.attributes.getEyes());
        attrHairEdit.setText(this.attributes.getHair());
        attrSkinEdit.setText(this.attributes.getSkin());
        attrRaceEdit.setText(this.attributes.getRace());
        attrDeityEdit.setText(this.attributes.getDeity());
        attrSizeEdit.setText(this.attributes.getSize());
        attrAlignEdit.setText(this.attributes.getAlignment());
    }

    private void pullContentsFromView() {
        String name = attrNameEdit.getText().toString();
        String level = attrLevelEdit.getText().toString();
        if (!Utils.isStringEmpty(name)) {
            this.attributes.setName(name);
            this.simpleCharacter.setName(name);
        } else {
            Toast.makeText(this.getContext(), getString(R.string.warning_no_empty_name), Toast.LENGTH_LONG).show();
        }

        if (!Utils.isStringEmpty(level)) {
            this.attributes.setLevel(Utils.stringToInt(level));
            this.simpleCharacter.setLevel(Utils.stringToInt(level));
        } else {
            Toast.makeText(this.getContext(), getString(R.string.warning_no_empty_level), Toast.LENGTH_LONG).show();
        }

        this.attributes.setClazz(attrClazzEdit.getText().toString());

        this.attributes.setXp(Utils.stringToInt(attrXpEdit.getText().toString()));
        this.attributes.setAge(Utils.stringToInt(attrAgeEdit.getText().toString()));
        this.attributes.setGender(attrGenderEdit.getText().toString());

        this.attributes.setHeight(Utils.stringToFloat(attrHeightEdit.getText().toString()));
        this.attributes.setWeight(Utils.stringToFloat(attrWeightEdit.getText().toString()));

        this.attributes.setEyes(attrEyesEdit.getText().toString());
        this.attributes.setHair(attrHairEdit.getText().toString());
        this.attributes.setSkin(attrSkinEdit.getText().toString());
        this.attributes.setRace(attrRaceEdit.getText().toString());
        this.attributes.setDeity(attrDeityEdit.getText().toString());
        this.attributes.setSize(attrSizeEdit.getText().toString());
        this.attributes.setAlignment(attrAlignEdit.getText().toString());
    }

    @Override
    public void onPause() {
        super.onPause();
        writeAttributes();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void writeAttributes() {
        pullContentsFromView();
        characterManager.setAttributes(characterId, this.attributes);
        characterManager.setSimpleCharacter(characterId, this.simpleCharacter);
    }

    @Override
    public int getTitle() {
        return R.string.title_fragment_attributes;
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }
}
