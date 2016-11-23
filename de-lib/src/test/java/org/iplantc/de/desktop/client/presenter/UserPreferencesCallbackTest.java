package org.iplantc.de.desktop.client.presenter;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.models.UserSettings;
import org.iplantc.de.client.models.userSettings.UserSetting;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.desktop.client.DesktopView;

import com.google.gwt.user.client.ui.Panel;
import com.google.gwtmockito.GwtMockitoTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

/**
 * @author aramsey
 */
@RunWith(GwtMockitoTestRunner.class)
public class UserPreferencesCallbackTest {

    @Mock DesktopPresenterImpl presenterMock;
    @Mock Panel panelMock;
    @Mock UserSettings userSettingsMock;
    @Mock IplantAnnouncer announcerMock;
    @Mock DesktopView.Presenter.DesktopPresenterAppearance appearanceMock;
    @Mock UserSetting resultMock;

    private InitializationCallbacks.UserPreferencesCallback uut;

    @Before
    public void setUp() {

        uut = new InitializationCallbacks.UserPreferencesCallback(presenterMock,
                                                                  panelMock,
                                                                  userSettingsMock,
                                                                  announcerMock,
                                                                  appearanceMock);
    }

    @Test
    public void onFailure() {
        Throwable throwableMock = mock(Throwable.class);
        when(appearanceMock.userPreferencesLoadError()).thenReturn("error");

        /** CALL METHOD UNDER TEST **/
        uut.onFailure(throwableMock);
        verify(announcerMock).schedule(isA(ErrorAnnouncementConfig.class));
        verify(userSettingsMock).setUserSettings(null);
        verify(presenterMock).postBootstrap(eq(panelMock));

    }

    @Test
    public void onSuccess() {

        /** CALL METHOD UNDER TEST **/
        uut.onSuccess(resultMock);
        verify(userSettingsMock).setUserSettings(eq(resultMock));
        verify(presenterMock).postBootstrap(eq(panelMock));
    }

}
