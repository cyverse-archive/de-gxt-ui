package org.iplantc.de.client.models.collaborators;

import java.util.List;

/**
 * The autobean representation of a list of Subjects
 * @author aramsey
 */
public interface SubjectList {

    List<Subject> getSubjects();

    void setSubjects(List<Subject> subjects);
}
