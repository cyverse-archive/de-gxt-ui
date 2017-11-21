package org.iplantc.de.diskResource.client.presenters.details;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.tags.Tag;
import org.iplantc.de.client.services.FileSystemMetadataServiceFacade;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;
import org.iplantc.de.diskResource.client.DetailsView;
import org.iplantc.de.diskResource.client.events.selection.RemoveResourceTagSelected;
import org.iplantc.de.diskResource.client.events.selection.UpdateResourceTagSelected;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwtmockito.GwtMockitoTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.List;

@RunWith(GwtMockitoTestRunner.class)
public class DetailsViewPresenterImplTest {

    @Mock IplantAnnouncer announcerMock;
    @Mock FileSystemMetadataServiceFacade metadataServiceMock;
    @Mock DetailsView.Presenter.Appearance appearanceMock;
    @Mock DetailsView viewMock;
    @Mock Tag tagMock;
    @Mock DiskResource diskResourceMock;
    @Mock List<Tag> tagListMock;

    @Captor ArgumentCaptor<AsyncCallback<Void>> voidCaptor;

    DetailsViewPresenterImpl uut;

    @Before
    public void setUp() {
        uut = new DetailsViewPresenterImpl(viewMock){
            @Override
            List<Tag> wrapInList(Tag tag) {
                return tagListMock;
            }
        };

        uut.metadataService = metadataServiceMock;
        uut.appearance = appearanceMock;
        uut.announcer = announcerMock;

        testConstructor();
    }

    void testConstructor() {
        verify(viewMock).addRemoveResourceTagSelectedHandler(eq(uut));
        verify(viewMock).addUpdateResourceTagSelectedHandler(eq(uut));
    }

    @Test
    public void onUpdateResourceTagSelected() {
        UpdateResourceTagSelected eventMock = mock(UpdateResourceTagSelected.class);
        when(eventMock.getDiskResource()).thenReturn(diskResourceMock);
        when(eventMock.getTag()).thenReturn(tagMock);
        when(appearanceMock.tagAttached(any(), any())).thenReturn("success");

        uut.onUpdateResourceTagSelected(eventMock);

        verify(metadataServiceMock).attachTags(eq(tagListMock), eq(diskResourceMock), voidCaptor.capture());

        voidCaptor.getValue().onSuccess(null);

        verify(announcerMock).schedule(isA(SuccessAnnouncementConfig.class));
    }

    @Test
    public void onRemoveResourceTagSelected() {
        RemoveResourceTagSelected eventMock = mock(RemoveResourceTagSelected.class);
        when(eventMock.getResource()).thenReturn(diskResourceMock);
        when(eventMock.getTag()).thenReturn(tagMock);
        when(appearanceMock.tagDetached(any(), any())).thenReturn("success");

        uut.onRemoveResourceTagSelected(eventMock);

        verify(metadataServiceMock).detachTags(eq(tagListMock), eq(diskResourceMock), voidCaptor.capture());

        voidCaptor.getValue().onSuccess(null);

        verify(announcerMock).schedule(isA(SuccessAnnouncementConfig.class));

    }

}
