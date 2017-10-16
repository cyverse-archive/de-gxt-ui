package org.iplantc.de.diskResource.client.presenters.genome;

import org.iplantc.de.client.models.genomes.Genome;
import org.iplantc.de.client.services.FileEditorServiceFacade;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.diskResource.client.GenomeSearchView;

import com.google.common.base.Strings;
import com.google.gwt.user.client.rpc.AsyncCallback;

import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.loader.FilterConfig;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

import java.util.List;
import javax.inject.Inject;

/**
 * An RpcProxy class that handles searching genomes when the user has typed a search term in the GenomeSearchView's search field
 */
public class GenomeSearchRpcProxy extends RpcProxy<FilterPagingLoadConfig, PagingLoadResult<Genome>> {

    private GenomeSearchView.GenomeSearchViewAppearance appearance;
    private FileEditorServiceFacade serviceFacade;
    private IplantAnnouncer announcer;
    String lastQueryText;

    @Inject
    GenomeSearchRpcProxy(GenomeSearchView.GenomeSearchViewAppearance appearance,
                         FileEditorServiceFacade serviceFacade,
                         IplantAnnouncer announcer) {
        this.appearance = appearance;
        this.serviceFacade = serviceFacade;
        this.announcer = announcer;
    }

    @Override
    public void load(FilterPagingLoadConfig loadConfig,
                     AsyncCallback<PagingLoadResult<Genome>> callback) {
        lastQueryText = "";

        List<FilterConfig> filterConfigs = loadConfig.getFilters();
        if (filterConfigs != null && !filterConfigs.isEmpty()) {
            lastQueryText = filterConfigs.get(0).getValue();
        }

        if (Strings.isNullOrEmpty(lastQueryText)) {
            PagingLoadResultBean<Genome> noResults = new PagingLoadResultBean<>();
            callback.onSuccess(noResults);
            return;
        }

        serviceFacade.searchGenomesInCoge(lastQueryText, new AsyncCallback<List<Genome>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
                announcer.schedule(new ErrorAnnouncementConfig(appearance.cogeSearchError()));
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(List<Genome> result) {
                PagingLoadResultBean<Genome> loadResult = getLoadResult(result, loadConfig);
                callback.onSuccess(loadResult);
            }
        });
    }

    PagingLoadResultBean<Genome> getLoadResult(List<Genome> result, FilterPagingLoadConfig loadConfig) {
        return new PagingLoadResultBean<>(result, result.size(), loadConfig.getOffset());
    }
}
