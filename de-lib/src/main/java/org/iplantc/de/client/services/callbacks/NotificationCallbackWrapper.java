package org.iplantc.de.client.services.callbacks;

import org.iplantc.de.client.models.notifications.Notification;
import org.iplantc.de.client.models.notifications.NotificationAutoBeanFactory;
import org.iplantc.de.client.models.notifications.NotificationList;
import org.iplantc.de.notifications.client.utils.NotificationUtil;
import org.iplantc.de.shared.NotificationCallback;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

/**
 * @author jstroot
 *
 */
public abstract class NotificationCallbackWrapper extends NotificationCallback<String> {

    private final NotificationAutoBeanFactory notFactory = GWT.create(NotificationAutoBeanFactory.class);
    private NotificationUtil notificationUtil = GWT.create(NotificationUtil.class);
    private NotificationList notifications;

    @Override
    public void onSuccess(String result) {
        AutoBean<NotificationList> bean = AutoBeanCodex.decode(notFactory, NotificationList.class, result);
        notifications = bean.as();
        for (Notification n : notifications.getNotifications()) {
            notificationUtil.getMessage(n, notFactory);
        }

        setNotifications(notifications);
    }

    private void setNotifications(NotificationList notifications) {
        this.notifications = notifications;
    }

    protected NotificationList getNotifications() {
        return notifications;
    }



}
