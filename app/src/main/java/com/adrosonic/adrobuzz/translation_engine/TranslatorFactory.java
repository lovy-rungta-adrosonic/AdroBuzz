package com.adrosonic.adrobuzz.translation_engine;

import com.adrosonic.adrobuzz.translation_engine.translators.IConvertor;
import com.adrosonic.adrobuzz.translation_engine.translators.SpeechToTextConvertor;
import com.adrosonic.adrobuzz.translation_engine.utils.ConversionCallaback;

/**
 * Created by Hitesh on 12-07-2016.
 * <p/>
 * This Factory class return object of TTS or STT dependending on input enum TRANSLATOR_TYPE
 */
public class TranslatorFactory {

    private static TranslatorFactory ourInstance = new TranslatorFactory();

    public IConvertor iConvertor;

    private TranslatorFactory() {
    }

    public static TranslatorFactory getInstance() {
        return ourInstance;
    }

    /**
     * Factory method to return object of STT or TTS
     *
     * @param conversionCallaback
     * @return
     */
    public IConvertor getTranslator(ConversionCallaback conversionCallaback) {

        iConvertor = new SpeechToTextConvertor(conversionCallaback);
        //Get speech to text translator
        return iConvertor;

    }
}
