package org.iplantc.de.notifications.client.presenter;

import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.HasUUIDs;
import org.iplantc.de.client.models.notifications.NotificationAutoBeanFactory;
import org.iplantc.de.client.models.notifications.NotificationMessage;
import org.iplantc.de.client.services.MessageServiceFacade;
import org.iplantc.de.client.services.callbacks.ReactErrorCallback;
import org.iplantc.de.client.services.callbacks.ReactSuccessCallback;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;
import org.iplantc.de.notifications.client.events.NotificationCountUpdateEvent;
import org.iplantc.de.notifications.client.views.NotificationView;
import org.iplantc.de.shared.DECallback;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwtmockito.GxtMockitoTestRunner;
import com.google.web.bindery.autobean.shared.AutoBean;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author aramsey
 */
@RunWith(GxtMockitoTestRunner.class)
public class NotificationPresenterImplTest {

    @Mock NotificationView.NotificationViewAppearance appearanceMock;
    @Mock MessageServiceFacade messageServiceFacadeMock;
    @Mock EventBus eventBusMock;
    @Mock
    NotificationView viewMock;

    @Mock List<NotificationMessage> listMock;
    @Mock NotificationMessage notificationMessageMock;
    @Mock Iterator<NotificationMessage> iteratorMock;
    @Mock IplantAnnouncer iplantAnnouncerMock;
    @Mock NotificationAutoBeanFactory factoryMock;
    @Mock List<NotificationMessage> notificationsMock;
    @Mock Stream<NotificationMessage> notificationStream;
    @Mock Stream<String> stringStream;
    @Mock List<String> idList;
    String[] ids = { "1", "2", "3" };
    @Mock AutoBean<HasUUIDs> uuiDsAutoBeanMock;
    @Mock HasUUIDs uuiDsMock;

    @Captor ArgumentCaptor<DECallback<String>> deCallbackStringCaptor;
    @Captor ArgumentCaptor<AsyncCallback<String>> asyncCallbackStringCaptor;
    @Mock NotificationCountUpdateEvent mockCountUpdateEvent;
    @Mock
    ReactSuccessCallback mockSuccessCallback;
    @Mock
    ReactErrorCallback mockErrorCallback;


    private NotificationPresenterImpl uut;

    @Before
    public void setUp() {
        when(notificationMessageMock.getId()).thenReturn("id");

        uut = new NotificationPresenterImpl(appearanceMock,
                                            factoryMock, viewMock,
                                            eventBusMock) {

            @Override
            protected void fireCountUpdateEvent(String result) {
                return;
            }


        };
        uut.messageServiceFacade = messageServiceFacadeMock;
        uut.announcer = iplantAnnouncerMock;
    }


    @Test
    public void deleteNotifications() {
        when(factoryMock.getHasUUIDs()).thenReturn(uuiDsAutoBeanMock);
        when(uuiDsAutoBeanMock.as()).thenReturn(uuiDsMock);

        uut.deleteNotifications(ids, mockSuccessCallback, mockErrorCallback);

        verify(messageServiceFacadeMock).deleteMessages(eq(uuiDsMock), deCallbackStringCaptor.capture());

        deCallbackStringCaptor.getValue().onSuccess("result");
    }

    @Test
    public void testOnNotificationToolbarMarkAsReadClicked() {
        NotificationPresenterImpl spy = spy(uut);

        when(listMock.isEmpty()).thenReturn(false);
        when(listMock.size()).thenReturn(1);
        when(iteratorMock.hasNext()).thenReturn(true, false);
        when(iteratorMock.next()).thenReturn(notificationMessageMock);
        when(listMock.iterator()).thenReturn(iteratorMock);
        when(appearanceMock.notificationMarkAsSeenSuccess()).thenReturn("Notifications marked as seen!");
        when(factoryMock.getHasUUIDs()).thenReturn(uuiDsAutoBeanMock);
        when(uuiDsAutoBeanMock.as()).thenReturn(uuiDsMock);

        spy.onNotificationToolbarMarkAsSeenClicked(ids, mockSuccessCallback, mockErrorCallback);
        verify(messageServiceFacadeMock).markAsSeen(eq(uuiDsMock), deCallbackStringCaptor.capture());

        deCallbackStringCaptor.getValue().onSuccess("result");
        verify(iplantAnnouncerMock).schedule(isA(SuccessAnnouncementConfig.class));
        verify(spy).fireCountUpdateEvent(eq("result"));
   }
}
