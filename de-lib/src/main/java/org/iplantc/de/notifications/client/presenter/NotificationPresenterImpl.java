package org.iplantc.de.notifications.client.presenter;

import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.HasId;
import org.iplantc.de.client.models.notifications.Notification;
import org.iplantc.de.client.models.notifications.NotificationCategory;
import org.iplantc.de.client.models.notifications.NotificationMessage;
import org.iplantc.de.client.services.MessageServiceFacade;
import org.iplantc.de.client.services.callbacks.NotificationCallbackWrapper;
import org.iplantc.de.client.util.JsonUtil;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;
import org.iplantc.de.notifications.client.events.DeleteNotificationsUpdateEvent;
import org.iplantc.de.notifications.client.events.NotificationCountUpdateEvent;
import org.iplantc.de.notifications.client.events.NotificationGridRefreshEvent;
import org.iplantc.de.notifications.client.events.NotificationSelectionEvent;
import org.iplantc.de.notifications.client.events.NotificationToolbarDeleteAllClickedEvent;
import org.iplantc.de.notifications.client.events.NotificationToolbarDeleteClickedEvent;
import org.iplantc.de.notifications.client.events.NotificationToolbarMarkAsSeenClickedEvent;
import org.iplantc.de.notifications.client.events.NotificationToolbarSelectionEvent;
import org.iplantc.de.notifications.client.gin.factory.NotificationViewFactory;
import org.iplantc.de.notifications.client.model.NotificationMessageProperties;
import org.iplantc.de.notifications.client.views.NotificationToolbarView;
import org.iplantc.de.notifications.client.views.NotificationView;
import org.iplantc.de.shared.NotificationCallback;

import com.google.common.collect.Lists;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.SortInfoBean;
import com.sencha.gxt.data.shared.loader.FilterConfig;
import com.sencha.gxt.data.shared.loader.FilterConfigBean;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfigBean;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.button.TextButton;

