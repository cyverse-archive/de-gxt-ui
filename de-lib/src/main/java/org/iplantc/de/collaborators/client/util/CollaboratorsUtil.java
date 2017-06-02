/**
 *
 */
package org.iplantc.de.collaborators.client.util;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.collaborators.CollaboratorAutoBeanFactory;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

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

    //TODO Do I still need this for Subject?
    public Subject getDummySubject(String userName) {
        JSONObject obj = new JSONObject();
        obj.put("username", new JSONString(userName));
        AutoBean<Subject> bean = AutoBeanCodex.decode(factory, Subject.class, obj.toString());
        return bean.as();
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
