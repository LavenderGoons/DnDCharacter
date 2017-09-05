package com.lavendergoons.dndcharacter.ui.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.lavendergoons.dndcharacter.ui.dialogs.ConfirmationDialog;
import com.lavendergoons.dndcharacter.models.Armor;
import com.lavendergoons.dndcharacter.models.SimpleCharacter;
import com.lavendergoons.dndcharacter.R;
import com.lavendergoons.dndcharacter.ui.adapters.ArmorAdapter;
import com.lavendergoons.dndcharacter.utils.CharacterManager;
import com.lavendergoons.dndcharacter.utils.Constants;
import com.lavendergoons.dndcharacter.utils.Utils;

import java.util.ArrayList;

import static android.R.attr.dialogLayout;


/**
 * Fragment to hold armor recycler view
 */
public class ArmorListFragment extends BaseFragment implements
        View.OnClickListener, ConfirmationDialog.ConfirmationDialogInterface, ArmorAdapter.ArmorAdapterListener {

    public static final String TAG = ArmorListFragment.class.getCanonicalName();

    private RecyclerView mArmorRecyclerView;
    private RecyclerView.Adapter mArmorRecyclerAdapter;
    private RecyclerView.LayoutManager mArmorRecyclerLayoutManager;
    private CharacterManager characterManager;

    private ArrayList<Armor> armorList = new ArrayList<>();
    private SimpleCharacter simpleCharacter;
    private long characterId = -1;

    private FloatingActionButton addArmorFAB;

    public ArmorListFragment() {
        // Required empty public constructor
    }

    public static ArmorListFragment newInstance(SimpleCharacter simpleCharacter, long characterId) {
        ArmorListFragment frag = new ArmorListFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.CHARACTER_KEY, simpleCharacter);
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
        characterManager = CharacterManager.getInstance();
        //armorList = characterManager.getCharacterArmor();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_armor_list, container, false);
        mArmorRecyclerView = (RecyclerView) rootView.findViewById(R.id.armorRecyclerView);

        // Keeps View same size on content change
        mArmorRecyclerView.setHasFixedSize(true);

        mArmorRecyclerLayoutManager = new LinearLayoutManager(this.getContext());
        mArmorRecyclerView.setLayoutManager(mArmorRecyclerLayoutManager);

        mArmorRecyclerAdapter = new ArmorAdapter(this, armorList);
        mArmorRecyclerView.setAdapter(mArmorRecyclerAdapter);

        addArmorFAB = (FloatingActionButton) rootView.findViewById(R.id.addArmorFAB);
        addArmorFAB.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onStop() {
        //characterManager.setCharacterArmor(armorList);
        super.onStop();
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
    public void onClick(View view) {
        showDialog();
    }

    private void launchArmorDetail(Armor armor) {
        LayoutInflater layoutInflater = this.getLayoutInflater(null);
    }

    private int addArmor(Armor armor) {
        int i = -1;
        if (armor != null) {
            armorList.add(armor);
            i = armorList.indexOf(armor);
            mArmorRecyclerAdapter.notifyDataSetChanged();
        }
        return i;
    }

    @Override
    public void removeArmor(Armor armor) {
        ConfirmationDialog.showConfirmDialog(this.getContext(), getString(R.string.confirm_delete_armor), this, armor);
    }

    @Override
    public void ConfirmDialogOk(Object armor) {
        if (armor instanceof Armor) {
            armorList.remove(armor);
            mArmorRecyclerAdapter.notifyDataSetChanged();
        }
    }


    private void showDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        LayoutInflater inflater = this.getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_armor2, null);

        final EditText armorDialogName, armorDialogWeight, armorDialogQuantity;
        armorDialogName = (EditText) view.findViewById(R.id.armorDialogName);
        armorDialogWeight = (EditText) view.findViewById(R.id.armorDialogWeight);
        armorDialogQuantity = (EditText) view.findViewById(R.id.armorDialogQuantity);

        builder.setTitle(this.getString(R.string.title_armor_dialog));
        builder.setView(dialogLayout).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String name = armorDialogName.getText().toString();
                int weight = Utils.checkIntNotNull(armorDialogWeight);
                int quantity = Utils.checkIntNotNull(armorDialogQuantity);

                if (!Utils.isStringEmpty(name)) {
                    Armor armor = new Armor(name, weight, quantity);
                    int index = ArmorListFragment.this.addArmor(armor);
                    ArmorListFragment.this.launchArmorDetail(armor);
                    FragmentTransaction fragTransaction = ArmorListFragment.this.getActivity().getSupportFragmentManager().beginTransaction();
                    fragTransaction.replace(R.id.content_character_nav, ArmorFragment.newInstance(armor, index), ArmorFragment.TAG).addToBackStack(ArmorFragment.TAG).commit();
                } else {
                    Toast.makeText(ArmorListFragment.this.getActivity(), ArmorListFragment.this.getString(R.string.warning_enter_required_fields), Toast.LENGTH_LONG).show();
                }
            }
        }).create().show();
    }

}
