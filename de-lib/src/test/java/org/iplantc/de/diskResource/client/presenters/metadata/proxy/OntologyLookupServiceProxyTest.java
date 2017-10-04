package org.iplantc.de.diskResource.client.presenters.metadata.proxy;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.models.ontologies.MetadataTermSearchResult;
import org.iplantc.de.client.services.OntologyLookupServiceFacade;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwtmockito.GwtMockitoTestRunner;

import com.sencha.gxt.data.shared.loader.PagingLoadResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

@RunWith(GwtMockitoTestRunner.class)
public class OntologyLookupServiceProxyTest {
    @Mock OntologyLookupServiceFacade svcFacadeMock;
    @Mock OntologyLookupServiceLoadConfig loadConfigMock;
    @Mock AsyncCallback<PagingLoadResult<MetadataTermSearchResult>> callbackMock;

    OntologyLookupServiceProxy proxyUnderTest;

    @Before
    public void setUp() {
        proxyUnderTest = new OntologyLookupServiceProxy(svcFacadeMock);
    }

    @Test
    public void loadQuery() {
        when(loadConfigMock.getQuery()).thenReturn("search");

        // Call method under test
        proxyUnderTest.load(loadConfigMock, callbackMock);

        verify(svcFacadeMock).searchOntologyLookupService(eq(loadConfigMock), any());
    }

    @Test
    public void loadNoQuery() {
        // Call method under test
        proxyUnderTest.load(loadConfigMock, callbackMock);

        verifyZeroInteractions(svcFacadeMock);
    }
}
