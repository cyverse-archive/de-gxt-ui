package org.iplantc.de.apps.client.gin.factory;

import org.iplantc.de.apps.client.CommunitiesView;
import org.iplantc.de.client.models.groups.Group;

import com.sencha.gxt.data.shared.TreeStore;

/**
 * @author aramsey
 */
public interface CommunitiesViewFactory {
    CommunitiesView create(TreeStore<Group> treeStore);
}
