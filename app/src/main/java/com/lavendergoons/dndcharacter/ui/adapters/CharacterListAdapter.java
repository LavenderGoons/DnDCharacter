package com.lavendergoons.dndcharacter.ui.adapters;

import android.content.Context;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lavendergoons.dndcharacter.models.SimpleCharacter;
import com.lavendergoons.dndcharacter.R;
import com.lavendergoons.dndcharacter.utils.Constants;

import java.util.ArrayList;

/**
 * RecyclerView list adapter for SimpleCharacter list.
 */
public class CharacterListAdapter extends RecyclerView.Adapter<CharacterListAdapter.ViewHolder> {

    public static final String TAG = CharacterListAdapter.class.getCanonicalName();

    private ArrayList<SimpleCharacter> mDataset;
    private CharacterListAdapterListener listener;
    private Fragment fragment;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View cardView;
        public TextView mNameTextView;
        public TextView mLevelTextView;
        public ViewHolder(View view) {
            super(view);
            this.cardView = view;
            mNameTextView = (TextView) view.findViewById(R.id.characterListName);
            mLevelTextView = (TextView) view.findViewById(R.id.characterListLevel);
        }
    }

    public CharacterListAdapter(Fragment fragment, ArrayList<SimpleCharacter> dataset) {
        this.fragment = fragment;
        this.mDataset = dataset;
        if (fragment instanceof CharacterListAdapterListener) {
            this.listener = (CharacterListAdapterListener) fragment;
        } else {
            throw new RuntimeException(fragment.toString()
                + " must implement CharacterListAdapterListener.");
        }
    }

    public interface CharacterListAdapterListener {
        void onCharacterClick(SimpleCharacter simpleCharacter);
        void removeCharacter(SimpleCharacter simpleCharacter);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_character_list, parent, false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mNameTextView.setText(mDataset.get(position).getName());
        holder.mLevelTextView.setText(String.valueOf(mDataset.get(position).getLevel()));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCardClick(holder, holder.getAdapterPosition());
            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onCardLongClick(holder, holder.getAdapterPosition());
                return true;
            }
        });
    }

    public void setData(ArrayList<SimpleCharacter> dataIn) {
        this.mDataset = dataIn;
        this.notifyDataSetChanged();
    }

    private void onCardClick(ViewHolder holder, int position) {
        String name = holder.mNameTextView.getText().toString();
        listener.onCharacterClick(mDataset.get(position));
    }

    private void onCardLongClick(ViewHolder holder, int position) {
        Vibrator v = (Vibrator) fragment.getContext().getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(Constants.LONG_CLICK_VIBRATION);
        listener.removeCharacter(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}