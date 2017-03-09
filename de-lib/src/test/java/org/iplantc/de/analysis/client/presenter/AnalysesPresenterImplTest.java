package org.iplantc.de.analysis.client.presenter;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.iplantc.de.analysis.client.AnalysesView;
import org.iplantc.de.analysis.client.AnalysisToolBarView;
import org.iplantc.de.analysis.client.events.AnalysisCommentUpdate;
import org.iplantc.de.analysis.client.events.AnalysisFilterChanged;
import org.iplantc.de.analysis.client.events.selection.AnalysisJobInfoSelected;
import org.iplantc.de.analysis.client.events.selection.CancelAnalysisSelected;
import org.iplantc.de.analysis.client.events.selection.DeleteAnalysisSelected;
import org.iplantc.de.analysis.client.events.selection.RenameAnalysisSelected;
import org.iplantc.de.analysis.client.gin.factory.AnalysesViewFactory;
import org.iplantc.de.analysis.client.models.AnalysisFilter;
import org.iplantc.de.analysis.client.presenter.proxy.AnalysisRpcProxy;
import org.iplantc.de.analysis.client.views.AnalysisStepsView;
import org.iplantc.de.analysis.client.views.dialogs.AnalysisSharingDialog;
import org.iplantc.de.analysis.client.views.dialogs.AnalysisStepsInfoDialog;
import org.iplantc.de.analysis.client.views.widget.AnalysisSearchField;
import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.analysis.Analysis;
import org.iplantc.de.client.models.analysis.AnalysisExecutionStatus;
import org.iplantc.de.client.models.analysis.AnalysisStep;
import org.iplantc.de.client.models.analysis.AnalysisStepsInfo;
import org.iplantc.de.client.models.analysis.support.AnalysisSupportAutoBeanFactory;
import org.iplantc.de.client.models.analysis.support.AnalysisSupportRequest;
import org.iplantc.de.client.models.analysis.support.AnalysisSupportRequestFields;
import org.iplantc.de.client.services.AnalysisServiceFacade;
import org.iplantc.de.client.util.JsonUtil;
import org.iplantc.de.collaborators.client.util.CollaboratorsUtil;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;
import org.iplantc.de.shared.AnalysisCallback;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwtmockito.GwtMockitoTestRunner;
import com.google.web.bindery.autobean.shared.AutoBean;

