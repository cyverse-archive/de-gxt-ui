package org.iplantc.de.diskResource.client.presenters;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.HasPaths;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.dataLink.DataLinkFactory;
import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.diskResources.DiskResourceAutoBeanFactory;
import org.iplantc.de.client.models.diskResources.File;
import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.client.models.diskResources.TYPE;
import org.iplantc.de.client.models.viewer.InfoType;
import org.iplantc.de.client.services.DiskResourceServiceFacade;
import org.iplantc.de.client.services.FileSystemMetadataServiceFacade;
import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.commons.client.CommonUiConstants;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.diskResource.client.DetailsView;
import org.iplantc.de.diskResource.client.DiskResourceView;
import org.iplantc.de.diskResource.client.GridView;
import org.iplantc.de.diskResource.client.NavigationView;
import org.iplantc.de.diskResource.client.SearchView;
import org.iplantc.de.diskResource.client.ToolbarView;
import org.iplantc.de.diskResource.client.events.RequestSendToCoGeEvent;
import org.iplantc.de.diskResource.client.events.RequestSendToTreeViewerEvent;
import org.iplantc.de.diskResource.client.events.search.UpdateSavedSearchesEvent;
import org.iplantc.de.diskResource.client.events.selection.CreateNcbiSraFolderStructureSubmitted;
import org.iplantc.de.diskResource.client.events.selection.CreateNewFolderConfirmed;
import org.iplantc.de.diskResource.client.events.selection.DeleteDiskResourcesSelected;
import org.iplantc.de.diskResource.client.events.selection.EmptyTrashSelected;
import org.iplantc.de.diskResource.client.events.selection.MoveDiskResourcesSelected;
import org.iplantc.de.diskResource.client.events.selection.RenameDiskResourceSelected;
import org.iplantc.de.diskResource.client.events.selection.RestoreDiskResourcesSelected;
import org.iplantc.de.diskResource.client.events.selection.SendToCogeSelected;
import org.iplantc.de.diskResource.client.events.selection.SendToTreeViewerSelected;
import org.iplantc.de.diskResource.client.gin.factory.DiskResourceViewFactory;
import org.iplantc.de.diskResource.client.gin.factory.FolderContentsRpcProxyFactory;
import org.iplantc.de.diskResource.client.gin.factory.GridViewPresenterFactory;
import org.iplantc.de.diskResource.client.gin.factory.ToolbarViewPresenterFactory;
import org.iplantc.de.diskResource.client.presenters.callbacks.CreateFolderCallback;
import org.iplantc.de.diskResource.client.presenters.callbacks.DiskResourceRestoreCallback;
import org.iplantc.de.diskResource.client.presenters.callbacks.NcbiSraSetupCompleteCallback;
import org.iplantc.de.diskResource.client.presenters.callbacks.RenameDiskResourceCallback;
import org.iplantc.de.diskResource.client.views.dialogs.FolderSelectDialog;
import org.iplantc.de.diskResource.client.views.dialogs.RenameResourceDialog;
import org.iplantc.de.diskResource.client.views.search.DiskResourceSearchField;
import org.iplantc.de.resources.client.messages.IplantContextualHelpStrings;
import org.iplantc.de.shared.AsyncProviderWrapper;
import org.iplantc.de.shared.DataCallback;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwtmockito.GwtMockitoTestRunner;
import com.google.web.bindery.autobean.shared.AutoBean;

import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author jstroot
 */
@RunWith(GwtMockitoTestRunner.class)
public class DiskResourcePresenterImplTest {

