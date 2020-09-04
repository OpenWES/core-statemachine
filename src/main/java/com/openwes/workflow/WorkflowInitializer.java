package com.openwes.workflow;

import com.typesafe.config.Config;
import com.openwes.core.interfaces.Initializer;

/**
 *
 * @author xuanloc0511@gmail.com
 */
public class WorkflowInitializer implements Initializer {

    @Override
    public String configKey() {
        return "workflow";
    }

    @Override
    public void onStart(Config config) throws Exception {
        WorkFlowManager.instance()
                .start(config);
    }

    @Override
    public void onShutdow(Config config) throws Exception {
        WorkFlowManager.instance()
                .shutdown();
    }

}
