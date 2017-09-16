package org.iplantc.de.theme.base.client.admin.metadata;

import org.iplantc.de.admin.desktop.client.metadata.view.EditMetadataTemplateView.EditMetadataTemplateViewAppearance;
import org.iplantc.de.resources.client.IplantResources;
import org.iplantc.de.resources.client.messages.IplantDisplayStrings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;

public class DefaultEditMetadataTemplateViewAppearance implements EditMetadataTemplateViewAppearance {

    private final IplantDisplayStrings iplantDisplayStrings;
    private final MetadataDisplayStrings displayStrings;
    private final IplantResources iplantResources;

    public DefaultEditMetadataTemplateViewAppearance() {
        this(GWT.<IplantDisplayStrings> create(IplantDisplayStrings.class),
             GWT.<MetadataDisplayStrings> create(MetadataDisplayStrings.class),
             GWT.<IplantResources> create(IplantResources.class));
    }

    public DefaultEditMetadataTemplateViewAppearance(final IplantDisplayStrings iplantDisplayStrings,
                                                     final MetadataDisplayStrings displayStrings,
                                                     final IplantResources iplantResources) {
        this.iplantDisplayStrings = iplantDisplayStrings;
        this.displayStrings = displayStrings;
        this.iplantResources = iplantResources;
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
    public String valColumn() {
        return displayStrings.valColumn();
    }

    @Override
    public String defColumn() {
        return displayStrings.defColumn();
    }

    @Override
    public String addBtn() {
        return iplantDisplayStrings.add();
    }

    @Override
    public String delBtn() {
        return iplantDisplayStrings.delete();
    }

    @Override
    public String enumError() {
        return displayStrings.enumError();
    }

    @Override
    public String enumValueOrValues() {
        return displayStrings.enumValueOrValues();
    }

    @Override
    public String editOLSQueryParamsDialogHeader() {
        return displayStrings.editOLSQueryParamsDialogHeader();
    }

    @Override
    public String editOLSQueryParamsDialogHeight() {
        return "480px";
    }

    @Override
    public String editOLSQueryParamsDialogWidth() {
        return "640px";
    }

    @Override
    public String allChildrenFieldLabel() {
        return displayStrings.allChildrenFieldLabel();
    }

    @Override
    public String allChildrenHelpLabel() {
        return displayStrings.allChildrenHelpLabel();
    }

    @Override
    public String childrenFieldLabel() {
        return displayStrings.childrenFieldLabel();
    }

    @Override
    public String childrenHelpLabel() {
        return displayStrings.childrenHelpLabel();
    }

    @Override
    public String entityTypeHelpLabel() {
        return displayStrings.entityTypeHelpLabel();
    }

    @Override
    public String iriColumnLabel() {
        return displayStrings.iriColumnLabel();
    }

    @Override
    public String ontologiesColumnHeader() {
        return displayStrings.ontologiesColumnHeader();
    }

    @Override
    public String ontologiesFieldLabel() {
        return displayStrings.ontologiesFieldLabel();
    }

    @Override
    public String ontologiesHelpLabel() {
        return displayStrings.ontologiesHelpLabel();
    }

    @Override
    public String querySettings() {
        return displayStrings.querySettings();
    }

    @Override
    public String valueOrValues() {
        return iplantDisplayStrings.valueOrValues();
    }

    @Override
    public int tempNameMaxLength() {
        return 20;
    }

}
