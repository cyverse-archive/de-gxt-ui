package org.iplantc.de.diskResource.client.presenters.sharing;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.models.HasPaths;
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.diskResources.DiskResourceAutoBeanFactory;
import org.iplantc.de.client.models.diskResources.sharing.DataPermission;
import org.iplantc.de.client.models.diskResources.sharing.DataSharingAutoBeanFactory;
import org.iplantc.de.client.models.diskResources.sharing.DataSharingRequest;
import org.iplantc.de.client.models.diskResources.sharing.DataSharingRequestList;
import org.iplantc.de.client.models.diskResources.sharing.DataUnsharingRequest;
import org.iplantc.de.client.models.diskResources.sharing.DataUnsharingRequestList;
import org.iplantc.de.client.models.diskResources.sharing.DataUserPermission;
import org.iplantc.de.client.models.diskResources.sharing.DataUserPermissionList;
import org.iplantc.de.client.models.sharing.OldUserPermission;
import org.iplantc.de.client.models.sharing.SharedResource;
import org.iplantc.de.client.models.sharing.Sharing;
import org.iplantc.de.client.services.CollaboratorsServiceFacade;
import org.iplantc.de.client.services.DiskResourceServiceFacade;
import org.iplantc.de.collaborators.client.util.CollaboratorsUtil;
import org.iplantc.de.commons.client.gin.factory.SharingPermissionViewFactory;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.presenter.SharingPresenter;
import org.iplantc.de.commons.client.views.sharing.SharingPermissionView;
import org.iplantc.de.diskResource.client.DataSharingView;
import org.iplantc.de.shared.DataCallback;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwtmockito.GwtMockitoTestRunner;
import com.google.web.bindery.autobean.shared.AutoBean;

import com.sencha.gxt.core.shared.FastMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@RunWith(GwtMockitoTestRunner.class)
public class DataSharingPresenterImplTest {

    @Mock DataSharingView viewMock;
    @Mock DiskResourceServiceFacade serviceFacadeMock;
    @Mock SharingPermissionView permissionsPanelMock;
    @Mock List<DiskResource> selectedResourcesMock;
    @Mock SharingPresenter.Appearance appearanceMock;
    @Mock DataSharingAutoBeanFactory dataSharingFactoryMock;
    @Mock DiskResourceAutoBeanFactory drFactoryMock;
    @Mock CollaboratorsServiceFacade collaboratorsServiceFacadeMock;
    @Mock CollaboratorsUtil collaboratorsUtilMock;
    @Mock DiskResource diskResourceMock;
    @Mock SharingPermissionViewFactory sharingViewFactoryMock;
    @Mock Widget shareWidgetMock;
    @Mock FastMap<SharedResource> fastMapSharedResourceMock;
    @Mock HasPaths hasPathsMock;
    @Mock AutoBean<HasPaths> hasPathsAutoBeanMock;
    @Mock Iterator<DiskResource> diskResourceIteratorMock;
    @Mock DataUserPermissionList dataUserPermissionListMock;
    @Mock List<DataUserPermission> dataUserPermissionsMock;
    @Mock DataUserPermission dataUserPermissionMock;
    @Mock Iterator<DataUserPermission> dataUserPermissionIteratorMock;
    @Mock List<OldUserPermission> oldUserPermissionsMock;
    @Mock Iterator<OldUserPermission> oldUserPermissionIteratorMock;
    @Mock OldUserPermission oldUserPermissionMock;
    @Mock ArrayList<String> stringListMock;
    @Mock FastMap<Subject> subjectFastMapMock;
    @Mock FastMap<List<Sharing>> sharingListFastMapMock;
    @Mock Subject subjectMock;
    @Mock List<Sharing> sharingListMock;
    @Mock Sharing sharingMock;
    @Mock DataSharingPresenterImpl.GetUserInfoCallback userInfoCallback;
    @Mock DataSharingRequestList dataSharingRequestListMock;
    @Mock DataUnsharingRequestList dataUnsharingRequestListMock;
    @Mock IplantAnnouncer iplantAnnouncerMock;
    @Mock ArrayList<DataSharingRequest> dataSharingRequestsMock;
    @Mock Set<String> keySetMock;
    @Mock Iterator<String> keySetIteratorMock;
    @Mock AutoBean<DataSharingRequest> dataSharingRequestAutoBeanMock;
    @Mock AutoBean<DataUnsharingRequest> dataUnsharingRequestAutoBeanMock;
    @Mock AutoBean<DataSharingRequestList> dataSharingRequestListAutoBeanMock;
    @Mock AutoBean<DataUnsharingRequestList> dataUnsharingRequestListAutoBeanMock;
    @Mock DataSharingRequest dataSharingRequestMock;
    @Mock DataUnsharingRequest dataUnsharingRequestMock;
    @Mock List<DataPermission> dataPermissionsMock;
    @Mock ArrayList<DataUnsharingRequest> dataUnsharingRequestsMock;

