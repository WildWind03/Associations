package com.chirikhin.association.teamcreate;

import android.view.View;
import android.widget.EditText;

import com.chirikhin.association.BaseFragment;
import com.chirikhin.association.R;
import com.chirikhin.association.TeamNamesTypedEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class TeamCreateFragment extends BaseFragment {

    @BindView(R.id.editFirstTeamName)
    protected EditText editFirstTeamName;

    @BindView(R.id.editSecondTeamName)
    protected EditText editSecondTeamName;

    public static TeamCreateFragment newInstance() {
        return new TeamCreateFragment();
    }

    @Override
    protected int getLayout() {
        return R.layout.team_create_fragment;
    }

    @OnClick(R.id.applyTeamNamesButton)
    void onApplyTeamNamesButtonClicked(View v) {
        EventBus.getDefault().post(new TeamNamesTypedEvent(editFirstTeamName.getText().toString(), editSecondTeamName.getText().toString()));
    }
}
