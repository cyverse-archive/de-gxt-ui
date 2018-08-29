package org.iplantc.de.diskResource.client.presenters.metadata;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.diskResources.DiskResourceAutoBeanFactory;
import org.iplantc.de.client.models.diskResources.DiskResourceMetadataList;
import org.iplantc.de.client.models.diskResources.MetadataTemplateInfo;
import org.iplantc.de.client.services.DiskResourceServiceFacade;
import org.iplantc.de.client.services.callbacks.ReactErrorCallback;
import org.iplantc.de.client.services.callbacks.ReactSuccessCallback;
import org.iplantc.de.diskResource.client.MetadataView;
import org.iplantc.de.diskResource.client.views.metadata.dialogs.SelectMetadataTemplateDialog;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwtmockito.GwtMockitoTestRunner;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.List;

/**
 * Created by sriram on 6/13/16.
 */
@RunWith(GwtMockitoTestRunner.class)
public class MetadataPresenterImplTest {

    @Mock MetadataView view;
    @Mock DiskResourceServiceFacade drService;
    @Mock DiskResourceAutoBeanFactory diskResourceFactoryMock;
    @Mock MetadataView.Presenter.Appearance appearanceMock;
    @Mock List<MetadataTemplateInfo> templatesMock;
    @Mock AsyncProviderWrapper<SelectMetadataTemplateDialog> selectMetaTemplateDlgProviderMock;
    @Mock SelectMetadataTemplateDialog selectMetaTemplateDlgMock;
    @Mock DiskResource diskResourceMock;
    @Mock DialogHideEvent hideEventMock;

    @Captor ArgumentCaptor<AsyncCallback<SelectMetadataTemplateDialog>> selectMetaTemplateDlgCaptor;
    @Captor ArgumentCaptor<AsyncCallback<List<MetadataTemplateInfo>>> templateInfosCaptor;
    @Captor ArgumentCaptor<AsyncCallback<String>> stringCallbackCaptor;
    @Captor ArgumentCaptor<DialogHideEvent.DialogHideHandler> hideHandlerCaptor;

    private MetadataPresenterImpl uut;
    private MetadataPresenterImpl spy;

    @Before
    public void setUp() {
        final DiskResourceMetadataList metadataMock = mock(DiskResourceMetadataList.class);

        uut = new MetadataPresenterImpl(view, drService) {
            @Override
            DiskResourceMetadataList decodeMetadata(Splittable formValues) {
                return metadataMock;
            }
        };
        uut.selectMetaTemplateDlgProvider = selectMetaTemplateDlgProviderMock;
        uut.autoBeanFactory = diskResourceFactoryMock;
        uut.appearance = appearanceMock;

        spy = spy(uut);
    }

    @Test
    public void testGo() {
        /** CALL METHOD UNDER TEST **/
        spy.go(diskResourceMock);

        verify(drService).getMetadataTemplateListing(templateInfosCaptor.capture());
        templateInfosCaptor.getValue().onSuccess(templatesMock);
        verify(spy).loadMetadata();
    }

    @Ignore
    @Test
    public void testSetDiskResourceMetadata() {
        ReactSuccessCallback callbackMock = mock(ReactSuccessCallback.class);
        ReactErrorCallback errCallbackMock = mock(ReactErrorCallback.class);
        DiskResourceMetadataList umdMock = mock(DiskResourceMetadataList.class);

        /** CALL METHOD UNDER TEST **/
        final Splittable metadata = mock(Splittable.class);
        uut.setDiskResourceMetadata(metadata, callbackMock, errCallbackMock);

        verify(drService).setDiskResourceMetaData(eq(diskResourceMock),
                                                  eq(umdMock),
                                                  stringCallbackCaptor.capture());

        stringCallbackCaptor.getValue().onSuccess("result");
        verify(callbackMock).onSuccess(metadata);
    }

    @Test
    public void testOnSelectTemplate() {
        MetadataTemplateInfo templateInfoMock = mock(MetadataTemplateInfo.class);
        when(templateInfoMock.getId()).thenReturn("id");
        when(hideEventMock.getHideButton()).thenReturn(Dialog.PredefinedButton.OK);
        when(selectMetaTemplateDlgMock.getSelectedTemplate()).thenReturn(templateInfoMock);

        /** CALL METHOD UNDER TEST **/
        final Splittable metadata = mock(Splittable.class);
        spy.onSelectTemplateBtnSelected(metadata);
        spy.templates = templatesMock;

        verify(selectMetaTemplateDlgProviderMock).get(selectMetaTemplateDlgCaptor.capture());

        selectMetaTemplateDlgCaptor.getValue().onSuccess(selectMetaTemplateDlgMock);
        verify(selectMetaTemplateDlgMock).addDialogHideHandler(hideHandlerCaptor.capture());
        hideHandlerCaptor.getValue().onDialogHide(hideEventMock);
        verify(spy).onTemplateSelected(eq("id"));

        verify(selectMetaTemplateDlgMock).show(eq(templatesMock),
                                               eq(true));
    }
}
