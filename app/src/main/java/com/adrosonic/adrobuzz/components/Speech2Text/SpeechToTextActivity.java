package com.adrosonic.adrobuzz.components.Speech2Text;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adrosonic.adrobuzz.R;
import com.adrosonic.adrobuzz.Utils.BasePermissionActivity;
import com.adrosonic.adrobuzz.Utils.PreferenceManager;
import com.adrosonic.adrobuzz.components.main.App;
import com.adrosonic.adrobuzz.components.main.MainActivity;
import com.adrosonic.adrobuzz.contract.SpeechToTextContract;
import com.adrosonic.adrobuzz.databinding.ActivitySpeechToTextBinding;
import com.adrosonic.adrobuzz.sync.api.Service;
import com.adrosonic.adrobuzz.translation_engine.TranslatorFactory;
import com.adrosonic.adrobuzz.translation_engine.translators.IConvertor;
import com.adrosonic.adrobuzz.translation_engine.utils.ConversionCallaback;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Retrofit;


public class SpeechToTextActivity extends BasePermissionActivity implements ConversionCallaback, SpeechToTextContract.View {

    public static final String TAG = SpeechToTextActivity.class.getSimpleName();
    AudioManager audioManager;
    Unbinder unbinder;
    private ActivitySpeechToTextBinding mBinding;

    @BindView(R.id.startRecording)
    FloatingActionButton startRecording;

    @BindView(R.id.stopRecording)
    FloatingActionButton stopRecording;

    @BindView(R.id.liveText)
    TextView liveText;

    @BindView(R.id.sttOutput)
    TextView sttOutput;

    @BindView(R.id.send_mail)
    ImageView send;

    @BindView(R.id.logout)
    ImageView logout;

    @BindView(R.id.endConf)
    Button endConf;

    @BindView(R.id.edit)
    ImageView edit;

    @BindView(R.id.statusOfRecording)
    TextView statusOfRecording;

    @Inject
    Retrofit retrofit;

