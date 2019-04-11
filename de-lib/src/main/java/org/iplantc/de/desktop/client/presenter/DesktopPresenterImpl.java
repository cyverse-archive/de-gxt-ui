package org.iplantc.de.desktop.client.presenter;

import org.iplantc.de.client.DEClientConstants;
import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.HasPath;
import org.iplantc.de.client.models.IsHideable;
import org.iplantc.de.client.models.QualifiedId;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.UserSettings;
import org.iplantc.de.client.models.WindowState;
import org.iplantc.de.client.models.WindowType;
import org.iplantc.de.client.models.analysis.AnalysesAutoBeanFactory;
import org.iplantc.de.client.models.diskResources.DiskResourceAutoBeanFactory;
import org.iplantc.de.client.models.diskResources.File;
import org.iplantc.de.client.models.notifications.Notification;
import org.iplantc.de.client.models.notifications.NotificationAutoBeanFactory;
import org.iplantc.de.client.models.notifications.NotificationCategory;
import org.iplantc.de.client.models.notifications.NotificationMessage;
import org.iplantc.de.client.services.DEUserSupportServiceFacade;
import org.iplantc.de.client.services.FileEditorServiceFacade;
import org.iplantc.de.client.services.MessageServiceFacade;
import org.iplantc.de.client.services.UserSessionServiceFacade;
import org.iplantc.de.client.services.callbacks.ReactErrorCallback;
import org.iplantc.de.client.util.CommonModelUtils;
import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.client.util.JsonUtil;
import org.iplantc.de.client.util.WebStorageUtil;
import org.iplantc.de.commons.client.CommonUiConstants;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;
import org.iplantc.de.commons.client.util.WindowUtil;
import org.iplantc.de.commons.client.views.dialogs.IplantErrorDialog;
import org.iplantc.de.commons.client.views.window.configs.AppWizardConfig;
import org.iplantc.de.commons.client.views.window.configs.AppsWindowConfig;
import org.iplantc.de.commons.client.views.window.configs.ConfigAutoBeanFactory;
import org.iplantc.de.commons.client.views.window.configs.ConfigFactory;
import org.iplantc.de.commons.client.views.window.configs.DiskResourceWindowConfig;
import org.iplantc.de.commons.client.views.window.configs.SavedWindowConfig;
import org.iplantc.de.commons.client.views.window.configs.WindowConfig;
import org.iplantc.de.desktop.client.DesktopView;
import org.iplantc.de.desktop.client.events.WindowHeadingUpdatedEvent;
import org.iplantc.de.desktop.client.presenter.callbacks.NotificationMarkAsSeenCallback;
import org.iplantc.de.desktop.client.presenter.util.MessagePoller;
import org.iplantc.de.desktop.client.presenter.util.WindowStateStorageWrapper;
import org.iplantc.de.desktop.client.views.widgets.PreferencesDialog;
import org.iplantc.de.desktop.client.views.windows.WindowBase;
import org.iplantc.de.desktop.client.views.windows.WindowInterface;
import org.iplantc.de.fileViewers.client.callbacks.LoadGenomeInCoGeCallback;
import org.iplantc.de.intercom.client.IntercomFacade;
import org.iplantc.de.notifications.client.utils.NotificationUtil;
import org.iplantc.de.notifications.client.utils.NotifyInfo;
import org.iplantc.de.shared.AppsCallback;
import org.iplantc.de.shared.AsyncProviderWrapper;
import org.iplantc.de.shared.DEProperties;
import org.iplantc.de.shared.NotificationCallback;
import org.iplantc.de.shared.events.ServiceDown;
import org.iplantc.de.shared.events.ServiceRestored;
import org.iplantc.de.shared.services.PropertyServiceAsync;

import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

import com.sencha.gxt.core.client.util.KeyNav;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.WindowManager;
import com.sencha.gxt.widget.core.client.box.AutoProgressMessageBox;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RegisterEvent;
import com.sencha.gxt.widget.core.client.event.UnregisterEvent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author jstroot
 */
