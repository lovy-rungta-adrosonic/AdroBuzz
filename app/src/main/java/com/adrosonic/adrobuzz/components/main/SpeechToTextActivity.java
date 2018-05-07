package com.adrosonic.adrobuzz.components.main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.adrosonic.adrobuzz.R;
import com.microsoft.bing.speech.SpeechClientStatus;
import com.microsoft.cognitiveservices.speechrecognition.DataRecognitionClient;
import com.microsoft.cognitiveservices.speechrecognition.ISpeechRecognitionServerEvents;
import com.microsoft.cognitiveservices.speechrecognition.MicrophoneRecognitionClient;
import com.microsoft.cognitiveservices.speechrecognition.RecognitionResult;
import com.microsoft.cognitiveservices.speechrecognition.RecognitionStatus;
import com.microsoft.cognitiveservices.speechrecognition.SpeechRecognitionMode;
import com.microsoft.cognitiveservices.speechrecognition.SpeechRecognitionServiceFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import omrecorder.AudioChunk;
import omrecorder.AudioRecordConfig;
import omrecorder.OmRecorder;
import omrecorder.PullTransport;
import omrecorder.PullableSource;
import omrecorder.Recorder;

public class SpeechToTextActivity extends AppCompatActivity implements ISpeechRecognitionServerEvents {

    int m_waitSeconds = 0;
    DataRecognitionClient dataClient = null;
    MicrophoneRecognitionClient micClient = null;
    FinalResponseStatus isReceivedResponse = FinalResponseStatus.NotReceived;
    EditText _logText;
    RadioGroup _radioGroup;
    Button _startButton;
    Button _startRecord, _stopRecord;
    Recorder recorder;
    private Chronometer chronometer;

    private int RECORD_AUDIO_REQUEST_CODE = 123;

    private boolean isRecording = false;

