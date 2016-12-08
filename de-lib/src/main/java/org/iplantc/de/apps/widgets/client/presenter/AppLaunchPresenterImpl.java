package org.iplantc.de.apps.widgets.client.presenter;

import org.iplantc.de.apps.widgets.client.events.AnalysisLaunchEvent;
import org.iplantc.de.apps.widgets.client.events.AnalysisLaunchEvent.AnalysisLaunchEventHandler;
import org.iplantc.de.apps.widgets.client.events.AppTemplateFetched;
import org.iplantc.de.apps.widgets.client.events.RequestAnalysisLaunchEvent.RequestAnalysisLaunchEventHandler;
import org.iplantc.de.apps.widgets.client.view.AppLaunchView;
import org.iplantc.de.client.DEClientConstants;
import org.iplantc.de.client.models.HasQualifiedId;
import org.iplantc.de.client.models.UserSettings;
import org.iplantc.de.client.models.apps.integration.AppTemplate;
import org.iplantc.de.client.models.apps.integration.AppTemplateAutoBeanFactory;
import org.iplantc.de.client.models.apps.integration.JobExecution;
import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.client.services.AppTemplateServices;
import org.iplantc.de.client.services.impl.models.AnalysisSubmissionResponse;
import org.iplantc.de.client.util.AppTemplateUtils;
import org.iplantc.de.client.util.CommonModelUtils;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;
import org.iplantc.de.commons.client.util.RegExp;
import org.iplantc.de.commons.client.views.window.configs.AppWizardConfig;
import org.iplantc.de.resources.client.constants.IplantValidationConstants;
import org.iplantc.de.resources.client.messages.I18N;
import org.iplantc.de.resources.client.uiapps.widgets.AppsWidgetsDisplayMessages;
import org.iplantc.de.resources.client.uiapps.widgets.AppsWidgetsErrorMessages;
import org.iplantc.de.shared.AppLaunchCallback;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;

import com.sencha.gxt.core.client.util.Format;

import java.util.List;

/**
 * 
 * @author jstroot
 *
 */
