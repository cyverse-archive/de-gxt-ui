package org.iplantc.de.notifications.client.presenter;

import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.HasUUIDs;
import org.iplantc.de.client.models.notifications.NotificationAutoBeanFactory;
import org.iplantc.de.client.models.notifications.NotificationCategory;
import org.iplantc.de.client.models.notifications.NotificationMessage;
import org.iplantc.de.client.services.MessageServiceFacade;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;
import org.iplantc.de.notifications.client.events.DeleteNotificationsUpdateEvent;
import org.iplantc.de.notifications.client.events.NotificationCountUpdateEvent;
import org.iplantc.de.notifications.client.events.NotificationGridRefreshEvent;
import org.iplantc.de.notifications.client.events.NotificationSelectionEvent;
import org.iplantc.de.notifications.client.events.NotificationToolbarDeleteAllClickedEvent;
import org.iplantc.de.notifications.client.events.NotificationToolbarMarkAsSeenClickedEvent;
import org.iplantc.de.notifications.client.gin.factory.NotificationViewFactory;
import org.iplantc.de.notifications.client.model.NotificationMessageProperties;
import org.iplantc.de.notifications.client.views.NotificationToolbarView;
import org.iplantc.de.notifications.client.views.NotificationView;
import org.iplantc.de.shared.DECallback;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwtmockito.GxtMockitoTestRunner;
import com.google.web.bindery.autobean.shared.AutoBean;

