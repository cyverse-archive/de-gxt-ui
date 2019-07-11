package org.iplantc.de.theme.base.client.admin.toolAdmin;

import org.iplantc.de.admin.desktop.client.toolAdmin.ToolAdminView;
import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SafeUri;

import com.sencha.gxt.core.client.XTemplates;

/**
 * @author aramsey
 */
public class ToolAdminViewDefaultAppearance implements ToolAdminView.ToolAdminViewAppearance {

    interface HelpTemplates extends SafeHtmlTemplates {
        @SafeHtmlTemplates.Template("{0}<img style='float: right;' src='{1}' qtip='{2}'></img>")
        SafeHtml fieldLabelImgFloatRight(SafeHtml label, SafeUri img, String toolTip);
    }

    private final ToolAdminDisplayStrings displayStrings;
    private final IplantResources iplantResources;
    private final HelpTemplates helpTemplates;


    public ToolAdminViewDefaultAppearance() {
        this(GWT.<ToolAdminDisplayStrings>create(ToolAdminDisplayStrings.class),
             GWT.<IplantResources>create(IplantResources.class),
             GWT.<HelpTemplates> create(HelpTemplates.class));

    }

    ToolAdminViewDefaultAppearance(final ToolAdminDisplayStrings displayStrings,
                                   final IplantResources iplantResources,
                                   HelpTemplates helpTemplates) {
        this.displayStrings = displayStrings;
        this.iplantResources = iplantResources;
        this.helpTemplates = helpTemplates;
    }

    SafeHtml getContextualHelp (String labelText, String helpText) {
        return helpTemplates.fieldLabelImgFloatRight(SafeHtmlUtils.fromString(labelText), iplantResources.help().getSafeUri(), helpText);
    }

    @Override
    public String add() {
        return displayStrings.add();
    }

    @Override
    public String filter() {
        return displayStrings.filter();
    }

    @Override
    public ImageResource addIcon() {
        return iplantResources.add();
    }

    @Override
    public ImageResource deleteIcon() {
        return iplantResources.delete();
    }

    @Override
    public String nameColumnLabel() {
        return displayStrings.nameColumnLabel();
    }

    @Override
    public int nameColumnWidth() {
        return 90;
    }

    @Override
    public String descriptionColumnLabel() {
        return displayStrings.descriptionColumnLabel();
    }

    @Override
    public int descriptionColumnWidth() {
        return 150;
    }

    @Override
    public String attributionColumnLabel() {
        return displayStrings.attributionColumnLabel();
    }

    @Override
    public int attributionColumnWidth() {
        return 90;
    }

    @Override
    public String locationColumnInfoLabel() {
        return displayStrings.locationColumnInfoLabel();
    }

    @Override
    public int locationColumnInfoWidth() {
        return 90;
    }

    @Override
    public String versionColumnInfoLabel() {
        return displayStrings.versionColumnInfoLabel();
    }

    @Override
    public int versionColumnInfoWidth() {
        return 50;
    }

    @Override
    public String typeColumnInfoLabel() {
        return displayStrings.typeColumnInfoLabel();
    }

    @Override
    public int typeColumnInfoWidth() {
        return 50;
    }

    @Override
    public String addToolSuccessText() {
        return displayStrings.addToolSuccessText();
    }

    @Override
    public String updateToolSuccessText() {
        return displayStrings.updateToolSuccessText();
    }

    @Override
    public String deleteBtnText() {
        return displayStrings.deleteBtnText();
    }

    @Override
    public String deleteToolSuccessText() {
        return displayStrings.deleteToolSuccessText();
    }

    @Override
    public String confirmOverwriteTitle() {
        return displayStrings.confirmOverwriteTitle();
    }

    @Override
    public String confirmOverwriteDangerZone() {
        return displayStrings.confirmOverwriteDangerZone();
    }

    @Override
    public String confirmOverwriteBody() {
        return displayStrings.confirmOverwriteBody();
    }

    @Override
    public String deletePublicToolTitle() {
        return displayStrings.deletePublicToolTitle();
    }

    @Override
    public String deletePublicToolBody() {
        return displayStrings.deletePublicToolBody();
    }

    @Override
    public int publicAppNameColumnWidth() {
        return 200;
    }

    @Override
    public String publicAppNameLabel() {
        return displayStrings.publicAppNameLabel();
    }

    @Override
    public int publicAppIntegratorColumnWidth() {
        return 100;
    }

    @Override
    public String publicAppIntegratorLabel() {
        return displayStrings.publicAppIntegratorLabel();
    }

    @Override
    public int publicAppIntegratorEmailColumnWidth() {
        return 200;
    }

    @Override
    public String publicAppIntegratorEmailLabel() {
        return displayStrings.publicAppIntegratorEmailLabel();
    }

    @Override
    public int publicAppDisabledColumnWidth() {
        return 100;
    }

    @Override
    public String publicAppDisabledLabel() {
        return displayStrings.publicAppDisabledLabel();
    }
}