    @Mock DiskResourceView viewMock;
    @Mock DiskResourceViewFactory viewFactoryMock;
    @Mock DiskResourceView.FolderRpcProxy folderRpcMock;
    @Mock FolderContentsRpcProxyFactory folderContentsRpcFactoryMock;
    @Mock DiskResourceView.FolderRpcProxy folderRpcProxyMock;
    @Mock DiskResourceServiceFacade diskResourceServiceMock;
    @Mock IplantContextualHelpStrings helpStringsMock;
    @Mock DiskResourceAutoBeanFactory factoryMock;
    @Mock DataLinkFactory mockDlFactory;
    @Mock SearchView.Presenter dataSearchPresenterMock;
    @Mock EventBus eventBusMock;
    @Mock UserInfo userInfoMock;
    @Mock FileSystemMetadataServiceFacade ileSystemMetadataServiceMock;
    @Mock UpdateSavedSearchesEvent eventMock;
    @Mock IplantAnnouncer announcerMock;
    @Mock ToolbarView toolbarMock;
    @Mock DiskResourceSearchField searchFieldMock;
    @Mock TreeStore<Folder> treeStoreMock;
    @Mock NavigationView.Presenter navigationPresenterMock;
    @Mock NavigationView navigationViewMock;
    @Mock GridViewPresenterFactory GridViewPresenterFactoryMock;
    @Mock GridView.Presenter gridViewPresenterMock;
    @Mock GridView gridViewMock;
    @Mock ToolbarViewPresenterFactory toolbarPresenterFactoryMock;
    @Mock ToolbarView.Presenter toolbarPresenterMock;
    @Mock DetailsView.Presenter detailsPresenterMock;
    @Mock TYPE entityTypeMock;
    @Mock List<InfoType> infoTypeFiltersMock;
    @Mock DetailsView detailsViewMock;
    @Mock DiskResourceUtil diskResourceUtilMock;
    @Mock List<DiskResource> diskResourcesMock;
    @Mock DiskResourceView.Presenter.Appearance appearanceMock;
    @Mock ConfirmMessageBox messageBoxMock;
    @Mock CommonUiConstants commonUiConstantsMock;
    @Mock DialogHideEvent hideEventMock;
    @Mock FolderSelectDialog folderSelectDlgMock;
    @Mock SelectEvent selectEventMock;
    @Mock Iterator<DiskResource> diskResourceIteratorMock;
    @Mock DiskResource diskResourceMock;
    @Mock Folder folderMock;
    @Mock List<Folder> foldersMock;
    @Mock RenameResourceDialog renameResourceDlgMock;
    @Mock AutoBean<HasPaths> hasPathsAutoBeanMock;
    @Mock HasPaths hasPathsMock;
    @Mock List<String> stringPathListMock;

    @Mock AsyncProviderWrapper<FolderSelectDialog> folderSelectDlgProviderMock;
    @Mock AsyncProviderWrapper<RenameResourceDialog> renameResourceDlgProviderMock;

    @Captor ArgumentCaptor<AsyncCallback<FolderSelectDialog>> folderSelectDlgCaptor;
    @Captor ArgumentCaptor<AsyncCallback<RenameResourceDialog>> renameResourceDlgCaptor;
    @Captor ArgumentCaptor<DialogHideEvent.DialogHideHandler> dialogHideHandlerCaptor;
    @Captor ArgumentCaptor<SelectEvent.SelectHandler> selectHandlerCaptor;
    @Captor ArgumentCaptor<DataCallback<List<Folder>>> folderListCaptor;

    private DiskResourcePresenterImpl uut;

    // TODO: SS complete tests with new service
    @Before public void setUp() {
        setupMocks();
        uut = new DiskResourcePresenterImpl(viewFactoryMock,
                                            factoryMock,
                                            navigationPresenterMock,
                                            GridViewPresenterFactoryMock,
                                            dataSearchPresenterMock,
                                            toolbarPresenterFactoryMock,
                                            detailsPresenterMock,
                                            announcerMock,
                                            eventBusMock,
                                            infoTypeFiltersMock,
                                            entityTypeMock) {
            @Override
            ConfirmMessageBox getEmptyTrashMessageBox(String title) {
                return messageBoxMock;
            }

            @Override
            public List<DiskResource> getSelectedDiskResources() {
                return diskResourcesMock;
            }
        };
        uut.diskResourceUtil = diskResourceUtilMock;
        uut.appearance = appearanceMock;
        uut.diskResourceService = diskResourceServiceMock;
        uut.userInfo = userInfoMock;
        uut.commonUiConstants = commonUiConstantsMock;
        uut.folderSelectDialogProvider = folderSelectDlgProviderMock;
        uut.renameResourceDlgProvider = renameResourceDlgProviderMock;
    }

