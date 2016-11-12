package com.chirikhin.association;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.chirikhin.association.startgame.MainMenuFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends BaseActivity {

    private GameState gameState = GameState.MAIN_MENU;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (null == savedInstanceState) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.root_layout, MainMenuFragment.newInstance(), MainMenuFragment.class.getName())
                    .commit();
        }

        switch (gameState) {
            case MAIN_MENU:
                break;
            case TEAM_WINDOW:
                break;
            case GAME_WINDOW:
                break;
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

    }

    @Subscribe
    public void onAboutGameEvent(AboutGameEvent aboutGameEvent) {
        showMessage(getString(R.string.about_game_str), getString(R.string.about_game_content_str));
    }


    @Override
    protected int getLayout() {
        return R.layout.main_activity;
    }
}
