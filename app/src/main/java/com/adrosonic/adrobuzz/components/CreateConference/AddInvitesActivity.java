package com.adrosonic.adrobuzz.components.CreateConference;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adrosonic.adrobuzz.R;
import com.adrosonic.adrobuzz.Utils.PreferenceManager;
import com.adrosonic.adrobuzz.components.main.App;
import com.adrosonic.adrobuzz.contract.AddInvitesContract;
import com.adrosonic.adrobuzz.databinding.ActivityAddInvitesBinding;
import com.adrosonic.adrobuzz.sync.api.Service;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Retrofit;

public class AddInvitesActivity extends AppCompatActivity implements AddInvitesContract.View {

    public static final String TAG = AddInvitesActivity.class.getSimpleName();

    @Inject
    Retrofit retrofit;

    @BindView(R.id.linear_layout_container)
    LinearLayout container;

    private ActivityAddInvitesBinding mBinding;
    Unbinder unbinder;

    int counter;
    private AddInvitesContract.Presenter mPresenter;
    private ArrayList<String> listOfEmails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFinishOnTouchOutside(false);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_invites);
        final View view = mBinding.getRoot();
        unbinder = ButterKnife.bind(this, view);

        mBinding.setStatus(false);

        ((App) getApplication()).getAppComponent().inject(this);

        Service service = retrofit.create(Service.class);
        mPresenter = new AddInvitesPresenter(this, this, service);

        counter = 0;
    }

    @OnClick({R.id.add_invites, R.id.send_invites})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_invites:

                boolean validationSuccess;

                TextInputLayout emailLayout = findViewById(R.id.layout_email);
                EditText email = findViewById(R.id.email);
                String emailText = email.getText().toString();
                if (emailValidator(emailText)) {
                    if (isEmailAlreadyAdded(emailText)) {
                        emailLayout.setErrorEnabled(true);
                        emailLayout.setError(getResources().getString(R.string.email_already_added));
                        validationSuccess = false;
                    } else {
                        emailLayout.setError(null);
                        emailLayout.setErrorEnabled(false);
                        validationSuccess = true;
                    }
                } else {
                    emailLayout.setErrorEnabled(true);
                    emailLayout.setError(getResources().getString(R.string.invalid_email));
                    validationSuccess = false;
                }

                if (validationSuccess) {
                    Log.v(TAG, "Validation success");
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View view = inflater.inflate(R.layout.cell_invites_email, null);
                    view.setTag(counter);
                    container.addView(view);

                    counter++;

                    TextView emailView = view.findViewById(R.id.email);
                    emailView.setText(emailText);
                    email.setText("");

                    ImageView delete = view.findViewById(R.id.delete_invite);
                    delete.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                      container.removeView(view);
                                                  }
                                              }
                    );

                } else {
                    Log.e(TAG, "Validation failed");
                }

                break;

            case R.id.send_invites:

                listOfEmails = new ArrayList<>();

                for (int i = 0; i < container.getChildCount(); i++) {
                    View row = container.getChildAt(i);
                    TextView text = row.findViewById(R.id.email);
                    String emailId = text.getText().toString();
                    listOfEmails.add(emailId);
                }
                mBinding.setStatus(true);
                mPresenter.addInvites();
                break;

            default:
                break;

        }
    }

    private boolean isEmailAlreadyAdded(String emailText) {
        ArrayList<String> listOfEmail = PreferenceManager.getInstance(this).getListOfInvitees();
        return listOfEmail.contains(emailText);
    }

    public boolean emailValidator(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        mBinding.setStatus(active);
    }

    @Override
    public void showLoadingError(String message) {

    }

    @Override
    public void finishActivity() {
        this.finish();
    }

    @Override
    public ArrayList<String> getListOfEmails() {
        return listOfEmails;
    }
}