public class DesktopPresenterImpl implements DesktopView.Presenter,
                                             UnregisterEvent.UnregisterHandler<Widget>,
                                             RegisterEvent.RegisterHandler<Widget> {

	interface AuthErrors {
        String API_NAME = "api_name";
        String ERROR = "error";
        String ERROR_DESCRIPTION = "error_description";
    }

    public interface QueryStrings {
        String APP_CATEGORY = "app-category";
        String FOLDER = "folder";
        String TYPE = "type";
        String SYSTEM_ID = "system-id";
        String APP_ID = "app-id";
        String STATE = "state";
    }

    public interface TypeQueryValues {
        String APPS = "apps";
        String DATA = "data";
    }

    private class HeadingUpdatedEventHandler
            implements WindowHeadingUpdatedEvent.WindowHeadingUpdatedEventHandler {
        @Override
        public void onWindowHeadingUpdated(WindowHeadingUpdatedEvent event) {
            DesktopPresenterImpl.this.buildWindowConfigList();
            DesktopPresenterImpl.this.view.renderView(windowConfigMap);
        }
    }

    final DesktopWindowManager desktopWindowManager;
    @Inject IplantAnnouncer announcer;
    @Inject CommonUiConstants commonUiConstants;
    @Inject DEClientConstants deClientConstants;
    @Inject DEProperties deProperties;
    @Inject Provider<ErrorHandler> errorHandlerProvider;
    @Inject AsyncProviderWrapper<DEUserSupportServiceFacade> userSupportServiceProvider;
    @Inject AsyncProviderWrapper<FileEditorServiceFacade> fileEditorServiceProvider;
    @Inject MessageServiceFacade messageServiceFacade;
    @Inject NotificationAutoBeanFactory notificationFactory;
    @Inject DiskResourceAutoBeanFactory diskResourceFactory;
    @Inject AnalysesAutoBeanFactory analysesFactory;
    @Inject
    ConfigAutoBeanFactory configAutoBeanFactory;
    @Inject PropertyServiceAsync propertyServiceFacade;
    @Inject UserInfo userInfo;
    @Inject UserSessionServiceFacade userSessionService;
    @Inject UserSettings userSettings;
    @Inject NotifyInfo notifyInfo;
    @Inject DiskResourceUtil diskResourceUtil;
    @Inject JsonUtil jsonUtil;
    @Inject NotificationUtil notificationUtil;
    @Inject AsyncProviderWrapper<PreferencesDialog> preferencesDialogProvider;
    private DesktopPresenterAppearance appearance;

    private final EventBus eventBus;
    private final MessagePoller messagePoller;
    private final SaveSessionPeriodic ssp;
    private SaveWindowStatesPeriodic swsp;
    private final DesktopView view;
    private boolean loggedOut;
    private final WindowManager windowManager;
    private Map<Splittable, WindowBase> windowConfigMap = new HashMap<>();

    Logger LOG = Logger.getLogger(DesktopPresenterImpl.class.getName());

    public static final int NEW_NOTIFICATION_LIMIT = 10;

    @Inject
    public DesktopPresenterImpl(final DesktopView view,
                                final DesktopPresenterEventHandler globalEventHandler,
                                final DesktopPresenterWindowEventHandler windowEventHandler,
                                final EventBus eventBus,
                                final WindowManager windowManager,
                                final DesktopWindowManager desktopWindowManager,
                                final MessagePoller messagePoller,
                                final DesktopPresenterAppearance appearance) {
        this.view = view;
        this.eventBus = eventBus;
        this.windowManager = windowManager;
        this.messagePoller = messagePoller;
        this.desktopWindowManager = desktopWindowManager;
        this.appearance = appearance;
        this.ssp = new SaveSessionPeriodic(this, appearance, 8);
        this.loggedOut = false;
        this.view.setPresenter(this);
        this.view.renderView(windowConfigMap);
        globalEventHandler.setPresenter(this);
        windowEventHandler.setPresenter(this, desktopWindowManager);
        windowManager.addRegisterHandler(this);
        windowManager.addUnregisterHandler(this);
    }

    @Override
    public void onRegister(RegisterEvent<Widget> event) {
        if (event.getItem() instanceof WindowInterface) {
            WindowBase wb = (WindowBase)event.getItem();
            wb.asWidget().addHandler(new HeadingUpdatedEventHandler(), WindowHeadingUpdatedEvent.TYPE);
            buildWindowConfigList();
            view.renderView(windowConfigMap);
        }

    }

    @Override
    public void onUnregister(UnregisterEvent<Widget> event) {
        if (event.getItem() instanceof WindowInterface) {
            buildWindowConfigList();
            view.renderView(windowConfigMap);
        }
    }

    private void buildWindowConfigList() {
        List<Widget> widgets = windowManager.getStack();
        windowConfigMap.clear();
        if (widgets.size() == 0) {
            return;
        }
        for (Widget w : widgets) {
            if (w instanceof WindowInterface) {
                WindowBase cyverseWin = (WindowBase)w;
                windowConfigMap.put(getConfigAsSplittable(cyverseWin), cyverseWin);
            }
        }
    }

    private Splittable getConfigAsSplittable(WindowBase win) {
        WindowConfig config = win.getWindowConfig();
        config.setMinimized(win.isMinimized());
        config.setWindowTitle(win.getHeader().getSafeHtml().asString());
        Splittable sp = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(config));
        return sp;
    }

    private WindowBase getWindowFromConfig(Splittable config) {
        Iterator it = windowConfigMap.keySet().iterator();
        while (it.hasNext()) {
            Splittable sp = (Splittable)it.next();
            if (sp.get("tag") != null && sp.get("tag").asString().equals(config.get("tag").asString())) {
                return windowConfigMap.get(sp);
            }
        }
        return null;
    }


    @Override
    public void displayNotificationPopup(String message, String category, String analysisStatus) {
        if (NotificationCategory.ANALYSIS.equals(NotificationCategory.fromTypeString(category))) {
            if ("Failed".equals(analysisStatus)) { //$NON-NLS-1$
                notifyInfo.displayWarning(message);
            } else {
                notifyInfo.display(message);
            }
        } else {
            notifyInfo.display(message);
        }
    }

   @Override
    public void doLogout(boolean sessionTimeout) {
       cleanUp();
       IntercomFacade.logout();

        //session is timed-out, following rpc call will fail and cause 401.
        if(!sessionTimeout) {
            userSessionService.logout(new RuntimeCallbacks.LogoutCallback(userSessionService,
                                                                          deClientConstants,
                                                                          userSettings,
                                                                          appearance,
                                                                          getOrderedWindowConfigs()));
        } else {
            final String redirectUrl = GWT.getHostPageBaseURL() + deClientConstants.logoutUrl();
            LOG.info("Session timeout.  Redirect url: " + redirectUrl);
            Window.Location.assign(redirectUrl);
        }
    }

    public void onBootstrapError(Integer statusCode, String errorMessage) {
        String redirectUrl = GWT.getHostPageBaseURL() + deClientConstants.errorUrl();
        if (statusCode != null) {
            redirectUrl += "-" + statusCode.toString();
        }
        if (errorMessage != null) {
            JSONObject json = jsonUtil.getObject(errorMessage);
            String errorString = json.keySet().stream()
                                     .map(key -> URL.encode(key) + "=" + URL.encode(json.get(key).toString()))
                                     .collect(Collectors.joining("&"));
            redirectUrl += "?" + URL.encode(errorString);
        }
        Window.Location.assign(redirectUrl);
    }

    private void cleanUp() {
        loggedOut = true;
        messagePoller.stop();
    }

    @Override
    public void doMarkAllSeen(final boolean announce,
                              final NotificationMarkAsSeenCallback callback,
                              final ReactErrorCallback errorCallback) {
       messageServiceFacade.markAllNotificationsSeen(new NotificationCallback<Void>() {
           @Override
           public void onFailure(Integer statusCode, Throwable caught) {
               errorHandlerProvider.get().post(caught);
               if (errorCallback != null) {
                   errorCallback.onError(statusCode, caught.getMessage());
               }
           }

           @Override
           public void onSuccess(Void result) {
               if (callback != null) {
                   callback.onMarkSeen(0);
               }
               if (announce) {
                   announcer.schedule(new SuccessAnnouncementConfig(appearance.markAllAsSeenSuccess(),
                                                                    true,
                                                                    3000));
               }
           }
       });
    }

    @Override
    public void doSeeAllNotifications() {
        show(ConfigFactory.notifyWindowConfig(NotificationCategory.ALL), true);
    }

    @Override
    public void doSeeNewNotifications() {
        show(ConfigFactory.notifyWindowConfig(NotificationCategory.NEW), true);
    }

    public void doViewGenomes(final File file) {
        JSONObject obj = new JSONObject();
        JSONArray pathArr = new JSONArray();
        pathArr.set(0, new JSONString(file.getPath()));
        obj.put("paths", pathArr);
        fileEditorServiceProvider.get(new AsyncCallback<FileEditorServiceFacade>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(FileEditorServiceFacade result) {
                result.loadGenomesInCoge(obj, new LoadGenomeInCoGeCallback(null));
            }
        });
    }

    @Override
    public void setDesktopContainer(Element desktopContainer) {
        desktopWindowManager.setDesktopContainer(desktopContainer);
    }

    @Override
    public List<SavedWindowConfig> getOrderedWindowConfigs() {
        List<SavedWindowConfig> windowConfigs = Lists.newArrayList();
        for (Widget w : windowManager.getStack()) {
            if (w instanceof WindowInterface) {
                SavedWindowConfig savedWindowConfig = configAutoBeanFactory.savedWindowConfig().as();
                WindowConfig wc = ((WindowInterface) w).getWindowConfig();
                savedWindowConfig.setWindowType(wc.getWindowType());
                savedWindowConfig.setWindowConfig(AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(wc)));
                windowConfigs.add(savedWindowConfig);
            }
        }
        return Collections.unmodifiableList(windowConfigs);
    }

    @Override
    public List<WindowState> getWindowStates() {
       List<WindowState> windowStates  = Lists.newArrayList();
        for (Widget w : windowManager.getStack()) {
            if (w instanceof WindowInterface) {
                windowStates.add(((WindowInterface)w).createWindowState());
            }
        }

        return windowStates;
    }

    @Override
    public void go(final Panel panel) {
        // Fetch DE properties, the rest of DE initialization is performed in callback
        propertyServiceFacade.getProperties(new InitializationCallbacks.PropertyServiceCallback(deProperties,
                                                                        userInfo,
                                                                        userSettings,
                                                                        userSessionService,
                                                                        errorHandlerProvider,
                                                                        appearance,
                                                                        announcer,
                                                                        panel,
                                                                        this));
        //make sure we have new structure for de storage. Else reset and start fresh
        if (!WindowStateStorageWrapper.isBootStraped()) {
            WebStorageUtil.clear(WindowStateStorageWrapper.LOCAL_STORAGE_PREFIX);
            WindowStateStorageWrapper.bootstrap();
        }
    }

    public void serviceDown(ServiceDown event) {
        desktopWindowManager.serviceDown(event);
    }

    public void serviceUp(ServiceRestored event) {
        desktopWindowManager.serviceUp(event.getWindowTypes());
    }

    @Override
    public void onAboutClick() {
        desktopWindowManager.show(WindowType.ABOUT);
    }

    @Override
    public void onAnalysesWinBtnSelect() {
        desktopWindowManager.show(WindowType.ANALYSES);
    }

    @Override
    public void onAppsWinBtnSelect() {
        desktopWindowManager.show(WindowType.APPS);
    }

    @Override
    public void onCollaboratorsClick() {
        desktopWindowManager.show(WindowType.COLLABORATORS);
    }

    @Override
    public void onTeamsClick() {
        desktopWindowManager.show(WindowType.TEAMS);
    }

    @Override
    public void onCommunitiesClick() {
        desktopWindowManager.show(WindowType.COMMUNITIES);
    }

    @Override
    public void onPreferencesClick() {
        preferencesDialogProvider.get(new AsyncCallback<PreferencesDialog>() {
            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(final PreferencesDialog dialog) {
                dialog.show(DesktopPresenterImpl.this, userSettings);
            }
	});
    }

    @Override
    public void onContactSupportClick() {
        WindowUtil.open(commonUiConstants.supportUrl());
    }

    @Override
    public void onDataWinBtnSelect() {
        desktopWindowManager.show(WindowType.DATA);
    }

    @Override
    public void onDocumentationClick() {
        WindowUtil.open(deClientConstants.deHelpFile());
    }

    @Override
    public void onForumsBtnSelect() {
        WindowUtil.open(commonUiConstants.forumsUrl());
    }

    @Override
    public void onFeedbackSelect() {
        view.onFeedbackBtnSelect();
    }

    /**
     * FIXME REFACTOR JDS Create notifications module and move this implementation there
     */
    @Override
    @SuppressWarnings("unusable-by-js")
    public void onNotificationSelected(Splittable notification,
                                       final NotificationMarkAsSeenCallback callback,
                                       final ReactErrorCallback errorCallback) {
        GWT.log(notification.getPayload());
        Notification n = AutoBeanCodex.decode(notificationFactory, Notification.class, notification).as();
        NotificationMessage nm = notificationUtil.getMessage(n, notificationFactory);
        notificationUtil.onNotificationClick(nm);
        markAsSeen(nm, callback, errorCallback);
    }

    public void markAsSeen(final NotificationMessage selectedItem,
                           final NotificationMarkAsSeenCallback callback,
                           final ReactErrorCallback errorCallback) {
        messageServiceFacade.markAsSeen(selectedItem, new NotificationCallback<String>() {
            @Override
            public void onFailure(Integer statusCode, Throwable caught) {
                errorHandlerProvider.get().post(caught);
                if (errorCallback != null) {
                    errorCallback.onError(statusCode, caught.getMessage());
                }
            }

            @Override
            public void onSuccess(String result) {
                if (callback != null) {
                    final String asString = StringQuoter.split(result).get("count").asString();
                    final int count = Integer.parseInt(asString);
                    callback.onMarkSeen(count);
                }

            }
        });
    }

    //TODO: SS Port this over to React Desktop. For now the Join Team Notification will remain in
    // Notifications List. I confirmed with Sarah that Join team operation is idempotent.
    @Override
    public void onJoinTeamRequestProcessed(NotificationMessage message) {
    /*   view.getNotificationStore().remove(message);
        //If the notifications window is open, the NotificationPresenter will handle this
         if (!desktopWindowManager.isOpen(WindowType.NOTIFICATIONS)) {
            HasUUIDs hasUUIDs = notificationFactory.getHasUUIDs().as();
            hasUUIDs.setUUIDs(Lists.newArrayList(message.getId()));
            messageServiceFacade.deleteMessages(hasUUIDs, new NotificationCallback<String>() {
                @Override
                public void onFailure(Integer statusCode, Throwable exception) {
                    ErrorHandler.post(exception);
                }

                @Override
                public void onSuccess(String result) {
                    //do nothing intentionally
                }
            });
        }*/
    }

    @Override
    @SuppressWarnings("unusable-by-js")
    public void onTaskButtonClicked(Splittable windowConfig) {
        WindowBase win = getWindowFromConfig(windowConfig);
        if (win != null) {
            if (win.isMinimized()) {
                win.show();
            } else if (((com.sencha.gxt.widget.core.client.Window)windowManager.getActive()).equals(win.asWindow())) {
                win.minimize();
            } else {
                win.toFront();
            }
        }
        buildWindowConfigList();
        view.renderView(windowConfigMap);
    }

    @Override
    public void updateNotificationCount(int unseeen_count) {
      view.renderView(unseeen_count, windowConfigMap);
    }

    @Override
    public void onIntroClick() {
        view.renderView(true, -1, windowConfigMap);
    }

    @Override
    public void saveUserSettings(final UserSettings value,
                                 final boolean updateSilently) {
        AppsCallback hookCallback = new AppsCallback<Void>() {
            @Override
            public void onFailure(Integer statusCode, Throwable exception) {
                announcer.schedule(new ErrorAnnouncementConfig(appearance.webhookSaveError(),
                                                               true,
                                                               3000));
            }

            @Override
            public void onSuccess(Void result) {
                //Do Nothing
            }
        };
        final RuntimeCallbacks.SaveUserSettingsCallback callback = new RuntimeCallbacks.SaveUserSettingsCallback(value,
                                                                               userSettings,
                                                                               announcer,
                                                                               this,
                                                                               appearance,
                                                                               updateSilently,
                                                                               userSessionService);
        userSessionService.saveUserPreferences(value.getUserSetting(), callback, hookCallback);
    }

    @Override
    public void show(final WindowConfig config) {
        desktopWindowManager.show(config, false);
    }

    @Override
    public void show(final WindowConfig config,
                     final boolean updateExistingWindow) {
        desktopWindowManager.show(config, updateExistingWindow);
    }

    @Override
    public void submitUserFeedback(final Splittable splittable, final IsHideable isHideable) {
        userSupportServiceProvider.get(new AsyncCallback<DEUserSupportServiceFacade>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(DEUserSupportServiceFacade result) {
                result.submitSupportRequest(splittable, new AsyncCallback<Void>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        errorHandlerProvider.get().post(appearance.feedbackServiceFailure(), caught);
                    }

                    @Override
                    public void onSuccess(Void result) {
                        isHideable.hide();
                        announcer.schedule(new SuccessAnnouncementConfig(appearance.feedbackSubmitted(),
                                                                         true,
                                                                         3000));
                    }
                });
            }
        });
    }

    @Override
    public void stickWindowToTop(com.sencha.gxt.widget.core.client.Window window) {
        desktopWindowManager.stickWindowToTop(window);
    }

    @Override
    public void onFaqSelect() {
        WindowUtil.open(commonUiConstants.faqUrl());
    }

    @Override
    public void doPeriodicSessionSave() {
        if (userSettings.hasUserSessionConnection() && userSettings.isSaveSession()) {
            ssp.run();
            messagePoller.addTask(ssp);
            // start if not started...
            messagePoller.start();
        } else {
            messagePoller.removeTask(ssp);
        }
    }

    @Override
    public void doPeriodicWindowStateSave() {
        swsp = new SaveWindowStatesPeriodic(this, userInfo);
        swsp.run();
        messagePoller.addTask(swsp);
        messagePoller.start();
    }


    void postBootstrap(final Panel panel) {
        setBrowserContextMenuEnabled(deProperties.isContextClickEnabled());
        messagePoller.start();
        initKBShortCuts();
        panel.add(view);
        checkDataQueryString();
        doPeriodicWindowStateSave();
        doPeriodicSessionSave();
    }

    @Override
    public void getNotifications(NotificationsCallback callback, ReactErrorCallback errorCallback) {
        messageServiceFacade.getRecentMessages(new InitializationCallbacks.GetInitialNotificationsCallback(
                view,
                appearance,
                announcer,
                callback,
                errorCallback));
    }

    @Override
    public void restoreWindows(List<SavedWindowConfig> savedWindowConfigs) {
        if (savedWindowConfigs != null && savedWindowConfigs.size() > 0) {
            for (SavedWindowConfig wc : savedWindowConfigs) {
                desktopWindowManager.show(wc);
            }
        }
    }

    private void getUserSession() {
        // do not attempt to get user session for new user
        if (userSettings.isSaveSession() && !userInfo.isNewUser()) {
            // This restoreSession's callback will also init periodic session saving.
            final AutoProgressMessageBox progressMessageBox = new AutoProgressMessageBox(appearance.loadingSession(),
                                                                                         appearance.loadingSessionWaitNotice());
            progressMessageBox.getProgressBar().setDuration(1000);
            progressMessageBox.getProgressBar().setInterval(100);
            progressMessageBox.auto();
            final Request req = userSessionService.getUserSession(new RuntimeCallbacks.GetUserSessionCallback(progressMessageBox,
                                                                                                              appearance,
                                                                                                              announcer,
                                                                                                              this));
            progressMessageBox.addDialogHideHandler(new DialogHideEvent.DialogHideHandler() {
                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (Dialog.PredefinedButton.CANCEL.equals(event.getHideButton())) {
                        req.cancel();
                        SafeHtml msg = appearance.sessionRestoreCancelled();
                        announcer.schedule(new SuccessAnnouncementConfig(msg, true, 5000));
                    }
                }
            });
        } else {
            checkRemainingQueryStrings();
        }
    }

    void initKBShortCuts() {
        new KeyNav(RootPanel.get()) {
            @Override
            public void handleEvent(NativeEvent event) {
                if (event.getCtrlKey() && event.getShiftKey()) {
                    final String keycode = String.valueOf((char)event.getKeyCode());
                    if (userSettings.getDataShortCut().equals(keycode)) {
                        show(ConfigFactory.diskResourceWindowConfig(true));
                    } else if (userSettings.getAnalysesShortCut().equals(keycode)) {
                        show(ConfigFactory.analysisWindowConfig());
                    } else if (userSettings.getAppsShortCut().equals(keycode)) {
                        show(ConfigFactory.appsWindowConfig());
                    } else if (userSettings.getNotifyShortCut().equals(keycode)) {
                        show(ConfigFactory.notifyWindowConfig(NotificationCategory.ALL));
                    } else if (userSettings.getCloseShortCut().equals(keycode)) {
                        desktopWindowManager.closeActiveWindow();
                    }
                }
            }
        };
    }

    private String getSystemId(final String systemId) {
        return Strings.isNullOrEmpty(systemId) ? deClientConstants.deSystemId() : systemId;
    }

    void checkDataQueryString() {
        Map<String, List<String>> params = Window.Location.getParameterMap();
        Set<String> keys = params.keySet();
        boolean hasTypeKeys = keys.stream().anyMatch(QueryStrings.TYPE::equalsIgnoreCase);
        boolean hasDataType = hasTypeKeys && params.get(QueryStrings.TYPE).stream().anyMatch(TypeQueryValues.DATA::equalsIgnoreCase);

        if (hasDataType) {
            DiskResourceWindowConfig drConfig = ConfigFactory.diskResourceWindowConfig(true);
            drConfig.setMaximized(true);
            // If user has multiple folder parameters, the last one will be used.
            String folderParameter = Window.Location.getParameter(QueryStrings.FOLDER);
            String selectedFolder = URL.decode(Strings.nullToEmpty(folderParameter));

            if (!Strings.isNullOrEmpty(selectedFolder)) {
                HasPath folder = CommonModelUtils.getInstance().createHasPathFromString(selectedFolder);
                drConfig.setSelectedFolder(folder);
            }
            show(drConfig);
        } else {
            getUserSession();
        }
    }

    void showAuthErrors(Map<String, List<String>> params) {
        // Remove underscores, and upper case whole error
        String upperCaseError = Iterables.getFirst(params.get(AuthErrors.ERROR), "").replaceAll("_", " ").toUpperCase();
        String apiName = Strings.nullToEmpty(Window.Location.getParameter(AuthErrors.API_NAME));
        String titleApi = apiName.isEmpty() ? "" : " : " + apiName;
        IplantErrorDialog errorDialog = new IplantErrorDialog(upperCaseError + titleApi,
                                                              Window.Location.getParameter(AuthErrors.ERROR_DESCRIPTION));
        errorDialog.show();
    }

    public void checkRemainingQueryStrings() {
        Map<String, List<String>> params = Window.Location.getParameterMap();
        Set<String> keys = params.keySet();
        boolean hasTypeKeys = keys.stream().anyMatch(QueryStrings.TYPE::equalsIgnoreCase);
        boolean hasStateKey = keys.stream().anyMatch(QueryStrings.STATE::equalsIgnoreCase);
        boolean hasAppsType = hasTypeKeys && params.get(QueryStrings.TYPE).stream().anyMatch(TypeQueryValues.APPS::equalsIgnoreCase);
        boolean hasError = keys.stream().anyMatch(AuthErrors.ERROR::equalsIgnoreCase);

        if (hasAppsType) {
            final AppsWindowConfig appsConfig = ConfigFactory.appsWindowConfig();
            final String appCategoryId = Window.Location.getParameter(QueryStrings.APP_CATEGORY);
            final String systemId = getSystemId(Window.Location.getParameter(QueryStrings.SYSTEM_ID));
            final String appId = Window.Location.getParameter(QueryStrings.APP_ID);
            if (!Strings.isNullOrEmpty(appCategoryId)) {
                appsConfig.setSelectedAppCategory(new QualifiedId(systemId, appCategoryId));
                show(appsConfig);
            } else if (!Strings.isNullOrEmpty(appId)) {
                AppWizardConfig config = ConfigFactory.appWizardConfig("", systemId, appId);
                show(config);
            }
        }
        if (hasStateKey) {
            WebStorageUtil.clear(WindowStateStorageWrapper.LOCAL_STORAGE_PREFIX);
        }
        if (hasError) {
            showAuthErrors(params);
        }
    }

    void setBrowserContextMenuEnabled(boolean enabled){
        setBrowserContextMenuEnabledNative(enabled);
    }

    /**
     * Disable the context menu of the browser using native JavaScript.
     * <p/>
     * This disables the user's ability to right-click on this widget and get the browser's context menu
     */
    private native void setBrowserContextMenuEnabledNative(boolean enabled)
    /*-{
		$doc.oncontextmenu = function() {
			return enabled;
		};
    }-*/;

    @Override
    public void setUserSessionConnection(boolean connected) {
        userSettings.setUserSessionConnection(connected);
    }

}

