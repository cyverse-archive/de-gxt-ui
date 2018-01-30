package org.iplantc.de.desktop.client.presenter;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.models.UserSession;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.desktop.client.DesktopView;

import com.google.gwtmockito.GwtMockitoTestRunner;

import com.sencha.gxt.widget.core.client.box.AutoProgressMessageBox;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

/**
 * @author aramsey
 */
@RunWith(GwtMockitoTestRunner.class)
public class GetUserSessionCallbackTest {

    @Mock IplantAnnouncer announcerMock;
    @Mock DesktopPresenterImpl presenterMock;
    @Mock AutoProgressMessageBox progressMessageBoxMock;
    @Mock DesktopView.Presenter.DesktopPresenterAppearance appearanceMock;
    @Mock UserSession resultMock;

    private RuntimeCallbacks.GetUserSessionCallback uut;

    @Before
    public void setUp() {
        when(appearanceMock.loadSessionFailed()).thenReturn("error");

        uut = new RuntimeCallbacks.GetUserSessionCallback(progressMessageBoxMock,
                                                          appearanceMock,
                                                          announcerMock,
                                                          presenterMock);

    }

    @Test
    public void onFailure() {
        Throwable throwableMock = mock(Throwable.class);
        /** CALL METHOD UNDER TEST **/
        uut.onFailure(throwableMock);

        verify(announcerMock).schedule(isA(ErrorAnnouncementConfig.class));
        verify(presenterMock).setUserSessionConnection(eq(false));
        verify(progressMessageBoxMock).hide();
    }

    @Test
    public void onSuccess() {

        /** CALL METHOD UNDER TEST **/
        uut.onSuccess(resultMock);

        verify(presenterMock).setUserSessionConnection(eq(true));
        verify(presenterMock).restoreWindows(eq(resultMock.getWindowConfigs()));
        verify(presenterMock).doPeriodicSessionSave();
        verify(progressMessageBoxMock).hide();
    }

}
