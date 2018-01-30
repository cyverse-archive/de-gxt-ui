package org.iplantc.de.desktop.client.presenter.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.WindowState;
import org.iplantc.de.client.util.WebStorageUtil;
import org.iplantc.de.desktop.client.views.windows.WindowBase;

import com.google.gwtmockito.GxtMockitoTestRunner;
import com.google.web.bindery.autobean.shared.AutoBean;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

/**
 * Created by sriram on 1/23/18.
 */

@RunWith(GxtMockitoTestRunner.class)
public class WindowStateStorageWrapperTest {

    private WindowStateStorageWrapper uut;

    @Mock
    WebStorageUtil storageUtilMock;

    @Mock
    WindowBase.WindowStateFactory wsfMock;

    @Mock
    UserInfo userInfoMock;

    @Mock
    AutoBean<WindowState> windowStateAutoBean;


    @Before
    public void setUp() {
        uut = new WindowStateStorageWrapper(userInfoMock, "DATA", "DATA_1");
        uut.userInfo = userInfoMock;
        uut.wsf = wsfMock;
        uut.windowType = "DATA";
        uut.tag = "DATA_1";
        when(userInfoMock.getUsername()).thenReturn("ipctest");
    }

    @Test
    public void testSaveWindowState() {
        WindowState ws1 = mock(WindowState.class);
        when(wsfMock.windowState()).thenReturn(windowStateAutoBean);
        when(windowStateAutoBean.as()).thenReturn(ws1);
        ws1.setHeight("600");
        ws1.setWinLeft(0);
        ws1.setWinTop(1);
        ws1.setWidth("500");
        ws1.setMaximized(false);
        ws1.setMinimized(false);
        ws1.setTag("DATA_1");

        uut.saveWindowState(ws1);

        spy(uut).saveTop(eq(1));
        spy(uut).saveLeft(eq(0));
        spy(uut).saveHeight(eq("600"));
        spy(uut).saveWidth(eq("500"));
        spy(uut).saveMaximizedState(eq(false));
        spy(uut).saveMiniMinimizedState(eq(false));

        assertEquals(ws1, spy(uut).retrieveWindowState());
    }
}



