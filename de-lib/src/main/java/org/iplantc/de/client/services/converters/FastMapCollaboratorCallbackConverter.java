package org.iplantc.de.client.services.converters;

import org.iplantc.de.client.models.collaborators.CollaboratorAutoBeanFactory;
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
                Subject subject = AutoBeanCodex.decode(factory, Subject.class,
                                                                      userJson.toString()).as();
                subject.setDisplayName(subject.getName());
                userResults.put(username, subject);
            }
        }
        return userResults;
    }
}
