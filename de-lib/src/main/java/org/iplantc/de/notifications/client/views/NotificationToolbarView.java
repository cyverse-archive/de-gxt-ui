package org.iplantc.de.notifications.client.views;

import org.iplantc.de.client.models.notifications.NotificationCategory;
import org.iplantc.de.notifications.client.events.NotificationToolbarDeleteAllClickedEvent;
import org.iplantc.de.notifications.client.events.NotificationToolbarDeleteClickedEvent;
import org.iplantc.de.notifications.client.events.NotificationToolbarMarkAsSeenClickedEvent;
import org.iplantc.de.notifications.client.events.NotificationToolbarSelectionEvent;

import com.google.gwt.user.client.ui.IsWidget;

import com.sencha.gxt.widget.core.client.button.TextButton;

/**
 * 
 * 
 * @author sriram
 * 
 */
public interface NotificationToolbarView extends IsWidget,
                                                 NotificationToolbarDeleteClickedEvent.HasNotificationToolbarDeleteClickedEventHandlers,
                                                 NotificationToolbarDeleteAllClickedEvent.HasNotificationToolbarDeleteAllClickedEventHandlers,
                                                 NotificationToolbarSelectionEvent.HasNotificationToolbarSelectionEventHandlers,
                                                 NotificationToolbarMarkAsSeenClickedEvent.HasNotificationToolbarMarkAsSeenClickedEventHandlers {

    void setDeleteButtonEnabled(boolean enabled);

    void setDeleteAllButtonEnabled(boolean enabled);

    void setRefreshButton(TextButton refreshBtn);

    void setMarkAsSeenButtonEnabled(boolean enabled);

    void setCurrentCategory(NotificationCategory category);

}
