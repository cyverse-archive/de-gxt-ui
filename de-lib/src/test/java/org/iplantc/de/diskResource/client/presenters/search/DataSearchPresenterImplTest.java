package org.iplantc.de.diskResource.client.presenters.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.search.DiskResourceQueryTemplate;
import org.iplantc.de.client.models.search.SearchAutoBeanFactory;
import org.iplantc.de.client.services.CollaboratorsServiceFacade;
import org.iplantc.de.client.services.SearchServiceFacade;
import org.iplantc.de.client.services.TagsServiceFacade;
import org.iplantc.de.client.services.callbacks.ReactSuccessCallback;
import org.iplantc.de.client.util.SearchModelUtils;
import org.iplantc.de.collaborators.client.util.CollaboratorsUtil;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.diskResource.client.SearchView;
import org.iplantc.de.diskResource.client.events.search.DeleteSavedSearchClickedEvent;
import org.iplantc.de.diskResource.client.events.search.UpdateSavedSearchesEvent;
import org.iplantc.de.tags.client.TagsView;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwtmockito.GxtMockitoTestRunner;
import com.google.web.bindery.autobean.shared.Splittable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author jstroot
 * 
 */
@SuppressWarnings("nls")
@RunWith(GxtMockitoTestRunner.class)
public class DataSearchPresenterImplTest {

    @Mock SearchServiceFacade searchService;
    @Mock IplantAnnouncer announcer;
    @Mock SearchView.SearchViewAppearance appearance;
    @Mock SearchView viewMock;
    @Mock TagsView.TagSuggestionProxy proxyMock;
    @Mock TagsServiceFacade tagsServiceMock;
    @Mock SearchAutoBeanFactory factoryMock;
    @Mock SearchModelUtils searchModelUtilsMock;
    @Mock CollaboratorsServiceFacade collaboratorsServiceFacade;
    @Mock CollaboratorsUtil collaboratorsUtilMock;
    @Mock ReactSuccessCallback reactCallbackMock;
    @Mock List<Subject> subjectListMock;
    @Mock Splittable subjectSplittable;

    @Captor ArgumentCaptor<List<DiskResourceQueryTemplate>> drqtListCaptor;
    @Captor ArgumentCaptor<UpdateSavedSearchesEvent> updateSavedSearchesEventCaptor;
    @Captor ArgumentCaptor<AsyncCallback<List<DiskResourceQueryTemplate>>> stringAsyncCbCaptor;
    @Captor ArgumentCaptor<AsyncCallback<List<DiskResourceQueryTemplate>>> drqtListAsyncCaptor;
    @Captor ArgumentCaptor<AsyncCallback<List<Subject>>> subjectListCaptor;

    private DataSearchPresenterImpl dsPresenter;

    @Before public void setUp() {
        dsPresenter = new DataSearchPresenterImpl(searchService,
                                                  collaboratorsServiceFacade,
                                                  collaboratorsUtilMock,
                                                  announcer,
                                                  appearance,
                                                  viewMock,
                                                  proxyMock,
                                                  tagsServiceMock,
                                                  factoryMock,
                                                  searchModelUtilsMock);
    }

    /**
     * Verifies that nothing will occur if a query template's name is null
     * 
     * @see org.iplantc.de.diskResource.client.SearchView.Presenter#onSaveSearch(Splittable, String)
     */
    @Test public void testDoSaveDiskResourceQueryTemplate_Case1() {
        DataSearchPresenterImpl spy = spy(dsPresenter);
        Splittable splTemplate = mock(Splittable.class);
        String originalName = "originalName";
        DiskResourceQueryTemplate mockTemplate = mock(DiskResourceQueryTemplate.class);
        // This mock will return null for a call to getName() by default
        when(searchModelUtilsMock.convertSplittableToTemplate(splTemplate)).thenReturn(mockTemplate);
        spy.onSaveSearch(splTemplate, originalName);
        // Perform verify for record keeping
        verify(spy).onSaveSearch(eq(splTemplate), eq(originalName));

        verifyZeroInteractions(searchService);

        verifyNoMoreInteractions(spy);
    }

