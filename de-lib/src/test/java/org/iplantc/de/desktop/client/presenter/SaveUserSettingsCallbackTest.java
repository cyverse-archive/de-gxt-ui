package org.iplantc.de.desktop.client.presenter;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.models.UserSettings;
import org.iplantc.de.client.models.userSettings.UserSetting;
import org.iplantc.de.client.services.UserSessionServiceFacade;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.views.window.configs.SavedWindowConfig;
import org.iplantc.de.desktop.client.DesktopView;

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
public class SaveUserSettingsCallbackTest {

    @Mock IplantAnnouncer announcerMock;
    @Mock DesktopView.Presenter.DesktopPresenterAppearance appearanceMock;
    @Mock UserSettings newValueMock;
    @Mock UserSettings userSettingsMock;
    @Mock DesktopView.Presenter presenterMock;
    @Mock UserSessionServiceFacade userSessionServiceMock;
    @Mock UserSetting userSettingMock;
    @Mock List<SavedWindowConfig> windowConfigsMock;

    @Captor ArgumentCaptor<AsyncCallback<Void>> voidCaptor;

    private RuntimeCallbacks.SaveUserSettingsCallback uut;

    @Before
    public void setUp() {
        when(appearanceMock.saveSessionFailed()).thenReturn("fail");
        when(appearanceMock.saveSettings()).thenReturn("saved");

        uut = new RuntimeCallbacks.SaveUserSettingsCallback(newValueMock,
                                                            userSettingsMock,
                                                            announcerMock,
                                                            presenterMock,
                                                            appearanceMock,
                                                            false,
                                                            userSessionServiceMock);
    }

    @Test
    public void onFailure() {
        Throwable throwableMock = mock(Throwable.class);

        /** CALL METHOD UNDER TEST **/
        uut.onFailure(throwableMock);

        verify(announcerMock).schedule(isA(ErrorAnnouncementConfig.class));
    }

    @Test
    public void onSuccess_savedSession_Fail() {
        Throwable throwableMock = mock(Throwable.class);
        when(newValueMock.getUserSetting()).thenReturn(userSettingMock);
        when(userSettingsMock.isSaveSession()).thenReturn(true);
        when(appearanceMock.saveSessionFailed()).thenReturn("fail");
        when(presenterMock.getOrderedWindowConfigs()).thenReturn(windowConfigsMock);

        /** CALL METHOD UNDER TEST **/
        uut.onSuccess(null);

        verify(userSettingsMock).setUserSettings(eq(userSettingMock));
        verify(userSettingsMock).isSaveSession();
        verify(userSessionServiceMock).saveUserSession(eq(windowConfigsMock), voidCaptor.capture());

        voidCaptor.getValue().onFailure(throwableMock);
        verify(announcerMock).schedule(isA(ErrorAnnouncementConfig.class));
        verify(presenterMock).setUserSessionConnection(eq(false));
    }

    @Test
    public void onSuccess_savedSession_Success() {
        RuntimeCallbacks.SaveUserSettingsCallback spy = spy(uut);
        Throwable throwableMock = mock(Throwable.class);
        uut.updateSilently = false;
        when(newValueMock.getUserSetting()).thenReturn(userSettingMock);
        when(userSettingsMock.isSaveSession()).thenReturn(true);

        when(presenterMock.getOrderedWindowConfigs()).thenReturn(windowConfigsMock);

        /** CALL METHOD UNDER TEST **/
        spy.onSuccess(null);

        verify(userSettingsMock).setUserSettings(eq(userSettingMock));
        verify(userSettingsMock).isSaveSession();
        verify(userSessionServiceMock).saveUserSession(eq(windowConfigsMock), voidCaptor.capture());

        voidCaptor.getValue().onSuccess(null);
        verify(presenterMock).setUserSessionConnection(eq(true));
        verify(spy).showSaveSettingSuccess();
    }

    @Test
    public void onSuccess_noSavedSession() {
        RuntimeCallbacks.SaveUserSettingsCallback spy = spy(uut);
        uut.updateSilently = false;
        when(newValueMock.getUserSetting()).thenReturn(userSettingMock);
        when(userSettingsMock.isSaveSession()).thenReturn(false);

        /** CALL METHOD UNDER TEST **/
        spy.onSuccess(null);

        verify(userSettingsMock).setUserSettings(eq(userSettingMock));
        verify(userSettingsMock).isSaveSession();

        verify(spy).showSaveSettingSuccess();
        verifyZeroInteractions(userSessionServiceMock);
    }
}
