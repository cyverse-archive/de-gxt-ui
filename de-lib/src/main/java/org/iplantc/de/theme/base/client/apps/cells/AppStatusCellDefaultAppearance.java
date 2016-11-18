package org.iplantc.de.theme.base.client.apps.cells;

import org.iplantc.de.apps.client.views.list.cells.AppStatusCell;
import org.iplantc.de.client.models.apps.App;
import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;
import org.iplantc.de.theme.base.client.apps.AppsMessages;

import com.google.common.base.Strings;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeUri;

/**
 * @author jstroot
 */
public class AppStatusCellDefaultAppearance implements AppStatusCell.AppStatusCellAppearance {

    interface MyCss extends CssResource {
        @ClassName("app_info")
        String appRun();
    }

    interface Resources extends ClientBundle {
        @Source("AppInfoCell.css")
        MyCss css();
    }

    interface Templates extends SafeHtmlTemplates {

        @Template("<img qtip='{1}' src='{0}'/>")
        SafeHtml cell(SafeUri img, String toolTip);

        @Template("<img id='{2}' qtip='{1}' src='{0}'/>")
        SafeHtml debugCell(SafeUri img, String toolTip, String debugId);
    }

    private final Templates templates;
    private final Resources resources;
    private final AppsMessages appsMessages;
    private final IplantResources iplantResources;
    private IplantDisplayStrings iplantDisplayStrings;


    public AppStatusCellDefaultAppearance() {
        this(GWT.<Templates> create(Templates.class),
             GWT.<Resources> create(Resources.class),
             GWT.<AppsMessages> create(AppsMessages.class),
             GWT.<IplantResources> create(IplantResources.class),
             GWT.<IplantDisplayStrings> create(IplantDisplayStrings.class));
    }

    AppStatusCellDefaultAppearance(final Templates templates,
                                   final Resources resources,
                                   final AppsMessages appsMessages,
                                   final IplantResources iplantResources,
                                   IplantDisplayStrings iplantDisplayStrings) {
        this.templates = templates;
        this.resources = resources;
        this.appsMessages = appsMessages;
        this.iplantResources = iplantResources;
        this.iplantDisplayStrings = iplantDisplayStrings;
        this.resources.css().ensureInjected();
    }

    @Override
    public void render(Cell.Context context, App value, SafeHtmlBuilder sb, String debugId) {
        String tooltip;
        final SafeUri safeUri;
        if (value.isDisabled()) {
            safeUri = iplantResources.xred().getSafeUri();
            tooltip = iplantDisplayStrings.appUnavailable();
        } else if (value.isBeta() != null && value.isBeta()) {
            safeUri = iplantResources.beta().getSafeUri();
            tooltip = appsMessages.betaToolTip();
        } else {
            return;
        }

        if (!Strings.isNullOrEmpty(debugId)) {
            sb.append(templates.debugCell(safeUri, tooltip, debugId));
        } else {
            sb.append(templates.cell(safeUri, tooltip));
        }
    }
}
