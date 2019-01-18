package org.iplantc.de.analysis.client.presenter;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.analysis.client.AnalysesView;
import org.iplantc.de.analysis.client.views.dialogs.AnalysisSharingDialog;
import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.AppTypeFilter;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.analysis.AnalysesAutoBeanFactory;
import org.iplantc.de.client.models.analysis.Analysis;
import org.iplantc.de.client.models.analysis.AnalysisExecutionStatus;
import org.iplantc.de.client.models.analysis.AnalysisPermissionFilter;
import org.iplantc.de.client.models.analysis.AnalysisStep;
import org.iplantc.de.client.models.analysis.AnalysisStepsInfo;
import org.iplantc.de.client.models.analysis.support.AnalysisSupportAutoBeanFactory;
import org.iplantc.de.client.models.analysis.support.AnalysisSupportRequest;
import org.iplantc.de.client.models.analysis.support.AnalysisSupportRequestFields;
import org.iplantc.de.client.services.AnalysisServiceFacade;
import org.iplantc.de.client.services.callbacks.ReactErrorCallback;
import org.iplantc.de.client.services.callbacks.ReactSuccessCallback;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;
import org.iplantc.de.commons.client.views.dialogs.IPlantPromptDialog;
import org.iplantc.de.shared.AnalysisCallback;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwtmockito.GwtMockitoTestRunner;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.loader.FilterConfig;
import com.sencha.gxt.data.shared.loader.FilterConfigBean;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author aramsey
 */
@RunWith(GwtMockitoTestRunner.class)
public class AnalysesPresenterImplTest {

    public static final String TESTUSER = "testuser";
    public static final String TEST_CYVERSE_ORG = "test@cyverse.org";
    public static final String TESTUSER_CYVERE_ORG = "testuser@cyvere.org";
    public static final String THIS_IS_A_COMMENT = "This is a comment!";
    public static final String NAME = "name";
    public static final String APP_NAME = "word count";
    public static final String REQUEST_HELP = " requesting help with Analysis";
    public static final String RESULT = "/iplant/home/result";

    @Mock AnalysisServiceFacade analysisServiceMock;
    @Mock IplantAnnouncer announcerMock;
    @Mock AnalysesView.Presenter.Appearance appearanceMock;
    @Mock AsyncProviderWrapper<AnalysisSharingDialog> aSharingDialogProviderMock;
    @Mock AnalysisSharingDialog analysisSharingDialogMock;
    @Mock
    AnalysisPermissionFilter currentPermFilterMock;
    @Mock
    AppTypeFilter currentTypeFilterMock;
    @Mock ListStore<Analysis> listStoreMock;
    @Mock AnalysesView viewMock;
    @Mock HasHandlers eventBusMock;
    @Mock HandlerRegistration handlerFirstLoadMock;
    @Mock PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Analysis>> loaderMock;
    @Mock EventBus eventBus;
    @Mock List<Analysis> analysisListMock;
    @Mock ArrayList<Analysis> analysisArrayMock;
    @Mock Iterator<Analysis> analysisIteratorMock;
    @Mock Throwable throwableMock;
    @Mock FilterPagingLoadConfig loadConfigMock;
    @Mock List<FilterConfig> filterConfigsMock;
    @Mock FilterConfigBean filterConfigBeanMock;
    @Mock AnalysisStepsInfo analysisStepsInfoMock;
    @Mock List<AnalysisStep> stepListMock;
    @Mock UserInfo userInfoMock;
    @Mock AnalysisSupportAutoBeanFactory supportFactoryMock;
    @Mock AutoBean<AnalysisSupportRequest> aSupportRequestBeanMock;
    @Mock AutoBean<AnalysisSupportRequestFields> aSupportRequestFieldsBeanMock;
    @Mock ConfirmMessageBox deleteAnalysisDlg;
    @Mock IPlantPromptDialog renameAnalysisDlg;
    @Mock
    AnalysesAutoBeanFactory analysesAutoBeanFactory;
    @Mock
    Analysis analysisMock;

    @Mock
    ReactSuccessCallback reactSuccessCallbackMock;
    @Mock
    ReactErrorCallback reactErrorCallbackMock;

    @Captor ArgumentCaptor<AnalysisCallback<String>> stringCallbackCaptor;
    @Captor ArgumentCaptor<AnalysisCallback<Void>> voidCallbackCaptor;
    @Captor ArgumentCaptor<AnalysisCallback<AnalysisStepsInfo>> analysisStepsCaptor;
    @Captor ArgumentCaptor<DialogHideEvent.DialogHideHandler> dialogHideCaptor;
    @Captor ArgumentCaptor<SelectEvent.SelectHandler> okSelectCaptor;


