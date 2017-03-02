package org.iplantc.de.apps.client.presenter.categories;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.iplantc.de.apps.client.AppCategoriesView;
import org.iplantc.de.apps.client.events.AppSavedEvent;
import org.iplantc.de.apps.client.events.AppSearchResultLoadEvent;
import org.iplantc.de.apps.client.events.selection.CopyAppSelected;
import org.iplantc.de.apps.client.events.selection.CopyWorkflowSelected;
import org.iplantc.de.apps.client.gin.factory.AppCategoriesViewFactory;
import org.iplantc.de.apps.client.views.details.dialogs.AppDetailsDialog;
import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppCategory;
import org.iplantc.de.client.models.apps.integration.AppTemplate;
import org.iplantc.de.client.services.AppServiceFacade;
import org.iplantc.de.client.services.AppUserServiceFacade;
import org.iplantc.de.client.util.JsonUtil;
import org.iplantc.de.commons.client.widgets.DETabPanel;
import org.iplantc.de.shared.AsyncProviderWrapper;
import org.iplantc.de.shared.DECallback;
import org.iplantc.de.shared.DEProperties;

import com.google.common.collect.Lists;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwtmockito.GxtMockitoTestRunner;

import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.event.StoreAddEvent;
import com.sencha.gxt.data.shared.event.StoreClearEvent;
import com.sencha.gxt.data.shared.event.StoreRemoveEvent;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.TreeSelectionModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author jstroot
 */
@RunWith(GxtMockitoTestRunner.class)
public class AppCategoriesPresenterImplTest {

    @Mock EventBus eventBusMock;
    @Mock JsonUtil jsonUtilMock;
    @Mock DEProperties propsMock;
    @Mock TreeStore<AppCategory> treeStoreMock;
    @Mock AppCategoriesViewFactory viewFactoryMock;
    @Mock AppCategoriesView workspaceViewMock;
    @Mock AppCategoriesView hpcViewMock;
    @Mock AppServiceFacade appServiceMock;
    @Mock AppUserServiceFacade appUserServiceMock;
    @Mock AppCategoriesView.AppCategoriesAppearance appearanceMock;
    @Mock Tree<AppCategory, String> treeMock;
    @Mock TreeSelectionModel<AppCategory> selectionModelMock;
    @Mock DETabPanel tabPanelMock;
    @Mock List<Tree<AppCategory, String>> treesMock;
    @Mock Iterator<Tree<AppCategory, String>> treesIteratorMock;
    @Mock AppCategory categoryMock;
    @Mock AppCategory secondCategoryMock;
    @Mock Tree.TreeNode<AppCategory> treeNodeMock;

    // Event mocks
    @Mock StoreAddEvent<App> mockAddEvent;
    @Mock StoreRemoveEvent<App> mockRemoveEvent;
    @Mock StoreClearEvent<App> mockClearEvent;
    @Mock Store<App> mockStore;

    @Captor ArgumentCaptor<DECallback<List<AppCategory>>> appCategoriesCaptor;
    @Captor ArgumentCaptor<DECallback<App>> appCallbackCaptor;
    @Captor ArgumentCaptor<DECallback<Void>> voidCallbackCaptor;


    @Mock AsyncProviderWrapper<AppDetailsDialog> mockDetailsProvider;
    @Captor ArgumentCaptor<AsyncCallback<AppDetailsDialog>> detailsCallbackCaptor;

    private AppCategoriesPresenterImpl uut;

