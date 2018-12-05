package org.iplantc.de.diskResource.client.presenters.grid;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.HasPath;
import org.iplantc.de.client.models.dataLink.DataLink;
import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.diskResources.DiskResourceAutoBeanFactory;
import org.iplantc.de.client.models.diskResources.File;
import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.client.models.diskResources.MetadataCopyRequest;
import org.iplantc.de.client.models.diskResources.MetadataTemplateInfo;
import org.iplantc.de.client.models.diskResources.TYPE;
import org.iplantc.de.client.models.sharing.PermissionValue;
import org.iplantc.de.client.models.viewer.InfoType;
import org.iplantc.de.client.services.DiskResourceServiceFacade;
import org.iplantc.de.client.services.FileSystemMetadataServiceFacade;
import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.commons.client.comments.view.dialogs.CommentsDialog;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;
import org.iplantc.de.diskResource.client.BulkMetadataView;
import org.iplantc.de.diskResource.client.DiskResourceView;
import org.iplantc.de.diskResource.client.GridView;
import org.iplantc.de.diskResource.client.NavigationView;
import org.iplantc.de.diskResource.client.events.FetchDetailsCompleted;
import org.iplantc.de.diskResource.client.events.RequestDiskResourceFavoriteEvent;
import org.iplantc.de.diskResource.client.events.selection.BulkMetadataSelected;
import org.iplantc.de.diskResource.client.events.selection.CopyMetadataSelected;
import org.iplantc.de.diskResource.client.events.selection.DownloadTemplateSelectedEvent;
import org.iplantc.de.diskResource.client.events.selection.EditInfoTypeSelected;
import org.iplantc.de.diskResource.client.events.selection.ManageCommentsSelected;
import org.iplantc.de.diskResource.client.events.selection.ManageMetadataSelected;
import org.iplantc.de.diskResource.client.events.selection.ManageSharingSelected;
import org.iplantc.de.diskResource.client.events.selection.SaveMetadataSelected;
import org.iplantc.de.diskResource.client.events.selection.ShareByDataLinkSelected;
import org.iplantc.de.diskResource.client.gin.factory.FolderContentsRpcProxyFactory;
import org.iplantc.de.diskResource.client.gin.factory.GridViewFactory;
import org.iplantc.de.diskResource.client.gin.factory.ShareResourcesLinkDialogFactory;
import org.iplantc.de.diskResource.client.views.dialogs.InfoTypeEditorDialog;
import org.iplantc.de.diskResource.client.views.dialogs.MetadataCopyDialog;
import org.iplantc.de.diskResource.client.views.dialogs.SaveAsDialog;
import org.iplantc.de.diskResource.client.views.grid.DiskResourceColumnModel;
import org.iplantc.de.diskResource.client.views.metadata.dialogs.BulkMetadataDialog;
import org.iplantc.de.diskResource.client.views.metadata.dialogs.ManageMetadataDialog;
import org.iplantc.de.diskResource.client.views.metadata.dialogs.SelectMetadataTemplateDialog;
import org.iplantc.de.diskResource.client.views.sharing.dialogs.DataSharingDialog;
import org.iplantc.de.diskResource.client.views.sharing.dialogs.ShareResourceLinkDialog;
import org.iplantc.de.shared.AsyncProviderWrapper;
import org.iplantc.de.shared.DataCallback;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwtmockito.GxtMockitoTestRunner;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.grid.LiveGridCheckBoxSelectionModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author jstroot
 */
@RunWith(GxtMockitoTestRunner.class)
public class GridViewPresenterImplTest {

