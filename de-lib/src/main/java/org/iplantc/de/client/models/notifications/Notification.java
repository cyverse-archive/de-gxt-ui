package org.iplantc.de.client.models.notifications;


import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;
import com.google.web.bindery.autobean.shared.Splittable;

public interface Notification {

    @PropertyName("seen")
    void setSeen(boolean seen);

    @PropertyName("seen")
    boolean isSeen();

    @PropertyName("message")
    void setMessage(NotificationMessage message);

    @PropertyName("message")
    NotificationMessage getMessage();

    @PropertyName("type")
    void setCategory(String category);

    @PropertyName("type")
    String getCategory();

    @PropertyName("payload")
    Splittable getNotificationPayload();

    @PropertyName("payload")
    void setNotificationPayload(Splittable payload);

    String getUser();
    void setUser(String user);

    String getSubject();
    void setSubject(String subject);

    @PropertyName("email_template")
    String getEmailTemplate();

    boolean isEmail();
    void setEmail(boolean email);
}
