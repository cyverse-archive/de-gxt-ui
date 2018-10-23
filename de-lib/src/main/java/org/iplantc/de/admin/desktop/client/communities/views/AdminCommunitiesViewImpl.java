package org.iplantc.de.admin.desktop.client.communities.views;

import org.iplantc.de.admin.apps.client.AdminAppsGridView;
import org.iplantc.de.admin.desktop.client.communities.AdminCommunitiesView;
import org.iplantc.de.admin.desktop.client.communities.events.AddCommunityClicked;
import org.iplantc.de.admin.desktop.client.communities.events.CommunitySelectionChanged;
import org.iplantc.de.admin.desktop.client.communities.events.EditCommunityClicked;
import org.iplantc.de.admin.desktop.client.ontologies.events.HierarchySelectedEvent;
import org.iplantc.de.apps.client.events.AppSearchResultLoadEvent;
import org.iplantc.de.apps.client.events.BeforeAppSearchEvent;
import org.iplantc.de.apps.client.events.selection.AppSelectionChangedEvent;
import org.iplantc.de.apps.client.presenter.communities.GroupComparator;
import org.iplantc.de.apps.client.views.toolBar.AppSearchField;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.ontologies.OntologyHierarchy;
import org.iplantc.de.client.util.OntologyUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.CardLayoutContainer;
import com.sencha.gxt.widget.core.client.container.CenterLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.tree.Tree;

import java.util.List;

/**
 * @author aramsey
 */
public class AdminCommunitiesViewImpl extends Composite implements AdminCommunitiesView {

    interface AdminCommunitiesViewImplUiBinder extends UiBinder<Widget, AdminCommunitiesViewImpl> {}

    private static AdminCommunitiesViewImplUiBinder uiBinder = GWT.create(AdminCommunitiesViewImplUiBinder.class);

    @UiField TextButton addButton;
    @UiField TextButton deleteButton;
    @UiField TextButton editCommunity;
    @UiField TextButton categorize;
    @UiField AppSearchField appSearchField;
    @UiField(provided = true) AdminCommunitiesView.Appearance appearance;
    @UiField(provided = true) Tree<Group, String> communityTree;
    @UiField(provided = true) Tree<OntologyHierarchy, String> hierarchyTree;
    @UiField(provided = true) AdminAppsGridView communityGridView;
    @UiField(provided = true) AdminAppsGridView hierarchyGridView;
    @UiField ContentPanel communityPanel, appsPanel;
    @UiField CardLayoutContainer cards;
    @UiField CenterLayoutContainer noCommunitiesPanel;
    @UiField ContentPanel communityTreePanel, previewTreePanel;

    @UiField(provided = true) TreeStore<Group> communityTreeStore;
    @UiField(provided = true) TreeStore<OntologyHierarchy> hierarchyTreeStore;
    private App targetApp;
    private Group selectedCommunity;
    private OntologyUtil ontologyUtil = OntologyUtil.getInstance();
    private PagingLoader<FilterPagingLoadConfig, PagingLoadResult<App>> loader;

    @Inject
    public AdminCommunitiesViewImpl(AdminCommunitiesView.Appearance appearance,
                                    @Assisted("communityTreeStore") TreeStore<Group> communityTreeStore,
                                    @Assisted("hierarchyTreeStore") TreeStore<OntologyHierarchy> hierarchyTreeStore,
                                    @Assisted PagingLoader<FilterPagingLoadConfig, PagingLoadResult<App>> loader,
                                    @Assisted("communityGridView") AdminAppsGridView communityGridView,
                                    @Assisted("hierarchyGridView") AdminAppsGridView hierarchyGridView) {
        this.appearance = appearance;
        this.loader = loader;
        this.communityTreeStore = communityTreeStore;
        this.hierarchyTreeStore = hierarchyTreeStore;
        this.communityGridView = communityGridView;
        this.hierarchyGridView = hierarchyGridView;

        addTreeSelectionHandlers();

        initWidget(uiBinder.createAndBindUi(this));

        updateButtonStatus();
    }

    void addTreeSelectionHandlers() {
        communityTree = createCommunityTree(communityTreeStore);
        communityTree.getSelectionModel().addSelectionChangedHandler(event -> {
            List<Group> selectedCommunities = event.getSelection();
            if (selectedCommunities.size() == 1) {
                selectedCommunity = selectedCommunities.get(0);
                fireEvent(new CommunitySelectionChanged(selectedCommunity));
                updateButtonStatus();
            }
        });
        hierarchyTree = createHierarchyTree(hierarchyTreeStore);
        hierarchyTree.getSelectionModel().addSelectionChangedHandler(event -> {
            List<OntologyHierarchy> selectedHierarchies = event.getSelection();
            if (selectedHierarchies.size() == 1) {
                fireEvent(new HierarchySelectedEvent(selectedHierarchies.get(0), null, null));
            }
            updateButtonStatus();
        });
    }