    private SpeechToTextContract.Presenter mPresenter;
    private CountDownTimer dataRefreshTimer;
    private boolean isAdmin;
    private int confStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_speech_to_text);
        final View view = mBinding.getRoot();
        unbinder = ButterKnife.bind(this, view);

        ((App) getApplication()).getAppComponent().inject(this);
        Service service = retrofit.create(Service.class);
        mPresenter = new SpeechToTextPresenter(this, this, service);

        mBinding.setPresenter((SpeechToTextPresenter) mPresenter);
        mBinding.setEditSummary(false);
        mBinding.setStatus(false);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        isAdmin = PreferenceManager.getInstance(this).getIsAdmin();
        if (isAdmin) {
            endConf.setVisibility(View.VISIBLE);
        } else {
            endConf.setVisibility(View.GONE);
        }
        sttOutput.setText(PreferenceManager.getInstance(this).getConferenceSummary());

         confStatus = PreferenceManager.getInstance(this).getConferenceStatus();
        switch (confStatus) {
            case 2:
            default:
                send.setVisibility(View.INVISIBLE);
                mBinding.setEditSummary(false);
                if (!isAdmin) {
                    dataRefreshTimer = new CountDownTimer(120000, 1000) {
                        @Override
                        public void onTick(long l) {
                        }

                        @Override
                        public void onFinish() {
                            mPresenter.getConferenceStatus();
                            dataRefreshTimer.start();
                        }
                    }.start();
                }
                break;
            case 3:
               setUpViewConfEnded();
                if (isAdmin) {
                    endConf.setVisibility(View.GONE);
                    PreferenceManager.getInstance(this).setAdminEndedConference(true);
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkForPermission();
    }

    @OnClick({R.id.startRecording, R.id.stopRecording, R.id.endConf, R.id.send_mail, R.id.logout, R.id.edit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startRecording:
                Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show();
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                TranslatorFactory.getInstance().
                        getTranslator(SpeechToTextActivity.this).
                        initialize("Hello There", SpeechToTextActivity.this);
                Log.d(TAG, "START LISTENING");
                setUpView(false);
                break;

            case R.id.stopRecording:
                Toast.makeText(this, "Recording stopped", Toast.LENGTH_SHORT).show();
                stopRecording();
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
                Log.d(TAG, "STOP LISTENING");
                setUpView(true);
                break;

            case R.id.endConf:
                IConvertor iConvertor = TranslatorFactory.getInstance().iConvertor;
                if (iConvertor != null) {
                    iConvertor.stopListening();
                }
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
                mBinding.setStatus(true);
                mPresenter.endConference();
                break;

            case R.id.send_mail:
                Toast.makeText(this, "Mail Sent", Toast.LENGTH_SHORT).show();
                PreferenceManager.getInstance(this).setEmailSent(true);
                //TODO send mail form admin to all
                // TODO send mail to self
                break;

            case R.id.logout:

                if (isAdmin) {
                    boolean isEnded = PreferenceManager.getInstance(this).getAdminEndedConference();
                    if (isEnded) {
                        boolean isEmailSent = PreferenceManager.getInstance(this).getEmailSent();
                        if (isEmailSent) {
                            showAlertSureToLogout();
                        } else {
                            showAlertEmailBeforeLogOutAdmin();
                        }
                    } else {
                        showAlertEndConference();
                    }
                } else {
                    stopRecording();
                    stopTimer();
                    mPresenter.getConferenceStatusLogOut();
                }
                break;

            case R.id.edit:

                LayoutInflater li = LayoutInflater.from(this);
                View promptsView = li.inflate(R.layout.conference_summary_edit, null);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        this);
                alertDialogBuilder.setView(promptsView);
                final EditText userInput = promptsView
                        .findViewById(R.id.conferenceSummary);
                userInput.setText(PreferenceManager.getInstance(this).getConferenceSummary());
                int textLength = userInput.getText().length();
                userInput.setSelection(textLength, textLength);
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Save",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        sttOutput.setText(userInput.getText());
                                        PreferenceManager.getInstance(SpeechToTextActivity.this).setEmailSent(false);
                                        PreferenceManager.getInstance(SpeechToTextActivity.this).setConferenceSummary(userInput.getText().toString());
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                break;

            default:
                break;

        }
    }

    @Override
    public void updateWithPermission(boolean granted) {
        if (granted) {
            if(confStatus == 3){

            }else{
                setUpView(true);
            }
        } else {
            startRecording.setVisibility(View.GONE);
            stopRecording.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSuccess(String result) {
        liveText.setText(result);
        String temp = sttOutput.getText() + "\n" + result;
        sttOutput.setText(temp);

        PreferenceManager.getInstance(this).setConferenceSummary(temp);

        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
        TranslatorFactory.getInstance().iConvertor.startListening(SpeechToTextActivity.this);
    }

    @Override
    public void onCompletion() {
        Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onErrorOccurred(String errorMessage) {
        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
        TranslatorFactory.getInstance().iConvertor.startListening(SpeechToTextActivity.this);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        mBinding.setStatus(active);
    }

    @Override
    public void conferenceEnded() {
        PreferenceManager.getInstance(this).setAdminEndedConference(true);
        PreferenceManager.getInstance(this).setConferenceStatus(3);
        endConf.setVisibility(View.GONE);
        setUpViewConfEnded();
        mBinding.setEditSummary(true);
    }

    @Override
    public void showError(String message) {
        showAlert(message);
    }

    @Override
    public void finalConfStatus(int status) {
        final boolean isEmailSent = PreferenceManager.getInstance(this).getEmailSent();

        switch (status) {

            case 2:
                showAlertConfOngoing();
                break;

            case 3:
                if (isEmailSent) {
                    showAlertSureToLogout();
                } else {
                    showAlertEmailBeforeLogOutAdmin();
                }
                break;
        }
    }

    @Override
    public void confStatus(int status) {
        showAlert("Conference has been ended by admin");
        setUpViewConfEnded();
    }

    private void setUpView(boolean flag) {
        if(flag){
            startRecording.setVisibility(View.VISIBLE);
            statusOfRecording.setText("Start Recording");
            stopRecording.setVisibility(View.GONE);
        }else{
            startRecording.setVisibility(View.GONE);
            stopRecording.setVisibility(View.VISIBLE);
            statusOfRecording.setText("Stop Recording");
        }

    }

    private void setUpViewConfEnded() {
        send.setVisibility(View.VISIBLE);
        mBinding.setEditSummary(true);
        startRecording.setVisibility(View.GONE);
        stopRecording.setVisibility(View.GONE);
        statusOfRecording.setVisibility(View.GONE);
    }

    public void showAlert(String message) {

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public void showAlertEmailBeforeLogOutAdmin() {
        AlertDialog alertDialog = new AlertDialog.Builder(SpeechToTextActivity.this).create();
        alertDialog.setMessage("Do you want to send email before logging out");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Send",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        //TODO send mail

                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Logout",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        clearPrefsAndExit();
                    }
                });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }

    public void showAlertEmailBeforeLogOutNonAdmin() {
        AlertDialog alertDialog = new AlertDialog.Builder(SpeechToTextActivity.this).create();
        alertDialog.setMessage("Do you want to send email before logging out");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Send",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        //TODO send mail

                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startTimer();
                        startRecording();
                        dialog.dismiss();
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Logout",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        clearPrefsAndExit();
                    }
                });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }

    public void showAlertSureToLogout() {

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage("Are you sure you want to logout");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        clearPrefsAndExit();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public void showAlertConfOngoing() {
        final boolean isEmailSent = PreferenceManager.getInstance(this).getEmailSent();

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage("Conference has not been ended yet. Are you sure you want to leave conference?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (isEmailSent) {
                            clearPrefsAndExit();
                        } else {
                            showAlertEmailBeforeLogOutNonAdmin();
                        }
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startTimer();
                        startRecording();
                    }
                });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }

    public void showAlertEndConference() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage("Please end conference before logging off");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public void clearPrefsAndExit() {
        PreferenceManager.getInstance(SpeechToTextActivity.this).clearSharedPreferences();
        stopTimer();
        Intent intent = new Intent(SpeechToTextActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void stopRecording() {
        IConvertor iConvertor = TranslatorFactory.getInstance().iConvertor;
        if (iConvertor != null) {
            iConvertor.stopListening();
        }
    }

    public void startRecording() {
        IConvertor iConvertor = TranslatorFactory.getInstance().iConvertor;
        if (iConvertor != null) {
            iConvertor.startListening(this);
        }
    }

    public void stopTimer() {
        if (dataRefreshTimer != null) {
            dataRefreshTimer.cancel();
            Log.v(TAG, "Timer stopped");
        }
    }

    public void startTimer() {
        if (dataRefreshTimer != null) {
            dataRefreshTimer.start();
            Log.v(TAG, "Timer started");
        }
    }

}
