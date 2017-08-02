package org.iplantc.de.teams.client.gin;

import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.teams.client.TeamsView;

import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;

public interface TeamsViewFactory {
    TeamsView create(PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Group>> loader);
}
