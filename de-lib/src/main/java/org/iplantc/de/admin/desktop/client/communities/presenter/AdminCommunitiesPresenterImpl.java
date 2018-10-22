package org.iplantc.de.admin.desktop.client.communities.presenter;

import org.iplantc.de.admin.apps.client.AdminAppsGridView;
import org.iplantc.de.admin.desktop.client.communities.AdminCommunitiesView;
import org.iplantc.de.admin.desktop.client.communities.events.AddCommunityClicked;
import org.iplantc.de.admin.desktop.client.communities.events.CommunitySelectionChanged;
import org.iplantc.de.admin.desktop.client.communities.events.EditCommunityClicked;
import org.iplantc.de.admin.desktop.client.communities.gin.AdminCommunitiesViewFactory;
import org.iplantc.de.admin.desktop.client.communities.service.AdminCommunityServiceFacade;
import org.iplantc.de.admin.desktop.client.communities.views.AppToCommunityDND;
import org.iplantc.de.admin.desktop.client.communities.views.CommunityToAppDND;
import org.iplantc.de.admin.desktop.client.communities.views.EditCommunityDialog;
import org.iplantc.de.admin.desktop.client.ontologies.events.HierarchySelectedEvent;
import org.iplantc.de.admin.desktop.client.ontologies.service.OntologyServiceFacade;
import org.iplantc.de.admin.desktop.client.services.AppAdminServiceFacade;
import org.iplantc.de.admin.desktop.shared.Belphegor;
import org.iplantc.de.apps.client.events.AppSearchResultLoadEvent;
import org.iplantc.de.apps.client.presenter.toolBar.proxy.AppSearchRpcProxy;
import org.iplantc.de.client.DEClientConstants;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.avu.Avu;
import org.iplantc.de.client.models.avu.AvuAutoBeanFactory;
import org.iplantc.de.client.models.avu.AvuList;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.GroupAutoBeanFactory;
import org.iplantc.de.client.models.groups.PrivilegeType;
import org.iplantc.de.client.models.ontologies.Ontology;
import org.iplantc.de.client.models.ontologies.OntologyHierarchy;
import org.iplantc.de.client.services.AppSearchFacade;
import org.iplantc.de.client.services.AppServiceFacade;
import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.client.util.JsonUtil;
import org.iplantc.de.client.util.OntologyUtil;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;
import org.iplantc.de.shared.AppsCallback;
import org.iplantc.de.shared.AsyncProviderWrapper;
import org.iplantc.de.shared.DEProperties;

import com.google.common.collect.Lists;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;

import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;

import java.util.List;

/**
 * @author aramsey
 */
