package org.iplantc.de.desktop.client.presenter;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.WindowType;
import org.iplantc.de.commons.client.views.window.configs.ConfigFactory;
import org.iplantc.de.desktop.client.DesktopView;
import org.iplantc.de.desktop.client.presenter.util.MessagePoller;

import com.google.gwtmockito.GxtMockitoTestRunner;
import com.google.gwtmockito.WithClassesToStub;

import com.sencha.gxt.widget.core.client.WindowManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

@RunWith(GxtMockitoTestRunner.class)
@WithClassesToStub(ConfigFactory.class)
public class DesktopPresenterImplTest {

    @Mock DesktopWindowManager desktopWindowManagerMock;
    @Mock EventBus eventBusMock;
    @Mock DesktopPresenterEventHandler globalEventHandlerMock;
    @Mock MessagePoller messagePollerMock;
    @Mock DesktopView viewMock;
    @Mock DesktopPresenterWindowEventHandler windowEventHandlerMock;
    @Mock WindowManager windowManagerMock;
    @Mock DesktopView.Presenter.DesktopPresenterAppearance appearanceMock;

    private DesktopPresenterImpl uut;
    @Before public void setUp() {
        uut = new DesktopPresenterImpl(viewMock,
                                          globalEventHandlerMock,
                                          windowEventHandlerMock,
                                          eventBusMock,
                                          windowManagerMock,
                                          desktopWindowManagerMock,
                                          messagePollerMock,
                                          appearanceMock);

    }

    @Test public void testOnAboutClick() {
        uut.onAboutClick();
        verify(desktopWindowManagerMock).show(eq(WindowType.ABOUT));
        verifyNoMoreInteractions(desktopWindowManagerMock);
    }

    @Test public void testOnAnalysesWinBtnSelect() {
        uut.onAnalysesWinBtnSelect();
        verify(desktopWindowManagerMock).show(eq(WindowType.ANALYSES));
        verifyNoMoreInteractions(desktopWindowManagerMock);
    }

    @Test public void testOnAppsWinBtnSelect() {
        uut.onAppsWinBtnSelect();
        verify(desktopWindowManagerMock).show(eq(WindowType.APPS));
        verifyNoMoreInteractions(desktopWindowManagerMock);
    }

    @Test public void testOnDataWinBtnSelect() {
        uut.onDataWinBtnSelect();
        verify(desktopWindowManagerMock).show(eq(WindowType.DATA));
        verifyNoMoreInteractions(desktopWindowManagerMock);
    }

}
