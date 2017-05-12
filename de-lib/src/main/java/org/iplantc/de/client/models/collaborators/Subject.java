package org.iplantc.de.client.models.collaborators;

import org.iplantc.de.client.models.HasSettableId;

import com.google.gwt.user.client.ui.HasName;
import com.google.web.bindery.autobean.shared.AutoBean;

import java.util.List;

/**
 * The autobean representation of a Grouper subject
 * @author aramsey
 */
public interface Subject extends HasSettableId, HasName {

    @AutoBean.PropertyName("first_name")
    String getFirstName();

    @AutoBean.PropertyName("first_name")
    void setFirstName(String firstName);

    @AutoBean.PropertyName("last_name")
    String getLastName();

    @AutoBean.PropertyName("last_name")
    void setLastName(String lastName);

    @AutoBean.PropertyName("email")
    String getEmail();

    @AutoBean.PropertyName("email")
    void setEmail(String email);

    @AutoBean.PropertyName("institution")
    String getInstitution();

    @AutoBean.PropertyName("institution")
    void setInstitution(String institution);

    @AutoBean.PropertyName("attribute_values")
    List<String> getAttributes();

    @AutoBean.PropertyName("attribute_values")
    void setAttributes(List<String> attributes);

    @AutoBean.PropertyName("source_id")
    String getSourceId();

    @AutoBean.PropertyName("source_id")
    void setSourceId(String sourceId);
}
