package org.iplantc.de.theme.base.client.apps.widgets;

import org.iplantc.de.apps.widgets.client.view.AppLaunchView;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;
import org.iplantc.de.resources.client.messages.IplantErrorStrings;

import com.google.gwt.core.client.GWT;

/**
 * @author aramsey
 */
public class AppLaunchViewDefaultAppearance implements AppLaunchView.AppLaunchViewAppearance {

    private final IplantDisplayStrings iplantDisplayStrings;
    private AppLaunchViewDisplayStrings displayStrings;
    private final IplantErrorStrings errorStrings;

    public AppLaunchViewDefaultAppearance() {
        this(GWT.<AppLaunchViewDisplayStrings> create(AppLaunchViewDisplayStrings.class),
             GWT.<IplantDisplayStrings> create(IplantDisplayStrings.class),
             GWT.<IplantErrorStrings> create(IplantErrorStrings.class));
    }

    public AppLaunchViewDefaultAppearance(AppLaunchViewDisplayStrings displayStrings,
                                          IplantDisplayStrings iplantDisplayStrings,
                                          IplantErrorStrings errorStrings) {
        this.displayStrings = displayStrings;
        this.errorStrings = errorStrings;
        this.iplantDisplayStrings = iplantDisplayStrings;
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
}
