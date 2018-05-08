package com.adrosonic.adrobuzz.translation_engine.translators;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;


import com.adrosonic.adrobuzz.translation_engine.utils.ConversionCallaback;
import com.adrosonic.adrobuzz.translation_engine.utils.TranslatorUtil;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Hitesh on 12-07-2016.
 */
public class SpeechToTextConvertor implements IConvertor {

    private ArrayList data;

    private SpeechRecognizer sr;

    public SpeechToTextConvertor(ConversionCallaback conversioCallaBack) {
        this.conversionCallaback = conversioCallaBack;
    }

    private ConversionCallaback conversionCallaback;

    /**
     * s
     * Take speech input and convert it back as text
     */
    @Override
    public SpeechToTextConvertor initialize(String message, Activity appContext) {

        //Prepeare Intent 
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                message);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,

                appContext.getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 15000);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 15000);
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 15000);
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false);
        intent.putExtra("android.speech.extra.DICTATION_MODE", true);

        //Add listeners 
        CustomRecognitionListener listener = new CustomRecognitionListener();
        sr = SpeechRecognizer.createSpeechRecognizer(appContext);
        sr.setRecognitionListener(listener);
        sr.startListening(intent);

        return this;
    }

    @Override
    public void stopListening() {
        if (sr != null) {
            sr.stopListening();
            sr.destroy();
        }
        sr = null;
    }

    @Override
    public void startListening(Context context) {
        if (sr != null) {
            //Prepeare Intent
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,

                    context.getPackageName());
            intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 5000);
            intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 1000);

            intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false);
            intent.putExtra("android.speech.extra.DICTATION_MODE", true);
            sr.startListening(intent);
        }
    }

    class CustomRecognitionListener implements RecognitionListener {
        private static final String TAG = "RecognitionListener";

        public void onReadyForSpeech(Bundle params) {
            Log.d(TAG, "onReadyForSpeech");
        }

        public void onBeginningOfSpeech() {
            Log.d(TAG, "onBeginningOfSpeech");
        }

        public void onRmsChanged(float rmsdB) {
            Log.d(TAG, "onRmsChanged");
        }

        public void onBufferReceived(byte[] buffer) {
            Log.d(TAG, "onBufferReceived");
        }

        public void onEndOfSpeech() {
            Log.d(TAG, "onEndofSpeech");

        }

        public void onError(int error) {
            Log.e(TAG, "error " + error);

            conversionCallaback.onErrorOccurred(TranslatorUtil.getErrorText(error));
        }

        public void onResults(Bundle results) {
            String str = new String();
            Log.d(TAG, "onResults " + results);
            data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

            if (data.size() > 0) {
                str = data.get(0) + "\n";
            } else {
                str = "";
            }
//            for (int i = 0; i < data.size(); i++) {
//                Log.d(TAG, "result " + data.get(i));
//                str += data.get(i) + "\n";
//            }

            conversionCallaback.onSuccess(str);
        }

        public void onPartialResults(Bundle partialResults) {
            Log.d(TAG, "onPartialResults");
        }

        public void onEvent(int eventType, Bundle params) {
            Log.d(TAG, "onEvent " + eventType);
        }


    }


} 