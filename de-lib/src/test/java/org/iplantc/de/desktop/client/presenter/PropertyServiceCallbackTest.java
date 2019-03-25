package org.iplantc.de.desktop.client.presenter;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.UserSettings;
import org.iplantc.de.client.services.UserSessionServiceFacade;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.desktop.client.DesktopView;
import org.iplantc.de.shared.DEProperties;
import org.iplantc.de.shared.exceptions.HttpException;

import com.google.gwt.user.client.ui.Panel;
import com.google.gwtmockito.GwtMockitoTestRunner;
import com.google.inject.Provider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.HashMap;

/**
 * @author aramsey
 */
@RunWith(GwtMockitoTestRunner.class)
public class PropertyServiceCallbackTest {

    @Mock DEProperties propertiesMock;
    @Mock UserInfo userInfoMock;
    @Mock UserSettings userSettingMock;
    @Mock UserSessionServiceFacade userSessionServiceMock;
    @Mock Provider<ErrorHandler> errorHandlerProviderMock;
    @Mock DesktopView.Presenter.DesktopPresenterAppearance appearanceMock;
    @Mock IplantAnnouncer announcerMock;
    @Mock Panel panelMock;
    @Mock DesktopPresenterImpl presenterMock;
    @Mock HashMap<String, String> resultMock;

    private InitializationCallbacks.PropertyServiceCallback uut;

    @Before
    public void setUp() {

        uut = new InitializationCallbacks.PropertyServiceCallback(propertiesMock,
                                                                  userInfoMock,
                                                                  userSettingMock,
                                                                  userSessionServiceMock,
                                                                  errorHandlerProviderMock,
                                                                  appearanceMock,
                                                                  announcerMock,
                                                                  panelMock,
                                                                  presenterMock);
    }

    @Test
    public void onFailure_randomThrowable() {
        Throwable throwableMock = mock(Throwable.class);
        Integer statusCode = null;
        /** CALL METHOD UNDER TEST **/
        uut.onFailure(throwableMock);

        verify(presenterMock).onBootstrapError(eq(statusCode), anyString());
    }

    @Test
    public void onFailure_HttpException() {
        HttpException throwableMock = mock(HttpException.class);
        when(throwableMock.getStatusCode()).thenReturn(500);
        Integer statusCode = 500;
        /** CALL METHOD UNDER TEST **/
        uut.onFailure(throwableMock);

        verify(presenterMock).onBootstrapError(eq(statusCode), anyString());
    }

    @Test
    public void onSuccess() {

        /** CALL METHOD UNDER TEST **/
        uut.onSuccess(resultMock);

        verify(propertiesMock).initialize(resultMock);
        verify(userSessionServiceMock).bootstrap(isA(InitializationCallbacks.BootstrapCallback.class));
    }

}
