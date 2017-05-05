package org.iplantc.de.client.models.groups;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

/**
 * The AutoBean representation of the response that is returned from updating the
 * list of members in a Group
 * @author aramsey
 */
public interface UpdateMemberResult {

    Boolean isSuccess();

    @PropertyName("subject_id")
    String getSubjectId();

    @PropertyName("subject_name")
    String getSubjectName();

}
