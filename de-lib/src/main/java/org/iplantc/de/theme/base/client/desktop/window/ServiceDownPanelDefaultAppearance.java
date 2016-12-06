package org.iplantc.de.theme.base.client.desktop.window;

import org.iplantc.de.desktop.client.views.widgets.ServiceDownPanel;

import com.google.gwt.core.client.GWT;

/**
 * @author aramsey
 */
public class ServiceDownPanelDefaultAppearance implements ServiceDownPanel.ServiceDownPanelAppearance {

    private final ServiceDownPanelDisplayStrings displayStrings;

    public ServiceDownPanelDefaultAppearance() {
        this(GWT.<ServiceDownPanelDisplayStrings>create(ServiceDownPanelDisplayStrings.class));
    }

    public ServiceDownPanelDefaultAppearance(ServiceDownPanelDisplayStrings displayStrings) {
        this.displayStrings = displayStrings;
    }

    @Override
    public String serviceDownText() {
        return displayStrings.serviceDownText();
    }

    @Override
    public String retryBtnText() {
        return displayStrings.retryBtnText();
    }

    @Override
    public String loadingMask() {
        return displayStrings.loadingMask();
    }
}
