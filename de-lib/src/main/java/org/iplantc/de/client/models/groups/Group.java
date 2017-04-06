package org.iplantc.de.client.models.groups;

import org.iplantc.de.client.models.HasDescription;
import org.iplantc.de.client.models.HasId;

import com.google.gwt.user.client.ui.HasName;

import com.oracle.webservices.internal.api.message.PropertySet;

/**
 * @author aramsey
 */
public interface Group extends HasName, HasDescription, HasId {
    
    String getType();
    void setType(String type);

    @PropertySet.Property("display_name")
    String getDisplayName();

    String getExtension();

}
