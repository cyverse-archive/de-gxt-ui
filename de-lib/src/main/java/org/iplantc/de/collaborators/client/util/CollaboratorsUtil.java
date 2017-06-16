/**
 *
 */
package org.iplantc.de.collaborators.client.util;

import org.iplantc.de.client.models.collaborators.CollaboratorAutoBeanFactory;
import org.iplantc.de.client.models.collaborators.Subject;

import com.google.gwt.core.shared.GWT;

import java.util.List;

/**
 * @author sriram
 *
 */
public class CollaboratorsUtil {

    private final CollaboratorAutoBeanFactory factory = GWT.create(CollaboratorAutoBeanFactory.class);
    private static CollaboratorsUtil INSTANCE;

    CollaboratorsUtil() {
    }

    public static CollaboratorsUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CollaboratorsUtil();
        }

        return INSTANCE;
    }

    public Subject getDummySubject(String userName) {
        Subject subject = factory.getSubject().as();
        subject.setId(userName);
        subject.setName(userName);
        return subject;
    }

    public boolean isCurrentCollaborator(Subject c, List<Subject> subjects) {
        for (Subject current : subjects) {
            if (current.getId().equals(c.getId())) {
                return true;
            }
        }

        return false;
    }
}
