package com.adrosonic.adrobuzz.components.CreateConference;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.adrosonic.adrobuzz.R;
import com.adrosonic.adrobuzz.Utils.Utility;
import com.adrosonic.adrobuzz.components.main.App;
import com.adrosonic.adrobuzz.contract.CreateConferenceContract;
import com.adrosonic.adrobuzz.databinding.ActivityCreateConferenceBinding;
import com.adrosonic.adrobuzz.model.CreateConf;
import com.adrosonic.adrobuzz.model.CreateConfRequest;
import com.adrosonic.adrobuzz.model.User;
import com.adrosonic.adrobuzz.sync.api.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import br.com.ilhasoft.support.validation.Validator;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Retrofit;

public class CreateConference extends AppCompatActivity implements CreateConferenceContract.View, Validator.ValidationListener {

    public static final String TAG = CreateConference.class.getSimpleName();

    CreateConfRequest request;
    @Inject
    Retrofit retrofit;

    @BindView(R.id.date)
    TextView dateView;
    @BindView(R.id.time)
    TextView timeView;
    @BindView(R.id.create_conference)
    Button createConference;

    Unbinder unbinder;
    int mYear, mMonth, mDay, mHour, mMinute;
    String myDate;
    private ActivityCreateConferenceBinding mBinding;
    private Validator validator;

    private CreateConferenceContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_conference);

        ((App) getApplication()).getAppComponent().inject(this);

        Service service = retrofit.create(Service.class);
        mPresenter = new CreateConferencePresenter(this,this,service);

        mBinding.setCreateConf(false);
        final View view = mBinding.getRoot();
        unbinder =  ButterKnife.bind(this, view);
        validator = new Validator(mBinding);
        validator.setValidationListener(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        getCurrentDateTime();
        setDate(mDay, mMonth, mYear);
        setTime(mHour, mMinute);
        myDate = mDay + "/" + (mMonth + 1) + "/" + mYear;

    }

    @OnClick({R.id.date,R.id.time,R.id.create_conference})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date:
                getCurrentDateTime();
                DatePickerDialog datePickerDialog = new DatePickerDialog(this,R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                setDate(dayOfMonth, monthOfYear, year);
                                myDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                getCurrentDateTime();
                                setTime(mHour, mMinute);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
                break;

            case R.id.time:
                getCurrentDateTime();
                TimePickerDialog timePickerDialog = new TimePickerDialog(this,R.style.DialogTheme,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hour,
                                                  int min) {
                                Date strDate = getSelectedDate(myDate);
                                if (strDate != null) {
                                    int val = Utility.compareDate(strDate, new Date());
                                    if (val == 0) {
                                        Calendar datetime = Calendar.getInstance();
                                        Calendar c = Calendar.getInstance();
                                        datetime.set(Calendar.HOUR_OF_DAY, hour);
                                        datetime.set(Calendar.MINUTE, min);
                                        if (datetime.getTimeInMillis() > c.getTimeInMillis()) {
                                            setTime(hour, min);
                                        } else {
                                            getCurrentDateTime();
                                            setTime(mHour, mMinute);
                                        }
                                    } else {
                                        setTime(hour, min);
                                    }
                                }
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
                break;

            case R.id.create_conference:
                validator.toValidate();
                break;

            default:
                break;

        }
    }

    @Override
    public void onValidationSuccess() {
        mBinding.setCreateConf(true);
        mPresenter.fetchConferenceID();
    }

    @Override
    public void onValidationError() {

    }

    @Override
    public void setLoadingIndicator(boolean active) {
        mBinding.setCreateConf(active);
    }

    @Override
    public void showLoadingError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showConfID(CreateConf confID) {
        // API CALL complete and launch start conference screen
        Intent start = new Intent(getBaseContext(), StartConferenceActivity.class);
        start.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(start);
    }

    @Override
    public CreateConfRequest getConferenceParameters() {
        String date = dateView.getText().toString()+ " "+timeView.getText().toString()+":00";

        EditText name = findViewById(R.id.conf_subject);
        EditText venue = findViewById(R.id.venue);
        EditText username = findViewById(R.id.invitee_username);
        EditText email = findViewById(R.id.invitee_email);

        request = new CreateConfRequest.Builder()
                .withName(name.getText().toString())
                .withVenue(venue.getText().toString())
                .withConferenceDate(date)
                .withUser(new User.Builder().withUserName(username.getText().toString())
                        .withEmail(email.getText().toString())
                        .build())
                .build();

        return request;
    }

    void getCurrentDateTime() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
    }

    void setDate(int day, int month, int year) {
//        dateView.setText(day + "-" + (month + 1) + "-" + year);
        dateView.setText(new StringBuilder().append(day).append("/").append(month + 1)
                .append("/").append(year));
    }

    void setTime(int hour, int min) {
//        String format;
//        if (hour == 0) {
//            hour += 12;
//            format = "AM";
//        } else if (hour == 12) {
//            format = "PM";
//        } else if (hour > 12) {
//            hour -= 12;
//            format = "PM";
//        } else {
//            format = "AM";
//        }
        if (min < 10) {
            timeView.setText(new StringBuilder().append(hour).append(":0").append(min));
//                    .append(" ").append(format));
        } else {
            timeView.setText(new StringBuilder().append(hour).append(":").append(min));
//                    .append(" ").append(format));
        }
    }

    Date getSelectedDate(String myDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = sdf.parse(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}

