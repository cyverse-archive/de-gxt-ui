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
import org.iplantc.de.notifications.client.events.NotificationCountUpdateEvent;
import org.iplantc.de.notifications.client.utils.NotificationUtil;
import org.iplantc.de.notifications.client.views.NotificationView;
import org.iplantc.de.shared.NotificationCallback;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

import java.util.Arrays;
import java.util.List;

/**
 * A presenter for notification window
 *
 * @author sriram
 */
public class NotificationPresenterImpl implements NotificationView.Presenter {

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
    NotificationCategory category;

    @Inject
    public NotificationPresenterImpl(NotificationView.NotificationViewAppearance appearance,
                                     NotificationAutoBeanFactory factory,
                                     NotificationView view,
                                     EventBus eventBus) {
        this.appearance = appearance;
        this.view = view;
        this.factory = factory;
        this.eventBus = eventBus;
        currentCategory = NotificationCategory.ALL;
    }
    @Override
    public void go(HasOneWidget container, String baseDebugId, NotificationCategory category) {
        container.setWidget(view.asWidget());
        this.category = category;
        view.setPresenter(this, baseDebugId);
        view.loadNotifications(category);
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
    public void getNotifications(int limit,
                                 int offset,
                                 String filter,
                                 String sortDir,
                                 NotificationsCallback callback,
                                 ReactErrorCallback errorCallback) {
        this.category = NotificationCategory.fromTypeString(filter);
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

   @Override
   public NotificationCategory getCurrentCategory() {
        return category != null ? category : NotificationCategory.ALL;
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
