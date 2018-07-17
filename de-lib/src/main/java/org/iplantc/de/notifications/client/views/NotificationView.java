package org.iplantc.de.notifications.client.views;

import org.iplantc.de.client.models.notifications.NotificationCategory;
import org.iplantc.de.client.models.notifications.NotificationMessage;
import org.iplantc.de.client.services.callbacks.ReactErrorCallback;
import org.iplantc.de.notifications.client.events.NotificationGridRefreshEvent;
import org.iplantc.de.notifications.client.events.NotificationSelectionEvent;
import org.iplantc.de.notifications.client.presenter.NotificationsCallback;

import com.google.gwt.user.client.ui.IsWidget;

import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.button.TextButton;

import java.util.List;

import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsType;
@JsType
public interface NotificationView extends IsWidget,
                                          NotificationGridRefreshEvent.HasNotificationGridRefreshEventHandlers,
                                          NotificationSelectionEvent.HasNotificationSelectionEventHandlers {

    interface NotificationViewAppearance {

        String notifications();

        String refresh();

        String notificationDeleteFail();

        String notificationMarkAsSeenFail();

        String notificationMarkAsSeenSuccess();

        String category();

        int categoryColumnWidth();

        String messagesGridHeader();

        int messagesColumnWidth();

        String createdDateGridHeader();

        int createdDateColumnWidth();

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


        public void getNotifications(int limit,
                                     int offset,
                                     NotificationsCallback callback,
                                     ReactErrorCallback errorCallback);
    }

    /**
     * get current loader config
     * 
     * @return the current load config
     */
    @JsIgnore
    public FilterPagingLoadConfig getCurrentLoadConfig();

    /**
     * Get list of selected notification
     * 
     * @return a list containing selected notification objects
     */
    @JsIgnore
    public List<NotificationMessage> getSelectedItems();

    /**
     * loads notifications using given laod conig
     * 
     * @param config FilterPagingLoadConfig
     */
    @JsIgnore
    public void loadNotifications(FilterPagingLoadConfig config);

    @JsIgnore
    public void setLoader(
            PagingLoader<FilterPagingLoadConfig, PagingLoadResult<NotificationMessage>> loader);

    @JsIgnore
    void setNorthWidget(IsWidget widget);

    @JsIgnore
    void mask();

    @JsIgnore
    void unmask();

    @JsIgnore
    public TextButton getRefreshButton();

    @JsIgnore
    public void updateStore(NotificationMessage nm);

    void setPresenter(NotificationView.Presenter presenter);
}
