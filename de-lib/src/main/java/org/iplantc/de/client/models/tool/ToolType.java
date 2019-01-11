package org.iplantc.de.client.models.tool;

import org.iplantc.de.client.models.HasDescription;
import org.iplantc.de.client.models.HasId;

import com.google.gwt.user.client.ui.HasName;

/**
 * @author aramsey
 *
 * The autobean representation of a tool type i.e. executable, interactive, osg, etc.
 */
public interface ToolType extends HasId, HasDescription, HasName {
    enum Types {
        executable,
        interactive,
        osg,
    }

    void setLabel(String label);
    String getLabel();
}
