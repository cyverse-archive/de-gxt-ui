package org.iplantc.de.client.services;

import java.util.List;

import org.iplantc.de.client.models.HasId;
import org.iplantc.de.client.models.HasUUIDs;
import org.iplantc.de.client.models.notifications.Counts;
import org.iplantc.de.client.models.notifications.NotificationCategory;
import org.iplantc.de.client.models.notifications.NotificationList;
import org.iplantc.de.client.services.callbacks.NotificationCallbackWrapper;
import org.iplantc.de.shared.DECallback;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.rpc.AsyncCallback;

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

    void markAsSeen(List<HasId> seenIds, DECallback<String> callback);

    void markAsSeen(HasId id, DECallback<String> callback);

    /**
     * Delete messages from the server.
     *
     * @param deleteIds array of notification ids to delete from the server.
     * @param callback called on RPC completion.
     */
    void deleteMessages(HasUUIDs deleteIds, DECallback<String> callback);

    /**
     * Retrieves the message counts from the server where the seen parameter is false.
     * 
     * @param callback called on RPC completion
     */
    void getMessageCounts(DECallback<Counts> callback);

    void deleteAll(NotificationCategory category, DECallback<String> callback);

    void markAllNotificationsSeen(DECallback<Void> callback);
    
    void getPermanentIdRequestStatusHistory(String id, DECallback<String> callback);

}
