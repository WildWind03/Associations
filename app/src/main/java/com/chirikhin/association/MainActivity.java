package com.chirikhin.association;

import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.FragmentManager;
import com.chirikhin.association.gamecontrol.GameControlFragment;
import com.chirikhin.association.game.GameFragment;
import com.chirikhin.association.startgame.MainMenuFragment;
import com.chirikhin.association.teamcreate.TeamCreateFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends BaseActivity {

    private static final int ROUND_TIME = 10;
    private static final int COUNT_OF_ROUNDS = 6;

    private static int currentRound;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (null == savedInstanceState) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.root_layout, MainMenuFragment.newInstance(), MainMenuFragment.class.getName())
                    .commit();
        } else {
            MainMenuFragment mainMenuFragment = (MainMenuFragment) getSupportFragmentManager()
                    .findFragmentByTag(MainMenuFragment.class.getName());

            if (null == mainMenuFragment) {
                mainMenuFragment = MainMenuFragment.newInstance();
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.root_layout,mainMenuFragment, MainMenuFragment.class.getName())
                    .commit();

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
        GameControlFragment mainMenuFragment = (GameControlFragment) getSupportFragmentManager()
                .findFragmentByTag(GameControlFragment.class.getName());

        if (null != mainMenuFragment) {
            getSupportFragmentManager().beginTransaction().remove(mainMenuFragment).commit();
            getSupportFragmentManager().executePendingTransactions();
        }


        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        mainMenuFragment = GameControlFragment.newInstance(teamNamesTypedEvent.getFirstTeamName(), teamNamesTypedEvent.getSecondTeamName());

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.root_layout, mainMenuFragment, GameControlFragment.class.getName())
                .addToBackStack(null)
                .commit();
    }

    @Subscribe
    public void onNewRound(NewRoundEvent newRoundEvent) {
        GameFragment gameFragment = ((GameFragment) getSupportFragmentManager()
                .findFragmentByTag(GameFragment.class.getName()));

        if (null == gameFragment) {
            gameFragment = GameFragment.newInstance();
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.root_layout, gameFragment, GameFragment.class.getName())
                .commit();

        getSupportFragmentManager().executePendingTransactions();


        ((GameFragment) getSupportFragmentManager()
                .findFragmentByTag(GameFragment.class.getName())).newRound(newRoundEvent.getTeamName(), 0, ROUND_TIME);
    }

    @Subscribe
    public void onRoundEnded(final RoundEndedEvent roundEndedEvent) {
        currentRound++;

        if (currentRound < COUNT_OF_ROUNDS) {
            GameControlFragment gameControlFragment = (GameControlFragment) getSupportFragmentManager()
                    .findFragmentByTag(GameControlFragment.class.getName());

            gameControlFragment.updateGameStatistics(roundEndedEvent.getFinalScore());

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.root_layout, gameControlFragment, GameControlFragment.class.getName())
                    .commit();

            getSupportFragmentManager().executePendingTransactions();
        } else {

        }
    }


    @Override
    protected int getLayout() {
        return R.layout.main_activity;
    }
}
