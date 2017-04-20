package org.iplantc.de.groups.client.gin;

import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.groups.client.GroupView;

import com.sencha.gxt.data.shared.ListStore;

/**
 * @author aramsey
 */
public interface GroupViewFactory {
    GroupView create(ListStore<Group> groups);
}
