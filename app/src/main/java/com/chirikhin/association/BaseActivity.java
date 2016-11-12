package com.chirikhin.association;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayout());
        ButterKnife.bind(this);
    }

    protected void showMessage(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog
                .Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok_message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

        alertDialogBuilder.show();
    }

    protected abstract int getLayout();
}


