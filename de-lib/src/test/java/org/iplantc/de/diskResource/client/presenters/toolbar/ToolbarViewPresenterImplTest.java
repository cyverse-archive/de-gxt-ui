package org.iplantc.de.diskResource.client.presenters.toolbar;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.diskResources.DiskResourceAutoBeanFactory;
import org.iplantc.de.client.models.diskResources.File;
import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.client.models.diskResources.PathListRequest;
import org.iplantc.de.client.models.genomes.GenomeAutoBeanFactory;
import org.iplantc.de.client.models.identifiers.PermanentIdRequestType;
import org.iplantc.de.client.models.viewer.InfoType;
import org.iplantc.de.client.services.DiskResourceServiceFacade;
import org.iplantc.de.client.services.PermIdRequestUserServiceFacade;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;
import org.iplantc.de.commons.client.views.window.configs.TabularFileViewerWindowConfig;
import org.iplantc.de.diskResource.client.DiskResourceView;
import org.iplantc.de.diskResource.client.PathListAutomationView;
import org.iplantc.de.diskResource.client.ToolbarView;
import org.iplantc.de.diskResource.client.events.CreateNewFileEvent;
import org.iplantc.de.diskResource.client.events.RequestSimpleDownloadEvent;
import org.iplantc.de.diskResource.client.events.selection.AutomatePathListSelected;
import org.iplantc.de.diskResource.client.events.selection.CreateNcbiFolderStructureSelected;
import org.iplantc.de.diskResource.client.events.selection.CreateNcbiSraFolderStructureSubmitted;
import org.iplantc.de.diskResource.client.events.selection.CreateNewDelimitedFileSelected;
import org.iplantc.de.diskResource.client.events.selection.CreateNewFolderSelected;
import org.iplantc.de.diskResource.client.events.selection.CreatePublicLinkSelected;
import org.iplantc.de.diskResource.client.events.selection.SimpleDownloadSelected;
import org.iplantc.de.diskResource.client.gin.factory.BulkMetadataViewFactory;
import org.iplantc.de.diskResource.client.gin.factory.DiskResourceSelectorFieldFactory;
import org.iplantc.de.diskResource.client.views.dialogs.CreateFolderDialog;
import org.iplantc.de.diskResource.client.views.dialogs.CreateNcbiSraFolderStructureDialog;
import org.iplantc.de.diskResource.client.views.dialogs.CreatePublicLinkDialog;
import org.iplantc.de.diskResource.client.views.dialogs.GenomeSearchDialog;
import org.iplantc.de.diskResource.client.views.toolbar.dialogs.PathListAutomationDialog;
import org.iplantc.de.diskResource.client.views.toolbar.dialogs.TabFileConfigDialog;
import org.iplantc.de.shared.AsyncProviderWrapper;
import org.iplantc.de.shared.DataCallback;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwtmockito.GwtMockitoTestRunner;
import com.google.web.bindery.autobean.shared.AutoBean;

import com.sencha.gxt.widget.core.client.event.SelectEvent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author jstroot
 */
@RunWith(GwtMockitoTestRunner.class)
public class ToolbarViewPresenterImplTest {

