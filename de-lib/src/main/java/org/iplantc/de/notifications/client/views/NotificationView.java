package org.iplantc.de.notifications.client.views;

import org.iplantc.de.client.models.notifications.NotificationCategory;
import org.iplantc.de.client.models.notifications.NotificationMessage;
import org.iplantc.de.client.services.callbacks.ReactErrorCallback;
import org.iplantc.de.client.services.callbacks.ReactSuccessCallback;
import org.iplantc.de.notifications.client.presenter.NotificationsCallback;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.widget.core.client.button.TextButton;

import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsType;
@JsType
public interface NotificationView extends IsWidget {

    interface NotificationViewAppearance {

        String notifications();

        String notificationDeleteFail();

        String notificationMarkAsSeenFail();

        String notificationMarkAsSeenSuccess();

        String windowWidth();

        String windowHeight();

        int windowMinWidth();

        int windowMinHeight();

        String notificationUrlPrompt(String url);

    }

    @JsType
    public interface Presenter extends org.iplantc.de.commons.client.presenter.Presenter {
        /**
         * Filters the list of notifications by a given Category.
         * 
         * @param category
         */
        @JsIgnore
        public void filterBy(NotificationCategory category);

        /**
         * get default paging config
         * 
         * @return default FilterPagingLoadConfig
         */
        @JsIgnore
        public FilterPagingLoadConfig buildDefaultLoadConfig();

        @JsIgnore
        void setRefreshButton(TextButton refreshBtn);

        @JsIgnore
        NotificationCategory getCurrentCategory();

        @JsIgnore
        public void markAsRead(NotificationMessage nm);

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

        void onMessageClicked(Splittable notificationMessage);
    }
    void setPresenter(NotificationView.Presenter presenter);

    void loadNotifications();
}
