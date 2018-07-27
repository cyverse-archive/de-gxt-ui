package org.iplantc.de.desktop.client.presenter;

import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.UserSettings;
import org.iplantc.de.client.services.UserSessionServiceFacade;
import org.iplantc.de.client.util.JsonUtil;
import org.iplantc.de.commons.client.events.LastSelectedPathChangedEvent;
import org.iplantc.de.desktop.client.DesktopView;
import org.iplantc.de.diskResource.client.events.DefaultUploadCompleteHandler;
import org.iplantc.de.diskResource.client.events.FileUploadedEvent;
import org.iplantc.de.notifications.client.events.JoinTeamRequest;
import org.iplantc.de.notifications.client.events.NotificationClickedEvent;
import org.iplantc.de.notifications.client.events.NotificationCountUpdateEvent;
import org.iplantc.de.shared.events.ServiceDown;
import org.iplantc.de.shared.events.ServiceRestored;
import org.iplantc.de.shared.events.UserLoggedOutEvent;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONObject;
import com.google.inject.Inject;

import java.util.List;

/**
 * Performs all global non-window event bus handling for the desktop presenter. Event handlers are
 * not registered until the presenter is set.
 *
 * @author jstroot
 */
public class DesktopPresenterEventHandler implements LastSelectedPathChangedEvent.LastSelectedPathChangedEventHandler,
                                                     NotificationCountUpdateEvent.NotificationCountUpdateEventHandler,
                                                     FileUploadedEvent.FileUploadedEventHandler,
                                                     UserLoggedOutEvent.UserLoggedOutEventHandler,
                                                     NotificationClickedEvent.NotificationClickedEventHandler,
                                                     ServiceDown.ServiceDownHandler,
                                                     ServiceRestored.ServiceRestoredHandler,
                                                     JoinTeamRequest.JoinTeamRequestHandler {

    @Inject EventBus eventBus;
    @Inject UserSessionServiceFacade userSessionService;
    @Inject JsonUtil jsonUtil;
    private final UserSettings userSettings;

    private DesktopPresenterImpl presenter;
    private DesktopView view;

    @Inject
    public DesktopPresenterEventHandler(final UserSettings userSettings) {
        this.userSettings = userSettings;
    }

    @Override
    public void onCountUpdate(NotificationCountUpdateEvent ncue) {
        presenter.updateNotificationCount(ncue.getTotal());
    }

    @Override
    public void onFileUploaded(FileUploadedEvent event) {

        DefaultUploadCompleteHandler duc = new DefaultUploadCompleteHandler(userSessionService,
                                                                            event.getUploadDestFolder().getPath());
        JSONObject obj = jsonUtil.getObject(event.getResponse());
        String fileJson = jsonUtil.getObject(obj, "file").toString();
        duc.onCompletion(event.getFilePath(), fileJson);
        // FIXME REFACTOR JDS move user notification posting from DefaultUpload...Handler into desktop presenter
        // FIXME REFACTOR JDS Have diskResource presenter listen to this and perform refresh

    }

    @Override
    public void onLastSelectedPathChanged(LastSelectedPathChangedEvent usue) {
        presenter.saveUserSettings(userSettings, usue.isUpdateSilently());
    }

    public void setPresenter(DesktopPresenterImpl presenter) {
        this.presenter = presenter;
        init(eventBus);
    }

    private void init(EventBus eventBus) {
        List<HandlerRegistration> handlerRegistrations = Lists.newArrayList();
        HandlerRegistration handlerRegistration = eventBus.addHandler(LastSelectedPathChangedEvent.TYPE, this);
        handlerRegistrations.add(handlerRegistration);
        handlerRegistration = eventBus.addHandler(FileUploadedEvent.TYPE, this);
        handlerRegistrations.add(handlerRegistration);
        handlerRegistration = eventBus.addHandler(NotificationCountUpdateEvent.TYPE, this);
        handlerRegistrations.add(handlerRegistration);
        handlerRegistration = eventBus.addHandler(UserLoggedOutEvent.TYPE,this);
        handlerRegistrations.add(handlerRegistration);
        handlerRegistration = eventBus.addHandler(NotificationClickedEvent.TYPE,this);
        handlerRegistrations.add(handlerRegistration);
        handlerRegistration = eventBus.addHandler(ServiceDown.TYPE, this);
        handlerRegistrations.add(handlerRegistration);
        handlerRegistration = eventBus.addHandler(ServiceRestored.TYPE, this);
        handlerRegistrations.add(handlerRegistration);
        handlerRegistration = eventBus.addHandler(JoinTeamRequest.TYPE, this);
        handlerRegistrations.add(handlerRegistration);
    }

    @Override
    public void OnLoggedOut(UserLoggedOutEvent event) {
        GWT.log("logout request received...");
        presenter.doLogout(true);
        eventBus.clearHandlers();
    }

    @Override
    public void onNotificationClicked(NotificationClickedEvent event) {
        presenter.markAsSeen(event.getMessage(), null, null);
    }

    @Override
    public void onServiceDown(ServiceDown event) {
        presenter.serviceDown(event);
    }

    @Override
    public void onServiceRestored(ServiceRestored event) {
        presenter.serviceUp(event);
    }

    @Override
    public void onJoinTeamRequestProcessed(JoinTeamRequest event) {
        presenter.onJoinTeamRequestProcessed(event.getMessage());
    }
}
