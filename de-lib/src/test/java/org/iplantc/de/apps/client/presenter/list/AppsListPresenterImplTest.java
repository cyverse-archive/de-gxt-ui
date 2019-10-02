package org.iplantc.de.apps.client.presenter.list;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.iplantc.de.apps.client.AppsListView;
import org.iplantc.de.apps.client.events.AppFavoritedEvent;
import org.iplantc.de.apps.client.events.AppSearchResultLoadEvent;
import org.iplantc.de.apps.client.events.AppUpdatedEvent;
import org.iplantc.de.apps.client.events.RunAppEvent;
import org.iplantc.de.apps.client.events.SwapViewButtonClickedEvent;
import org.iplantc.de.apps.client.events.selection.AppCategorySelectionChangedEvent;
import org.iplantc.de.apps.client.events.selection.AppCommentSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppFavoriteSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppNameSelectedEvent;
import org.iplantc.de.apps.client.events.selection.AppRatingDeselected;
import org.iplantc.de.apps.client.events.selection.DeleteAppsSelected;
import org.iplantc.de.apps.client.events.selection.RunAppSelected;
import org.iplantc.de.apps.client.presenter.callbacks.DeleteRatingCallback;
import org.iplantc.de.apps.client.presenter.callbacks.RateAppCallback;
import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.AppCategory;
import org.iplantc.de.client.models.apps.proxy.AppListLoadResult;
import org.iplantc.de.client.services.AppMetadataServiceFacade;
import org.iplantc.de.client.services.AppUserServiceFacade;
import org.iplantc.de.client.services.OntologyServiceFacade;
import org.iplantc.de.commons.client.comments.view.dialogs.CommentsDialog;
import org.iplantc.de.shared.AsyncProviderWrapper;
import org.iplantc.de.shared.DECallback;
import org.iplantc.de.shared.DEProperties;

import com.google.common.collect.Lists;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwtmockito.GwtMockitoTestRunner;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

import com.sencha.gxt.widget.core.client.container.CardLayoutContainer;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;

import java.util.List;


/**
 * @author jstroot
 */
@RunWith(GwtMockitoTestRunner.class)
public class AppsListPresenterImplTest {


    @Mock
    AppsListView listView;
    @Mock
    AppUserServiceFacade appServiceMock;
    @Mock
    AppsListView.AppsListAppearance appearanceMock;
    @Mock
    UserInfo userInfoMock;
    @Mock
    AppUserServiceFacade appUserServiceMock;
    @Mock
    OntologyServiceFacade ontologyServiceMock;
    @Mock
    CardLayoutContainer cardsMock;
    @Mock
    DEProperties dePropertiesMock;

    @Mock
    AsyncProviderWrapper<CommentsDialog> commentsProviderMock;
    @Captor
    ArgumentCaptor<AsyncCallback<CommentsDialog>> commentsDlgCaptor;

    @Captor
    ArgumentCaptor<DECallback<String>> stringCallbackCaptor;
    @Captor
    ArgumentCaptor<DECallback<Void>> voidCallbackCaptor;
    @Captor
    ArgumentCaptor<DECallback<Splittable>> appListCallbackCaptor;
    @Mock
    EventBus eventBusMock;


    private AppsListPresenterImpl uut;

    @Before
    public void setUp() {
        when(appearanceMock.appLoadError()).thenReturn("error");
        uut = new AppsListPresenterImpl(eventBusMock, ontologyServiceMock, dePropertiesMock, listView) {
            @Override
            void postToErrorHandler(Throwable caught) {
                return;
            }

            @Override
            App splittableToApp(Splittable app) {
                App appMock = mock(App.class);
                return appMock;
            }
        };
        uut.appService = appServiceMock;
        uut.appUserService = appUserServiceMock;
        uut.appearance = appearanceMock;
        uut.commentsDialogProvider = commentsProviderMock;
        uut.userInfo = userInfoMock;
    }

    @Test
    @Ignore
    public void verifyAppServiceCalled_onAppCategorySelected() {

        AppCategorySelectionChangedEvent eventMock = mock(AppCategorySelectionChangedEvent.class);
        final AppCategory appCategoryMock = mock(AppCategory.class);
        when(appCategoryMock.getId()).thenReturn("mock category id");
        List<AppCategory> selection = Lists.newArrayList(appCategoryMock);
        when(eventMock.getAppCategorySelection()).thenReturn(selection);

        when(appearanceMock.getAppsLoadingMask()).thenReturn("loading mask");

        /*** CALL METHOD UNDER TEST ***/
        uut.onAppCategorySelectionChanged(eventMock);

        verify(listView).setLoadingMask(true);
        verify(appServiceMock).getAppsAsSplittable(eq(appCategoryMock),
                                                   eq(null),
                                                   appListCallbackCaptor.capture());

        Splittable resultList = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(mock(App.class)));

