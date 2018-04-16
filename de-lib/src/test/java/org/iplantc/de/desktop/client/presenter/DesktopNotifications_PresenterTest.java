package org.iplantc.de.desktop.client.presenter;

import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.HasUUIDs;
import org.iplantc.de.client.models.WindowState;
import org.iplantc.de.client.models.WindowType;
import org.iplantc.de.client.models.notifications.NotificationAutoBeanFactory;
import org.iplantc.de.client.models.notifications.NotificationCategory;
import org.iplantc.de.client.models.notifications.NotificationList;
import org.iplantc.de.client.models.notifications.NotificationMessage;
import org.iplantc.de.client.services.MessageServiceFacade;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.desktop.client.DesktopView;
import org.iplantc.de.desktop.client.presenter.util.MessagePoller;
import org.iplantc.de.desktop.client.views.widgets.UnseenNotificationsView;
import org.iplantc.de.notifications.client.utils.NotificationUtil;
import org.iplantc.de.notifications.client.utils.NotifyInfo;
import org.iplantc.de.resources.client.messages.IplantNewUserTourStrings;
import org.iplantc.de.shared.DECallback;
import org.iplantc.de.shared.DEProperties;
import org.iplantc.de.shared.NotificationCallback;

import com.google.common.collect.Lists;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwtmockito.GxtMockitoTestRunner;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.WindowManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author jstroot
 */
@RunWith(GxtMockitoTestRunner.class)
public class DesktopNotifications_PresenterTest {

    @Mock IplantNewUserTourStrings tourStringsMock;
    @Mock EventBus eventBusMock;
    @Mock WindowManager windowMangerMock;
    @Mock DesktopView.Presenter presenterMock;
    @Mock DesktopView viewMock;
    @Mock DesktopPresenterEventHandler globalEvntHndlrMock;
    @Mock DesktopPresenterWindowEventHandler windowEvntHndlrMock;
    @Mock DesktopWindowManager desktopWindowManagerMock;
    @Mock MessagePoller msgPollerMock;
    @Mock NotifyInfo notifyInfoMock;
    @Mock DesktopView.Presenter.DesktopPresenterAppearance appearanceMock;
    @Mock NotificationUtil notificationUtilMock;
    @Mock IplantAnnouncer announcerMock;
    @Mock ListStore<NotificationMessage> notificationStoreMock;
    @Mock NotificationAutoBeanFactory notificationFactoryMock;
    @Mock HasUUIDs hasUUIDsMock;
    @Mock AutoBean<HasUUIDs> hasUUIDsAutoBeanMock;
    @Mock MessageServiceFacade messageServiceFacadeMock;

    @Mock ListStore<NotificationMessage> msgStoreMock;

    @Captor ArgumentCaptor<DECallback<String>> stringAsyncCaptor;
    @Captor ArgumentCaptor<DECallback<Void>> voidAsyncCaptor;

    DesktopPresenterImpl uut;

    @Before
    public void setup(){
        uut = new DesktopPresenterImpl(viewMock,
                                       globalEvntHndlrMock,
                                       windowEvntHndlrMock,
                                       eventBusMock,
                                       windowMangerMock,
                                       desktopWindowManagerMock,
                                       msgPollerMock,
                                       appearanceMock) {
            @Override
            void initKBShortCuts() {
                // Test stub, Do nothing
            }

            @Override
            void processQueryStrings() {
                // Test stub, Do nothing
            }

            @Override
            void setBrowserContextMenuEnabled(boolean enabled) {
                // Test stub, Do nothing
            }

            public List<WindowState> getWindowStates() {
                return new ArrayList<>();
            }
        };

        uut.messageServiceFacade = messageServiceFacadeMock;
        uut.deProperties = mock(DEProperties.class);
        uut.announcer = announcerMock;
        uut.notificationUtil = notificationUtilMock;
        uut.notificationFactory = notificationFactoryMock;
    }

    @Test public void recentNotificationsFetchedAtWebappInitialization() {

        /** CALL METHOD UNDER TEST **/
        uut.postBootstrap(mock(Panel.class));
        verify(uut.messageServiceFacade).getRecentMessages(Matchers.<AsyncCallback<NotificationList>>any());
        // TODO JDS Expand test to verify that notification store is updated
    }

    @Test public void notificationMarkedAsSeenWhenSelected() {
        final NotificationMessage mockMsg = mock(NotificationMessage.class);
        final NotificationCategory category = NotificationCategory.ALL;
        when(mockMsg.getCategory()).thenReturn(category);
        when(mockMsg.getContext()).thenReturn("context");

        /** CALL METHOD UNDER TEST **/
        uut.onNotificationSelected(mockMsg);

        when(viewMock.getNotificationStore()).thenReturn(msgStoreMock);
        when(msgStoreMock.getAll()).thenReturn(Arrays.asList(mockMsg));
        when(msgStoreMock.findModel(mockMsg)).thenReturn(mockMsg);
        verify(uut.messageServiceFacade).markAsSeen(eq(mockMsg), stringAsyncCaptor.capture());
        Splittable parent = StringQuoter.createSplittable();
        StringQuoter.create("1").assign(parent, "count");
        stringAsyncCaptor.getValue().onSuccess(parent.getPayload());
        verify(mockMsg).setSeen(eq(true));
        verify(msgStoreMock).update(eq(mockMsg));
    }

    @Test public void allNotificationsMarkedAsSeenWhenMarkAllSeenLinkClicked() {
        DesktopPresenterImpl testPresenter = spy(uut);
        when(appearanceMock.markAllAsSeenSuccess()).thenReturn("Mock success");

        when(viewMock.getNotificationStore()).thenReturn(msgStoreMock);
        when(msgStoreMock.getAll()).thenReturn(Lists.<NotificationMessage>newArrayList());
        UnseenNotificationsView testUnseenNotification = new UnseenNotificationsView();
        testUnseenNotification.setPresenter(testPresenter);

        testUnseenNotification.onMarkAllSeenClicked(mock(ClickEvent.class));
        verify(testPresenter).doMarkAllSeen(eq(true));
        verify(testPresenter.messageServiceFacade).markAllNotificationsSeen(voidAsyncCaptor.capture());
        voidAsyncCaptor.getValue().onSuccess(null);
        verify(viewMock).setUnseenNotificationCount(eq(0));
        // TODO JDS Expand test to verify that notification store is updated
    }


    @Test
    public void onJoinTeamRequestProcessed() {
        NotificationMessage messageMock = mock(NotificationMessage.class);
        when(desktopWindowManagerMock.isOpen(WindowType.NOTIFICATIONS)).thenReturn(false);
        when(viewMock.getNotificationStore()).thenReturn(notificationStoreMock);
        when(notificationFactoryMock.getHasUUIDs()).thenReturn(hasUUIDsAutoBeanMock);
        when(hasUUIDsAutoBeanMock.as()).thenReturn(hasUUIDsMock);
        when(messageMock.getId()).thenReturn("id");

        /** CALL METHOD UNDER TEST **/
        uut.onJoinTeamRequestProcessed(messageMock);
        verify(notificationStoreMock).remove(eq(messageMock));

        verify(hasUUIDsMock).setUUIDs(anyList());

        verify(messageServiceFacadeMock).deleteMessages(eq(hasUUIDsMock), isA(NotificationCallback.class));
    }

}
