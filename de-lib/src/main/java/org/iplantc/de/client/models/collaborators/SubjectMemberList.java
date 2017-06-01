package org.iplantc.de.client.models.collaborators;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import java.util.List;

/**
 * @author aramsey
 */
public interface SubjectMemberList {

    @PropertyName("members")
    List<Subject> getSubjects();

    @PropertyName("members")
    void setSubjects(List<Subject> subjects);
}