    @Before public void setUp() {

        when(viewFactoryMock.create(Matchers.<TreeStore<AppCategory>>any(),
                                    any(AppCategoriesView.AppCategoryHierarchyProvider.class))).thenReturn(
                workspaceViewMock).thenReturn(hpcViewMock);
        when(workspaceViewMock.getTree()).thenReturn(treeMock);
        when(hpcViewMock.getTree()).thenReturn(treeMock);
        when(treeMock.getStore()).thenReturn(treeStoreMock);
        when(treeMock.getSelectionModel()).thenReturn(selectionModelMock);
        when(treeStoreMock.getRootItems()).thenReturn(Lists.newArrayList(mock(AppCategory.class),
                                                                         mock(AppCategory.class)));
        when(treeStoreMock.findModelWithKey("id")).thenReturn(categoryMock);
        when(treesMock.size()).thenReturn(2);
        when(treesMock.iterator()).thenReturn(treesIteratorMock);
        when(treesIteratorMock.hasNext()).thenReturn(true, true, false);
        when(treesIteratorMock.next()).thenReturn(treeMock, treeMock);
        when(categoryMock.getId()).thenReturn("id");
        when(treeMock.findNode(categoryMock)).thenReturn(treeNodeMock);
        when(treeMock.findNode(secondCategoryMock)).thenReturn(null);
        when(secondCategoryMock.getId()).thenReturn("id2");

        uut = new AppCategoriesPresenterImpl(propsMock,
                                             jsonUtilMock,
                                             eventBusMock,
                                             viewFactoryMock);
        uut.appUserService = appUserServiceMock;
        uut.appearance = appearanceMock;
        uut.appService = appServiceMock;
        uut.workspaceView = workspaceViewMock;
        uut.hpcView = hpcViewMock;
        uut.trees = treesMock;
        uut.viewTabPanel = tabPanelMock;
    }

    @Test public void testConstructorEventHandlerWiring() {
        verifyConstructor();
        verifyNoMoreInteractions(treeStoreMock, workspaceViewMock,
                                 viewFactoryMock,
                                 eventBusMock);
    }


    /**
     * Ensures that the view is appropriately masked and unmasked when "go(..)" is called.
     */
    @Test public void viewMaskedAndUnmaskedOnGo() {

        // Return empty list
        when(treeStoreMock.getAll()).thenReturn(Collections.<AppCategory>emptyList());
        when(appearanceMock.getAppCategoriesLoadingMask()).thenReturn("mask");
        when(appearanceMock.workspaceTab()).thenReturn("workspace");
        when(appearanceMock.hpcTab()).thenReturn("hpc");

        /*** CALL METHOD UNDER TEST ***/
        uut.go(null, false, tabPanelMock);

        verify(treeMock, times(2)).mask(anyString());
        verify(appServiceMock).getAppCategories(anyBoolean(), appCategoriesCaptor.capture());

        // Call failure with arbitrary exception
        appCategoriesCaptor.getValue().onFailure(500, null);
        verify(tabPanelMock).unmask();

        appCategoriesCaptor.getValue().onSuccess(Collections.<AppCategory>emptyList());
        verify(treeStoreMock).addSortInfo(Matchers.<Store.StoreSortInfo<AppCategory>>any());
        verify(treeMock, times(2)).unmask(); // At this point, it has been called 2 times
    }

    @Test public void testGetSelectedAppCategory() {
        /*** CALL METHOD UNDER TEST ***/
        uut.getSelectedAppCategory();

        verify(selectionModelMock, times(2)).getSelectedItem();
        verifyNoMoreInteractions(selectionModelMock);
    }

    @Test public void treeSelectionsClearedAndRegexStored_onAppSearchResultLoad() {
        uut.searchRegexPattern = "initial regex pattern";
        final String TEST_SEARCH_PATTERN = "test pattern";

        AppSearchResultLoadEvent eventMock = mock(AppSearchResultLoadEvent.class);
        when(eventMock.getSearchPattern()).thenReturn(TEST_SEARCH_PATTERN);


        /*** CALL METHOD UNDER TEST ***/
        uut.onAppSearchResultLoad(eventMock);

        verify(eventMock).getSearchPattern();
        verify(selectionModelMock, times(2)).deselectAll();
    }

