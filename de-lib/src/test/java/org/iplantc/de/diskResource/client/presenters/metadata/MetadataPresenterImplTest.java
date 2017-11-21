package org.iplantc.de.diskResource.client.presenters.metadata;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.models.avu.Avu;
import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.diskResources.DiskResourceMetadataList;
import org.iplantc.de.client.models.diskResources.MetadataTemplateAttribute;
import org.iplantc.de.client.models.diskResources.MetadataTemplateInfo;
import org.iplantc.de.client.models.ontologies.OntologyAutoBeanFactory;
import org.iplantc.de.client.services.DiskResourceServiceFacade;
import org.iplantc.de.diskResource.client.MetadataView;
import org.iplantc.de.diskResource.client.events.selection.SelectTemplateBtnSelected;
import org.iplantc.de.diskResource.client.presenters.callbacks.DiskResourceMetadataUpdateCallback;
import org.iplantc.de.diskResource.client.presenters.metadata.proxy.AstroThesaurusProxy;
import org.iplantc.de.diskResource.client.presenters.metadata.proxy.OntologyLookupServiceProxy;
import org.iplantc.de.diskResource.client.views.metadata.dialogs.MetadataTemplateViewDialog;
import org.iplantc.de.diskResource.client.views.metadata.dialogs.SelectMetadataTemplateDialog;
import org.iplantc.de.diskResource.client.views.search.MetadataTermSearchField;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwtmockito.GwtMockitoTestRunner;

import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sriram on 6/13/16.
 */
@RunWith(GwtMockitoTestRunner.class)
public class MetadataPresenterImplTest {

    @Mock MetadataView view;
    @Mock DiskResourceServiceFacade drService;
    @Mock List<Avu> userMdList;
    @Mock OntologyAutoBeanFactory autoBeanFactory;
    @Mock OntologyLookupServiceProxy olsSearchProxy;
    @Mock AstroThesaurusProxy uatSearchProxy;
    @Mock MetadataTermSearchField.MetadataTermSearchFieldAppearance searchFieldAppearanceMock;
    @Mock MetadataView.Presenter.Appearance appearanceMock;
    @Mock List<MetadataTemplateInfo> templatesMock;
    @Mock List<MetadataTemplateAttribute> templateAttributesMock;
    @Mock AsyncProviderWrapper<MetadataTemplateViewDialog> templateViewDlgProviderMock;
    @Mock MetadataTemplateViewDialog templateViewDlgMock;
    @Mock AsyncProviderWrapper<SelectMetadataTemplateDialog> selectMetaTemplateDlgProviderMock;
    @Mock SelectMetadataTemplateDialog selectMetaTemplateDlgMock;
    @Mock DiskResource diskResourceMock;
    @Mock DialogHideEvent hideEventMock;

    @Captor ArgumentCaptor<AsyncCallback<MetadataTemplateViewDialog>> templateViewDlgCaptor;
    @Captor ArgumentCaptor<AsyncCallback<SelectMetadataTemplateDialog>> selectMetaTemplateDlgCaptor;
    @Captor ArgumentCaptor<AsyncCallback<List<MetadataTemplateInfo>>> templateInfosCaptor;
    @Captor ArgumentCaptor<AsyncCallback<String>> stringCallbackCaptor;
    @Captor ArgumentCaptor<DialogHideEvent.DialogHideHandler> hideHandlerCaptor;

    private MetadataPresenterImpl uut;
    private MetadataPresenterImpl spy;

    @Before
    public void setUp() {
        uut = new MetadataPresenterImpl(view,
                                              drService);
        uut.templateViewDialogProvider = templateViewDlgProviderMock;
        uut.selectMetaTemplateDlgProvider = selectMetaTemplateDlgProviderMock;
        uut.appearance = appearanceMock;

        spy = spy(uut);
    }

    @Test
    public void testIsDirtyOnEmpty(){
        List<Avu> userMetadata = new ArrayList<>();
        when(view.getUserMetadata()).thenReturn(userMetadata);
        when(userMdList.size()).thenReturn(0);
        when(view.isDirty()).thenReturn(false);
        Assert.assertEquals(false, uut.isDirty());
    }

    @Test
    public void testIsDirtyNonEmpty() {
        List<Avu> userMetadata = new ArrayList<>();
        Avu md1 = mock(Avu.class);
        Avu md2 = mock(Avu.class);
        userMetadata.add(md1);
        userMetadata.add(md2);
        when(view.getUserMetadata()).thenReturn(userMetadata);
        when(userMdList.size()).thenReturn(0);
        when(view.isDirty()).thenReturn(false);
        Assert.assertEquals(false, uut.isDirty());
  }

    @Test
    public void testIsDirtyEdited() {
        List<Avu> userMetadata = new ArrayList<>();
        Avu md1 = mock(Avu.class);
        Avu md2 = mock(Avu.class);
        userMetadata.add(md1);
        userMetadata.add(md2);
        when(view.getUserMetadata()).thenReturn(userMetadata);
        when(userMdList.size()).thenReturn(1);
        when(view.isDirty()).thenReturn(true);
        Assert.assertEquals(true, uut.isDirty());
    }

    @Test
    public void testOnImportNull() {
        view.addToUserMetadata(null);
        Assert.assertEquals(view.getUserMetadata().size(), 0);
    }

    @Test
    public void testOnImportFromIrodsAvu() {
        List<Avu> irodsAvu = new ArrayList<>();
        Avu md1 = mock(Avu.class);
        Avu md2 = mock(Avu.class);
        irodsAvu.add(md1);
        irodsAvu.add(md2);
        view.addToUserMetadata(irodsAvu);
        when(view.getUserMetadata()).thenReturn(irodsAvu);
        Assert.assertEquals(view.getUserMetadata().size(),2);
    }

    @Test
    public void testGo() {
        HasOneWidget containerMock = mock(HasOneWidget.class);

        /** CALL METHOD UNDER TEST **/
        spy.go(containerMock, diskResourceMock);

        verify(drService).getMetadataTemplateListing(templateInfosCaptor.capture());
        templateInfosCaptor.getValue().onSuccess(templatesMock);
        verify(spy).loadMetadata();
    }

    @Ignore
    @Test
    public void testSetDiskResourceMetadata() {
        DiskResourceMetadataUpdateCallback callbackMock = mock(DiskResourceMetadataUpdateCallback.class);
        DiskResourceMetadataList umdMock = mock(DiskResourceMetadataList.class);

        /** CALL METHOD UNDER TEST **/
        uut.setDiskResourceMetadata(callbackMock);

        verify(drService).setDiskResourceMetaData(eq(diskResourceMock),
                                                  eq(umdMock),
                                                  stringCallbackCaptor.capture());

        stringCallbackCaptor.getValue().onSuccess("result");
        verify(callbackMock).onSuccess(eq("result"));
    }

    @Test
    public void testOnSelectTemplate() {
        SelectTemplateBtnSelected eventMock = mock(SelectTemplateBtnSelected.class);
        MetadataTemplateInfo templateInfoMock = mock(MetadataTemplateInfo.class);
        when(templateInfoMock.getId()).thenReturn("id");
        when(hideEventMock.getHideButton()).thenReturn(Dialog.PredefinedButton.OK);
        when(selectMetaTemplateDlgMock.getSelectedTemplate()).thenReturn(templateInfoMock);

        /** CALL METHOD UNDER TEST **/
        spy.onSelectTemplateBtnSelected(eventMock);
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
