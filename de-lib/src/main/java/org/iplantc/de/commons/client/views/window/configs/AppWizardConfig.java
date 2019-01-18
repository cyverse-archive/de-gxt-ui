package org.iplantc.de.commons.client.views.window.configs;

import com.google.web.bindery.autobean.shared.Splittable;

public interface AppWizardConfig extends WindowConfig {
    
    Splittable getAppTemplate();

    String getSystemId();

    void setSystemId(String systemId);

    String getAppId();

    void setAppId(String appId);

    void setAppTemplate(Splittable appTemplate);

    boolean isRelaunchAnalysis();

    void setRelaunchAnalysis(boolean relaunchAnalysis);

    String getAnalysisId();

    void setAnalysisId(String analysisId);

}

