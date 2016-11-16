package com.chirikhin.association;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.FragmentManager;
import com.chirikhin.association.aftergame.AfterGameFragment;
import com.chirikhin.association.beforeturn.BeforeTurnFragment;
import com.chirikhin.association.database.DatabaseController;
import com.chirikhin.association.game.GameFragment;
import com.chirikhin.association.startgame.MainMenuFragment;
import com.chirikhin.association.teamcreate.TeamCreateFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends BaseActivity {

    private static final String CURRENT_ROUND_TAG = "CURRENT_ROUND_TAG";
    private static final String FIRST_TEAM_NAME = "FIRST_TEAM_NAME";
    private static final String SECOND_TEAM_NAME = "SECOND_TEAM_NAME";

    private static final String FIRST_TEAM_SCORE_TAG = "FIRST_TEAM_SCORE_TAG";
    private static final String SECOND_TEAM_SCORE_TAG = "SECOND_TEAM_SCORE_TAG";

    private static final int ROUND_TIME = 10;
    private static final int COUNT_OF_ROUNDS = 4;

    private static final String APP_PREFERENCES = "MY_SETTINGS";
    private static final String IS_FIRST_START= "IS FIRST_START";

    private int currentRound;

    private String team1Name;
    private String team2Name;

    private int team1Score;
    private int team2Score;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        boolean isFirstStart = sharedPreferences.getBoolean(IS_FIRST_START, true);

        if (isFirstStart) {
            DatabaseController databaseController = new DatabaseController(this);

            databaseController.addNewWord("vague");
            databaseController.addNewWord("mash");
            databaseController.addNewWord("beam");
            databaseController.addNewWord("shaft");
            databaseController.addNewWord("impostor");
            databaseController.addNewWord("immense");
            databaseController.addNewWord("encompass");
            databaseController.addNewWord("instigate");
            databaseController.addNewWord("unduly");
            databaseController.addNewWord("theft");
            databaseController.addNewWord("subtle");

            sharedPreferences.edit()
                    .putBoolean(IS_FIRST_START, false)
                    .apply();

            databaseController.close();
        }

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (null == savedInstanceState) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.root_layout, MainMenuFragment.newInstance(), MainMenuFragment.class.getName())
                    .commit();

            currentRound = 0;

            team1Score = 0;
            team2Score = 0;
        } else {

            currentRound = savedInstanceState.getInt(CURRENT_ROUND_TAG);
            team1Name = savedInstanceState.getString(team1Name);
            team2Name = savedInstanceState.getString(team2Name);

            team1Score = savedInstanceState.getInt(FIRST_TEAM_SCORE_TAG);
            team2Score = savedInstanceState.getInt(SECOND_TEAM_SCORE_TAG);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(CURRENT_ROUND_TAG, currentRound);

        outState.putString(FIRST_TEAM_NAME, team1Name);
        outState.putString(SECOND_TEAM_NAME, team2Name);

        outState.putInt(FIRST_TEAM_SCORE_TAG, team1Score);
        outState.putInt(SECOND_TEAM_SCORE_TAG, team2Score);
    }

    @Subscribe
    public void onNewGameEvent(NewGameEvent newGameEvent) {
        TeamCreateFragment teamCreateFragment = (TeamCreateFragment) getSupportFragmentManager().findFragmentByTag(TeamCreateFragment.class.getName());
        if (null == teamCreateFragment) {
            teamCreateFragment = TeamCreateFragment.newInstance();
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.root_layout, teamCreateFragment, TeamCreateFragment.class.getName())
                .addToBackStack(null)
                .commit();
    }

    @Subscribe
    public void onAboutGameEvent(AboutGameEvent aboutGameEvent) {
        showMessage(getString(R.string.about_game_str), getString(R.string.about_game_content_str));
    }

    @Subscribe
    public void onTeamNamesTypedEvent(TeamNamesTypedEvent teamNamesTypedEvent) {
        BeforeTurnFragment gameControlFragment = (BeforeTurnFragment) getSupportFragmentManager()
                .findFragmentByTag(BeforeTurnFragment.class.getName());

        if (null != gameControlFragment) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(gameControlFragment)
                    .commitNow();
        }


        getSupportFragmentManager()
                .popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        this.team1Name = teamNamesTypedEvent.getFirstTeamName();
        this.team2Name = teamNamesTypedEvent.getSecondTeamName();

        BeforeTurnFragment newGameControlFragment = BeforeTurnFragment.newInstance(team1Name, team2Name);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.root_layout, newGameControlFragment, BeforeTurnFragment.class.getName())
                .addToBackStack(null)
                .commit();
    }

    @Subscribe
    public void onNewRound(NewRoundEvent newRoundEvent) {

        GameFragment gameFragment = ((GameFragment) getSupportFragmentManager()
                .findFragmentByTag(GameFragment.class.getName()));

        if (null == gameFragment) {
            gameFragment = GameFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.root_layout, gameFragment, GameFragment.class.getName())
                    .addToBackStack(null)
                    .commit();
        } else {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.root_layout, gameFragment, GameFragment.class.getName())
                    .commit();
        }

        getSupportFragmentManager().executePendingTransactions();


        ((GameFragment) getSupportFragmentManager()
                .findFragmentByTag(GameFragment.class.getName())).newRound(newRoundEvent.getTeamName(), 0, ROUND_TIME);
    }

    @Subscribe
    public void onRoundEnded(final RoundEndedEvent roundEndedEvent) {

        if (currentRound % 2 == 0) {
            team1Score+=roundEndedEvent.getFinalScore();
        } else {
            team2Score+=roundEndedEvent.getFinalScore();
        }

        currentRound++;

        if (currentRound < COUNT_OF_ROUNDS) {
            BeforeTurnFragment gameControlFragment = (BeforeTurnFragment) getSupportFragmentManager()
                    .findFragmentByTag(BeforeTurnFragment.class.getName());

            gameControlFragment.updateGameStatistics(roundEndedEvent.getFinalScore());

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.root_layout, gameControlFragment, BeforeTurnFragment.class.getName())
                    .commit();

            getSupportFragmentManager().executePendingTransactions();
        } else {

            AfterGameFragment afterGameFragment = AfterGameFragment.newInstance(team1Name, team2Name, team1Score, team2Score);

            getSupportFragmentManager()
                    .popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.root_layout, afterGameFragment, AfterGameFragment.class.getName())
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Subscribe
    public void onGameOverEvent(GameOverEvent gameOverMessage) {
        getSupportFragmentManager()
                .popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        currentRound = 0;
    }

    @Subscribe
    public void onGameCancelledEvent(GameCancelledEvent gameCancelledEvent) {
        getSupportFragmentManager()
                .popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        currentRound = 0;
    }


    @Override
    protected int getLayout() {
        return R.layout.main_activity;
    }
}
