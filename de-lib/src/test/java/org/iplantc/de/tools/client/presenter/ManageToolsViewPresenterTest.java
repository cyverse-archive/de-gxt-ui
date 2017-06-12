package org.iplantc.de.tools.client.presenter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.apps.client.models.ToolFilter;
import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.client.services.ToolServices;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.shared.AppsCallback;
import org.iplantc.de.shared.AsyncProviderWrapper;
import org.iplantc.de.tools.client.events.AddNewToolSelected;
import org.iplantc.de.tools.client.events.EditToolSelected;
import org.iplantc.de.tools.client.events.RequestToMakeToolPublicSelected;
import org.iplantc.de.tools.client.events.RequestToolSelected;
import org.iplantc.de.tools.client.events.ShareToolsSelected;
import org.iplantc.de.tools.client.events.ShowToolInfoEvent;
import org.iplantc.de.tools.client.events.ToolFilterChanged;
import org.iplantc.de.tools.client.events.ToolSelectionChangedEvent;
import org.iplantc.de.tools.client.views.dialogs.EditToolDialog;
import org.iplantc.de.tools.client.views.dialogs.NewToolRequestDialog;
import org.iplantc.de.tools.client.views.dialogs.ToolInfoDialog;
import org.iplantc.de.tools.client.views.dialogs.ToolSharingDialog;
import org.iplantc.de.tools.client.views.manage.ManageToolsToolbarView;
import org.iplantc.de.tools.client.views.manage.ManageToolsView;
import org.iplantc.de.tools.client.views.requests.NewToolRequestFormView;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwtmockito.GwtMockitoTestRunner;

import com.sencha.gxt.widget.core.client.container.SimpleContainer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sriram on 6/7/17.
 */
@RunWith(GwtMockitoTestRunner.class)
public class ManageToolsViewPresenterTest {

    @Mock
    ManageToolsView toolsViewMock;

    @Mock
    ManageToolsView.ManageToolsViewAppearance appearanceMock;

    @Mock
    ToolServices toolServicesMock;

    @Mock
    AsyncProviderWrapper<EditToolDialog> editDialogProviderMock;

    @Captor
    ArgumentCaptor<AsyncCallback<EditToolDialog>> editDialogCaptor;

    @Mock
    EditToolDialog editToolDialogMock;

    @Mock
    IplantAnnouncer announcerMock;

    @Mock
    AsyncProviderWrapper<ToolSharingDialog> shareDialogProviderMock;

    @Captor
    ArgumentCaptor<AsyncCallback<ToolSharingDialog>> sharingDialogCaptor;

    @Mock
    ToolSharingDialog toolSharingDialogMock;

    @Mock
    AsyncProviderWrapper<NewToolRequestDialog> newToolRequestDialogProviderMock;

    @Captor
    ArgumentCaptor<AsyncCallback<NewToolRequestDialog>> newToolDialogCaptor;

    @Mock
    NewToolRequestDialog toolRequestMock;

    @Mock
    AsyncProviderWrapper<ToolInfoDialog> toolInfoDialogProviderMock;

    @Captor
    ArgumentCaptor<AsyncCallback<ToolInfoDialog>> toolInfoDialogCaptor;

    @Mock
    ToolInfoDialog toolInfoDialogMock;

    @Mock
    EventBus eventBusMock;

    @Mock
    List<Tool> currentSelectionMock;

    @Mock
    ManageToolsToolbarView manageToolsToolbarViewMock;

    @Captor
    ArgumentCaptor<AppsCallback<List<Tool>>> toolListCaptor;

    @Captor
    ArgumentCaptor<AppsCallback<List<App>>> appListCaptor;


    @Captor
    ArgumentCaptor<AppsCallback<Tool>> toolCaptor;


    @Captor
    ArgumentCaptor<AppsCallback<Void>> voidCaptor;

    private ManageToolsViewPresenter uut;

    @Mock
    Iterator<Tool> iteratorMock;

