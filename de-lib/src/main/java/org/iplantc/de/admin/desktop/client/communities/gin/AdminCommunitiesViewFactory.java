package org.iplantc.de.admin.desktop.client.communities.gin;

import org.iplantc.de.admin.apps.client.AdminAppsGridView;
import org.iplantc.de.admin.desktop.client.communities.AdminCommunitiesView;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.ontologies.OntologyHierarchy;

import com.google.inject.assistedinject.Assisted;

import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;

/**
 * @author aramsey
 */
public interface AdminCommunitiesViewFactory {

    AdminCommunitiesView create(@Assisted("communityTreeStore") TreeStore<Group> communityTreeStore,
                                @Assisted("hierarchyTreeStore") TreeStore<OntologyHierarchy> hierarchyTreeStore,
                                PagingLoader<FilterPagingLoadConfig, PagingLoadResult<App>> loader,
                                @Assisted("hierarchyGridView") AdminAppsGridView communityGridView,
                                @Assisted("communityGridView") AdminAppsGridView hierarchyGridView);
}
