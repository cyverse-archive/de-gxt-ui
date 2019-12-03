package org.iplantc.de.admin.apps.client.presenter;

import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.iplantc.de.admin.apps.client.AdminAppsGridView;
import org.iplantc.de.admin.apps.client.AdminAppsToolbarView;
import org.iplantc.de.admin.apps.client.AdminCategoriesView;
import org.iplantc.de.admin.apps.client.gin.factory.AdminAppsViewFactory;

import com.google.gwtmockito.GwtMockitoTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;


@RunWith(GwtMockitoTestRunner.class)
public class AdminAppsViewPresenterImplTest {


    @Mock AdminCategoriesView.Presenter categoriesPresenterMock;
    @Mock AdminAppsGridView.Presenter gridPresenterMock;
    @Mock AdminAppsToolbarView.Presenter toolbarPresenterMock;
    @Mock AdminAppsViewFactory viewFactoryMock;
    @Mock AdminCategoriesView categoriesViewMock;
    @Mock AdminAppsGridView gridViewMock;
    @Mock AdminAppsToolbarView toolbarViewMock;

    private AdminAppsViewPresenterImpl uut;

    @Before public void setUp() {
        when(categoriesPresenterMock.getView()).thenReturn(categoriesViewMock);
        when(toolbarPresenterMock.getView()).thenReturn(toolbarViewMock);
        when(gridPresenterMock.getView()).thenReturn(gridViewMock);
        uut = new AdminAppsViewPresenterImpl(viewFactoryMock,
                                             categoriesPresenterMock,
                                             toolbarPresenterMock,
                                             gridPresenterMock);
    }

    @Test public void verifyConstructorEventHandlerWiring() {
        verify(viewFactoryMock).create(eq(categoriesPresenterMock),
                                       eq(toolbarPresenterMock),
                                       eq(gridPresenterMock));


        // Verify categories wiring
        verify(categoriesViewMock).addAppCategorySelectedEventHandler(eq(gridPresenterMock));
        verify(categoriesViewMock).addAppCategorySelectedEventHandler(eq(gridViewMock));
        verify(categoriesViewMock).addAppCategorySelectedEventHandler(eq(toolbarViewMock));

        // Verify Toolbar wiring
        verify(toolbarViewMock).addAddCategorySelectedHandler(eq(categoriesPresenterMock));
        verify(toolbarViewMock).addRenameCategorySelectedHandler(eq(categoriesPresenterMock));
        verify(toolbarViewMock).addDeleteCategorySelectedHandler(eq(categoriesPresenterMock));
        verify(toolbarViewMock).addDeleteAppsSelectedHandler(eq(gridPresenterMock));
        verify(toolbarViewMock).addRestoreAppSelectedHandler(eq(gridPresenterMock));
        verify(toolbarViewMock).addCategorizeAppSelectedHandler(eq(categoriesPresenterMock));
        verify(toolbarViewMock).addMoveCategorySelectedHandler(eq(categoriesPresenterMock));
        verify(toolbarViewMock).addAppSearchResultLoadEventHandler(categoriesPresenterMock);
        verify(toolbarViewMock).addAppSearchResultLoadEventHandler(gridPresenterMock);
        verify(toolbarViewMock).addBeforeAppSearchEventHandler(gridViewMock);

        verify(categoriesPresenterMock, times(3)).getView();
        verify(toolbarPresenterMock, times(12)).getView();
        verify(gridPresenterMock, times(2)).getView();

        verifyNoMoreInteractions(categoriesPresenterMock,
                                 toolbarPresenterMock,
                                 categoriesViewMock,
                                 gridViewMock,
                                 toolbarViewMock);
    }
}