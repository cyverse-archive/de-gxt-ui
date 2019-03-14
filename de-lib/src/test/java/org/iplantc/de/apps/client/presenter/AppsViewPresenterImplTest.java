package org.iplantc.de.apps.client.presenter;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.iplantc.de.apps.client.AppCategoriesView;
import org.iplantc.de.apps.client.AppsListView;
import org.iplantc.de.apps.client.AppsToolbarView;
import org.iplantc.de.apps.client.CommunitiesView;
import org.iplantc.de.apps.client.OntologyHierarchiesView;
import org.iplantc.de.apps.client.WorkspaceView;
import org.iplantc.de.apps.client.gin.factory.AppsViewFactory;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.gwtmockito.GwtMockitoTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.List;

@RunWith(GwtMockitoTestRunner.class)
public class AppsViewPresenterImplTest {

    private AppsViewPresenterImpl uut;
    @Mock AppCategoriesView categoriesViewMock;
    @Mock AppCategoriesView.Presenter categoriesPresenterMock;
    @Mock OntologyHierarchiesView hierarchiesView;
    @Mock OntologyHierarchiesView.Presenter hierarchiesPresenter;
    @Mock WorkspaceView.Presenter workspacePresenterMock;
    @Mock CommunitiesView.Presenter communitiesPresenterMock;

    @Mock AppsListView.Presenter listPresenterMock;

    @Mock AppsToolbarView toolbarViewMock;
    @Mock AppsToolbarView.Presenter toolbarPresenterMock;

    @Mock AppsViewFactory viewFactoryMock;

    @Before public void setUp() {
        when(categoriesPresenterMock.getWorkspaceView()).thenReturn(categoriesViewMock);
        when(toolbarPresenterMock.getView()).thenReturn(toolbarViewMock);
        when(workspacePresenterMock.getCategoriesPresenter()).thenReturn(categoriesPresenterMock);
        when(workspacePresenterMock.getCommunitiesPresenter()).thenReturn(communitiesPresenterMock);
        uut = new AppsViewPresenterImpl(viewFactoryMock,
                                        listPresenterMock,
                                        toolbarPresenterMock,
                                        hierarchiesPresenter,
                                        workspacePresenterMock);
    }

    @Test public void testConstructorEventHandlerWiring() {
        verify(viewFactoryMock).create(eq(categoriesPresenterMock),
                                       eq(communitiesPresenterMock),
                                       eq(hierarchiesPresenter),
                                       eq(listPresenterMock),
                                       eq(toolbarPresenterMock));

        // Verify categories wiring
        verify(categoriesPresenterMock).addAppCategorySelectedEventHandler(eq(listPresenterMock));
        verify(categoriesPresenterMock).addAppCategorySelectedEventHandler(eq(toolbarViewMock));
        verify(categoriesPresenterMock).addAppCategorySelectedEventHandler(eq(communitiesPresenterMock));

        hierarchiesPresenter.addOntologyHierarchySelectionChangedEventHandler(listPresenterMock);
        hierarchiesPresenter.addOntologyHierarchySelectionChangedEventHandler(toolbarViewMock);
        hierarchiesPresenter.addSelectedHierarchyNotFoundHandler(eq(categoriesPresenterMock));

        // Verify grid wiring
        verify(listPresenterMock).addAppSelectionChangedEventHandler(toolbarViewMock);
        verify(listPresenterMock).addAppInfoSelectedEventHandler(hierarchiesPresenter);

        verify(communitiesPresenterMock).addCommunitySelectedEventHandler(eq(listPresenterMock));
        verify(communitiesPresenterMock).addCommunitySelectedEventHandler(eq(toolbarViewMock));
        verify(communitiesPresenterMock).addCommunitySelectedEventHandler(eq(categoriesPresenterMock));

        // Verify toolbar wiring
        verify(toolbarViewMock).addDeleteAppsSelectedHandler(listPresenterMock);
        verify(toolbarViewMock).addCopyAppSelectedHandler(categoriesPresenterMock);
        verify(toolbarViewMock).addCopyWorkflowSelectedHandler(categoriesPresenterMock);
        verify(toolbarViewMock).addRunAppSelectedHandler(listPresenterMock);
        verify(toolbarViewMock).addAppSearchResultLoadEventHandler(communitiesPresenterMock);
        verify(toolbarViewMock).addAppSearchResultLoadEventHandler(categoriesPresenterMock);
        verify(toolbarViewMock).addAppSearchResultLoadEventHandler(listPresenterMock);
        verify(toolbarViewMock).addAppSearchResultLoadEventHandler(hierarchiesPresenter);
        verify(toolbarViewMock).addBeforeAppSearchEventHandler(listPresenterMock);
        verify(toolbarViewMock).addSwapViewButtonClickedEventHandler(listPresenterMock);
        verify(toolbarViewMock).addRefreshAppsSelectedEventHandler(isA(AppsViewPresenterImpl.class));
        verify(toolbarViewMock).addManageToolsClickedEventHandler(toolbarPresenterMock);

        verify(toolbarPresenterMock, times(16)).getView();


        verifyNoMoreInteractions(viewFactoryMock,
                                 categoriesPresenterMock, listPresenterMock,
                                 toolbarPresenterMock,
                                 categoriesViewMock,
                                 listPresenterMock,
                                 toolbarViewMock);

        final List<String> dirStack = Lists.newArrayList();
        final List<String> output = Lists.newArrayList();
        for(String s : Splitter.on("/").split("/foo/bar/baz")){
            dirStack.add(s);
            output.add(Joiner.on("/").join(dirStack));
        }
        System.out.println(output);
    }



}
