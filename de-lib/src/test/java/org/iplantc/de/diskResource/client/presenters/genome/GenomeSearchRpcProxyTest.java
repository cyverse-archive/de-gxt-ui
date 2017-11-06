package org.iplantc.de.diskResource.client.presenters.genome;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.models.genomes.Genome;
import org.iplantc.de.client.services.FileEditorServiceFacade;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.diskResource.client.GenomeSearchView;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwtmockito.GwtMockitoTestRunner;

import com.sencha.gxt.data.shared.loader.FilterConfig;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.List;

@RunWith(GwtMockitoTestRunner.class)
public class GenomeSearchRpcProxyTest {

    @Mock GenomeSearchView.GenomeSearchViewAppearance appearanceMock;
    @Mock FileEditorServiceFacade serviceFacadeMock;
    @Mock IplantAnnouncer announcerMock;
    @Mock FilterPagingLoadConfig loadConfigMock;
    @Mock AsyncCallback<PagingLoadResult<Genome>> callbackMock;
    @Mock List<Genome> genomeListMock;
    @Mock List<FilterConfig> filterConfigListMock;
    @Mock PagingLoadResultBean<Genome> loadResultMock;
    @Mock FilterConfig filterConfigMock;

    @Captor ArgumentCaptor<AsyncCallback<List<Genome>>> genomeListCaptor;

    GenomeSearchRpcProxy uut;

    @Before
    public void setUp() {

        uut = new GenomeSearchRpcProxy(appearanceMock,
                                       serviceFacadeMock,
                                       announcerMock) {
            @Override
            PagingLoadResultBean<Genome> getLoadResult(List<Genome> result,
                                                       FilterPagingLoadConfig loadConfig) {
                return loadResultMock;
            }
        };
    }

    @Test
    public void load() {
        when(loadConfigMock.getFilters()).thenReturn(filterConfigListMock);
        when(filterConfigListMock.get(0)).thenReturn(filterConfigMock);
        when(filterConfigMock.getValue()).thenReturn("queryText");

        /** CALL METHOD UNDER TEST **/
        uut.load(loadConfigMock, callbackMock);

        verify(serviceFacadeMock).searchGenomesInCoge(eq("queryText"), genomeListCaptor.capture());

        genomeListCaptor.getValue().onSuccess(genomeListMock);
        verify(callbackMock).onSuccess(eq(loadResultMock));

    }

}