    /**
     * Verifies that nothing will occur if a query template's name is an empty string.
     * 
     * @see org.iplantc.de.diskResource.client.SearchView.Presenter#onSaveSearch(Splittable, String)
     */
    @Test public void testDoSaveDiskResourceQueryTemplate_Case2() {
        DataSearchPresenterImpl spy = spy(dsPresenter);
        Splittable splTemplate = mock(Splittable.class);
        String originalName = "originalName";
        DiskResourceQueryTemplate mockTemplate = mock(DiskResourceQueryTemplate.class);
        when(searchModelUtilsMock.convertSplittableToTemplate(splTemplate)).thenReturn(mockTemplate);

        when(mockTemplate.getName()).thenReturn("");

        spy.onSaveSearch(splTemplate, originalName);
        // Perform verify for record keeping
        verify(spy).onSaveSearch(eq(splTemplate), eq(originalName));

        verifyZeroInteractions(searchService);

        verifyNoMoreInteractions(spy);
    }

    /**
     * Verifies that an existing query template will be replaced when a request to save a template of the
     * same name is received.
     * 
     * @see org.iplantc.de.diskResource.client.SearchView.Presenter#onSaveSearch(Splittable, String)
     */
    @Test public void testDoSaveDiskResourceQueryTemplate_Case3() {
        DataSearchPresenterImpl spy = spy(dsPresenter);
        Splittable splTemplate = mock(Splittable.class);
        String originalName = "originalName";

        /* ================ Save first template =================== */
        DiskResourceQueryTemplate mockTemplate = mock(DiskResourceQueryTemplate.class);
        when(mockTemplate.getName()).thenReturn("firstMock");
        when(searchModelUtilsMock.convertSplittableToTemplate(splTemplate)).thenReturn(mockTemplate);

        // Call method under test
        spy.onSaveSearch(splTemplate, originalName);

        /* Verify that the service was called to save the template, and only 1 template was saved */
        verify(searchService).saveQueryTemplates(drqtListCaptor.capture(), drqtListAsyncCaptor.capture());
        assertEquals("list passed to search service is expected size", 1, drqtListCaptor.getValue().size());
        assertTrue("list passed to search service contains the template to be saved", drqtListCaptor.getValue().contains(mockTemplate));
        // Mock expected behavior from service success
        drqtListAsyncCaptor.getValue().onSuccess(drqtListCaptor.getValue());


        /* ================ Save second template =================== */
        DiskResourceQueryTemplate mockTemplate_2 = mock(DiskResourceQueryTemplate.class);
        when(mockTemplate_2.getName()).thenReturn("secondMock");
        when(searchModelUtilsMock.convertSplittableToTemplate(splTemplate)).thenReturn(mockTemplate_2);

        // Call method under test
        spy.onSaveSearch(splTemplate, originalName);


        /* Verify that the service was called to save the template, and only 2 templates were saved */
        verify(searchService, times(2)).saveQueryTemplates(drqtListCaptor.capture(), drqtListAsyncCaptor.capture());
        assertEquals("list passed to search service is expected size", 2, drqtListCaptor.getValue().size());
        assertTrue("list passed to search service contains previously saved template", drqtListCaptor.getValue().contains(mockTemplate));
        assertTrue("list passed to search service contains the template to be saved", drqtListCaptor.getValue().contains(mockTemplate_2));
        // Mock expected behavior from service success
        drqtListAsyncCaptor.getValue().onSuccess(drqtListCaptor.getValue());


        /* ================ Save third template =================== */
        DiskResourceQueryTemplate mockTemplate_3 = mock(DiskResourceQueryTemplate.class);
        when(mockTemplate_3.getName()).thenReturn("firstMock");
        when(searchModelUtilsMock.convertSplittableToTemplate(splTemplate)).thenReturn(mockTemplate_3);
        spy.onSaveSearch(splTemplate, originalName);


        /* Verify that the service was called to save the template, and only 2 templates were saved but one of them was
         * replaced.
         */
        verify(searchService, times(3)).saveQueryTemplates(drqtListCaptor.capture(), drqtListAsyncCaptor.capture());
        assertEquals("list passed to search service is expected size", 2, drqtListCaptor.getValue().size());
        assertTrue("list passed to search service contains previously saved template", drqtListCaptor.getValue().contains(mockTemplate_2));
        assertTrue("list passed to search service contains previously saved template", drqtListCaptor.getValue().contains(mockTemplate_3));
        assertFalse("list passed to search service contains the template to be saved", drqtListCaptor.getValue().contains(mockTemplate));
    }

