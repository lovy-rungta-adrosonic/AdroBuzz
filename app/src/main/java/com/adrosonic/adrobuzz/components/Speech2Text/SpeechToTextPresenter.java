package com.adrosonic.adrobuzz.components.Speech2Text;

import android.content.Context;

import com.adrosonic.adrobuzz.Utils.PreferenceManager;
import com.adrosonic.adrobuzz.contract.SpeechToTextContract;
import com.adrosonic.adrobuzz.interactor.SpeechToTextInteractor;
import com.adrosonic.adrobuzz.model.CreateConfRequest;
import com.adrosonic.adrobuzz.sync.api.Service;

/**
 * Created by Lovy on 09-05-2018.
 */

public class SpeechToTextPresenter implements SpeechToTextContract.Presenter{

    private static final String TAG = SpeechToTextPresenter.class.getSimpleName();

    private final SpeechToTextContract.View view;
    private final SpeechToTextInteractor mInteractor;
    private Context context;

    public SpeechToTextPresenter(SpeechToTextContract.View view, Context context, Service service) {
        this.view = view;
        this.context = context;
        mInteractor = new SpeechToTextInteractor(context,service);
    }

    @Override
    public String getConfSubject() {
        CreateConfRequest request = PreferenceManager.getInstance(context).getConfParams();
        if(request!=null){
            return request.getName();
        }else{
            return "Subject";
        }
    }

    @Override
    public void endConference() {

    }
}
