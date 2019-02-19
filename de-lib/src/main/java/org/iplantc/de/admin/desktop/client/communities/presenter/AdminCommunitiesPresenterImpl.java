package org.iplantc.de.admin.desktop.client.communities.presenter;

import org.iplantc.de.admin.apps.client.AdminAppsGridView;
import org.iplantc.de.admin.desktop.client.communities.AdminCommunitiesView;
import org.iplantc.de.admin.desktop.client.communities.ManageCommunitiesView;
import org.iplantc.de.admin.desktop.client.communities.events.AddCommunityClicked;
import org.iplantc.de.admin.desktop.client.communities.events.CategorizeButtonClicked;
import org.iplantc.de.admin.desktop.client.communities.events.CommunitySelectionChanged;
import org.iplantc.de.admin.desktop.client.communities.events.DeleteCommunityClicked;
import org.iplantc.de.admin.desktop.client.communities.events.EditCommunityClicked;
import org.iplantc.de.admin.desktop.client.communities.gin.AdminCommunitiesViewFactory;
import org.iplantc.de.admin.desktop.client.communities.service.AdminCommunityServiceFacade;
import org.iplantc.de.admin.desktop.client.communities.views.AppToCommunityDND;
import org.iplantc.de.admin.desktop.client.communities.views.CommunityToAppDND;
import org.iplantc.de.admin.desktop.client.communities.views.dialogs.AppCommunityListEditorDialog;
import org.iplantc.de.admin.desktop.client.communities.views.dialogs.DeleteCommunityConfirmationDialog;
import org.iplantc.de.admin.desktop.client.communities.views.dialogs.EditCommunityDialog;
import org.iplantc.de.admin.desktop.client.communities.views.dialogs.RetagAppsConfirmationDialog;
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
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.errorHandling.ServiceErrorCode;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.PrivilegeType;
import org.iplantc.de.client.models.groups.UpdateMemberResult;
import org.iplantc.de.client.models.ontologies.Ontology;
import org.iplantc.de.client.models.ontologies.OntologyHierarchy;
import org.iplantc.de.client.services.AppSearchFacade;
import org.iplantc.de.client.services.AppServiceFacade;
import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.client.util.JsonUtil;
import org.iplantc.de.client.util.OntologyUtil;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
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
import com.sencha.gxt.widget.core.client.Dialog;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private AvuAutoBeanFactory avuAutoBeanFactory;
    @Inject AppSearchFacade appSearchService;
    @Inject AsyncProviderWrapper<EditCommunityDialog> editCommunityDlgProvider;
    @Inject AsyncProviderWrapper<AppCommunityListEditorDialog> communityEditorDlgProvider;
    @Inject AsyncProviderWrapper<DeleteCommunityConfirmationDialog> deleteCommunityDlgProvider;
    @Inject AsyncProviderWrapper<RetagAppsConfirmationDialog> retagAppsConfirmationDlgProvider;
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
        view.addCommunitySelectionChangedHandler(communityGridPresenter.getView());
        view.addAppSearchResultLoadEventHandler(hierarchyGridPresenter);
        view.addAppSearchResultLoadEventHandler(hierarchyGridPresenter.getView());
        view.addBeforeAppSearchEventHandler(hierarchyGridPresenter.getView());
        view.addAddCommunityClickedHandler(this);
        view.addEditCommunityClickedHandler(this);
        view.addCategorizeButtonClickedHandler(this);
        view.addDeleteCommunityClickedHandler(this);
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
        AvuList avuList = avuAutoBeanFactory.getAvuList().as();
        avuList.setAvus(getCommunityAvuList(Lists.newArrayList(community)));
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

    List<Avu> getCommunityAvuList(List<Group> communities) {
        List<Avu> avuList = Lists.newArrayList();
        for (Group community : communities) {
            Avu avu = getCommunityAvu(community);
            avuList.add(avu);
        }
        return avuList;
    }

    Avu getCommunityAvu(Group community) {
        Avu avu = avuAutoBeanFactory.getAvu().as();
        avu.setAttribute(properties.getCommunityAttr());
        avu.setValue(community.getDisplayName());
        avu.setUnit("");

        return avu;
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

        view.maskCommunities();
        serviceFacade.getCommunities(new AsyncCallback<List<Group>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
                view.unmaskCommunities();
            }

            @Override
            public void onSuccess(List<Group> result) {
                if (!result.isEmpty()) {
                    communityTreeStore.add(result);
                    view.showCommunitiesPanel();
                } else {
                    view.showNoCommunitiesPanel();
                }
                view.unmaskCommunities();
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
                result.show(null, ManageCommunitiesView.MODE.CREATE);
                result.addOkButtonSelectHandler(event -> {
                    Group community = result.getUpdatedCommunity();
                    List<Subject> admins = result.getSelectedAdmins();

                    addCommunityWithAdmins(community, admins);
                });
            }
        });
    }

    void addCommunityWithAdmins(Group community, List<Subject> admins) {
        view.maskCommunities();

        groupServiceFacade.addCommunity(community, new AsyncCallback<Group>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
                view.unmaskCommunities();
            }

            @Override
            public void onSuccess(Group result) {
                communityTreeStore.add(result);
                if (admins != null) {
                    addCommunityAdmins(result, admins);
                } else {
                    view.unmaskCommunities();
                }
            }
        });
    }

    void addCommunityAdmins(Group community, List<Subject> admins) {
        serviceFacade.addCommunityAdmins(community, admins, new AsyncCallback<List<UpdateMemberResult>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
                view.unmaskCommunities();
            }

            @Override
            public void onSuccess(List<UpdateMemberResult> result) {
                result.forEach(updateMemberResult -> {
                    if (!updateMemberResult.isSuccess()) {
                        announcer.schedule(new ErrorAnnouncementConfig(appearance.failedToAddCommunityAdmin(updateMemberResult.getSubjectName(), community)));
                    }
                });
                view.unmaskCommunities();
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
                String originalCommunityName = community.getName();
                result.show(community, ManageCommunitiesView.MODE.EDIT);
                result.addOkButtonSelectHandler(event -> {
                    Group updatedCommunity = result.getUpdatedCommunity();
                    updateCommunity(originalCommunityName, updatedCommunity, false, result);
                });
                result.addCancelButtonSelectHandler(event -> {
                    // EditorDriver will have updated the community name, revert it back
                    Group updatedGroup = communityTreeStore.findModelWithKey(community.getId());
                    updatedGroup.setName(originalCommunityName);
                    result.hide();
                });
            }
        });
    }

    void updateCommunity(String originalCommunity, Group updatedCommunity, boolean retagApps, IPlantDialog dlg) {
        dlg.mask(appearance.loadingMask());
        serviceFacade.updateCommunity(originalCommunity, updatedCommunity, retagApps, new AsyncCallback<Group>() {
            @Override
            public void onFailure(Throwable caught) {
                if (ServiceErrorCode.ERR_EXISTS.toString().equals(ErrorHandler.getServiceError(caught))) {
                    confirmReTagApps(originalCommunity, updatedCommunity, dlg);
                } else {
                    ErrorHandler.post(caught);
                    dlg.unmask();
                }
            }

            @Override
            public void onSuccess(Group result) {
                communityTreeStore.update(result);
                communityTreeStore.applySort(false);
                view.selectCommunity(result);
                dlg.hide();
            }
        });
    }

    void confirmReTagApps(String originalCommunity, Group updatedCommunity, IPlantDialog dlg) {
        retagAppsConfirmationDlgProvider.get(new AsyncCallback<RetagAppsConfirmationDialog>() {
            @Override
            public void onFailure(Throwable caught) {}

            @Override
            public void onSuccess(RetagAppsConfirmationDialog result) {
                result.show(originalCommunity);
                result.addDialogHideHandler(event -> {
                    if (Dialog.PredefinedButton.YES.equals(event.getHideButton())) {
                        updateCommunity(originalCommunity, updatedCommunity, true, dlg);
                    } else {
                        dlg.unmask();
                    }
                });
            }
        });
    }

    @Override
    public void onCategorizeButtonClicked(CategorizeButtonClicked event) {
        App selectedApp = event.getTargetApp();
        ontologyServiceFacade.getAppAVUs(selectedApp, new AsyncCallback<List<Avu>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(List<Avu> appAvus) {
                communityEditorDlgProvider.get(new AsyncCallback<AppCommunityListEditorDialog>() {
                    @Override
                    public void onFailure(Throwable caught) {}

                    @Override
                    public void onSuccess(AppCommunityListEditorDialog result) {
                        Map<Boolean, List<Avu>> isCommunityAvu = getCommunityAvuMap(appAvus);
                        result.show(selectedApp,
                                    communityTreeStore.getAll(),
                                    isCommunityAvu.get(true));
                        result.addOkButtonSelectHandler(event -> {
                            setCommunityAvus(selectedApp, result.getSelectedCommunities(), isCommunityAvu.get(false));
                        });
                    }
                });
            }
        });
    }

    void setCommunityAvus(App app, List<Group> selectedCommunities, List<Avu> nonCommunityAvus) {
        List<Avu> listAvus = getCommunityAvuList(selectedCommunities);
        listAvus.addAll(nonCommunityAvus);
        AvuList avuList = avuAutoBeanFactory.getAvuList().as();
        avuList.setAvus(listAvus);

        ontologyServiceFacade.setAppAVUs(app, avuList, new AsyncCallback<List<Avu>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(List<Avu> result) {
                announcer.schedule(new SuccessAnnouncementConfig(appearance.communityAvusSet(app, selectedCommunities)));
            }
        });
    }

    Map<Boolean, List<Avu>> getCommunityAvuMap(List<Avu> avus) {
        return avus.stream().collect(Collectors.partitioningBy(avu -> properties.getCommunityAttr().equals(avu.getAttribute())));
    }

    @Override
    public void onDeleteCommunityClicked(DeleteCommunityClicked event) {
        Group community = event.getSelectedCommunity();

        deleteCommunityDlgProvider.get(new AsyncCallback<DeleteCommunityConfirmationDialog>() {
            @Override
            public void onFailure(Throwable caught) {}

            @Override
            public void onSuccess(DeleteCommunityConfirmationDialog result) {
                result.show(community);
                result.addDialogHideHandler(event -> {
                    if (Dialog.PredefinedButton.YES == event.getHideButton()) {
                        deleteCommunity(community);
                    }
                });
            }
        });
    }

    void deleteCommunity(Group community) {
        view.maskCommunities();
        serviceFacade.deleteCommunity(community, new AsyncCallback<Group>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
                view.unmaskCommunities();
            }

            @Override
            public void onSuccess(Group result) {
                announcer.schedule(new SuccessAnnouncementConfig(appearance.communityDeleted(community)));
                communityTreeStore.remove(community);
                view.unmaskCommunities();
            }
        });
    }
}