    /**
     * Verifies that a search of a given query will be requested after it is successfully persisted.
     * 
     * @see org.iplantc.de.diskResource.client.SearchView.Presenter#onSaveSearch(Splittable, String)
     */
    @Test public void testDoSaveDiskResourceQueryTemplateOnSuccess_Case1() {
        DataSearchPresenterImpl spy = spy(dsPresenter);
        DiskResourceQueryTemplate mockTemplate = mock(DiskResourceQueryTemplate.class);
        when(mockTemplate.getName()).thenReturn("mock1");
        Splittable splTemplate = mock(Splittable.class);
        String originalName = "originalName";
        when(searchModelUtilsMock.convertSplittableToTemplate(splTemplate)).thenReturn(mockTemplate);

        spy.onSaveSearch(splTemplate, originalName);
        verify(searchService).saveQueryTemplates(drqtListCaptor.capture(), drqtListAsyncCaptor.capture());

        assertEquals("Verify that the query has not been added to the presenter's list", 0, spy.getQueryTemplates().size());

        // Force service success
        drqtListAsyncCaptor.getValue().onSuccess(drqtListCaptor.getValue());

        verify(searchService).createFrozenList(drqtListCaptor.capture());
        assertEquals("Verify that list passed to createFrozenList is expected size", 1, drqtListCaptor.getValue().size());
        assertEquals("Verify that list passed to createFrozenList contains intended template", mockTemplate, drqtListCaptor.getValue().get(0));

        verify(spy).setCleanCopyQueryTemplates(anyListOf(DiskResourceQueryTemplate.class));

        assertEquals("Verify that the query has been added to the presenter's list after successful persist", 1, spy.queryTemplates.size());
        verifyNoMoreInteractions(searchService);
    }

    /**
     * Verifies that a query will be removed from the treestore when a name change is detected.
     */
    @Test public void testDoSaveDiskResourceQueryTemplateOnSuccess_Case2() {
        final String originalName = "originalMockName";
        DataSearchPresenterImpl spy = spy(dsPresenter);
        DiskResourceQueryTemplate mockTemplate = mock(DiskResourceQueryTemplate.class);
        DiskResourceQueryTemplate cleanMockTemplate = mock(DiskResourceQueryTemplate.class);

        when(mockTemplate.getName()).thenReturn("mock1");
        when(cleanMockTemplate.getName()).thenReturn(originalName);

        Splittable splTemplate = mock(Splittable.class);
        when(searchModelUtilsMock.convertSplittableToTemplate(splTemplate)).thenReturn(mockTemplate);

        spy.cleanCopyQueryTemplates = Lists.newArrayList(cleanMockTemplate);
        spy.onSaveSearch(splTemplate, originalName);
        verify(searchService).saveQueryTemplates(drqtListCaptor.capture(), drqtListAsyncCaptor.capture());
        
        // Force service success
        drqtListAsyncCaptor.getValue().onSuccess(Collections.<DiskResourceQueryTemplate> emptyList());

        verify(spy).fireEvent(updateSavedSearchesEventCaptor.capture());
        assertEquals("Verify that the event's remove list only contains one item",
                     1,
                     updateSavedSearchesEventCaptor.getValue().getRemovedSearches().size());
        assertEquals("Verify that the intended template was passed to event's remove list",
                     cleanMockTemplate,
                     updateSavedSearchesEventCaptor.getValue().getRemovedSearches().get(0));
    }
    
