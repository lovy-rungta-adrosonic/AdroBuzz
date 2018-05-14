package com.adrosonic.adrobuzz.components.Speech2Text;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.databinding.DataBindingUtil;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adrosonic.adrobuzz.R;
import com.adrosonic.adrobuzz.Utils.BasePermissionActivity;
import com.adrosonic.adrobuzz.Utils.PreferenceManager;
import com.adrosonic.adrobuzz.Utils.Utility;
import com.adrosonic.adrobuzz.components.main.App;
import com.adrosonic.adrobuzz.components.main.MainActivity;
import com.adrosonic.adrobuzz.contract.SpeechToTextContract;
import com.adrosonic.adrobuzz.databinding.ActivitySpeechToTextBinding;
import com.adrosonic.adrobuzz.sync.api.Service;
import com.adrosonic.adrobuzz.translation_engine.TranslatorFactory;
import com.adrosonic.adrobuzz.translation_engine.translators.IConvertor;
import com.adrosonic.adrobuzz.translation_engine.utils.ConversionCallaback;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
                    dataRefreshTimer = new CountDownTimer(5000, 1000) {
                        @Override

                        public void onTick(long l) {
                        }

                        @Override
                        public void onFinish() {
                            Log.v(TAG, "TIMER FINISH");
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

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
    }

    @OnClick({R.id.startRecording, R.id.stopRecording, R.id.endConf, R.id.send_mail, R.id.logout, R.id.edit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startRecording:
                Toast.makeText(this, R.string.recording_started, Toast.LENGTH_SHORT).show();
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                TranslatorFactory.getInstance().
                        getTranslator(SpeechToTextActivity.this).
                        initialize("Hello There", SpeechToTextActivity.this);
                setUpView(false);
                break;

            case R.id.stopRecording:
                Toast.makeText(this, R.string.recording_stopped, Toast.LENGTH_SHORT).show();
                stopRecording();
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
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

                File file = new File(Utility.filePath());
                if (file.exists()) {
                    file.delete();
                }
                String summary = PreferenceManager.getInstance(SpeechToTextActivity.this).getConferenceSummary();
                FileWriter writer;
                try {
                    writer = new FileWriter(file);
                    writer.write(summary);
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                PreferenceManager.getInstance(this).setEmailSent(true);
                if (isAdmin) {
                    //TODO send mail form admin to all
                    sendEmail();
                } else {
                    // TODO send mail to self
                }

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
                        .setPositiveButton(R.string.save,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        sttOutput.setText(userInput.getText());
                                        PreferenceManager.getInstance(SpeechToTextActivity.this).setEmailSent(false);
                                        PreferenceManager.getInstance(SpeechToTextActivity.this).setConferenceSummary(userInput.getText().toString());
                                    }
                                })
                        .setNegativeButton(R.string.cancel,
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
            if (confStatus == 3) {

            } else {
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
        Toast.makeText(this, R.string.done, Toast.LENGTH_SHORT).show();
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
        showAlert(getString(R.string.msg_conference_ended_by_admin));
        setUpViewConfEnded();
        stopTimer();
    }

    private void setUpView(boolean flag) {
        if (flag) {
            startRecording.setVisibility(View.VISIBLE);
            statusOfRecording.setText(R.string.start_recording);
            stopRecording.setVisibility(View.GONE);
        } else {
            startRecording.setVisibility(View.GONE);
            stopRecording.setVisibility(View.VISIBLE);
            statusOfRecording.setText(R.string.stop_recording);
        }

    }

    private void setUpViewConfEnded() {
        send.setVisibility(View.VISIBLE);
        mBinding.setEditSummary(true);
        startRecording.setVisibility(View.GONE);
        stopRecording.setVisibility(View.GONE);
        statusOfRecording.setVisibility(View.INVISIBLE);
    }

    public void showAlert(String message) {

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        Button neutralButton = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        LinearLayout.LayoutParams neutralButtonLL = (LinearLayout.LayoutParams) neutralButton.getLayoutParams();
        neutralButtonLL.weight = 10;
        neutralButton.setLayoutParams(neutralButtonLL);
    }

    public void showAlertEmailBeforeLogOutAdmin() {
        AlertDialog alertDialog = new AlertDialog.Builder(SpeechToTextActivity.this).create();
        alertDialog.setMessage(getString(R.string.msg_do_you_want_to_send_email_before_logging_out));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.send),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        //TODO send mail for admin

                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.logout),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        clearPrefsAndExit();
                    }
                });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        Button btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        Button btnNeutral = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
        layoutParams.weight = 10;
        btnPositive.setLayoutParams(layoutParams);
        btnNegative.setLayoutParams(layoutParams);
        btnNeutral.setLayoutParams(layoutParams);

    }

    public void showAlertEmailBeforeLogOutNonAdmin() {
        AlertDialog alertDialog = new AlertDialog.Builder(SpeechToTextActivity.this).create();
        alertDialog.setMessage(getString(R.string.msg_do_you_want_to_send_email_before_logging_out));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.send),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO send mail for non admin


                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startTimer();
                        startRecording();
                        dialog.dismiss();
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.logout),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        clearPrefsAndExit();
                    }
                });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        Button btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        Button btnNeutral = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
        layoutParams.weight = 10;
        btnPositive.setLayoutParams(layoutParams);
        btnNegative.setLayoutParams(layoutParams);
        btnNeutral.setLayoutParams(layoutParams);

    }

    public void showAlertSureToLogout() {

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage(getString(R.string.msg_are_you_sure_you_want_to_logout));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        clearPrefsAndExit();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        Button btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
        layoutParams.weight = 10;
        btnPositive.setLayoutParams(layoutParams);
        btnNegative.setLayoutParams(layoutParams);
    }

    public void showAlertConfOngoing() {
        final boolean isEmailSent = PreferenceManager.getInstance(this).getEmailSent();

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage(getString(R.string.msg_conference_not_ended_sure_you_want_to_logout));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (isEmailSent) {
                            clearPrefsAndExit();
                        } else {
                            showAlertEmailBeforeLogOutNonAdmin();
                        }
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startTimer();
                        startRecording();
                    }
                });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        Button btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
        layoutParams.weight = 10;
        btnPositive.setLayoutParams(layoutParams);
        btnNegative.setLayoutParams(layoutParams);

    }

    public void showAlertEndConference() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage(getString(R.string.msg_end_conference_before_logging_off));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        Button neutralButton = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        LinearLayout.LayoutParams neutralButtonLL = (LinearLayout.LayoutParams) neutralButton.getLayoutParams();
        neutralButtonLL.weight = 10;
        neutralButton.setLayoutParams(neutralButtonLL);
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

    public void sendEmail() {
        try {
            PackageManager pm =this.getPackageManager();
            ResolveInfo selectedEmailActivity = null;

            Intent emailDummyIntent = new Intent(Intent.ACTION_SENDTO);
            emailDummyIntent.setData(Uri.parse("mailto:test@gmail.com"));
            List<ResolveInfo> emailActivities = pm.queryIntentActivities(emailDummyIntent, 0);
            if (null == emailActivities || emailActivities.size() == 0) {
                Intent emailDummyIntentRFC822 = new Intent(Intent.ACTION_SEND_MULTIPLE);
                emailDummyIntentRFC822.setType("message/rfc822");
                emailActivities = pm.queryIntentActivities(emailDummyIntentRFC822, 0);
            }
            if (null != emailActivities) {
                if (emailActivities.size() == 1) {
                    selectedEmailActivity = emailActivities.get(0);
                } else {
                    for (ResolveInfo currAvailableEmailActivity : emailActivities) {
                        if (currAvailableEmailActivity.isDefault) {
                            selectedEmailActivity = currAvailableEmailActivity;
                        }
                    }
                }

                if (null != selectedEmailActivity) {
                    sendEmailUsingSelectedEmailApp(selectedEmailActivity);
                } else {
                    final List<ResolveInfo> emailActivitiesForDialog = emailActivities;
                    String[] availableEmailAppsName = new String[emailActivitiesForDialog.size()];
                    for (int i = 0; i < emailActivitiesForDialog.size(); i++) {
                        availableEmailAppsName[i] = emailActivitiesForDialog.get(i).activityInfo.applicationInfo.loadLabel(pm).toString();
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Please select Email Client");
                    builder.setItems(availableEmailAppsName, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sendEmailUsingSelectedEmailApp(emailActivitiesForDialog.get(which));
                        }
                    });
                    builder.create().show();
                }
            } else {
                sendEmailUsingSelectedEmailApp(null);
            }
        } catch (Exception ex) {
            Log.e(TAG, "Can't send email", ex);
        }
    }

    private void sendEmailUsingSelectedEmailApp(ResolveInfo p_selectedEmailApp) {
        try {

            String confID = PreferenceManager.getInstance(this).getConfID();
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            String aEmailList[] = {"lovy.rungta@adrosonic" +
                    "" +
                    ".com"};
            emailIntent.putExtra(Intent.EXTRA_EMAIL, aEmailList);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Summary for Conference "+confID);
            emailIntent.putExtra(Intent.EXTRA_TEXT, body());

            ArrayList<Uri> attachmentsUris = new ArrayList<>();

            File fileIn = new File(Utility.filePath());
            Uri currAttachemntUri = Uri.fromFile(fileIn);
            attachmentsUris.add(currAttachemntUri);
            emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, attachmentsUris);

            if (null != p_selectedEmailApp) {
                Log.d(TAG, "Sending email using " + p_selectedEmailApp);
                emailIntent.setComponent(new ComponentName(p_selectedEmailApp.activityInfo.packageName, p_selectedEmailApp.activityInfo.name));
                this.startActivity(emailIntent);
            } else {
                Intent emailAppChooser = Intent.createChooser(emailIntent, "Select Email app");
                this.startActivity(emailAppChooser);
            }
        } catch (Exception ex) {
            Log.e(TAG, "Error sending email", ex);
        }
    }

    public String body(){
        return "Dear User,\n\nThank you for attending the conference. Please find attached conference summary.\n\nRegards,\nAdroBuzz Team";
    }
}
