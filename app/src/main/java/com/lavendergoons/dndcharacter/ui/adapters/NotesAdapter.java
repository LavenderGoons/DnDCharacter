package com.lavendergoons.dndcharacter.ui.adapters;

import android.content.Context;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lavendergoons.dndcharacter.ui.fragments.NoteFragment;
import com.lavendergoons.dndcharacter.models.Note;
import com.lavendergoons.dndcharacter.R;
import com.lavendergoons.dndcharacter.utils.Constants;

import java.util.ArrayList;


public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    //private NotesListFragment fragment;
    private Fragment fragment;
    private NotesAdapterListener listener;
    private ArrayList<Note> mDataset;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View cardView;
        public TextView notesTitleView;

        public ViewHolder(View view) {
            super(view);
            this.cardView = view;
            notesTitleView = (TextView) view.findViewById(R.id.notesTitleView);
        }
    }

    public NotesAdapter(Fragment fragment, ArrayList<Note> notes) {
        this.fragment = fragment;
        this.mDataset = notes;
        if (fragment instanceof NotesAdapterListener) {
            this.listener = (NotesAdapterListener) fragment;
        } else {
            throw new RuntimeException(fragment.toString()
                + " must implement NotesAdapterListener.");
        }
    }

    public interface NotesAdapterListener {
        void remoteNote(Note note);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_notes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.notesTitleView.setText(mDataset.get(position).getTitle());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCardClick(position);
            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return onCardLongClick(position);
            }
        });
    }

    private void onCardClick(int position) {
        launchNoteFragment(mDataset.get(position), position);
    }

    private boolean onCardLongClick(int position) {
        Vibrator v = (Vibrator) fragment.getContext().getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(Constants.LONG_CLICK_VIBRATION);
        listener.remoteNote(mDataset.get(position));
        return true;
    }

    private void launchNoteFragment(Note note, int i) {
        FragmentTransaction fragTransaction = fragment.getActivity().getSupportFragmentManager().beginTransaction();
        fragTransaction.replace(R.id.content_character_nav, NoteFragment.newInstance(note, i), NoteFragment.TAG).addToBackStack(NoteFragment.TAG).commit();
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