    @Test public void verifyConstructorEventWiring() {
        verify(GridViewPresenterFactoryMock).create(eq(navigationPresenterMock),
                                                    eq(infoTypeFiltersMock),
                                                    eq(entityTypeMock));

        verify(navigationPresenterMock).setMaskable(eq(viewMock));

        // Details
        verify(detailsViewMock).addManageSharingSelectedEventHandler(eq(gridViewPresenterMock));
        verify(detailsViewMock).addEditInfoTypeSelectedEventHandler(eq(gridViewPresenterMock));
        verify(detailsViewMock).addResetInfoTypeSelectedHandler(eq(gridViewPresenterMock));
        verify(detailsViewMock).addMd5ValueClickedHandler(eq(gridViewPresenterMock));
        verify(detailsViewMock).addSubmitDiskResourceQueryEventHandler(eq(gridViewMock));
        verify(detailsViewMock).addSubmitDiskResourceQueryEventHandler(eq(gridViewPresenterMock));
        verify(detailsViewMock).addSendToCogeSelectedHandler(eq(uut));
        verify(detailsViewMock).addSendToEnsemblSelectedHandler(eq(uut));
        verify(detailsViewMock).addSendToTreeViewerSelectedHandler(eq(uut));

        // Toolbar
        verify(toolbarMock).getSearchField();
        verify(searchFieldMock).addSaveDiskResourceQueryClickedEventHandler(eq(dataSearchPresenterMock));
        verify(searchFieldMock).addSubmitDiskResourceQueryEventHandler(eq(gridViewMock));
        verify(searchFieldMock).addSubmitDiskResourceQueryEventHandler(eq(gridViewPresenterMock));
        verify(toolbarMock).addDeleteSelectedDiskResourcesSelectedEventHandler(eq(uut));
        verify(toolbarMock).addDeleteSelectedDiskResourcesSelectedEventHandler(eq(uut));
        verify(toolbarMock).addEditInfoTypeSelectedEventHandler(eq(gridViewPresenterMock));
        verify(toolbarMock).addEmptyTrashSelectedHandler(eq(uut));
        verify(toolbarMock).addManageSharingSelectedEventHandler(eq(gridViewPresenterMock));
        verify(toolbarMock).addManageMetadataSelectedEventHandler(eq(gridViewPresenterMock));
        verify(toolbarMock).addCopyMetadataSelectedEventHandler(eq(gridViewPresenterMock));
        verify(toolbarMock).addManageCommentsSelectedEventHandler(eq(gridViewPresenterMock));
        verify(toolbarMock).addMoveDiskResourcesSelectedHandler(eq(uut));
        verify(toolbarMock).addRefreshFolderSelectedHandler(eq(uut));
        verify(toolbarMock).addRenameDiskResourceSelectedHandler(eq(uut));
        verify(toolbarMock).addRestoreDiskResourcesSelectedHandler(eq(uut));
        verify(toolbarMock).addShareByDataLinkSelectedEventHandler(eq(gridViewPresenterMock));
        verify(toolbarMock).addSendToCogeSelectedHandler(eq(uut));
        verify(toolbarMock).addSendToEnsemblSelectedHandler(eq(uut));
        verify(toolbarMock).addSendToTreeViewerSelectedHandler(eq(uut));
        verify(toolbarMock).addSimpleUploadSelectedHandler(eq(navigationPresenterMock));
        verify(toolbarMock).addImportFromUrlSelectedHandler(eq(navigationPresenterMock));
        verify(toolbarPresenterMock).addCreateNcbiSraFolderStructureSubmittedHandler(eq(uut));
        verify(toolbarPresenterMock).addCreateNewFolderConfirmedHandler(eq(uut));

        // Grid
        verify(gridViewMock).addBeforeLoadHandler(eq(navigationPresenterMock));
        verify(gridViewMock).addDiskResourceNameSelectedEventHandler(eq(navigationPresenterMock));
        verify(gridViewMock).addDiskResourcePathSelectedEventHandler(eq(navigationPresenterMock));
        verify(gridViewMock).addDiskResourceSelectionChangedEventHandler(eq(detailsViewMock));
        verify(gridViewMock).addDiskResourceSelectionChangedEventHandler(eq(toolbarMock));
        verify(gridViewPresenterMock).addStoreUpdateHandler(eq(detailsViewMock));
        verify(gridViewPresenterMock).addFetchDetailsCompletedHandler(detailsViewMock);

        // Navigation
        verify(navigationPresenterMock).addSavedSearchedRetrievedEventHandler(eq(dataSearchPresenterMock));
        verify(navigationPresenterMock).addSubmitDiskResourceQueryEventHandler(eq(gridViewMock));
        verify(navigationPresenterMock).addSubmitDiskResourceQueryEventHandler(eq(gridViewPresenterMock));
        verify(navigationPresenterMock).addRootFoldersRetrievedEventHandler(eq(uut));
        verify(navigationViewMock).addFolderSelectedEventHandler(eq(gridViewPresenterMock));
        verify(navigationViewMock).addFolderSelectedEventHandler(eq(gridViewMock));
        verify(navigationViewMock).addFolderSelectedEventHandler(eq(toolbarMock));
        verify(navigationViewMock).addFolderSelectedEventHandler(eq(searchFieldMock));
        verify(navigationViewMock).addDeleteSavedSearchClickedEventHandler(eq(dataSearchPresenterMock));
        verify(navigationPresenterMock).addDNDDiskResourcesCompletedHandler(eq(uut));
        verify(navigationPresenterMock).addRefreshFolderSelectedHandler(eq(uut));

        // Search
        verify(dataSearchPresenterMock).addUpdateSavedSearchesEventHandler(eq(navigationPresenterMock));
        verify(dataSearchPresenterMock).addSavedSearchDeletedEventHandler(eq(searchFieldMock));

        verify(detailsPresenterMock, times(12)).getView();
        verify(gridViewPresenterMock, times(9)).getView();
        verify(navigationPresenterMock, times(5)).getView();
        verify(toolbarPresenterMock, times(23)).getView();

        verifyNoMoreInteractions(navigationPresenterMock,
                                 detailsPresenterMock,
                                 navigationViewMock,
                                 gridViewMock,
                                 detailsViewMock);
        verifyZeroInteractions(diskResourceServiceMock,
                               eventBusMock);

    }