    @Captor ArgumentCaptor<DataCallback<DataUserPermissionList>> userPermissionListCaptor;
    @Captor ArgumentCaptor<DataSharingPresenterImpl.GetUserInfoCallback> getUserInfoCaptor;


    DataSharingPresenterImpl uut;
    DataSharingPresenterImpl spy;

    @Before
    public void setUp() {
        when(sharingViewFactoryMock.create(any(), any())).thenReturn(permissionsPanelMock);
        when(permissionsPanelMock.asWidget()).thenReturn(shareWidgetMock);
        when(drFactoryMock.pathsList()).thenReturn(hasPathsAutoBeanMock);
        when(hasPathsAutoBeanMock.as()).thenReturn(hasPathsMock);
        when(selectedResourcesMock.iterator()).thenReturn(diskResourceIteratorMock);
        when(diskResourceIteratorMock.hasNext()).thenReturn(true, false);
        when(diskResourceIteratorMock.next()).thenReturn(diskResourceMock);
        when(diskResourceMock.getPath()).thenReturn("path");
        when(dataUserPermissionsMock.iterator()).thenReturn(dataUserPermissionIteratorMock);
        when(dataUserPermissionIteratorMock.hasNext()).thenReturn(true, false);
        when(dataUserPermissionIteratorMock.next()).thenReturn(dataUserPermissionMock);
        when(oldUserPermissionsMock.iterator()).thenReturn(oldUserPermissionIteratorMock);
        when(oldUserPermissionIteratorMock.hasNext()).thenReturn(true, false);
        when(oldUserPermissionIteratorMock.next()).thenReturn(oldUserPermissionMock);
        when(keySetMock.iterator()).thenReturn(keySetIteratorMock);
        when(keySetIteratorMock.hasNext()).thenReturn(true, false);
        when(dataSharingFactoryMock.getDataSharingRequest()).thenReturn(dataSharingRequestAutoBeanMock);
        when(dataSharingFactoryMock.getDataUnsharingRequest()).thenReturn(dataUnsharingRequestAutoBeanMock);
        when(dataSharingRequestAutoBeanMock.as()).thenReturn(dataSharingRequestMock);
        when(dataUnsharingRequestAutoBeanMock.as()).thenReturn(dataUnsharingRequestMock);
        when(dataSharingFactoryMock.getDataSharingRequestList()).thenReturn(dataSharingRequestListAutoBeanMock);
        when(dataSharingFactoryMock.getDataUnsharingRequestList()).thenReturn(dataUnsharingRequestListAutoBeanMock);
        when(dataSharingRequestListAutoBeanMock.as()).thenReturn(dataSharingRequestListMock);
        when(dataUnsharingRequestListAutoBeanMock.as()).thenReturn(dataUnsharingRequestListMock);

        uut = new DataSharingPresenterImpl(serviceFacadeMock,
                                           selectedResourcesMock,
                                           viewMock,
                                           collaboratorsUtilMock,
                                           collaboratorsServiceFacadeMock,
                                           appearanceMock,
                                           sharingViewFactoryMock,
                                           dataSharingFactoryMock,
                                           drFactoryMock) {
            @Override
            FastMap<SharedResource> getSelectedResourcesAsMap(List<DiskResource> selectedResources) {
                return fastMapSharedResourceMock;
            }

            @Override
            HasPaths getPaths(List<DiskResource> selectedResources) {
                return hasPathsMock;
            }

            @Override
            ArrayList<String> createEmptyStringList() {
                return stringListMock;
            }

            @Override
            FastMap<List<Sharing>> createSharingListFastMap() {
                return sharingListFastMapMock;
            }

            @Override
            Sharing buildDataSharing(OldUserPermission userPerms, Subject user, String path) {
                return sharingMock;
            }

            @Override
            ArrayList<DataSharingRequest> createEmptyDataSharingRequestList() {
                return dataSharingRequestsMock;
            }

            @Override
            List<DataPermission> buildShareDataPermissionList(List<Sharing> shareList) {
                return dataPermissionsMock;
            }

            @Override
            List<String> buildUnsharePathList(List<Sharing> shareList) {
                return stringListMock;
            }

            @Override
            ArrayList<DataUnsharingRequest> createEmptyDataUnsharingRequestList() {
                return dataUnsharingRequestsMock;
            }
        };
        uut.selectedResources = selectedResourcesMock;
        uut.iplantAnnouncer = iplantAnnouncerMock;

        spy = spy(uut);

        testConstructor();
    }

