package org.iplantc.de.preferences.client.presenter;

import org.iplantc.de.client.DEClientConstants;
import org.iplantc.de.client.models.UserSettings;
import org.iplantc.de.client.models.WindowState;
import org.iplantc.de.client.models.userSettings.UserSetting;
import org.iplantc.de.client.services.OauthServiceFacade;
import org.iplantc.de.client.services.UserSessionServiceFacade;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;
import org.iplantc.de.desktop.client.DesktopView;
import org.iplantc.de.preferences.client.PreferencesView;
import org.iplantc.de.preferences.client.events.PrefDlgRetryUserSessionClicked;
import org.iplantc.de.preferences.client.events.ResetHpcTokenClicked;
import org.iplantc.de.preferences.client.events.TestWebhookClicked;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import java.util.List;
import java.util.Map;

/**
 * @author aramsey
 */
public class PreferencesPresenterImpl implements PreferencesView.Presenter,
                                                 PrefDlgRetryUserSessionClicked.PrefDlgRetryUserSessionClickedHandler,
                                                 ResetHpcTokenClicked.ResetHpcTokenClickedHandler,
                                                 TestWebhookClicked.TestWebhookClickedHandler {


    private final PreferencesView view;
    private final UserSessionServiceFacade serviceFacade;
    private final OauthServiceFacade oauthServiceFacade;
    private final PreferencesView.PreferencesViewAppearance appearance;
    DesktopView.Presenter desktopPresenter;
    UserSettings userSettings;
    @Inject IplantAnnouncer announcer;
    private final DEClientConstants constants;

    @Inject
    public PreferencesPresenterImpl(PreferencesView view,
                                    UserSessionServiceFacade serviceFacade,
                                    OauthServiceFacade oauthServiceFacade,
                                    PreferencesView.PreferencesViewAppearance appearance,
                                    DEClientConstants constants) {
        this.view = view;
        this.serviceFacade = serviceFacade;
        this.oauthServiceFacade = oauthServiceFacade;
        this.appearance = appearance;
        this.constants = constants;

        this.view.addPrefDlgRetryUserSessionClickedHandlers(this);
        this.view.addResetHpcTokenClickedHandlers(this);
        this.view.addTestWebhookClickedHandlers(this);
    }

    @Override
    public void go(DesktopView.Presenter presenter, UserSettings userSettings) {
        this.desktopPresenter = presenter;
        this.userSettings = userSettings;

        if (userSettings.isUsingDefaults()) {
            retryUserPreferences();
        } else {
            setUpView();
        }
    }

    void setUpView() {
        view.initAndShow(userSettings);
    }

    @Override
    public void setDefaultValues() {
        view.setDefaultValues();
    }

    @Override
    public void onPrefDlgRetryUserSessionClicked(PrefDlgRetryUserSessionClicked event) {
        serviceFacade.getUserSession(new AsyncCallback<List<WindowState>>() {

            @Override
            public void onFailure(Throwable caught) {
                desktopPresenter.setUserSessionConnection(false);
                view.userSessionFail();
            }

            @Override
            public void onSuccess(List<WindowState> result) {
                desktopPresenter.setUserSessionConnection(true);
                view.userSessionSuccess();
                desktopPresenter.restoreWindows(result);
                desktopPresenter.doPeriodicSessionSave();
            }
        });
    }

    void retryUserPreferences() {
        serviceFacade.getUserPreferences(new AsyncCallback<UserSetting>() {
            @Override
            public void onFailure(Throwable caught) {
                announcer.schedule(new ErrorAnnouncementConfig(appearance.preferencesFailure()));
                setUpView();
            }

            @Override
            public void onSuccess(UserSetting result) {
                announcer.schedule(new SuccessAnnouncementConfig(appearance.preferencesSuccess()));
                userSettings.setUserSettings(result);
                setUpView();
            }
        });
    }

    @Override
    public void saveUserSettings() {
        desktopPresenter.saveUserSettings(view.getValue(), false);
    }

    @Override
    public PreferencesView getView() {
        return view;
    }

    @Override
    public void setViewDebugId(String baseId) {
        view.asWidget().ensureDebugId(baseId);
    }

    @Override
    public void onResetHpcClicked(ResetHpcTokenClicked event) {
        oauthServiceFacade.deleteHpcToken(new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                announcer.schedule(new ErrorAnnouncementConfig(appearance.hpcResetFailure()));
            }

            @Override
            public void onSuccess(Void result) {
                redirectForOAuth();

            }
        });
    }

    /**
     * Redirects users to OAuth page
     */
    void redirectForOAuth() {
        oauthServiceFacade.getRedirectUris(new AsyncCallback<Map<String, String>>() {
            @Override
            public void onFailure(Throwable throwable) {
                announcer.schedule(new ErrorAnnouncementConfig(appearance.hpcResetFailure()));
            }

            @Override
            public void onSuccess(Map<String, String> map) {
                Window.Location.replace(map.get(constants.hpcSystemId()));
            }
        });
    }

    /**
     * Attempt to send a test webhook notification to the URL given by user.
     * @param event
     */
    @Override
    public void onTestClicked(TestWebhookClicked event) {
        serviceFacade.testWebhook(event.getUrl(), new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable throwable) {
                ErrorHandler.post(appearance.testWebhookFail(),
                                  throwable);
            }

            @Override
            public void onSuccess(String s) {
                announcer.schedule(new SuccessAnnouncementConfig(appearance.testWebhookSuccess()));
            }
        });
    }
    
}
