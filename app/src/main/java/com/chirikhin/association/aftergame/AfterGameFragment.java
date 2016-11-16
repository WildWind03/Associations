package com.chirikhin.association.aftergame;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.chirikhin.association.BaseFragment;
import com.chirikhin.association.GameOverEvent;
import com.chirikhin.association.R;
import org.greenrobot.eventbus.EventBus;

public class AfterGameFragment extends BaseFragment {

    private static final String FIRST_TEAM_NAME_TAG = "FIRST_TEAM_NAME_TAG";
    private static final String SECOND_TEAM_NAME_TAG = "SECOND_TEAM_NAME_TAG";
    private static final String FIRST_TEAM_SCORE_TAG = "FIRST_TEAM_SCORE_TAG";
    private static final String SECOND_TEAM_SCORE_TAG = "SECOND_TEAM_SCORE_TAG";

    @BindView(R.id.firstTeamResultTitle)
    protected TextView firstTeamResultTitle;

    @BindView(R.id.secondTeamResultTitle)
    protected TextView secondTeamResultTitle;

    public static AfterGameFragment newInstance(String team1Name, String team2Name, int team1Score, int team2Score) {
        AfterGameFragment afterGameFragment = new AfterGameFragment();

        Bundle args = new Bundle();
        args.putString(FIRST_TEAM_NAME_TAG, team1Name);
        args.putString(SECOND_TEAM_NAME_TAG, team2Name);
        args.putInt(FIRST_TEAM_SCORE_TAG, team1Score);
        args.putInt(SECOND_TEAM_SCORE_TAG, team2Score);

        afterGameFragment.setArguments(args);

        return afterGameFragment;
    }


    @Override
    protected void onPostViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onPostViewCreated(view, savedInstanceState);

        if (null == savedInstanceState) {
            String team1Name = getArguments().getString(FIRST_TEAM_NAME_TAG);
            String team2Name = getArguments().getString(SECOND_TEAM_NAME_TAG);

            int team1Score = getArguments().getInt(FIRST_TEAM_SCORE_TAG);
            int team2Score = getArguments().getInt(SECOND_TEAM_SCORE_TAG);

            firstTeamResultTitle.setText(getString(R.string.filed_and_value_placeholder, team1Name, team1Score));
            secondTeamResultTitle.setText(getString(R.string.filed_and_value_placeholder, team2Name, team2Score));
        }
    }

    @OnClick(R.id.okButton)
    protected void onOKButtonClicked(View v) {
        EventBus.getDefault().post(new GameOverEvent());
    }

    @Override
    protected int getLayout() {
        return R.layout.after_game;
    }
}
