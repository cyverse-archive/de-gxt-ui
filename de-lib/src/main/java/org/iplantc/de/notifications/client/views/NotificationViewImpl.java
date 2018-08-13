/**
 *
 */
package org.iplantc.de.notifications.client.views;

import org.iplantc.de.client.models.notifications.NotificationCategory;
import org.iplantc.de.commons.client.util.CyVerseReactComponents;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * 
 * Notification View as grid
 * 
 * @author sriram
 * 
 */
public class NotificationViewImpl implements NotificationView {


    HTMLPanel panel;

    NotificationView.Presenter presenter;

    String baseDebugId;

    @Inject
    public NotificationViewImpl() {
        panel = new HTMLPanel("<div></div>");
    }

    @Override
    public void setPresenter(NotificationView.Presenter presenter, String baseDebugId) {
        this.presenter = presenter;
        this.baseDebugId = baseDebugId;
    }


    /*
     * (non-Javadoc)
     * 
     * @see com.google.gwt.user.client.ui.IsWidget#asWidget()
     */
    @Override
    public Widget asWidget() {
        return panel;
    }


    @SuppressWarnings("unchecked")
    @Override
    public void loadNotifications(NotificationCategory category) {
        Scheduler.get().scheduleFinally(() -> {
            ReactNotifications.NotificationsProps props = new ReactNotifications.NotificationsProps();
            props.presenter = presenter;
            props.baseDebugId = baseDebugId;
            props.category = category.toString();
            CyVerseReactComponents.render(ReactNotifications.notifiProps, props, panel.getElement());

        });
    }
}
