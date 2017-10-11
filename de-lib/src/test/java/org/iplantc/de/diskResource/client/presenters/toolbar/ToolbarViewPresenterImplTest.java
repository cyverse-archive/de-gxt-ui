package org.iplantc.de.diskResource.client.presenters.toolbar;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.diskResources.DiskResourceAutoBeanFactory;
import org.iplantc.de.client.models.diskResources.File;
import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.client.models.diskResources.HTPathListRequest;
import org.iplantc.de.client.models.genomes.GenomeAutoBeanFactory;
import org.iplantc.de.client.models.viewer.InfoType;
import org.iplantc.de.client.services.DiskResourceServiceFacade;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.diskResource.client.DiskResourceView;
import org.iplantc.de.diskResource.client.ToolbarView;
import org.iplantc.de.diskResource.client.events.RequestSimpleDownloadEvent;
import org.iplantc.de.diskResource.client.events.selection.SimpleDownloadSelected;
import org.iplantc.de.diskResource.client.gin.factory.BulkMetadataViewFactory;
import org.iplantc.de.diskResource.client.gin.factory.DiskResourceSelectorFieldFactory;
import org.iplantc.de.diskResource.client.gin.factory.HTPathListAutomationDialogFactory;
import org.iplantc.de.diskResource.client.gin.factory.ToolbarViewFactory;
import org.iplantc.de.diskResource.client.views.dialogs.CreateFolderDialog;
import org.iplantc.de.diskResource.client.views.dialogs.GenomeSearchDialog;
import org.iplantc.de.diskResource.client.views.dialogs.HTPathListAutomationDialog;
import org.iplantc.de.shared.DataCallback;

import com.google.gwtmockito.GwtMockitoTestRunner;
import com.google.web.bindery.autobean.shared.AutoBean;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author jstroot
 */
@RunWith(GwtMockitoTestRunner.class)
public class ToolbarViewPresenterImplTest {

    @Mock ToolbarViewFactory viewFactoryMock;
    @Mock DiskResourceView.Presenter parentPresenterMock;
    @Mock EventBus eventBusMock;
    @Mock ToolbarView viewMock;
    @Mock
    GenomeSearchDialog genomeSearchView;
    @Mock
    GenomeAutoBeanFactory gFactory;
    @Mock BulkMetadataViewFactory bulkMetadataViewFactor;
    @Mock
    DiskResourceServiceFacade drFacadeMock;
    @Mock UserInfo userInfoMock;
    @Mock CreateFolderDialog cfDialogMock;
    @Mock
    DiskResourceAutoBeanFactory drAbFactory;
    @Mock
    AutoBean<Folder> folderAb;
    @Mock
    HTPathListAutomationDialogFactory htPathListAutomationViewFactory;
    @Captor
    ArgumentCaptor<DataCallback<List<InfoType>>> infoTypeCaptor;
    @Mock
    HTPathListAutomationDialog.HTPathListAutomationAppearance appearanceMock;
    @Mock
    DiskResourceSelectorFieldFactory drSelectorFactoryMock;
    @Mock
    HTPathListAutomationDialog dialogMock;
    @Captor
    ArgumentCaptor<DataCallback<File>> fileCaptor;
    @Mock
    IplantAnnouncer announcerMock;

    private ToolbarViewPresenterImpl uut;

    @Before public void setUp() {
        when(viewFactoryMock.create(Matchers.<ToolbarView.Presenter>any())).thenReturn(viewMock);
        when(userInfoMock.getUsername()).thenReturn("username");
        uut = new ToolbarViewPresenterImpl(viewFactoryMock,
                                           genomeSearchView,
                                           bulkMetadataViewFactor,
                                           htPathListAutomationViewFactory,
                                           gFactory,
                                           parentPresenterMock){
            @Override
            protected CreateFolderDialog getCreateFolderDialog(Folder selectedFolder) {
                return cfDialogMock;
            }

            @Override
            protected void showHTProcessingError() {
                //do nothing
            }
        };
        uut.htAppearance = appearanceMock;
        uut.eventBus = eventBusMock;
        uut.drAbFactory = drAbFactory;
        uut.userInfo = userInfoMock;
        uut.drFacade = drFacadeMock;
        uut.drSelectorFactory = drSelectorFactoryMock;
        uut.dialog = dialogMock;
        uut.announcer = announcerMock;
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
        uut.onCreateNewFolderSelected(selectedFolderMock);

        verifyZeroInteractions(userInfoMock);
    }

    @Test public void onCreateNewFolderSelectedNoParent() {
        when(userInfoMock.getHomePath()).thenReturn("/iplant/home/ipctest");
        Folder parentMock = mock(Folder.class);
        when(drAbFactory.folder()).thenReturn(folderAb);
        when(folderAb.as()).thenReturn(parentMock);
        uut.onCreateNewFolderSelected(null);

        verify(userInfoMock).getHomePath();

        verifyNoMoreInteractions(userInfoMock);
    }

    @Test
    public void onAutomateHTPathList() {
        when(appearanceMock.dialogWidth()).thenReturn("800px");
        when(appearanceMock.dialogHeight()).thenReturn("700px");
        when(dialogMock.isValid()).thenReturn(true);
        HTPathListRequest requestMock = mock(HTPathListRequest.class);
        when(dialogMock.getRequest()).thenReturn(requestMock);
        InfoType type1 = mock(InfoType.class);
        List<InfoType> typeList = Arrays.asList(type1);

        when(htPathListAutomationViewFactory.create(drSelectorFactoryMock, typeList)).thenReturn(
                dialogMock);

        uut.onAutomateHTPathList();

        verify(drFacadeMock).getInfoTypes(infoTypeCaptor.capture());
        infoTypeCaptor.getValue().onSuccess(typeList);
        verify(dialogMock).show();
    }

    @Test
    public void requestHTPathListCreation() {
        when(appearanceMock.processing()).thenReturn("Processing Request...");
        when(appearanceMock.requestSuccess()).thenReturn("Your request completed successfully!");
        HTPathListRequest requestMock = mock(HTPathListRequest.class);
        File file = mock(File.class);

        uut.requestHTPathListCreation(dialogMock, requestMock);

        verify(drFacadeMock).requestHTPathlistFile(eq(requestMock), fileCaptor.capture());
        fileCaptor.getValue().onSuccess(file);
    }

}
