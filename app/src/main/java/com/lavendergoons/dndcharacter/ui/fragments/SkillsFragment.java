package com.lavendergoons.dndcharacter.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lavendergoons.dndcharacter.DndApplication;
import com.lavendergoons.dndcharacter.data.DatabaseHelper;
import com.lavendergoons.dndcharacter.models.Abilities;
import com.lavendergoons.dndcharacter.models.SimpleCharacter;
import com.lavendergoons.dndcharacter.models.Skill;
import com.lavendergoons.dndcharacter.R;
import com.lavendergoons.dndcharacter.ui.adapters.SkillAdapter2;
import com.lavendergoons.dndcharacter.utils.CharacterManager2;
import com.lavendergoons.dndcharacter.utils.Constants;
import com.lavendergoons.dndcharacter.ui.adapters.SkillsAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class SkillsFragment extends BaseFragment implements SkillsAdapter.SkillAdapterListener, SkillAdapter2.SkillAdapter2Listener {

    public static final String TAG = SkillsFragment.class.getCanonicalName();

    private RecyclerView mSkillsRecyclerView;
    private SkillAdapter2 mSkillRecyclerAdapter;
    private RecyclerView.LayoutManager mSkillRecyclerLayoutManager;
    private ProgressBar progressBar;
    private ArrayList<Skill> skillsList = new ArrayList<>();
    Gson gson = new Gson();

    private CompositeDisposable disposable = new CompositeDisposable();

    @Inject CharacterManager2 characterManager;

    private long characterId = -1;
    private SimpleCharacter simpleCharacter;
    private Abilities abilities;

    public SkillsFragment() {
        // Required empty public constructor
    }

    public static SkillsFragment newInstance(SimpleCharacter charIn, long characterId) {
        SkillsFragment frag = new SkillsFragment();
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
        abilities = characterManager.getAbilities(characterId);
    }

    @Override
    public void onStart() {
        super.onStart();
        Observable<String> skillsStringObservable = characterManager.getObservableJson(characterId, DatabaseHelper.COLUMN_SKILL);
        disposable.add(skillsStringObservable.observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String skills) throws Exception {
                        progressBar.setVisibility(View.VISIBLE);
                        Log.d(TAG, "doOnNext: Thread "+Thread.currentThread().getName());
                    }
                })
                .observeOn(Schedulers.computation())
                .map(new Function<String, ArrayList<Skill>>() {
                    @Override
                    public ArrayList<Skill> apply(String json) {
                        Log.d(TAG, "map: Thread "+Thread.currentThread().getName());
                        Type type = new TypeToken<ArrayList<Skill>>(){}.getType();
                        return gson.fromJson(json, type);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArrayList<Skill>>() {
                    // TODO look into subscriber
                    @Override
                    public void accept(@NonNull ArrayList<Skill> skills) throws Exception {
                        Log.d(TAG, "accept: Thread "+Thread.currentThread().getName());
                        progressBar.setVisibility(View.GONE);
                        skillsList = skills;
                        mSkillRecyclerAdapter.setData(skills);
                    }
                }));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_skills, container, false);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        mSkillsRecyclerView = (RecyclerView) rootView.findViewById(R.id.skillsRecyclerView);

        // Keeps View same size on content change
        mSkillsRecyclerView.setHasFixedSize(true);

        mSkillRecyclerLayoutManager = new LinearLayoutManager(this.getContext());
        mSkillsRecyclerView.setLayoutManager(mSkillRecyclerLayoutManager);

        mSkillRecyclerAdapter = new SkillAdapter2(this, skillsList, abilities);
        mSkillsRecyclerView.setAdapter(mSkillRecyclerAdapter);

        return rootView;
    }

    @Override
    public int getTitle() {
        return R.string.title_fragment_skills;
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }

    @Override
    public void onPause() {
        super.onPause();
        writeSkills();
    }

    @Override
    public void onStop() {
        super.onStop();
        disposable.clear();
    }

    private void writeSkills() {
        skillsList = mSkillRecyclerAdapter.getSkillList();
        characterManager.setSkills(characterId, skillsList);
    }
}
