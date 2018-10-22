package org.iplantc.de.admin.desktop.client.communities.views;

import org.iplantc.de.admin.apps.client.AdminAppsGridView;
import org.iplantc.de.admin.desktop.client.communities.AdminCommunitiesView;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.groups.Group;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Widget;

import com.sencha.gxt.dnd.core.client.DndDragEnterEvent;
import com.sencha.gxt.dnd.core.client.DndDragMoveEvent;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.StatusProxy;
import com.sencha.gxt.fx.client.DragMoveEvent;

import java.util.List;

/**
 * @author aramsey
 *
 * The drag and drop handler for dragging an app to a community (Group)
 */
public class AppToCommunityDND implements DndDragStartEvent.DndDragStartHandler,
                                          DndDropEvent.DndDropHandler,
                                          DndDragMoveEvent.DndDragMoveHandler,
                                          DndDragEnterEvent.DndDragEnterHandler {

    AdminCommunitiesView.Appearance appearance;
    AdminAppsGridView.Presenter hierarchyGridPresenter;
    AdminAppsGridView.Presenter communityGridPresenter;
    Widget hierarchyGridView;
    Widget communityGridView;
    AdminCommunitiesView.Presenter presenter;
    boolean moved;

    public AppToCommunityDND(AdminCommunitiesView.Appearance appearance,
                             AdminAppsGridView.Presenter hierarchyGridPresenter,
                             AdminAppsGridView.Presenter communityGridPresenter,
                             AdminCommunitiesView.Presenter presenter) {
        this.appearance = appearance;
        this.hierarchyGridPresenter = hierarchyGridPresenter;
        this.communityGridPresenter = communityGridPresenter;
        this.presenter = presenter;
        this.hierarchyGridView = hierarchyGridPresenter.getView().asWidget();
        this.communityGridView = communityGridPresenter.getView().asWidget();
    }

    @Override
    public void onDragEnter(DndDragEnterEvent event) {
        moved = false;

        Widget dragSource = event.getDragEnterEvent().getSource().getDragWidget();
        List<App> apps = getDragSources(dragSource);
        DragMoveEvent dragEnterEvent = event.getDragEnterEvent();
        EventTarget target = dragEnterEvent.getNativeEvent().getEventTarget();
        Group community = getDropTargetCommunity(Element.as(target));

        if (!validateDropStatus(community, apps, event.getStatusProxy())) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onDragMove(DndDragMoveEvent event) {
        moved = true;

        Widget dragSource = event.getDragMoveEvent().getSource().getDragWidget();
        List<App> apps = getDragSources(dragSource);
        EventTarget target = event.getDragMoveEvent().getNativeEvent().getEventTarget();
        Group community  = getDropTargetCommunity(Element.as(target));

        if (!validateDropStatus(community, apps, event.getStatusProxy())) {
            event.setCancelled(true);
        }
    }


    @Override
    public void onDragStart(DndDragStartEvent event) {
        moved = false;

        Widget dragSource = event.getDragStartEvent().getSource().getDragWidget();
        List<App> apps = getDragSources(dragSource);

        if (apps == null) {
            // Cancel drag
            event.setCancelled(true);
        } else {
            event.setData(apps);
            event.getStatusProxy().update((SafeHtml)() -> getAppLabels(apps));
            event.getStatusProxy().setStatus(true);
            event.setCancelled(false);
        }
    }

    @Override
    public void onDrop(DndDropEvent event) {
        if (!moved) return;

        Widget dragSource = event.getDragEndEvent().getSource().getDragWidget();
        List<App> apps = getDragSources(dragSource);
        EventTarget target = event.getDragEndEvent().getNativeEvent().getEventTarget();
        Group community  = getDropTargetCommunity(Element.as(target));

        if (validateDropStatus(community, apps, event.getStatusProxy())) {
            presenter.appsDNDtoCommunity(apps, community);
        }

    }

    private List<App> getDragSources(Widget dragWidget) {
        if (isHierarchyGridApp(dragWidget)) {
            return hierarchyGridPresenter.getSelectedApps();
        }
        if (isCommunityGridApp(dragWidget)) {
            return communityGridPresenter.getSelectedApps();
        }
        return null;
    }

    private boolean isHierarchyGridApp(Widget dragSource) {
        return dragSource == hierarchyGridView;
    }

    private boolean isCommunityGridApp(Widget dragSource) {
        return dragSource == communityGridView;
    }

    private Group getDropTargetCommunity(Element eventTarget) {
        return presenter.getCommunityFromElement(eventTarget);
    }

    private boolean validateDropStatus(final Group targetCommunity,
                                       final List<App> apps,
                                       final StatusProxy status) {
        // Verify we have drag data.
        if (apps == null || apps.size() == 0) {
            status.setStatus(false);
            status.update(SafeHtmlUtils.fromString(""));
            return false;
        }

        // Verify no external apps are selected
        List<App> agaveApps = Lists.newArrayList();
        for (App app : apps) {
            if (app.getAppType().equalsIgnoreCase(App.EXTERNAL_APP)) {
                agaveApps.add(app);
            }
        }

        if (agaveApps.size() > 0) {
            status.setStatus(false);
            status.update(SafeHtmlUtils.fromString(appearance.externalAppDND(getAppLabels(agaveApps))));
            return false;
        }

        String appList = getAppLabels(apps);

        // Verify we have a drop target.
        if (targetCommunity == null) {
            status.setStatus(false);
            status.update(SafeHtmlUtils.fromString(appList));
            return false;
        }

        // Reset status message
        status.setStatus(true);
        status.update(SafeHtmlUtils.fromString(appList + " > " + targetCommunity.getName()));
        return true;
    }

    private String getAppLabels(List<App> apps) {
        List<String> labels = Lists.newArrayList();
        for (App app: apps) {
            labels.add(app.getName());
        }
        return Joiner.on(",").join(labels);
    }
}