import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.loader.FilterConfig;
import com.sencha.gxt.data.shared.loader.FilterConfigBean;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfigBean;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
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
    @Mock AnalysisStepsView analysisStepsViewMock;
    @Mock AsyncProviderWrapper<AnalysisSharingDialog> aSharingDialogProviderMock;
    @Mock AnalysisSharingDialog analysisSharingDialogMock;
    @Mock CollaboratorsUtil collaboratorsUtilMock;
    @Mock JsonUtil jsonUtilMock;
    @Mock AnalysisFilter currentFilterMock;
    @Mock ListStore<Analysis> listStoreMock;
    @Mock AnalysesView viewMock;
    @Mock HasHandlers eventBusMock;
    @Mock HandlerRegistration handlerFirstLoadMock;
    @Mock PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Analysis>> loaderMock;
    @Mock AnalysesViewFactory viewFactoryMock;
    @Mock AnalysisRpcProxy proxyMock;
    @Mock EventBus eventBus;
    @Mock List<Analysis> analysisListMock;
    @Mock ArrayList<Analysis> analysisArrayMock;
    @Mock Iterator<Analysis> analysisIteratorMock;
    @Mock Analysis analysisMock;
    @Mock Throwable throwableMock;
    @Mock AnalysisSearchField analysisSearchFieldMock;
    @Mock FilterPagingLoadConfig loadConfigMock;
    @Mock List<FilterConfig> filterConfigsMock;
    @Mock FilterConfigBean filterConfigBeanMock;
    @Mock AnalysisStepsInfo analysisStepsInfoMock;
    @Mock AnalysisStepsInfoDialog analysisStepsInfoDialogMock;
    @Mock List<AnalysisStep> stepListMock;
    @Mock UserInfo userInfoMock;
    @Mock AnalysisSupportAutoBeanFactory supportFactoryMock;
    @Mock
    AutoBean<AnalysisSupportRequest> aSupportRequestBeanMock;
    @Mock
    AutoBean<AnalysisSupportRequestFields> aSupportRequestFieldsBeanMock;

    @Mock AnalysisToolBarView toolbarView;
    @Mock AsyncProviderWrapper<AnalysisStepsInfoDialog> stepsInfoDialogMock;

    @Captor ArgumentCaptor<AnalysisCallback<String>> stringCallbackCaptor;
    @Captor ArgumentCaptor<AnalysisCallback<Void>> voidCallbackCaptor;
    @Captor ArgumentCaptor<AnalysisCallback<AnalysisStepsInfo>> analysisStepsCaptor;
    @Captor ArgumentCaptor<AsyncCallback<AnalysisStepsInfoDialog>> stepsInfoDialogCaptor;

    private AnalysesPresenterImpl uut;


    @Before
    public void setUp() {
        when(viewFactoryMock.create(Matchers.<ListStore<Analysis>>any(),
                                    Matchers.<PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Analysis>>>any()))
                .thenReturn(viewMock);
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
        when(viewMock.getToolBarView()).thenReturn(toolbarView);
        when(viewMock.getSearchField()).thenReturn(analysisSearchFieldMock);
        when(loaderMock.getLastLoadConfig()).thenReturn(loadConfigMock);
        when(loadConfigMock.getFilters()).thenReturn(filterConfigsMock);


        uut = new AnalysesPresenterImpl(viewFactoryMock,
                                        proxyMock,
                                        listStoreMock,
                                        eventBus){
            @Override
            PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Analysis>> getPagingLoader(AnalysisRpcProxy proxy) {
                return loaderMock;
            }

            @Override
            ArrayList<Analysis> getNewAnalysisList() {
                return analysisArrayMock;
            }

            @Override
            FilterConfigBean getFilterConfigBean() {
                return filterConfigBeanMock;
            }
        };

        uut.currentFilter = currentFilterMock;
        uut.analysisService = analysisServiceMock;
        uut.announcer = announcerMock;
        uut.appearance = appearanceMock;
        uut.analysisStepView = analysisStepsViewMock;
        uut.aSharingDialogProvider = aSharingDialogProviderMock;
        uut.collaboratorsUtil = collaboratorsUtilMock;
        uut.userInfo = userInfoMock;
        uut.supportFactory = supportFactoryMock;
        uut.stepsInfoDialogProvider = stepsInfoDialogMock;

        verifyConstructor(uut);
    }

    private void verifyConstructor(AnalysesPresenterImpl uut) {
        verify(loaderMock).useLoadConfig(isA(FilterPagingLoadConfigBean.class));
        verify(loaderMock).setRemoteSort(anyBoolean());
        verify(loaderMock).setReuseLoadConfig(anyBoolean());

        verify(viewMock).addAnalysisNameSelectedEventHandler(eq(uut));
        verify(viewMock).addAnalysisAppSelectedEventHandler(eq(uut));
        verify(viewMock).addHTAnalysisExpandEventHandler(eq(uut));
    }

    @Test
    public void cancelSelectedAnalyses() {
        CancelAnalysisSelected eventMock = mock(CancelAnalysisSelected.class);
        when(eventMock.getAnalysisList()).thenReturn(analysisListMock);
        AnalysesPresenterImpl spy = spy(uut);
        when(appearanceMock.analysisStopSuccess(anyString())).thenReturn("success");

        /** CALL METHOD UNDER TEST **/
        spy.onCancelAnalysisSelected(eventMock);

        verify(analysisServiceMock, times(1)).stopAnalysis(eq(analysisMock), stringCallbackCaptor.capture());

        stringCallbackCaptor.getValue().onSuccess("result");
        verify(announcerMock).schedule(isA(SuccessAnnouncementConfig.class));
        verify(spy).loadAnalyses(eq(currentFilterMock));
    }

    @Test
    public void deleteSelectedAnalyses() {
        AnalysesPresenterImpl spy = spy(uut);
        DeleteAnalysisSelected eventMock = mock(DeleteAnalysisSelected.class);
        when(eventMock.getAnalyses()).thenReturn(analysisListMock);

        /** CALL METHOD UNDER TEST **/
        spy.onDeleteAnalysisSelected(eventMock);

        verify(analysisServiceMock).deleteAnalyses(eq(analysisListMock), stringCallbackCaptor.capture());

        stringCallbackCaptor.getValue().onSuccess("result");
        verify(spy).loadAnalyses(currentFilterMock);
    }

    @Test
    public void getSelectedAnalyses() {

        /** CALL METHOD UNDER TEST **/
        uut.getSelectedAnalyses();

        verify(viewMock).getSelectedAnalyses();
    }

    @Test
    public void setSelectedAnalyses_null() {

        /** CALL METHOD UNDER TEST **/
        uut.setSelectedAnalyses(null);
        verifyZeroInteractions(listStoreMock);
    }

    @Test
    public void setSelectedAnalyses_Found() {
        when(listStoreMock.findModel(analysisMock)).thenReturn(analysisMock);

        /** CALL METHOD UNDER TEST **/
        uut.setSelectedAnalyses(analysisListMock);

        verify(listStoreMock).findModel(eq(analysisMock));
        verify(analysisArrayMock).add(eq(analysisMock));

        verify(viewMock).setSelectedAnalyses(eq(analysisArrayMock));
    }

    @Test
    public void setSelectedAnalyses_NotFound() {
        when(listStoreMock.findModel(analysisMock)).thenReturn(null);
        when(analysisArrayMock.isEmpty()).thenReturn(true);
        when(analysisListMock.get(0)).thenReturn(analysisMock);

        /** CALL METHOD UNDER TEST **/
        uut.setSelectedAnalyses(analysisListMock);

        verify(listStoreMock).findModel(eq(analysisMock));

        verify(viewMock).filterByAnalysisId(eq("id"), eq("name"));
    }

    @Test
    public void go() {
        AnalysesPresenterImpl spy = spy(uut);
        HasOneWidget containerMock = mock(HasOneWidget.class);

        /** CALL METHOD UNDER TEST **/
        spy.go(containerMock, analysisListMock);
        verify(spy).loadAnalyses(eq(AnalysisFilter.ALL));
        verify(containerMock).setWidget(eq(viewMock));
    }

    @Test
    public void loadAnalyses_search() {
        AnalysisFilter filterMock = mock(AnalysisFilter.class);
        when(analysisSearchFieldMock.getCurrentValue()).thenReturn("Value");

        /** CALL METHOD UNDER TEST **/
        uut.loadAnalyses(filterMock);
        verify(analysisSearchFieldMock).refreshSearch();
        verifyZeroInteractions(loaderMock);
    }

    @Test
    public void loadAnalyses() {
        AnalysisFilter filterMock = mock(AnalysisFilter.class);
        when(analysisSearchFieldMock.getCurrentValue()).thenReturn(null);


        /** CALL METHOD UNDER TEST **/
        uut.loadAnalyses(filterMock);

        verify(filterConfigsMock).clear();
        verify(filterConfigBeanMock, times(2)).setField(anyString());
        verify(filterConfigBeanMock, times(2)).setValue(anyString());

        verify(filterConfigsMock, times(2)).add(filterConfigBeanMock);
        verify(loadConfigMock).setLimit(anyInt());
        verify(loadConfigMock).setOffset(anyInt());
        verify(loaderMock).load(loadConfigMock);
    }

    @Test
    public void setCurrentFilter_null() {
        AnalysesPresenterImpl spy = spy(uut);
        AnalysisFilterChanged eventMock = mock(AnalysisFilterChanged.class);
        when(eventMock.getFilter()).thenReturn(null);

        /** CALL METHOD UNDER TEST **/
        spy.onAnalysisFilterChanged(eventMock);
        verify(spy, times(0)).loadAnalyses(eq(currentFilterMock));
    }

    @Test
    public void setCurrentFilter() {
        AnalysesPresenterImpl spy = spy(uut);
        AnalysisFilterChanged eventMock = mock(AnalysisFilterChanged.class);
        when(eventMock.getFilter()).thenReturn(AnalysisFilter.MY_ANALYSES);

        /** CALL METHOD UNDER TEST **/
        spy.onAnalysisFilterChanged(eventMock);
        currentFilterMock = AnalysisFilter.MY_ANALYSES;
        verify(spy, times(1)).loadAnalyses(eq(currentFilterMock));
    }

    @Test
    public void renameSelectedAnalysis() {
        SafeHtml safeHtmlMock = mock(SafeHtml.class);
        RenameAnalysisSelected eventMock = mock(RenameAnalysisSelected.class);
        when(eventMock.getAnalysis()).thenReturn(analysisMock);
        when(eventMock.getNewName()).thenReturn("newName");
        when(appearanceMock.analysisRenameSuccess()).thenReturn(safeHtmlMock);

        /** CALL METHOD UNDER TEST **/
        uut.onRenameAnalysisSelected(eventMock);

        verify(analysisServiceMock).renameAnalysis(eq(analysisMock),
                                                   eq("newName"),
                                                   voidCallbackCaptor.capture());

        voidCallbackCaptor.getValue().onSuccess(null);

        verify(analysisMock).setName(eq("newName"));
        verify(listStoreMock).update(eq(analysisMock));
        verify(announcerMock).schedule(isA(SuccessAnnouncementConfig.class));
    }

    @Test
    public void updateAnalysisComment() {
        SafeHtml safeHtmlMock = mock(SafeHtml.class);
        AnalysisCommentUpdate eventMock = mock(AnalysisCommentUpdate.class);
        when(eventMock.getAnalysis()).thenReturn(analysisMock);
        when(eventMock.getComment()).thenReturn("comment");
        when(appearanceMock.analysisCommentUpdateSuccess()).thenReturn(safeHtmlMock);

        /** CALL METHOD UNDER TEST **/
        uut.onAnalysisCommentUpdate(eventMock);

        verify(analysisServiceMock).updateAnalysisComments(eq(analysisMock),
                                                           eq("comment"),
                                                           voidCallbackCaptor.capture());

        voidCallbackCaptor.getValue().onSuccess(null);
        verify(analysisMock).setComments(eq("comment"));
        verify(listStoreMock).update(eq(analysisMock));
        verify(announcerMock).schedule(isA(SuccessAnnouncementConfig.class));
    }

    @Test
    public void getAnalysisStepInfo() {
        AnalysisJobInfoSelected eventMock = mock(AnalysisJobInfoSelected.class);
        when(eventMock.getAnalysis()).thenReturn(analysisMock);
        when(analysisStepsInfoMock.getSteps()).thenReturn(stepListMock);

        /** CALL METHOD UNDER TEST **/
        uut.onAnalysisJobInfoSelected(eventMock);

        verify(analysisServiceMock).getAnalysisSteps(eq(analysisMock), analysisStepsCaptor.capture());

        analysisStepsCaptor.getValue().onSuccess(analysisStepsInfoMock);
        verify(stepsInfoDialogMock).get(stepsInfoDialogCaptor.capture());
        stepsInfoDialogCaptor.getValue().onSuccess(analysisStepsInfoDialogMock);
        verify(analysisStepsInfoDialogMock).show(eq(analysisStepsInfoMock));

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
        verify(request).setSubject(eq(TESTUSER + REQUEST_HELP));

        verify(requestFields).setEmail(eq(TEST_CYVERSE_ORG));
        verify(requestFields).setStatus(eq(AnalysisExecutionStatus.COMPLETED.toString()));
        verify(requestFields).setName(eq(NAME));
        verify(requestFields).setApp(eq(APP_NAME));
        verify(requestFields).setComment(eq(THIS_IS_A_COMMENT));
        verify(requestFields).setOutputFolder(eq(RESULT));
    }

}
