package org.iplantc.de.desktop.client.views.windows;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.WindowState;
import org.iplantc.de.client.util.WebStorageUtil;
import org.iplantc.de.commons.client.views.window.configs.WindowConfig;

import com.google.gwtmockito.GxtMockitoTestRunner;
import com.google.web.bindery.autobean.shared.AutoBean;

import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Rectangle;
import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.widget.core.client.button.ToolButton;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;

@RunWith(GxtMockitoTestRunner.class)
public class WindowBaseTest {

    private WindowBase uut;
    @Mock
    WebStorageUtil storageUtilMock;

    @Mock
    WindowBase.WindowStateFactory wsfMock;

    @Mock
    UserInfo userInfoMock;

    @Mock
    AutoBean<WindowState> windowStateAutoBean;

    @Before public void setup() {
        uut = new WindowBase() {
            @Override
            public String getWindowType() {
                return "DATA";
            }

            @Override
            public FastMap<String> getAdditionalWindowStates() {
                return null;
            }

            @Override
            public WindowConfig getWindowConfig() {
                return null;
            }
        };
        uut.wsf = wsfMock;
        uut.userInfo = userInfoMock;
    }

    @Test public void minimizeFlagSetToFalseWhenWindowShown(){
        uut.minimized = true;

        uut.onShow();
        assertFalse(uut.minimized);
    }

    @Test public void minimizeFlagSetToFalseWhenMaximized(){
        uut.minimized = true;
        uut.isMaximizable = true;
        uut.btnRestore = mock(ToolButton.class);
        // To cause construction of Resizable object
        uut.getResizable();

        uut.setMaximized(true);
        assertFalse(uut.minimized);
    }

    @Test public void testSnapLeft() {
        final XElement mock = mock(XElement.class);
        final Rectangle mockRectangle = mock(Rectangle.class);
        // Test values are prime
        final int testValueX_half = 11;
        final int testValueWidth_half = 3;

        final int testValueX = testValueX_half*2;
        final int testValueY = 17;
        final int testValueWidth = testValueWidth_half*2;
        final int testValueHeight = 5;
        when(mockRectangle.getX()).thenReturn(testValueX);
        when(mockRectangle.getY()).thenReturn(testValueY);
        when(mockRectangle.getWidth()).thenReturn(testValueWidth);
        when(mockRectangle.getHeight()).thenReturn(testValueHeight);
        when(mock.getBounds()).thenReturn(mockRectangle);
        WindowBase uutSpy = spy(uut);
        uutSpy.doSnapLeft(mock);

        // Verify that page position is set to element's given X and Y
        verify(uutSpy).setPagePosition(eq(testValueX), eq(testValueY));
        verify(uutSpy).setPixelSize(eq(testValueWidth_half), eq(testValueHeight));
    }

    @Test public void testSnapRight() {
        final XElement mock = mock(XElement.class);
        final Rectangle mockRectangle = mock(Rectangle.class);
        // Test values are prime
        final int testValueX_half = 11;
        final int testValueWidth_half = 3;

        final int testValueX = testValueX_half*2;
        final int testValueY = 17;
        final int testValueWidth = testValueWidth_half*2;
        final int testValueHeight = 5;
        when(mockRectangle.getX()).thenReturn(testValueX);
        when(mockRectangle.getY()).thenReturn(testValueY);
        when(mockRectangle.getWidth()).thenReturn(testValueWidth);
        when(mockRectangle.getHeight()).thenReturn(testValueHeight);
        when(mock.getBounds()).thenReturn(mockRectangle);
        WindowBase uutSpy = spy(uut);
        uutSpy.doSnapRight(mock);

        // Verify that page position is set to half of element's width
        verify(uutSpy).setPagePosition(eq(testValueWidth_half), eq(testValueY));
        verify(uutSpy).setPixelSize(eq(testValueWidth_half), eq(testValueHeight));
    }

    @Test
    public void testGetWindowStateFromLocalStorage() {
        WindowState ws1 = mock(WindowState.class);
        when(wsfMock.windowState()).thenReturn(windowStateAutoBean);
        when(windowStateAutoBean.as()).thenReturn(ws1);
        when(userInfoMock.getUsername()).thenReturn("ipctest");
        uut.getWindowStateFromLocalStorage("DATA");
        verify(ws1).setMaximized(Matchers.anyBoolean());
        verify(ws1).setMinimized(Matchers.anyBoolean());
        verify(ws1).setHeight(Matchers.anyString());
        verify(ws1).setWidth(Matchers.anyString());
        verify(ws1).setWinLeft(Matchers.anyInt());
        verify(ws1).setWinTop(Matchers.anyInt());
        verify(ws1).setTag(eq("DATA"));
   }
}
