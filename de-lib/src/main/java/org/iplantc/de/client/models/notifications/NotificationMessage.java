package org.iplantc.de.client.models.notifications;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;
import org.iplantc.de.client.models.HasSettableId;

/**
 * 
 * Notification bean
 * 
 * @author sriram
 * 
 */
public interface NotificationMessage extends HasSettableId {

    @PropertyName("text")
    String getMessage();

    @PropertyName("text")
    void setMessage(String message);

    @PropertyName("timestamp")
    void setTimestamp(long date);

    @PropertyName("timestamp")
    long getTimestamp();

    @PropertyName("type")
    NotificationCategory getCategory();

    @PropertyName("type")
    void setCategory(NotificationCategory type);

    String getContext();

    void setContext(String context);

    void setSeen(boolean seen);

    boolean isSeen();
}
