package org.iplantc.de.client.services.callbacks;

import org.iplantc.de.client.models.notifications.Notification;
import org.iplantc.de.client.models.notifications.NotificationAutoBeanFactory;
import org.iplantc.de.client.models.notifications.NotificationList;
import org.iplantc.de.notifications.client.utils.NotificationUtil;
import org.iplantc.de.shared.NotificationCallback;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

import java.util.Collections;
import java.util.List;

/**
 * @author jstroot
 *
 */
public abstract class NotificationCallbackWrapper extends NotificationCallback<String> {

    private final NotificationAutoBeanFactory notFactory = GWT.create(NotificationAutoBeanFactory.class);
    private NotificationUtil notificationUtil = GWT.create(NotificationUtil.class);
    private List<Notification> notifications;

    @Override
    public void onSuccess(String result) {
        AutoBean<NotificationList> bean = AutoBeanCodex.decode(notFactory, NotificationList.class, result);
        List<Notification> notifications = bean.as().getNotifications();
        for (Notification n : notifications) {
            notificationUtil.getMessage(n, notFactory);
        }

        setNotifications(notifications);
    }

    private void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    protected List<Notification> getNotifications() {
        if (notifications == null) {
            return Collections.emptyList();
        } else {
            return notifications;
        }
    }

}
