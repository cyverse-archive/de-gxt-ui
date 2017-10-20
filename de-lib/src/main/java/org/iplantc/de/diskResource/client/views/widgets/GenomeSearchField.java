package org.iplantc.de.diskResource.client.views.widgets;

import org.iplantc.de.client.models.genomes.Genome;
import org.iplantc.de.commons.client.widgets.SearchField;

import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;

/**
 * A SearchField specifically for searching genomes, used in the Data window
 */
public class GenomeSearchField extends SearchField<Genome> {
    public GenomeSearchField(PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Genome>> loader) {
        super(loader);
    }

    @Override
    protected void clearFilter() {
        // Do not reload.
    }
}