    /**
     * Verifies that a search will not be requested after a failure to persist a query.
     *
     * @see org.iplantc.de.diskResource.client.SearchView.Presenter#onSaveSearch(Splittable, String)
     */
    @Test public void testDoSaveDiskResourceQueryTemplate_Case5() {
        DataSearchPresenterImpl spy = spy(dsPresenter);
        DiskResourceQueryTemplate mockTemplate = mock(DiskResourceQueryTemplate.class);
        when(mockTemplate.getName()).thenReturn("mock1");
        when(appearance.saveQueryTemplateFail()).thenReturn("fail");

        Splittable splTemplate = mock(Splittable.class);
        String originalName = "originalName";
        when(searchModelUtilsMock.convertSplittableToTemplate(splTemplate)).thenReturn(mockTemplate);

        spy.onSaveSearch(splTemplate, originalName);
        // Perform verify for record keeping
        verify(spy).onSaveSearch(eq(splTemplate), eq(originalName));
        verify(searchService).saveQueryTemplates(drqtListCaptor.capture(), drqtListAsyncCaptor.capture());

        assertEquals("Verify that the query has not been added to the presenter's list", 0, spy.getQueryTemplates().size());

        // Force service failure
        drqtListAsyncCaptor.getValue().onFailure(null);

        /* Verify that a search is not requested after failure to persist */
        verifyNoMoreInteractions(searchService);

        assertEquals("Verify that the query has not been added to the presenter's list after failed persist", 0, spy.getQueryTemplates().size());
    }

    /**
     * Verifies the list passed to the updateNavigationWindow method when the given query template is not
     * new, and has been changed.
     * 
     * The list should be equivalent to what was returned by getQueryTemplates() prior to the method
     * call, but with the changed item in place of the non-changed item.
     * 
     * @see org.iplantc.de.diskResource.client.SearchView.Presenter#onSaveSearch(Splittable, String)
     */
    @Test public void testDoSaveDiskResourceQueryTemplate_Case6() {
        // Create presenter with overridden method to control test execution.
        dsPresenter = new DataSearchPresenterImpl(searchService,
                                                  collaboratorsServiceFacade,
                                                  collaboratorsUtilMock,
                                                  announcer,
                                                  appearance,
                                                  viewMock,
                                                  proxyMock,
                                                  tagsServiceMock,
                                                  factoryMock,
                                                  searchModelUtilsMock) {
            @Override
            boolean areTemplatesEqual(DiskResourceQueryTemplate lhs, DiskResourceQueryTemplate rhs) {
                return !(lhs.getName().equals("qtMockId1") && rhs.getName().equals("qtMockId1"));
            }
        };
        DataSearchPresenterImpl spy = spy(dsPresenter);
        DiskResourceQueryTemplate eventMockTemplate = mock(DiskResourceQueryTemplate.class);
        // Return empty string to indicate that the template is new
        Splittable splTemplate = mock(Splittable.class);
        when(searchModelUtilsMock.convertSplittableToTemplate(splTemplate)).thenReturn(eventMockTemplate);
        when(eventMockTemplate.getName()).thenReturn("qtMockId1");
        String originalName = "originalName";

        // Add an existing mock to the classes template list
        DiskResourceQueryTemplate existingMock1 = mock(DiskResourceQueryTemplate.class);
        DiskResourceQueryTemplate existingMock2 = mock(DiskResourceQueryTemplate.class);
        when(existingMock1.getName()).thenReturn("qtMockId1");
        when(existingMock2.getName()).thenReturn("qtMockId2");
        spy.getQueryTemplates().clear();
        ArrayList<DiskResourceQueryTemplate> existingTemplates = Lists.newArrayList(existingMock1,
                existingMock2);
        spy.getQueryTemplates().addAll(existingTemplates);
        int initialSize = spy.getQueryTemplates().size();

        spy.setCleanCopyQueryTemplates(existingTemplates);

        // Call method under test
        spy.onSaveSearch(splTemplate, originalName);
        verify(searchService)
                .saveQueryTemplates(drqtListCaptor.capture(), drqtListAsyncCaptor.capture());

        // Force service success
        when(searchService.createFrozenList(anyListOf(DiskResourceQueryTemplate.class))).thenReturn(
                drqtListCaptor.getValue());
        drqtListAsyncCaptor.getValue().onSuccess(drqtListCaptor.getValue());

        /* Verify that the setDirty flag of the changed template has been set */
        verify(eventMockTemplate).setDirty(eq(true));

        /* Verify that the given list only contains the existing and event mock templates */
        verify(spy).fireEvent(updateSavedSearchesEventCaptor.capture());
        assertEquals("verify that the size of the list has not changed",
                     initialSize,
                     drqtListCaptor.getValue().size());
        assertTrue("verify that the list contains the updated query template",
                   drqtListCaptor.getValue().contains(eventMockTemplate));
        assertTrue("verify that the list contains the unmodified starting original template",
                drqtListCaptor.getValue().contains(existingMock2));
        assertFalse("verify that the list does not contain the modified starting original template",
                drqtListCaptor.getValue().contains(existingMock1));
    }

