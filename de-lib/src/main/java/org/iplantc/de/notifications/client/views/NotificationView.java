package org.iplantc.de.notifications.client.views;

import org.iplantc.de.client.models.notifications.NotificationCategory;
import org.iplantc.de.client.services.callbacks.ReactErrorCallback;
import org.iplantc.de.client.services.callbacks.ReactSuccessCallback;
import org.iplantc.de.notifications.client.presenter.NotificationsCallback;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.autobean.shared.Splittable;

import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsType;
@JsType
public interface NotificationView extends IsWidget {

    interface NotificationViewAppearance {

        String notifications();

        String notificationDeleteFail();

        String notificationMarkAsSeenFail();

        String notificationMarkAsSeenSuccess();

        int windowMinWidth();

        int windowMinHeight();

        String notificationUrlPrompt(String url);

    }

    @JsType
    interface Presenter {

        @JsIgnore
        void go(final HasOneWidget container, String baseDebugId, NotificationCategory category);

        void getNotifications(int limit,
                              int offset,
                              String filter,
                              String sortDir,
                              NotificationsCallback callback,
                              ReactErrorCallback errorCallback);

        void deleteNotifications(String[] ids,
                                 ReactSuccessCallback callback,
                                 ReactErrorCallback errorCallback);

        void onNotificationToolbarMarkAsSeenClicked(String[] ids,
                                                    ReactSuccessCallback callback,
                                                    ReactErrorCallback errorCallback);

        @SuppressWarnings("unusable-by-js")
        void onMessageClicked(Splittable notificationMessage);

        NotificationCategory getCurrentCategory();
    }

    void setPresenter(NotificationView.Presenter presenter, String baseDebugId);

    void loadNotifications(NotificationCategory category);
}
