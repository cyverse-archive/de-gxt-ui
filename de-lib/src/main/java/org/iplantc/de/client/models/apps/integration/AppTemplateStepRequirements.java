package org.iplantc.de.client.models.apps.integration;

import org.iplantc.de.client.models.tool.ToolResourceRequirements;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

/**
 * Resource requirements for a step in an app template or analysis submission.
 *
 * @author psarando
 */
public interface AppTemplateStepRequirements extends ToolResourceRequirements {

    @PropertyName("step_number")
    int getStepNumber();

    @PropertyName("step_number")
    void setStepNumber(int step);
}
