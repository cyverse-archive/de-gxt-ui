package org.iplantc.de.admin.desktop.client.toolAdmin.presenter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.iplantc.de.admin.desktop.client.toolAdmin.ToolAdminView;
import org.iplantc.de.admin.desktop.client.toolAdmin.events.AddToolSelectedEvent;
import org.iplantc.de.admin.desktop.client.toolAdmin.events.DeleteToolSelectedEvent;
import org.iplantc.de.admin.desktop.client.toolAdmin.events.SaveToolSelectedEvent;
import org.iplantc.de.admin.desktop.client.toolAdmin.events.ToolSelectedEvent;
import org.iplantc.de.admin.desktop.client.toolAdmin.gin.factory.ToolAdminViewFactory;
import org.iplantc.de.admin.desktop.client.toolAdmin.model.ToolProperties;
import org.iplantc.de.admin.desktop.client.toolAdmin.service.ToolAdminServiceFacade;
import org.iplantc.de.admin.desktop.client.toolAdmin.view.dialogs.DeleteToolDialog;
import org.iplantc.de.admin.desktop.client.toolAdmin.view.dialogs.OverwriteToolDialog;
import org.iplantc.de.client.models.errorHandling.ServiceErrorCode;
import org.iplantc.de.client.models.errorHandling.SimpleServiceError;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.client.models.tool.ToolAutoBeanFactory;
import org.iplantc.de.client.models.tool.ToolList;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.shared.AsyncProviderWrapper;
import org.iplantc.de.tools.client.ReactToolViews;
import org.iplantc.de.tools.client.gin.factory.EditToolViewFactory;
import org.iplantc.de.tools.client.views.manage.EditToolView;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwtmockito.GwtMockitoTestRunner;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;

import java.util.List;

/**
 * @author aramsey
 */
@RunWith(GwtMockitoTestRunner.class)
public class ToolAdminPresenterImplTest {

    @Mock ToolAdminViewFactory toolAdminViewFactoryMock;
    @Mock ToolAdminServiceFacade toolAdminServiceFacadeMock;
    @Mock ToolAutoBeanFactory toolAutoBeanFactoryMock;
    @Mock ToolProperties toolPropertiesMock;
    @Mock ToolAdminView.ToolAdminViewAppearance toolAdminViewAppearanceMock;
    @Mock ToolAdminView viewMock;
    @Mock ModelKeyProvider<Tool> idMock;
    @Mock AutoBean<ToolList> toolListAutoBeanMock;
    @Mock List<Tool> listToolMock;
    @Mock ListStore<Tool> listStoreToolMock;
    @Mock IplantAnnouncer iplantAnnouncerMock;
    @Mock AsyncProviderWrapper<OverwriteToolDialog> overwriteAppDialogMock;
    @Mock AsyncProviderWrapper<DeleteToolDialog> deleteAppDialogMock;
    @Mock EditToolViewFactory editToolViewFactoryMock;
    @Mock EditToolView editToolViewMock;
    @Mock Splittable toolSplMock;
    @Mock Tool toolMock;
    @Mock ReactToolViews.AdminEditToolProps baseEditToolPropsMock;

    @Captor ArgumentCaptor<AsyncCallback<Tool>> asyncCallbackToolCaptor;
    @Captor ArgumentCaptor<AsyncCallback<List<Tool>>> asyncCallbackToolListCaptor;
    @Captor ArgumentCaptor<AsyncCallback<Void>> asyncCallbackVoidCaptor;
    @Captor ArgumentCaptor<AsyncCallback<OverwriteToolDialog>> asyncCallbackOverwriteDlgCaptor;
    @Captor ArgumentCaptor<AsyncCallback<DeleteToolDialog>> asyncCallbackDeleteDlgCaptor;
    @Captor ArgumentCaptor<SelectEvent.SelectHandler> selectHandlerArgumentCaptor;

