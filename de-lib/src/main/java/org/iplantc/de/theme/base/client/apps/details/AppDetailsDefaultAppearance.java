package org.iplantc.de.theme.base.client.apps.details;

import org.iplantc.de.apps.client.AppDetailsView;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;
import org.iplantc.de.theme.base.client.apps.AppSearchHighlightAppearance;
import org.iplantc.de.theme.base.client.apps.AppsMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;

/**
 * @author jstroot
 */
public class AppDetailsDefaultAppearance implements AppDetailsView.AppDetailsAppearance {


    private final AppsMessages appsMessages;
    private final IplantDisplayStrings iplantDisplayStrings;
    private final AppSearchHighlightAppearance highlightAppearance;

    public AppDetailsDefaultAppearance() {
        this(GWT.<AppsMessages> create(AppsMessages.class),
             GWT.<IplantDisplayStrings> create(IplantDisplayStrings.class),
             GWT.<AppSearchHighlightAppearance> create(AppSearchHighlightAppearance.class));
    }

    AppDetailsDefaultAppearance(final AppsMessages appsMessages,
                                final IplantDisplayStrings iplantDisplayStrings,
                                final AppSearchHighlightAppearance highlightAppearance) {
        this.appsMessages = appsMessages;
        this.iplantDisplayStrings = iplantDisplayStrings;
        this.highlightAppearance = highlightAppearance;
    }

    @Override
    public SafeHtml getAppDocError(Throwable caught) {
        return appsMessages.getAppDocError(caught.getMessage());
    }


    @Override
    public SafeHtml saveAppDocError(Throwable caught) {
        return appsMessages.saveAppDocError(caught.getMessage());
    }

    @Override
    public String getQuickLaunchesError() {
        return appsMessages.getQuickLaunchesError();
    }
}