    @Before
    public void setUp() {
        uut = new ManageToolsViewPresenter(appearanceMock);
        uut.toolsView = toolsViewMock;
        uut.announcer = announcerMock;
        uut.appearance = appearanceMock;
        uut.toolServices = toolServicesMock;
        uut.editDialogProvider = editDialogProviderMock;
        uut.shareDialogProvider = shareDialogProviderMock;
        uut.newToolRequestDialogProvider = newToolRequestDialogProviderMock;
        uut.toolInfoDialogProvider = toolInfoDialogProviderMock;
        uut.eventBus = eventBusMock;
        uut.currentSelection = currentSelectionMock;
    }

    @Test
    public void testGo() {
        HasOneWidget containerMock = mock(HasOneWidget.class);
        when(toolsViewMock.getToolbar()).thenReturn(manageToolsToolbarViewMock);

        SimpleContainer scMock = mock(SimpleContainer.class);
        when(toolsViewMock.asWidget()).thenReturn(scMock);

        uut.go(containerMock);
        verify(containerMock).setWidget(eq(scMock));

        verify(manageToolsToolbarViewMock, times(1)).addBeforeToolSearchEventHandler(eq(toolsViewMock));
        verify(manageToolsToolbarViewMock,
               times(1)).addToolSearchResultLoadEventHandler(eq(toolsViewMock));
        verify(manageToolsToolbarViewMock, times(1)).addRefreshToolsSelectedEventHandler(eq(uut));
        verify(manageToolsToolbarViewMock, times(1)).addNewToolSelectedHandler(eq(uut));
        verify(manageToolsToolbarViewMock, times(1)).addShareToolselectedHandler(eq(uut));
        verify(manageToolsToolbarViewMock, times(1)).addDeleteToolsSelectedHandler(eq(uut));
        verify(manageToolsToolbarViewMock, times(1)).addToolFilterChangedHandler(eq(uut));
        verify(manageToolsToolbarViewMock, times(1)).addRequestToolSelectedHandler(eq(uut));
        verify(manageToolsToolbarViewMock, times(1)).addEditToolSelectedHandler(eq(uut));
        verify(manageToolsToolbarViewMock, times(1)).addRequestToMakeToolPublicSelectedHandler(eq(uut));

        verify(toolsViewMock, times(1)).addToolSelectionChangedEventHandler(uut);
        verify(toolsViewMock, times(1)).addShowToolInfoEventHandlers(uut);
    }

    @Test
    public void testOnToolSelectionChanged() {
        when(toolsViewMock.getToolbar()).thenReturn(manageToolsToolbarViewMock);

        ToolSelectionChangedEvent tsceMock = mock(ToolSelectionChangedEvent.class);
        Tool t1Mock = mock(Tool.class);
        Tool t2Mock = mock(Tool.class);
        Tool t3Mock = mock(Tool.class);

        when(tsceMock.getToolSelection()).thenReturn(Arrays.asList(t1Mock, t2Mock, t3Mock));
        uut.onToolSelectionChanged(tsceMock);
        assertEquals(3, uut.currentSelection.size());

    }

    @Test
    public void testLoadTools() {
        Boolean isPublic = null;
        when(appearanceMock.mask()).thenReturn("Loading...");
        Tool t1Mock = mock(Tool.class);
        Tool t2Mock = mock(Tool.class);
        Tool t3Mock = mock(Tool.class);
        List<Tool> tools = Arrays.asList(t1Mock, t2Mock, t3Mock);

        uut.loadTools(isPublic);
        verify(toolServicesMock).searchTools(eq(isPublic), eq(null), toolListCaptor.capture());
        toolListCaptor.getValue().onSuccess(tools);

        verify(toolsViewMock).unmask();
        verify(toolsViewMock).loadTools(tools);
    }

