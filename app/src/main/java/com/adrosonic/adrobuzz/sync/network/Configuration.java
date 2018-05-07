package com.adrosonic.adrobuzz.sync.network;

/**
 * Created by Lovy on 26-04-2018.
 */

public class Configuration {

    private Environment mEnvironment;

    public Configuration(Environment environment) {
        this.mEnvironment = environment;
    }

    public String getBaseURL() {
        switch (mEnvironment) {
            case DEV:
                return "http://34.231.184.70/Conference/services/conferenceAPI/";
            case TEST:
                return "http://34.249.194.50/TEG/services/";
            case UAT:
                return "https://www.bestforexrate.com/TEGAPI/services/";
            case LIVE:
                return "https://www.bestforexrate.co.uk/TEGAPI/services/";
            default:
                return null;
        }
    }

}
