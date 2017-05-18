package org.iplantc.de.client.services.converters;

import org.iplantc.de.client.models.collaborators.CollaboratorAutoBeanFactory;
import org.iplantc.de.client.models.collaborators.OldCollaborator;
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.util.JsonUtil;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

import com.sencha.gxt.core.shared.FastMap;

/**
 * @author aramsey
 */
public class FastMapCollaboratorCallbackConverter extends AsyncCallbackConverter<String, FastMap<Subject>> {

    CollaboratorAutoBeanFactory factory;
    JsonUtil jsonUtil = JsonUtil.getInstance();

    public FastMapCollaboratorCallbackConverter(AsyncCallback<FastMap<Subject>> callback, CollaboratorAutoBeanFactory factory) {
        super(callback);
        this.factory = factory;
    }

    @Override
    protected FastMap<Subject> convertFrom(String object) {
        FastMap<Subject> userResults = new FastMap<>();

        JSONObject users = jsonUtil.getObject(object);
        if (object != null) {

            for (String username : users.keySet()) {
                JSONObject userJson = jsonUtil.getObject(users, username);
                OldCollaborator oldCollaborator = AutoBeanCodex.decode(factory, OldCollaborator.class,
                                                                      userJson.toString()).as();
                Subject subject = factory.getSubject().as();
                subject.setId(oldCollaborator.getUserName());
                subject.setFirstName(oldCollaborator.getFirstName());
                subject.setLastName(oldCollaborator.getLastName());
                subject.setName(oldCollaborator.getName());
                subject.setEmail(oldCollaborator.getEmail());
                subject.setInstitution(oldCollaborator.getInstitution());
                subject.setSourceId("ldap");

                userResults.put(username, subject);
            }

        }
        return userResults;
    }
}
