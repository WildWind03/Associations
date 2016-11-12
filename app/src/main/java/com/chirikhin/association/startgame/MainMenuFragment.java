package com.chirikhin.association.startgame;

import android.util.Log;
import android.view.View;

import com.chirikhin.association.AboutGameEvent;
import com.chirikhin.association.BaseFragment;
import com.chirikhin.association.NewGameEvent;
import com.chirikhin.association.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.OnClick;

public class MainMenuFragment extends BaseFragment {

    public static MainMenuFragment newInstance() {
        return new MainMenuFragment();
    }

    @OnClick(R.id.startGameButton)
    void onNewGameButtonClick(View view) {
        EventBus.getDefault().post(new NewGameEvent());
        Log.d(getClass().getName(), "OnNewGameButtonClick");
    }

    @OnClick(R.id.aboutTheGameButton)
    void onAboutGameButtonClick(View view) {
        EventBus.getDefault().post(new AboutGameEvent());
        Log.d(getClass().getName(), "OnAboutGameButtonClick");
    }

    @Override
    protected int getLayout() {
        return R.layout.main_menu_fragment;
    }
}