    @Test public void userAppsCategoryReselected_onAppUpdated() {
        uut.USER_APPS_GROUP = "user apps group";
        AppCategory userGroupMock = mock(AppCategory.class);
        when(userGroupMock.getName()).thenReturn(uut.USER_APPS_GROUP);
        when(treeStoreMock.getAll()).thenReturn(Lists.newArrayList(userGroupMock));

        // Setup event
        AppSavedEvent eventMock = mock(AppSavedEvent.class);

        /*** CALL METHOD UNDER TEST ***/
        uut.onAppSaved(eventMock);

        verify(selectionModelMock).deselectAll();
        verify(selectionModelMock).select(eq(userGroupMock), eq(false));
    }

    @Test public void testOnCopyAppSelected() {
        /*** CALL METHOD UNDER TEST ***/
        CopyAppSelected eventMock = mock(CopyAppSelected.class);
        final App mockApp = mock(App.class);
        when(mockApp.getId()).thenReturn("mockAppId");
        when(eventMock.getApps()).thenReturn(Lists.newArrayList(mockApp));
        uut.onCopyAppSelected(eventMock);

        verify(appUserServiceMock).copyApp(eq(mockApp), Matchers.<DECallback<AppTemplate>>any());
    }

    @Test public void testOnCopyWorkflowSelected() {
        /*** CALL METHOD UNDER TEST ***/
        CopyWorkflowSelected eventMock = mock(CopyWorkflowSelected.class);
        final App mockApp = mock(App.class);
        when(mockApp.getId()).thenReturn("mockAppId");
        when(eventMock.getApps()).thenReturn(Lists.newArrayList(mockApp));
        uut.onCopyWorkflowSelected(eventMock);

        verify(appUserServiceMock).copyWorkflow(eq(mockApp.getId()), Matchers.<DECallback<String>>any());
    }

    @Test
    public void testSelectDesiredCategory_selectDefault() {
        AppCategoriesPresenterImpl spy = spy(uut);

        /*** CALL METHOD UNDER TEST ***/
        spy.selectDesiredCategory(null, true);
        verify(spy).selectDefaultCategory();
    }

    @Test
    public void testSelectDesiredCategory_noCategoryNoDefault() {

        /*** CALL METHOD UNDER TEST ***/
        uut.selectDesiredCategory(null, false);
        verifyZeroInteractions(treesMock);
    }

    @Test
    public void testSelectDesiredCategory_selectCategory() {
        AppCategoriesPresenterImpl spy = spy(uut);

        /*** CALL METHOD UNDER TEST ***/
        spy.selectDesiredCategory(categoryMock, false);
        verify(treeStoreMock).findModelWithKey(eq("id"));
        verify(spy).doSelectCategory(treeMock, categoryMock);
        verify(spy, times(0)).selectDefaultCategory();
    }

    @Test
    public void testSelectDesiredCategory_selectCategoryNotFound() {
        AppCategoriesPresenterImpl spy = spy(uut);

        /*** CALL METHOD UNDER TEST ***/
        spy.selectDesiredCategory(secondCategoryMock, false);
        verify(treeStoreMock, times(2)).findModelWithKey(eq("id2"));
        verify(spy, times(2)).doSelectCategory(treeMock, null);
        verify(spy).selectDefaultCategory();
    }

    @Test
    public void testSelectDefaultCategory() {

        /*** CALL METHOD UNDER TEST ***/
        uut.selectDefaultCategory();
        verify(tabPanelMock).setActiveWidget(treeMock);
        verify(selectionModelMock).select(isA(AppCategory.class), eq(true));
    }


    private void verifyConstructor() {
        verify(viewFactoryMock, times(2)).create(Matchers.<TreeStore<AppCategory>> any(), eq(uut));
        verify(workspaceViewMock).getTree();
        verify(hpcViewMock).getTree();
        verify(workspaceViewMock).addAppCategorySelectedEventHandler(eq(uut));
        verify(hpcViewMock).addAppCategorySelectedEventHandler(eq(uut));
        verify(eventBusMock, times(2)).addHandler(Matchers.<GwtEvent.Type<AppCategoriesPresenterImpl>>any(), eq(uut));
    }

}
