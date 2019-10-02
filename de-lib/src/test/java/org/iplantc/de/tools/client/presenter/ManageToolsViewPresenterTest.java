package org.iplantc.de.tools.client.presenter;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.client.models.tool.ToolAutoBeanFactory;
import org.iplantc.de.client.models.tool.ToolContainer;
import org.iplantc.de.client.models.tool.ToolList;
import org.iplantc.de.client.services.ToolServices;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.shared.AppsCallback;
import org.iplantc.de.shared.AsyncProviderWrapper;
import org.iplantc.de.tools.client.ReactToolViews;
import org.iplantc.de.tools.client.gin.factory.EditToolViewFactory;
import org.iplantc.de.tools.client.gin.factory.ManageToolsViewFactory;
import org.iplantc.de.tools.client.views.dialogs.NewToolRequestDialog;
import org.iplantc.de.tools.client.views.dialogs.ToolInfoDialog;
import org.iplantc.de.tools.client.views.dialogs.ToolSharingDialog;
import org.iplantc.de.tools.client.views.manage.EditToolView;
import org.iplantc.de.tools.client.views.manage.ManageToolsView;
import org.iplantc.de.tools.client.views.requests.NewToolRequestFormView;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwtmockito.GwtMockitoTestRunner;

import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.widget.core.client.container.SimpleContainer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;

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

    @Mock Splittable toolSplMock;
    @Mock Tool toolMock;

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

    @Mock EditToolView editToolViewMock;
    @Mock EditToolViewFactory editToolViewFactoryMock;
    @Mock ManageToolsViewFactory manageToolsViewFactoryMock;
    @Mock ReactToolViews.EditToolProps baseEditToolPropsMock;
    @Mock ToolContainer toolContainerMock;
    @Mock ToolList toolListMock;
    @Mock Splittable toolListSpl;
    @Mock Splittable toolListToolsSpl;

    @Mock
    EventBus eventBusMock;

    @Mock
    List<Tool> currentSelectionMock;

    @Mock
    ToolAutoBeanFactory factoryMock;

    @Captor
    ArgumentCaptor<AppsCallback<List<Tool>>> listToolsCaptor;

    @Captor ArgumentCaptor<AppsCallback<ToolList>> toolListCaptor;

    @Captor
    ArgumentCaptor<AppsCallback<List<App>>> appListCaptor;

    @Captor
    ArgumentCaptor<AppsCallback<Tool>> toolCaptor;

    @Captor
    ArgumentCaptor<AppsCallback<Splittable>> splittableCaptor;


    @Captor
    ArgumentCaptor<AppsCallback<Void>> voidCaptor;

    private ManageToolsViewPresenter uut;

    @Mock
    Iterator<Tool> iteratorMock;
    @Mock ManageToolsViewPresenter spy;

    @Before
    public void setUp() {
        uut = new ManageToolsViewPresenter(appearanceMock, manageToolsViewFactoryMock, editToolViewFactoryMock) {

            @Override
            void displayInfoMessage(String title, String message) {
                //do nothing
            }

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
        };
        uut.toolsView = toolsViewMock;
        uut.announcer = announcerMock;
        uut.appearance = appearanceMock;
        uut.toolServices = toolServicesMock;
        uut.editToolView = editToolViewMock;
        uut.shareDialogProvider = shareDialogProviderMock;
        uut.newToolRequestDialogProvider = newToolRequestDialogProviderMock;
        uut.toolInfoDialogProvider = toolInfoDialogProviderMock;
        uut.eventBus = eventBusMock;
        uut.currentSelection = currentSelectionMock;
        uut.factory = factoryMock;
        spy = Mockito.spy(uut);

        when(editToolViewFactoryMock.create(baseEditToolPropsMock)).thenReturn(editToolViewMock);
        when(toolMock.getType()).thenReturn("executable");
        when(toolMock.getContainer()).thenReturn(toolContainerMock);
        when(toolListSpl.get("tools")).thenReturn(toolListToolsSpl);
    }

    @Test
    public void testGo() {
        HasOneWidget containerMock = mock(HasOneWidget.class);

        SimpleContainer scMock = mock(SimpleContainer.class);
        when(toolsViewMock.asWidget()).thenReturn(scMock);

        uut.go(containerMock);
        verify(containerMock).setWidget(eq(scMock));
    }

    @Test
    public void testLoadTools() {
        Boolean isPublic = null;

        uut.loadTools(isPublic, "", "asc", "name", 100, 0);
        verify(toolsViewMock).setListingConfig(eq(isPublic), eq(""), eq("asc"), eq("name"), eq(100), eq(0));
        verify(toolServicesMock).searchTools(eq(isPublic),
                                             eq(""),
                                             eq("asc"),
                                             eq("name"),
                                             eq(100),
                                             eq(0),
                                             splittableCaptor.capture());
        splittableCaptor.getValue().onSuccess(toolListSpl);

        verify(toolsViewMock).loadTools(toolListSpl);
    }

    @Test
    public void testAddTool() {
        when(toolsViewMock.getCurrentToolList()).thenReturn(toolListSpl);
        when(toolListToolsSpl.size()).thenReturn(3);

        spy.addTool(toolSplMock);
        verify(toolServicesMock).addTool(eq(toolMock), splittableCaptor.capture());
        splittableCaptor.getValue().onSuccess(toolSplMock);
        verify(spy).refreshListing();
    }

    @Test
    public void testUpdateTool() {
        when(toolMock.getName()).thenReturn("Shiny Tool");
        when(appearanceMock.toolUpdated("Shiny Tool")).thenReturn("Tool Shiny Tool updated successfully!");

        spy.updateTool(toolSplMock);
        verify(toolServicesMock).updateTool(eq(toolMock), toolCaptor.capture());
        toolCaptor.getValue().onSuccess(toolMock);

        verify(spy).refreshListing();
    }

    @Test
    public void testDoDelete() {
        String toolId = "toolId";
        String toolName = "shiny";
        when(appearanceMock.toolDeleted(toolName)).thenReturn("Tool Shiny Tool deleted successfully!");

        spy.doDelete(toolId, toolName);
        verify(toolServicesMock).deleteTool(eq(toolId), voidCaptor.capture());
        voidCaptor.getValue().onSuccess(null);

        verify(spy).refreshListing();
    }

    @Test
    public void testOnNewToolSelected() {
        uut.onNewToolSelected();
        verify(editToolViewMock).edit(eq(null));
    }

    @Test
    public void testOnEditToolSelected() {
        String toolId = "toolId";

        uut.onEditToolSelected(toolId);

        verify(toolServicesMock).getToolInfo(eq(toolId), toolCaptor.capture());
        toolCaptor.getValue().onSuccess(toolMock);

        verify(editToolViewMock).edit(eq(toolSplMock));
    }

    @Test
    public void testOnShareToolSelected() {
        uut.onShareToolsSelected(toolSplMock);

        verify(shareDialogProviderMock).get(sharingDialogCaptor.capture());
        sharingDialogCaptor.getValue().onSuccess(toolSharingDialogMock);
        verify(toolSharingDialogMock).show(any());
    }

    @Test
    public void testOnRequestToolSelected() {
        uut.onRequestToolSelected();

        verify(newToolRequestDialogProviderMock).get(newToolDialogCaptor.capture());
        newToolDialogCaptor.getValue().onSuccess(toolRequestMock);
        verify(toolRequestMock).show(eq(NewToolRequestFormView.Mode.NEWTOOL));
    }

    @Test
    public void testOnRequestToMakePublicSelected() {
        uut.onRequestToMakeToolPublicSelected(toolSplMock);

        verify(newToolRequestDialogProviderMock).get(newToolDialogCaptor.capture());
        newToolDialogCaptor.getValue().onSuccess(toolRequestMock);
        verify(toolRequestMock).setTool(eq(toolMock));
        verify(toolRequestMock).show(eq(NewToolRequestFormView.Mode.MAKEPUBLIC));
    }

    @Test
    public void testOnShowToolInfo() {
        App a1 = mock(App.class);
        List<App> appList = Arrays.asList(a1);
        String toolId = "toolId";

        uut.onShowToolInfo(toolId);

        verify(toolServicesMock).getAppsForTool(eq(toolId), appListCaptor.capture());
        appListCaptor.getValue().onSuccess(appList);

        verify(toolServicesMock).getToolInfo(eq(toolId), toolCaptor.capture());
        toolCaptor.getValue().onSuccess(toolMock);

        verify(toolInfoDialogProviderMock).get(toolInfoDialogCaptor.capture());
        toolInfoDialogCaptor.getValue().onSuccess(toolInfoDialogMock);
        verify(toolInfoDialogMock).show(eq(toolMock), eq(appList));

        verify(toolInfoDialogMock).show(eq(t1Mock),
                                        eq(AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(appList))));

    }

}