    @Mock DiskResourceView.Presenter parentPresenterMock;
    @Mock EventBus eventBusMock;
    @Mock ToolbarView viewMock;
    @Mock GenomeSearchDialog genomeSearchDialog;
    @Mock GenomeAutoBeanFactory gFactory;
    @Mock BulkMetadataViewFactory bulkMetadataViewFactor;
    @Mock DiskResourceServiceFacade drFacadeMock;
    @Mock UserInfo userInfoMock;
    @Mock CreateFolderDialog cfDialogMock;
    @Mock DiskResourceAutoBeanFactory drAbFactory;
    @Mock AutoBean<Folder> folderAb;
    @Mock Folder folderMock;
    @Mock PathListAutomationView.PathListAutomationAppearance htPathAppearanceMock;
    @Mock ToolbarView.Presenter.Appearance appearanceMock;
    @Mock DiskResourceSelectorFieldFactory drSelectorFactoryMock;
    @Mock CreateNcbiSraFolderStructureDialog ncbiStructureDlgMock;
    @Mock PathListAutomationDialog htPathAutomationDlg;
    @Mock AsyncProviderWrapper<CreateFolderDialog> createFolderDlgProvider;
    @Mock AsyncProviderWrapper<PathListAutomationDialog> htPathAutomationDlgProvider;
    @Mock AsyncProviderWrapper<TabFileConfigDialog> tabFileConfigDlgProviderMock;
    @Mock AsyncProviderWrapper<CreateNcbiSraFolderStructureDialog> ncbiStructureDlgProviderMock;
    @Mock TabFileConfigDialog tabFileConfigDlgMock;
    @Mock SelectEvent selectEventMock;
    @Mock HandlerManager handlerManagerMock;
    @Mock TabularFileViewerWindowConfig tabFileWindowConfigMock;
    @Mock AsyncProviderWrapper<CreatePublicLinkDialog> createPublicLinkDlgProviderMock;
    @Mock CreatePublicLinkDialog createPublicLinkDlgMock;
    @Mock List<DiskResource> diskResourcesMock;
    @Mock PermIdRequestUserServiceFacade prFacadeMock;
    @Mock List<InfoType> infoTypesMock;
    @Mock IplantAnnouncer announcerMock;

    @Captor ArgumentCaptor<DataCallback<List<InfoType>>> infoTypeCaptor;
    @Captor ArgumentCaptor<DataCallback<String>> dataCallbackStringCaptor;
    @Captor ArgumentCaptor<DataCallback<File>> fileCaptor;
    @Captor ArgumentCaptor<AsyncCallback<CreateFolderDialog>> createFolderDlgCaptor;
    @Captor ArgumentCaptor<AsyncCallback<PathListAutomationDialog>> htPathAutomationDlgCaptor;
    @Captor ArgumentCaptor<AsyncCallback<TabFileConfigDialog>> tabFileConfigDlgCaptor;
    @Captor ArgumentCaptor<SelectEvent.SelectHandler> selectHandlerCaptor;
    @Captor ArgumentCaptor<AsyncCallback<CreateNcbiSraFolderStructureDialog>> ncbiFolderStructureDlgCaptor;
    @Captor ArgumentCaptor<AsyncCallback<CreatePublicLinkDialog>> createPublicLinkDlgCaptor;

    private ToolbarViewPresenterImpl uut;

    @Before public void setUp() {
        when(userInfoMock.getUsername()).thenReturn("username");
        when(userInfoMock.getHomePath()).thenReturn("path");
        uut = new ToolbarViewPresenterImpl(viewMock){

            @Override
            protected void showHTProcessingError(String heading) {
                //do nothing
            }

            @Override
            TabularFileViewerWindowConfig getTabFileViewerWindowConfig() {
                return tabFileWindowConfigMock;
            }

            @Override
            HandlerManager ensureHandlers() {
                return handlerManagerMock;
            }
        };
        uut.htAppearance = htPathAppearanceMock;
        uut.eventBus = eventBusMock;
        uut.drAbFactory = drAbFactory;
        uut.userInfo = userInfoMock;
        uut.drFacade = drFacadeMock;
        uut.drSelectorFactory = drSelectorFactoryMock;
        uut.announcer = announcerMock;
        uut.createFolderDlgProvider = createFolderDlgProvider;
        uut.pathAutomationDlgProvider = htPathAutomationDlgProvider;
        uut.tabFileConfigDlgProvider = tabFileConfigDlgProviderMock;
        uut.createNcbiSraDlgProvider = ncbiStructureDlgProviderMock;
        uut.createPublicLinkDlgProvider = createPublicLinkDlgProviderMock;
        uut.appearance = appearanceMock;
        uut.prFacade = prFacadeMock;
    }

