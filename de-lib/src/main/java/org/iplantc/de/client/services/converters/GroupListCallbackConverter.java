package org.iplantc.de.client.services.converters;

import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.GroupAutoBeanFactory;
import org.iplantc.de.client.models.groups.GroupList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

import java.util.List;

/**
 * @author aramsey
 *
 * Converts a string of JSON data to a List of Group
 */
public class GroupListCallbackConverter extends AsyncCallbackConverter<String, List<Group>> {
    private GroupAutoBeanFactory factory;

    public GroupListCallbackConverter(GroupAutoBeanFactory factory,
                                      AsyncCallback<List<Group>> callback) {
        super(callback);
        this.factory = factory;
    }

    @Override
    protected List<Group> convertFrom(String object) {
        GroupList groupList = AutoBeanCodex.decode(factory, GroupList.class, object).as();
        return groupList.getGroups();
    }
}