    @Mock FolderContentsRpcProxyFactory folderContentsProxyFactoryMock;
    @Mock GridViewFactory gridViewFactoryMock;
    @Mock List<InfoType> infoTypeFiltersMock;
    @Mock NavigationView.Presenter navigationPresenterMock;
    @Mock TYPE entityTypeMock;
    @Mock GridView viewMock;
    @Mock GridView.FolderContentsRpcProxy folderContentsProxyMock;
    @Mock DiskResourceColumnModel columnModelMock;
    @Mock GridView.Presenter.Appearance appearanceMock;
    @Mock IplantAnnouncer announcerMock;
    @Mock DiskResourceServiceFacade diskResourceServiceMock;
    @Mock DiskResourceUtil diskResourceUtilMock;
    @Mock EventBus eventBusMock;
    @Mock FileSystemMetadataServiceFacade metadataServiceMock;
    @Mock AsyncProviderWrapper<InfoTypeEditorDialog> infoTypeDialogProviderMock;
    @Mock InfoTypeEditorDialog infoTypeEditorDialogMock;
    @Mock AsyncProviderWrapper<CommentsDialog> commentDialogProviderMock;
    @Mock CommentsDialog commentsDialogMock;
    @Mock AsyncProviderWrapper<ManageMetadataDialog> metadataDialogProviderMock;
    @Mock AsyncProviderWrapper<DataSharingDialog> dataSharingDialogProviderMock;
    @Mock AsyncProviderWrapper<SaveAsDialog> saveAsDialogProviderMock;
    @Mock DiskResourceAutoBeanFactory drFactoryMock;
    @Mock MetadataCopyDialog mCopyDialog;
    @Mock ListStore<DiskResource> listStoreMock;
    @Mock HashMap<EventHandler, HandlerRegistration> registeredHandlersMock;
    @Mock DiskResourceView.Presenter parentPresenterMock;
    @Mock List<DiskResource> resourcesMock;
    @Mock Iterator<DiskResource> resourceIteratorMock;
    @Mock DiskResource resourceMock;
    @Mock InfoType infoTypeMock;
    @Mock PermissionValue permissionValueMock;
    @Mock AsyncProviderWrapper<ManageMetadataDialog> manageMetadataDlgProviderMock;
    @Mock ManageMetadataDialog manageMetadataDlgMock;
    @Mock AsyncProviderWrapper<MetadataCopyDialog> copyMetadataDlgProviderMock;
    @Mock MetadataCopyDialog copyMetadataDlgMock;
    @Mock List<HasPath> hasPathListMock;
    @Mock AsyncProviderWrapper<BulkMetadataDialog> bulkMetadataDlgProviderMock;
    @Mock BulkMetadataDialog bulkMetadataDlgMock;
    @Mock LiveGridCheckBoxSelectionModel selectionModelMock;
    @Mock SelectEvent selectEventMock;
    @Mock AlertMessageBox alertMessageBoxMock;
    @Mock Splittable splittableMock;
    @Mock DataSharingDialog dataSharingDlgMock;
    @Mock DialogHideEvent hideEventMock;
    @Mock FastMap<TYPE> fastMapTypeMock;
    @Mock ShareResourceLinkDialog shareResourceLinkDlgMock;
    @Mock List<String> stringListMock;
    @Mock FastMap<List<DataLink>> fastMapDataLinksMock;
    @Mock List<DataLink> dataLinksMock;
    @Mock FastMap<DiskResource> fastMapDiskResourceMock;
    @Mock HandlerManager handlerManagerMock;
    @Mock AsyncProviderWrapper<SaveAsDialog> saveAsDlgProviderMock;
    @Mock SaveAsDialog saveAsDlgMock;
    @Mock AsyncProviderWrapper<SelectMetadataTemplateDialog> selectTemplateDlgProviderMock;
    @Mock SelectMetadataTemplateDialog selectTemplateDlgMock;
    @Mock List<MetadataTemplateInfo> metadataTemplateInfosMock;
    @Mock MetadataCopyRequest metadataCopyRequestMock;
    @Mock
    ShareResourcesLinkDialogFactory srldFactoryMock;