    private ToolAdminPresenterImpl uut;

    @Before
    public void setUp() {
        when(toolAdminViewFactoryMock.create(Matchers.<ListStore<Tool>>any())).thenReturn(viewMock);
        when(toolPropertiesMock.id()).thenReturn(idMock);

        when(toolAdminViewAppearanceMock.addToolSuccessText()).thenReturn("sample");
        when(toolAdminViewAppearanceMock.deleteToolSuccessText()).thenReturn("sample");
        when(toolAdminViewAppearanceMock.updateToolSuccessText()).thenReturn("sample");
        when(editToolViewFactoryMock.create(baseEditToolPropsMock)).thenReturn(editToolViewMock);
        uut = new ToolAdminPresenterImpl(toolAdminViewFactoryMock,
                                         toolAdminServiceFacadeMock,
                                         toolAutoBeanFactoryMock,
                                         toolPropertiesMock,
                                         toolAdminViewAppearanceMock,
                                         editToolViewFactoryMock) {
            @Override
            void checkForViceTool(Tool tool) {
            }

            @Override
            Tool convertSplittableToTool(Splittable toolSpl) {
                return toolMock;
            }

            @Override
            Splittable convertToolToSplittable(Tool tool) {
                return toolSplMock;
            }

            @Override
            ReactToolViews.AdminEditToolProps getBaseProps() {
                return baseEditToolPropsMock;
            }
        };
        uut.announcer = iplantAnnouncerMock;
        uut.overwriteAppDialog = overwriteAppDialogMock;
        uut.deleteAppDialog = deleteAppDialogMock;

        verifyConstructor(uut);
    }

    private void verifyConstructor(ToolAdminPresenterImpl uut) {
        verify(toolAdminViewFactoryMock).create(Matchers.<ListStore<Tool>>any());
        verify(viewMock).addAddToolSelectedEventHandler(eq(uut));
        verify(viewMock).addToolSelectedEventHandler(eq(uut));
        verify(viewMock).addDeleteToolSelectedEventHandler(eq(uut));

        verifyNoMoreInteractions(toolAdminViewFactoryMock);
    }

    @Test
    public void testOnAddToolSelected() {
        AddToolSelectedEvent addToolSelectedEventMock = mock(AddToolSelectedEvent.class);

        /** CALL METHOD UNDER TEST **/
        uut.onAddToolSelected(addToolSelectedEventMock);

        verify(editToolViewMock).edit(eq(null));
    }

    @Test
    public void testAddTool() {
        ToolList toolListMock = mock(ToolList.class);
        when(toolAutoBeanFactoryMock.getToolList()).thenReturn(toolListAutoBeanMock);
        when(toolListAutoBeanMock.as()).thenReturn(toolListMock);

        /** CALL METHOD UNDER TEST **/
        uut.addTool(toolSplMock);

        verify(toolAdminServiceFacadeMock).addTool(eq(toolListMock), asyncCallbackVoidCaptor.capture());

        /** CALL METHOD UNDER TEST **/
        asyncCallbackVoidCaptor.getValue().onSuccess(null);
        uut.updateView();
    }

    @Test
    public void testOnDeleteToolSelected_success() {
        DeleteToolSelectedEvent deleteToolSelectedEventMock = mock(DeleteToolSelectedEvent.class);
        Tool toolMock = mock(Tool.class);
        String idMock = "toolIdMock";

        when(toolMock.getId()).thenReturn(idMock);
        when(deleteToolSelectedEventMock.getTool()).thenReturn(toolMock);
        when(listStoreToolMock.findModelWithKey(anyString())).thenReturn(toolMock);

        /** CALL METHOD UNDER TEST **/
        uut.onDeleteToolSelected(deleteToolSelectedEventMock);

        verify(toolAdminServiceFacadeMock).deleteTool(eq(idMock), asyncCallbackVoidCaptor.capture());

        /** CALL METHOD UNDER TEST **/
        asyncCallbackVoidCaptor.getValue().onSuccess(null);
        uut.updateView();

    }

