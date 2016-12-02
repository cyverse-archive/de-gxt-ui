package org.iplantc.de.desktop.client.presenter;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.UserSettings;
import org.iplantc.de.client.services.UserSessionServiceFacade;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.views.dialogs.AgaveAuthPrompt;
import org.iplantc.de.desktop.client.DesktopView;
import org.iplantc.de.shared.exceptions.HttpException;

import com.google.gwt.user.client.ui.Panel;
import com.google.gwtmockito.GxtMockitoTestRunner;
import com.google.inject.Provider;

import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

/**
 * @author aramsey
 */
@RunWith(GxtMockitoTestRunner.class)
public class BootstrapCallbackTest {

    @Mock DesktopPresenterImpl presenterMock;
    @Mock UserInfo userInfoMock;
    @Mock Provider<ErrorHandler> errorHandlerProviderMock;
    @Mock DesktopView.Presenter.DesktopPresenterAppearance appearanceMock;
    @Mock UserSessionServiceFacade userSessionServiceMock;
    @Mock ConfirmMessageBox introBoxMock;
    @Mock AgaveAuthPrompt agaveAuthPromptMock;
    @Mock Panel panelMock;
    @Mock UserSettings userSettingsMock;
    @Mock IplantAnnouncer announcerMock;

    private InitializationCallbacks.BootstrapCallback uut;

    @Before
    public void setup() {

        uut = new InitializationCallbacks.BootstrapCallback(presenterMock,
                                                            panelMock,
                                                            userInfoMock,
                                                            userSettingsMock,
                                                            announcerMock,
                                                            appearanceMock) {
            @Override
            ConfirmMessageBox getIntroConfirmation() {
                return introBoxMock;
            }

            @Override
            AgaveAuthPrompt getAgavePrompt() {
                return agaveAuthPromptMock;
            }
        };

    }

    @Test
    public void onFailure_randomThrowable() {
        Throwable throwableMock = mock(Throwable.class);
        Integer statusCode = null;
        /** CALL METHOD UNDER TEST **/
        uut.onFailure(throwableMock);

        verify(presenterMock).onBootstrapError(eq(statusCode));
    }

    @Test
    public void onFailure_HttpException() {
        HttpException throwableMock = mock(HttpException.class);
        when(throwableMock.getStatusCode()).thenReturn(500);
        Integer statusCode = 500;
        /** CALL METHOD UNDER TEST **/
        uut.onFailure(throwableMock);

        verify(presenterMock).onBootstrapError(eq(statusCode));
    }

    @Test
    public void onSuccess() {
        String result = "json";

        /** CALL METHOD UNDER TEST **/
        uut.onSuccess(result);
        verify(userInfoMock).init(eq(result));
    }

    @Test
    public void onSuccess_newUser() {
        String result = "json";

        when(userInfoMock.isNewUser()).thenReturn(true);
        when(appearanceMock.welcome()).thenReturn("welcome");
        when(appearanceMock.introWelcome()).thenReturn("introWelcome");

        /** CALL METHOD UNDER TEST **/
        uut.onSuccess(result);
        verify(userInfoMock).init(eq(result));
        verify(userInfoMock).isNewUser();

    }

    @Test
    public void onSuccess_agaveRedirect() {
        String result = "json";

        when(userInfoMock.isNewUser()).thenReturn(false);
        when(userInfoMock.hasAgaveRedirect()).thenReturn(true);

        /** CALL METHOD UNDER TEST **/
        uut.onSuccess(result);
        verify(userInfoMock).init(eq(result));
        verify(userInfoMock).hasAgaveRedirect();
        verify(agaveAuthPromptMock).show();
        verify(presenterMock).stickWindowToTop(agaveAuthPromptMock);
    }

}
