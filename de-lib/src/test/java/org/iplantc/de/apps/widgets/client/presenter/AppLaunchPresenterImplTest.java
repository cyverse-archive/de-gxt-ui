package org.iplantc.de.apps.widgets.client.presenter;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.iplantc.de.apps.widgets.client.events.AnalysisLaunchEvent;
import org.iplantc.de.apps.widgets.client.view.AppLaunchView;
import org.iplantc.de.apps.widgets.client.view.dialogs.HPCWaitTimeDialog;
import org.iplantc.de.client.DEClientConstants;
import org.iplantc.de.client.models.HasId;
import org.iplantc.de.client.models.HasQualifiedId;
import org.iplantc.de.client.models.UserSettings;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.integration.AppTemplate;
import org.iplantc.de.client.models.apps.integration.AppTemplateAutoBeanFactory;
import org.iplantc.de.client.models.apps.integration.JobExecution;
import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.client.services.AppTemplateServices;
import org.iplantc.de.client.services.impl.models.AnalysisSubmissionResponse;
import org.iplantc.de.client.util.AppTemplateUtils;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;
import org.iplantc.de.commons.client.views.window.configs.AppWizardConfig;
import org.iplantc.de.resources.client.constants.IplantValidationConstants;
import org.iplantc.de.shared.AppLaunchCallback;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwtmockito.GwtMockitoTestRunner;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.widget.core.client.event.DialogHideEvent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

/**
 * @author aramsey
 */
@RunWith(GwtMockitoTestRunner.class)
public class AppLaunchPresenterImplTest {

    @Mock AppTemplate appTemplateMock;
    @Mock AppTemplateServices atServicesMock;
    @Mock HandlerManager handlerManagerMock;
    @Mock UserSettings userSettingsMock;
    @Mock AppTemplateAutoBeanFactory factoryMock;
    @Mock DEClientConstants deClientConstantsMock;
    @Mock AppTemplateUtils appTemplateUtilsMock;
    @Mock AppLaunchView.AppLaunchViewAppearance appearanceMock;
    @Mock IplantValidationConstants valConstantsMock;
    @Mock AppLaunchView viewMock;
    @Mock HasOneWidget containerMock;
    @Mock AppWizardConfig configMock;
    @Mock Splittable templateSplittableMock;
    @Mock JobExecution jeMock;
    @Mock Folder defaultOutputFolder;
    @Mock HasId hasIdMock;
    @Mock HasQualifiedId hasQualifiedIdMock;
    @Mock IplantAnnouncer announcerMock;
    @Mock AnalysisSubmissionResponse responseMock;
    @Mock HPCWaitTimeDialog hpcWaitTimeDialogMock;
    @Mock AsyncProviderWrapper<HPCWaitTimeDialog> hpcWaitDlgProviderMock;
    @Mock Widget viewWidgetMock;

    @Captor ArgumentCaptor<AppLaunchCallback<AppTemplate>> appTemplateCaptor;
    @Captor ArgumentCaptor<AppLaunchCallback<AnalysisSubmissionResponse>> analysisSubmissionCaptor;
    @Captor ArgumentCaptor<AsyncCallback<HPCWaitTimeDialog>> hpcWaitDlgCaptor;
    @Captor ArgumentCaptor<DialogHideEvent.DialogHideHandler> hideHandlerCaptor;

    private AppLaunchPresenterImpl uut;

    @Before
    public void setUp() {
        when(appTemplateUtilsMock.convertConfigToTemplate(configMock)).thenReturn(appTemplateMock);
        when(appearanceMock.defaultAnalysisName()).thenReturn("name");
        when(appearanceMock.launchAnalysisSuccess(anyString())).thenReturn("success");
        when(appearanceMock.launchAnalysisFailure(anyString())).thenReturn("fail");
        when(appTemplateMock.getId()).thenReturn("id");
        when(appTemplateMock.getName()).thenReturn("name");
        when(userSettingsMock.getDefaultOutputFolder()).thenReturn(defaultOutputFolder);
        when(defaultOutputFolder.getPath()).thenReturn("path");
        when(userSettingsMock.isEnableAnalysisEmailNotification()).thenReturn(true);
        when(viewMock.asWidget()).thenReturn(viewWidgetMock);


        uut = new AppLaunchPresenterImpl(viewMock,
                                         userSettingsMock,
                                         atServicesMock,
                                         factoryMock,
                                         deClientConstantsMock,
                                         appTemplateUtilsMock,
                                         appearanceMock){
            @Override
            JobExecution getJobExecution() {
                return jeMock;
            }

            @Override
            String getRestrictedCharRegEx() {
                return "@!";
            }

            @Override
            HasQualifiedId getQualifiedIdFromConfig(AppWizardConfig config) {
                return hasQualifiedIdMock;
            }
        };
        uut.appTemplate = appTemplateMock;
        uut.handlerManager = handlerManagerMock;
        uut.container = containerMock;
        uut.announcer = announcerMock;
        uut.hpcWaitDlgProvider = hpcWaitDlgProviderMock;
    }

