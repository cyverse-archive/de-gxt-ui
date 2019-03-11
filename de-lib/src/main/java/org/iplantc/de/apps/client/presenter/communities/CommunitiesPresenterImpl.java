package org.iplantc.de.apps.client.presenter.communities;

import static org.iplantc.de.apps.client.CommunitiesView.COMMUNITIES_ROOT;

import org.iplantc.de.apps.client.CommunitiesView;
import org.iplantc.de.apps.client.events.AppSearchResultLoadEvent;
import org.iplantc.de.apps.client.events.selection.AppCategorySelectionChangedEvent;
import org.iplantc.de.apps.client.events.selection.CommunitySelectionChangedEvent;
import org.iplantc.de.apps.client.gin.CommunityTreeStoreProvider;
import org.iplantc.de.apps.client.gin.factory.CommunitiesViewFactory;
import org.iplantc.de.apps.shared.AppsModule;
import org.iplantc.de.client.models.HasId;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.GroupAutoBeanFactory;
import org.iplantc.de.client.models.groups.GroupList;
import org.iplantc.de.client.services.AppServiceFacade;
import org.iplantc.de.client.services.AppUserServiceFacade;
import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.client.services.OauthServiceFacade;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.widgets.DETabPanel;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.tree.Tree;

import java.util.List;

/**
 * @author aramsey
 */
public class CommunitiesPresenterImpl implements CommunitiesView.Presenter {

    protected String searchRegexPattern;
    @Inject IplantAnnouncer announcer;

    @Inject AppServiceFacade appService;
    @Inject GroupServiceFacade groupServiceFacade;
    @Inject AppUserServiceFacade appUserService;
    @Inject OauthServiceFacade oauthServiceFacade;
    @Inject CommunitiesView.Appearance appearance;
    @Inject UserInfo userInfo;
    @Inject GroupAutoBeanFactory factory;
    DETabPanel deTabPanel;
    CommunitiesViewFactory viewFactory;
    CommunitiesView view;
    HandlerManager handlerManager;
    String baseId;

    @Inject
    CommunitiesPresenterImpl(CommunitiesViewFactory viewFactory,
                             GroupServiceFacade groupServiceFacade,
                             CommunitiesView.Appearance appearance) {
        this.viewFactory = viewFactory;
        this.groupServiceFacade = groupServiceFacade;
        this.appearance = appearance;
        this.view = viewFactory.create(new CommunityTreeStoreProvider().get());

        view.addCommunitySelectedEventHandler(this);
    }

    @Override
    public Group getSelectedCommunity() {
        return view.getTree().getSelectionModel().getSelectedItem();
    }

    @Override
    public CommunitiesView getView() {
        return view;
    }

    @Override
    public void go(final HasId selectedCommunity, final DETabPanel deTabPanel) {
        this.deTabPanel = deTabPanel;
        final Tree<Group, String> tree = view.getTree();
        tree.mask();

        groupServiceFacade.getMyCommunities(new AsyncCallback<Splittable>() {
            @Override
            public void onFailure(Throwable caught) {
                announcer.schedule(new ErrorAnnouncementConfig(appearance.failedToLoadCommunities()));
                tree.unmask();
            }

            @Override
            public void onSuccess(Splittable result) {
                List<Group> groupList = AutoBeanCodex.decode(factory, GroupList.class, result).as().getGroups();
                addCommunitiesToTree(groupList);
                selectDesiredCommunity(selectedCommunity);
                tree.unmask();
            }
        });

    }

    void selectDesiredCommunity(HasId selectedCommunity) {
        if (selectedCommunity != null) {
            Tree<Group, String> tree = view.getTree();
            Group community = tree.getStore().findModelWithKey(selectedCommunity.getId());
            deTabPanel.setActiveWidget(tree);
            tree.getSelectionModel().select(community, true);
        }
    }

    @Override
    public void setViewDebugId(String baseID) {
        this.baseId = baseID;
        view.asWidget().ensureDebugId(baseID + AppsModule.Ids.COMMUNITIES);
    }

    void addCommunitiesToTree(List<Group> communities) {
        Tree<Group, String> tree = view.getTree();
        TreeStore<Group> treeStore = tree.getStore();
        final Store.StoreSortInfo<Group> info = new Store.StoreSortInfo<>(new GroupComparator(), SortDir.ASC);
        treeStore.addSortInfo(info);
        treeStore.clear();

        Group myCommunitiesRoot = createMyCommunitiesRoot();
        treeStore.add(myCommunitiesRoot);
        treeStore.add(myCommunitiesRoot, communities);

        tree.expandAll();
    }

    Group createMyCommunitiesRoot() {
        Group myCommunities = factory.getGroup().as();
        myCommunities.setName("My Communities");
        myCommunities.setDisplayName(myCommunities.getName());
        myCommunities.setId(COMMUNITIES_ROOT);
        return myCommunities;
    }

    @Override
    public void onAppCategorySelectionChanged(AppCategorySelectionChangedEvent event) {
        view.getTree().getSelectionModel().deselectAll();
    }

    @Override
    public void onCommunitySelectionChanged(CommunitySelectionChangedEvent event) {
        Group selectedCommunity = event.getCommunitySelection();
        Tree<Group, String> tree = view.getTree();
        // If My Communities is selected, select first child node instead
        if (selectedCommunity.getId().equals(CommunitiesView.COMMUNITIES_ROOT) &&
            tree.getStore().hasChildren(selectedCommunity)) {
            tree.getSelectionModel().select(tree.getStore().getFirstChild(selectedCommunity), false);
        } else {
            fireEvent(event);
        }
    }

    @Override
    public HandlerRegistration addCommunitySelectedEventHandler(CommunitySelectionChangedEvent.CommunitySelectionChangedEventHandler handler) {
        return ensureHandlers().addHandler(CommunitySelectionChangedEvent.TYPE, handler);
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

    @Override
    public void onAppSearchResultLoad(AppSearchResultLoadEvent event) {
        view.getTree().getSelectionModel().deselectAll();
    }
}