    private void setupMocks() {
        when(viewFactoryMock.create(any(NavigationView.Presenter.class),
                                    any(GridView.Presenter.class),
                                    any(ToolbarView.Presenter.class),
                                    any(DetailsView.Presenter.class))).thenReturn(viewMock);
        when(GridViewPresenterFactoryMock.create(any(NavigationView.Presenter.class),
                                                 anyList(),
                                                 any(TYPE.class))).thenReturn(gridViewPresenterMock);
        when(toolbarMock.getSearchField()).thenReturn(searchFieldMock);
        when(navigationPresenterMock.getView()).thenReturn(navigationViewMock);
        when(gridViewPresenterMock.getView()).thenReturn(gridViewMock);
        when(detailsPresenterMock.getView()).thenReturn(detailsViewMock);
        when(toolbarPresenterFactoryMock.create(any(DiskResourceView.Presenter.class))).thenReturn(toolbarPresenterMock);
        when(toolbarPresenterMock.getView()).thenReturn(toolbarMock);
        when(diskResourcesMock.iterator()).thenReturn(diskResourceIteratorMock);
        when(diskResourceIteratorMock.hasNext()).thenReturn(true, false);
        when(diskResourceIteratorMock.next()).thenReturn(diskResourceMock);
        when(appearanceMock.loadingMask()).thenReturn("loading");
        when(factoryMock.pathsList()).thenReturn(hasPathsAutoBeanMock);
        when(hasPathsAutoBeanMock.as()).thenReturn(hasPathsMock);
    }

