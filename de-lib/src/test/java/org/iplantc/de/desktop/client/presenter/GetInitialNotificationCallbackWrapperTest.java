package org.iplantc.de.desktop.client.presenter;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.models.notifications.Notification;
import org.iplantc.de.client.models.notifications.NotificationList;
import org.iplantc.de.client.models.notifications.NotificationMessage;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.desktop.client.DesktopView;

import com.google.gwtmockito.GwtMockitoTestRunner;

import com.sencha.gxt.data.shared.ListStore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.Iterator;
import java.util.List;

/**
 * @author aramsey
 */
@RunWith(GwtMockitoTestRunner.class)
public class GetInitialNotificationCallbackWrapperTest {

    @Mock DesktopView viewMock;
    @Mock DesktopView.Presenter.DesktopPresenterAppearance appearanceMock;
    @Mock IplantAnnouncer announcerMock;
    @Mock ListStore<NotificationMessage> notificationStoreMock;
    @Mock NotificationList resultsMock;
    @Mock List<Notification> notificationsMock;
    @Mock Iterator<Notification> notificationIterator;
    @Mock Notification notificationMock;
    @Mock NotificationMessage messageMock;

    private InitializationCallbacks.GetInitialNotificationsCallback uut;

    @Before
    public void setUp() throws Exception {
        when(viewMock.getNotificationStore()).thenReturn(notificationStoreMock);
        when(resultsMock.getNotifications()).thenReturn(notificationsMock);
        when(notificationsMock.size()).thenReturn(1);
        when(notificationsMock.iterator()).thenReturn(notificationIterator);
        when(notificationIterator.hasNext()).thenReturn(true, false);
        when(notificationIterator.next()).thenReturn(notificationMock);

        uut = new InitializationCallbacks.GetInitialNotificationsCallback(viewMock,
                                                                          appearanceMock,
                                                                          announcerMock);
    }

    @Test
    public void onFailure() {
        when(appearanceMock.fetchNotificationsError()).thenReturn("error");

        Throwable throwableMock = mock(Throwable.class);
        /** CALL METHOD UNDER TEST **/
        uut.onFailure(throwableMock);

        verify(announcerMock).schedule(isA(ErrorAnnouncementConfig.class));
        verify(viewMock).setNotificationConnection(eq(false));
    }

    @Test
    public void onSuccess_nullResult() {

        when(notificationMock.getMessage()).thenReturn(messageMock);

        /** CALL METHOD UNDER TEST **/
        uut.onSuccess(null);

        verify(viewMock).setNotificationConnection(eq(true));
        verifyZeroInteractions(notificationStoreMock);
    }

    @Test
    public void onSuccess() {
        when(notificationMock.getMessage()).thenReturn(messageMock);
        when(resultsMock.getUnseenTotal()).thenReturn("2");

        /** CALL METHOD UNDER TEST **/
        uut.onSuccess(resultsMock);

        verify(viewMock).setNotificationConnection(eq(true));
        verify(viewMock).setUnseenNotificationCount(eq(2));
        verify(notificationStoreMock).add(eq(messageMock));
    }

}
