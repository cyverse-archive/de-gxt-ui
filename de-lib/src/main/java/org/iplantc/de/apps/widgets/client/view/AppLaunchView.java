package org.iplantc.de.apps.widgets.client.view;

import org.iplantc.de.apps.widgets.client.ReactQuickLaunch;
import org.iplantc.de.apps.widgets.client.events.AnalysisLaunchEvent.AnalysisLaunchEventHandler;
import org.iplantc.de.apps.widgets.client.events.AppTemplateFetched;
import org.iplantc.de.apps.widgets.client.events.CreateQuickLaunchEvent;
import org.iplantc.de.apps.widgets.client.events.RequestAnalysisLaunchEvent.HasRequestAnalysisLaunchHandlers;
import org.iplantc.de.client.models.apps.integration.AppTemplate;
import org.iplantc.de.client.models.apps.integration.JobExecution;
import org.iplantc.de.client.services.callbacks.ReactErrorCallback;
import org.iplantc.de.client.services.callbacks.ReactSuccessCallback;
import org.iplantc.de.commons.client.views.window.configs.AppWizardConfig;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsType;

/**
 * The interface definition for the App Wizard view.
 * 
 * The App wizard is an editor which is data bound to a <code>AppTemplate</code>.
 * 
 * XXX Research ways to lazy load the view.
 * 
 * @author jstroot
 *
 */

@JsType
public interface AppLaunchView extends IsWidget,
                                       Editor<AppTemplate>,
                                       HasRequestAnalysisLaunchHandlers,
                                       CreateQuickLaunchEvent.HasCreateQuickLaunchEventHandlers {

    interface AppLaunchViewAppearance {
        String deprecatedAppMask();

        String hpcAppWaitTimes();

        String waitTimes();

        String dontShow();

        String appUnavailable();

        String unableToRetrieveWorkflowGuide();

        String loadingMask();

        String defaultAnalysisName();

        String launchAnalysisFailure(String name);

        String launchAnalysisSuccess(String name);

        String analysisFailedToLaunch(String name);

        String diskResourcesDoNotExist(String missingPaths);

        String launchPreviewWidth();

        String launchPreviewHeight();

        String launchPreviewHeader(AppTemplate appTemplate);

        String launchAnalysis();

        String windowHeight();

        String windowWidth();

        int windowMinWidth();

        int windowMinHeight();

        String createQuickLaunch();

        String createQuickLaunchSuccess(String name);

        String launchButtonPositionClassName();
    }

    @JsType
    interface Presenter extends AppTemplateFetched.HasAppTemplateFetchedHandlers {

        @JsIgnore
        AppTemplate getAppTemplate();

        void setViewDebugId(String baseID);

        @JsIgnore
        void addAnalysisLaunchHandler(AnalysisLaunchEventHandler handler);

        @JsIgnore
        void go(HasOneWidget container, AppWizardConfig config);

        void onHideCreateQuickLaunchRequestDialog();

        void createQuickLaunch(String name,
                               String description,
                               boolean isPublic,
                               ReactSuccessCallback callback,
                               ReactErrorCallback errorCallback);
    }

    interface RenameWindowHeaderCommand extends Command {
        void setAppTemplate(AppTemplate appTemplate);
    }

    @JsIgnore
    void analysisLaunchFailed();

    @JsIgnore
    void edit(AppTemplate appTemplate, JobExecution je);

    @JsIgnore
    void showOrHideCreateQuickLaunchView(ReactQuickLaunch.CreateQLProps props);
}
