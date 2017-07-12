package org.iplantc.de.client.services.converters;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.GroupAutoBeanFactory;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

/**
 * @author aramsey
 */
public class GroupToSubjectCallbackConverter extends AsyncCallbackConverter<String, Subject> {

    private final GroupAutoBeanFactory factory;

    public GroupToSubjectCallbackConverter(AsyncCallback<Subject> callback, GroupAutoBeanFactory factory) {
        super(callback);
        this.factory = factory;
    }
    @Override
    protected Subject convertFrom(String object) {
        final Group group = AutoBeanCodex.decode(factory, Group.class, object).as();
        Subject subject = factory.convertGroupToSubject(group);
        return subject;
    }
}
