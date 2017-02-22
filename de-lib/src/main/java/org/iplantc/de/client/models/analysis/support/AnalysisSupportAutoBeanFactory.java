package org.iplantc.de.client.models.analysis.support;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

/**
 * Created by sriram on 2/22/17.
 */
public interface AnalysisSupportAutoBeanFactory extends AutoBeanFactory {

    AutoBean<AnalysisSupportRequestFields> analysisSupportRequest();

    AutoBean<AnalysisSupportRequest> analysisSupportRequestSubject();
}
