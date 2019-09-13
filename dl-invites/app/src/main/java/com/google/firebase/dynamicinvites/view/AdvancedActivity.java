package com.google.firebase.dynamicinvites.view;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.dynamicinvites.R;
import com.google.firebase.dynamicinvites.presenter.InvitePresenter;

public class AdvancedActivity extends AppCompatActivity implements ShareDialogFragment.Listener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced);

        findViewById(R.id.buttonShare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareDialogFragment.newInstance().show(getSupportFragmentManager(), "dialog");
            }
        });
    }

    @Override
    public void onItemClicked(InvitePresenter presenter) {
        presenter.sendInvite(this);
    }
}
