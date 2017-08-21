package com.lavendergoons.dndcharacter.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lavendergoons.dndcharacter.ui.dialogs.ConfirmationDialog;
import com.lavendergoons.dndcharacter.ui.dialogs.ItemGeneralDialog;
import com.lavendergoons.dndcharacter.models.SimpleCharacter;
import com.lavendergoons.dndcharacter.models.Item;
import com.lavendergoons.dndcharacter.R;
import com.lavendergoons.dndcharacter.utils.CharacterManager;
import com.lavendergoons.dndcharacter.utils.Constants;
import com.lavendergoons.dndcharacter.ui.adapters.ItemsGeneralAdapter;

import java.util.ArrayList;

/**
 * Fragment to display General Items ie. Bedroll, Potions, Flint/Steel
 */
public class ItemsGeneralFragment extends BaseFragment
        implements View.OnClickListener, ItemGeneralDialog.ItemsGeneralDialogListener, ItemsGeneralAdapter.ItemsGeneralAdapterListener, ConfirmationDialog.ConfirmationDialogInterface {

    public static final String TAG = "ITEMS_GENERAL_FRAG";


    private RecyclerView mItemsRecyclerView;
    private RecyclerView.Adapter mItemsRecyclerAdapter;
    private RecyclerView.LayoutManager mItemsRecyclerLayoutManager;
    private CharacterManager characterManager;

    private ArrayList<Item> itemList = new ArrayList<>();
    private SimpleCharacter simpleCharacter;
    private long id = -1;
    private FloatingActionButton addItemFAB;

    public ItemsGeneralFragment() {
        // Required empty public constructor
    }

    public static ItemsGeneralFragment newInstance(SimpleCharacter simpleCharacter, long characterId) {
        ItemsGeneralFragment frag = new ItemsGeneralFragment();
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
            id = getArguments().getLong(Constants.CHARACTER_ID);
            simpleCharacter = getArguments().getParcelable(Constants.CHARACTER_KEY);
        }
        characterManager = CharacterManager.getInstance();
        //itemList = characterManager.getCharacterItems();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_items_general, container, false);
        mItemsRecyclerView = (RecyclerView) rootView.findViewById(R.id.itemsGeneralRecyclerView);

        // Keeps View same size on content change
        mItemsRecyclerView.setHasFixedSize(true);

        mItemsRecyclerLayoutManager = new LinearLayoutManager(this.getContext());
        mItemsRecyclerView.setLayoutManager(mItemsRecyclerLayoutManager);

        mItemsRecyclerAdapter = new ItemsGeneralAdapter(this, itemList);
        mItemsRecyclerView.setAdapter(mItemsRecyclerAdapter);

        addItemFAB = (FloatingActionButton) rootView.findViewById(R.id.addItemFAB);
        addItemFAB.setOnClickListener(this);
        return rootView;
    }

    private void writeItemsGeneral() {
       //characterManager.setCharacterItems(itemList);
    }

    @Override
    public int getTitle() {
        return R.string.title_fragment_items;
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }

    @Override
    public void onStop() {
        super.onStop();
        writeItemsGeneral();
    }

    @Override
    public void OnItemsPositive(Item item) {
        if (item != null) {
            itemList.add(item);
        }
        mItemsRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void OnItemsNegative() {}

    @Override
    public void ConfirmDialogOk(Object item) {
        if(item instanceof Item) {
            itemList.remove(item);
            mItemsRecyclerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void ConfirmDialogCancel(Object o) {}

    @Override
    public void removeItem(Item item) {
        ConfirmationDialog.showConfirmDialog(this.getActivity(), getString(R.string.confirm_delete_item), this, item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addItemFAB:
                new ItemGeneralDialog(this, null).showDialog();
                break;
        }
    }

}
