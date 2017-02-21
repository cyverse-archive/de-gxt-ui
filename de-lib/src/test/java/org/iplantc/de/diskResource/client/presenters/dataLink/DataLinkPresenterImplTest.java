package org.iplantc.de.diskResource.client.presenters.dataLink;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.models.dataLink.DataLink;
import org.iplantc.de.client.models.dataLink.DataLinkFactory;
import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.services.DiskResourceServiceFacade;
import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.diskResource.client.DataLinkView;
import org.iplantc.de.diskResource.client.events.selection.DeleteDataLinkSelected;
import org.iplantc.de.diskResource.client.gin.factory.DataLinkViewFactory;
import org.iplantc.de.shared.DECallback;

import com.google.gwtmockito.GwtMockitoTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.Iterator;
import java.util.List;

/**
 * @author aramsey 
 */
@RunWith(GwtMockitoTestRunner.class)
public class DataLinkPresenterImplTest {

    @Mock DataLinkView viewMock;
    @Mock DiskResourceServiceFacade drServiceMock;
    @Mock DataLinkFactory dlFactoryMock;
    @Mock DiskResourceUtil diskResourceUtilMock;
    @Mock List<DiskResource> resourcesMock;
    @Mock List<DiskResource> diskResourceListMock;
    @Mock DataLinkViewFactory dataLinkViewFactoryMock;
    @Mock Iterator<DiskResource> diskResourceIteratorMock;
    @Mock DiskResource resourceMock;
    @Mock List<String> stringListMock;
    @Mock DataLink dataLinkMock;
    @Mock DataLinkView.Appearance appearanceMock;

    @Captor ArgumentCaptor<DECallback<String>> stringCallbackCaptor;
    @Captor ArgumentCaptor<DECallback<List<DataLink>>> dataLinkCallbackCaptor;


    private DataLinkPresenterImpl uut;

    @Before
    public void setUp() {
        when(dataLinkViewFactoryMock.create(isA(DataLinkPresenterImpl.class), eq(resourcesMock))).thenReturn(viewMock);
        when(resourcesMock.size()).thenReturn(2);
        when(resourcesMock.iterator()).thenReturn(diskResourceIteratorMock);
        when(diskResourceIteratorMock.hasNext()).thenReturn(true, true, false);
        when(diskResourceIteratorMock.next()).thenReturn(resourceMock, resourceMock);
        when(appearanceMock.loadingMask()).thenReturn("loading...");

        uut = new DataLinkPresenterImpl(drServiceMock,
                                        dataLinkViewFactoryMock,
                                        dlFactoryMock,
                                        diskResourceUtilMock,appearanceMock,
                                        resourcesMock){
            @Override
            List<DiskResource> createDiskResourcesList() {
                return diskResourceListMock;
            }

            @Override
            List<String> createDiskResourceIdsList() {
                return stringListMock;
            }
        };

        verifyConstructor();
    }

    private void verifyConstructor() {
        verify(dataLinkViewFactoryMock).create(isA(DataLinkPresenterImpl.class), eq(resourcesMock));
        verify(diskResourceListMock, times(2)).add(resourceMock);
    }

    @Test
    public void testGetExistingDataLinks() {
        when(diskResourceUtilMock.asStringPathList(resourcesMock)).thenReturn(stringListMock);

        /** CALL METHOD UNDER TEST **/
        uut.getExistingDataLinks(resourcesMock);

        verify(viewMock).addRoots(eq(resourcesMock));
        verify(drServiceMock).listDataLinks(eq(stringListMock), stringCallbackCaptor.capture());
    }

    @Test
    public void testOnDeleteDataLinkSelected() {
        DeleteDataLinkSelected eventMock = mock(DeleteDataLinkSelected.class);
        when(eventMock.getLink()).thenReturn(dataLinkMock);
        when(dataLinkMock.getId()).thenReturn("id");

        /** CALL METHOD UNDER TEST **/
        uut.onDeleteDataLinkSelected(eventMock);
        verify(drServiceMock).deleteDataLinks(anyListOf(String.class), stringCallbackCaptor.capture());
    }

    @Test
    public void testCreateDataLinks() {
        when(dataLinkMock.getPath()).thenReturn("string");

        /** CALL METHOD UNDER TEST **/
        uut.createDataLinks(resourcesMock);

        verify(viewMock).mask(appearanceMock.loadingMask());
        verify(drServiceMock).createDataLinks(eq(stringListMock), dataLinkCallbackCaptor.capture());
    }
}
