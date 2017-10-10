package org.iplantc.de.diskResource.client.gin.factory;

import org.iplantc.de.client.models.genomes.Genome;
import org.iplantc.de.diskResource.client.GenomeSearchView;

import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;

public interface GenomeSearchViewFactory {
    GenomeSearchView create(PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Genome>> loader);
}