import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;

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

    @Mock NotificationViewFactory viewFactoryMock;
    @Mock NotificationView.NotificationViewAppearance appearanceMock;
    @Mock NotificationView viewMock;
    @Mock NotificationMessageProperties messagePropertiesMock;
    @Mock MessageServiceFacade messageServiceFacadeMock;
    @Mock NotificationToolbarView toolbarViewMock;
    @Mock EventBus eventBusMock;
    @Mock NotificationCategory currentCategoryMock;
    @Mock ListStore<NotificationMessage> listStoreMock;
    @Mock List<NotificationMessage> listMock;
    @Mock NotificationMessage notificationMessageMock;
    @Mock Iterator<NotificationMessage> iteratorMock;
    @Mock IplantAnnouncer iplantAnnouncerMock;
    @Mock NotificationAutoBeanFactory factoryMock;
    @Mock List<NotificationMessage> notificationsMock;
    @Mock Stream<NotificationMessage> notificationStream;
    @Mock Stream<String> stringStream;
    @Mock List<String> idList;
    @Mock AutoBean<HasUUIDs> uuiDsAutoBeanMock;
    @Mock HasUUIDs uuiDsMock;

    @Captor ArgumentCaptor<DECallback<String>> deCallbackStringCaptor;
    @Captor ArgumentCaptor<AsyncCallback<String>> asyncCallbackStringCaptor;
    @Mock NotificationCountUpdateEvent mockCountUpdateEvent;

    private NotificationPresenterImpl uut;

    @Before
    public void setUp() {
        when(viewFactoryMock.create(listStoreMock)).thenReturn(viewMock);
        when(currentCategoryMock.toString()).thenReturn("sample");
        when(viewMock.getCurrentLoadConfig()).thenReturn(mock(FilterPagingLoadConfig.class));
        when(notificationMessageMock.getId()).thenReturn("id");

        uut = new NotificationPresenterImpl(viewFactoryMock,
                                            appearanceMock,
                                            toolbarViewMock,
                                            messagePropertiesMock,
                                            factoryMock,
                                            eventBusMock) {
            @Override
            ListStore<NotificationMessage> createListStore(NotificationMessageProperties messageProperties) {
                return listStoreMock;
            }

            @Override
            protected void fireCountUpdateEvent(String result) {
                return;
            }

            @Override
            List<String> convertNotificationsToIds(List<NotificationMessage> notifications) {
                return idList;
            }
        };
        uut.currentCategory = currentCategoryMock;
        uut.messageServiceFacade = messageServiceFacadeMock;
        uut.announcer = iplantAnnouncerMock;
    }

    @Test
    public void testOnNotificationGridRefresh_emptyListStore() {
        NotificationGridRefreshEvent eventMock = mock(NotificationGridRefreshEvent.class);
        when(listStoreMock.size()).thenReturn(0);

        uut.onNotificationGridRefresh(eventMock);
        verify(toolbarViewMock).setDeleteAllButtonEnabled(eq(false));
    }

    @Test
    public void testOnNotificationGridRefresh_nonEmptyListStore() {
        NotificationGridRefreshEvent eventMock = mock(NotificationGridRefreshEvent.class);
        when(listStoreMock.size()).thenReturn(5);

        uut.onNotificationGridRefresh(eventMock);

        verify(toolbarViewMock).setDeleteAllButtonEnabled(eq(true));
    }

    @Test
    public void testOnNotificationSelection_emptyListStore() {
        NotificationSelectionEvent eventMock = mock(NotificationSelectionEvent.class);
        when(eventMock.getNotifications()).thenReturn(listMock);
        when(listMock.size()).thenReturn(0);

        uut.onNotificationSelection(eventMock);

        verify(toolbarViewMock).setDeleteButtonEnabled(eq(false));
        verify(toolbarViewMock).setMarkAsSeenButtonEnabled(eq(false));
    }

    @Test
    public void testOnNotificationSelection_nonEmptyListStore() {
        NotificationSelectionEvent eventMock = mock(NotificationSelectionEvent.class);
        final NotificationMessage mockNm1 = mock(NotificationMessage.class);
        final NotificationMessage mockNm2 = mock(NotificationMessage.class);
        when(mockNm1.isSeen()).thenReturn(false);
        when(mockNm2.isSeen()).thenReturn(false);

        when(eventMock.getNotifications()).thenReturn(Lists.newArrayList(mockNm1, mockNm2));

        uut.onNotificationSelection(eventMock);

        verify(toolbarViewMock).setDeleteButtonEnabled(eq(true));
        verify(toolbarViewMock).setMarkAsSeenButtonEnabled(eq(true));
    }

    @Test
    public void testOnNotificationSelection_seenItemSelected() {
        NotificationSelectionEvent eventMock = mock(NotificationSelectionEvent.class);
        final NotificationMessage mockNm1 = mock(NotificationMessage.class);
        final NotificationMessage mockNm2 = mock(NotificationMessage.class);
        when(mockNm1.isSeen()).thenReturn(false);
        when(mockNm2.isSeen()).thenReturn(true);

        when(eventMock.getNotifications()).thenReturn(Lists.newArrayList(mockNm1, mockNm2));
        uut.onNotificationSelection(eventMock);
        verify(toolbarViewMock).setMarkAsSeenButtonEnabled(eq(false));

    }


    @Test
    public void testOnNotificationToolbarDeleteAllClicked() {
        NotificationToolbarDeleteAllClickedEvent eventMock = mock(NotificationToolbarDeleteAllClickedEvent.class);
        uut.onNotificationToolbarDeleteAllClicked(eventMock);

        verify(viewMock).mask();
        verify(messageServiceFacadeMock).deleteAll(eq(currentCategoryMock), deCallbackStringCaptor.capture());

        DECallback<String> asyncCallback = deCallbackStringCaptor.getValue();

        asyncCallback.onSuccess("result");
        verify(viewMock).unmask();
        verify(viewMock).loadNotifications(eq(viewMock.getCurrentLoadConfig()));
        verify(eventBusMock).fireEvent(isA(DeleteNotificationsUpdateEvent.class));

    }

    @Test
    public void deleteNotifications() {
        FilterPagingLoadConfig configMock = mock(FilterPagingLoadConfig.class);
        when(factoryMock.getHasUUIDs()).thenReturn(uuiDsAutoBeanMock);
        when(uuiDsAutoBeanMock.as()).thenReturn(uuiDsMock);
        when(viewMock.getCurrentLoadConfig()).thenReturn(configMock);

        uut.deleteNotifications(notificationsMock);

        verify(messageServiceFacadeMock).deleteMessages(eq(uuiDsMock), deCallbackStringCaptor.capture());

        deCallbackStringCaptor.getValue().onSuccess("result");
        verify(viewMock).loadNotifications(eq(configMock));
        verify(eventBusMock).fireEvent(isA(DeleteNotificationsUpdateEvent.class));
    }

    @Test
    public void testOnNotificationToolbarMarkAsReadClicked() {
        NotificationToolbarMarkAsSeenClickedEvent eventMock = mock(NotificationToolbarMarkAsSeenClickedEvent.class);
        NotificationPresenterImpl spy = spy(uut);

        when(listMock.isEmpty()).thenReturn(false);
        when(listMock.size()).thenReturn(1);
        when(iteratorMock.hasNext()).thenReturn(true, false);
        when(iteratorMock.next()).thenReturn(notificationMessageMock);
        when(listMock.iterator()).thenReturn(iteratorMock);
        when(viewMock.getSelectedItems()).thenReturn(listMock);
        when(appearanceMock.notificationMarkAsSeenSuccess()).thenReturn("Notifications marked as seen!");

        spy.onNotificationToolbarMarkAsSeenClicked(eventMock);

        verify(messageServiceFacadeMock).markAsSeen(isA(List.class), deCallbackStringCaptor.capture());

        deCallbackStringCaptor.getValue().onSuccess("result");
        verify(iplantAnnouncerMock).schedule(isA(SuccessAnnouncementConfig.class));
        verify(spy).fireCountUpdateEvent(eq("result"));
   }
}
