package org.iplantc.de.preferences.client.presenter;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.DEClientConstants;
import org.iplantc.de.client.models.UserSettings;
import org.iplantc.de.client.models.WindowState;
import org.iplantc.de.client.models.webhooks.WebhookTypeList;
import org.iplantc.de.client.services.OauthServiceFacade;
import org.iplantc.de.client.services.UserSessionServiceFacade;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;
import org.iplantc.de.desktop.client.DesktopView;
import org.iplantc.de.preferences.client.PreferencesView;
import org.iplantc.de.preferences.client.events.PrefDlgRetryUserSessionClicked;
import org.iplantc.de.preferences.client.events.ResetHpcTokenClicked;
import org.iplantc.de.preferences.client.events.TestWebhookClicked;
import org.iplantc.de.shared.AppsCallback;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwtmockito.GwtMockitoTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.List;
import java.util.Map;

/**
 * @author aramsey
 */
@RunWith(GwtMockitoTestRunner.class)
public class PreferencesPresenterImplTest {

    @Mock PreferencesView viewMock;
    @Mock UserSessionServiceFacade serviceFacadeMock;
    @Mock
    OauthServiceFacade oauthServiceFacadeMock;
    @Mock DesktopView.Presenter desktopPresenterMock;
    @Mock UserSettings userSettingsMock;
    @Mock List<WindowState> windowStatesMock;
    @Mock PreferencesView.PreferencesViewAppearance appearanceMock;
    @Mock
    DEClientConstants constantsMock;

    @Captor ArgumentCaptor<AsyncCallback<List<WindowState>>> userSessionCaptor;
    @Captor ArgumentCaptor<AsyncCallback<Void>> voidCaptor;
    @Captor ArgumentCaptor<AsyncCallback<Map<String, String>>> urlMapCaptor;
    @Mock Map<String, String> redirectUrlsMock;
    @Captor ArgumentCaptor<AsyncCallback<Void>> voidAppsCaptor;
    @Captor ArgumentCaptor<AppsCallback<WebhookTypeList>> typeListCaptor;

    @Mock
    IplantAnnouncer iplantAnnouncerMock;
    private PreferencesPresenterImpl uut;

    @Before
    public void setUp() {
        when(viewMock.getValue()).thenReturn(userSettingsMock);

        uut = new PreferencesPresenterImpl(viewMock,
                                           serviceFacadeMock,
                                           oauthServiceFacadeMock,
                                           appearanceMock,
                                           constantsMock);

        uut.desktopPresenter = desktopPresenterMock;
        uut.announcer = iplantAnnouncerMock;
    }
    
    @Test
    public void go() {
        /** CALL METHOD UNDER TEST **/
        WebhookTypeList typeList = mock(WebhookTypeList.class);
        uut.go(desktopPresenterMock, userSettingsMock);

        verify(serviceFacadeMock).getWebhookTypes(typeListCaptor.capture());

        typeListCaptor.getValue().onSuccess(typeList);
        verify(viewMock).initAndShow(userSettingsMock, typeList);
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

    @Test
    public void testOnResetHpcClicked() {
        ResetHpcTokenClicked eventMock = mock(ResetHpcTokenClicked.class);
        /** CALL METHOD UNDER TEST **/
        uut.onResetHpcClicked(eventMock);
        verify(oauthServiceFacadeMock).deleteHpcToken(voidCaptor.capture());
    }

    @Test
    public void testRedirectForOAuth() {
        when(constantsMock.hpcSystemId()).thenReturn("agave");
        uut.redirectForOAuth();
        verify(oauthServiceFacadeMock).getRedirectUris(urlMapCaptor.capture());
        urlMapCaptor.getValue().onSuccess(redirectUrlsMock);
        verify(redirectUrlsMock).get(eq(constantsMock.hpcSystemId()));
    }
    @Test
    public void testOnTestClicked() {
        TestWebhookClicked twc = mock(TestWebhookClicked.class);
        when(twc.getUrl()).thenReturn("https://requestb.in/ukaletuk");
        when(appearanceMock.testWebhookSuccess()).thenReturn("Request successful!");
        uut.onTestClicked(twc);
        verify(serviceFacadeMock).testWebhook(eq(twc.getUrl()), voidAppsCaptor.capture());
        voidAppsCaptor.getValue().onSuccess(null);
        verify(iplantAnnouncerMock).schedule(isA(SuccessAnnouncementConfig.class));
    }

}
