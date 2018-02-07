package org.iplantc.de.diskResource.client.presenters.dataLink;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.models.dataLink.DataLink;
import org.iplantc.de.client.models.dataLink.UpdateTicketResponse;
import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.services.DiskResourceServiceFacade;
import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.diskResource.client.DataLinkView;
import org.iplantc.de.diskResource.client.events.selection.CreateDataLinkSelected;
import org.iplantc.de.diskResource.client.events.selection.DeleteDataLinkSelected;
import org.iplantc.de.diskResource.client.events.selection.ShowDataLinkSelected;
import org.iplantc.de.diskResource.client.gin.factory.DataLinkDialogFactory;
import org.iplantc.de.diskResource.client.views.dialogs.DataLinkDialog;
import org.iplantc.de.shared.AsyncProviderWrapper;
import org.iplantc.de.shared.DECallback;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwtmockito.GwtMockitoTestRunner;

import com.sencha.gxt.core.shared.FastMap;

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
 * @author aramsey 
 */
@RunWith(GwtMockitoTestRunner.class)
public class DataLinkPresenterImplTest {

    @Mock DataLinkView viewMock;
    @Mock DiskResourceServiceFacade drServiceMock;
    @Mock DiskResourceUtil diskResourceUtilMock;
    @Mock List<DiskResource> resourcesMock;
    @Mock List<DiskResource> diskResourceListMock;
    @Mock Iterator<DiskResource> diskResourceIteratorMock;
    @Mock DiskResource resourceMock;
    @Mock List<String> stringListMock;
    @Mock DataLink dataLinkMock;
    @Mock DataLinkView.Appearance appearanceMock;
    @Mock AsyncProviderWrapper<DataLinkDialog> dataLinkDlgProviderMock;
    @Mock DataLinkDialog dataLinkDlgMock;

    @Captor ArgumentCaptor<DECallback<UpdateTicketResponse>> ticketResponseCallbackCaptor;
    @Captor ArgumentCaptor<DECallback<List<DataLink>>> dataLinkCallbackCaptor;
    @Captor ArgumentCaptor<DECallback<FastMap<List<DataLink>>>> mapLinkCallbackCaptor;
    @Captor ArgumentCaptor<AsyncCallback<DataLinkDialog>> dataLinkDlgCaptor;

    @Mock
    DataLinkDialogFactory dldFactoryMock;

    private DataLinkPresenterImpl uut;

    @Before
    public void setUp() {
        when(resourcesMock.size()).thenReturn(2);
        when(resourcesMock.iterator()).thenReturn(diskResourceIteratorMock);
        when(diskResourceIteratorMock.hasNext()).thenReturn(true, true, false);
        when(diskResourceIteratorMock.next()).thenReturn(resourceMock, resourceMock);
        when(appearanceMock.loadingMask()).thenReturn("loading...");
        when(diskResourceUtilMock.asStringPathList(resourcesMock)).thenReturn(stringListMock);

        uut = new DataLinkPresenterImpl(drServiceMock,
                                        viewMock,
                                        diskResourceUtilMock,
                                        appearanceMock,
                                        resourcesMock){
            @Override
            List<DiskResource> createDiskResourcesList() {
                return resourcesMock;
            }

            @Override
            List<String> createDiskResourceIdsList() {
                return stringListMock;
            }
        };

        uut.dldFactory = dldFactoryMock;

        verifyConstructor();
    }

    private void verifyConstructor() {
        verify(viewMock).addCreateDataLinkSelectedHandler(eq(uut));
        verify(viewMock).addAdvancedSharingSelectedHandler(eq(uut));
        verify(viewMock).addShowDataLinkSelectedHandler(eq(uut));
        verify(viewMock).addDeleteDataLinkSelectedHandler(eq(uut));

        testGetExistingDataLinks();
    }

    private void testGetExistingDataLinks() {
        verify(viewMock).addRoots(eq(resourcesMock));
        verify(drServiceMock).listDataLinks(eq(stringListMock), mapLinkCallbackCaptor.capture());
    }

    @Test
    public void testOnDeleteDataLinkSelected() {
        DeleteDataLinkSelected eventMock = mock(DeleteDataLinkSelected.class);
        when(eventMock.getLink()).thenReturn(dataLinkMock);
        when(dataLinkMock.getId()).thenReturn("id");

        /** CALL METHOD UNDER TEST **/
        uut.onDeleteDataLinkSelected(eventMock);
        verify(drServiceMock).deleteDataLinks(anyListOf(String.class), ticketResponseCallbackCaptor.capture());
    }

    @Test
    public void testCreateDataLinks() {
        CreateDataLinkSelected eventMock = mock(CreateDataLinkSelected.class);
        when(eventMock.getSelectedDiskResources()).thenReturn(resourcesMock);
        when(dataLinkMock.getPath()).thenReturn("string");

        /** CALL METHOD UNDER TEST **/
        uut.onCreateDataLinkSelected(eventMock);

        verify(viewMock).mask(appearanceMock.loadingMask());
        verify(drServiceMock).createDataLinks(eq(stringListMock), dataLinkCallbackCaptor.capture());
    }

    @Test
    public void onShowDataLinkPresented() {
        DataLink dl1 = mock(DataLink.class);
        DataLink dl2 = mock(DataLink.class);

        when(dl1.getDownloadUrl()).thenReturn("http://google.com");
        when(dl2.getDownloadUrl()).thenReturn("http://google.com");

        ShowDataLinkSelected eventMock = mock(ShowDataLinkSelected.class);
        when(eventMock.getSelectedResource()).thenReturn(Arrays.asList(dl1, dl2));

        when(dldFactoryMock.create(true)).thenReturn(dataLinkDlgMock);

        /** CALL METHOD UNDER TEST **/
        uut.onShowDataLinkSelected(eventMock);

        verify(dldFactoryMock).create(eq(true));

        verify(dataLinkDlgMock).show(eq("http://google.com\nhttp://google.com"));
    }
}
