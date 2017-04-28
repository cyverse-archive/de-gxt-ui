package org.iplantc.de.client.services.converters;

import org.iplantc.de.client.models.collaborators.Collaborator;
import org.iplantc.de.client.models.collaborators.CollaboratorAutoBeanFactory;
import org.iplantc.de.client.models.collaborators.CollaboratorsList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

import java.util.List;

/**
 * @author aramsey
 */
public class CollaboratorListCallbackConverter extends AsyncCallbackConverter<String, List<Collaborator>> {

    private CollaboratorAutoBeanFactory factory;

    public CollaboratorListCallbackConverter(AsyncCallback<List<Collaborator>> callback, CollaboratorAutoBeanFactory factory) {
        super(callback);
        this.factory = factory;
    }

    @Override
    protected List<Collaborator> convertFrom(String object) {
        AutoBean<CollaboratorsList> decode = AutoBeanCodex.decode(factory, CollaboratorsList.class, object);
        return decode.as().getCollaborators();
    }
}
