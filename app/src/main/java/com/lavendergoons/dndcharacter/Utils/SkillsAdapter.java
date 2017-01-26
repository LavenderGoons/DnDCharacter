package com.lavendergoons.dndcharacter.Utils;

import android.content.Context;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.lavendergoons.dndcharacter.Dialogs.ConfirmationDialog;
import com.lavendergoons.dndcharacter.Fragments.SkillsFragment;
import com.lavendergoons.dndcharacter.Objects.Skill;
import com.lavendergoons.dndcharacter.R;

import java.util.ArrayList;

/**
 * Adapter for skill RecyclerView
 */

public class SkillsAdapter extends RecyclerView.Adapter<SkillsAdapter.ViewHolder> {

    private SkillsFragment skillsFragment;
    private ArrayList<Skill> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View cardView;
        public CheckBox skillCheckBox;
        public TextView skillModTypeText;
        public EditText skillTotalEdit;
        public EditText skillModEdit;
        public EditText skillRankEdit;
        public EditText skillMiscEdit;

        public ViewHolder(View view) {
            super(view);
            this.cardView = view;
            skillCheckBox = (CheckBox) view.findViewById(R.id.skillCheckBox);
            skillModTypeText = (TextView) view.findViewById(R.id.skillModTypeText);
            skillTotalEdit = (EditText) view.findViewById(R.id.skillTotalEdit);
            skillModEdit = (EditText) view.findViewById(R.id.skillModEdit);
            skillRankEdit = (EditText) view.findViewById(R.id.skillRankEdit);
            skillMiscEdit = (EditText) view.findViewById(R.id.skillMiscEdit);
        }
    }

    public SkillsAdapter(SkillsFragment skillsFragment, ArrayList<Skill> dataset) {
        this.skillsFragment = skillsFragment;
        this.mDataset = dataset;
    }

    @Override
    public SkillsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_skills, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.skillCheckBox.setText(mDataset.get(position).getName());
        holder.skillCheckBox.setChecked(mDataset.get(position).isTrained());

        holder.skillModTypeText.setText(mDataset.get(position).getModType());

        holder.skillTotalEdit.setText(String.valueOf(mDataset.get(position).getTotal()));
        holder.skillModEdit.setText(String.valueOf(mDataset.get(position).getMod()));
        holder.skillRankEdit.setText(String.valueOf(mDataset.get(position).getRank()));
        holder.skillMiscEdit.setText(String.valueOf(mDataset.get(position).getMisc()));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCardClick();
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return onCardLongClick();
            }
        });
    }

    private void onCardClick() {

    }

    private boolean onCardLongClick() {
        //TODO Clean Up
        Vibrator v = (Vibrator) skillsFragment.getContext().getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(Constants.LONG_CLICK_VIBRATION);
        ConfirmationDialog.showConfirmDialog(skillsFragment.getContext(), "Skills?", skillsFragment);
        return true;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
