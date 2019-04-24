package org.iplantc.de.apps.client.presenter.hierarchies;

import static com.google.common.base.Preconditions.checkNotNull;

import org.iplantc.de.apps.client.AppDetailsView;
import org.iplantc.de.apps.client.OntologyHierarchiesView;
import org.iplantc.de.apps.client.events.AppFavoritedEvent;
import org.iplantc.de.apps.client.events.AppSearchResultLoadEvent;
import org.iplantc.de.apps.client.events.AppUpdatedEvent;
import org.iplantc.de.apps.client.events.SelectedHierarchyNotFound;
import org.iplantc.de.apps.client.events.selection.AppFavoriteSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppInfoSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppRatingDeselected;
import org.iplantc.de.apps.client.events.selection.AppRatingSelected;
import org.iplantc.de.apps.client.events.selection.DetailsCategoryClicked;
import org.iplantc.de.apps.client.events.selection.DetailsHierarchyClicked;
import org.iplantc.de.apps.client.events.selection.OntologyHierarchySelectionChangedEvent;
import org.iplantc.de.apps.client.gin.AppCategoryTreeStoreProvider;
import org.iplantc.de.apps.client.gin.OntologyHierarchyTreeStoreProvider;
import org.iplantc.de.apps.client.gin.factory.OntologyHierarchiesViewFactory;
import org.iplantc.de.apps.client.presenter.callbacks.DeleteRatingCallback;
import org.iplantc.de.apps.client.presenter.callbacks.ParentFilteredHierarchyCallback;
import org.iplantc.de.apps.client.presenter.callbacks.RateAppCallback;
import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppCategory;
import org.iplantc.de.client.models.ontologies.OntologyHierarchy;
import org.iplantc.de.client.services.AppUserServiceFacade;
import org.iplantc.de.client.services.OntologyServiceFacade;
import org.iplantc.de.client.util.OntologyUtil;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.widgets.DETabPanel;
import org.iplantc.de.shared.AppsCallback;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.common.collect.Lists;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.tree.Tree;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author aramsey
 */