    @Captor ArgumentCaptor<AsyncCallback<InfoTypeEditorDialog>> infoTypeEditorDialogCaptor;
    @Captor ArgumentCaptor<AsyncCallback<String>> stringCaptor;
    @Captor ArgumentCaptor<AsyncCallback<CommentsDialog>> commentsDialogCaptor;
    @Captor ArgumentCaptor<AsyncCallback<ManageMetadataDialog>> manageMetadataDlgCaptor;
    @Captor ArgumentCaptor<AsyncCallback<MetadataCopyDialog>> copyMetadataDlgCaptor;
    @Captor ArgumentCaptor<AsyncCallback<BulkMetadataDialog>> bulkMetadataDlgCaptor;
    @Captor ArgumentCaptor<SelectEvent.SelectHandler> selectHandlerCaptor;
    @Captor ArgumentCaptor<DataCallback<String>> dataStringCallback;
    @Captor ArgumentCaptor<AsyncCallback<DataSharingDialog>> dataSharingDlgCaptor;
    @Captor ArgumentCaptor<DialogHideEvent.DialogHideHandler> dialogHideCaptor;
    @Captor ArgumentCaptor<AsyncCallback<ShareResourceLinkDialog>> shareResourceLinkDlgCaptor;
    @Captor ArgumentCaptor<DataCallback<FastMap<List<DataLink>>>> fastMapDataLinkCaptor;
    @Captor ArgumentCaptor<DataCallback<FastMap<DiskResource>>> fastMapDiskResourceCaptor;
    @Captor ArgumentCaptor<AsyncCallback<SaveAsDialog>> saveAsDlgCaptor;
    @Captor ArgumentCaptor<AsyncCallback<List<MetadataTemplateInfo>>> metadataTemplatesInfoCaptor;
    @Captor ArgumentCaptor<AsyncCallback<SelectMetadataTemplateDialog>> selectTemplateDlgCaptor;

    private GridViewPresenterImpl uut;

    @Before public void setUp() {
        when(folderContentsProxyFactoryMock.createWithEntityType(eq(infoTypeFiltersMock), eq(entityTypeMock))).thenReturn(folderContentsProxyMock);
        when(gridViewFactoryMock.create(any(GridView.Presenter.class), Matchers.<ListStore<DiskResource>>any(), eq(folderContentsProxyMock))).thenReturn(viewMock);
        when(viewMock.getColumnModel()).thenReturn(columnModelMock);
        when(resourcesMock.size()).thenReturn(1);
        when(resourcesMock.iterator()).thenReturn(resourceIteratorMock);
        when(resourceIteratorMock.hasNext()).thenReturn(true, false);
        when(resourceIteratorMock.next()).thenReturn(resourceMock);
        when(resourceMock.getInfoType()).thenReturn(InfoType.RAW.toString());
        when(resourceMock.getId()).thenReturn("id");
        when(resourceMock.getPermission()).thenReturn(permissionValueMock);
        when(infoTypeMock.toString()).thenReturn(InfoType.RAW.toString());
        when(appearanceMock.markFavoriteError()).thenReturn("error");
        when(appearanceMock.removeFavoriteError()).thenReturn("error");

        uut = new GridViewPresenterImpl(gridViewFactoryMock,
                                        folderContentsProxyFactoryMock,
                                        appearanceMock,
                                        eventBusMock,
                                        navigationPresenterMock,
                                        infoTypeFiltersMock,
                                        entityTypeMock) {
            @Override
            public List<DiskResource> getSelectedDiskResources() {
                return resourcesMock;
            }

            @Override
            boolean isViewingFavoritesFolder() {
                return true;
            }

            @Override
            boolean hasOwnPermissions(DiskResource dr) {
                return true;
            }

            @Override
            ListStore<DiskResource> getDiskResourceListStore() {
                return listStoreMock;
            }

            @Override
            AlertMessageBox getNoResourcesMessageBox(DiskResource selected) {
                return alertMessageBoxMock;
            }

            @Override
            MetadataCopyRequest buildCopyRequest (List<HasPath> paths) {
                return metadataCopyRequestMock;
            }

            @Override
            HandlerManager ensureHandlers() {
                return handlerManagerMock;
            }
        };
        uut.announcer = announcerMock;
        uut.infoTypeDialogProvider = infoTypeDialogProviderMock;
        uut.metadataService = metadataServiceMock;
        uut.commentDialogProvider = commentDialogProviderMock;
        uut.diskResourceService = diskResourceServiceMock;
        uut.metadataDialogProvider = manageMetadataDlgProviderMock;
        uut.copyMetadataDlgProvider = copyMetadataDlgProviderMock;
        uut.bulkMetadataDlgProvider = bulkMetadataDlgProviderMock;
        uut.dataSharingDialogProvider = dataSharingDialogProviderMock;
        uut.diskResourceUtil = diskResourceUtilMock;
        uut.saveAsDialogProvider = saveAsDialogProviderMock;
        uut.selectMetaTemplateDlgProvider = selectTemplateDlgProviderMock;
        uut.srldFactory = srldFactoryMock;

        verifyConstructor();
    }

