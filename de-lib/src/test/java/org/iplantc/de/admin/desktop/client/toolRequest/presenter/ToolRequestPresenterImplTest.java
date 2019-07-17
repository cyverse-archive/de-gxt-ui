package org.iplantc.de.admin.desktop.client.toolRequest.presenter;

import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.admin.desktop.client.toolAdmin.ToolAdminView;
import org.iplantc.de.admin.desktop.client.toolAdmin.events.PublishToolEvent;
import org.iplantc.de.admin.desktop.client.toolAdmin.gin.factory.ToolAdminViewFactory;
import org.iplantc.de.admin.desktop.client.toolAdmin.service.ToolAdminServiceFacade;
import org.iplantc.de.admin.desktop.client.toolRequest.ToolRequestView;
import org.iplantc.de.admin.desktop.client.toolRequest.events.AdminMakeToolPublicSelectedEvent;
import org.iplantc.de.admin.desktop.client.toolRequest.service.ToolRequestServiceFacade;
import org.iplantc.de.admin.desktop.client.toolRequest.view.ToolRequestDetailsPanel;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.client.models.tool.ToolAutoBeanFactory;
import org.iplantc.de.client.models.toolRequests.ToolRequest;
import org.iplantc.de.client.models.toolRequests.ToolRequestDetails;
import org.iplantc.de.client.models.toolRequests.ToolRequestUpdate;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.tools.client.gin.factory.EditToolViewFactory;
import org.iplantc.de.tools.client.views.manage.EditToolView;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwtmockito.GwtMockitoTestRunner;
import com.google.web.bindery.autobean.shared.Splittable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

/**
 * Created by sriram on 6/8/17.
 */
@RunWith(GwtMockitoTestRunner.class)
public class ToolRequestPresenterImplTest {
    @Mock
    ToolRequestView mockRequestView;

    @Mock
    ToolRequestServiceFacade mockRequestServiceFacade;

    @Mock
    UserInfo mockUserInfo;

    @Mock EditToolViewFactory editToolViewFactoryMock;
    @Mock EditToolView editToolViewMock;
    @Mock ToolAutoBeanFactory factoryMock;
    @Mock Tool toolMock;
    @Mock Splittable toolSplMock;

    @Mock
    ToolRequestView.Presenter.ToolRequestPresenterAppearance mockAppearance;

    @Mock
    ToolAdminViewFactory mockAdminFactory;

    @Mock
    ToolAdminView mockAdminView;

    @Mock
    ToolAdminServiceFacade mockToolAdminServiceFacade;

    @Mock
    IplantAnnouncer mockAnnouncer;

    @Captor
    ArgumentCaptor<AsyncCallback<List<ToolRequest>>> listToolRequestCaptor;

    @Captor
    ArgumentCaptor<AsyncCallback<Tool>> toolCaptor;

    @Captor
    ArgumentCaptor<AsyncCallback<Void>> voidCaptor;

    @Captor
    ArgumentCaptor<AsyncCallback<ToolRequestDetails>> toolRequestDetailsCaptor;

    @Mock
    ToolRequestDetailsPanel mockDetailsPanel;

    private ToolRequestPresenterImpl uut;

    @Before
    public void setUp() {
        uut = new ToolRequestPresenterImpl(mockRequestView,
                                           mockRequestServiceFacade,
                                           mockUserInfo,
                                           mockAppearance,
                                           editToolViewFactoryMock,
                                           factoryMock,
                                           mockToolAdminServiceFacade){
            @Override
            Tool convertSplittableToTool(Splittable toolSpl) {
                return toolMock;
            }

            @Override
            Splittable convertToolToSplittable(Tool tool) {
                return toolSplMock;
            }
        };
        uut.view = mockRequestView;
        uut.toolReqService = mockRequestServiceFacade;
        uut.userInfo = mockUserInfo;
        uut.appearance = mockAppearance;
        uut.editToolViewFactory = editToolViewFactoryMock;
        uut.editToolView = editToolViewMock;
        uut.factory = factoryMock;
        uut.toolAdminServiceFacade = mockToolAdminServiceFacade;
        uut.announcer = mockAnnouncer;
    }

    @Test
    public void testGo() {
        when(mockRequestView.getDetailsPanel()).thenReturn(mockDetailsPanel);
        HasOneWidget containerMock = mock(HasOneWidget.class);
        when(mockAppearance.getToolRequestsLoadingMask()).thenReturn("Loading...");
        when(mockUserInfo.getUsername()).thenReturn("admin");
        ToolRequest tr1 = mock(ToolRequest.class);
        ToolRequest tr2 = mock(ToolRequest.class);
        List<ToolRequest> toolRequestList = Arrays.asList(tr1, tr2);

        uut.go(containerMock);
        verify(mockRequestView).mask(eq(mockAppearance.getToolRequestsLoadingMask()));
        verify(containerMock).setWidget(eq(mockRequestView));
        verify(mockRequestServiceFacade).getToolRequests(eq(null),
                                                         eq("admin"),
                                                         listToolRequestCaptor.capture());
        listToolRequestCaptor.getValue().onSuccess(toolRequestList);
        verify(mockRequestView).unmask();
        verify(mockRequestView).setToolRequests(eq(toolRequestList));
    }

    @Test
    public void testFetchToolRequestDetails() {
        ToolRequest trMock = mock(ToolRequest.class);
        ToolRequestDetails trDetailsMock = mock(ToolRequestDetails.class);
        when(mockAppearance.getToolRequestDetailsLoadingMask()).thenReturn("Loading...");

        uut.fetchToolRequestDetails(trMock);

        verify(mockRequestServiceFacade).getToolRequestDetails(eq(trMock),
                                                               toolRequestDetailsCaptor.capture());
        toolRequestDetailsCaptor.getValue().onSuccess(trDetailsMock);
        verify(mockRequestView).unmaskDetailsPanel();
        verify(mockRequestView).setDetailsPanel(trDetailsMock);
    }

    @Test
    public void testUpdateToolRequest() {
        ToolRequestDetails trDetailsMock = mock(ToolRequestDetails.class);
        ToolRequestUpdate trUpdateMock = mock(ToolRequestUpdate.class);
        when(mockAppearance.toolRequestUpdateSuccessMessage()).thenReturn("Request updated succssfully!");

        uut.updateToolRequest("1234567890", trUpdateMock);

        verify(mockRequestServiceFacade).updateToolRequest(eq("1234567890"),
                                                           eq(trUpdateMock),
                                                           toolRequestDetailsCaptor.capture());
        toolRequestDetailsCaptor.getValue().onSuccess(trDetailsMock);
        verify(mockRequestView).update(eq(trUpdateMock), eq(trDetailsMock));
    }

    @Test
    public void testGetToolDetails() {
        AdminMakeToolPublicSelectedEvent amtpseMock = mock(AdminMakeToolPublicSelectedEvent.class);
        when(amtpseMock.getToolId()).thenReturn("1234567890");

        uut.getToolDetails(amtpseMock);

        verify(mockToolAdminServiceFacade).getToolDetails(eq(amtpseMock.getToolId()),
                                                          toolCaptor.capture());
        toolCaptor.getValue().onSuccess(toolMock);
        verify(editToolViewMock).edit(eq(toolSplMock));
   }

   @Test
    public void testOnPublish() {
       when(mockAppearance.publishSuccess()).thenReturn("Tool published successfully!");

       uut.onPublish(toolSplMock);

       verify(mockToolAdminServiceFacade).publishTool(eq(toolMock), voidCaptor.capture());
       voidCaptor.getValue().onSuccess(null);
   }
}

