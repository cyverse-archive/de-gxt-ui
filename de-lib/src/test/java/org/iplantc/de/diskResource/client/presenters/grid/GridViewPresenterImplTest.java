package org.iplantc.de.diskResource.client.presenters.grid;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.diskResources.DiskResourceAutoBeanFactory;
import org.iplantc.de.client.models.diskResources.PermissionValue;
import org.iplantc.de.client.models.diskResources.TYPE;
import org.iplantc.de.client.models.viewer.InfoType;
import org.iplantc.de.client.services.DiskResourceServiceFacade;
import org.iplantc.de.client.services.FileSystemMetadataServiceFacade;
import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.commons.client.comments.view.dialogs.CommentsDialog;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.diskResource.client.DiskResourceView;
import org.iplantc.de.diskResource.client.GridView;
import org.iplantc.de.diskResource.client.NavigationView;
import org.iplantc.de.diskResource.client.events.RequestDiskResourceFavoriteEvent;
import org.iplantc.de.diskResource.client.events.selection.EditInfoTypeSelected;
import org.iplantc.de.diskResource.client.events.selection.ManageCommentsSelected;
import org.iplantc.de.diskResource.client.gin.factory.FolderContentsRpcProxyFactory;
import org.iplantc.de.diskResource.client.gin.factory.GridViewFactory;
import org.iplantc.de.diskResource.client.views.dialogs.InfoTypeEditorDialog;
import org.iplantc.de.diskResource.client.views.dialogs.MetadataCopyDialog;
import org.iplantc.de.diskResource.client.views.dialogs.SaveAsDialog;
import org.iplantc.de.diskResource.client.views.grid.DiskResourceColumnModel;
import org.iplantc.de.diskResource.client.views.metadata.dialogs.ManageMetadataDialog;
import org.iplantc.de.diskResource.client.views.sharing.dialogs.DataSharingDialog;
import org.iplantc.de.diskResource.client.views.sharing.dialogs.ShareResourceLinkDialog;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwtmockito.GxtMockitoTestRunner;

import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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
    @Mock AsyncProviderWrapper<ShareResourceLinkDialog> shareLinkDialogProviderMock;
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

    @Captor ArgumentCaptor<AsyncCallback<InfoTypeEditorDialog>> infoTypeEditorDialogCaptor;
    @Captor ArgumentCaptor<AsyncCallback<String>> stringCaptor;
    @Captor ArgumentCaptor<AsyncCallback<CommentsDialog>> commentsDialogCaptor;

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
        };
        uut.announcer = announcerMock;
        uut.infoTypeDialogProvider = infoTypeDialogProviderMock;
        uut.metadataService = metadataServiceMock;
        uut.commentDialogProvider = commentDialogProviderMock;
        uut.diskResourceService = diskResourceServiceMock;

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
        when(eventMock.getSelectedDiskResources()).thenReturn(resourcesMock);
        when(infoTypeEditorDialogMock.getSelectedValue()).thenReturn(infoTypeMock);

        /** CALL METHOD UNDER TEST **/
        uut.onEditInfoTypeSelected(eventMock);
        verify(infoTypeDialogProviderMock).get(infoTypeEditorDialogCaptor.capture());

        infoTypeEditorDialogCaptor.getValue().onSuccess(infoTypeEditorDialogMock);
        verify(infoTypeEditorDialogMock).addOkButtonSelectHandler(Matchers.<SelectEvent.SelectHandler>any());
        infoTypeEditorDialogMock.show(isA(InfoType.class));
    }

    @Test
    public void testOnFavoriteRequest_favorite() {
        RequestDiskResourceFavoriteEvent eventMock = mock(RequestDiskResourceFavoriteEvent.class);
        when(eventMock.getDiskResource()).thenReturn(resourceMock);
        when(resourceMock.isFavorite()).thenReturn(false);

        /** CALL METHOD UNDER TEST **/
        uut.onFavoriteRequest(eventMock);
        verify(metadataServiceMock).addToFavorites(eq("id"), stringCaptor.capture());
    }

    @Test
    public void testOnFavoriteRequest_defavorite() {
        RequestDiskResourceFavoriteEvent eventMock = mock(RequestDiskResourceFavoriteEvent.class);
        when(eventMock.getDiskResource()).thenReturn(resourceMock);
        when(resourceMock.isFavorite()).thenReturn(true);

        /** CALL METHOD UNDER TEST **/
        uut.onFavoriteRequest(eventMock);
        verify(metadataServiceMock).removeFromFavorites(eq("id"), stringCaptor.capture());
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
    }

    @Test
    public void testCopyMetadata() {
    }

    @Test
    public void testOnRequestManageSharingSelected() {
    }

    @Test
    public void testOnRequestShareByDataLinkSelected() {
    }

    @Test
    public void testFetchDetails() {
    }

    @Test
    public void testSetInfoType() {
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
    }

    @Test
    public void testOnDownloadTemplateSelected() {
    }
}
