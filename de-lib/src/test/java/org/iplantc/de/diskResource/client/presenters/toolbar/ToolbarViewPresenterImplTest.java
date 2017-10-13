package org.iplantc.de.diskResource.client.presenters.toolbar;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
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
import org.iplantc.de.client.models.diskResources.HTPathListRequest;
import org.iplantc.de.client.models.genomes.GenomeAutoBeanFactory;
import org.iplantc.de.client.models.viewer.InfoType;
import org.iplantc.de.client.services.DiskResourceServiceFacade;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.diskResource.client.DiskResourceView;
import org.iplantc.de.diskResource.client.HTPathListAutomationView;
import org.iplantc.de.diskResource.client.ToolbarView;
import org.iplantc.de.diskResource.client.events.RequestSimpleDownloadEvent;
import org.iplantc.de.diskResource.client.events.selection.AutomateHTPathListSelected;
import org.iplantc.de.diskResource.client.events.selection.SimpleDownloadSelected;
import org.iplantc.de.diskResource.client.gin.factory.BulkMetadataViewFactory;
import org.iplantc.de.diskResource.client.gin.factory.DiskResourceSelectorFieldFactory;
import org.iplantc.de.diskResource.client.gin.factory.ToolbarViewFactory;
import org.iplantc.de.diskResource.client.views.dialogs.CreateFolderDialog;
import org.iplantc.de.diskResource.client.views.dialogs.GenomeSearchDialog;
import org.iplantc.de.diskResource.client.views.toolbar.dialogs.HTPathListAutomationDialog;
import org.iplantc.de.shared.AsyncProviderWrapper;
import org.iplantc.de.shared.DataCallback;

import com.google.gwt.user.client.rpc.AsyncCallback;
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
    @Mock GenomeSearchDialog genomeSearchDialog;
    @Mock GenomeAutoBeanFactory gFactory;
    @Mock BulkMetadataViewFactory bulkMetadataViewFactor;
    @Mock DiskResourceServiceFacade drFacadeMock;
    @Mock UserInfo userInfoMock;
    @Mock CreateFolderDialog cfDialogMock;
    @Mock DiskResourceAutoBeanFactory drAbFactory;
    @Mock AutoBean<Folder> folderAb;
    @Captor ArgumentCaptor<DataCallback<List<InfoType>>> infoTypeCaptor;
    @Mock HTPathListAutomationView.HTPathListAutomationAppearance appearanceMock;
    @Mock DiskResourceSelectorFieldFactory drSelectorFactoryMock;
    @Mock HTPathListAutomationDialog htPathAutomationDlg;
    @Mock AsyncProviderWrapper<CreateFolderDialog> createFolderDlgProvider;
    @Mock AsyncProviderWrapper<HTPathListAutomationDialog> htPathAutomationDlgProvider;
    @Captor ArgumentCaptor<DataCallback<File>> fileCaptor;
    @Captor ArgumentCaptor<AsyncCallback<CreateFolderDialog>> createFolderDlgCaptor;
    @Captor ArgumentCaptor<AsyncCallback<HTPathListAutomationDialog>> htPathAutomationDlgCaptor;
    @Mock IplantAnnouncer announcerMock;

    private ToolbarViewPresenterImpl uut;

    @Before public void setUp() {
        when(viewFactoryMock.create(Matchers.<ToolbarView.Presenter>any())).thenReturn(viewMock);
        when(userInfoMock.getUsername()).thenReturn("username");
        uut = new ToolbarViewPresenterImpl(viewFactoryMock){

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
        uut.announcer = announcerMock;
        uut.createFolderDlgProvider = createFolderDlgProvider;
        uut.htPathAutomationDlgProvider = htPathAutomationDlgProvider;
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

        verify(createFolderDlgProvider).get(createFolderDlgCaptor.capture());
        createFolderDlgCaptor.getValue().onSuccess(cfDialogMock);
        verify(cfDialogMock).show(selectedFolderMock);
    }

    @Test public void onCreateNewFolderSelectedNoParent() {
        when(userInfoMock.getHomePath()).thenReturn("/iplant/home/ipctest");
        Folder parentMock = mock(Folder.class);
        when(drAbFactory.folder()).thenReturn(folderAb);
        when(folderAb.as()).thenReturn(parentMock);
        uut.onCreateNewFolderSelected(null);

        verify(userInfoMock).getHomePath();

        verify(createFolderDlgProvider).get(createFolderDlgCaptor.capture());
        createFolderDlgCaptor.getValue().onSuccess(cfDialogMock);
        verify(cfDialogMock).show(parentMock);
    }

    @Test
    public void onAutomateHTPathListSelected() {
        AutomateHTPathListSelected eventMock = mock(AutomateHTPathListSelected.class);
        ToolbarViewPresenterImpl spy = spy(uut);
        InfoType type1 = mock(InfoType.class);
        List<InfoType> typeList = Arrays.asList(type1);

        spy.onAutomateHTPathListSelected(eventMock);

        verify(drFacadeMock).getInfoTypes(infoTypeCaptor.capture());
        infoTypeCaptor.getValue().onSuccess(typeList);
        verify(spy).showHtPathAutomationDialog(eq(typeList));
    }

    @Test
    public void showHtPathAutomationDialog() {
        when(htPathAutomationDlg.isValid()).thenReturn(true);
        HTPathListRequest requestMock = mock(HTPathListRequest.class);
        when(htPathAutomationDlg.getRequest()).thenReturn(requestMock);
        InfoType type1 = mock(InfoType.class);
        List<InfoType> typeList = Arrays.asList(type1);

        uut.showHtPathAutomationDialog(typeList);
        verify(htPathAutomationDlgProvider).get(htPathAutomationDlgCaptor.capture());

        htPathAutomationDlgCaptor.getValue().onSuccess(htPathAutomationDlg);
        verify(htPathAutomationDlg).show(eq(typeList));
    }

    @Test
    public void requestHTPathListCreation() {
        when(appearanceMock.processing()).thenReturn("Processing Request...");
        when(appearanceMock.requestSuccess()).thenReturn("Your request completed successfully!");
        HTPathListRequest requestMock = mock(HTPathListRequest.class);
        File file = mock(File.class);

        uut.requestHTPathListCreation(htPathAutomationDlg, requestMock);

        verify(drFacadeMock).requestHTPathlistFile(eq(requestMock), fileCaptor.capture());
        fileCaptor.getValue().onSuccess(file);
    }

}
