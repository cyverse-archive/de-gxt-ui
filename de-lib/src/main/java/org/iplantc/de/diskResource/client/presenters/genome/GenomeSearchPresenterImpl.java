package org.iplantc.de.diskResource.client.presenters.genome;

import org.iplantc.de.client.models.genomes.Genome;
import org.iplantc.de.client.services.FileEditorServiceFacade;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.diskResource.client.GenomeSearchView;
import org.iplantc.de.diskResource.client.gin.factory.GenomeSearchViewFactory;

import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;

import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.box.MessageBox;

public class GenomeSearchPresenterImpl implements GenomeSearchView.GenomeSearchPresenter {

    private GenomeSearchView.GenomeSearchViewAppearance appearance;
    private GenomeSearchView view;
    private FileEditorServiceFacade serviceFacade;
    @Inject IplantAnnouncer announcer;

    @Inject
    public GenomeSearchPresenterImpl(GenomeSearchView.GenomeSearchViewAppearance appearance,
                                     GenomeSearchViewFactory viewFactory,
                                     FileEditorServiceFacade serviceFacade,
                                     GenomeSearchRpcProxy searchProxy) {
        this.appearance = appearance;
        this.serviceFacade = serviceFacade;
        this.view = viewFactory.create(getPagingLoader(searchProxy));
    }

    @Override
    public void go(HasOneWidget container) {
        container.setWidget(view);
    }

    @Override
    public void importGenomeFromCoge(Integer id) {
        serviceFacade.importGenomeFromCoge(id, true, new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                announcer.schedule(new ErrorAnnouncementConfig(appearance.cogeImportGenomeError()));
            }

            @Override
            public void onSuccess(String result) {
                MessageBox amb =
                        new MessageBox(SafeHtmlUtils.fromTrustedString(appearance.importFromCoge()), SafeHtmlUtils.fromTrustedString(appearance.cogeImportGenomeSuccess()));
                amb.setIcon(appearance.infoIcon());
                amb.show();
            }
        });
    }

    PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Genome>> getPagingLoader(GenomeSearchRpcProxy searchProxy) {
        return new PagingLoader<>(searchProxy);
    }
}
