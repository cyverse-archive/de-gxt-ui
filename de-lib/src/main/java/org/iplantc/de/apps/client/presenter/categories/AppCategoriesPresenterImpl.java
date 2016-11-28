package org.iplantc.de.apps.client.presenter.categories;

import org.iplantc.de.apps.client.AppCategoriesView;
import org.iplantc.de.apps.client.events.AppSavedEvent;
import org.iplantc.de.apps.client.events.AppSearchResultLoadEvent;
import org.iplantc.de.apps.client.events.AppUpdatedEvent;
import org.iplantc.de.apps.client.events.EditAppEvent;
import org.iplantc.de.apps.client.events.EditWorkflowEvent;
import org.iplantc.de.apps.client.events.SelectedHierarchyNotFound;
import org.iplantc.de.apps.client.events.selection.AppCategorySelectionChangedEvent;
import org.iplantc.de.apps.client.events.selection.CopyAppSelected;
import org.iplantc.de.apps.client.events.selection.CopyWorkflowSelected;
import org.iplantc.de.apps.client.gin.AppCategoryTreeStoreProvider;
import org.iplantc.de.apps.client.gin.factory.AppCategoriesViewFactory;
import org.iplantc.de.apps.client.views.details.dialogs.AppDetailsDialog;
import org.iplantc.de.apps.shared.AppsModule;
import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.HasId;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppCategory;
import org.iplantc.de.client.models.apps.integration.AppTemplate;
import org.iplantc.de.client.services.AppServiceFacade;
import org.iplantc.de.client.services.AppUserServiceFacade;
import org.iplantc.de.client.util.JsonUtil;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;
import org.iplantc.de.commons.client.widgets.DETabPanel;
import org.iplantc.de.shared.AppsCallback;
import org.iplantc.de.shared.AsyncProviderWrapper;
import org.iplantc.de.shared.DEProperties;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.tree.Tree;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author jstroot
 */
