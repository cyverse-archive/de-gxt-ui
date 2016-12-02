package org.iplantc.de.preferences.client.presenter;

import org.iplantc.de.client.models.UserSettings;
import org.iplantc.de.client.models.WindowState;
import org.iplantc.de.client.models.userSettings.UserSetting;
import org.iplantc.de.client.services.UserSessionServiceFacade;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;
import org.iplantc.de.desktop.client.DesktopView;
import org.iplantc.de.preferences.client.PreferencesView;
import org.iplantc.de.preferences.client.events.PrefDlgRetryUserSessionClicked;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

import java.util.List;

/**
 * @author aramsey
 */
public class PreferencesPresenterImpl implements PreferencesView.Presenter,
                                                 PrefDlgRetryUserSessionClicked.PrefDlgRetryUserSessionClickedHandler {


    private final PreferencesView view;
    private final UserSessionServiceFacade serviceFacade;
    private final PreferencesView.PreferencesViewAppearance appearance;
    DesktopView.Presenter desktopPresenter;
    UserSettings userSettings;
    @Inject IplantAnnouncer announcer;

    @Inject
    public PreferencesPresenterImpl(PreferencesView view,
                                    UserSessionServiceFacade serviceFacade,
                                    PreferencesView.PreferencesViewAppearance appearance) {
        this.view = view;
        this.serviceFacade = serviceFacade;
        this.appearance = appearance;

        this.view.addPrefDlgRetryUserSessionClickedHandlers(this);
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
}