    private void verifyConstructor() {
        verify(columnModelMock).addDiskResourceNameSelectedEventHandler(eq(uut));
        verify(columnModelMock).addManageSharingSelectedEventHandler(eq(uut));
        verify(columnModelMock).addManageMetadataSelectedEventHandler(eq(uut));
        verify(columnModelMock).addCopyMetadataSelectedEventHandler(eq(uut));
        verify(columnModelMock).addShareByDataLinkSelectedEventHandler(eq(uut));
        verify(columnModelMock).addManageFavoritesEventHandler(eq(uut));
        verify(columnModelMock).addManageCommentsSelectedEventHandler(eq(uut));
        verify(columnModelMock).addDiskResourcePathSelectedEventHandler(eq(uut));

        verify(viewMock).addDiskResourceSelectionChangedEventHandler(eq(uut));
    }

    @Test
    public void testOnEditInfoTypeSelected() {
        EditInfoTypeSelected eventMock = mock(EditInfoTypeSelected.class);
        Stream<DiskResource> mockStr = mock(Stream.class);
        when(eventMock.getSelectedDiskResources()).thenReturn(resourcesMock);
        when(resourcesMock.get(0)).thenReturn(resourceMock);
        when(resourceMock.getInfoType()).thenReturn("txt");
        when(infoTypeEditorDialogMock.getSelectedValue()).thenReturn(infoTypeMock);
        when(resourcesMock.stream()).thenReturn(mockStr);

        /** CALL METHOD UNDER TEST **/
        uut.onEditInfoTypeSelected(eventMock);
        verify(infoTypeDialogProviderMock).get(infoTypeEditorDialogCaptor.capture());

        infoTypeEditorDialogCaptor.getValue().onSuccess(infoTypeEditorDialogMock);
        verify(infoTypeEditorDialogMock).addOkButtonSelectHandler(Matchers.<SelectEvent.SelectHandler>any());
        infoTypeEditorDialogMock.show();
    }

    @Test
    public void testOnFavoriteRequest_favorite() {
        RequestDiskResourceFavoriteEvent eventMock = mock(RequestDiskResourceFavoriteEvent.class);
        when(eventMock.getDiskResource()).thenReturn(resourceMock);
        when(resourceMock.isFavorite()).thenReturn(false);
        GridViewPresenterImpl spy = spy(uut);

        /** CALL METHOD UNDER TEST **/
        spy.onFavoriteRequest(eventMock);
        verify(metadataServiceMock).addToFavorites(eq("id"), stringCaptor.capture());

        stringCaptor.getValue().onSuccess("result");
        verify(spy).updateFav(eq(resourceMock), eq(true));
    }

    @Test
    public void testOnFavoriteRequest_defavorite() {
        RequestDiskResourceFavoriteEvent eventMock = mock(RequestDiskResourceFavoriteEvent.class);
        when(eventMock.getDiskResource()).thenReturn(resourceMock);
        when(resourceMock.isFavorite()).thenReturn(true);
        GridViewPresenterImpl spy = spy(uut);

        /** CALL METHOD UNDER TEST **/
        spy.onFavoriteRequest(eventMock);
        verify(metadataServiceMock).removeFromFavorites(eq("id"), stringCaptor.capture());
        stringCaptor.getValue().onSuccess("result");
        verify(spy).updateFav(eq(resourceMock), eq(false));

    }

