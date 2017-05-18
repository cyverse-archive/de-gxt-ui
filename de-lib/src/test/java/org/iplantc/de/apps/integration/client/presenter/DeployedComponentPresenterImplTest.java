package org.iplantc.de.apps.integration.client.presenter;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.models.tool.Tool;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwtmockito.GwtMockitoTestRunner;

import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfigBean;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

/**
 * @author aramsey
 */
@RunWith(GwtMockitoTestRunner.class)
public class DeployedComponentPresenterImplTest {

    @Mock DeployedComponentListingViewFactory viewFactoryMock;
    @Mock ToolSearchRPCProxy searchProxyMock;
    @Mock PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Tool>> loaderMock;
    @Mock DeployedComponentsListingView viewMock;
    @Mock ListStore<Tool> listStoreMock;

    DeployedComponentPresenterImpl uut;

    @Before
    public void setUp() {
        when(viewFactoryMock.createDcListingView(eq(listStoreMock), eq(loaderMock))).thenReturn(viewMock);

        uut = new DeployedComponentPresenterImpl(viewFactoryMock) {
            @Override
            PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Tool>> buildLoader() {
                return loaderMock;
            }

            @Override
            ListStore<Tool> getToolListStore() {
                return listStoreMock;
            }

            @Override
            ToolSearchRPCProxy getToolSearchRPCProxy() {
                return searchProxyMock;
            }
        };

        verifyConstructor();
    }

    private void verifyConstructor() {
        verify(viewFactoryMock).createDcListingView(eq(listStoreMock), eq(loaderMock));
        verify(loaderMock).load(isA(FilterPagingLoadConfigBean.class));
    }

    @Test
    public void go() {
        Widget viewWidgetMock = mock(Widget.class);
        HasOneWidget containerMock = mock(HasOneWidget.class);
        when(viewMock.asWidget()).thenReturn(viewWidgetMock);

        /** CALL METHOD UNDER TEST **/
        uut.go(containerMock);
        verify(containerMock).setWidget(eq(viewWidgetMock));
    }

}
