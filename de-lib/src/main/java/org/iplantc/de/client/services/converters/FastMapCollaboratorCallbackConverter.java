package org.iplantc.de.client.services.converters;

import org.iplantc.de.client.models.collaborators.Collaborator;
import org.iplantc.de.client.models.collaborators.CollaboratorAutoBeanFactory;
import org.iplantc.de.client.util.JsonUtil;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

import com.sencha.gxt.core.shared.FastMap;

/**
 * @author aramsey
 */
public class FastMapCollaboratorCallbackConverter extends AsyncCallbackConverter<String, FastMap<Collaborator>> {

    CollaboratorAutoBeanFactory factory;
    JsonUtil jsonUtil = JsonUtil.getInstance();

    public FastMapCollaboratorCallbackConverter(AsyncCallback<FastMap<Collaborator>> callback, CollaboratorAutoBeanFactory factory) {
        super(callback);
        this.factory = factory;
    }

    @Override
    protected FastMap<Collaborator> convertFrom(String object) {
        FastMap<Collaborator> userResults = new FastMap<>();

        JSONObject users = jsonUtil.getObject(object);
        if (object != null) {

            for (String username : users.keySet()) {
                JSONObject userJson = jsonUtil.getObject(users, username);
                AutoBean<Collaborator> bean = AutoBeanCodex.decode(factory, Collaborator.class,
                                                                   userJson.toString());
                userResults.put(username, bean.as());
            }

        }
        return userResults;
    }
}