    @Test public void onSimpleDownloadSelected_firesEvent() {
        SimpleDownloadSelected eventMock = mock(SimpleDownloadSelected.class);
        List<DiskResource> selectedResourcesMock = Collections.emptyList();
        Folder selectedFolderMock = mock(Folder.class);
        when(eventMock.getSelectedDiskResources()).thenReturn(selectedResourcesMock);
        when(eventMock.getSelectedFolder()).thenReturn(selectedFolderMock);


        /** CALL METHOD UNDER TEST **/
        uut.onSimpleDownloadSelected(eventMock);

        ArgumentCaptor<RequestSimpleDownloadEvent> captor = ArgumentCaptor.forClass(RequestSimpleDownloadEvent.class);
        verify(eventBusMock).fireEvent(captor.capture());

        assertEquals(selectedResourcesMock, captor.getValue().getRequestedResources());
        assertEquals(selectedFolderMock, captor.getValue().getCurrentFolder());

        verifyNoMoreInteractions(eventBusMock);
    }

    @Test public void onCreateNewFolderSelected() {
        when(userInfoMock.getHomePath()).thenReturn("/iplant/home/ipctest");
        Folder selectedFolderMock = mock(Folder.class);
        CreateNewFolderSelected eventMock = mock(CreateNewFolderSelected.class);
        when(eventMock.getSelectedFolder()).thenReturn(selectedFolderMock);
        uut.onCreateNewFolderSelected(eventMock);

        verify(createFolderDlgProvider).get(createFolderDlgCaptor.capture());
        createFolderDlgCaptor.getValue().onSuccess(cfDialogMock);
        verify(cfDialogMock).show(selectedFolderMock);
    }

    @Test public void onCreateNewFolderSelectedNoParent() {
        when(userInfoMock.getHomePath()).thenReturn("/iplant/home/ipctest");
        Folder parentMock = mock(Folder.class);
        when(drAbFactory.folder()).thenReturn(folderAb);
        when(folderAb.as()).thenReturn(parentMock);
        CreateNewFolderSelected eventMock = mock(CreateNewFolderSelected.class);
        uut.onCreateNewFolderSelected(eventMock);

        verify(userInfoMock).getHomePath();

        verify(createFolderDlgProvider).get(createFolderDlgCaptor.capture());
        createFolderDlgCaptor.getValue().onSuccess(cfDialogMock);
        verify(cfDialogMock).show(parentMock);
    }

    @Test
    public void onAutomateHTPathListSelected() {
        AutomatePathListSelected eventMock = mock(AutomatePathListSelected.class);
        when(eventMock.getPathListInfoType()).thenReturn(InfoType.HT_ANALYSIS_PATH_LIST);
        ToolbarViewPresenterImpl spy = spy(uut);
        InfoType type1 = mock(InfoType.class);
        List<InfoType> typeList = Arrays.asList(type1);

        spy.onAutomatePathListSelected(eventMock);

        verify(drFacadeMock).getInfoTypes(infoTypeCaptor.capture());
        infoTypeCaptor.getValue().onSuccess(typeList);
    }

    @Test
    public void showHtPathAutomationDialog() {
        when(htPathAutomationDlg.isValid()).thenReturn(true);
        PathListRequest requestMock = mock(PathListRequest.class);
        when(htPathAutomationDlg.getRequest()).thenReturn(requestMock);
        InfoType type1 = mock(InfoType.class);
        List<InfoType> typeList = Arrays.asList(type1);
        ToolbarViewPresenterImpl spy = spy(uut);

        spy.showPathAutomationDialog(typeList, InfoType.HT_ANALYSIS_PATH_LIST);
        verify(htPathAutomationDlgProvider).get(htPathAutomationDlgCaptor.capture());

        htPathAutomationDlgCaptor.getValue().onSuccess(htPathAutomationDlg);
        verify(htPathAutomationDlg).show(eq(typeList), eq(InfoType.HT_ANALYSIS_PATH_LIST));
        verify(htPathAutomationDlg).addOkButtonSelectHandler(selectHandlerCaptor.capture());
        selectHandlerCaptor.getValue().onSelect(selectEventMock);
        verify(spy).requestHTPathListCreation(eq(htPathAutomationDlg), eq(requestMock));
    }

