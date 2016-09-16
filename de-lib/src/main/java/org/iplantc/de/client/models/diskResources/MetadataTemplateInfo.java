package org.iplantc.de.client.models.diskResources;

import com.google.gwt.user.client.ui.HasName;
import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;
import org.iplantc.de.client.models.HasDescription;
import org.iplantc.de.client.models.HasSettableId;

import java.util.Date;

public interface MetadataTemplateInfo  extends HasSettableId, HasName, HasDescription {

    @PropertyName("last_modified_on")
    Date getLastModifiedDate();

    @PropertyName("last_modified_on")
    void setLastModifiedDate(Date lastModifiedDate);

    @PropertyName("last_modified_by")
    String getLastModifiedBy();

    @PropertyName("last_modified_by")
    void setLastModifiedBy(String userName);

    @PropertyName("created_on")
    Date getCreatedDate();

    @PropertyName("created_on")
    void setCreatedDate(Date createdOn);

    @PropertyName("created_by")
    String getCreatedBy();

    @PropertyName("created_by")
    void setCreatedBy(String userName);

    @PropertyName("deleted")
    Boolean isDeleted();

    @PropertyName("deleted")
    void setDeleted(Boolean delete);

    @PropertyName("description")
    String getDescription();

    @PropertyName("description")
    void setDescription(String desc);

}
