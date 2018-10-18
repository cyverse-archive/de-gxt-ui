package org.iplantc.de.apps.client.presenter.communities;

import org.iplantc.de.apps.client.CommunitiesView;
import org.iplantc.de.apps.client.events.selection.CommunitySelectionChangedEvent;
import org.iplantc.de.apps.client.gin.CommunityTreeStoreProvider;
import org.iplantc.de.apps.client.gin.factory.CommunitiesViewFactory;
import org.iplantc.de.apps.client.views.details.dialogs.AppDetailsDialog;
import org.iplantc.de.apps.shared.AppsModule;
import org.iplantc.de.client.models.HasId;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.services.AppServiceFacade;
import org.iplantc.de.client.services.AppUserServiceFacade;
import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.client.services.OauthServiceFacade;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.widgets.DETabPanel;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

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
    @Inject AsyncProviderWrapper<AppDetailsDialog> appDetailsDlgAsyncProvider;
    @Inject AppServiceFacade appService;
    @Inject GroupServiceFacade groupServiceFacade;
    @Inject AppUserServiceFacade appUserService;
    @Inject OauthServiceFacade oauthServiceFacade;
    @Inject CommunitiesView.Appearance appearance;
    @Inject UserInfo userInfo;
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

        groupServiceFacade.getCommunities(new AsyncCallback<List<Group>>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
                deTabPanel.unmask();
            }

            @Override
            public void onSuccess(List<Group> result) {
                createView();
                view.getTree().mask(appearance.loadingMask());
                addCommunitiesToTree(result);
                selectDesiredCommunity(selectedCommunity);
                view.getTree().unmask();
            }
        });

    }

    void createView() {
        this.view = viewFactory.create(new CommunityTreeStoreProvider().get());
        view.addCommunitySelectedEventHandler(this);

        this.deTabPanel.insert(view.getTree(), deTabPanel.getWidgetCount() - 1, appearance.communities());
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
        TreeStore<Group> treeStore = view.getTree().getStore();
        final Store.StoreSortInfo<Group> info = new Store.StoreSortInfo<>(new GroupComparator(), SortDir.ASC);
        treeStore.addSortInfo(info);
        treeStore.add(communities);
    }

    @Override
    public void onCommunitySelectionChanged(CommunitySelectionChangedEvent event) {
        fireEvent(event);
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
}