public class AppCategoriesPresenterImpl implements AppCategoriesView.Presenter,
                                                   AppCategoriesView.AppCategoryHierarchyProvider,
                                                   AppUpdatedEvent.AppUpdatedEventHandler,
                                                   AppSavedEvent.AppSavedEventHandler,
                                                   AppCategorySelectionChangedEvent.AppCategorySelectionChangedEventHandler {

    private static class AppCategoryComparator implements Comparator<AppCategory> {

        private final TreeStore<AppCategory> treeStore;

        public AppCategoryComparator(final TreeStore<AppCategory> treeStore) {
            this.treeStore = treeStore;
        }

        @Override
        public int compare(AppCategory group1, AppCategory group2) {
            if (treeStore.getRootItems().contains(group1)
                    || treeStore.getRootItems().contains(group2)) {
                // Do not sort Root groups, since we want to keep the service's root order.
                return 0;
            }

            return group1.getName().compareToIgnoreCase(group2.getName());
        }
    }

    protected static String FAVORITES;
    protected static String HPC_ID;
    protected static String USER_APPS_GROUP;
    protected static String WORKSPACE;
    protected static String PATH_KEY = "category_path";
    protected String searchRegexPattern;
    @Inject IplantAnnouncer announcer;
    @Inject AsyncProviderWrapper<AppDetailsDialog> appDetailsDlgAsyncProvider;
    @Inject AppServiceFacade appService;
    @Inject AppUserServiceFacade appUserService;
    @Inject AppCategoriesView.AppCategoriesAppearance appearance;
    private final EventBus eventBus;
    List<AppCategory> workspaceCategories = Lists.newArrayList();
    List<AppCategory> hpcCategories = Lists.newArrayList();
    List<Tree<AppCategory, String>> trees = Lists.newArrayList();
    DETabPanel viewTabPanel;
    AppCategoriesViewFactory viewFactory;
    HandlerManager handlerManager;
    AppCategoriesView workspaceView;
    AppCategoriesView hpcView;
    String baseId;

    @Inject
    AppCategoriesPresenterImpl(final DEProperties props,
                               final JsonUtil jsonUtil,
                               final EventBus eventBus,
                               final AppCategoriesViewFactory viewFactory) {
        this.eventBus = eventBus;
        this.viewFactory = viewFactory;
        this.workspaceView = viewFactory.create(new AppCategoryTreeStoreProvider().get(), this);
        this.hpcView = viewFactory.create(new AppCategoryTreeStoreProvider().get(), this);
        this.trees.add(workspaceView.getTree());
        this.trees.add(hpcView.getTree());

        workspaceView.addAppCategorySelectedEventHandler(this);
        hpcView.addAppCategorySelectedEventHandler(this);

        initConstants(props, jsonUtil);

        eventBus.addHandler(AppUpdatedEvent.TYPE, this);
        eventBus.addHandler(AppSavedEvent.TYPE, this);
    }

    @Override
    public List<String> getGroupHierarchy(AppCategory appCategory) {
        List<String> groupNames;

        final AutoBean<AppCategory> categoryAutoBean = AutoBeanUtils.getAutoBean(appCategory);
        String path = categoryAutoBean.getTag(PATH_KEY);
        groupNames = Arrays.asList(path.split("/"));

        return groupNames;
    }

    @Override
    public AppCategory getSelectedAppCategory() {
        AppCategory selectedCategory;
        for (Tree<AppCategory, String> tree : trees) {
            if (tree != null) {
                selectedCategory = tree.getSelectionModel().getSelectedItem();
                if (selectedCategory != null) {
                    return selectedCategory;
                }
            }
        }
        return null;
    }

    @Override
    public AppCategoriesView getWorkspaceView() {
        return workspaceView;
    }

    @Override
    public void go(final HasId selectedAppCategory, final boolean selectDefaultCategory, final DETabPanel tabPanel) {
        this.viewTabPanel = tabPanel;

        viewTabPanel.add(workspaceView.getTree(), new TabItemConfig(appearance.workspaceTab()), baseId + AppsModule.Ids.WORKSPACE_TAB);
        viewTabPanel.add(hpcView.getTree(), new TabItemConfig(appearance.hpcTab()), baseId + AppsModule.Ids.HPC_TAB);

        workspaceView.getTree().mask(appearance.getAppCategoriesLoadingMask());
        hpcView.getTree().mask(appearance.getAppCategoriesLoadingMask());

        appService.getAppCategories(true, new AppsCallback<List<AppCategory>>() {
            @Override
            public void onFailure(Integer statusCode, Throwable caught) {
                ErrorHandler.post(caught);
                viewTabPanel.unmask();
            }

            @Override
            public void onSuccess(List<AppCategory> result) {
                filterCategories(result);
                addCategoriesToWorkspaceTree();
                addCategoriesToHPCTree();
                selectDesiredCategory(selectedAppCategory, selectDefaultCategory);
                addHPCTabSelectionHandler();
                workspaceView.getTree().unmask();
                hpcView.getTree().unmask();
            }
        });

    }

    void selectDesiredCategory(HasId selectedAppCategory, boolean selectDefaultCategory) {
        if (selectedAppCategory == null) {
            if (selectDefaultCategory) {
                selectDefaultCategory();
            }
        } else {
            boolean desiredCategoryFound = false;
            for (Tree<AppCategory, String> tree : trees) {
                AppCategory category = tree.getStore().findModelWithKey(selectedAppCategory.getId());
                desiredCategoryFound = doSelectCategory(tree, category);
                if (desiredCategoryFound) {
                    break;
                }
            }
            if (!desiredCategoryFound) {
                selectDefaultCategory();
            }
        }
    }

    boolean doSelectCategory(Tree<AppCategory, String> tree, AppCategory category) {
        Tree.TreeNode<AppCategory> node = tree.findNode(category);
        if (node != null) {
            viewTabPanel.setActiveWidget(tree);
            tree.getSelectionModel().select(node.getModel(), true);
            return true;
        }
        return false;
    }

    void selectDefaultCategory() {
        Tree<AppCategory, String> tree = workspaceView.getTree();
        viewTabPanel.setActiveWidget(tree);
        tree.getSelectionModel().select(tree.getStore().getRootItems().get(0), true);
    }

    @Override
    public void onSelectedHierarchyNotFound(SelectedHierarchyNotFound event) {
        selectDefaultCategory();
    }

    void addHPCTabSelectionHandler() {
        viewTabPanel.addSelectionHandler(new SelectionHandler<Widget>() {
            @Override
            public void onSelection(SelectionEvent<Widget> event) {
                if (event.getSelectedItem() == hpcView.getTree() && hpcCategories.size() == 1) {
                    hpcView.getTree().getSelectionModel().select(hpcCategories.get(0), true);
                }
            }
        });
    }

    @Override
    public void setViewDebugId(String baseID) {
        this.baseId = baseID;
        workspaceView.asWidget().ensureDebugId(baseID + AppsModule.Ids.CATEGORIES);
        hpcView.asWidget().ensureDebugId(baseID + AppsModule.Ids.HPC);
    }

    void addCategoriesToHPCTree() {
        addAppCategories(hpcView.getTree().getStore(), null, hpcCategories);
    }

    void addCategoriesToWorkspaceTree() {
        TreeStore<AppCategory> treeStore = workspaceView.getTree().getStore();
        final Store.StoreSortInfo<AppCategory> info = new Store.StoreSortInfo<>(new AppCategoryComparator(treeStore),
                                                                                SortDir.ASC);
        treeStore.addSortInfo(info);
        addAppCategories(treeStore, null, workspaceCategories);
    }

    void filterCategories(List<AppCategory> result) {
        workspaceCategories.clear();
        hpcCategories.clear();
        for (AppCategory category : result) {
            if (category.getId().equals(HPC_ID)) {
                hpcCategories.add(category);
            } else {
                workspaceCategories.addAll(category.getCategories());
            }
        }
    }

    @Override
    public void onAppSaved(AppSavedEvent event) {
        /* JDS When an app is saved, always assume that the app is in the
         * "Apps Under Development" group
         */
        workspaceView.getTree().getSelectionModel().deselectAll();
        AppCategory userAppCategory = findAppCategoryByName(USER_APPS_GROUP);
        workspaceView.getTree().getSelectionModel().select(userAppCategory, false);
    }

    @Override
    public void onAppSearchResultLoad(AppSearchResultLoadEvent event) {
        searchRegexPattern = event.getSearchPattern();
        workspaceView.getTree().getSelectionModel().deselectAll();
        hpcView.getTree().getSelectionModel().deselectAll();
    }

    @Override
    public void onAppUpdated(final AppUpdatedEvent event) {
        final AppCategory currentCategory = getSelectedAppCategory();
        if (currentCategory != null && FAVORITES.equals(currentCategory.getName())) {
            // If our current category is Favorites, initiate refetch by reselecting category
            // This will cause the favorite count to be updated
            workspaceView.getTree().getSelectionModel().deselectAll();
            workspaceView.getTree().getSelectionModel().select(currentCategory, false);
        }

    }

    @Override
    public void onCopyAppSelected(CopyAppSelected event) {
        Preconditions.checkArgument(event.getApps().size() == 1);
        // JDS For now, assume only one app
        final App appToBeCopied = event.getApps().iterator().next();
        appUserService.copyApp(appToBeCopied, new AsyncCallback<AppTemplate>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(final AppTemplate app) {
                // Update the user's private apps group count.
                if (!app.getId().isEmpty()) {
                    announcer.schedule(new SuccessAnnouncementConfig(appearance.copyAppSuccessMessage(appToBeCopied.getName())));

                    workspaceView.getTree().getSelectionModel().deselectAll();
                    AppCategory userCategory = findAppCategoryByName(USER_APPS_GROUP);

                    // Select "Apps Under Dev" to cause fetch of center
                    workspaceView.getTree().getSelectionModel().select(userCategory, false);
                    eventBus.fireEvent(new EditAppEvent(app, false));
                }
            }
        });

    }

    @Override
    public void onCopyWorkflowSelected(final CopyWorkflowSelected event) {
        Preconditions.checkArgument(event.getApps().size() == 1);
        // JDS For now, assume only one app
        final App appToBeCopied = event.getApps().iterator().next();
        appUserService.copyWorkflow(appToBeCopied.getId(), new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                // TODO Add error message for the user.
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(String result) {
                // Update the user's private apps group count.
                workspaceView.getTree().getSelectionModel().deselectAll();
                AppCategory userAppsGrp = findAppCategoryByName(USER_APPS_GROUP);
                // Select "Apps Under Dev" to cause fetch of center
                workspaceView.getTree().getSelectionModel().select(userAppsGrp, false);

                // Fire an EditWorkflowEvent for the new workflow copy.
                Splittable serviceWorkflowJson = StringQuoter.split(result);
                eventBus.fireEvent(new EditWorkflowEvent(appToBeCopied, serviceWorkflowJson));
            }
        });
    }

    @Override
    public HandlerRegistration addAppCategorySelectedEventHandler(AppCategorySelectionChangedEvent.AppCategorySelectionChangedEventHandler handler) {
        return ensureHandlers().addHandler(AppCategorySelectionChangedEvent.TYPE, handler);
    }

    @Override
    public void onAppCategorySelectionChanged(AppCategorySelectionChangedEvent event) {
        fireEvent(event);
    }

    void addAppCategories(TreeStore<AppCategory> treeStore,
                          AppCategory parent,
                          List<AppCategory> children) {
        if ((children == null)
                || children.isEmpty()) {
            return;
        }
        if (parent == null) {
            treeStore.clear();
            treeStore.add(children);
        } else {
            treeStore.add(parent, children);
        }

        setCategoryPathTag(parent, children);

        for (AppCategory ag : children) {
            addAppCategories(treeStore, ag, ag.getCategories());
        }
    }

    void setCategoryPathTag(AppCategory parent, List<AppCategory> children) {
        String parentPath = "";
        if (parent != null) {
            final AutoBean<AppCategory> parentAutoBean = AutoBeanUtils.getAutoBean(parent);
            parentPath = parentAutoBean.getTag(PATH_KEY);
            if (children != null) {
                parentPath += "/";
            }
        }
        if (children != null) {
            for (AppCategory child : children) {
                final AutoBean<AppCategory> childAutoBean = AutoBeanUtils.getAutoBean(child);
                childAutoBean.setTag(PATH_KEY, parentPath + child.getName());
            }
        }
    }

    AppCategory findAppCategoryByName(String name) {
        for (AppCategory appCategory : workspaceView.getTree().getStore().getAll()) {
            if (appCategory.getName().equalsIgnoreCase(name)) {
                return appCategory;
            }
        }

        return null;
    }

    void initConstants(final DEProperties props,
                       final JsonUtil jsonUtil) {
        WORKSPACE = props.getPrivateWorkspace();
        HPC_ID = props.getDefaultHpcCategoryId();

        if (props.getPrivateWorkspaceItems() != null) {
            JSONArray items = JSONParser.parseStrict(props.getPrivateWorkspaceItems()).isArray();
            USER_APPS_GROUP = jsonUtil.getRawValueAsString(items.get(0));
            FAVORITES = jsonUtil.getRawValueAsString(items.get(1));
        }
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

}
