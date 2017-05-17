package org.iplantc.de.client.services.converters;

import org.iplantc.de.client.models.collaborators.OldCollaborator;
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.collaborators.CollaboratorAutoBeanFactory;
import org.iplantc.de.client.models.collaborators.CollaboratorsList;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author aramsey
 */
public class CollaboratorListCallbackConverter extends AsyncCallbackConverter<String, List<Subject>> {

    private CollaboratorAutoBeanFactory factory;

    public CollaboratorListCallbackConverter(AsyncCallback<List<Subject>> callback, CollaboratorAutoBeanFactory factory) {
        super(callback);
        this.factory = factory;
    }

    @Override
    protected List<Subject> convertFrom(String object) {
        AutoBean<CollaboratorsList> decode = AutoBeanCodex.decode(factory, CollaboratorsList.class, object);
        List<OldCollaborator> collaborators = decode.as().getCollaborators();

        List<Subject> subjects = Lists.newArrayList();
        collaborators.forEach(new Consumer<OldCollaborator>() {
            @Override
            public void accept(OldCollaborator oldCollaborator) {
                Subject subject = factory.getSubject().as();
                subject.setId(oldCollaborator.getUserName());
                subject.setFirstName(oldCollaborator.getFirstName());
                subject.setLastName(oldCollaborator.getLastName());
                subject.setEmail(oldCollaborator.getEmail());
                subject.setInstitution(oldCollaborator.getInstitution());
                subject.setSourceId("ldap");
                subjects.add(subject);
            }
        });
        return subjects;
    }
}
