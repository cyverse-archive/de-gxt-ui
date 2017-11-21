package org.iplantc.de.diskResource.client.presenters.genome;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.models.genomes.Genome;
import org.iplantc.de.client.services.FileEditorServiceFacade;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.diskResource.client.GenomeSearchView;
import org.iplantc.de.diskResource.client.events.selection.ImportGenomeFromCogeSelected;
import org.iplantc.de.diskResource.client.gin.factory.GenomeSearchViewFactory;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwtmockito.GwtMockitoTestRunner;

import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.box.MessageBox;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

@RunWith(GwtMockitoTestRunner.class)
public class GenomeSearchPresenterImplTest {

    @Mock GenomeSearchView.GenomeSearchViewAppearance appearanceMock;
    @Mock GenomeSearchView viewMock;
    @Mock FileEditorServiceFacade serviceFacadeMock;
    @Mock IplantAnnouncer announcerMock;
    @Mock GenomeSearchViewFactory viewFactoryMock;
    @Mock GenomeSearchRpcProxy searchProxyMock;
    @Mock Genome genomeMock;
    @Mock PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Genome>> pagingLoaderMock;
    @Mock MessageBox successMessageBoxMock;
    @Mock ImageResource imageResourceMock;

    @Captor ArgumentCaptor<AsyncCallback<String>> stringCaptor;

    GenomeSearchPresenterImpl uut;

    @Before
    public void setUp() {
        when(viewFactoryMock.create(pagingLoaderMock)).thenReturn(viewMock);

        uut = new GenomeSearchPresenterImpl(appearanceMock,
                                            viewFactoryMock,
                                            serviceFacadeMock,
                                            searchProxyMock) {
            @Override
            PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Genome>> getPagingLoader(GenomeSearchRpcProxy searchProxy) {
                return pagingLoaderMock;
            }

            @Override
            MessageBox getSuccessMessageBox() {
                return successMessageBoxMock;
            }
        };

        testConstructor();
    }

    void testConstructor() {
        verify(viewMock).addImportGenomeFromCogeSelectedHandler(eq(uut));
    }


    @Test
    public void onImportGenomeFromCogeSelected() {
        ImportGenomeFromCogeSelected eventMock = mock(ImportGenomeFromCogeSelected.class);
        when(eventMock.getSelectedGenome()).thenReturn(genomeMock);
        when(genomeMock.getId()).thenReturn(123);
        when(appearanceMock.infoIcon()).thenReturn(imageResourceMock);

        /** CALL METHOD UNDER TEST **/
        uut.onImportGenomeFromCogeSelected(eventMock);

        verify(serviceFacadeMock).importGenomeFromCoge(eq(123), eq(true), stringCaptor.capture());

        stringCaptor.getValue().onSuccess("result");
        verify(successMessageBoxMock).show();
    }

}
