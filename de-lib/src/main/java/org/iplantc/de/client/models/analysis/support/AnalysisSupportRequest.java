package org.iplantc.de.client.models.analysis.support;

import com.google.web.bindery.autobean.shared.AutoBean;

/**
 * Created by sriram on 2/22/17.
 */
public interface AnalysisSupportRequest {

    String getFrom();

    void setFrom(String from);

    String getSubject();

    void setSubject(String subject);

    @AutoBean.PropertyName("fields")
    AnalysisSupportRequestFields getFields();

    @AutoBean.PropertyName("fields")
    void setFields(AnalysisSupportRequestFields fields);
}