    @Test
    public void testOnManageCommentsSelected() {
        ManageCommentsSelected eventMock = mock(ManageCommentsSelected.class);
        when(eventMock.getDiskResource()).thenReturn(resourceMock);

        /** CALL METHOD UNDER TEST **/
        uut.onManageCommentsSelected(eventMock);
        verify(commentDialogProviderMock).get(commentsDialogCaptor.capture());

        commentsDialogCaptor.getValue().onSuccess(commentsDialogMock);
        verify(commentsDialogMock).show(eq(resourceMock), eq(true), eq(metadataServiceMock));
    }

    @Test
    public void testOnRequestManageMetadataSelected() {
        ManageMetadataSelected eventMock = mock(ManageMetadataSelected.class);
        when(eventMock.getDiskResource()).thenReturn(resourceMock);

        /** CALL METHOD UNDER TEST **/
        uut.onRequestManageMetadataSelected(eventMock);

        verify(manageMetadataDlgProviderMock).get(manageMetadataDlgCaptor.capture());

        manageMetadataDlgCaptor.getValue().onSuccess(manageMetadataDlgMock);
        verify(manageMetadataDlgMock).show(eq(resourceMock));
    }

    @Test
    public void testOnRequestCopyMetadataSelected() {
        CopyMetadataSelected eventMock = mock(CopyMetadataSelected.class);
        when(eventMock.getDiskResource()).thenReturn(resourceMock);
        when(resourceMock.getPath()).thenReturn("path");
        when(copyMetadataDlgMock.getSource()).thenReturn(resourceMock);
        when(copyMetadataDlgMock.getValue()).thenReturn(hasPathListMock);
        when(appearanceMock.copyMetadata("path")).thenReturn("copyMetadata");
        when(appearanceMock.copyMetadataNoResources()).thenReturn("noResources");
        when(hasPathListMock.size()).thenReturn(1);

        GridViewPresenterImpl spy = spy(uut);

        /** CALL METHOD UNDER TEST **/
        spy.onRequestCopyMetadataSelected(eventMock);

        verify(copyMetadataDlgProviderMock).get(copyMetadataDlgCaptor.capture());

        copyMetadataDlgCaptor.getValue().onSuccess(copyMetadataDlgMock);
        verify(copyMetadataDlgMock).show(eq(resourceMock));
        verify(copyMetadataDlgMock).addOkButtonSelectHandler(selectHandlerCaptor.capture());
        selectHandlerCaptor.getValue().onSelect(selectEventMock);
        verify(spy).copyMetadata(eq(resourceMock), eq(hasPathListMock), eq(copyMetadataDlgMock));
    }

    @Test
    public void testOnBulkMetadataSelected() {
        BulkMetadataSelected eventMock = mock(BulkMetadataSelected.class);
        when(eventMock.getMode()).thenReturn(BulkMetadataView.BULK_MODE.SELECT);
        when(viewMock.getSelectionModel()).thenReturn(selectionModelMock);
        when(selectionModelMock.getSelectedItem()).thenReturn(resourceMock);
        when(resourceMock.getPath()).thenReturn("destPath");
        when(bulkMetadataDlgMock.isValid()).thenReturn(true);
        when(bulkMetadataDlgMock.getSelectedPath()).thenReturn("path");
        GridViewPresenterImpl spy = spy(uut);

        /** CALL METHOD UNDER TEST **/
        spy.onBulkMetadataSelected(eventMock);

        verify(bulkMetadataDlgProviderMock).get(bulkMetadataDlgCaptor.capture());
        bulkMetadataDlgCaptor.getValue().onSuccess(bulkMetadataDlgMock);
        verify(bulkMetadataDlgMock).show(eq(BulkMetadataView.BULK_MODE.SELECT));
        verify(bulkMetadataDlgMock).addOkButtonSelectHandler(selectHandlerCaptor.capture());
        selectHandlerCaptor.getValue().onSelect(selectEventMock);
        verify(spy).submitBulkMetadataFromExistingFile(eq("path"), eq("destPath"));
    }