    @Override
    public void onAppSelectionChanged(AppSelectionChangedEvent event) {
        List<App> appSelection = event.getAppSelection();
        targetApp = null;
        if (appSelection != null && appSelection.size() != 0) {
            if (event.getSource() == communityGridView) {
                hierarchyGridView.deselectAll();
            } else {
                communityGridView.deselectAll();
            }
            targetApp = appSelection.get(0);
        }
        updateButtonStatus();
    }

    @Override
    public void showNoCommunitiesPanel() {
        cards.setActiveWidget(noCommunitiesPanel);
    }

    @Override
    public void showCommunitiesPanel() {
        cards.setActiveWidget(communityTreePanel);
    }

    @Override
    public void deselectHierarchies() {
        hierarchyTree.getSelectionModel().deselectAll();
    }

    @Override
    public Group getCommunityFromElement(Element el) {
        Tree.TreeNode<Group> node = communityTree.findNode(el);
        if (node != null) {
            return node.getModel();
        }
        return null;
    }

    @Override
    public Group getSelectedCommunity() {
        return communityTree.getSelectionModel().getSelectedItem();
    }

    @UiFactory
    AppSearchField createAppSearchField() {
        return new AppSearchField(loader);
    }

    public void updateButtonStatus() {
        editCommunity.setEnabled(selectedCommunity != null);
        deleteButton.setEnabled(selectedCommunity != null);
    }

    @UiHandler("addButton")
    void addButtonClicked(SelectEvent event) {
        fireEvent(new AddCommunityClicked());
    }

    @UiHandler("deleteButton")
    void deleteButtonClicked(SelectEvent event) {
    }

    @UiHandler("editCommunity")
    void editCommunityClicked(SelectEvent event) {
        if (selectedCommunity != null) {
            fireEvent(new EditCommunityClicked(selectedCommunity));
        }
    }

    @UiHandler("categorize")
    void categorizeButtonClicked(SelectEvent event) {
    }

    Tree<OntologyHierarchy, String> createHierarchyTree(TreeStore<OntologyHierarchy> store) {
        Tree<OntologyHierarchy, String> ontologyTree = new Tree<>(store, new ValueProvider<OntologyHierarchy, String>() {
            @Override
            public String getValue(OntologyHierarchy object) {
                return object.getLabel();
            }

            @Override
            public void setValue(OntologyHierarchy object, String value) {

            }

            @Override
            public String getPath() {
                return null;
            }
        });

        ontologyTree.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);

        store.addSortInfo(new Store.StoreSortInfo<>(ontologyUtil.getOntologyNameComparator(), SortDir.ASC));

        return ontologyTree;
    }

    Tree<Group, String> createCommunityTree(TreeStore<Group> store) {
        Tree<Group, String> communityTree = new Tree<>(store, new ValueProvider<Group, String>() {
            @Override
            public String getValue(Group object) {
                return object.getName();
            }

            @Override
            public void setValue(Group object, String value) {}

            @Override
            public String getPath() {
                return null;
            }
        });
        communityTree.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
        store.addSortInfo(new Store.StoreSortInfo<>(new GroupComparator(), SortDir.ASC));

        return communityTree;
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
    }

    @Override
    public HandlerRegistration addCommunitySelectionChangedHandler(CommunitySelectionChanged.CommunitySelectionChangedHandler handler) {
        return addHandler(handler, CommunitySelectionChanged.TYPE);
    }

    @Override
    public HandlerRegistration addHierarchySelectedEventHandler(HierarchySelectedEvent.HierarchySelectedEventHandler handler) {
        return addHandler(handler, HierarchySelectedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addAppSearchResultLoadEventHandler(AppSearchResultLoadEvent.AppSearchResultLoadEventHandler handler) {
        return addHandler(handler, AppSearchResultLoadEvent.TYPE);
    }

    @Override
    public HandlerRegistration addBeforeAppSearchEventHandler(BeforeAppSearchEvent.BeforeAppSearchEventHandler handler) {
        return addHandler(handler, BeforeAppSearchEvent.TYPE);
    }

    @Override
    public HandlerRegistration addAddCommunityClickedHandler(AddCommunityClicked.AddCommunityClickedHandler handler) {
        return addHandler(handler, AddCommunityClicked.TYPE);
    }

    @Override
    public HandlerRegistration addEditCommunityClickedHandler(EditCommunityClicked.EditCommunityClickedHandler handler) {
        return addHandler(handler, EditCommunityClicked.TYPE);
    }
}
