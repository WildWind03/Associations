package com.chirikhin.association.game;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import com.chirikhin.association.BaseFragment;
import com.chirikhin.association.R;
import com.chirikhin.association.RoundEndedEvent;
import org.greenrobot.eventbus.EventBus;

public class GameFragment extends BaseFragment {
    private static final String TEAM_NAME_SAVED = "TEAM_NAME_SAVED";
    private static final String INITIAL_TIME_SAVED = "INIT_TIME_SAVED";
    private static final String CURRENT_SCORE_SAVED = "CURRENT_TIME_SAVED";

    private String teamName;
    private int score;
    private int initialTime;

    @BindView(R.id.timeTitle)
    protected TextView timeView;

    @BindView(R.id.scoreTitle)
    protected TextView scoreTitle;

    @BindView(R.id.teamTitle)
    protected TextView teamTitle;

    public static GameFragment newInstance() {
        return new GameFragment();
    }

    @Override
    protected void onPostViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onPostViewCreated(view, savedInstanceState);

        if (null != savedInstanceState) {
            teamName = savedInstanceState.getString(TEAM_NAME_SAVED);
            initialTime = savedInstanceState.getInt(INITIAL_TIME_SAVED);
            score = savedInstanceState.getInt(CURRENT_SCORE_SAVED);

            newRound(teamName, score, initialTime);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_SCORE_SAVED, score);
        outState.putInt(INITIAL_TIME_SAVED, initialTime);
        outState.putString(TEAM_NAME_SAVED, teamName);
    }


    @Override
    protected int getLayout() {
        return R.layout.game_layout;
    }

    public void newRound(final String teamName, final int initialScore, final int initialTime) {

        new AsyncTask<Void, Integer, Void>() {
            private int counter = initialTime;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                teamTitle.setText("Team: " + teamName);
                scoreTitle.setText("Score: " + initialScore);
                timeView.setText(initialTime + "");
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    while (counter > 0) {
                        Thread.sleep(1000);
                        counter--;
                        publishProgress(counter);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                EventBus.getDefault().post(new RoundEndedEvent(score));
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                timeView.setText(counter + "");
            }
        }.execute();

    }
}
