/**
 *
 */
package org.iplantc.de.collaborators.client.util;

import org.iplantc.de.client.models.collaborators.CollaboratorAutoBeanFactory;
import org.iplantc.de.client.models.collaborators.Subject;

import com.google.gwt.core.shared.GWT;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.Splittable;

import java.util.List;

import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsType;

/**
 * @author sriram
 *
 */
@JsType
public class CollaboratorsUtil {

    CollaboratorAutoBeanFactory factory = GWT.create(CollaboratorAutoBeanFactory.class);
    private static CollaboratorsUtil INSTANCE;

    CollaboratorsUtil() {
    }

    public static CollaboratorsUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CollaboratorsUtil();
        }

        return INSTANCE;
    }

    @JsIgnore
    public Subject getDummySubject(String userName) {
        Subject subject = factory.getSubject().as();
        subject.setId(userName);
        subject.setName(userName);
        return subject;
    }

    @JsIgnore
    public boolean isCurrentCollaborator(Subject c, List<Subject> subjects) {
        for (Subject current : subjects) {
            if (current.getId().equals(c.getId())) {
                return true;
            }
        }

        return false;
    }

    @SuppressWarnings("unusable-by-js")
    public boolean isGroup(Splittable splSubject) {
        Subject subject = convertToSubject(splSubject);
        return subject.isGroup();
    }

    @SuppressWarnings("unusable-by-js")
    public String getSubjectDisplayName(Splittable splSubject) {
        Subject subject = convertToSubject(splSubject);
        return subject.getSubjectDisplayName();
    }

    @SuppressWarnings("unusable-by-js")
    public boolean isCollaboratorList(Splittable splSubject) {
        Subject subject = convertToSubject(splSubject);
        return subject.isCollaboratorList();
    }

    @SuppressWarnings("unusable-by-js")
    public boolean isTeam(Splittable splSubject) {
        Subject subject = convertToSubject(splSubject);
        return subject.isTeam();
    }

    private Subject convertToSubject(Splittable splittable) {
        return AutoBeanCodex.decode(factory, Subject.class, splittable).as();
    }
}