public class AdminCommunitiesPresenterImpl implements AdminCommunitiesView.Presenter,
                                                      AppSearchResultLoadEvent.AppSearchResultLoadEventHandler {

    @Inject AppAdminServiceFacade adminAppService;
    @Inject DEClientConstants constants;
    @Inject DEProperties properties;
    @Inject IplantAnnouncer announcer;
    @Inject JsonUtil jsonUtil;
    @Inject AppServiceFacade appService;
    private OntologyServiceFacade ontologyServiceFacade;
    private GroupAutoBeanFactory groupFactory;
    private AvuAutoBeanFactory avuAutoBeanFactory;
    @Inject AppSearchFacade appSearchService;
    @Inject AsyncProviderWrapper<EditCommunityDialog> editCommunityDlgProvider;
    OntologyUtil ontologyUtil;
    AppSearchRpcProxy proxy;
    PagingLoader<FilterPagingLoadConfig, PagingLoadResult<App>> loader;
    private AdminCommunitiesView view;
    private AdminCommunityServiceFacade serviceFacade;
    private GroupServiceFacade groupServiceFacade;
    private final TreeStore<Group> communityTreeStore;
    private final TreeStore<OntologyHierarchy> hierarchyTreeStore;
    private AdminCommunitiesView.Appearance appearance;
    private AdminAppsGridView.Presenter hierarchyGridPresenter;
    private AdminAppsGridView.Presenter communityGridPresenter;
    String activeOntologyVersion;

    @Inject
    public AdminCommunitiesPresenterImpl(AdminCommunityServiceFacade serviceFacade,
                                         OntologyServiceFacade ontologyServiceFacade,
                                         GroupAutoBeanFactory groupFactory,
                                         AvuAutoBeanFactory avuAutoBeanFactory,
                                         AppSearchFacade appSearchService,
                                         GroupServiceFacade groupServiceFacade,
                                         final TreeStore<Group> communityTreeStore,
                                         final TreeStore<OntologyHierarchy> hierarchyTreeStore,
                                         AdminCommunitiesViewFactory factory,
                                         AdminCommunitiesView.Appearance appearance,
                                         AdminAppsGridView.Presenter hierarchyGridPresenter,
                                         AdminAppsGridView.Presenter communityGridPresenter) {
        this.serviceFacade = serviceFacade;
        this.ontologyServiceFacade = ontologyServiceFacade;
        this.groupFactory = groupFactory;
        this.avuAutoBeanFactory = avuAutoBeanFactory;
        this.appSearchService = appSearchService;
        this.groupServiceFacade = groupServiceFacade;
        this.communityTreeStore = communityTreeStore;
        this.hierarchyTreeStore = hierarchyTreeStore;
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
                                   communityGridPresenter.getView(),
                                   new CommunityToAppDND(appearance,
                                                         hierarchyGridPresenter,
                                                         communityGridPresenter,
                                                         this),
                                   new AppToCommunityDND(appearance,
                                                         hierarchyGridPresenter,
                                                         communityGridPresenter,
                                                         this));

        hierarchyGridPresenter.getView().addAppSelectionChangedEventHandler(view);
        communityGridPresenter.getView().addAppSelectionChangedEventHandler(view);

        proxy.setHasHandlers(view);

        view.addCommunitySelectionChangedHandler(this);
        view.addHierarchySelectedEventHandler(this);
        view.addHierarchySelectedEventHandler(hierarchyGridPresenter.getView());
        view.addAppSearchResultLoadEventHandler(hierarchyGridPresenter);
        view.addAppSearchResultLoadEventHandler(hierarchyGridPresenter.getView());
        view.addBeforeAppSearchEventHandler(hierarchyGridPresenter.getView());
        view.addAddCommunityClickedHandler(this);
        view.addEditCommunityClickedHandler(this);
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
        getHierarchies();
        container.setWidget(view);
    }

    @Override
    public AdminCommunitiesView getView() {
        return view;
    }

    @Override
    public void appsDNDtoCommunity(List<App> apps, Group community) {
        if (apps != null && apps.size() > 0) {
            for (App app: apps) {
                communityDNDtoApp(community, app);
            }
        }
    }

    @Override
    public void communityDNDtoApp(Group community, App targetApp) {
        AvuList avuList = getCommunityAvu(community);
        ontologyServiceFacade.addAVUsToApp(targetApp, avuList, new AsyncCallback<List<Avu>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(List<Avu> result) {
                announcer.schedule(new SuccessAnnouncementConfig(appearance.appAddedToCommunity(targetApp.getName(), community.getName())));
                view.selectCommunity(community);
            }
        });
    }

    AvuList getCommunityAvu(Group community) {
        AvuList avuList = avuAutoBeanFactory.getAvuList().as();
        Avu avu = avuAutoBeanFactory.getAvu().as();
        avu.setAttribute(properties.getCommunityAttr());
        avu.setValue(community.getDisplayName());
        avu.setUnit("");

        avuList.setAvus(Lists.newArrayList(avu));

        return avuList;
    }

    boolean previewTreeHasHierarchy(OntologyHierarchy hierarchy) {
        String id = ontologyUtil.getOrCreateHierarchyPathTag(hierarchy);
        return hierarchyTreeStore.findModelWithKey(id) != null;
    }

    void getHierarchies() {
        ontologyServiceFacade.getOntologies(new AsyncCallback<List<Ontology>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(List<Ontology> result) {
                if (result != null) {
                    Ontology activeOntology = result.stream().filter(Ontology::isActive).findFirst().get();
                    activeOntologyVersion = activeOntology.getVersion();
                    ontologyServiceFacade.getOntologyHierarchies(activeOntologyVersion, new AppsCallback<List<OntologyHierarchy>>() {
                        @Override
                        public void onFailure(Integer statusCode, Throwable exception) {
                            ErrorHandler.post(exception);
                        }

                        @Override
                        public void onSuccess(List<OntologyHierarchy> result) {
                            addHierarchies(null, result);
                        }
                    });
                }
            }
        });
    }

    void addHierarchies(OntologyHierarchy parent,
                        List<OntologyHierarchy> children) {
        if ((children == null)
            || children.isEmpty()) {
            return;
        }
        if (parent == null) {
            ontologyUtil.addUnclassifiedChild(children);
            hierarchyTreeStore.add(children);
        } else {
            hierarchyTreeStore.add(parent, children);
        }

        for (OntologyHierarchy hierarchy : children) {
            addHierarchies(hierarchy, hierarchy.getSubclasses());
        }
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

    @Override
    public void onCommunitySelectionChanged(CommunitySelectionChanged event) {
        Group community = event.getCommunity();
        AdminAppsGridView communityApps = communityGridPresenter.getView();
        communityApps.mask(appearance.loadingMask());

        serviceFacade.getCommunityApps(community, new AppsCallback<List<App>>() {

            @Override
            public void onFailure(Integer statusCode, Throwable exception) {
                communityApps.unmask();
                ErrorHandler.post(exception);
            }

            @Override
            public void onSuccess(List<App> result) {
                communityApps.clearAndAdd(result);
                communityApps.unmask();
            }
        });
    }

    @Override
    public void onHierarchySelected(HierarchySelectedEvent event) {
        AdminAppsGridView hierarchyView = hierarchyGridPresenter.getView();
        hierarchyView.mask(appearance.loadingMask());
        OntologyHierarchy hierarchy = event.getHierarchy();
        ontologyServiceFacade.getAppsByHierarchy(activeOntologyVersion,
                                                 hierarchy.getIri(),
                                                 ontologyUtil.getAttr(hierarchy),
                                                 new AsyncCallback<List<App>>() {
                                                     @Override
                                                     public void onFailure(Throwable caught) {
                                                         ErrorHandler.post(caught);
                                                         hierarchyView.unmask();
                                                     }

                                                     @Override
                                                     public void onSuccess(List<App> result) {
                                                        hierarchyView.clearAndAdd(result);
                                                        hierarchyView.unmask();
                                                     }
                                                 });
    }

    @Override
    public void onAppSearchResultLoad(AppSearchResultLoadEvent event) {
        view.deselectHierarchies();
    }

    @Override
    public void onAddCommunityClicked(AddCommunityClicked event) {
        editCommunityDlgProvider.get(new AsyncCallback<EditCommunityDialog>() {
            @Override
            public void onFailure(Throwable caught) {}

            @Override
            public void onSuccess(EditCommunityDialog result) {
                result.show(null);
                result.addOkButtonSelectHandler(event -> {
                    Group community = groupFactory.getGroup().as();
                    community.setName(result.getName());
                    community.setDescription(result.getDescription());

                    List<PrivilegeType> publicPrivileges = Lists.newArrayList(PrivilegeType.read);

                    groupServiceFacade.addCommunity(community, publicPrivileges, new AsyncCallback<Group>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            ErrorHandler.post(caught);
                        }

                        @Override
                        public void onSuccess(Group result) {
                            communityTreeStore.add(result);
                        }
                    });
                });
            }
        });
    }

    @Override
    public void onEditCommunityClicked(EditCommunityClicked event) {
        editCommunityDlgProvider.get(new AsyncCallback<EditCommunityDialog>() {
            @Override
            public void onFailure(Throwable caught) {}

            @Override
            public void onSuccess(EditCommunityDialog result) {
                Group community = event.getCommunity();
                result.show(community);
                result.addOkButtonSelectHandler(event -> {
                    Group updatedCommunity = groupFactory.getGroup().as();
                    updatedCommunity.setName(result.getName());
                    updatedCommunity.setDescription(result.getDescription());

                    serviceFacade.updateCommunity(result.getOriginalName(), updatedCommunity, new AsyncCallback<Group>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            ErrorHandler.post(caught);
                        }

                        @Override
                        public void onSuccess(Group result) {
                            communityTreeStore.update(result);
                            communityTreeStore.applySort(true);
                        }
                    });
                });
            }
        });
    }
}