    @Test
    public void go_withTemplate() {
        AppLaunchPresenterImpl spy = spy(uut);
        when(configMock.getAppTemplate()).thenReturn(templateSplittableMock);

        /** CALL METHOD UNDER TEST **/
        spy.go(containerMock, configMock);
        verify(configMock).getAppTemplate();
        verify(spy).createJobExecution();
        verifyZeroInteractions(atServicesMock);
    }

    @Test
    public void go_relaunch() {
        AppLaunchPresenterImpl spy = spy(uut);
        when(configMock.getAppTemplate()).thenReturn(null);
        when(configMock.isRelaunchAnalysis()).thenReturn(true);
        when(configMock.getAppId()).thenReturn("id");
        when(configMock.getAnalysisId()).thenReturn(hasIdMock);
        when(appTemplateMock.isAppDisabled()).thenReturn(false);

        /** CALL METHOD UNDER TEST **/
        spy.go(containerMock, configMock);
        verify(configMock).getAppTemplate();
        verify(atServicesMock).rerunAnalysis(eq(hasIdMock),
                                             eq("id"),
                                             appTemplateCaptor.capture());

        appTemplateCaptor.getValue().onSuccess(appTemplateMock);
        verify(spy).createJobExecution();
    }

    @Test
    public void go_nullTemplate() {
        AppLaunchPresenterImpl spy = spy(uut);
        when(configMock.getAppTemplate()).thenReturn(null);
        when(configMock.isRelaunchAnalysis()).thenReturn(false);
        when(configMock.getAppId()).thenReturn("id");
        when(configMock.getAnalysisId()).thenReturn(hasIdMock);
        when(appTemplateMock.isAppDisabled()).thenReturn(false);

        /** CALL METHOD UNDER TEST **/
        spy.go(containerMock, configMock);
        verify(configMock).getAppTemplate();
        verify(atServicesMock).getAppTemplate(eq(hasQualifiedIdMock),
                                             appTemplateCaptor.capture());

        appTemplateCaptor.getValue().onSuccess(appTemplateMock);
        verify(spy).createJobExecution();
    }

    @Test
    public void createJobExecution() {
        when(appTemplateMock.isRetainInputs()).thenReturn(true);
        /** CALL METHOD UNDER TEST **/
        uut.createJobExecution();

        verify(jeMock).setAppTemplateId(eq("id"));
        verify(jeMock).setRetainInputs(eq(true));
        verify(jeMock).setEmailNotificationEnabled(eq(userSettingsMock.isEnableAnalysisEmailNotification()));
        verify(jeMock).setName(anyString());
        verify(jeMock).setOutputDirectory(eq("path"));
        verify(viewMock).edit(eq(appTemplateMock), eq(jeMock));
        verify(containerMock).setWidget(eq(viewMock));
    }

    @Test
    public void launchAnalysis() {
        when(responseMock.getMissingPaths()).thenReturn(null);

        /** CALL METHOD UNDER TEST **/
        uut.launchAnalysis(appTemplateMock, jeMock);

        verify(atServicesMock).launchAnalysis(eq(appTemplateMock),
                                              eq(jeMock),
                                              analysisSubmissionCaptor.capture());

        analysisSubmissionCaptor.getValue().onSuccess(responseMock);
        verify(announcerMock).schedule(isA(SuccessAnnouncementConfig.class));
        verify(handlerManagerMock).fireEvent(isA(AnalysisLaunchEvent.class));
    }

    @Test
    public void onAnalysisLaunchRequest_External() {
        when(appTemplateMock.getAppType()).thenReturn(App.EXTERNAL_APP);
        when(userSettingsMock.isEnableWaitTimeMessage()).thenReturn(true);
        AppLaunchPresenterImpl spy = spy(uut);

        /** CALL METHOD UNDER TEST **/
        spy.onAnalysisLaunchRequest(appTemplateMock, jeMock);
        verify(spy).showWaitTimeNotice(eq(appTemplateMock), eq(jeMock));
    }

    @Test
    public void onAnalysisLaunchRequest_nonExternal() {
        when(appTemplateMock.getAppType()).thenReturn("notExternal");
        AppLaunchPresenterImpl spy = spy(uut);

        /** CALL METHOD UNDER TEST **/
        spy.onAnalysisLaunchRequest(appTemplateMock, jeMock);
        verify(spy).launchAnalysis(eq(appTemplateMock), eq(jeMock));
    }

    @Test
    public void showWaitTimeNotice() {
        DialogHideEvent hideEvent = mock(DialogHideEvent.class);
        AppLaunchPresenterImpl spy = spy(uut);

        /** CALL METHOD UNDER TEST **/
        spy.showWaitTimeNotice(appTemplateMock, jeMock);
        verify(hpcWaitDlgProviderMock).get(hpcWaitDlgCaptor.capture());

        hpcWaitDlgCaptor.getValue().onSuccess(hpcWaitTimeDialogMock);
        verify(hpcWaitTimeDialogMock).addDialogHideHandler(hideHandlerCaptor.capture());
        verify(hpcWaitTimeDialogMock).show();

        hideHandlerCaptor.getValue().onDialogHide(hideEvent);
        verify(spy).launchAnalysis(eq(appTemplateMock), eq(jeMock));
    }

}
