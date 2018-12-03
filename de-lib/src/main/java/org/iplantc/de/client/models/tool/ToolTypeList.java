package org.iplantc.de.client.models.tool;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import java.util.List;

/**
 * @author aramsey
 *
 * The autobean representation of a list of type ToolType
 */

public interface ToolTypeList {

    @PropertyName("tool_types")
    void setToolTypes(List<ToolType> types);

    @PropertyName("tool_types")
    List<ToolType> getToolTypes();
}