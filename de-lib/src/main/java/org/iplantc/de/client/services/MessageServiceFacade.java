package org.iplantc.de.client.services;

import org.iplantc.de.client.models.HasId;
import org.iplantc.de.client.models.HasUUIDs;
import org.iplantc.de.client.models.notifications.NotificationCategory;
import org.iplantc.de.client.models.notifications.NotificationList;
import org.iplantc.de.client.services.callbacks.NotificationCallbackWrapper;
import org.iplantc.de.shared.DECallback;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface MessageServiceFacade {

    /**
     * Get notifications from the server.
     *
     * @param maxNotifications the maximum number of notifications to retrieve.
     * @param callback called on RPC completion.
     */
    void getNotifications(int limit, int offset, String filter, String sortDir, NotificationCallbackWrapper callback);

    /**
     * Get messages from the server.
     *
     * @param callback called on RPC completion.
     */
    void getRecentMessages(AsyncCallback<NotificationList> callback);

    void markAsSeen(HasUUIDs seenIds, DECallback<String> callback);

    void markAsSeen(HasId id, DECallback<String> callback);

    /**
     * Delete messages from the server.
     *
     * @param deleteIds array of notification ids to delete from the server.
     * @param callback called on RPC completion.
     */
    void deleteMessages(HasUUIDs deleteIds, DECallback<String> callback);

    void deleteAll(NotificationCategory category, DECallback<String> callback);

    void markAllNotificationsSeen(DECallback<Void> callback);
    
    void getPermanentIdRequestStatusHistory(String id, DECallback<String> callback);

}
