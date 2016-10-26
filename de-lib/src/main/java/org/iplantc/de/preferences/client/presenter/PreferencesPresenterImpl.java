package org.iplantc.de.preferences.client.presenter;

import org.iplantc.de.client.models.UserSettings;
import org.iplantc.de.client.models.WindowState;
import org.iplantc.de.client.services.UserSessionServiceFacade;
import org.iplantc.de.desktop.client.DesktopView;
import org.iplantc.de.preferences.client.events.PrefDlgRetryUserSessionClicked;
import org.iplantc.de.preferences.client.PreferencesView;

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
    DesktopView.Presenter desktopPresenter;

    @Inject
    public PreferencesPresenterImpl(PreferencesView view,
                                    UserSessionServiceFacade serviceFacade) {
        this.view = view;
        this.serviceFacade = serviceFacade;

        this.view.addPrefDlgRetryUserSessionClickedHandlers(this);
    }

    @Override
    public void go(DesktopView.Presenter presenter, UserSettings userSettings) {
        this.desktopPresenter = presenter;
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
                view.userSessionFail();
            }

            @Override
            public void onSuccess(List<WindowState> result) {
                view.userSessionSuccess();
                desktopPresenter.restoreWindows(result);
                desktopPresenter.doPeriodicSessionSave();
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
