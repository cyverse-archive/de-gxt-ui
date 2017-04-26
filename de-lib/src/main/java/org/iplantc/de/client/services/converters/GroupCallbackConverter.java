package org.iplantc.de.client.services.converters;

import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.GroupAutoBeanFactory;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

/**
 * @author aramsey
 */
public class GroupCallbackConverter extends AsyncCallbackConverter<String, Group> {

    private final GroupAutoBeanFactory factory;

    public GroupCallbackConverter(AsyncCallback<Group> callback, GroupAutoBeanFactory factory) {
        super(callback);
        this.factory = factory;
    }

    @Override
    protected Group convertFrom(String object) {
        final AutoBean<Group> decode = AutoBeanCodex.decode(factory, Group.class, object);
        return decode.as();
    }
}
