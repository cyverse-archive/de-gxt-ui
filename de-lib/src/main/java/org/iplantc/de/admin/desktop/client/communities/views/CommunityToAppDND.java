package org.iplantc.de.admin.desktop.client.communities.views;

import org.iplantc.de.admin.apps.client.AdminAppsGridView;
import org.iplantc.de.admin.desktop.client.communities.AdminCommunitiesView;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.groups.Group;

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

/**
 * @author aramsey
 */
public class CommunityToAppDND implements DndDragStartEvent.DndDragStartHandler,
                                          DndDropEvent.DndDropHandler,
                                          DndDragMoveEvent.DndDragMoveHandler,
                                          DndDragEnterEvent.DndDragEnterHandler {

    AdminCommunitiesView.Appearance appearance;
    AdminAppsGridView.Presenter hierarchyGridPresenter;
    AdminAppsGridView.Presenter communityGridPresenter;
    AdminCommunitiesView.Presenter presenter;
    boolean moved;

    public CommunityToAppDND(AdminCommunitiesView.Appearance appearance,
                             AdminAppsGridView.Presenter hierarchyGridPresenter,
                             AdminAppsGridView.Presenter communityGridPresenter,
                             AdminCommunitiesView.Presenter presenter) {
        this.appearance = appearance;
        this.hierarchyGridPresenter = hierarchyGridPresenter;
        this.communityGridPresenter = communityGridPresenter;
        this.presenter = presenter;
    }

    @Override
    public void onDragEnter(DndDragEnterEvent event) {
        moved = false;
        Group dropData = getDragSources();
        DragMoveEvent dragEnterEvent = event.getDragEnterEvent();
        Widget widget = dragEnterEvent.getTarget();
        EventTarget target = dragEnterEvent.getNativeEvent().getEventTarget();
        App targetApp = getDropTargetApp(Element.as(target), widget);

        if (!validateDropStatus(targetApp, dropData, event.getStatusProxy())) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onDragMove(DndDragMoveEvent event) {
        moved = true;

        Group community = getDragSources();
        Widget widget = event.getDropTarget().getWidget();
        EventTarget target = event.getDragMoveEvent().getNativeEvent().getEventTarget();
        App targetApp = getDropTargetApp(Element.as(target), widget);

        if (!validateDropStatus(targetApp, community, event.getStatusProxy())) {
            event.setCancelled(true);
        }
    }


    @Override
    public void onDragStart(DndDragStartEvent event) {
        moved = false;

        Group community = getDragSources();

        if (community == null) {
            // Cancel drag
            event.setCancelled(true);
        } else {
            event.setData(community);
            event.getStatusProxy().update((SafeHtml)community::getName);
            event.getStatusProxy().setStatus(true);
            event.setCancelled(false);
        }
    }

    @Override
    public void onDrop(DndDropEvent event) {
        if (!moved) return;

        Group community = getDragSources();
        Widget widget = event.getDropTarget().getWidget();
        EventTarget target = event.getDragEndEvent().getNativeEvent().getEventTarget();
        App targetApp = getDropTargetApp(Element.as(target), widget);

        if (validateDropStatus(targetApp, community, event.getStatusProxy())) {
            presenter.communityDNDtoApp(community, targetApp);
        }

    }

    private Group getDragSources() {
        return presenter.getSelectedCommunity();
    }

    private App getDropTargetApp(Element eventTarget, Widget dropTarget) {
        if (dropTarget == hierarchyGridPresenter.getView().asWidget()) {
            return hierarchyGridPresenter.getAppFromElement(eventTarget);
        }

        if (dropTarget == communityGridPresenter.getView().asWidget()) {
            return communityGridPresenter.getAppFromElement(eventTarget);
        }

        return null;
    }

    private boolean validateDropStatus(final App targetApp,
                                       final Group community,
                                       final StatusProxy status) {
        // Verify we have drag data.
        if (community == null) {
            status.setStatus(false);
            status.update(SafeHtmlUtils.fromString(""));
            return false;
        }

        // Verify we have a drop target.
        if (targetApp == null) {
            status.setStatus(false);
            status.update(SafeHtmlUtils.fromString(""));
            return false;
        }

        // Verify the target is not an external app
        if (targetApp.getAppType().equalsIgnoreCase(App.EXTERNAL_APP)) {
            status.setStatus(false);
            status.update(SafeHtmlUtils.fromString(appearance.externalAppDND(targetApp.getName())));
            return false;
        }

        // Reset status message
        status.setStatus(true);
        status.update(SafeHtmlUtils.fromString(community.getName() + " > " + targetApp.getName()));
        return true;
    }
}