    @Test
    public void testOnDeleteToolSelected_fail() {
        DeleteToolSelectedEvent deleteToolSelectedEventMock = mock(DeleteToolSelectedEvent.class);
        Tool toolMock = mock(Tool.class);
        String idMock = "toolIdMock";

        when(toolMock.getId()).thenReturn(idMock);
        when(deleteToolSelectedEventMock.getTool()).thenReturn(toolMock);
        when(listStoreToolMock.findModelWithKey(anyString())).thenReturn(toolMock);

        ToolAdminPresenterImpl uuti = new ToolAdminPresenterImpl(toolAdminViewFactoryMock,
                                                                 toolAdminServiceFacadeMock,
                                                                 toolAutoBeanFactoryMock,
                                                                 toolPropertiesMock,
                                                                 toolAdminViewAppearanceMock,
                                                                 editToolViewFactoryMock) {
            @Override
            String getServiceError(Throwable caught) {
                return "ERR_NOT_WRITEABLE";
            }
        };
        uuti.deleteAppDialog = deleteAppDialogMock;

        /** CALL METHOD UNDER TEST **/
        uuti.onDeleteToolSelected(deleteToolSelectedEventMock);
        verify(toolAdminServiceFacadeMock).deleteTool(eq(idMock), asyncCallbackVoidCaptor.capture());

        String serviceError = uuti.getServiceError(mock(Throwable.class));
        Throwable caughtMock = mock(Throwable.class);

        /** CALL METHOD UNDER TEST **/
        asyncCallbackVoidCaptor.getValue().onFailure(caughtMock);
        assertEquals(serviceError, ServiceErrorCode.ERR_NOT_WRITEABLE.toString());
        verify(deleteAppDialogMock).get(asyncCallbackDeleteDlgCaptor.capture());

        DeleteToolDialog dialog = mock(DeleteToolDialog.class);

        /** CALL METHOD UNDER TEST **/
        asyncCallbackDeleteDlgCaptor.getValue().onSuccess(dialog);
        verify(dialog).setText(eq(caughtMock));
        verify(dialog).show();

        verifyNoMoreInteractions(dialog, toolAdminServiceFacadeMock);
    }

    @Test
    public void onToolSelected_callbackSuccess() {
        String idMock = "mockToolId";
        ToolSelectedEvent eventMock = mock(ToolSelectedEvent.class);
        when(toolMock.getId()).thenReturn(idMock);
        when(eventMock.getTool()).thenReturn(toolMock);

        /** CALL METHOD UNDER TEST **/
        uut.onToolSelected(eventMock);

        verify(viewMock).mask(any());
        verify(toolAdminServiceFacadeMock).getToolDetails(eq(idMock), asyncCallbackToolCaptor.capture());
        AsyncCallback<Tool> value = asyncCallbackToolCaptor.getValue();

        /** CALL METHOD UNDER TEST **/
        value.onSuccess(toolMock);
        verify(viewMock).unmask();
        verify(editToolViewMock).edit(eq(toolSplMock));

        verifyNoMoreInteractions(toolAdminServiceFacadeMock, viewMock);
    }

    @Test
    public void testUpdateTool() {
        /** CALL METHOD UNDER TEST **/
        uut.updateTool(toolSplMock);
        verify(toolAdminServiceFacadeMock).updateTool(eq(toolMock),
                                                      anyBoolean(),
                                                      asyncCallbackVoidCaptor.capture());
    }

    @Test
    public void testUpdateTool_overwriteFalse() {
        Tool toolMock = mock(Tool.class);
        String idMock = "sample";
        when(toolMock.getId()).thenReturn(idMock);


        /** CALL METHOD UNDER TEST **/
        uut.updateTool(toolMock, false);

        verify(toolAdminServiceFacadeMock).updateTool(eq(toolMock),
                                                      anyBoolean(),
                                                      asyncCallbackVoidCaptor.capture());

        asyncCallbackVoidCaptor.getValue().onSuccess(null);
        uut.updateView();
    }

