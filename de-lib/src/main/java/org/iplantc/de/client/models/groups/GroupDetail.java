package org.iplantc.de.client.models.groups;

import org.iplantc.de.client.models.HasDescription;
import org.iplantc.de.client.models.collaborators.Subject;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import org.apache.xpath.operations.Bool;

/**
 * Autobean currently used to represent the details within a Group
 *
 * @author aramsey
 */
public interface GroupDetail {

    @PropertyName("created_at")
    Long getCreatedAt();

    @PropertyName("created_by")
    String getCreatedBy();

    @PropertyName("has_composite")
    Boolean hasComposite();

    @PropertyName("is_composite")
    Boolean isComposite();

    @PropertyName("modified_at")
    Long getModifiedAt();

    @PropertyName("modified_by")
    String getModifiedBy();

    @PropertyName("created_by_detail")
    Subject getCreatedByDetail();
}
