package org.iplantc.de.client.models.pipelines;

import org.iplantc.de.client.models.HasSystemId;

import com.google.gwt.user.client.ui.HasName;
import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import java.util.List;

/**
 * An AutoBean interface for a Pipeline App.
 * 
 * @author psarando
 *
 */
public interface PipelineTask extends HasName, HasSystemId {

    @PropertyName("id")
    String getTaskId();

    @PropertyName("id")
    void setTaskId(String task_id);

    @PropertyName("app_type")
    String getAppType();

    @PropertyName("app_type")
    void setAppType(String appType);

    String getDescription();

    void setDescription(String description);

    Integer getStep();

    void setStep(Integer step);

    List<PipelineAppMapping> getMappings();

    void setMappings(List<PipelineAppMapping> mappings);

    List<PipelineAppData> getInputs();

    void setInputs(List<PipelineAppData> inputs);

    List<PipelineAppData> getOutputs();

    void setOutputs(List<PipelineAppData> outputs);
}

