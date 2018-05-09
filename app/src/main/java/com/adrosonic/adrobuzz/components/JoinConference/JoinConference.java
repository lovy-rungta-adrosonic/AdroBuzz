package com.adrosonic.adrobuzz.components.JoinConference;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.adrosonic.adrobuzz.R;
import com.adrosonic.adrobuzz.components.CreateConference.CreateConferencePresenter;
import com.adrosonic.adrobuzz.components.main.App;
import com.adrosonic.adrobuzz.contract.JoinConferenceContract;
import com.adrosonic.adrobuzz.databinding.ActivityJoinConferenceBinding;
import com.adrosonic.adrobuzz.model.JoinConfRequest;
import com.adrosonic.adrobuzz.sync.api.Service;

import javax.inject.Inject;

import br.com.ilhasoft.support.validation.Validator;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Retrofit;

public class JoinConference extends AppCompatActivity implements Validator.ValidationListener, JoinConferenceContract.View{

    JoinConfRequest request;

    @Inject
    Retrofit retrofit;

    @BindView(R.id.join_conference)
    Button joinConference;

    Unbinder unbinder;
    private ActivityJoinConferenceBinding mBinding;
    private Validator validator;

    private JoinConferenceContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_join_conference);

        final View view = mBinding.getRoot();
        unbinder =  ButterKnife.bind(this, view);
        validator = new Validator(mBinding);
        validator.setValidationListener(this);

        ((App) getApplication()).getAppComponent().inject(this);

        Service service = retrofit.create(Service.class);
        mPresenter = new JoinConferencePresenter(this,this,service);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mBinding.setJoinConf(false);

    }

    @OnClick(R.id.join_conference)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.join_conference:
                validator.toValidate();
                break;

            default:
                break;

        }
    }

    @Override
    public void onValidationSuccess() {
        mBinding.setJoinConf(true);
        mPresenter.joinConference();
    }

    @Override
    public void onValidationError() {

    }

    @Override
    public void setLoadingIndicator(boolean active) {
        mBinding.setJoinConf(active);
    }

    @Override
    public JoinConfRequest getParameters() {
        return null;
    }
}
