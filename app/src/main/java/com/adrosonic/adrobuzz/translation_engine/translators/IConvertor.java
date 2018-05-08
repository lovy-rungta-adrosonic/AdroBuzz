package com.adrosonic.adrobuzz.translation_engine.translators;

import android.app.Activity;
import android.content.Context;

/**
 * Created by Hitesh on 12-07-2016.
 */
public interface IConvertor {

    IConvertor initialize(String message, Activity appContext);

    void stopListening();

    void startListening(Context context);


}
