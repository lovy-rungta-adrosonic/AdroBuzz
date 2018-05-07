package com.adrosonic.adrobuzz.components.CreateConference;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.adrosonic.adrobuzz.R;
import com.adrosonic.adrobuzz.components.main.App;
import com.adrosonic.adrobuzz.contract.CreateConferenceContract;
import com.adrosonic.adrobuzz.contract.StartConferenceContract;
import com.adrosonic.adrobuzz.databinding.ActivityStartConferenceBinding;
import com.adrosonic.adrobuzz.model.CreateConfRequest;
import com.adrosonic.adrobuzz.model.User;
import com.adrosonic.adrobuzz.sync.api.Service;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Retrofit;

public class StartConferenceActivity extends AppCompatActivity implements StartConferenceContract.View{

    Unbinder unbinder;
    private ActivityStartConferenceBinding mBinding;

    @Inject
    Retrofit retrofit;

    private StartConferenceContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_start_conference);
        final View view = mBinding.getRoot();

        ((App) getApplication()).getAppComponent().inject(this);
        Service service = retrofit.create(Service.class);
        mPresenter = new StartConferencePresenter(this,this,service);
        setPresenter(mPresenter);

        mBinding.setPresenter((StartConferencePresenter)mPresenter);
        unbinder =  ButterKnife.bind(this, view);
    }

    @OnClick({R.id.add_invites})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_invites:
                Intent start = new Intent(getBaseContext(), AddInvitesActivity.class);
                startActivity(start);
                break;

            default:
                break;

        }
    }

    @Override
    public void setPresenter(StartConferenceContract.Presenter presenter) {

    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showLoadingError(String message) {

    }
}
