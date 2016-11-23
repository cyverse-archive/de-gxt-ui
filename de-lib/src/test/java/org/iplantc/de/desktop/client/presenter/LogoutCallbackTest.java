package org.iplantc.de.desktop.client.presenter;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.DEClientConstants;
import org.iplantc.de.client.models.UserSettings;
import org.iplantc.de.client.models.WindowState;
import org.iplantc.de.client.services.UserSessionServiceFacade;
import org.iplantc.de.desktop.client.DesktopView;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwtmockito.GwtMockitoTestRunner;

import com.sencha.gxt.widget.core.client.AutoProgressBar;
import com.sencha.gxt.widget.core.client.box.AutoProgressMessageBox;

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
public class LogoutCallbackTest {
    
    @Mock DEClientConstants constantsMock;
    @Mock List<WindowState> orderedWindowStatesMock;
    @Mock UserSessionServiceFacade userSessionServiceMock;
    @Mock UserSettings userSettingsMock;
    @Mock DesktopView.Presenter.DesktopPresenterAppearance appearanceMock;
    @Mock AutoProgressMessageBox progressMessageMock;

    @Captor ArgumentCaptor<AsyncCallback<Void>> voidCaptor;
    
    private RuntimeCallbacks.LogoutCallback uut;
    
    @Before
    public void setUp() {
        
        when(constantsMock.logoutUrl()).thenReturn("logout");
        
        uut = new RuntimeCallbacks.LogoutCallback(userSessionServiceMock,
                                                  constantsMock,
                                                  userSettingsMock,
                                                  appearanceMock,
                                                  orderedWindowStatesMock){
            @Override
            AutoProgressMessageBox getProgressMessage() {
                return progressMessageMock;
            }
        };
    }

    @Test
    public void onFailure() {
        RuntimeCallbacks.LogoutCallback spy = spy(uut);
        Throwable throwableMock = mock(Throwable.class);

        /** CALL METHOD UNDER TEST **/
        spy.onFailure(throwableMock);

        verify(spy).logout();
    }

    @Test
    public void onSuccess() {
        RuntimeCallbacks.LogoutCallback spy = spy(uut);

        /** CALL METHOD UNDER TEST **/
        spy.onSuccess("string");
        verify(spy).logout();
    }
    
    @Test
    public void logoutTest() {
        when(userSettingsMock.isSaveSession()).thenReturn(true);
        when(appearanceMock.savingSession()).thenReturn("saving");
        when(appearanceMock.savingSessionWaitNotice()).thenReturn("wait");
        when(progressMessageMock.getProgressBar()).thenReturn(mock(AutoProgressBar.class));

        /** CALL METHOD UNDER TEST **/
        uut.logout();

        verify(userSettingsMock).isSaveSession();
        verify(progressMessageMock).auto();
        verify(userSessionServiceMock).saveUserSession(eq(orderedWindowStatesMock), voidCaptor.capture());
        verify(progressMessageMock).show();

        voidCaptor.getValue().onSuccess(null);
        verify(progressMessageMock).hide();
    }

}
