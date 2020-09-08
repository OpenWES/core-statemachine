package com.openwes.statemachine;

import com.typesafe.config.Config;
import com.openwes.core.interfaces.Initializer;

/**
 *
 * @author xuanloc0511@gmail.com
 */
public class StateFlowInitializer implements Initializer {

    @Override
    public String configKey() {
        return "stateflow";
    }

    @Override
    public void onStart(Config config) throws Exception {
        StateFlowManager.instance()
                .start(config);
    }

    @Override
    public void onShutdow(Config config) throws Exception {
        StateFlowManager.instance()
                .shutdown();
    }

}