public class AppLaunchPresenterImpl implements AppLaunchView.Presenter,
                                               RequestAnalysisLaunchEventHandler {

    private final class AppTemplateCallback extends AppLaunchCallback<AppTemplate> {

        @Override
        public void onFailure(Integer statusCode, Throwable caught) {
            ensureHandlers().fireEvent(new AppTemplateFetched(null));
            ErrorHandler.post(appearance.unableToRetrieveWorkflowGuide(), caught);
        }

        @Override
        public void onSuccess(AppTemplate result) {
            if (result.isAppDisabled()) {
                ErrorAnnouncementConfig config = new ErrorAnnouncementConfig(appearance.appUnavailable());
                IplantAnnouncer.getInstance().schedule(config);
                return;
            }
            appTemplate = result;
            createJobExecution();
        }
    }

    @Inject
    private AppsWidgetsDisplayMessages appsWidgetsDisplayMessages;
    @Inject
    private AppsWidgetsErrorMessages appsWidgetsErrMessages;
    AppTemplate appTemplate;
    private final AppTemplateServices atServices;
    HandlerManager handlerManager;
    private final UserSettings userSettings;
    private AppTemplateAutoBeanFactory factory;
    private DEClientConstants deClientConstants;
    private AppTemplateUtils appTemplateUtils;
    private AppLaunchView.AppLaunchViewAppearance appearance;
    HasOneWidget container;

    @Inject
    private IplantValidationConstants valConstants;
    private final AppLaunchView view;

    @Inject
    public AppLaunchPresenterImpl(final AppLaunchView view,
                                  final UserSettings userSettings,
                                  final AppTemplateServices atServices,
                                  AppTemplateAutoBeanFactory factory,
                                  DEClientConstants deClientConstants,
                                  AppTemplateUtils appTemplateUtils,
                                  AppLaunchView.AppLaunchViewAppearance appearance) {
        this.view = view;
        this.userSettings = userSettings;
        this.factory = factory;
        this.deClientConstants = deClientConstants;
        this.appTemplateUtils = appTemplateUtils;
        this.appearance = appearance;
        this.view.addRequestAnalysisLaunchEventHandler(this);
        this.atServices = atServices;
    }
    
    @Override
    public void addAnalysisLaunchHandler(AnalysisLaunchEventHandler handler) {
        ensureHandlers().addHandler(AnalysisLaunchEvent.TYPE, handler);
    }

    @Override
    public void go(final HasOneWidget container, AppWizardConfig config) {
        this.container = container;
        if (config.getAppTemplate() != null) {
            this.appTemplate = appTemplateUtils.convertConfigToTemplate(config);
            createJobExecution();
        } else if (config.isRelaunchAnalysis()) {
            atServices.rerunAnalysis(config.getAnalysisId(),
                                          config.getAppId(),
                                          new AppTemplateCallback());
        } else {
            final HasQualifiedId id = getQualifiedIdFromConfig(config);
            atServices.getAppTemplate(id, new AppTemplateCallback());
        }
    }

    private HasQualifiedId getQualifiedIdFromConfig(AppWizardConfig config) {
        final String systemId = Strings.isNullOrEmpty(config.getSystemId())
                                ? deClientConstants.deSystemId()
                                : config.getSystemId();
        final String appId = config.getAppId();
        return CommonModelUtils.getInstance().createHasQualifiedId(systemId, appId);
    }

    @Override
    public AppTemplate getAppTemplate() {
        return appTemplate;
    }

    void createJobExecution() {
        final JobExecution je = factory.jobExecution().as();
        je.setSystemId(appTemplate.getSystemId());
        je.setAppTemplateId(appTemplate.getId());
        je.setEmailNotificationEnabled(userSettings.isEnableAnalysisEmailNotification());
        // JDS Replace all Cmd Line restricted chars with underscores
        String regex = Format.substitute("[{0}]",
                                         RegExp.escapeCharacterClassSet(valConstants.restrictedCmdLineChars()
                                                 + " "));
        String newName = appTemplate.getName().replaceAll(regex, "_");
        je.setName(newName + "_" + appsWidgetsDisplayMessages.defaultAnalysisName()); //$NON-NLS-1$

        final Folder defaultOutputFolder = userSettings.getDefaultOutputFolder();
        if(defaultOutputFolder != null){
            je.setOutputDirectory(defaultOutputFolder.getPath());
        }

        view.edit(appTemplate, je);
        container.setWidget(view);
        ensureHandlers().fireEvent(new AppTemplateFetched(appTemplate));
    }

    @Override
    public void setViewDebugId(String baseID) {
        view.asWidget().ensureDebugId(baseID);
    }

    @Override
    public void onAnalysisLaunchRequest(final AppTemplate at, final JobExecution je) {
        launchAnalysis(at, je);
    }

    HandlerManager ensureHandlers() {
        return handlerManager == null ? handlerManager = createHandlerManager() : handlerManager;
    }

    private HandlerManager createHandlerManager() {
        return new HandlerManager(this);
    }

    private void launchAnalysis(final AppTemplate at, final JobExecution je) {
        atServices.launchAnalysis(at, je, new AppLaunchCallback<AnalysisSubmissionResponse>() {

            @Override
            public void onFailure(Integer statusCode, Throwable caught) {
                IplantAnnouncer.getInstance().schedule(new ErrorAnnouncementConfig(appsWidgetsErrMessages.launchAnalysisFailure(je.getName())));
                ErrorHandler.post(I18N.ERROR.analysisFailedToLaunch(at.getName()), caught);
                view.analysisLaunchFailed();
            }

            @Override
            public void onSuccess(AnalysisSubmissionResponse result) {
                IplantAnnouncer.getInstance().schedule(new SuccessAnnouncementConfig(appsWidgetsDisplayMessages.launchAnalysisSuccess(je.getName())));
                ensureHandlers().fireEvent(new AnalysisLaunchEvent(at));

                if (result != null) {
                    final List<String> missingPaths = result.getMissingPaths();
                    if (missingPaths != null && !missingPaths.isEmpty()) {
                        ErrorHandler.post(I18N.ERROR.diskResourcesDoNotExist(Joiner.on(", ")
                                                                                   .join(missingPaths)));
                    }
                }
            }
        });
    }

    @Override
    public HandlerRegistration addAppTemplateFetchedHandler(AppTemplateFetched.AppTemplateFetchedHandler handler) {
        return ensureHandlers().addHandler(AppTemplateFetched.TYPE, handler);
    }
}
