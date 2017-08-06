package com.lavendergoons.dndcharacter.ui.adapters;

import android.content.Context;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lavendergoons.dndcharacter.ui.dialogs.AttackDialog;
import com.lavendergoons.dndcharacter.models.Attack;
import com.lavendergoons.dndcharacter.R;
import com.lavendergoons.dndcharacter.utils.Constants;

import java.util.ArrayList;

/**
 * Adapter for Attack RecyclerView
 */

public class AttackAdapter extends RecyclerView.Adapter<AttackAdapter.ViewHolder> {

    private Fragment fragment;
    private AttackAdapterListener listener;
    private ArrayList<Attack> mDataset;


    public AttackAdapter(Fragment fragment, ArrayList<Attack> dataset) {
        this.mDataset = dataset;
        this.fragment = fragment;
        if (fragment instanceof AttackAdapterListener) {
            this.listener = (AttackAdapterListener) fragment;
        } else {
            throw new RuntimeException(fragment.toString()
                    + " must implement AttackAdapterListener");
        }
    }

    public interface AttackAdapterListener {
        void removeAttack(Attack attack);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View cardview;
        public TextView cardAttackNameValue;
        public TextView cardAttackBonusValue;
        public TextView cardAttackDamageValue;
        public TextView cardAttackCriticalValue;
        public TextView cardAttackRangeValue;
        public TextView cardAttackTypeValue;
        public TextView cardAttackAmmoValue;
        public ViewHolder(View view) {
            super(view);
            this.cardview = view;
            cardAttackNameValue = (TextView) view.findViewById(R.id.cardAttackNameValue);
            cardAttackBonusValue = (TextView) view.findViewById(R.id.cardAttackBonusValue);
            cardAttackDamageValue = (TextView) view.findViewById(R.id.cardAttackDamageValue);
            cardAttackCriticalValue = (TextView) view.findViewById(R.id.cardAttackCriticalValue);
            cardAttackRangeValue = (TextView) view.findViewById(R.id.cardAttackRangeValue);
            cardAttackTypeValue = (TextView) view.findViewById(R.id.cardAttackTypeValue);
            cardAttackAmmoValue = (TextView) view.findViewById(R.id.cardAttackAmmoValue);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_attack, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.cardAttackNameValue.setText(mDataset.get(position).getAttack());
        holder.cardAttackBonusValue.setText(mDataset.get(position).getAttackBonus());
        holder.cardAttackDamageValue.setText(mDataset.get(position).getDamage());
        holder.cardAttackCriticalValue.setText(mDataset.get(position).getCritical());
        holder.cardAttackRangeValue.setText(String.valueOf(mDataset.get(position).getRange()));
        holder.cardAttackTypeValue.setText(mDataset.get(position).getType());
        holder.cardAttackAmmoValue.setText(String.valueOf(mDataset.get(position).getAmmo()));

        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCardClick(mDataset.get(position));
            }
        });
        holder.cardview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return onCardLongClick(mDataset.get(position));
            }
        });
    }

    private void onCardClick(Attack attack) {
        //AttackDialog.showAttackDialog(fragment.getActivity(), (AttackDialog.AttackDialogListener) fragment, attack);
        new AttackDialog(fragment, attack).showDialog();
    }

    private boolean onCardLongClick(Attack attack) {
        Vibrator v = (Vibrator) fragment.getContext().getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(Constants.LONG_CLICK_VIBRATION);
        listener.removeAttack(attack);
        return true;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
