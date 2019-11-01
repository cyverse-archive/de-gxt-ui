package org.iplantc.de.theme.base.client.admin.apps;

import org.iplantc.de.admin.apps.client.AppPublicationRequestView;
import org.iplantc.de.theme.base.client.admin.BelphegorDisplayStrings;

import com.google.gwt.core.client.GWT;

public class AppPublicationRequestDefaultAppearance implements AppPublicationRequestView.AppPublicationRequestAppearance {

    final BelphegorDisplayStrings displayStrings;

    public AppPublicationRequestDefaultAppearance() {
       this(GWT.<BelphegorDisplayStrings> create(BelphegorDisplayStrings.class));
    }

    public AppPublicationRequestDefaultAppearance(final BelphegorDisplayStrings displayStrings) {
        this.displayStrings = displayStrings;
    }
    @Override
    public String publicationRequestSuccess(String appName) {
        return displayStrings.publicationRequestSuccess(appName);
    }
}
