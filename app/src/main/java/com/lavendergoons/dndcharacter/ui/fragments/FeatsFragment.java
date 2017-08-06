package com.lavendergoons.dndcharacter.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lavendergoons.dndcharacter.ui.adapters.FeatAdapter;
import com.lavendergoons.dndcharacter.ui.dialogs.ConfirmationDialog;
import com.lavendergoons.dndcharacter.ui.dialogs.FeatDialog;
import com.lavendergoons.dndcharacter.models.Feat;
import com.lavendergoons.dndcharacter.models.SimpleCharacter;
import com.lavendergoons.dndcharacter.R;
import com.lavendergoons.dndcharacter.utils.CharacterManager;
import com.lavendergoons.dndcharacter.utils.Constants;

import java.util.ArrayList;


public class FeatsFragment extends BaseFragment implements
        View.OnClickListener, ConfirmationDialog.ConfirmationDialogInterface, FeatDialog.FeatDialogListener, FeatAdapter.FeatAdapterListener {

    public static final String TAG = "FEATS_FRAG";


    private RecyclerView mFeatsRecyclerView;
    private FeatAdapter mFeatsRecyclerAdapter;
    private RecyclerView.LayoutManager mFeatsRecyclerLayoutManager;


    private FloatingActionButton addFeatFAB;

    private ArrayList<Feat> featList = new ArrayList<>();
    private CharacterManager characterManager;
    private SimpleCharacter simpleCharacter;
    private Long characterId;

    public FeatsFragment() {
        // Required empty public constructor
    }

    public static FeatsFragment newInstance(SimpleCharacter charIn, long characterId) {
        FeatsFragment fragment = new FeatsFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.CHARACTER_KEY, charIn);
        args.putLong(Constants.CHARACTER_ID, characterId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            characterId = getArguments().getLong(Constants.CHARACTER_ID);
            simpleCharacter = getArguments().getParcelable(Constants.CHARACTER_KEY);
        }
        characterManager = CharacterManager.getInstance();
        //featList = characterManager.getCharacterFeats();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_feats, container, false);


        mFeatsRecyclerView = (RecyclerView) rootView.findViewById(R.id.featsRecyclerView);

        // Keeps View same size on content change
        mFeatsRecyclerView.setHasFixedSize(true);

        mFeatsRecyclerLayoutManager = new LinearLayoutManager(this.getContext());
        mFeatsRecyclerView.setLayoutManager(mFeatsRecyclerLayoutManager);

        mFeatsRecyclerAdapter = new FeatAdapter(this, featList);
        mFeatsRecyclerView.setAdapter(mFeatsRecyclerAdapter);

        addFeatFAB = (FloatingActionButton) rootView.findViewById(R.id.addFeatFAB);
        addFeatFAB.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onStop() {
        //characterManager.setCharacterFeats(featList);
        super.onStop();
    }

    @Override
    public String getTitle() {
        return getString(R.string.title_fragment_feats);
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }

    @Override
    public void onClick(View view) {
        new FeatDialog(this).showDialog(null);
    }

    @Override
    public void ConfirmDialogOk(Object o) {
        if (o != null && o instanceof Feat) {
            featList.remove(o);
        }
        mFeatsRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void ConfirmDialogCancel(Object o) {

    }

    @Override
    public void OnFeatPositive(Feat feat) {
        if (feat != null) {
            featList.add(feat);
        }
        mFeatsRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void OnFeatNegative(Feat feat) {

    }

    @Override
    public void removeFeat(Feat feat) {
        ConfirmationDialog.showConfirmDialog(this.getContext(), getString(R.string.confirm_delete_feat), this, feat);
    }
}