    @Test
    public void testAddTool() {
        Tool t1Mock = mock(Tool.class);
        when(t1Mock.getName()).thenReturn("Shiny Tool");
        Command mockCmd = mock(Command.class);
        when(appearanceMock.toolAdded("Shiny Tool")).thenReturn("Tool Shiny Tool added successfully!");

        uut.addTool(t1Mock, mockCmd);
        verify(toolServicesMock).addTool(eq(t1Mock), toolCaptor.capture());
        toolCaptor.getValue().onSuccess(t1Mock);

        verify(mockCmd).execute();
        verify(toolsViewMock).addTool(eq(t1Mock));
    }

    @Test
    public void testUpdateTool() {
        Tool t1Mock = mock(Tool.class);
        when(t1Mock.getName()).thenReturn("Shiny Tool");
        Command mockCmd = mock(Command.class);
        when(appearanceMock.toolUpdated("Shiny Tool")).thenReturn("Tool Shiny Tool updated successfully!");

        uut.updateTool(t1Mock, mockCmd);
        verify(toolServicesMock).updateTool(eq(t1Mock), toolCaptor.capture());
        toolCaptor.getValue().onSuccess(t1Mock);

        verify(mockCmd).execute();
        verify(toolsViewMock).updateTool(eq(t1Mock));
    }

    @Test
    public void testDoDelete() {
        Tool t1Mock = mock(Tool.class);
        when(t1Mock.getName()).thenReturn("Shiny Tool");
        when(appearanceMock.toolDeleted("Shiny Tool")).thenReturn("Tool Shiny Tool deleted successfully!");

        uut.doDelete(t1Mock);
        verify(toolServicesMock).deleteTool(eq(t1Mock), voidCaptor.capture());
        voidCaptor.getValue().onSuccess(null);

        verify(toolsViewMock).removeTool(t1Mock);
    }

    @Test
    public void testOnToolFilterChanged() {
        ToolFilterChanged tfcMock = mock(ToolFilterChanged.class);
        when(toolsViewMock.getToolbar()).thenReturn(manageToolsToolbarViewMock);

        when(tfcMock.getFilter()).thenReturn(ToolFilter.ALL);
        uut.onToolFilterChanged(tfcMock);
        verify(toolServicesMock).searchTools(eq(null), eq(null), toolListCaptor.capture());

        when(tfcMock.getFilter()).thenReturn(ToolFilter.MY_TOOLS);
        uut.onToolFilterChanged(tfcMock);
        verify(toolServicesMock).searchTools(eq(false), eq(null), toolListCaptor.capture());

        when(tfcMock.getFilter()).thenReturn(ToolFilter.PUBLIC);
        uut.onToolFilterChanged(tfcMock);
        verify(toolServicesMock).searchTools(eq(true), eq(null), toolListCaptor.capture());
    }

    @Test
    public void testOnNewToolSelected() {
        AddNewToolSelected antsMock = mock(AddNewToolSelected.class);
        when(appearanceMock.editDialogHeight()).thenReturn("300px");
        when(appearanceMock.editDialogWidth()).thenReturn("600px");

        uut.onNewToolSelected(antsMock);

        verify(editDialogProviderMock).get(editDialogCaptor.capture());
        editDialogCaptor.getValue().onSuccess(editToolDialogMock);
        verify(editToolDialogMock).show(eq(uut));
    }

    @Test
    public void testOnEditToolSelected() {
        EditToolSelected etsMock = mock(EditToolSelected.class);
        Tool t1Mock = mock(Tool.class);
        when(appearanceMock.editDialogHeight()).thenReturn("300px");
        when(appearanceMock.editDialogWidth()).thenReturn("600px");
        when(currentSelectionMock.isEmpty()).thenReturn(false);
        when(currentSelectionMock.size()).thenReturn(1);
        when(iteratorMock.hasNext()).thenReturn(true, false);
        when(iteratorMock.next()).thenReturn(t1Mock);
        when(currentSelectionMock.iterator()).thenReturn(iteratorMock);
        when(uut.getSelectedTool()).thenReturn(t1Mock);

        uut.onEditToolSelected(etsMock);

        verify(editDialogProviderMock).get(editDialogCaptor.capture());
        editDialogCaptor.getValue().onSuccess(editToolDialogMock);
        verify(editToolDialogMock).editTool(eq(t1Mock));
        verify(editToolDialogMock).show(eq(uut));
    }

