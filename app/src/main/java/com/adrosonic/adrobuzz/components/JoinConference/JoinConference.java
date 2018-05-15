package com.adrosonic.adrobuzz.components.JoinConference;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.adrosonic.adrobuzz.R;
import com.adrosonic.adrobuzz.Utils.Utility;
import com.adrosonic.adrobuzz.components.Speech2Text.SpeechToTextActivity;
import com.adrosonic.adrobuzz.components.main.App;
import com.adrosonic.adrobuzz.contract.JoinConferenceContract;
import com.adrosonic.adrobuzz.databinding.ActivityJoinConferenceBinding;
import com.adrosonic.adrobuzz.model.JoinConf.JoinConf;
import com.adrosonic.adrobuzz.model.JoinConf.JoinConfRequest;
import com.adrosonic.adrobuzz.sync.api.Service;

import javax.inject.Inject;

import br.com.ilhasoft.support.validation.Validator;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Retrofit;

public class JoinConference extends AppCompatActivity implements Validator.ValidationListener, JoinConferenceContract.View {

    JoinConfRequest request;

    @Inject
    Retrofit retrofit;

    @BindView(R.id.conf_id)
    EditText confId;

    @BindView(R.id.joinee_username)
    EditText username;

    @BindView(R.id.joinee_email)
    EditText email;

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
        unbinder = ButterKnife.bind(this, view);
        validator = new Validator(mBinding);
        validator.setValidationListener(this);

        ((App) getApplication()).getAppComponent().inject(this);

        Service service = retrofit.create(Service.class);
        mPresenter = new JoinConferencePresenter(this, this, service);

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
        if(Utility.checkIfInternetConnected(this)){
            mBinding.setJoinConf(true);
            mPresenter.joinConference();
        }else{
            Utility.noInternetConnection(this);
        }
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

        EditText username = findViewById(R.id.joinee_username);
        EditText email = findViewById(R.id.joinee_email);

        request = new JoinConfRequest.Builder()
                .withName(username.getText().toString())
                .withEmail(email.getText().toString())
                .build();

        return request;
    }

    @Override
    public String getConfId() {
        EditText confId = findViewById(R.id.conf_id);
        return "AB-"+confId.getText().toString();
    }

    @Override
    public void showLoadingError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void joinedConference(JoinConf joinConf) {
        Intent speechToText = new Intent(getBaseContext(), SpeechToTextActivity.class);
        speechToText.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(speechToText);
    }

}
