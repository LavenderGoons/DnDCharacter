package com.lavendergoons.dndcharacter.ui.adapters;

import android.content.DialogInterface;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.lavendergoons.dndcharacter.R;
import com.lavendergoons.dndcharacter.models.Abilities;
import com.lavendergoons.dndcharacter.models.Skill;
import com.lavendergoons.dndcharacter.utils.Constants;
import com.lavendergoons.dndcharacter.utils.Utils;

import java.util.ArrayList;


public class SkillAdapter2 extends RecyclerView.Adapter<SkillAdapter2.ViewHolder> {

    public static final String TAG = SkillAdapter2.class.getCanonicalName();

    private Fragment target;
    private ArrayList<Skill> mDataset;
    private Abilities abilities;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View cardView;
        public TextView skillNameText;
        public TextView skillModTypeText;
        public TextView skillTotalText;
        public TextView skillModText;
        public TextView skillRankText;
        public TextView skillMiscText;
        public ViewHolder(View view) {
            super(view);
            this.cardView = view;
            skillNameText = (TextView) view.findViewById(R.id.skillCheckBox);

            skillModTypeText = (TextView) view.findViewById(R.id.skillModTypeText);
            skillTotalText = (TextView) view.findViewById(R.id.skillTotalText);
            skillModText = (TextView) view.findViewById(R.id.skillMiscText);
            skillRankText = (TextView) view.findViewById(R.id.skillRankText);
            skillMiscText = (TextView) view.findViewById(R.id.skillModText);
            //nativePaint -1274180592
            //defaultColor -1275068417
        }
    }

    public interface SkillAdapter2Listener {}

    public SkillAdapter2(Fragment target, ArrayList<Skill> skills, Abilities abilities) {
        this.mDataset = skills;
        this.abilities = abilities;
        if (target instanceof SkillAdapter2Listener) {
            this.target = target;
        } else {
            throw new RuntimeException(target.toString()
                    + " must implement SkillAdapterListener");
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_skills_no_edit, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (mDataset.get(position).isTrained()) {
            holder.skillNameText.setTextColor(ContextCompat.getColor(target.getActivity(), R.color.colorAccent));
        } else {
            holder.skillNameText.setTextColor(ContextCompat.getColor(target.getActivity(), R.color.defaultColor));
        }
        holder.skillNameText.setText(mDataset.get(position).getName());

        int mod = modTypeToInt(mDataset.get(position).getModType());
        holder.skillModTypeText.setText(mDataset.get(position).getModType());
        holder.skillModText.setText(String.valueOf(abilities.getMod(mod)));

        holder.skillRankText.setText(String.valueOf(mDataset.get(position).getRank()));
        holder.skillMiscText.setText(String.valueOf(mDataset.get(position).getMisc()));
        holder.skillTotalText.setText(String.valueOf(mDataset.get(position).getTotal()));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSkillBottomEdit(v, position);
            }
        });
    }

    private void launchSkillBottomEdit(View v, final int pos) {
        BottomSheetDialog bottomSkillDialog = new BottomSheetDialog(target.getContext(), R.style.BottomDialog);

        View view = target.getActivity().getLayoutInflater().inflate(R.layout.dialog_bottom_skills, null);

        //TODO Add Butterknife?
        final CheckBox skillCheckBox = (CheckBox) view.findViewById(R.id.skillCheckBox);
        final EditText skillModEdit = (EditText) view.findViewById(R.id.skillModEdit);
        final EditText skillRankEdit = (EditText) view.findViewById(R.id.skillRankEdit);
        final EditText skillMiscEdit = (EditText) view.findViewById(R.id.skillMiscEdit);
        final EditText skillTotalEdit = (EditText) view.findViewById(R.id.skillTotalEdit);

        // Text Change listeners
        skillRankEdit.addTextChangedListener(new SkillTextWatcher(skillMiscEdit, skillTotalEdit));
        skillMiscEdit.addTextChangedListener(new SkillTextWatcher(skillRankEdit, skillTotalEdit));

        skillCheckBox.setText(mDataset.get(pos).getName());
        skillCheckBox.setChecked(mDataset.get(pos).isTrained());

        skillModEdit.setText(String.valueOf(mDataset.get(pos).getMod()));
        skillRankEdit.setText(String.valueOf(mDataset.get(pos).getRank()));
        skillMiscEdit.setText(String.valueOf(mDataset.get(pos).getMisc()));
        skillTotalEdit.setText(String.valueOf(mDataset.get(pos).getTotal()));

        bottomSkillDialog.setContentView(view);
        bottomSkillDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mDataset.get(pos).setTrained(skillCheckBox.isChecked());
                mDataset.get(pos).setRank(Utils.checkIntNotNull(skillRankEdit));
                mDataset.get(pos).setMisc(Utils.checkIntNotNull(skillRankEdit));
                mDataset.get(pos).setTotal(Utils.checkIntNotNull(skillTotalEdit));
                SkillAdapter2.this.notifyDataSetChanged();
            }
        });
        bottomSkillDialog.show();
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public ArrayList<Skill> getSkillList() {
        return mDataset;
    }

    public void setData(ArrayList<Skill> skills) {
        if (skills.isEmpty()) {
            for (Constants.Skills s : Constants.Skills.values()) {
                skills.add(new Skill(s.getName(), s.getMod(), s.getDefault()));
            }
        }
        mDataset = skills;
        notifyDataSetChanged();
    }

    // TODO Clean up you bum
    public int modTypeToInt(String type) {
        int mod = 0;
        switch (type) {
            case "STR":
                mod = Abilities.STR;
                break;
            case "DEX":
                mod = Abilities.DEX;
                break;
            case "CON":
                mod = Abilities.CON;
                break;
            case "INT":
                mod = Abilities.INT;
                break;
            case "WIS":
                mod = Abilities.WIS;
                break;
            case "CHA":
                mod = Abilities.CHA;
                break;
        }
        return mod;
    }

    private class SkillTextWatcher implements TextWatcher {
        // The other EditText in the Dialog
        EditText other;
        // Total EditText in the Dialog
        EditText total;

        public SkillTextWatcher(EditText other, EditText total) {
            this.other = other;
            this.total = total;
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        public void afterTextChanged(Editable s) {}

        @Override
        public void onTextChanged(CharSequence sequence, int start, int before, int count) {
            int value = Utils.stringToInt(sequence.toString());
            int otherVal = Utils.checkIntNotNull(this.other);
            total.setText(String.valueOf((value+otherVal)));
        }
    }
    }
