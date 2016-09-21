package org.iplantc.de.client.models.pipelines;

import com.google.gwt.user.client.ui.HasName;
import org.iplantc.de.client.models.HasQualifiedId;
import org.iplantc.de.client.models.HasSystemId;

import java.util.List;

/**
 * An AutoBean interface for a Pipeline.
 * 
 * @author psarando
 *
 */
public interface Pipeline extends HasName, HasQualifiedId {

    public String getDescription();

    public void setDescription(String description);

    public List<PipelineTask> getApps();

    public void setApps(List<PipelineTask> apps);
}
