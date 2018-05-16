package com.adrosonic.adrobuzz.components.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.adrosonic.adrobuzz.R;
import com.adrosonic.adrobuzz.components.createConference.CreateConference;
import com.adrosonic.adrobuzz.components.joinConference.JoinConference;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    private Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
    }

    @OnClick({R.id.create_conference,R.id.join_conference})
    public void onClick(View v) {
       switch(v.getId()){
           case R.id.create_conference:
               Intent create = new Intent(getBaseContext(), CreateConference.class);
               startActivity(create);
               break;

           case R.id.join_conference:
               Intent join = new Intent(getBaseContext(), JoinConference.class);
               startActivity(join);
               break;
       }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}

