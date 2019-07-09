package org.iplantc.de.theme.base.client.apps.widgets;

import org.iplantc.de.apps.widgets.client.view.AppLaunchView;
import org.iplantc.de.client.models.apps.integration.AppTemplate;
import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;
import org.iplantc.de.resources.client.messages.IplantErrorStrings;
import org.iplantc.de.resources.client.uiapps.widgets.AppsWidgetsDisplayMessages;
import org.iplantc.de.resources.client.uiapps.widgets.AppsWidgetsErrorMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

/**
 * @author aramsey
 */
public class AppLaunchViewDefaultAppearance implements AppLaunchView.AppLaunchViewAppearance {


    interface Style extends CssResource {
        String launchButtonPosition();
    }

    interface Resources extends IplantResources {
        @Source("AppLaunchView.gss")
        Style css();
    }

    private final IplantDisplayStrings iplantDisplayStrings;
    private AppsWidgetsErrorMessages appsWidgetsErrorMessages;
    private AppsWidgetsDisplayMessages appsWidgetsDisplayMessages;
    private AppLaunchViewDisplayStrings displayStrings;
    private final IplantErrorStrings errorStrings;
    private Style style;
    private final Resources res;

    public AppLaunchViewDefaultAppearance() {
        this(GWT.<Resources>create(Resources.class),
             GWT.<AppLaunchViewDisplayStrings> create(AppLaunchViewDisplayStrings.class),
             GWT.<IplantDisplayStrings> create(IplantDisplayStrings.class),
             GWT.<IplantErrorStrings> create(IplantErrorStrings.class),
             GWT.<AppsWidgetsErrorMessages>create(AppsWidgetsErrorMessages.class),
             GWT.<AppsWidgetsDisplayMessages>create(AppsWidgetsDisplayMessages.class));
    }

    public AppLaunchViewDefaultAppearance(Resources res,
                                          AppLaunchViewDisplayStrings displayStrings,
                                          IplantDisplayStrings iplantDisplayStrings,
                                          IplantErrorStrings errorStrings,
                                          AppsWidgetsErrorMessages appsWidgetsErrorMessages,
                                          AppsWidgetsDisplayMessages appsWidgetsDisplayMessages) {
        this.res = res;
        this.displayStrings = displayStrings;
        this.errorStrings = errorStrings;
        this.iplantDisplayStrings = iplantDisplayStrings;
        this.appsWidgetsErrorMessages = appsWidgetsErrorMessages;
        this.appsWidgetsDisplayMessages = appsWidgetsDisplayMessages;
        this.style = res.css();
        style.ensureInjected();
    }

    @Override
    public String launchButtonPositionClassName() {
        return style.launchButtonPosition();
    }

    @Override
    public SafeHtml quickLaunchDeleted() {
        return SafeHtmlUtils.fromTrustedString(displayStrings.quickLaunchDeleted());
    }

    @Override
    public String deprecatedAppMask() {
        return displayStrings.deprecatedAppMask();
    }

    @Override
    public String hpcAppWaitTimes() {
        return displayStrings.hpcAppWaitTimes();
    }

    @Override
    public String waitTimes() {
            return displayStrings.waitTimes();
    }

    @Override
    public String dontShow() {
        return displayStrings.dontShow();
    }

    @Override
    public String appUnavailable() {
        return iplantDisplayStrings.appUnavailable();
    }

    @Override
    public String unableToRetrieveWorkflowGuide() {
        return errorStrings.unableToRetrieveWorkflowGuide();
    }

    @Override
    public String loadingMask() {
        return iplantDisplayStrings.loadingMask();
    }

    @Override
    public String defaultAnalysisName() {
        return appsWidgetsDisplayMessages.defaultAnalysisName();
    }

    @Override
    public String launchAnalysisFailure(String name) {
        return appsWidgetsErrorMessages.launchAnalysisFailure(name);
    }

    @Override
    public String launchAnalysisSuccess(String name) {
        return appsWidgetsDisplayMessages.launchAnalysisSuccess(name);
    }

    @Override
    public String analysisFailedToLaunch(String name) {
        return errorStrings.analysisFailedToLaunch(name);
    }

    @Override
    public String diskResourcesDoNotExist(String missingPaths) {
        return errorStrings.diskResourcesDoNotExist(missingPaths);
    }

    @Override
    public String launchPreviewWidth() {
        return "640px";
    }

    @Override
    public String launchPreviewHeight() {
        return "375px";
    }

    @Override
    public String launchPreviewHeader(AppTemplate appTemplate) {
        return displayStrings.launchPreviewHeader(appTemplate.getName());
    }

    @Override
    public String launchAnalysis() {
        return iplantDisplayStrings.launchAnalysis();
    }

    @Override
    public String windowHeight() {
        return "375";
    }

    @Override
    public String windowWidth() {
        return "640";
    }

    @Override
    public int windowMinWidth() {
        return 375;
    }

    @Override
    public int windowMinHeight() {
        return 350;
    }

    @Override
    public String createQuickLaunch() {
        return iplantDisplayStrings.createQuickLaunch();
    }

    @Override
    public String createQuickLaunchSuccess(String name) {
        return displayStrings.createQuickLaunchSuccess(name);
    }
}
