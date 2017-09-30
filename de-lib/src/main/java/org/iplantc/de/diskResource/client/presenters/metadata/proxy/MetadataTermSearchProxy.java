package org.iplantc.de.diskResource.client.presenters.metadata.proxy;

import org.iplantc.de.client.models.ontologies.MetadataTermSearchResult;

import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

/**
 * Metadata term {@link RpcProxy} that calls a search service with a given
 * {@link MetadataTermLoadConfig} and {@link PagingLoadResult} callback.
 *
 * @author psarando
 */
abstract public class MetadataTermSearchProxy
        extends RpcProxy<MetadataTermLoadConfig, PagingLoadResult<MetadataTermSearchResult>> {
}
