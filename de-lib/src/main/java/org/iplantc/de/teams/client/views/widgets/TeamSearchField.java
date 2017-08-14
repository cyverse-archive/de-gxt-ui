package org.iplantc.de.teams.client.views.widgets;

import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.commons.client.widgets.SearchField;

import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;

/**
 * A SearchField used in the Teams tab of the Collaboration window
 */
public class TeamSearchField extends SearchField<Group> {

    public TeamSearchField(PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Group>> loader) {
        super(loader);
    }
}