        /*** CALL METHOD UNDER TEST ***/
        appListCallbackCaptor.getValue().onSuccess(resultList);
        verify(listView).setApps(eq(resultList), false);
        verifyNoMoreInteractions(appServiceMock, appearanceMock);
    }

    @Test
    public void verifyAppServiceCalled_onAppCategorySelected_Fail() {
        Throwable throwableMock = mock(Throwable.class);
        AppCategorySelectionChangedEvent eventMock = mock(AppCategorySelectionChangedEvent.class);
        final AppCategory appCategoryMock = mock(AppCategory.class);
        when(appCategoryMock.getId()).thenReturn("mock category id");
        List<AppCategory> selection = Lists.newArrayList(appCategoryMock);
        when(eventMock.getAppCategorySelection()).thenReturn(selection);

        when(appearanceMock.getAppsLoadingMask()).thenReturn("loading mask");

        /*** CALL METHOD UNDER TEST ***/
        uut.onAppCategorySelectionChanged(eventMock);

        verify(listView).setLoadingMask(true);
        verify(appServiceMock).getAppsAsSplittable(eq(appCategoryMock),
                                                   eq(null),
                                                   appListCallbackCaptor.capture());

        List<App> resultList = Lists.newArrayList(mock(App.class));

        /*** CALL METHOD UNDER TEST ***/
        appListCallbackCaptor.getValue().onFailure(500, throwableMock);

        verify(listView).setApps(eq(null), eq(false));
        verifyNoMoreInteractions(appServiceMock);
    }

    @Test
    public void doNothingIfSelectionIsEmpty_onAppCategorySelected() {

        AppCategorySelectionChangedEvent eventMock = mock(AppCategorySelectionChangedEvent.class);
        List<AppCategory> selection = Lists.newArrayList();
        when(eventMock.getAppCategorySelection()).thenReturn(selection);

        /*** CALL METHOD UNDER TEST ***/
        uut.onAppCategorySelectionChanged(eventMock);

        verifyZeroInteractions(appearanceMock, appServiceMock, listView);
    }

    /**
     * Verifies that the dlg provider is called, and the proper dlg show(..) method
     * is called.
     */
    @Test
    @Ignore
    public void commentsDlgShown_onAppCommentSelectedEvent() {
        AppCommentSelectedEvent eventMock = mock(AppCommentSelectedEvent.class);
        App appMock = uut.splittableToApp(StringQuoter.split("{id: \"123-456-789\"}"));
        when(appMock.getIntegratorEmail()).thenReturn("foo@bar.com");
        when(userInfoMock.getEmail()).thenReturn("baz@foo.com");

        /*** CALL METHOD UNDER TEST ***/
        uut.onAppCommentSelected(StringQuoter.split("{id: \"123-456-789\"}"));
       verify(commentsProviderMock).get(commentsDlgCaptor.capture());

        /*** CALL METHOD UNDER TEST ***/
        CommentsDialog dlgMock = mock(CommentsDialog.class);
        commentsDlgCaptor.getValue().onSuccess(dlgMock);

        verify(dlgMock).show(eq(appMock),
                             eq(false),
                             any(AppMetadataServiceFacade.class));
    }

    /**
     * Verify that the service is called, and appropriate actions are taken
     * on success.
     */
    @Test
    @Ignore
    public void verifyAppServiceCalled_onAppFavoriteSelected() {
        // Book-keeping for constructor
        verify(eventBusMock, atLeast(1)).addHandler(Matchers.<GwtEvent.Type<AppsListPresenterImpl>>any(),
                                                    eq(uut));
        AppFavoriteSelectedEvent eventMock = mock(AppFavoriteSelectedEvent.class);
        App appMock = mock(App.class);
        final String mockId = "mock id";
        when(appMock.getId()).thenReturn(mockId);
        when(appMock.isFavorite()).thenReturn(true);
        when(eventMock.getApp()).thenReturn(appMock);


        when(userInfoMock.getWorkspaceId()).thenReturn("workspace id");

        /*** CALL METHOD UNDER TEST ***/
        uut.onAppFavoriteSelected(AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(appMock)));

        verify(appUserServiceMock).favoriteApp(eq(appMock), eq(false), voidCallbackCaptor.capture());

        /*** CALL METHOD UNDER TEST ***/
        voidCallbackCaptor.getValue().onSuccess(null);
        verify(appMock).setFavorite(false);
        verify(eventBusMock, times(2)).fireEvent(any(AppFavoritedEvent.class));
        verify(eventBusMock, times(2)).fireEvent(any(AppUpdatedEvent.class));

        verifyNoMoreInteractions(appServiceMock, listView);
    }

    @Test
    @Ignore
    public void runAppEventFired_onAppNameSelected() {
        // Book-keeping for constructor
        verify(eventBusMock, atLeast(1)).addHandler(Matchers.<GwtEvent.Type<AppsListPresenterImpl>>any(),
                                                    eq(uut));
        AppNameSelectedEvent eventMock = mock(AppNameSelectedEvent.class);
        App appMock = uut.splittableToApp(StringQuoter.split("{id: \"123-456-789\"}"));
        when(eventMock.getSelectedApp()).thenReturn(appMock);
        when(appMock.isRunnable()).thenReturn(true);
        when(appMock.isDisabled()).thenReturn(false);

        /*** CALL METHOD UNDER TEST ***/
        uut.onAppNameSelected(StringQuoter.split("{id: \"123-456-789\",can_run: true}"));

        verify(eventMock).getSelectedApp();

        verify(eventBusMock).fireEvent(any(RunAppEvent.class));

        verifyNoMoreInteractions(eventBusMock);
        verifyZeroInteractions(appServiceMock);
    }

    @Test
    public void runAppEventFired_onRunAppSelected() {
        // Book-keeping for constructor
        verify(eventBusMock, atLeast(1)).addHandler(Matchers.<GwtEvent.Type<AppsListPresenterImpl>>any(),
                                                    eq(uut));
        RunAppSelected eventMock = mock(RunAppSelected.class);
        App appMock = mock(App.class);
        when(eventMock.getApp()).thenReturn(appMock);
        when(appMock.isRunnable()).thenReturn(true);
        when(appMock.isDisabled()).thenReturn(false);

        /*** CALL METHOD UNDER TEST ***/
        uut.onRunAppSelected(eventMock);

        verify(eventMock).getApp();

        verify(eventBusMock).fireEvent(any(RunAppEvent.class));

        verifyNoMoreInteractions(eventBusMock);
        verifyZeroInteractions(appServiceMock);
    }

    @Test
    public void verifyAppServiceCalled_onAppRatingDeselected() {
        AppRatingDeselected eventMock = mock(AppRatingDeselected.class);
        App appMock = mock(App.class);
        when(eventMock.getApp()).thenReturn(appMock);

        /*** CALL METHOD UNDER TEST ***/
        uut.onAppRatingDeselected(AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(appMock)));

        verify(appUserServiceMock).deleteRating(any(App.class), any(DeleteRatingCallback.class));
        verifyNoMoreInteractions(appServiceMock, appMock);
    }

    @Test
    public void verifyAppServiceCalled_onAppRatingSelected() {
        int mockScore = 3;
        App appMock = uut.splittableToApp(StringQuoter.split("{id: \"123-456-789\"}"));
        /*** CALL METHOD UNDER TEST ***/
        uut.onAppRatingSelected(StringQuoter.split("{id: \"123-456-789\"}"), mockScore);


        verify(appUserServiceMock).rateApp(any(App.class), eq(mockScore),
                                           any(RateAppCallback.class));
        verifyNoMoreInteractions(appServiceMock, appMock);
    }

    @Test
    @Ignore
    public void verifyStoreClearedAndResultsAdded_onAppSearchResultLoad() {
        AppSearchResultLoadEvent eventMock = mock(AppSearchResultLoadEvent.class);
        AppListLoadResult results = mock(AppListLoadResult.class);
        String searchPatternMock = "mock search pattern";
        when(eventMock.getResults()).thenReturn(results);
        when(eventMock.getSearchPattern()).thenReturn(searchPatternMock);

        /*** CALL METHOD UNDER TEST ***/
        uut.onAppSearchResultLoad(eventMock);

        verifyZeroInteractions(appServiceMock, appUserServiceMock);
    }

    @Test
    @Ignore
    public void verifyAppServiceCalled_onDeleteAppsSelected() {
        // Book-keeping for constructor
        verify(eventBusMock, atLeast(1)).addHandler(Matchers.<GwtEvent.Type<AppsListPresenterImpl>>any(),
                                                    eq(uut));
        DeleteAppsSelected eventMock = mock(DeleteAppsSelected.class);
        App mock1 = mock(App.class);
        App mock2 = mock(App.class);
        when(mock1.getId()).thenReturn("mock id 1");
        when(mock2.getId()).thenReturn("mock id 2");
        List<App> appsToBeDeleted = Lists.newArrayList(mock1, mock2);
        when(eventMock.getAppsToBeDeleted()).thenReturn(appsToBeDeleted);

        String mockUserName = "mock user name";
        when(userInfoMock.getUsername()).thenReturn(mockUserName);
        String mockFullUserName = "mock full user name";
        when(userInfoMock.getFullUsername()).thenReturn(mockFullUserName);

        /*** CALL METHOD UNDER TEST ***/
        uut.onDeleteAppsSelected(eventMock);

        verify(eventMock).getAppsToBeDeleted();

        verify(appUserServiceMock).deleteAppsFromWorkspace(eq(appsToBeDeleted),
                                                           Matchers.<DECallback<Void>>any());
        verifyNoMoreInteractions(appServiceMock, eventMock, eventBusMock, userInfoMock, mock1, mock2);
    }

    @Test
    public void testOnSwapViewButtonClicked() {
        SwapViewButtonClickedEvent eventMock = mock(SwapViewButtonClickedEvent.class);
        //TileViewMock is the default start, so switching should go to gridView, then back to tileView

        /*** CALL METHOD UNDER TEST ***/
        uut.onSwapViewButtonClicked(eventMock);
        verify(listView).setViewType(eq(AppsListView.GRID_VIEW));

        uut.onSwapViewButtonClicked(eventMock);
        verify(listView).setViewType(eq(AppsListView.TILE_VIEW));
    }

}
