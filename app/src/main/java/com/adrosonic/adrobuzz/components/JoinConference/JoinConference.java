package com.adrosonic.adrobuzz.components.JoinConference;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.adrosonic.adrobuzz.R;
import com.adrosonic.adrobuzz.databinding.ActivityJoinConferenceBinding;

import br.com.ilhasoft.support.validation.Validator;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class JoinConference extends AppCompatActivity implements Validator.ValidationListener {

    @BindView(R.id.join_conference)
    Button joinConference;

    Unbinder unbinder;
    private ActivityJoinConferenceBinding mBinding;
    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_join_conference);

        final View view = mBinding.getRoot();
        unbinder =  ButterKnife.bind(this, view);
        validator = new Validator(mBinding);
        validator.setValidationListener(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

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
        Toast.makeText(this,"Validations successful",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onValidationError() {

    }
}
