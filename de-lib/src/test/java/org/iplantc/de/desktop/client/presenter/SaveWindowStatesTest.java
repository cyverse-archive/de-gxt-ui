package org.iplantc.de.desktop.client.presenter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.WindowState;
import org.iplantc.de.client.util.WebStorageUtil;
import org.iplantc.de.desktop.client.DesktopView;

import com.google.gwtmockito.GwtMockitoTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by sriram on 1/23/18.
 */
@RunWith(GwtMockitoTestRunner.class)
public class SaveWindowStatesTest {

    @Mock
    DesktopView.Presenter desktopPresenterMock;

    @Mock
    UserInfo userInfoMock;

    @Mock
    WebStorageUtil storageUtilMock;

    private SaveWindowStatesPeriodic uut;

    @Before
    public void setUp() {
       uut = new SaveWindowStatesPeriodic(desktopPresenterMock, userInfoMock);
       uut.presenter = desktopPresenterMock;
       uut.userInfo = userInfoMock;
    }


    @Test
    public void run() {
       WindowState ws1  = mock(WindowState.class);
       when(ws1.getWindowType()).thenReturn("APPS");
       when(ws1.getHeight()).thenReturn("300");
       when(ws1.getWidth()).thenReturn("500");
       when(ws1.getWinLeft()).thenReturn(0);
       when(ws1.getWinTop()).thenReturn(0);
       when(ws1.isMaximized()).thenReturn(false);
       when(ws1.isMinimized()).thenReturn(false);
       when(ws1.getTag()).thenReturn("APPS");
       when(ws1.getAdditionalWindowStates()).thenReturn(new HashMap<String, String>());

        WindowState ws2  = mock(WindowState.class);
        when(ws2.getWindowType()).thenReturn("DATA");
        when(ws2.getHeight()).thenReturn("300");
        when(ws2.getWidth()).thenReturn("500");
        when(ws2.getWinLeft()).thenReturn(0);
        when(ws2.getWinTop()).thenReturn(0);
        when(ws2.isMaximized()).thenReturn(false);
        when(ws2.isMinimized()).thenReturn(false);
        when(ws2.getTag()).thenReturn("DATA");
        when(ws2.getAdditionalWindowStates()).thenReturn(new HashMap<String, String>());

        when(desktopPresenterMock.getWindowStates()).thenReturn(Arrays.asList(ws1,ws2));

        uut.run();

        verify(desktopPresenterMock).getWindowStates();
        verify(storageUtilMock, times(6)).writeToStorage(Matchers.anyString(), Matchers.anyString());
    }


}
