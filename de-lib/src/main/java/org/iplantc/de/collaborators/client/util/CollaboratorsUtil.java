/**
 *
 */
package org.iplantc.de.collaborators.client.util;

import org.iplantc.de.client.models.collaborators.Collaborator;
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

    public Collaborator getDummyCollaborator(String userName) {
        JSONObject obj = new JSONObject();
        obj.put("username", new JSONString(userName));
        AutoBean<Collaborator> bean = AutoBeanCodex.decode(factory, Collaborator.class, obj.toString());
        return bean.as();
    }

    public boolean isCurrentCollaborator(Collaborator c, List<Collaborator> collaborators) {
        for (Collaborator current : collaborators) {
            if (current.getUserName().equals(c.getUserName())) {
                return true;
            }
        }

        return false;
    }
}