    private AnalysesPresenterImpl uut;


    @Before
    public void setUp() {
        when(analysisListMock.size()).thenReturn(1);
        when(analysisListMock.iterator()).thenReturn(analysisIteratorMock);
        when(analysisIteratorMock.hasNext()).thenReturn(true, false);
        when(analysisIteratorMock.next()).thenReturn(analysisMock).thenReturn(null);

        when(analysisMock.getId()).thenReturn("id");
        when(analysisMock.getName()).thenReturn(NAME);
        when(analysisMock.getAppName()).thenReturn(APP_NAME);
        when(analysisMock.getResultFolderId()).thenReturn(RESULT);
        when(analysisMock.getStartDate()).thenReturn(new Date().getTime());
        when(analysisMock.getEndDate()).thenReturn(new Date().getTime());
        when(analysisMock.getStatus()).thenReturn(AnalysisExecutionStatus.COMPLETED.toString());


        when(appearanceMock.deleteAnalysisError()).thenReturn("error");
        when(loaderMock.getLastLoadConfig()).thenReturn(loadConfigMock);
        when(loadConfigMock.getFilters()).thenReturn(filterConfigsMock);


        uut = new AnalysesPresenterImpl(eventBus);

        uut.currentPermFilter = currentPermFilterMock;
        uut.currentTypeFilter = currentTypeFilterMock;
        uut.analysisService = analysisServiceMock;
        uut.announcer = announcerMock;
        uut.appearance = appearanceMock;
        uut.aSharingDialogProvider = aSharingDialogProviderMock;
        uut.userInfo = userInfoMock;
        uut.supportFactory = supportFactoryMock;
        uut.view = viewMock;

    }

    @Test
    public void cancelSelectedAnalyses() {
        AnalysesPresenterImpl spy = spy(uut);
        spy.onCancelAnalysisSelected("test_id",
                                     "test_name",
                                     reactSuccessCallbackMock,
                                     reactErrorCallbackMock);

        when(appearanceMock.analysisStopSuccess(anyString())).thenReturn("success");

        verify(analysisServiceMock, times(1)).stopAnalysis(eq("test_id"),
                                                           stringCallbackCaptor.capture(),
                                                           eq("Canceled"));

        stringCallbackCaptor.getValue().onSuccess("result");
        verify(announcerMock).schedule(isA(SuccessAnnouncementConfig.class));
    }

    @Test
    public void completeSelectedAnalyses() {
        AnalysesPresenterImpl spy = spy(uut);
        when(appearanceMock.analysisStopSuccess(anyString())).thenReturn("success");

        /** CALL METHOD UNDER TEST **/
        spy.onCompleteAnalysisSelected("test_id",
                                       "test_name",
                                       reactSuccessCallbackMock,
                                       reactErrorCallbackMock);

        verify(analysisServiceMock, times(1)).stopAnalysis(eq("test_id"),
                                                           stringCallbackCaptor.capture(),
                                                           eq("Completed"));

        stringCallbackCaptor.getValue().onSuccess("result");
        verify(announcerMock).schedule(isA(SuccessAnnouncementConfig.class));
        verify(reactSuccessCallbackMock).onSuccess(null);
    }

    @Test
    public void deleteAnalysis() {
        AnalysesPresenterImpl spy = spy(uut);
        String [] ids = {"1", "2", "3"};

        /** CALL METHOD UNDER TEST **/
        spy.deleteAnalyses(ids, reactSuccessCallbackMock, reactErrorCallbackMock);
        verify(analysisServiceMock).deleteAnalyses(eq(ids), stringCallbackCaptor.capture());

        stringCallbackCaptor.getValue().onSuccess("result");
        verify(reactSuccessCallbackMock).onSuccess(null);
    }

    @Test
    public void go() {
        AnalysesPresenterImpl spy = spy(uut);
        HasOneWidget containerMock = mock(HasOneWidget.class);

        /** CALL METHOD UNDER TEST **/
        spy.go(containerMock,"baseId", analysisListMock);

        verify(viewMock).setPresenter(spy, "baseId", analysisListMock);
        verify(viewMock).load();
    }

