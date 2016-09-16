package org.iplantc.de.client.models.toolRequest;

import com.google.gwt.user.client.ui.HasName;
import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;
import org.iplantc.de.client.models.HasSettableId;

import java.util.Date;

/**
 * @author jstroot
 */
public interface ToolRequest extends HasName, HasSettableId {

    @PropertyName("date_submitted")
    Date getDateSubmitted();

    @PropertyName("date_updated")
    Date getDateUpdated();

    @PropertyName("date_updated")
    void setDateUpdated(Date dateUpdated);

    String getStatus();

    void setStatus(String status);

    @PropertyName("updated_by")
    String getUpdatedBy();

    @Override
    @PropertyName("id")
    String getId();

    String getVersion();
}
