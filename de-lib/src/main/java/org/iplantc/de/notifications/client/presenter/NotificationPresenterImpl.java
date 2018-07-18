package org.iplantc.de.notifications.client.presenter;

import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.HasUUIDs;
import org.iplantc.de.client.models.notifications.NotificationAutoBeanFactory;
import org.iplantc.de.client.models.notifications.NotificationCategory;
import org.iplantc.de.client.models.notifications.NotificationMessage;
import org.iplantc.de.client.services.MessageServiceFacade;
import org.iplantc.de.client.services.callbacks.ReactErrorCallback;
import org.iplantc.de.client.services.callbacks.ReactSuccessCallback;
import org.iplantc.de.client.util.JsonUtil;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;
import org.iplantc.de.notifications.client.events.DeleteNotificationsUpdateEvent;
import org.iplantc.de.notifications.client.events.JoinTeamRequestProcessed;
import org.iplantc.de.notifications.client.events.NotificationCountUpdateEvent;
import org.iplantc.de.notifications.client.events.NotificationGridRefreshEvent;
import org.iplantc.de.notifications.client.events.NotificationSelectionEvent;
import org.iplantc.de.notifications.client.events.NotificationToolbarDeleteAllClickedEvent;
import org.iplantc.de.notifications.client.events.NotificationToolbarDeleteClickedEvent;
import org.iplantc.de.notifications.client.events.NotificationToolbarMarkAsSeenClickedEvent;
import org.iplantc.de.notifications.client.events.NotificationToolbarSelectionEvent;
import org.iplantc.de.notifications.client.model.NotificationMessageProperties;
import org.iplantc.de.notifications.client.utils.NotificationUtil;
import org.iplantc.de.notifications.client.views.NotificationToolbarView;
import org.iplantc.de.notifications.client.views.NotificationView;
import org.iplantc.de.shared.NotificationCallback;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.SortInfoBean;
import com.sencha.gxt.data.shared.loader.FilterConfig;
import com.sencha.gxt.data.shared.loader.FilterConfigBean;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfigBean;
import com.sencha.gxt.widget.core.client.button.TextButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A presenter for notification window
 *
 * @author sriram
 */
