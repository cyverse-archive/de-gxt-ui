package org.iplantc.de.admin.apps.client.presenter.grid;

import com.google.common.collect.Lists;
import com.google.gwtmockito.GwtMockitoTestRunner;
import org.iplantc.de.admin.apps.client.AdminAppStatsGridView;
import org.iplantc.de.admin.desktop.client.services.AppAdminServiceFacade;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.proxy.AppListLoadResult;
import org.iplantc.de.shared.DECallback;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static org.mockito.Mockito.*;


/**
 * Created by sriram on 11/1/16.
 */
@RunWith(GwtMockitoTestRunner.class)
public class AdminAppsStatsGridPresenterImplTest {

    @Mock AdminAppStatsGridView mockView;
    @Mock AppAdminServiceFacade mockAppService;

    @Captor
    ArgumentCaptor<DECallback<AppListLoadResult>> appListCallbackCaptor;

    private AdminAppsStatsGridPresenterImpl uut;

    @Before public void setUp() {
       uut = new AdminAppsStatsGridPresenterImpl(mockView);
       uut.appService = mockAppService;
    }

    @Test public void searchAppsTest() {
        uut.searchApps("","","",null,null);
        verify(mockAppService).searchApp(eq(""),eq(""),eq(""), appListCallbackCaptor.capture());

        AppListLoadResult result = mock(AppListLoadResult.class);
        when(result.getData()).thenReturn(Lists.newArrayList(mock(App.class)));

        appListCallbackCaptor.getValue().onSuccess(result);
    }

}
