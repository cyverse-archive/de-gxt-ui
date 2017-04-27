package org.iplantc.de.client.models.groups;

import org.iplantc.de.client.models.HasDescription;
import org.iplantc.de.client.models.HasId;

import com.google.gwt.user.client.ui.HasName;
import com.google.web.bindery.autobean.shared.AutoBean;

/**
 * Autobean currently used to represent a Group as defined in the iplant-groups POST /groups endpoint
 *
 * @author aramsey
 */
public interface Group extends HasName, HasDescription, HasId {
    
    String getType();
    void setType(String type);

    @AutoBean.PropertyName("display_name")
    String getDisplayName();

    String getExtension();

}