public class NotificationPresenterImpl implements NotificationView.Presenter,
                                                  NotificationGridRefreshEvent.NotificationGridRefreshEventHandler,
                                                  NotificationSelectionEvent.NotificationSelectionEventHandler,
                                                  NotificationToolbarSelectionEvent.NotificationToolbarSelectionEventHandler,
                                                  NotificationToolbarDeleteClickedEvent.NotificationToolbarDeleteClickedEventHandler,
                                                  NotificationToolbarDeleteAllClickedEvent.NotificationToolbarDeleteAllClickedEventHandler,
                                                  NotificationToolbarMarkAsSeenClickedEvent.NotificationToolbarMarkAsSeenClickedEventHandler,
                                                  JoinTeamRequestProcessed.JoinTeamRequestProcessedHandler {

    private final ListStore<NotificationMessage> listStore;
    private final NotificationToolbarView toolbar;
    private final NotificationView view;
    private NotificationAutoBeanFactory factory;
    private EventBus eventBus;
    private NotificationView.NotificationViewAppearance appearance;
    NotificationCategory currentCategory;
    @Inject MessageServiceFacade messageServiceFacade;
    @Inject JsonUtil jsonUtil;
    @Inject IplantAnnouncer announcer;
    @Inject
    NotificationUtil notificationUtil;

    @Inject
    public NotificationPresenterImpl(NotificationView.NotificationViewAppearance appearance,
                                     NotificationToolbarView toolbar,
                                     NotificationMessageProperties messageProperties,
                                     NotificationAutoBeanFactory factory,
                                     NotificationView view,
                                     EventBus eventBus) {
        this.appearance = appearance;
        this.listStore = createListStore(messageProperties);
        this.view = view;
        this.factory = factory;
        this.eventBus = eventBus;
        currentCategory = NotificationCategory.ALL;
        this.toolbar = toolbar;
        view.setNorthWidget(toolbar);

        setRefreshButton(view.getRefreshButton());
        addEventHandlers();
    }

    private void addEventHandlers() {
        eventBus.addHandler(JoinTeamRequestProcessed.TYPE, this);
        view.addNotificationGridRefreshEventHandler(this);
        view.addNotificationSelectionEventHandler(this);
        toolbar.addNotificationToolbarDeleteAllClickedEventHandler(this);
        toolbar.addNotificationToolbarDeleteClickedEventHandler(this);
        toolbar.addNotificationToolbarSelectionEventHandler(this);
        toolbar.addNotificationToolbarMarkAsSeenClickedEventHandler(this);
    }

    ListStore<NotificationMessage> createListStore(NotificationMessageProperties messageProperties) {
        return new ListStore<>(messageProperties.id());
    }

    @Override
    public void onNotificationGridRefresh(NotificationGridRefreshEvent event) {
        if (listStore.size() > 0) {
            toolbar.setDeleteAllButtonEnabled(true);
        } else {
            toolbar.setDeleteAllButtonEnabled(false);
        }
    }

    @Override
    public FilterPagingLoadConfig buildDefaultLoadConfig() {
        FilterPagingLoadConfig config = new FilterPagingLoadConfigBean();
        config.setLimit(10);

        SortInfo info = new SortInfoBean("timestamp", SortDir.DESC);
        List<SortInfo> sortInfo = new ArrayList<>();
        sortInfo.add(info);
        config.setSortInfo(sortInfo);

        FilterConfig filterBean = new FilterConfigBean();
        if (!NotificationCategory.ALL.equals(currentCategory)) {
            filterBean.setField(currentCategory.toString());
        }

        List<FilterConfig> filters = new ArrayList<>();
        filters.add(filterBean);
        config.setFilters(filters);

        return config;
    }

    @Override
    public void filterBy(NotificationCategory category) {
        currentCategory = category;
        toolbar.setCurrentCategory(category);
        FilterPagingLoadConfig config = view.getCurrentLoadConfig();
        FilterConfig filterBean = new FilterConfigBean();
        if (!NotificationCategory.ALL.equals(currentCategory)) {
            filterBean.setField(currentCategory.toString());
        }

        List<FilterConfig> filters = new ArrayList<>();
        filters.add(filterBean);
        config.setFilters(filters);

        view.loadNotifications(config);

    }

    @Override
    public void onNotificationSelection(NotificationSelectionEvent event) {
        if (event.getNotifications() == null || event.getNotifications().size() == 0) {
            toolbar.setDeleteButtonEnabled(false);
            toolbar.setMarkAsSeenButtonEnabled(false);
        } else {
            toolbar.setDeleteButtonEnabled(true);
            for(NotificationMessage nm :event.getNotifications()) {
                   if(nm.isSeen()) {
                       toolbar.setMarkAsSeenButtonEnabled(false);
                       return;
                   }
            }
            toolbar.setMarkAsSeenButtonEnabled(true);
      }
    }

    @Override
    public void go(HasOneWidget container) {
        container.setWidget(view.asWidget());
        //view.setLoader(initProxyLoader());
        view.setPresenter(this);
        view.loadNotifications(null);
    }


    @Override
    public void onNotificationToolbarDeleteAllClicked(NotificationToolbarDeleteAllClickedEvent event) {
        view.mask();
        messageServiceFacade.deleteAll(currentCategory, new NotificationCallback<String>() {

            @Override
            public void onFailure(Integer statusCode, Throwable caught) {
                ErrorHandler.post(caught);
                view.unmask();
            }

            @Override
            public void onSuccess(String result) {
                view.unmask();
                view.loadNotifications(view.getCurrentLoadConfig());
                DeleteNotificationsUpdateEvent event = new DeleteNotificationsUpdateEvent(null);
                eventBus.fireEvent(event);
            }
        });
    }


    private HasUUIDs getUUIDsFromIds(String[] ids) {
        List<String> nids = Arrays.asList(ids);
        HasUUIDs hasUUIDs = factory.getHasUUIDs().as();
        hasUUIDs.setUUIDs(nids);
        return hasUUIDs;
    }

    @Override
    public void deleteNotifications(String[] ids, ReactSuccessCallback callback,
                                    ReactErrorCallback errorCallback) {
        if (ids != null && ids.length > 0) {
            messageServiceFacade.deleteMessages(getUUIDsFromIds(ids),
                                                new NotificationCallback<String>() {
                                                    @Override
                                                    public void onFailure(Integer statusCode,
                                                                          Throwable caught) {
                                                        ErrorHandler.post(appearance.notificationDeleteFail(),
                                                                          caught);
                                                        if (errorCallback != null) {
                                                            errorCallback.onError(statusCode,
                                                                                  caught.getMessage());
                                                        }
                                                    }

                                                    @Override
                                                    public void onSuccess(String result) {
                                                        if (callback != null) {
                                                            callback.onSuccess(StringQuoter.create(result));
                                                        }
                                                    }
                                                });

        }
    }

    List<String> convertNotificationsToIds(List<NotificationMessage> notifications) {
        return notifications.stream().map(NotificationMessage::getId).collect(Collectors.toList());
    }

    @Override
    public void onNotificationToolbarMarkAsSeenClicked(String[] ids,
                                                       ReactSuccessCallback callback,
                                                       ReactErrorCallback errorCallback) {
        if (ids != null && ids.length > 0) {
            messageServiceFacade.markAsSeen(getUUIDsFromIds(ids), new NotificationCallback<String>() {
                @Override
                public void onFailure(Integer statusCode, Throwable caught) {
                    announcer.schedule(new ErrorAnnouncementConfig(appearance.notificationMarkAsSeenFail()));
                    if (errorCallback != null) {
                        errorCallback.onError(statusCode, caught.getMessage());
                    }
                }

                @Override
                public void onSuccess(String result) {
                    announcer.schedule(new SuccessAnnouncementConfig(appearance.notificationMarkAsSeenSuccess()));
                    if (callback != null) {
                        callback.onSuccess(StringQuoter.create(result));
                    }

                    fireCountUpdateEvent(result);

                }
            });
        }

    }

     protected void fireCountUpdateEvent(String result) {
         final String asString =
                 StringQuoter.split(result)
                             .get("count")
                             .asString();
         final int count = Integer.parseInt(asString);
         eventBus.fireEvent(new NotificationCountUpdateEvent(
                 count));
     }

    @Override
    public void onNotificationToolbarSelection(NotificationToolbarSelectionEvent event) {
        filterBy(event.getNotificationCategory());
    }

    @Override
    public void setRefreshButton(TextButton refreshBtn) {
        if (refreshBtn != null) {
            toolbar.setRefreshButton(refreshBtn);
        }
    }

    @Override
    public void markAsRead(NotificationMessage nm) {
        nm.setSeen(true);
        view.updateStore(nm);
    }

    @Override
    public NotificationCategory getCurrentCategory() {
        return currentCategory;
    }

    @Override
    public void onJoinTeamRequestProcessed(JoinTeamRequestProcessed event) {
        NotificationMessage notification = event.getMessage();
        deleteNotifications(Lists.newArrayList(notification));
    }

    @Override
    public void getNotifications(int limit,
                                 int offset,
                                 String filter,
                                 String sortDir,
                                 NotificationsCallback callback,
                                 ReactErrorCallback errorCallback) {
        messageServiceFacade.getNotifications(limit,
                                              offset,
                                              filter,
                                              sortDir,
                                              new NotificationCallbackWrapper(errorCallback, callback));
    }

    @Override
    public void onMessageClicked(Splittable notificationMessage) {
        NotificationMessage nm = AutoBeanCodex.decode(factory, NotificationMessage.class, notificationMessage).as();
        notificationUtil.onNotificationClick(nm);
   }

    private static class NotificationCallbackWrapper
            extends org.iplantc.de.client.services.callbacks.NotificationCallbackWrapper {

        private final ReactErrorCallback errorCallback;
        private final NotificationsCallback callback;

        public NotificationCallbackWrapper(ReactErrorCallback errorCallback,
                                           NotificationsCallback callback) {
            this.errorCallback = errorCallback;
            this.callback = callback;
        }

        @Override
        public void onFailure(Integer statusCode,
                              Throwable exception) {
            if (errorCallback != null) {
                errorCallback.onError(statusCode,
                                      exception.getMessage());
            }
        }

        @Override
        public void onSuccess(String result) {
            super.onSuccess(result);
            Splittable splitResult =
                    StringQuoter.split(result);
            int total = 0;

            if (splitResult.get("total") != null) {
                total = Integer.parseInt(splitResult.get(
                        "total").asString());
            }


            Splittable sp = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(getNotifications()));

            if (callback != null) {
                callback.onFetchNotifications(sp, total);
            }
        }
    }
}