    @Test
    public void loadAnalyses() {
/*        AnalysisPermissionFilter filterMock = mock(AnalysisPermissionFilter.class);
        AppTypeFilter typeFilterMock = mock(AppTypeFilter.class);
        when(analysisSearchFieldMock.getCurrentValue()).thenReturn(null);


        *//** CALL METHOD UNDER TEST **//*
        uut.loadAnalyses(filterMock, typeFilterMock);

        verify(filterConfigsMock).clear();
        verify(filterConfigBeanMock, times(3)).setField(anyString());
        verify(filterConfigBeanMock, times(3)).setValue(anyString());

        verify(filterConfigsMock, times(3)).add(filterConfigBeanMock);
        verify(loadConfigMock).setLimit(anyInt());
        verify(loadConfigMock).setOffset(anyInt());
        verify(loaderMock).load(loadConfigMock);*/
    }

    @Test
    public void renameAnalysis() {
        SafeHtml safeHtmlMock = mock(SafeHtml.class);
        when(appearanceMock.analysisRenameSuccess()).thenReturn(safeHtmlMock);

        /** CALL METHOD UNDER TEST **/
        uut.renameAnalysis("test_id",
                           "newName",
                           reactSuccessCallbackMock,
                           reactErrorCallbackMock);

        verify(analysisServiceMock).renameAnalysis(eq("test_id"),
                                                   eq("newName"),
                                                   voidCallbackCaptor.capture());

        voidCallbackCaptor.getValue().onSuccess(null);
        verify(announcerMock).schedule(isA(SuccessAnnouncementConfig.class));
        verify(reactSuccessCallbackMock).onSuccess(null);
    }

    @Test
    public void updateAnalysisComments() {
        SafeHtml safeHtmlMock = mock(SafeHtml.class);
        when(appearanceMock.analysisCommentUpdateSuccess()).thenReturn(safeHtmlMock);

        /** CALL METHOD UNDER TEST **/
        uut.updateAnalysisComments("test_id",
                                   "comment",
                                   reactSuccessCallbackMock,
                                   reactErrorCallbackMock);

        verify(analysisServiceMock).updateAnalysisComments(eq("test_id"),
                                                           eq("comment"),
                                                           voidCallbackCaptor.capture());

        voidCallbackCaptor.getValue().onSuccess(null);
        verify(announcerMock).schedule(isA(SuccessAnnouncementConfig.class));
        verify(reactSuccessCallbackMock).onSuccess(null);
    }

    @Test
    public void getAnalysisStepInfo() {

        /** CALL METHOD UNDER TEST **/
        uut.onAnalysisJobInfoSelected("test_id",
                                      reactSuccessCallbackMock,
                                      reactErrorCallbackMock);

        verify(analysisServiceMock).getAnalysisSteps(eq("test_id"),
                                                     analysisStepsCaptor.capture());

        analysisStepsCaptor.getValue().onSuccess(analysisStepsInfoMock);
    }

    @Test
    public void getAnalysisSupportRequest() {
        AnalysisSupportRequestFields requestFields = mock(AnalysisSupportRequestFields.class);
        AnalysisSupportRequest request = mock(AnalysisSupportRequest.class);

        when(supportFactoryMock.analysisSupportRequestFields()).thenReturn(aSupportRequestFieldsBeanMock);
        when(supportFactoryMock.analysisSupportRequest()).thenReturn(aSupportRequestBeanMock);
        when(aSupportRequestBeanMock.as()).thenReturn(request);
        when(aSupportRequestFieldsBeanMock.as()).thenReturn(requestFields);
        when(userInfoMock.getUsername()).thenReturn(TESTUSER);
        when(userInfoMock.getEmail()).thenReturn(TEST_CYVERSE_ORG);
        when(userInfoMock.getFullUsername()).thenReturn(TESTUSER_CYVERE_ORG);
        when(appearanceMock.userRequestingHelpSubject()).thenReturn(REQUEST_HELP);

        uut.getAnalysisSupportRequest(analysisMock, THIS_IS_A_COMMENT);

        verify(request).setFrom(eq(TESTUSER_CYVERE_ORG));
        verify(request).setSubject(eq(TESTUSER + " " + REQUEST_HELP));

        verify(requestFields).setEmail(eq(TEST_CYVERSE_ORG));
        verify(requestFields).setStatus(eq(AnalysisExecutionStatus.COMPLETED.toString()));
        verify(requestFields).setName(eq(NAME));
        verify(requestFields).setApp(eq(APP_NAME));
        verify(requestFields).setComment(eq(THIS_IS_A_COMMENT));
        verify(requestFields).setOutputFolder(eq(RESULT));
    }



}