    @Test
    public void testSubmitBulkMetadataFromExistingFile() {
        when(appearanceMock.bulkMetadataSuccess()).thenReturn("success");

        /** CALL METHOD UNDER TEST **/
        uut.submitBulkMetadataFromExistingFile("filePath", "destFolder");

        verify(diskResourceServiceMock).setBulkMetadataFromFile(eq("filePath"),
                                                                eq("destFolder"),
                                                                dataStringCallback.capture());

        dataStringCallback.getValue().onSuccess("result");
        verify(announcerMock).schedule(isA(SuccessAnnouncementConfig.class));
    }

    @Test
    public void testOnRequestManageSharingSelected() {
        ManageSharingSelected eventMock = mock(ManageSharingSelected.class);
        when(eventMock.getDiskResourceToShare()).thenReturn(resourcesMock);
        when(resourceMock.isFilter()).thenReturn(false);
        when(diskResourceUtilMock.asStringPathTypeMap(any(), any())).thenReturn(fastMapTypeMock);
        GridViewPresenterImpl spy = spy(uut);

        /** CALL METHOD UNDER TEST **/
        spy.onRequestManageSharingSelected(eventMock);

        verify(dataSharingDialogProviderMock).get(dataSharingDlgCaptor.capture());
        dataSharingDlgCaptor.getValue().onSuccess(dataSharingDlgMock);
        verify(dataSharingDlgMock).show(eq(resourcesMock));
        verify(dataSharingDlgMock).addDialogHideHandler(dialogHideCaptor.capture());

        dialogHideCaptor.getValue().onDialogHide(hideEventMock);
        verify(spy).fetchDetails(eq(resourceMock));
    }

    @Test
    public void testOnRequestShareByDataLinkSelected_Folder() {
        ShareByDataLinkSelected eventMock = mock(ShareByDataLinkSelected.class);
        Folder folderMock = mock(Folder.class);
        when(eventMock.getDiskResourceToShare()).thenReturn(folderMock);
        when(srldFactoryMock.create(false)).thenReturn(shareResourceLinkDlgMock);

        /** CALL METHOD UNDER TEST **/
        uut.onRequestShareByDataLinkSelected(eventMock);

        verify(srldFactoryMock).create(false);
        verify(shareResourceLinkDlgMock).show(any());
    }

    @Test
    public void testOnRequestShareByDataLinkSelected_File() {
        ShareByDataLinkSelected eventMock = mock(ShareByDataLinkSelected.class);
        File fileMock = mock(File.class);
        when(eventMock.getDiskResourceToShare()).thenReturn(fileMock);
        when(diskResourceUtilMock.asStringPathList(any())).thenReturn(stringListMock);
        when(fileMock.getPath()).thenReturn("path");
        when(fastMapDataLinksMock.get("path")).thenReturn(dataLinksMock);
        when(dataLinksMock.isEmpty()).thenReturn(false);
        when(srldFactoryMock.create(false)).thenReturn(shareResourceLinkDlgMock);
        GridViewPresenterImpl spy = spy(uut);

        /**  CALL METHOD UNDER TEST **/
        spy.onRequestShareByDataLinkSelected(eventMock);

        verify(diskResourceServiceMock).listDataLinks(eq(stringListMock), fastMapDataLinkCaptor.capture());
   }

    @Test
    public void testFetchDetails() {
        when(diskResourceUtilMock.asStringPathTypeMap(any(), any())).thenReturn(fastMapTypeMock);
        when(resourceMock.getPath()).thenReturn("path");
        when(fastMapDiskResourceMock.get("path")).thenReturn(resourceMock);
        GridViewPresenterImpl spy = spy(uut);

        /** CALL METHOD UNDER TEST **/
        spy.fetchDetails(resourceMock);

        verify(diskResourceServiceMock).getStat(eq(fastMapTypeMock), fastMapDiskResourceCaptor.capture());

        fastMapDiskResourceCaptor.getValue().onSuccess(fastMapDiskResourceMock);
        verify(spy).updateDiskResource(eq(resourceMock));
        verify(handlerManagerMock).fireEvent(isA(FetchDetailsCompleted.class));
    }

