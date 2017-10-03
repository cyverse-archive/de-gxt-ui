package org.iplantc.de.diskResource.client.presenters.metadata.proxy;

import org.iplantc.de.client.models.ontologies.AstroThesaurusDoc;
import org.iplantc.de.client.models.ontologies.AstroThesaurusResponse;
import org.iplantc.de.client.models.ontologies.MetadataTermSearchResult;
import org.iplantc.de.client.services.OntologyLookupServiceFacade;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

import java.util.List;

/**
 * {@link MetadataTermSearchProxy} that calls the Unified Astronomy Thesaurus service.
 *
 * @author psarando
 */
public class AstroThesaurusProxy extends MetadataTermSearchProxy {

    OntologyLookupServiceFacade svcFacade;

    @Inject
    public AstroThesaurusProxy(OntologyLookupServiceFacade svcFacade) {
        this.svcFacade = svcFacade;
    }

    @Override
    public void load(MetadataTermLoadConfig loadConfig,
                     AsyncCallback<PagingLoadResult<MetadataTermSearchResult>> callback) {
        String queryText = loadConfig.getQuery();

        if (Strings.isNullOrEmpty(queryText)) {
            // nothing to search
            callback.onSuccess(new PagingLoadResultBean<>());

            return;
        }

        AstroThesaurusLoadConfig uatLoadConfig = (AstroThesaurusLoadConfig)loadConfig;

        svcFacade.searchUnifiedAstronomyThesaurus(uatLoadConfig, new AsyncCallback<AstroThesaurusResponse>() {
            @Override
            public void onSuccess(AstroThesaurusResponse result) {
                List<AstroThesaurusDoc> astroTerms = result.getResult().getItems();

                // The UAT service may return duplicates in the results.
                FastMap<AstroThesaurusDoc> filteredResults = new FastMap<>();

                astroTerms.forEach(item -> {
                    if (!Strings.isNullOrEmpty(item.getId())) {
                        filteredResults.put(item.getId(), item);

                        // flatten label
                        item.setLabel(item.getPrefLabel().getValue());
                    }
                });

                List<MetadataTermSearchResult> results = Lists.newArrayList(filteredResults.values());

                // UAT responses do not include a 'total' number of results,
                // so we can't properly support paging.
                callback.onSuccess(new PagingLoadResultBean<>(results, results.size(), 0));
            }

            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }
        });
    }
}