    @Test
    public void onDeleteSelectedDiskResourcesSelected_emptyTrash() {
        DiskResourcePresenterImpl spy = spy(uut);
        DeleteDiskResourcesSelected eventMock = mock(DeleteDiskResourcesSelected.class);
        when(eventMock.getSelectedDiskResources()).thenReturn(diskResourcesMock);
        when(eventMock.isConfirmDelete()).thenReturn(true);
        when(appearanceMock.warning()).thenReturn("warning");
        when(diskResourcesMock.isEmpty()).thenReturn(false);
        when(diskResourceUtilMock.isOwner(diskResourcesMock)).thenReturn(true);
        when(diskResourceUtilMock.containsTrashedResource(diskResourcesMock)).thenReturn(true);

        /** CALL METHOD UNDER TEST **/
        spy.onDeleteSelectedDiskResourcesSelected(eventMock);
        verify(spy).confirmDelete(eq(diskResourcesMock));
    }

    @Test
    public void onDeleteSelectedDiskResourcesSelected_delete() {
        DiskResourcePresenterImpl spy = spy(uut);
        DeleteDiskResourcesSelected eventMock = mock(DeleteDiskResourcesSelected.class);
        when(eventMock.getSelectedDiskResources()).thenReturn(diskResourcesMock);
        when(eventMock.isConfirmDelete()).thenReturn(true);
        when(diskResourcesMock.isEmpty()).thenReturn(false);
        when(diskResourceUtilMock.isOwner(diskResourcesMock)).thenReturn(true);
        when(diskResourceUtilMock.containsTrashedResource(diskResourcesMock)).thenReturn(false);
        when(appearanceMock.deleteMsg()).thenReturn("delete");

        /** CALL METHOD UNDER TEST **/
        spy.onDeleteSelectedDiskResourcesSelected(eventMock);
        verify(spy).delete(eq(diskResourcesMock),
                           eq("delete"));
    }

    @Test
    public void onEmptyTrashSelected() {
        DiskResourcePresenterImpl spy = spy(uut);
        EmptyTrashSelected eventMock = mock(EmptyTrashSelected.class);
        when(hideEventMock.getHideButton()).thenReturn(Dialog.PredefinedButton.YES);

        /** CALL METHOD UNDER TEST **/
        spy.onEmptyTrashSelected(eventMock);
        verify(messageBoxMock).addDialogHideHandler(dialogHideHandlerCaptor.capture());

        dialogHideHandlerCaptor.getValue().onDialogHide(hideEventMock);
        verify(spy).doEmptyTrash();
        verify(messageBoxMock).show();
    }

