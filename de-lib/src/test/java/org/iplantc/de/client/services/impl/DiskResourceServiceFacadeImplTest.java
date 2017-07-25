package org.iplantc.de.client.services.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.DEClientConstants;
import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.events.diskResources.FolderRefreshedEvent;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.dataLink.DataLinkFactory;
import org.iplantc.de.client.models.diskResources.DiskResourceAutoBeanFactory;
import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.client.services.converters.DECallbackConverter;
import org.iplantc.de.shared.DECallback;
import org.iplantc.de.shared.DEProperties;
import org.iplantc.de.shared.services.DiscEnvApiService;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.common.collect.Lists;
import com.google.gwtmockito.GwtMockitoTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jstroot
 */
@RunWith(GwtMockitoTestRunner.class)
public class DiskResourceServiceFacadeImplTest {

    @Mock DiscEnvApiService deServiceFacadeMock;
    @Mock DEProperties dePropertiesMock;
    @Mock DEClientConstants deConstantsMock;
    @Mock DiskResourceAutoBeanFactory drAutoBeanFactoryMock;
    @Mock
    DataLinkFactory dataLinkFactoryMock;
    @Mock UserInfo userInfoMock;
    @Mock EventBus eventBusMock;
    @Mock DECallback<List<Folder>> folderListCallbackMock;

    @Captor ArgumentCaptor<DECallback<List<Folder>>> folderListCallbackCaptor;


    @Before public void setup() {
    }

    @Test public void refreshFolder_onSuccess() {
        final Folder folderMock = mock(Folder.class);
        DiskResourceServiceFacadeImpl spy = spy(new DiskResourceServiceFacadeImpl(deServiceFacadeMock,
                                                                                  dePropertiesMock,
                                                                                  deConstantsMock,
                                                                                  drAutoBeanFactoryMock,
                                                                                  dataLinkFactoryMock,
                                                                                  userInfoMock) {
            public Folder findModel(Folder model) {
                return folderMock;
            }
            public void removeChildren(Folder parent){

            }
            public void getSubFolders(Folder parent, DECallback<List<Folder>> callback){
                callback.onSuccess(Lists.<Folder>newArrayList());
            }
        });
        spy.eventBus = eventBusMock;
        when(folderMock.getFolders()).thenReturn(null);

        // Call unit under test
        spy.refreshFolder(folderMock, folderListCallbackMock);


        ArgumentCaptor<FolderRefreshedEvent> folderRefreshedEventCaptor = ArgumentCaptor.forClass(FolderRefreshedEvent.class);
        verify(spy).removeChildren(eq(folderMock));
        verify(folderMock).setFolders(Matchers.<List<Folder>>eq(null));

        verify(folderListCallbackMock).onSuccess(Matchers.<List<Folder>>any());
        verify(eventBusMock).fireEvent(folderRefreshedEventCaptor.capture());
        assertEquals(folderMock, folderRefreshedEventCaptor.getValue().getFolder());
    }

    @Test public void refreshFolder_onFailure() {
        final Folder folderMock = mock(Folder.class);
        final Throwable throwableMock = mock(Throwable.class);
        DiskResourceServiceFacadeImpl spy = spy(new DiskResourceServiceFacadeImpl(deServiceFacadeMock,
                                                                                  dePropertiesMock,
                                                                                  deConstantsMock,
                                                                                  drAutoBeanFactoryMock,
                                                                                  dataLinkFactoryMock,
                                                                                  userInfoMock) {
            public Folder findModel(Folder model) {
                return folderMock;
            }

            public void removeChildren(Folder parent){ }

            public void getSubFolders(Folder parent, DECallback<List<Folder>> callback){
                callback.onFailure(500, throwableMock);
            }
        });
        spy.eventBus = eventBusMock;
        when(folderMock.getFolders()).thenReturn(null);

        // Call unit under test
        spy.refreshFolder(folderMock, folderListCallbackMock);

        verify(spy).removeChildren(eq(folderMock));
        verify(folderMock).setFolders(Matchers.<List<Folder>>eq(null));

        verify(spy).getSubFolders(eq(folderMock), folderListCallbackCaptor.capture());

        verify(folderListCallbackMock).onFailure(anyInt(), eq(throwableMock));
        verifyZeroInteractions(eventBusMock);
    }

    @Test public void getSubFolders_hasFoldersLoaded() {
        final Folder folderMock = mock(Folder.class);
        DiskResourceServiceFacadeImpl spy = spy(new DiskResourceServiceFacadeImpl(deServiceFacadeMock,
                                                                                  dePropertiesMock,
                                                                                  deConstantsMock,
                                                                                  drAutoBeanFactoryMock,
                                                                                  dataLinkFactoryMock,
                                                                                  userInfoMock) {
            public Folder findModel(Folder model) {
                return folderMock;
            }
        });
        spy.eventBus = eventBusMock;

        final ArrayList<Folder> subFolders = Lists.newArrayList();
        when(folderMock.getFolders()).thenReturn(subFolders);

        // Call unit under test
        spy.getSubFolders(folderMock, folderListCallbackMock);

        verify(folderListCallbackMock).onSuccess(eq(subFolders));
    }

    @Test public void getSubFolders_noFoldersLoaded() {
        final Folder folderMock = mock(Folder.class);
        DiskResourceServiceFacadeImpl spy = spy(new DiskResourceServiceFacadeImpl(deServiceFacadeMock,
                                                                                  dePropertiesMock,
                                                                                  deConstantsMock,
                                                                                  drAutoBeanFactoryMock,
                                                                                  dataLinkFactoryMock,
                                                                                  userInfoMock) {
            public Folder findModel(Folder model) {
                return folderMock;
            }
        });
        spy.eventBus = eventBusMock;

        when(folderMock.getFolders()).thenReturn(null);

        // Call unit under test
        spy.getSubFolders(folderMock, folderListCallbackMock);

        verify(spy).callService(any(ServiceCallWrapper.class),
                                Matchers.<DECallbackConverter<String, List<Folder>>>any());
        verify(folderListCallbackMock, never()).onSuccess(Matchers.<List<Folder>>any());
    }
}
