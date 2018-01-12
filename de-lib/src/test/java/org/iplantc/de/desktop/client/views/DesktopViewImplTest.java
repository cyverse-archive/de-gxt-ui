package org.iplantc.de.desktop.client.views;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import org.iplantc.de.desktop.client.views.widgets.TaskBar;
import org.iplantc.de.desktop.client.views.widgets.TaskButton;
import org.iplantc.de.desktop.client.views.windows.WindowInterface;
import org.iplantc.de.desktop.client.views.windows.WindowBase;
import org.iplantc.de.resources.client.messages.IplantNewUserTourStrings;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwtmockito.GxtMockitoTestRunner;

import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.WindowManager;
import com.sencha.gxt.widget.core.client.event.RegisterEvent;
import com.sencha.gxt.widget.core.client.event.UnregisterEvent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.List;

@RunWith(GxtMockitoTestRunner.class)
public class DesktopViewImplTest {

    @Mock RegisterEvent<Widget> registerEventMock;
    @Mock IplantNewUserTourStrings tourStringsMock;
    @Mock UnregisterEvent<Widget> unregisterEventMock;
    @Mock WindowManager windowManagerMock;

    @Test public void viewAddsTaskButtonWhenWindowIsRegistered() {
        DesktopViewImpl uut = new DesktopViewImpl(tourStringsMock, windowManagerMock);
        verifyViewInit(uut);
        uut.taskBar = mock(TaskBar.class);

        TaskButton mockTaskButton = mock(TaskButton.class);
        List<TaskButton> tbList = Lists.newArrayList(mockTaskButton);
        when(uut.taskBar.getButtons()).thenReturn(tbList);

        final WindowBase window = mock(WindowBase.class);
        when(registerEventMock.getItem()).thenReturn(window);


        uut.onRegister(registerEventMock);
        verify(uut.taskBar).addTaskButton(eq(window));
        verifyNoMoreInteractions(windowManagerMock);
    }

    @Test public void viewDoesNotAddNewTaskButtonsForExistingWindows() {
        DesktopViewImpl uut = new DesktopViewImpl(tourStringsMock, windowManagerMock);
        verifyViewInit(uut);
        uut.taskBar = mock(TaskBar.class);

        TaskButton mockTaskButton = mock(TaskButton.class);
        List<TaskButton> tbList = Lists.newArrayList(mockTaskButton);
        when(uut.taskBar.getButtons()).thenReturn(tbList);

        final Window window = mock(Window.class);
        when(registerEventMock.getItem()).thenReturn(window);
        when(mockTaskButton.getWindow()).thenReturn(window);


        uut.onRegister(registerEventMock);
        verify(uut.taskBar, never()).addTaskButton(eq(window));
        verifyNoMoreInteractions(windowManagerMock);
    }

    @Test public void taskButtonRemovedWhenWindowIsUnregisteredAndNotMinimized() {
        DesktopViewImpl uut = new DesktopViewImpl(tourStringsMock, windowManagerMock);
        verifyViewInit(uut);
        uut.taskBar = mock(TaskBar.class);

        TaskButton mockTaskButton = mock(TaskButton.class);
        List<TaskButton> tbList = Lists.newArrayList(mockTaskButton);
        when(uut.taskBar.getButtons()).thenReturn(tbList);

        final Window window = mock(Window.class, withSettings().extraInterfaces(WindowInterface.class));
        when(unregisterEventMock.getItem()).thenReturn(window);
        when(mockTaskButton.getWindow()).thenReturn(window);
        uut.onUnregister(unregisterEventMock);
        verify(mockTaskButton).getWindow();
        verify(uut.taskBar).removeTaskButton(eq(mockTaskButton));
        verifyNoMoreInteractions(mockTaskButton, windowManagerMock);
    }

    private void verifyViewInit(DesktopViewImpl uut) {
        verify(windowManagerMock).addRegisterHandler(eq(uut));
        verify(windowManagerMock).addUnregisterHandler(eq(uut));
    }
}