public class OntologyHierarchiesPresenterImpl implements OntologyHierarchiesView.Presenter,
                                                         OntologyHierarchySelectionChangedEvent.OntologyHierarchySelectionChangedEventHandler,
                                                         AppRatingSelected.AppRatingSelectedHandler,
                                                         AppRatingDeselected.AppRatingDeselectedHandler,
                                                         AppFavoriteSelectedEvent.AppFavoriteSelectedEventHandler,
                                                         DetailsHierarchyClicked.DetailsHierarchyClickedHandler,
                                                         DetailsCategoryClicked.DetailsCategoryClickedHandler {

    class AppDetailsCallback extends AppsCallback<App> {

        private App app;

        public AppDetailsCallback(App app) {
            this.app = app;
        }

        @Override
        public void onFailure(Integer statusCode, final Throwable caught) {
            announcer.schedule(new ErrorAnnouncementConfig(appearance.fetchAppDetailsError(caught)));
        }

        @Override
        public void onSuccess(final App appDetails) {
            presenterProvider.get(new AsyncCallback<AppDetailsView.Presenter>() {
                @Override
                public void onFailure(Throwable caught) {
                    ErrorHandler.post(caught);
                }

                @Override
                public void onSuccess(final AppDetailsView.Presenter result) {
                    result.go(app, searchRegexPattern);
                }
            });


        }


    }

    public class FilteredHierarchyCallback extends AppsCallback<OntologyHierarchy> {
        private final Tree<OntologyHierarchy, String> tree;
        private final OntologyHierarchy root;
        private ParentFilteredHierarchyCallback parent;

        public FilteredHierarchyCallback(Tree<OntologyHierarchy, String> tree,
                                         OntologyHierarchy root) {
            this.tree = tree;
            this.root = root;
        }

        @Override
        public void onFailure(Integer statusCode, Throwable caught) {
            ErrorHandler.post(caught);
            tree.unmask();
            parent.done();
        }

        @Override
        public void onSuccess(OntologyHierarchy result) {
            if (result == null || result.getSubclasses() == null) {
                result = root;
                result.setSubclasses(Lists.<OntologyHierarchy>newArrayList());
            }
            OntologyHierarchy unclassifiedChild = ontologyUtil.addUnclassifiedChild(result);
            unclassifiedHierarchies.add(unclassifiedChild);
            //Set the key for the current root (which won't appear in the tree, but will be the name of the tab)
            // which will allow the children to know the full path from its parent to node
            ontologyUtil.getOrCreateHierarchyPathTag(result);
            addHierarchies(tree.getStore(), null, result.getSubclasses());

            tree.unmask();
            parent.done();
        }

        public void setParent(ParentFilteredHierarchyCallback parent) {
            this.parent = parent;
        }
    }

    @Inject IplantAnnouncer announcer;
    OntologyUtil ontologyUtil;
    @Inject
    AsyncProviderWrapper<AppDetailsView.Presenter> presenterProvider;
    @Inject AppUserServiceFacade appUserService;
    DETabPanel viewTabPanel;
    private OntologyServiceFacade serviceFacade;
    private OntologyHierarchiesView.OntologyHierarchiesAppearance appearance;
    protected String searchRegexPattern;
    private final EventBus eventBus;
    HandlerManager handlerManager;
    Map<String, List<OntologyHierarchy>> iriToHierarchyMap = new FastMap<>();
    private OntologyHierarchiesViewFactory viewFactory;
    String baseID;
    List<OntologyHierarchy> unclassifiedHierarchies;
    List<OntologyHierarchiesView> views;
    boolean desiredHierarchyFound = false;
    Logger LOG = Logger.getLogger("OntologyHierarchiesPresenterImpl");

    @Inject
    public OntologyHierarchiesPresenterImpl(OntologyHierarchiesViewFactory viewFactory,
                                            OntologyServiceFacade serviceFacade,
                                            EventBus eventBus,
                                            OntologyHierarchiesView.OntologyHierarchiesAppearance appearance) {

        this.serviceFacade = serviceFacade;
        this.appearance = appearance;
        this.viewFactory = viewFactory;
        this.eventBus = eventBus;

        this.ontologyUtil = OntologyUtil.getInstance();
    }

    @Override
    public void go(final OntologyHierarchy selectedHierarchy, final DETabPanel tabPanel) {
        desiredHierarchyFound = false;
        unclassifiedHierarchies = Lists.newArrayList();
        views = Lists.newArrayList();
        viewTabPanel = tabPanel;
        serviceFacade.getRootHierarchies(new AppsCallback<List<OntologyHierarchy>>() {
            @Override
            public void onFailure(Integer statusCode, Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(List<OntologyHierarchy> result) {
                if (result != null && result.size() > 0) {
                    boolean isValid = ontologyUtil.createIriToAttrMap(result);
                    if (!isValid) {
                        announcer.schedule(new ErrorAnnouncementConfig(appearance.ontologyAttrMatchingFailure()));
                        LOG.log(Level.SEVERE, "ERROR UI's ontology configs do not exist or do not match published ontology!");
                    } else {
                        createViewTabs(selectedHierarchy, result);
                    }
                }
            }
        });
    }

    @Override
    public void setViewDebugId(String baseID) {
        this.baseID = baseID;
    }

    @Override
    public OntologyHierarchy getSelectedHierarchy() {
        OntologyHierarchy hierarchy;
        for (OntologyHierarchiesView view : views) {
            if (view.getTree() != null) {
                hierarchy = view.getTree().getSelectionModel().getSelectedItem();
                if (hierarchy != null) {
                    return hierarchy;
                }
            }
        }
        return null;
    }

    @Override
    public void onAppInfoSelected(final AppInfoSelectedEvent event) {
        App app = event.getApp();
        appUserService.getAppDetails(app, new AppDetailsCallback(app));
    }

    void createViewTabs(final OntologyHierarchy selectedHierarchy, List<OntologyHierarchy> results) {
        for (OntologyHierarchy hierarchy : results) {
            TreeStore<OntologyHierarchy> treeStore = getHierarchyTreeStore();
            OntologyHierarchiesView view = viewFactory.create(treeStore);
            Tree<OntologyHierarchy, String> tree = view.getTree();
            view.setRoot(hierarchy);
            views.add(view);

            String hierarchyDebugId = baseID + "." + hierarchy.getIri();
            view.asWidget().ensureDebugId(hierarchyDebugId);
            view.addOntologyHierarchySelectionChangedEventHandler(this);
            //As a preference, insert the hierarchy tabs before the HPC tab which is last
            viewTabPanel.insert(tree, viewTabPanel.getWidgetCount() - 1, new TabItemConfig(appearance.hierarchyLabelName(hierarchy)), hierarchyDebugId);
        }

        populateViewTabs(selectedHierarchy);
    }

    void populateViewTabs(final OntologyHierarchy selectedHierarchy) {
        //Create all the callbacks I'll need
        List<FilteredHierarchyCallback> childCallbacks = createFilteredHierarchyList();
        for (OntologyHierarchiesView view: views) {
            FilteredHierarchyCallback callback =
                    new FilteredHierarchyCallback(view.getTree(), view.getRoot());
            childCallbacks.add(callback);

            view.getTree().mask(appearance.getAppCategoriesLoadingMask());
            serviceFacade.getFilteredHierarchies(view.getRoot().getIri(),
                                                 ontologyUtil.convertHierarchyToAvu(view.getRoot()),
                                                 callback);
        }

        //Create a parent callback that will run handleSuccess when all the callbacks complete
        new ParentFilteredHierarchyCallback(childCallbacks) {
            @Override
            public void handleSuccess() {
                for (OntologyHierarchiesView view : views) {
                    selectDesiredHierarchy(view.getTree(), selectedHierarchy);
                }
                if (selectedHierarchy != null && !desiredHierarchyFound) {
                    fireEvent(new SelectedHierarchyNotFound());
                }
            }
        };
    }

    @Override
    public void onAppSearchResultLoad(AppSearchResultLoadEvent event) {
        searchRegexPattern = event.getSearchPattern();
        for (Widget widget : viewTabPanel) {
            if (widget instanceof Tree) {
                ((Tree)widget).getSelectionModel().deselectAll();
            }
        }
    }

    TreeStore<OntologyHierarchy> getHierarchyTreeStore() {
        TreeStore<OntologyHierarchy> treeStore = new OntologyHierarchyTreeStoreProvider().get();
        treeStore.addSortInfo(new Store.StoreSortInfo<>(ontologyUtil.getOntologyNameComparator(),
                                                        SortDir.ASC));
        return treeStore;
    }

    TreeStore<AppCategory> getCategoryTreeStore() {
        return new AppCategoryTreeStoreProvider().get();
    }

    void selectDesiredHierarchy(Tree<OntologyHierarchy, String> tree, OntologyHierarchy selectedHierarchy) {
        if (selectedHierarchy == null || desiredHierarchyFound) {
            return;
        }
        desiredHierarchyFound = doSelectHierarchy(tree, selectedHierarchy);
    }

    boolean doSelectHierarchy(Tree<OntologyHierarchy, String> tree,
                              OntologyHierarchy selectedHierarchy) {
        Tree.TreeNode<OntologyHierarchy> node = tree.findNode(selectedHierarchy);
        if (node != null) {
            viewTabPanel.setActiveWidget(tree);
            tree.getSelectionModel().select(node.getModel(), true);
            return true;
        }
        return false;
    }

    void addHierarchies(TreeStore<OntologyHierarchy> treeStore, OntologyHierarchy parent, List<OntologyHierarchy> children) {
        if ((children == null)
            || children.isEmpty()) {
            return;
        }
        if (parent == null) {
            treeStore.add(children);

        } else {
            treeStore.add(parent, children);
        }

        helperMap(children);

        for (OntologyHierarchy hierarchy : children) {
            addHierarchies(treeStore, hierarchy, hierarchy.getSubclasses());
        }
    }

    void helperMap(List<OntologyHierarchy> children) {
        for (OntologyHierarchy hierarchy : children) {
            String iri = hierarchy.getIri();
            List<OntologyHierarchy> hierarchies = iriToHierarchyMap.get(iri);
            if (hierarchies == null) {
                hierarchies = Lists.newArrayList();
            }
            hierarchies.add(hierarchy);
            iriToHierarchyMap.put(hierarchy.getIri(), hierarchies);
        }
    }

    @Override
    public void onOntologyHierarchySelectionChanged(OntologyHierarchySelectionChangedEvent event) {
        fireEvent(event);
    }

    @Override
    public void onAppRatingDeselected(AppRatingDeselected event) {
        final App appToUnRate = event.getApp();
        appUserService.deleteRating(appToUnRate, new DeleteRatingCallback(appToUnRate,
                                                                          eventBus));
    }

    @Override
    public void onAppRatingSelected(AppRatingSelected event) {
        final App appToRate = event.getApp();
        appUserService.rateApp(appToRate,
                               event.getScore(),
                               new RateAppCallback(appToRate,
                                                   eventBus));
    }

    @Override
    public void onAppFavoriteSelected(AppFavoriteSelectedEvent event) {
        final App app = event.getApp();
        appUserService.favoriteApp(app, !app.isFavorite(), new AppsCallback<Void>() {
            @Override
            public void onFailure(Integer statusCode, Throwable caught) {
                announcer.schedule(new ErrorAnnouncementConfig(appearance.favServiceFailure()));
            }

            @Override
            public void onSuccess(Void result) {
                app.setFavorite(!app.isFavorite());
                // Have to fire global events.
                eventBus.fireEvent(new AppFavoritedEvent(app));
                eventBus.fireEvent(new AppUpdatedEvent(app));
            }
        });
    }

    @Override
    public void onDetailsHierarchyClicked(DetailsHierarchyClicked event) {
        OntologyHierarchy hierarchy = event.getHierarchy();
        checkNotNull(hierarchy);

        String id = ontologyUtil.getOrCreateHierarchyPathTag(hierarchy);
        for (int i = 0; i < viewTabPanel.getWidgetCount(); i++) {
            Tree<OntologyHierarchy, String> tree = ((Tree)viewTabPanel.getWidget(i));
            OntologyHierarchy selectedHierarchy = tree.getStore().findModelWithKey(id);
            if (selectedHierarchy != null) {
                viewTabPanel.setActiveWidget(tree);
                tree.scrollIntoView(selectedHierarchy);
                tree.getSelectionModel().select(selectedHierarchy, true);
                break;
            }
        }
    }

    @Override
    public void onDetailsCategoryClicked(DetailsCategoryClicked event) {
        AppCategory category = event.getCategory();
        checkNotNull(category);

        for (int i = 0 ; i < viewTabPanel.getWidgetCount() ; i++) {
            Tree<AppCategory, String> tree = ((Tree)viewTabPanel.getWidget(i));
            AppCategory selectedCategory = tree.getStore().findModelWithKey(category.getId());
            if (selectedCategory != null) {
                viewTabPanel.setActiveWidget(tree);
                tree.scrollIntoView(selectedCategory);
                tree.getSelectionModel().select(selectedCategory, true);
                break;
            }
        }
    }

    @Override
    public HandlerRegistration addOntologyHierarchySelectionChangedEventHandler(
            OntologyHierarchySelectionChangedEvent.OntologyHierarchySelectionChangedEventHandler handler) {
        return ensureHandlers().addHandler(OntologyHierarchySelectionChangedEvent.TYPE, handler);
    }

    @Override
    public HandlerRegistration addSelectedHierarchyNotFoundHandler(SelectedHierarchyNotFound.SelectedHierarchyNotFoundHandler handler) {
        return ensureHandlers().addHandler(SelectedHierarchyNotFound.TYPE, handler);
    }

    HandlerManager createHandlerManager() {
        return new HandlerManager(this);
    }

    HandlerManager ensureHandlers() {
        return handlerManager == null ? handlerManager = createHandlerManager() : handlerManager;
    }

    void fireEvent(GwtEvent<?> event) {
        if (handlerManager != null) {
            handlerManager.fireEvent(event);
        }
    }

    List<FilteredHierarchyCallback> createFilteredHierarchyList() {
        return Lists.newArrayList();
    }
}
