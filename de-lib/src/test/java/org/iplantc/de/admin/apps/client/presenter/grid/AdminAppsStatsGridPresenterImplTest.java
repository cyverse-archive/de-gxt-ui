package org.iplantc.de.admin.apps.client.presenter.grid;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.admin.apps.client.AdminAppStatsGridView;
import org.iplantc.de.admin.desktop.client.services.AppAdminServiceFacade;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.proxy.AppListLoadResult;
import org.iplantc.de.shared.DECallback;

import com.google.common.collect.Lists;
import com.google.gwtmockito.GwtMockitoTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;


/**
 * Created by sriram on 11/1/16.
 */
@RunWith(GwtMockitoTestRunner.class)
public class AdminAppsStatsGridPresenterImplTest {

    @Mock AdminAppStatsGridView mockView;
    @Mock AppAdminServiceFacade mockAppService;
    @Mock AdminAppStatsGridView.Appearance mockApppearance;

    @Captor
    ArgumentCaptor<DECallback<AppListLoadResult>> appListCallbackCaptor;

    private AdminAppsStatsGridPresenterImpl uut;

    @Before public void setUp() {
       uut = new AdminAppsStatsGridPresenterImpl(mockView);
       uut.appService = mockAppService;
       uut.appearance = mockApppearance;
    }

    @Test public void verifyLoad() {
        when(mockApppearance.loading()).thenReturn("loading...");
        uut.load();
        verify(mockView).mask(anyString());
        verify(mockAppService).searchApp(eq(""), appListCallbackCaptor.capture());

        AppListLoadResult result = mock(AppListLoadResult.class);
        when(result.getData()).thenReturn(Lists.newArrayList(mock(App.class)));

        appListCallbackCaptor.getValue().onSuccess(result);

        verify(mockView).clear();
        verify(mockView).addAll(result.getData());
        verify(mockView).unmask();
    }

}
