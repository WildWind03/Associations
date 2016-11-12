package com.chirikhin.association.game;

import android.os.Bundle;

import com.chirikhin.association.BaseFragment;
import com.chirikhin.association.R;

public class GameFragment extends BaseFragment {

    private static final String FIRST_TEAM_NAME_TAG = "FIRST_TEAM";
    private static final String SECOND_TEAM_NAME_TAG = "SECOND_TEAM";

    public static GameFragment newInstance(String firstTeamName, String secondTeamName) {
        GameFragment gameFragment = new GameFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FIRST_TEAM_NAME_TAG, firstTeamName);
        bundle.putString(SECOND_TEAM_NAME_TAG, secondTeamName);
        gameFragment.setArguments(bundle);

        return gameFragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.game_layout;
    }
}
