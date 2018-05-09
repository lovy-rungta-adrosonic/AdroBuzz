package com.adrosonic.adrobuzz.components.Speech2Text;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.adrosonic.adrobuzz.R;
import com.adrosonic.adrobuzz.Utils.PreferenceManager;
import com.adrosonic.adrobuzz.components.main.App;
import com.adrosonic.adrobuzz.contract.SpeechToTextContract;
import com.adrosonic.adrobuzz.databinding.ActivitySpeechToTextBinding;
import com.adrosonic.adrobuzz.model.CreateConfRequest;
import com.adrosonic.adrobuzz.sync.api.Service;
import com.adrosonic.adrobuzz.translation_engine.TranslatorFactory;
import com.adrosonic.adrobuzz.translation_engine.utils.ConversionCallaback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Retrofit;

import static com.adrosonic.adrobuzz.sync.network.Status.LOADING;


public class SpeechToTextActivity extends AppCompatActivity implements ConversionCallaback, SpeechToTextContract.View {

    public static final String TAG = SpeechToTextActivity.class.getSimpleName();

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 12345;
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

    @Inject
    Retrofit retrofit;

    private SpeechToTextContract.Presenter mPresenter;
    private CountDownTimer dataRefreshTimer;

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
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        boolean isAdmin = PreferenceManager.getInstance(this).getIsAdmin();

        if (isAdmin) {
            mBinding.endConf.setVisibility(View.VISIBLE);
        } else {
            mBinding.endConf.setVisibility(View.INVISIBLE);
        }

        if (Build.VERSION.SDK_INT >= 23) {
            requestForPermission();
        } else {
            setUpView();
        }

        dataRefreshTimer = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long l) {

            }
            @Override
            public void onFinish() {
//                @Override
//                public void didRecieveResource(Resource<List<ExchangeRate>> resource) {
////                        view.setLoadingIndicator(false);
//                    switch (resource.status) {
//                        case LOADING:
////                                view.setLoadingIndicator(true);
//                            break;
//                        case ERROR:
////                                view.showLoadingRatesError();
//                            break;
//                        case SUCCESS:
////                                PreferenceManager.getInstance(context).updateSelected(resource.data.get(0));
////                                view.showExchangeRates(resource.data);
//                            Intent intent = new Intent();
//                            intent.setAction("FETCH_RATES_SUCCESSFUL");
//                            sendBroadcast(intent);
//                            break;
//                        default:
//                            break;
//                    }
//                }
//            });
               //TODO make status call and update UI appropriately
                dataRefreshTimer.start();
            }
        }.start();
    }

    @OnClick({R.id.startRecording, R.id.stopRecording, R.id.endConf})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startRecording:
                Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show();
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                TranslatorFactory.getInstance().
                        getTranslator(SpeechToTextActivity.this).
                        initialize("Hello There", SpeechToTextActivity.this);
                Log.d(TAG, "START LISTENING");
                break;

            case R.id.stopRecording:
                Toast.makeText(this, "Recording stopped", Toast.LENGTH_SHORT).show();
                TranslatorFactory.getInstance().iConvertor.stopListening();
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
                Log.d(TAG, "STOP LISTENING");
                break;

            case R.id.endConf:
                Toast.makeText(this, "End Conference", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;

        }
    }


    /**
     * Request Permission
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void requestForPermission() {
        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        if (!isPermissionGranted(permissionsList, Manifest.permission.RECORD_AUDIO))
            permissionsNeeded.add("Require for Speech to text");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {

                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);

                for (int i = 1; i < permissionsNeeded.size(); i++) {
                    message = message + ", " + permissionsNeeded.get(i);
                }
//
//                showMessageOKCancel(message,
//                        new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
//                            }
//                        });
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }

        setUpView();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean isPermissionGranted(List<String> permissionsList, String permission) {

        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);

            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                perms.put(Manifest.permission.RECORD_AUDIO, PackageManager.PERMISSION_GRANTED);

                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                        ) {
                    setUpView();

                } else {
                    Toast.makeText(SpeechToTextActivity.this, "Permission denied: Recording wont work", Toast.LENGTH_SHORT)
                            .show();
//                    finish();
                    //TODO disable buttons for recording
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void setUpView() {
    }


    @Override
    public void onSuccess(String result) {
        liveText.setText(result);
        sttOutput.setText(sttOutput.getText() + "\n" + result);

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

    }

    @Override
    public void conferenceEnded() {

    }

}