    @Test
    public void testUpdateTool_overwriteTrue() {
        Tool toolMock = mock(Tool.class);
        String idMock = "sample";
        when(toolMock.getId()).thenReturn(idMock);

        SimpleServiceError simpleServiceErrorMock = mock(SimpleServiceError.class);
        when(simpleServiceErrorMock.getErrorCode()).thenReturn("ERR_NOT_WRITEABLE");

        ToolAdminPresenterImpl uuti = new ToolAdminPresenterImpl(toolAdminViewFactoryMock,
                                                                 toolAdminServiceFacadeMock,
                                                                 toolAutoBeanFactoryMock,
                                                                 toolPropertiesMock,
                                                                 toolAdminViewAppearanceMock,
                                                                 editToolViewFactoryMock) {
            @Override
            String getServiceError(Throwable caught) {
                return "ERR_NOT_WRITEABLE";
            }

            @Override
            void checkForViceTool(Tool tool) {
            }
        };
        uuti.overwriteAppDialog = overwriteAppDialogMock;
        uuti.editToolView = editToolViewMock;

        /** CALL METHOD UNDER TEST **/
        uuti.updateTool(toolMock, true);

        verify(toolAdminServiceFacadeMock).updateTool(Matchers.<Tool>any(),
                                                      anyBoolean(),
                                                      asyncCallbackVoidCaptor.capture());


        String serviceError = uuti.getServiceError(mock(Throwable.class));
        Throwable caughtMock = mock(Throwable.class);

        /** CALL METHOD UNDER TEST **/
        asyncCallbackVoidCaptor.getValue().onFailure(caughtMock);
        assertEquals(serviceError, ServiceErrorCode.ERR_NOT_WRITEABLE.toString());
        verify(overwriteAppDialogMock).get(asyncCallbackOverwriteDlgCaptor.capture());

        OverwriteToolDialog dialog = mock(OverwriteToolDialog.class);

        /** CALL METHOD UNDER TEST **/
        asyncCallbackOverwriteDlgCaptor.getValue().onSuccess(dialog);
        verify(dialog).setText(eq(caughtMock));
        verify(dialog).show();
        verify(dialog).addOkButtonSelectHandler(selectHandlerArgumentCaptor.capture());

        selectHandlerArgumentCaptor.getValue().onSelect(mock(SelectEvent.class));

    }

    @Test
    public void testUpdateView() {
        final ToolAdminPresenterImpl uuti = new ToolAdminPresenterImpl(toolAdminViewFactoryMock,
                                                                       toolAdminServiceFacadeMock,
                                                                       toolAutoBeanFactoryMock,
                                                                       toolPropertiesMock,
                                                                       toolAdminViewAppearanceMock,
                                                                       editToolViewFactoryMock) {
            @Override
            ListStore<Tool> createListStore(ToolProperties toolProps) {
                return listStoreToolMock;
            }
        };

        String searchTermMock = "searchTermMock";

        /** CALL METHOD UNDER TEST **/
        uuti.updateView(searchTermMock);
        verify(toolAdminServiceFacadeMock).getTools(eq(searchTermMock),
                                                    asyncCallbackToolListCaptor.capture());

        AsyncCallback<List<Tool>> listAsyncCallback = asyncCallbackToolListCaptor.getValue();

        List<Tool> toolListMock = Lists.newArrayList(mock(Tool.class));


        /** CALL METHOD UNDER TEST **/
        listAsyncCallback.onSuccess(toolListMock);
        verify(listStoreToolMock).replaceAll(eq(toolListMock));
        verifyNoMoreInteractions(listStoreToolMock, toolAdminServiceFacadeMock);
    }

}
