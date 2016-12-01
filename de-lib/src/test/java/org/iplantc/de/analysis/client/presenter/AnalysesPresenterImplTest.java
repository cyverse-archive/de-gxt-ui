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
import org.iplantc.de.analysis.client.gin.factory.AnalysesViewFactory;
import org.iplantc.de.analysis.client.models.AnalysisFilter;
import org.iplantc.de.analysis.client.presenter.proxy.AnalysisRpcProxy;
import org.iplantc.de.analysis.client.views.AnalysisStepsView;
import org.iplantc.de.analysis.client.views.dialogs.AnalysisSharingDialog;
import org.iplantc.de.analysis.client.views.dialogs.AnalysisStepsInfoDialog;
import org.iplantc.de.analysis.client.views.widget.AnalysisSearchField;
import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.analysis.Analysis;
import org.iplantc.de.client.models.analysis.AnalysisStep;
import org.iplantc.de.client.models.analysis.AnalysisStepsInfo;
import org.iplantc.de.client.services.AnalysisServiceFacade;
import org.iplantc.de.client.util.JsonUtil;
import org.iplantc.de.collaborators.client.util.CollaboratorsUtil;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;
import org.iplantc.de.shared.AnalysisCallback;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwtmockito.GwtMockitoTestRunner;
import com.google.inject.Provider;

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
import java.util.Iterator;
import java.util.List;

/**
 * @author aramsey
 */
@RunWith(GwtMockitoTestRunner.class)
public class AnalysesPresenterImplTest {

    @Mock AnalysisServiceFacade analysisServiceMock;
    @Mock IplantAnnouncer announcerMock;
    @Mock AnalysesView.Presenter.Appearance appearanceMock;
    @Mock AnalysisStepsView analysisStepsViewMock;
    @Mock Provider<AnalysisSharingDialog> aSharingDialogProviderMock;
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

    @Captor ArgumentCaptor<AnalysisCallback<String>> stringCallbackCaptor;
    @Captor ArgumentCaptor<AnalysisCallback<Void>> voidCallbackCaptor;
    @Captor ArgumentCaptor<AnalysisCallback<AnalysisStepsInfo>> analysisStepsCaptor;

    private AnalysesPresenterImpl uut;


    @Before
    public void setUp() {
        when(viewFactoryMock.create(Matchers.<ListStore<Analysis>>any(),
                                    Matchers.<PagingLoader<FilterPagingLoadConfig, PagingLoadResult<Analysis>>>any(),
                                    Matchers.<AnalysesPresenterImpl>any()))
                .thenReturn(viewMock);
        when(analysisListMock.size()).thenReturn(1);
        when(analysisListMock.iterator()).thenReturn(analysisIteratorMock);
        when(analysisIteratorMock.hasNext()).thenReturn(true, false);
        when(analysisIteratorMock.next()).thenReturn(analysisMock).thenReturn(null);
        when(analysisMock.getId()).thenReturn("id");
        when(analysisMock.getName()).thenReturn("name");
        when(appearanceMock.deleteAnalysisError()).thenReturn("error");
        when(viewMock.getSearchField()).thenReturn(analysisSearchFieldMock);
        when(loaderMock.getLastLoadConfig()).thenReturn(loadConfigMock);
        when(loadConfigMock.getFilters()).thenReturn(filterConfigsMock);
        when(aSharingDialogProviderMock.get()).thenReturn(analysisSharingDialogMock);

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
            AnalysisStepsInfoDialog getAnalysisStepsDialog() {
                return analysisStepsInfoDialogMock;
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
        uut.jsonUtil = jsonUtilMock;

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
        AnalysesPresenterImpl spy = spy(uut);
        when(appearanceMock.analysisStopSuccess(anyString())).thenReturn("success");

        /** CALL METHOD UNDER TEST **/
        spy.cancelSelectedAnalyses(analysisListMock);

        verify(analysisServiceMock, times(1)).stopAnalysis(eq(analysisMock), stringCallbackCaptor.capture());

        stringCallbackCaptor.getValue().onSuccess("result");
        verify(announcerMock).schedule(isA(SuccessAnnouncementConfig.class));
        verify(spy).loadAnalyses(eq(currentFilterMock));
    }

    @Test
    public void deleteSelectedAnalyses() {
        AnalysesPresenterImpl spy = spy(uut);

        /** CALL METHOD UNDER TEST **/
        spy.deleteSelectedAnalyses(analysisListMock);

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

        /** CALL METHOD UNDER TEST **/
        spy.setCurrentFilter(null);
        verify(spy, times(0)).loadAnalyses(eq(currentFilterMock));
    }

    @Test
    public void setCurrentFilter() {
        AnalysesPresenterImpl spy = spy(uut);

        /** CALL METHOD UNDER TEST **/
        spy.setCurrentFilter(AnalysisFilter.MY_ANALYSES);
        currentFilterMock = AnalysisFilter.MY_ANALYSES;
        verify(spy, times(1)).loadAnalyses(eq(currentFilterMock));
    }

    @Test
    public void renameSelectedAnalysis() {
        SafeHtml safeHtmlMock = mock(SafeHtml.class);
        when(appearanceMock.analysisRenameSuccess()).thenReturn(safeHtmlMock);

        /** CALL METHOD UNDER TEST **/
        uut.renameSelectedAnalysis(analysisMock, "newName");

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
        when(appearanceMock.analysisCommentUpdateSuccess()).thenReturn(safeHtmlMock);

        /** CALL METHOD UNDER TEST **/
        uut.updateAnalysisComment(analysisMock, "comment");

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
        when(analysisStepsInfoMock.getSteps()).thenReturn(stepListMock);

        /** CALL METHOD UNDER TEST **/
        uut.getAnalysisStepInfo(analysisMock);

        verify(analysisServiceMock).getAnalysisSteps(eq(analysisMock), analysisStepsCaptor.capture());

        analysisStepsCaptor.getValue().onSuccess(analysisStepsInfoMock);
        verify(analysisStepsInfoDialogMock).show();
        verify(analysisStepsViewMock).clearData();
        verify(analysisStepsViewMock).setData(eq(stepListMock));

    }

}
