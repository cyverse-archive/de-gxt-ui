package org.iplantc.de.apps.integration.client.presenter;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.iplantc.de.apps.integration.client.events.DeleteArgumentGroupEvent;
import org.iplantc.de.apps.integration.client.model.ArgumentProperties;
import org.iplantc.de.apps.integration.client.view.AppEditorToolbar;
import org.iplantc.de.apps.integration.client.view.AppsEditorView;
import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.apps.integration.AppTemplate;
import org.iplantc.de.client.models.apps.integration.ArgumentGroup;
import org.iplantc.de.client.services.AppTemplateServices;
import org.iplantc.de.client.services.UUIDServiceAsync;
import org.iplantc.de.client.util.AppTemplateUtils;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;

import com.google.common.collect.Lists;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwtmockito.GxtMockitoTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

/**
 * FIXME Update test to verify editor dirty state.
 * This will require getting rid of the AppTemplateUtils static class. Need to be able to mock it out.
 */
@RunWith(GxtMockitoTestRunner.class)
public class AppsEditorPresenterImplTest {

    @Mock private AppsEditorView mockView;
    @Mock private EventBus mockEventBus;
    @Mock private AppTemplateServices mockAppTemplateService;
    @Mock private UUIDServiceAsync mockUuidService;
    @Mock private AppsEditorView.AppsEditorViewAppearance mockAppearance;
    @Mock private IplantAnnouncer mockAnnouncer;
    @Mock AppEditorToolbar toolbarMock;
    @Mock AppTemplateUtils mockAppTemplateUtils;
    @Mock ArgumentProperties propertiesMock;

    @Mock private AsyncCallback<Void> mockVoidCallback;

    private AppsEditorPresenterImpl uut;

    @Before public void setUp() {
        when(mockView.getToolbar()).thenReturn(toolbarMock);

        uut = new AppsEditorPresenterImpl(mockView,
                                          mockEventBus,
                                          mockAppTemplateService,
                                          mockUuidService,
                                          mockAppearance,
                                          mockAnnouncer,
                                          mockAppTemplateUtils,
                                          propertiesMock);
    }

    @Test public void testDoArgumentGroupDelete() {
        AppTemplate mockAppTemplate = mock(AppTemplate.class);
        SafeHtml safeHtmlMock = mock(SafeHtml.class);
        when(mockView.flush()).thenReturn(mockAppTemplate);
        when(mockAppearance.cannotDeleteLastArgumentGroup()).thenReturn(safeHtmlMock);
        when(mockAppTemplate.getArgumentGroups()).thenReturn(Lists.newArrayList(mock(ArgumentGroup.class)));

        DeleteArgumentGroupEvent mockEvent = mock(DeleteArgumentGroupEvent.class);
        uut.doArgumentGroupDelete(mockEvent);

        verify(mockAnnouncer).schedule(any(ErrorAnnouncementConfig.class));
        verify(mockAppearance).cannotDeleteLastArgumentGroup();

        verifyZeroInteractions(mockEventBus, mockAppTemplateService, mockUuidService, mockAppearance);
        verifyNoMoreInteractions(mockAnnouncer);
    }
    

}