    @Test
    public void requestHTPathListCreation() {
        when(htPathAppearanceMock.processing()).thenReturn("Processing Request...");
        when(htPathAppearanceMock.requestSuccess()).thenReturn("Your request completed successfully!");
        PathListRequest requestMock = mock(PathListRequest.class);
        File file = mock(File.class);

        uut.requestHTPathListCreation(htPathAutomationDlg, requestMock);

        verify(drFacadeMock).requestPathListFile(eq(requestMock), fileCaptor.capture());
        fileCaptor.getValue().onSuccess(file);
    }

    @Test
    public void testOnCreateNewDelimitedFileSelected() {
        when(tabFileConfigDlgMock.getSeparator()).thenReturn("separator");
        when(tabFileConfigDlgMock.getNumberOfColumns()).thenReturn(1);
        CreateNewDelimitedFileSelected eventMock = mock(CreateNewDelimitedFileSelected.class);

        /** CALL METHOD UNDER TEST **/
        uut.onCreateNewDelimitedFileSelected(eventMock);

        verify(tabFileConfigDlgProviderMock).get(tabFileConfigDlgCaptor.capture());
        tabFileConfigDlgCaptor.getValue().onSuccess(tabFileConfigDlgMock);
        verify(tabFileConfigDlgMock).addOkButtonSelectHandler(selectHandlerCaptor.capture());
        selectHandlerCaptor.getValue().onSelect(selectEventMock);
        verify(tabFileWindowConfigMock).setSeparator(eq("separator"));
        verify(tabFileWindowConfigMock).setColumns(eq(1));
        verify(eventBusMock).fireEvent(isA(CreateNewFileEvent.class));
    }

    @Test
    public void testOnCreateNcbiSraFolderStructure() {
        when(ncbiStructureDlgMock.isValid()).thenReturn(true);
        CreateNcbiFolderStructureSelected eventMock = mock(CreateNcbiFolderStructureSelected.class);
        when(eventMock.getSelectedFolder()).thenReturn(folderMock);

        /** CALL METHOD UNDER TEST **/
        uut.onCreateNcbiFolderStructureSelected(eventMock);

        verify(ncbiStructureDlgProviderMock).get(ncbiFolderStructureDlgCaptor.capture());
        ncbiFolderStructureDlgCaptor.getValue().onSuccess(ncbiStructureDlgMock);
        verify(ncbiStructureDlgMock).addOkButtonSelectHandler(selectHandlerCaptor.capture());
        selectHandlerCaptor.getValue().onSelect(selectEventMock);
        verify(handlerManagerMock).fireEvent(isA(CreateNcbiSraFolderStructureSubmitted.class));
        verify(ncbiStructureDlgMock).show(eq(folderMock));
    }

    @Test
    public void testOnCreatePublicLinkSelected() {
        CreatePublicLinkSelected eventMock = mock(CreatePublicLinkSelected.class);
        when(eventMock.getSelectedDiskResources()).thenReturn(diskResourcesMock);

        /** CALL METHOD UNDER TEST **/
        uut.onCreatePublicLinkSelected(eventMock);
        verify(createPublicLinkDlgProviderMock).get(createPublicLinkDlgCaptor.capture());
        createPublicLinkDlgCaptor.getValue().onSuccess(createPublicLinkDlgMock);
        verify(createPublicLinkDlgMock).show(eq(diskResourcesMock));
    }

    @Test
    public void requestDoi() {
        when(appearanceMock.doiRequestSuccess()).thenReturn("success");

        /** CALL METHOD UNDER TEST **/
        uut.requestDoi("uuid");
        verify(prFacadeMock).requestPermId(eq("uuid"),
                                           eq(PermanentIdRequestType.DOI),
                                           dataCallbackStringCaptor.capture());
        dataCallbackStringCaptor.getValue().onSuccess("result");
        verify(announcerMock).schedule(isA(SuccessAnnouncementConfig.class));
    }

}