    public enum FinalResponseStatus {NotReceived, OK, Timeout}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_to_text);
        this._logText = (EditText) findViewById(R.id.editText1);
        this._startButton = (Button) findViewById(R.id.button1);
        this._startRecord = (Button) findViewById(R.id.startRecord);
        this._stopRecord = (Button) findViewById(R.id.stopRecord);
        if (getString(R.string.primaryKey).startsWith("Please")) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.add_subscription_key_tip_title))
                    .setMessage(getString(R.string.add_subscription_key_tip))
                    .setCancelable(false)
                    .show();
        }

        // setup the buttons
        final SpeechToTextActivity This = this;
        this._startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                _logText.setText("");
                This.StartButton_Click(arg0);
            }
        });

        chronometer = (Chronometer) findViewById(R.id.chronometerTimer);
        chronometer.setBase(SystemClock.elapsedRealtime());

        this._startRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getPermissionToRecordAudio();
                }
            }
        });


        this._stopRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(isRecording){
                        recorder.stopRecording();
                        chronometer.stop();
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        isRecording = false;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * Gets the primary subscription key
     */
    public String getPrimaryKey() {
        return this.getString(R.string.primaryKey);
    }

    /**
     * Gets the LUIS application identifier.
     *
     * @return The LUIS application identifier.
     */
    private String getLuisAppId() {
        return this.getString(R.string.luisAppID);
    }

    /**
     * Gets the LUIS subscription identifier.
     *
     * @return The LUIS subscription identifier.
     */
    private String getLuisSubscriptionID() {
        return this.getString(R.string.luisSubscriptionID);
    }

    /**
     * Gets the default locale.
     *
     * @return The default locale.
     */
    private String getDefaultLocale() {
        return "en-us";
    }

    /**
     * Gets the Cognitive Service Authentication Uri.
     *
     * @return The Cognitive Service Authentication Uri.  Empty if the global default is to be used.
     */
    private String getAuthenticationUri() {
        return this.getString(R.string.authenticationUri);
    }

    @NonNull
    private File file() {
        File root = android.os.Environment.getExternalStorageDirectory();
        File file = new File(root.getAbsolutePath() + "/AdroBuzzWAV/Audios");
        if (!file.exists()) {
            file.mkdirs();
        }

        int num = 0;
        String filePath = root.getAbsolutePath() + "/AdroBuzzWAV/Audios/" +
                "Buzz" + ".wav";

        File f = new File(filePath);
        while (f.exists()) {
            filePath = root.getAbsolutePath() + "/AdroBuzzWAV/Audios/Buzz" + (num++) + ".wav";
            f = new File(filePath);
        }

        return new File(filePath);
    }


    private PullableSource mic() {
        return new PullableSource.Default(
                new AudioRecordConfig.Default(
                        MediaRecorder.AudioSource.MIC, AudioFormat.ENCODING_PCM_16BIT,
                        AudioFormat.CHANNEL_IN_MONO, 44100
                )
        );

    }

    /**
     * Handles the Click event of the _startButton control.
     */
    private void StartButton_Click(View arg0) {
        this._startButton.setEnabled(false);
        this.m_waitSeconds = 200;

        this.LogRecognitionStart();

            if (null == this.dataClient) {
                    this.dataClient = SpeechRecognitionServiceFactory.createDataClient(
                            this,
                            SpeechRecognitionMode.LongDictation,
                            this.getDefaultLocale(),
                            this,
                            this.getPrimaryKey());

                this.dataClient.setAuthenticationUri(this.getAuthenticationUri());
            }

            this.SendAudioHelper();

    }

    /**
     * Logs the recognition start.
     */
    private void LogRecognitionStart() {

        this.WriteLine("\n Start speech recognition " + "\n");

    }

    private void SendAudioHelper() {
        RecognitionTask doDataReco = new RecognitionTask(this.dataClient);

        try {
            doDataReco.execute().get(m_waitSeconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            doDataReco.cancel(true);
            isReceivedResponse = FinalResponseStatus.Timeout;
        }
    }

    public void onFinalResponseReceived(final RecognitionResult response) {
        boolean isFinalDicationMessage = SpeechRecognitionMode.LongDictation == SpeechRecognitionMode.LongDictation &&
                (response.RecognitionStatus == RecognitionStatus.EndOfDictation ||
                        response.RecognitionStatus == RecognitionStatus.DictationEndSilenceTimeout);

        if (isFinalDicationMessage) {
            this._startButton.setEnabled(true);
            this.isReceivedResponse = FinalResponseStatus.OK;
        }

        if (!isFinalDicationMessage) {
            for (int i = 0; i < response.Results.length; i++) {
                this.WriteLine(response.Results[i].DisplayText);
            }
            this.WriteLine();
        }
    }

    /**
     * Called when a final response is received and its intent is parsed
     */
    public void onIntentReceived(final String payload) {
        this.WriteLine("--- Intent received by onIntentReceived() ---");
        this.WriteLine(payload);
        this.WriteLine();
    }

    public void onPartialResponseReceived(final String response) {
//        this.WriteLine("--- Partial result received by onPartialResponseReceived() ---");
//        this.WriteLine(response);
//        this.WriteLine();
    }

    public void onError(final int errorCode, final String response) {
        this._startButton.setEnabled(true);
        this.WriteLine("--- Error received by onError() ---");
        this.WriteLine("Error code: " + SpeechClientStatus.fromInt(errorCode) + " " + errorCode);
        this.WriteLine("Error text: " + response);
        this.WriteLine();
    }

    /**
     * Called when the microphone status has changed.
     *
     * @param recording The current recording state
     */
    public void onAudioEvent(boolean recording) {
        this.WriteLine("--- Microphone status change received by onAudioEvent() ---");
        this.WriteLine("********* Microphone status: " + recording + " *********");
        if (recording) {
            this.WriteLine("Please start speaking.");
        }

        WriteLine();
        if (!recording) {
            this.micClient.endMicAndRecognition();
            this._startButton.setEnabled(true);
        }
    }

    /**
     * Writes the line.
     */
    private void WriteLine() {
        this.WriteLine("");
    }

    /**
     * Writes the line.
     *
     * @param text The line to write.
     */
    private void WriteLine(String text) {
        this._logText.append(text + "\n");
    }


    /*
     * Speech recognition with data (for example from a file or audio source).
     * The data is broken up into buffers and each buffer is sent to the Speech Recognition Service.
     * No modification is done to the buffers, so the user can apply their
     * own VAD (Voice Activation Detection) or Silence Detection
     *
     * @param dataClient
     */
    private class RecognitionTask extends AsyncTask<Void, Void, Void> {
        DataRecognitionClient dataClient;
        SpeechRecognitionMode recoMode;
        String filename;
//
//        RecognitionTask(DataRecognitionClient dataClient, SpeechRecognitionMode recoMode, String filename) {
//            this.dataClient = dataClient;
//            this.recoMode = recoMode;
//            this.filename = filename;
//        }

        RecognitionTask(DataRecognitionClient dataClient) {
            this.dataClient = dataClient;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Note for wave files, we can just send data from the file right to the server.
                // In the case you are not an audio file in wave format, and instead you have just
                // raw data (for example audio coming over bluetooth), then before sending up any
                // audio data, you must first send up an SpeechAudioFormat descriptor to describe
                // the layout and format of your raw audio data via DataRecognitionClient's sendAudioFormat() method.
                // String filename = recoMode == SpeechRecognitionMode.ShortPhrase ? "whatstheweatherlike.wav" : "batman.wav";
//                InputStream fileStream = getAssets().open(filename);


                File root = android.os.Environment.getExternalStorageDirectory();


                File dir = new File(root.getAbsolutePath() + "/AdroBuzzWAV/Audios/");
                if(dir.exists()){
                    File[] files = dir.listFiles();
                    for (int i = 0; i < files.length; ++i) {
                        File file = files[i];
                        Log.e("CHECKING",file.getAbsolutePath()+":"+file.getName());
                        FileInputStream fileStream = new FileInputStream(file);
                        int bytesRead = 0;
                        byte[] buffer = new byte[1024];

                        do {
                            // Get  Audio data to send into byte buffer.
                            bytesRead = fileStream.read(buffer);

                            if (bytesRead > -1) {
                                // Send of audio data to service.
                                dataClient.sendAudio(buffer, bytesRead);
                            }
                        } while (bytesRead > 0);
                    }
                }
//           String filePath = root.getAbsolutePath() + "/AdroBuzzWAV/Audios/" +
//                        "Buzz" + ".wav";
//
//                File file = new File(filePath);

//                FileInputStream fileStream = new FileInputStream(file);
//                int bytesRead = 0;
//                byte[] buffer = new byte[1024];
//
//                do {
//                    // Get  Audio data to send into byte buffer.
//                    bytesRead = fileStream.read(buffer);
//
//                    if (bytesRead > -1) {
//                        // Send of audio data to service.
//                        dataClient.sendAudio(buffer, bytesRead);
//                    }
//                } while (bytesRead > 0);

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            } finally {
                dataClient.endAudio();
            }

            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getPermissionToRecordAudio() {
        // 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid
        // checking the build version since Context.checkSelfPermission(...) is only available
        // in Marshmallow
        // 2) Always check for permission (even if permission has already been granted)
        // since the user can revoke permissions at any time through Settings
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // The permission is NOT already granted.
            // Check if the user has been asked about this permission already and denied
            // it. If so, we want to give more explanation about why the permission is needed.
            // Fire off an async request to actually get the permission
            // This will show the standard permission request dialog UI
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    RECORD_AUDIO_REQUEST_CODE);

        } else {

            start();

        }
    }

    // Callback with the request from calling requestPermissions(...)
    // Callback with the request from calling requestPermissions(...)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == RECORD_AUDIO_REQUEST_CODE) {
            if (grantResults.length == 3 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED) {

//                Toast.makeText(this, "Record Audio permission granted", Toast.LENGTH_SHORT).show();
                start();

            } else {
                Toast.makeText(this, "You must give permissions to use this app. App is exiting.", Toast.LENGTH_SHORT).show();
                finishAffinity();
            }
        }

    }

    void startRecord() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopAndRestartRecord();
            }
        }, 20000);
    }

    void stopAndRestartRecord() {
        try {
            if (isRecording) {
                recorder.stopRecording();
                chronometer.stop();
                chronometer.setBase(SystemClock.elapsedRealtime());
                isRecording = false;
                start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void start() {
        isRecording = true;
        recorder = OmRecorder.wav(
                new PullTransport.Default(mic(), new PullTransport.OnAudioChunkPulledListener() {
                    @Override
                    public void onAudioChunkPulled(AudioChunk audioChunk) {
//                        animateVoice((float) (audioChunk.maxAmplitude() / 200.0));
                        Log.e("CHECKING", "CHUNK PULLED");
                    }
                }), file());

        recorder.startRecording();
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        startRecord();
    }
}