import java.util.ArrayList;
import java.util.List;

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
                                                  NotificationToolbarMarkAsSeenClickedEvent.NotificationToolbarMarkAsSeenClickedEventHandler
                                                 {

    private final class NotificationsServiceCallback extends NotificationCallbackWrapper {
        private final AsyncCallback<PagingLoadResult<NotificationMessage>> callback;
        private final PagingLoadConfig loadConfig;

        private NotificationsServiceCallback(PagingLoadConfig loadConfig,
                                             AsyncCallback<PagingLoadResult<NotificationMessage>> callback) {
            this.loadConfig = loadConfig;
            this.callback = callback;
        }

        @Override
        public void onFailure(Integer statusCode, Throwable caught) {
            ErrorHandler.post(caught);
        }

        @Override
        public void onSuccess(String result) {
            super.onSuccess(result);
            Splittable splitResult = StringQuoter.split(result);
            int total = 0;

            if (splitResult.get("total") != null) {
                total = Integer.parseInt(splitResult.get("total").asString());
            }

            List<NotificationMessage> messages = Lists.newArrayList();
            for (Notification n : this.getNotifications()) {
                messages.add(n.getMessage());
            }

            PagingLoadResult<NotificationMessage> callbackResult =
                    new PagingLoadResultBean<>(messages, total, loadConfig.getOffset());
            callback.onSuccess(callbackResult);
       }
    }
    private final ListStore<NotificationMessage> listStore;
    private final NotificationToolbarView toolbar;
    private final NotificationView view;
    private NotificationView.NotificationViewAppearance appearance;
    NotificationCategory currentCategory;
    @Inject MessageServiceFacade messageServiceFacade;
    @Inject JsonUtil jsonUtil;
    @Inject EventBus eventBus;
    @Inject IplantAnnouncer announcer;

    @Inject
    public NotificationPresenterImpl(final NotificationViewFactory viewFactory,
                                     NotificationView.NotificationViewAppearance appearance,
                                     NotificationToolbarView toolbar,
                                     NotificationMessageProperties messageProperties) {
        this.appearance = appearance;
        this.listStore = createListStore(messageProperties);
        this.view = viewFactory.create(listStore);
        currentCategory = NotificationCategory.ALL;
        this.toolbar = toolbar;
        view.setNorthWidget(toolbar);

        setRefreshButton(view.getRefreshButton());
        addEventHandlers();
    }

    private void addEventHandlers() {
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
        view.setLoader(initProxyLoader());
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

    @Override
    public void onNotificationToolbarDeleteClicked(NotificationToolbarDeleteClickedEvent event) {
        final List<NotificationMessage> notifications = view.getSelectedItems();
        final Command callback = new Command() {
            @Override
            public void execute() {
                view.loadNotifications(view.getCurrentLoadConfig());
            }
        };
        // do we have any notifications to delete?
        if (notifications != null && !notifications.isEmpty()) {
            JSONObject obj = new JSONObject();
            JSONArray arr = new JSONArray();
            int i = 0;
            for (NotificationMessage n : notifications) {
                arr.set(i++, new JSONString(n.getId()));
            }
            obj.put("uuids", arr);

            messageServiceFacade.deleteMessages(obj, new NotificationCallback<String>() {
                @Override
                public void onFailure(Integer statusCode, Throwable caught) {
                    ErrorHandler.post(appearance.notificationDeleteFail(), caught);
                }

                @Override
                public void onSuccess(String result) {
                    callback.execute();
                    DeleteNotificationsUpdateEvent event = new DeleteNotificationsUpdateEvent(notifications);
                    eventBus.fireEvent(event);
                }
            });
        }
    }

    @Override
    public void onNotificationToolbarMarkAsSeenClicked(NotificationToolbarMarkAsSeenClickedEvent event) {
        final List<NotificationMessage> notifications = view.getSelectedItems();
        List<HasId> ids = new ArrayList<>();
        //StringQuoter.create("max").assign(hasChild, "score_mode");
        for (NotificationMessage n : notifications) {
            ids.add(n);
        }

        messageServiceFacade.markAsSeen(ids, new NotificationCallback<String>() {
            @Override
            public void onFailure(Integer statusCode, Throwable caught) {
               announcer.schedule(new ErrorAnnouncementConfig(
                                       appearance.notificationMarkAsSeenFail()));
            }

            @Override
            public void onSuccess(String result) {
               announcer.schedule(new SuccessAnnouncementConfig(appearance.notificationMarkAsSeenSuccess()));
                for (NotificationMessage nm : notifications) {
                    nm.setSeen(true);
                    view.updateStore(nm);
                }

                fireCountUpdateEvent(result);

            }
        });
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

    private PagingLoader<FilterPagingLoadConfig, PagingLoadResult<NotificationMessage>> initProxyLoader() {
     RpcProxy<FilterPagingLoadConfig, PagingLoadResult<NotificationMessage>> proxy = new RpcProxy<FilterPagingLoadConfig, PagingLoadResult<NotificationMessage>>() {
            @Override
            public void load(final FilterPagingLoadConfig loadConfig,
                             final AsyncCallback<PagingLoadResult<NotificationMessage>> callback) {
        messageServiceFacade.getNotifications(loadConfig.getLimit(),
                                              loadConfig.getOffset(),
                                              (loadConfig.getFilters().get(0).getField()) == null ?
                                              "" :
                                              loadConfig.getFilters().get(0).getField().toLowerCase(),
                                              loadConfig.getSortInfo().get(0).getSortDir().toString(),
                                              new NotificationsServiceCallback(loadConfig, callback));
            }

     };

        final PagingLoader<FilterPagingLoadConfig, PagingLoadResult<NotificationMessage>> loader =
                new PagingLoader<>(proxy);
        loader.setRemoteSort(true);
        loader.addLoadHandler(new LoadResultListStoreBinding<FilterPagingLoadConfig, NotificationMessage, PagingLoadResult<NotificationMessage>>(
                listStore));
        loader.useLoadConfig(buildDefaultLoadConfig());
        return loader;
    }
}
