package org.iplantc.de.apps.widgets.client.presenter;

import org.iplantc.de.apps.shared.AppsModule;
import org.iplantc.de.apps.widgets.client.ReactQuickLaunch;
import org.iplantc.de.apps.widgets.client.events.AnalysisLaunchEvent;
import org.iplantc.de.apps.widgets.client.events.AnalysisLaunchEvent.AnalysisLaunchEventHandler;
import org.iplantc.de.apps.widgets.client.events.AppTemplateFetched;
import org.iplantc.de.apps.widgets.client.events.CreateQuickLaunchEvent;
import org.iplantc.de.apps.widgets.client.events.RequestAnalysisLaunchEvent.RequestAnalysisLaunchEventHandler;
import org.iplantc.de.apps.widgets.client.view.AppLaunchView;
import org.iplantc.de.apps.widgets.client.view.dialogs.HPCWaitTimeDialog;
import org.iplantc.de.client.DEClientConstants;
import org.iplantc.de.client.models.HasQualifiedId;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.UserSettings;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.client.models.apps.integration.AppTemplate;
import org.iplantc.de.client.models.apps.integration.AppTemplateAutoBeanFactory;
import org.iplantc.de.client.models.apps.integration.JobExecution;
import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.client.services.AppTemplateServices;
import org.iplantc.de.client.services.callbacks.ReactErrorCallback;
import org.iplantc.de.client.services.callbacks.ReactSuccessCallback;
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
import org.iplantc.de.shared.AppLaunchCallback;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
                                               RequestAnalysisLaunchEventHandler,
                                               CreateQuickLaunchEvent.CreateQuickLaunchEventHandler {


    private ReactQuickLaunch.CreateQLProps props;
    private String integratorsEmail;

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
                announcer.schedule(config);
                return;
            }
            appTemplate = result;
            createJobExecution();
        }
    }

    @Inject IplantAnnouncer announcer;
    @Inject CommonModelUtils commonModelUtils;
    AppTemplate appTemplate;
    JobExecution jobExecution;
    private final AppTemplateServices atServices;
    HandlerManager handlerManager;
    private final UserSettings userSettings;
    private AppTemplateAutoBeanFactory factory;
    private DEClientConstants deClientConstants;
    private AppTemplateUtils appTemplateUtils;
    private AppLaunchView.AppLaunchViewAppearance appearance;
    HasOneWidget container;

    @Inject private IplantValidationConstants valConstants;
    @Inject AsyncProviderWrapper<HPCWaitTimeDialog> hpcWaitDlgProvider;
    @Inject
    UserInfo userInfo;
    private final AppLaunchView view;
    private String baseID;

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
        this.view.addCreateQuickLaunchEventHandler(this);
        this.atServices = atServices;
    }
    
    @Override
    public void addAnalysisLaunchHandler(AnalysisLaunchEventHandler handler) {
        ensureHandlers().addHandler(AnalysisLaunchEvent.TYPE, handler);
    }

    @Override
    public void go(final HasOneWidget container, AppWizardConfig config) {
        this.container = container;
        this.integratorsEmail = config.getAppIntegratorEmail();
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

    HasQualifiedId getQualifiedIdFromConfig(AppWizardConfig config) {
        final String systemId = Strings.isNullOrEmpty(config.getSystemId())
                                ? deClientConstants.deSystemId()
                                : config.getSystemId();
        final String appId = config.getAppId();
        return commonModelUtils.createHasQualifiedId(systemId, appId);
    }

    @Override
    public AppTemplate getAppTemplate() {
        return appTemplate;
    }

    void createJobExecution() {
        final JobExecution je = getJobExecution();
        je.setSystemId(appTemplate.getSystemId());
        je.setAppTemplateId(appTemplate.getId());
        je.setRetainInputs(appTemplate.isRetainInputs());
        je.setEmailNotificationEnabled(userSettings.isEnableAnalysisEmailNotification());
        // JDS Replace all Cmd Line restricted chars with underscores
        String regex = getRestrictedCharRegEx();
        String newName = appTemplate.getName().replaceAll(regex, "_");
        je.setName(newName + "_" + appearance.defaultAnalysisName()); //$NON-NLS-1$

        final Folder defaultOutputFolder = userSettings.getDefaultOutputFolder();
        if(defaultOutputFolder != null){
            je.setOutputDirectory(defaultOutputFolder.getPath());
        }

        view.edit(appTemplate, je);
        container.setWidget(view);
        ensureHandlers().fireEvent(new AppTemplateFetched(appTemplate));

        // The view debug ID cannot be set earlier in the case where the app template
        // must be fetched first
        view.asWidget().ensureDebugId(baseID + AppsModule.Ids.APP_LAUNCH_VIEW);
    }

    String getRestrictedCharRegEx() {
        return Format.substitute("[{0}]",
                                 RegExp.escapeCharacterClassSet(valConstants.restrictedCmdLineChars()
                                                                + " "));
    }

    JobExecution getJobExecution() {
        return factory.jobExecution().as();
    }

    @Override
    public void setViewDebugId(String baseID) {
        this.baseID = baseID;
    }

    @Override
    public void onAnalysisLaunchRequest(final AppTemplate at, final JobExecution je) {
        if(at.getAppType().equalsIgnoreCase(App.EXTERNAL_APP)
           && userSettings.isEnableWaitTimeMessage()) {
            showWaitTimeNotice(at, je);
        } else {
            launchAnalysis(at, je);
        }
    }

    @Override
    public void onCreateQuickLaunchRequest(AppTemplate appTemplate,
                                           JobExecution jobExecution) {
        this.appTemplate = appTemplate;
        this.jobExecution = jobExecution;
        props = new ReactQuickLaunch.CreateQLProps();
        props.presenter = this;
        props.appName = appTemplate.getName();
        props.dialogOpen = true;
        props.isOwner = userInfo.getEmail().equals(integratorsEmail);
        view.showOrHideCreateQuickLaunchView(props);
    }

    @Override
    public void onHideCreateQuickLaunchRequestDialog() {
        props.dialogOpen = false;
        view.showOrHideCreateQuickLaunchView(props);
    }

    @Override
    public void createQuickLaunch(String name,
                                  String description,
                                  boolean isDefault,
                                  boolean isPublic,
                                  ReactSuccessCallback callback,
                                  ReactErrorCallback errorCallback) {

    }

     void showWaitTimeNotice(final AppTemplate cleaned, final JobExecution je) {
        hpcWaitDlgProvider.get(new AsyncCallback<HPCWaitTimeDialog>() {
            @Override
            public void onFailure(Throwable throwable) {}

            @Override
            public void onSuccess(HPCWaitTimeDialog dialog) {
                dialog.addDialogHideHandler(hideEvent -> launchAnalysis(cleaned, je));
                dialog.show();
            }
        });
    }


    HandlerManager ensureHandlers() {
        return handlerManager == null ? handlerManager = createHandlerManager() : handlerManager;
    }

    private HandlerManager createHandlerManager() {
        return new HandlerManager(this);
    }

    void launchAnalysis(final AppTemplate at, final JobExecution je) {
        atServices.launchAnalysis(at, je, new AppLaunchCallback<AnalysisSubmissionResponse>() {

            @Override
            public void onFailure(Integer statusCode, Throwable caught) {
                announcer.schedule(new ErrorAnnouncementConfig(appearance.launchAnalysisFailure(je.getName())));
                ErrorHandler.post(appearance.analysisFailedToLaunch(at.getName()), caught);
                view.analysisLaunchFailed();
            }

            @Override
            public void onSuccess(AnalysisSubmissionResponse result) {
                announcer.schedule(new SuccessAnnouncementConfig(appearance.launchAnalysisSuccess(je.getName())));
                ensureHandlers().fireEvent(new AnalysisLaunchEvent(at));

                if (result != null) {
                    final List<String> missingPaths = result.getMissingPaths();
                    if (missingPaths != null && !missingPaths.isEmpty()) {
                        ErrorHandler.post(appearance.diskResourcesDoNotExist(Joiner.on(", ")
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
