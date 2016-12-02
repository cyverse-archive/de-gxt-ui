package org.iplantc.de.preferences.client.presenter;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.models.UserSettings;
import org.iplantc.de.client.models.WindowState;
import org.iplantc.de.client.services.UserSessionServiceFacade;
import org.iplantc.de.desktop.client.DesktopView;
import org.iplantc.de.preferences.client.PreferencesView;
import org.iplantc.de.preferences.client.events.PrefDlgRetryUserSessionClicked;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwtmockito.GwtMockitoTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.List;

/**
 * @author aramsey
 */
@RunWith(GwtMockitoTestRunner.class)
public class PreferencesPresenterImplTest {

    @Mock PreferencesView viewMock;
    @Mock UserSessionServiceFacade serviceFacadeMock;
    @Mock DesktopView.Presenter desktopPresenterMock;
    @Mock UserSettings userSettingsMock;
    @Mock List<WindowState> windowStatesMock;
    @Mock PreferencesView.PreferencesViewAppearance appearanceMock;

    @Captor ArgumentCaptor<AsyncCallback<List<WindowState>>> userSessionCaptor;

    private PreferencesPresenterImpl uut;

    @Before
    public void setUp() {
        when(viewMock.getValue()).thenReturn(userSettingsMock);

        uut = new PreferencesPresenterImpl(viewMock,
                                           serviceFacadeMock,
                                           appearanceMock);

        uut.desktopPresenter = desktopPresenterMock;
    }
    
    @Test
    public void go() {
        /** CALL METHOD UNDER TEST **/
        uut.go(desktopPresenterMock, userSettingsMock);
        verify(viewMock).initAndShow(userSettingsMock);
    }

    @Test
    public void onPrefDlgRetryUserSessionClicked_onSuccess() {
        PrefDlgRetryUserSessionClicked eventMock = mock(PrefDlgRetryUserSessionClicked.class);

        /** CALL METHOD UNDER TEST **/
        uut.onPrefDlgRetryUserSessionClicked(eventMock);
        verify(serviceFacadeMock).getUserSession(userSessionCaptor.capture());

        userSessionCaptor.getValue().onSuccess(windowStatesMock);
        verify(viewMock).userSessionSuccess();
        verify(desktopPresenterMock).restoreWindows(windowStatesMock);
        verify(desktopPresenterMock).doPeriodicSessionSave();
    }

    @Test
    public void onPrefDlgRetryUserSessionClicked_onFail() {
        PrefDlgRetryUserSessionClicked eventMock = mock(PrefDlgRetryUserSessionClicked.class);
        Throwable caughtMock = mock(Throwable.class);

        /** CALL METHOD UNDER TEST **/
        uut.onPrefDlgRetryUserSessionClicked(eventMock);
        verify(serviceFacadeMock).getUserSession(userSessionCaptor.capture());

        userSessionCaptor.getValue().onFailure(caughtMock);
        verify(viewMock).userSessionFail();
    }

    @Test
    public void saveUserSettings() {
        /** CALL METHOD UNDER TEST **/
        uut.saveUserSettings();
        verify(desktopPresenterMock).saveUserSettings(eq(userSettingsMock), eq(false));
    }

}