    @Test
    public void testSetInfoType() {
        when(resourceMock.getPath()).thenReturn("path");
        GridViewPresenterImpl spy = spy(uut);

        /** CALL METHOD UNDER TEST **/
        spy.setInfoType(Arrays.asList(resourceMock), "newType");
        verify(diskResourceServiceMock).setFileType(eq("path"), eq("newType"), dataStringCallback.capture());

        dataStringCallback.getValue().onSuccess("result");
        verify(spy).fetchDetails(eq(resourceMock));
    }

    @Test
    public void testUpdateFav_removeFav() {
        DiskResource diskResourceMock = mock(DiskResource.class);
        when(diskResourceMock.getId()).thenReturn("id");

        /** CALL METHOD UNDER TEST **/
        uut.updateFav(diskResourceMock, false);

        verify(resourceMock).setFavorite(eq(false));
        verify(listStoreMock).remove(eq(resourceMock));
    }

    @Test
    public void testUpdateFav_addFav() {
        DiskResource diskResourceMock = mock(DiskResource.class);
        when(diskResourceMock.getId()).thenReturn("id");
        when(listStoreMock.findModelWithKey("id")).thenReturn(resourceMock);
        when(diskResourceServiceMock.combineDiskResources(resourceMock, resourceMock)).thenReturn(resourceMock);


        /** CALL METHOD UNDER TEST **/
        uut.updateFav(diskResourceMock, true);

        verify(resourceMock).setFavorite(eq(true));
        verify(listStoreMock, times(0)).remove(eq(resourceMock));
    }

    @Test
    public void testOnRequestSaveMetadataSelected() {
        SaveMetadataSelected eventMock = mock(SaveMetadataSelected.class);
        when(eventMock.getDiskResource()).thenReturn(resourceMock);
        when(resourceMock.getId()).thenReturn("id");
        Folder folderMock = mock(Folder.class);
        when(saveAsDlgMock.getSelectedFolder()).thenReturn(folderMock);
        when(folderMock.getPath()).thenReturn("path");
        when(saveAsDlgMock.getFileName()).thenReturn("fileName");
        when(appearanceMock.saving()).thenReturn("saving");

        /** CALL METHOD UNDER TEST **/
        uut.onRequestSaveMetadataSelected(eventMock);

        verify(saveAsDialogProviderMock).get(saveAsDlgCaptor.capture());

        saveAsDlgCaptor.getValue().onSuccess(saveAsDlgMock);
        verify(saveAsDlgMock).addOkButtonSelectHandler(selectHandlerCaptor.capture());
        selectHandlerCaptor.getValue().onSelect(selectEventMock);
        verify(diskResourceServiceMock).saveMetadata(eq("id"),
                                                     eq("path/fileName"),
                                                     eq(true),
                                                     isA(GridViewPresenterImpl.SaveMetadataCallback.class));
        verify(saveAsDlgMock).show(any());
    }

    @Test
    public void testOnDownloadTemplateSelected() {
        DownloadTemplateSelectedEvent eventMock = mock(DownloadTemplateSelectedEvent.class);
        MetadataTemplateInfo infoMock = mock(MetadataTemplateInfo.class);
        when(selectTemplateDlgMock.getSelectedTemplate()).thenReturn(infoMock);
        when(infoMock.getId()).thenReturn("id");
        when(diskResourceServiceMock.downloadTemplate("id")).thenReturn("url");

        /** CALL METHOD UNDER TEST **/
        uut.onDownloadTemplateSelected(eventMock);

        verify(diskResourceServiceMock).getMetadataTemplateListing(metadataTemplatesInfoCaptor.capture());

        metadataTemplatesInfoCaptor.getValue().onSuccess(metadataTemplateInfosMock);
        verify(selectTemplateDlgProviderMock).get(selectTemplateDlgCaptor.capture());

        selectTemplateDlgCaptor.getValue().onSuccess(selectTemplateDlgMock);
        verify(selectTemplateDlgMock).addOkButtonSelectHandler(selectHandlerCaptor.capture());
        selectHandlerCaptor.getValue().onSelect(selectEventMock);
        verify(diskResourceServiceMock).downloadTemplate("id");

        verify(selectTemplateDlgMock).show(eq(metadataTemplateInfosMock), eq(false));
    }
}
