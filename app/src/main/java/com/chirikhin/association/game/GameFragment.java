package com.chirikhin.association.game;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import com.chirikhin.association.BaseFragment;
import com.chirikhin.association.GameCancelledEvent;
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

    private AsyncTask<Void, Integer, Void> asyncTask;

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
        } else {
            if (null != getView()) {
                getView().setFocusableInTouchMode(true);
                getView().requestFocus();
                getView().setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            if (null != asyncTask) {
                                asyncTask.cancel(true);
                                asyncTask = null;
                                EventBus.getDefault().post(new GameCancelledEvent());
                            }
                            return true;
                        }
                        return false;
                    }
                });
            }
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

        asyncTask = new AsyncTask<Void, Integer, Void>() {
            private int counter = initialTime;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                teamTitle.setText(getString(R.string.filed_and_value_placeholder, getString(R.string.team_str), teamName));
                scoreTitle.setText(getString(R.string.filed_and_value_placeholder, getString(R.string.score_str), initialScore));
                timeView.setText(String.valueOf(initialTime));
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    while (counter > 0 && !isCancelled()) {
                        Thread.sleep(1000);
                        publishProgress(--counter);
                    }
                } catch (InterruptedException e) {
                    Log.d(getClass().getName(), e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                asyncTask = null;
                EventBus.getDefault().post(new RoundEndedEvent(score));
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                timeView.setText(String.valueOf(counter));
            }
        }
                .execute();

    }
}
