package com.chirikhin.association;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.chirikhin.association.game.GameFragment;
import com.chirikhin.association.startgame.MainMenuFragment;
import com.chirikhin.association.teamcreate.TeamCreateFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends BaseActivity {

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (null == savedInstanceState) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.root_layout, MainMenuFragment.newInstance(), MainMenuFragment.class.getName())
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
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.root_layout, TeamCreateFragment.newInstance(), TeamCreateFragment.class.getName())
                .addToBackStack(null)
                .commit();
    }

    @Subscribe
    public void onAboutGameEvent(AboutGameEvent aboutGameEvent) {
        showMessage(getString(R.string.about_game_str), getString(R.string.about_game_content_str));
    }

    @Subscribe
    public void onTeamNamesTypedEvent(TeamNamesTypedEvent teamNamesTypedEvent) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.root_layout, GameFragment.newInstance(teamNamesTypedEvent.getFirstTeamName(), teamNamesTypedEvent.getSecondTeamName()))
                .addToBackStack(null)
                .commit();
    }


    @Override
    protected int getLayout() {
        return R.layout.main_activity;
    }
}