    private void testConstructor() {
        verify(viewMock).addShareWidget(eq(shareWidgetMock));
    }

    @Test
    public void testGo() {
        HasOneWidget containerMock = mock(HasOneWidget.class);
        Widget viewWidget = mock(Widget.class);
        when(viewMock.asWidget()).thenReturn(viewWidget);

        /** CALL METHOD UNDER TEST **/
        spy.go(containerMock);
        verify(containerMock).setWidget(eq(viewWidget));
        verify(spy).loadResources();
        verify(spy).loadPermissions();
    }

    @Test
    public void loadResources() {

        /** CALL METHOD UNDER TEST **/
        uut.loadResources();
        verify(viewMock).setSelectedDiskResources(eq(selectedResourcesMock));
    }

    @Test
    public void loadPermissions() {
        when(dataUserPermissionListMock.getDataUserPermissions()).thenReturn(dataUserPermissionsMock);
        when(dataUserPermissionMock.getUserPermissions()).thenReturn(oldUserPermissionsMock);
        when(oldUserPermissionMock.getUser()).thenReturn("user");
        when(subjectFastMapMock.get("user")).thenReturn(subjectMock);
        when(collaboratorsUtilMock.getDummySubject("user")).thenReturn(subjectMock);
        when(sharingListFastMapMock.get("user")).thenReturn(sharingListMock);
        when(dataUserPermissionMock.getPath()).thenReturn("path");

        /** CALL METHOD UNDER TEST **/
        uut.loadPermissions();

        verify(serviceFacadeMock).getPermissions(eq(hasPathsMock),
                                                 userPermissionListCaptor.capture());

        //LoadPermissionsCallback
        userPermissionListCaptor.getValue().onSuccess(dataUserPermissionListMock);

        verify(collaboratorsServiceFacadeMock).getUserInfo(eq(stringListMock),
                                                           getUserInfoCaptor.capture());
    }