    @Test
    public void testOnShareToolSelected() {
        ShareToolsSelected stsMock = mock(ShareToolsSelected.class);
        Tool t1Mock = mock(Tool.class);
        when(currentSelectionMock.isEmpty()).thenReturn(false);
        when(currentSelectionMock.size()).thenReturn(1);
        when(iteratorMock.hasNext()).thenReturn(true, false);
        when(iteratorMock.next()).thenReturn(t1Mock);
        when(currentSelectionMock.iterator()).thenReturn(iteratorMock);

        uut.onShareToolselected(stsMock);

        verify(shareDialogProviderMock).get(sharingDialogCaptor.capture());
        sharingDialogCaptor.getValue().onSuccess(toolSharingDialogMock);
        verify(toolSharingDialogMock).show(eq(currentSelectionMock));
    }

    @Test
    public void testOnRequestToolSelected() {
        RequestToolSelected rtsMock = mock(RequestToolSelected.class);

        uut.onRequestToolSelected(rtsMock);

        verify(newToolRequestDialogProviderMock).get(newToolDialogCaptor.capture());
        newToolDialogCaptor.getValue().onSuccess(toolRequestMock);
        verify(toolRequestMock).show(eq(NewToolRequestFormView.Mode.NEWTOOL));
    }

    @Test
    public void testOnRequestToMakePublicSelected() {
        RequestToMakeToolPublicSelected rtmtpsMock = mock(RequestToMakeToolPublicSelected.class);
        Tool t1Mock = mock(Tool.class);
        when(currentSelectionMock.isEmpty()).thenReturn(false);
        when(currentSelectionMock.size()).thenReturn(1);
        when(iteratorMock.hasNext()).thenReturn(true, false);
        when(iteratorMock.next()).thenReturn(t1Mock);
        when(currentSelectionMock.iterator()).thenReturn(iteratorMock);
        when(uut.getSelectedTool()).thenReturn(t1Mock);

        uut.onRequestToMakeToolPublicSelected(rtmtpsMock);

        verify(newToolRequestDialogProviderMock).get(newToolDialogCaptor.capture());
        newToolDialogCaptor.getValue().onSuccess(toolRequestMock);
        verify(toolRequestMock).setTool(eq(t1Mock));
        verify(toolRequestMock).show(eq(NewToolRequestFormView.Mode.MAKEPUBLIC));
    }

    @Test
    public void testOnShowToolInfo() {
        ShowToolInfoEvent stieMock = mock(ShowToolInfoEvent.class);
        Tool t1Mock = mock(Tool.class);
        when(t1Mock.getId()).thenReturn("1234567890");
        when(stieMock.getTool()).thenReturn(t1Mock);

        App a1 = mock(App.class);
        List<App> appList = Arrays.asList(a1);

        when(currentSelectionMock.isEmpty()).thenReturn(false);
        when(currentSelectionMock.size()).thenReturn(1);
        when(iteratorMock.hasNext()).thenReturn(true, false);
        when(iteratorMock.next()).thenReturn(t1Mock);
        when(currentSelectionMock.iterator()).thenReturn(iteratorMock);
        when(uut.getSelectedTool()).thenReturn(t1Mock);


        uut.onShowToolInfo(stieMock);

        verify(toolServicesMock).getAppsForTool(eq(stieMock.getTool().getId()), appListCaptor.capture());
        appListCaptor.getValue().onSuccess(appList);

        verify(toolInfoDialogProviderMock).get(toolInfoDialogCaptor.capture());
        toolInfoDialogCaptor.getValue().onSuccess(toolInfoDialogMock);
        verify(toolInfoDialogMock).show(eq(t1Mock), eq(appList));
    }

}