    @Test
    public void onMoveDiskResourcesSelected_movable() {
        uut = new DiskResourcePresenterImpl(viewFactoryMock,
                                            factoryMock,
                                            navigationPresenterMock,
                                            GridViewPresenterFactoryMock,
                                            dataSearchPresenterMock,
                                            toolbarPresenterFactoryMock,
                                            detailsPresenterMock,
                                            announcerMock,
                                            eventBusMock,
                                            infoTypeFiltersMock,
                                            entityTypeMock){
            @Override
            boolean canDragDataToTargetFolder(Folder targetFolder, Collection<DiskResource> dropData) {
                return true;
            }

            @Override
            public List<DiskResource> getSelectedDiskResources() {
                return diskResourcesMock;
            }
        };
        uut.folderSelectDialogProvider = folderSelectDlgProviderMock;
        uut.diskResourceUtil = diskResourceUtilMock;
        uut.appearance = appearanceMock;
        uut.diskResourceService = diskResourceServiceMock;

        DiskResourcePresenterImpl spy = spy(uut);
        MoveDiskResourcesSelected eventMock = mock(MoveDiskResourcesSelected.class);
        Folder targetFolder = mock(Folder.class);
        Folder selectedFolder = mock(Folder.class);
        when(folderSelectDlgMock.getValue()).thenReturn(targetFolder);
        when(diskResourceUtilMock.isMovable(any(), any())).thenReturn(true);
        when(navigationPresenterMock.getSelectedFolder()).thenReturn(selectedFolder);
        when(appearanceMock.permissionErrorMessage()).thenReturn("error");

        /** CALL METHOD UNDER TEST **/
        spy.onMoveDiskResourcesSelected(eventMock);
        verify(folderSelectDlgProviderMock).get(folderSelectDlgCaptor.capture());
        folderSelectDlgCaptor.getValue().onSuccess(folderSelectDlgMock);
        verify(folderSelectDlgMock).addOkButtonSelectHandler(selectHandlerCaptor.capture());
        selectHandlerCaptor.getValue().onSelect(selectEventMock);
        verify(spy).doMoveDiskResources(eq(targetFolder),
                                        eq(diskResourcesMock));
        verify(folderSelectDlgMock).show(eq(selectedFolder),
                                         anyList());
    }

    @Test
    public void refreshFolder() {

        /** CALL METHOD UNDER TEST **/
        uut.refreshFolder(folderMock);

        verify(diskResourceServiceMock).refreshFolder(eq(folderMock),
                                                      folderListCaptor.capture());

        folderListCaptor.getValue().onSuccess(foldersMock);
        verify(viewMock).unmask();
    }

    @Test
    public void onRenameDiskResourceSelected() {
        RenameDiskResourceSelected eventMock = mock(RenameDiskResourceSelected.class);
        when(gridViewPresenterMock.getSelectedDiskResources()).thenReturn(diskResourcesMock);
        when(diskResourcesMock.isEmpty()).thenReturn(false);
        when(diskResourcesMock.size()).thenReturn(1);

        /** CALL METHOD UNDER TEST **/
        uut.onRenameDiskResourceSelected(eventMock);
        verify(renameResourceDlgProviderMock).get(renameResourceDlgCaptor.capture());
        renameResourceDlgCaptor.getValue().onSuccess(renameResourceDlgMock);
        verify(renameResourceDlgMock).show(eq(diskResourceMock));
    }

    @Test
    public void onRestoreDiskResourcesSelected_selectAll() {
        RestoreDiskResourcesSelected eventMock = mock(RestoreDiskResourcesSelected.class);
        when(diskResourcesMock.isEmpty()).thenReturn(false);
        when(navigationPresenterMock.getSelectedFolder()).thenReturn(folderMock);
        when(gridViewPresenterMock.isSelectAllChecked()).thenReturn(true);

        /** CALL METHOD UNDER TEST **/
        uut.onRestoreDiskResourcesSelected(eventMock);
        verify(diskResourceServiceMock).restoreAll(isA(DiskResourceRestoreCallback.class));
    }

    @Test
    public void onRestoreDiskResourcesSelected_selectAllFalse() {
        RestoreDiskResourcesSelected eventMock = mock(RestoreDiskResourcesSelected.class);
        when(diskResourcesMock.isEmpty()).thenReturn(false);
        when(navigationPresenterMock.getSelectedFolder()).thenReturn(folderMock);
        when(gridViewPresenterMock.isSelectAllChecked()).thenReturn(false);
        when(diskResourceUtilMock.asStringPathList(any())).thenReturn(stringPathListMock);

        /** CALL METHOD UNDER TEST **/
        uut.onRestoreDiskResourcesSelected(eventMock);
        verify(hasPathsMock).setPaths(eq(stringPathListMock));
        verify(diskResourceServiceMock).restoreDiskResource(eq(hasPathsMock),
                                                            isA(DiskResourceRestoreCallback.class));
    }

