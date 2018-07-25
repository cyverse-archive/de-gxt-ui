/**
 *
 */
package org.iplantc.de.notifications.client.views;

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

    @Inject
    public NotificationViewImpl() {
        panel = new HTMLPanel("<div></div>");
    }

    @Override
    public void setPresenter(NotificationView.Presenter presenter) {
        this.presenter = presenter;
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
    public void loadNotifications() {
        Scheduler.get().scheduleFinally(() -> {
            ReactNotifications.NotificationsProps props = new ReactNotifications.NotificationsProps();
            props.presenter = presenter;
            CyVerseReactComponents.render(ReactNotifications.notifiProps, props, panel.getElement());

        });
    }
}
