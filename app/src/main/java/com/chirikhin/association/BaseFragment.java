package com.chirikhin.association;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayout(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        onPostViewCreated(view, savedInstanceState);
    }

    protected void onPostViewCreated(View view, @Nullable Bundle savedInstanceState) {

    }

    protected void showMessage(String title, String message) {
        showMessage(title, message, null);
    }

    protected void showMessage(String title, String message, final Runnable runnable) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog
                .Builder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok_message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (null != runnable) {
                            runnable.run();
                        }
                    }
                });

        alertDialogBuilder.show();
    }

    protected abstract int getLayout();
}
