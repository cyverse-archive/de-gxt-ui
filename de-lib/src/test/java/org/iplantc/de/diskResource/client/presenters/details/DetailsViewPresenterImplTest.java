package org.iplantc.de.diskResource.client.presenters.details;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwtmockito.GwtMockitoTestRunner;
import com.google.web.bindery.autobean.shared.AutoBean;
import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.tags.IplantTagAutoBeanFactory;
import org.iplantc.de.client.models.tags.IplantTagList;
import org.iplantc.de.client.models.tags.Tag;
import org.iplantc.de.client.services.FileSystemMetadataServiceFacade;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.diskResource.client.DetailsView;
import org.iplantc.de.diskResource.client.events.selection.RemoveResourceTagSelected;
import org.iplantc.de.diskResource.client.events.selection.UpdateResourceTagSelected;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class DetailsViewPresenterImplTest {

    @Mock IplantAnnouncer announcerMock;
    @Mock FileSystemMetadataServiceFacade metadataServiceMock;
    @Mock DetailsView.Presenter.Appearance appearanceMock;
    @Mock DetailsView viewMock;
    @Mock Tag tagMock;
    @Mock DiskResource diskResourceMock;
    @Mock
    List<String> tagIdListMock;
    @Mock
    IplantTagList tagListMock;
    @Mock
    IplantTagAutoBeanFactory tagAutoBeanFactory;

    @Captor ArgumentCaptor<AsyncCallback<Void>> voidCaptor;

    DetailsViewPresenterImpl uut;

    @Mock
    AutoBean<Tag> tagAbMock;

    @Mock
    AutoBean<IplantTagList> tagListAbMock;

    @Before
    public void setUp() {
        uut = new DetailsViewPresenterImpl(viewMock, tagAutoBeanFactory) {
            @Override
            List<String> wrapInList(String tagId) {
                return tagIdListMock;
            }
        };

        uut.metadataService = metadataServiceMock;
        uut.appearance = appearanceMock;
        uut.announcer = announcerMock;
        uut.factory = tagAutoBeanFactory;
    }


    @Test
    public void onUpdateResourceTagSelected() {
        UpdateResourceTagSelected eventMock = mock(UpdateResourceTagSelected.class);
        when(eventMock.getDiskResource()).thenReturn(diskResourceMock);
        when(eventMock.getTag()).thenReturn(tagMock);
        when(appearanceMock.tagAttached(any(), any())).thenReturn("success");

        when(tagAutoBeanFactory.getTag()).thenReturn(tagAbMock);
        when(tagAbMock.as()).thenReturn(tagMock);

        when(tagMock.getId()).thenReturn("1");
        when(tagMock.getValue()).thenReturn("TestTag");

        when(tagAutoBeanFactory.getTagList()).thenReturn(tagListAbMock);
        when(tagListAbMock.as()).thenReturn(tagListMock);


        when(diskResourceMock.getId()).thenReturn("2");

        uut.onUpdateResourceTagSelected(eventMock);

        verify(metadataServiceMock).attachTags(eq(tagIdListMock), eq("2"), voidCaptor.capture());

        voidCaptor.getValue().onSuccess(null);
    }

    @Test
    public void onRemoveResourceTagSelected() {
        RemoveResourceTagSelected eventMock = mock(RemoveResourceTagSelected.class);
        when(eventMock.getResource()).thenReturn(diskResourceMock);

        when(tagAutoBeanFactory.getTag()).thenReturn(tagAbMock);
        when(tagAbMock.as()).thenReturn(tagMock);
        when(eventMock.getTag()).thenReturn(tagMock);

        when(tagAutoBeanFactory.getTagList()).thenReturn(tagListAbMock);
        when(tagListAbMock.as()).thenReturn(tagListMock);

        when(appearanceMock.tagDetached(any(), any())).thenReturn("success");
        when(tagMock.getId()).thenReturn("1");

        when(tagMock.getValue()).thenReturn("TestTag");
        when(diskResourceMock.getId()).thenReturn("2");

        uut.onRemoveResourceTagSelected(eventMock);

        verify(metadataServiceMock).detachTags(eq(tagIdListMock), eq("2"), voidCaptor.capture());

        voidCaptor.getValue().onSuccess(null);
    }

}
