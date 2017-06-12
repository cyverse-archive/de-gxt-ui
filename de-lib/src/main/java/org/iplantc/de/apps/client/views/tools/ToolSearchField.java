package org.iplantc.de.apps.client.views.tools;

import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.commons.client.widgets.SearchField;

import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;

/**
 * Created by sriram on 4/21/17.
 */
public class ToolSearchField extends SearchField<Tool> {

    public ToolSearchField(PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Tool>> loader) {
        super(loader);
    }

    @Override
    protected void clearFilter() {
        // Do not reload.
    }
}
