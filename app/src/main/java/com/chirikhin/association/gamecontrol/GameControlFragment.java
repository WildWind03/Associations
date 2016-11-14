package com.chirikhin.association.gamecontrol;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.chirikhin.association.BaseFragment;
import com.chirikhin.association.NewRoundEvent;
import com.chirikhin.association.R;
import org.greenrobot.eventbus.EventBus;

public class GameControlFragment extends BaseFragment {
    private static final String FIRST_TEAM_NAME_TAG = "FIRST_TEAM";
    private static final String SECOND_TEAM_NAME_TAG = "SECOND_TEAM";

    private static final String FIRST_TEAM_NAME_SAVED = "FIRST_TEAM_NAME_SAVED";
    private static final String SECOND_TEAM_NAME_SAVED = "SECOND_TEAM_NAME_SAVED";
    private static final String CURRENT_ROUND = "CURRENT_ROUND_SAVED";
    private static final String TEAM1_SCORE_SAVED = "TEAM1_SAVED";
    private static final String TEAM2_SCORE_SAVED = "TEAM2_SAVED";

    private int team1Score;
    private int team2Score;
    private int currentRoundNum;

    private String teamName1;
    private String teamName2;

    @BindView(R.id.roundNumTextView)
    protected TextView roundNumTextView;

    @BindView(R.id.firstTeamScoreTextView)
    protected TextView firstTeamScoreTextView;

    @BindView(R.id.secondTeamScoreTextView)
    protected TextView secondTeamScoreTextView;

    public static GameControlFragment newInstance(String firstTeamName, String secondTeamName) {
        GameControlFragment gameControlFragment = new GameControlFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FIRST_TEAM_NAME_TAG, firstTeamName);
        bundle.putString(SECOND_TEAM_NAME_TAG, secondTeamName);
        gameControlFragment.setArguments(bundle);

        return gameControlFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null == savedInstanceState) {
            Bundle args = getArguments();
            teamName1 = args.getString(FIRST_TEAM_NAME_TAG);
            teamName2 = args.getString(SECOND_TEAM_NAME_TAG);
            currentRoundNum = 0;
            team1Score = 0;
            team2Score = 0;
        } else {
            teamName1 = savedInstanceState.getString(FIRST_TEAM_NAME_SAVED);
            teamName2 = savedInstanceState.getString(SECOND_TEAM_NAME_SAVED);
            currentRoundNum = savedInstanceState.getInt(CURRENT_ROUND);
            team1Score = savedInstanceState.getInt(TEAM1_SCORE_SAVED);
            team2Score = savedInstanceState.getInt(TEAM2_SCORE_SAVED);

        }
    }

    @Override
    protected void onPostViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onPostViewCreated(view, savedInstanceState);

        roundNumTextView.setText("Round: " + currentRoundNum);
        firstTeamScoreTextView.setText(teamName1 + ": " + team1Score);
        secondTeamScoreTextView.setText(teamName2 + ": " + team2Score);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(FIRST_TEAM_NAME_SAVED, teamName1);
        outState.putString(SECOND_TEAM_NAME_SAVED, teamName2);
        outState.putInt(CURRENT_ROUND, currentRoundNum);
        outState.putInt(TEAM1_SCORE_SAVED, team1Score);
        outState.putInt(TEAM2_SCORE_SAVED, team2Score);
    }

    @OnClick(R.id.nextTurnButton)
    public void onNextTurnButtonClick(View view) {
        if (currentRoundNum % 2 == 0) {
            EventBus.getDefault().post(new NewRoundEvent(teamName1));
        } else {
            EventBus.getDefault().post(new NewRoundEvent(teamName2));
        }
    }

    public void updateGameStatistics(int resultOfPrevGame) {
        if (currentRoundNum % 2 == 0) {
            team1Score+=resultOfPrevGame;
        } else {
            team2Score+=resultOfPrevGame;
        }

        currentRoundNum++;
    }

    @Override
    protected int getLayout() {
        return R.layout.after_turn;
    }
}