    @Test
    public void processRequest_share() {
        when(appearanceMock.sharingCompleteMsg()).thenReturn("success");
        uut = new DataSharingPresenterImpl(serviceFacadeMock,
                                           selectedResourcesMock,
                                           viewMock,
                                           collaboratorsUtilMock,
                                           collaboratorsServiceFacadeMock,
                                           appearanceMock,
                                           sharingViewFactoryMock,
                                           dataSharingFactoryMock,
                                           drFactoryMock) {

            @Override
            DataSharingRequestList buildSharingRequest() {
                return dataSharingRequestListMock;
            }

            @Override
            DataUnsharingRequestList buildUnsharingRequest() {
                return null;
            }

            @Override
            List<DataPermission> buildShareDataPermissionList(List<Sharing> shareList) {
                return dataPermissionsMock;
            }
        };

        spy = spy(uut);
        spy.iplantAnnouncer = iplantAnnouncerMock;

        /** CALL METHOD UNDER TEST **/
        spy.processRequest();

        verify(spy).callSharingService(eq(dataSharingRequestListMock));
        verify(spy, times(0)).callUnshareService(any());
    }

    @Test
    public void processRequest_unshare() {
        when(appearanceMock.sharingCompleteMsg()).thenReturn("success");
        uut = new DataSharingPresenterImpl(serviceFacadeMock,
                                           selectedResourcesMock,
                                           viewMock,
                                           collaboratorsUtilMock,
                                           collaboratorsServiceFacadeMock,
                                           appearanceMock,
                                           sharingViewFactoryMock,
                                           dataSharingFactoryMock,
                                           drFactoryMock) {

            @Override
            DataSharingRequestList buildSharingRequest() {
                return null;
            }

            @Override
            DataUnsharingRequestList buildUnsharingRequest() {
                return dataUnsharingRequestListMock;
            }
        };

        spy = spy(uut);
        spy.iplantAnnouncer = iplantAnnouncerMock;

        /** CALL METHOD UNDER TEST **/
        spy.processRequest();

        verify(spy).callUnshareService(eq(dataUnsharingRequestListMock));
        verify(spy, times(0)).callSharingService(any());
    }

    @Test
    public void buildSharingRequest() {
        when(permissionsPanelMock.getSharingMap()).thenReturn(sharingListFastMapMock);
        when(sharingListFastMapMock.size()).thenReturn(1);
        when(sharingListFastMapMock.keySet()).thenReturn(keySetMock);
        when(keySetIteratorMock.next()).thenReturn("key");
        when(sharingListFastMapMock.get("key")).thenReturn(sharingListMock);

        /** CALL METHOD UNDER TEST **/
        uut.buildSharingRequest();

        verify(dataSharingRequestMock).setUser(eq("key"));
        verify(sharingListFastMapMock).get(eq("key"));
        verify(dataSharingRequestMock).setDataPermissions(eq(dataPermissionsMock));
        verify(dataSharingRequestListMock).setDataSharingRequestList(eq(dataSharingRequestsMock));
    }

    @Test
    public void buildUnsharingRequest() {
        when(permissionsPanelMock.getUnshareList()).thenReturn(sharingListFastMapMock);
        when(sharingListFastMapMock.size()).thenReturn(1);
        when(sharingListFastMapMock.keySet()).thenReturn(keySetMock);
        when(keySetIteratorMock.next()).thenReturn("key");
        when(sharingListFastMapMock.get("key")).thenReturn(sharingListMock);

        /** CALL METHOD UNDER TEST **/
        uut.buildUnsharingRequest();

        verify(dataUnsharingRequestMock).setUser(eq("key"));
        verify(sharingListFastMapMock).get(eq("key"));
        verify(dataUnsharingRequestMock).setPaths(eq(stringListMock));
        verify(dataUnsharingRequestListMock).setDataUnsharingRequests(eq(dataUnsharingRequestsMock));
    }

    @Test
    public void callSharingService() {

        /** CALL METHOD UNDER TEST **/
        uut.callSharingService(dataSharingRequestListMock);
        verify(serviceFacadeMock).shareDiskResource(eq(dataSharingRequestListMock),
                                                      any());
    }

    @Test
    public void callUnshareService() {

        /** CALL METHOD UNDER TEST **/
        uut.callUnshareService(dataUnsharingRequestListMock);
        verify(serviceFacadeMock).unshareDiskResource(eq(dataUnsharingRequestListMock),
                                                    any());
    }

}
