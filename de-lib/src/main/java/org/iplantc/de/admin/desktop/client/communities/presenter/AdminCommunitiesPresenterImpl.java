package org.iplantc.de.admin.desktop.client.communities.presenter;

import org.iplantc.de.admin.apps.client.AdminAppsGridView;
import org.iplantc.de.admin.desktop.client.communities.AdminCommunitiesView;
import org.iplantc.de.admin.desktop.client.communities.gin.AdminCommunitiesViewFactory;
import org.iplantc.de.admin.desktop.client.communities.service.AdminCommunityServiceFacade;
import org.iplantc.de.admin.desktop.client.ontologies.OntologiesView;
import org.iplantc.de.admin.desktop.client.services.AppAdminServiceFacade;
import org.iplantc.de.admin.desktop.shared.Belphegor;
import org.iplantc.de.apps.client.presenter.toolBar.proxy.AppSearchRpcProxy;
import org.iplantc.de.client.DEClientConstants;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.ontologies.OntologyHierarchy;
import org.iplantc.de.client.services.AppSearchFacade;
import org.iplantc.de.client.services.AppServiceFacade;
import org.iplantc.de.client.util.JsonUtil;
import org.iplantc.de.client.util.OntologyUtil;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.shared.DEProperties;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;

import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;

import java.util.Collections;
import java.util.List;

/**
 * @author aramsey
 */
public class AdminCommunitiesPresenterImpl implements AdminCommunitiesView.Presenter {

    @Inject AppAdminServiceFacade adminAppService;
    @Inject DEClientConstants constants;
    @Inject DEProperties properties;
    @Inject IplantAnnouncer announcer;
    @Inject JsonUtil jsonUtil;
    @Inject AppServiceFacade appService;
    @Inject AppSearchFacade appSearchService;
    OntologyUtil ontologyUtil;
    AppSearchRpcProxy proxy;
    PagingLoader<FilterPagingLoadConfig, PagingLoadResult<App>> loader;
    private AdminCommunitiesView view;
    private AdminCommunityServiceFacade serviceFacade;
    private final TreeStore<Group> communityTreeStore;
    private final TreeStore<OntologyHierarchy> previewTreeStore;
    private AdminCommunitiesView.Appearance appearance;
    private AdminAppsGridView.Presenter hierarchyGridPresenter;
    private AdminAppsGridView.Presenter communityGridPresenter;

    @Inject
    public AdminCommunitiesPresenterImpl(AdminCommunityServiceFacade serviceFacade,
                                         AppSearchFacade appSearchService,
                                         AppServiceFacade appService,
                                         final TreeStore<Group> communityTreeStore,
                                         final TreeStore<OntologyHierarchy> hierarchyTreeStore,
                                         AdminCommunitiesViewFactory factory,
                                         AdminCommunitiesView.Appearance appearance,
                                         AdminAppsGridView.Presenter hierarchyGridPresenter,
                                         AdminAppsGridView.Presenter communityGridPresenter) {
        this.serviceFacade = serviceFacade;
        this.appSearchService = appSearchService;
        this.communityTreeStore = communityTreeStore;
        this.previewTreeStore = hierarchyTreeStore;
        this.appearance = appearance;
        this.ontologyUtil = OntologyUtil.getInstance();

        this.hierarchyGridPresenter = hierarchyGridPresenter;
        this.communityGridPresenter = communityGridPresenter;

        proxy = getProxy(appSearchService);
        loader = getPagingLoader();
        this.view = factory.create(communityTreeStore,
                                   hierarchyTreeStore,
                                   loader,
                                   hierarchyGridPresenter.getView(),
                                   communityGridPresenter.getView());

        hierarchyGridPresenter.getView().addAppSelectionChangedEventHandler(view);
        communityGridPresenter.getView().addAppSelectionChangedEventHandler(view);

        proxy.setHasHandlers(view);
    }

    @Override
    public void setViewDebugId(String id) {
        view.asWidget().ensureDebugId(id + Belphegor.CommunityIds.VIEW);
    }

    PagingLoader<FilterPagingLoadConfig, PagingLoadResult<App>> getPagingLoader() {
        return new PagingLoader<>(proxy);
    }

    AppSearchRpcProxy getProxy(AppSearchFacade appService) {
        return new AppSearchRpcProxy(appService);
    }

    @Override
    public void go(HasOneWidget container) {
        getCommunities();
        container.setWidget(view);
    }

    @Override
    public AdminCommunitiesView getView() {
        return view;
    }

    boolean previewTreeHasHierarchy(OntologyHierarchy hierarchy) {
        String id = ontologyUtil.getOrCreateHierarchyPathTag(hierarchy);
        return previewTreeStore.findModelWithKey(id) != null;
    }

    void getCommunities() {
        communityGridPresenter.getView().clearAndAdd(null);
        hierarchyGridPresenter.getView().clearAndAdd(null);
        serviceFacade.getCommunities(new AsyncCallback<List<Group>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(List<Group> result) {
                if (!result.isEmpty()) {
                    communityTreeStore.add(result);
                    view.showCommunitiesPanel();
                } else {
                    view.showNoCommunitiesPanel();
                }
            }
        });
    }

    @Override
    public Group getCommunityFromElement(Element el) {
        return view.getCommunityFromElement(el);
    }

    @Override
    public Group getSelectedCommunity() {
        return view.getSelectedCommunity();
    }

}