    @Test
    public void onSendToCogeSelected() {
        SendToCogeSelected eventMock = mock(SendToCogeSelected.class);
        File fileMock = mock(File.class);
        when(diskResourceIteratorMock.next()).thenReturn(fileMock);
        when(eventMock.getResourcesToSend()).thenReturn(diskResourcesMock);
        when(fileMock.getInfoType()).thenReturn(InfoType.FASTA.toString());
        when(appearanceMock.unsupportedCogeInfoType()).thenReturn("fail");
        when(diskResourceUtilMock.isGenomeVizInfoType(any())).thenReturn(true);

        uut.onSendToCogeSelected(eventMock);
        verify(eventBusMock).fireEvent(isA(RequestSendToCoGeEvent.class));
    }

    @Test
    public void onSendToTreeViewerSelected() {
        SendToTreeViewerSelected eventMock = mock(SendToTreeViewerSelected.class);
        File fileMock = mock(File.class);
        when(diskResourceIteratorMock.next()).thenReturn(fileMock);
        when(eventMock.getResourcesToSend()).thenReturn(diskResourcesMock);
        when(fileMock.getInfoType()).thenReturn(InfoType.FASTA.toString());
        when(diskResourceUtilMock.isTreeInfoType(any())).thenReturn(true);
        when(appearanceMock.unsupportedTreeInfoType()).thenReturn("fail");

        /** CALL METHOD UNDER TEST **/
        uut.onSendToTreeViewerSelected(eventMock);
        verify(eventBusMock).fireEvent(isA(RequestSendToTreeViewerEvent.class));
    }

    @Test
    public void onCreateNewFolderConfirmed() {
        CreateNewFolderConfirmed eventMock = mock(CreateNewFolderConfirmed.class);
        when(eventMock.getParentFolder()).thenReturn(folderMock);
        when(eventMock.getFolderName()).thenReturn("name");
        when(appearanceMock.createFolderLoadingMask()).thenReturn("loading");

        /** CALL METHOD UNDER TEST **/
        uut.onCreateNewFolderConfirmed(eventMock);
        verify(diskResourceServiceMock).createFolder(eq(folderMock),
                                                     eq("name"),
                                                     isA(CreateFolderCallback.class));
    }

    @Test
    public void onCreateNcbiSraFolderStructureSubmitted() {
        CreateNcbiSraFolderStructureSubmitted eventMock = mock(CreateNcbiSraFolderStructureSubmitted.class);
        when(eventMock.getSelectedFolder()).thenReturn(folderMock);
        when(eventMock.getProjectText()).thenReturn("project");
        when(eventMock.getBioSampleNumber()).thenReturn(1);
        when(eventMock.getLibraryNumber()).thenReturn(20);
        when(appearanceMock.createFolderLoadingMask()).thenReturn("loading");

        /** CALL METHOD UNDER TEST **/
        uut.onCreateNcbiSraFolderStructureSubmitted(eventMock);
        verify(diskResourceServiceMock).createNcbiSraFolderStructure(eq(folderMock),
                                                                     any(),
                                                                     isA(NcbiSraSetupCompleteCallback.class));
    }

    @Test
    public void doRenameDiskResource() {
        when(appearanceMock.renameDiskResourcesLoadingMask()).thenReturn("loading");
        when(diskResourceMock.getName()).thenReturn("differentName");

        /** CALL METHOD UNDER TEST **/
        uut.doRenameDiskResource(diskResourceMock, "newName");
        verify(diskResourceServiceMock).renameDiskResource(eq(diskResourceMock),
                                                           eq("newName"),
                                                           isA(RenameDiskResourceCallback.class));
    }

}