    @Test public void testLoadSavedQueries_Case1() {
        DataSearchPresenterImpl spy = spy(dsPresenter);

        assertTrue("Verify that store query templates are empty prior to calling method under test", spy.getQueryTemplates().isEmpty());

        final ArrayList<DiskResourceQueryTemplate> newArrayList = Lists.newArrayList(mock(DiskResourceQueryTemplate.class), mock(DiskResourceQueryTemplate.class));
        // Call method under test
        spy.loadSavedQueries(newArrayList);
        // Verify for record keeping
        verify(spy).loadSavedQueries(eq(newArrayList));

        verify(spy).setCleanCopyQueryTemplates(anyListOf(DiskResourceQueryTemplate.class));
        verify(searchService).createFrozenList(eq(newArrayList));
        // Verify for record keeping
        verify(spy).getQueryTemplates();
        verify(spy).fireEvent(any(UpdateSavedSearchesEvent.class));

        verifyNoMoreInteractions(searchService, spy);
    }
    
    /**
     * 
     */
    @Test public void testOnDeleteSavedSearch_Case1() {
        DataSearchPresenterImpl spy = spy(dsPresenter);

        final DeleteSavedSearchClickedEvent mockEvent = mock(DeleteSavedSearchClickedEvent.class);
        final DiskResourceQueryTemplate mockSavedSearch = mock(DiskResourceQueryTemplate.class);
        when(mockEvent.getSavedSearch()).thenReturn(mockSavedSearch);
        when(appearance.deleteSearchSuccess(anyString())).thenReturn("success");
        spy.getQueryTemplates().add(mockSavedSearch);

        // Call method under test
        spy.onDeleteSavedSearchClicked(mockEvent);

        verify(mockEvent).getSavedSearch();

        verify(searchService).saveQueryTemplates(eq(Lists.newArrayList()),
                                                   drqtListAsyncCaptor.capture());

        verifyNoMoreInteractions(searchService);
    }

    @Test
    public void testSearchCollaborators() {
        String searchTerm = "search";

        dsPresenter.searchCollaborators(searchTerm, reactCallbackMock);

        verify(collaboratorsServiceFacade).searchCollaborators(eq(searchTerm),
                                                               subjectListCaptor.capture());

        subjectListCaptor.getValue().onSuccess(subjectListMock);
        verify(reactCallbackMock).onSuccess(any(Splittable.class));
    }
}
