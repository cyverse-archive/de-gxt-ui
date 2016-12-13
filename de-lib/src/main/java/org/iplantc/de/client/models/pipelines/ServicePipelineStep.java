package org.iplantc.de.client.models.pipelines;

import org.iplantc.de.client.models.HasDescription;
import org.iplantc.de.client.models.HasSystemId;

import com.google.gwt.user.client.ui.HasName;
import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

/**
 * An AutoBean interface for a service Pipeline step.
 * 
 * @author psarando
 * 
 */
public interface ServicePipelineStep extends HasName, HasDescription, HasSystemId {

    @PropertyName("task_id")
    String getTaskId();

    @PropertyName("task_id")
    void setTaskId(String task_id);

    @PropertyName("app_type")
    String getAppType();

    @PropertyName("app_type")
    void setAppType(String appType);

}
