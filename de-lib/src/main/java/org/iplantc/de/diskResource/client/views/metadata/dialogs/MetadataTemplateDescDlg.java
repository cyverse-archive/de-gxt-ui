package org.iplantc.de.diskResource.client.views.metadata.dialogs;

import org.iplantc.de.client.models.diskResources.MetadataTemplateInfo;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.diskResource.client.views.metadata.cells.TemplateInfoCell;
import org.iplantc.de.diskResource.share.DiskResourceModule;

import com.google.gwt.user.client.ui.HTML;
import com.google.inject.Inject;

/**
 * A dialog that displays when the user clicks the "i" button on a metadata template
 */
public class MetadataTemplateDescDlg extends IPlantDialog {

    private TemplateInfoCell.TemplateInfoCellAppearance appearance;

    @Inject
    public MetadataTemplateDescDlg(TemplateInfoCell.TemplateInfoCellAppearance appearance) {
        this.appearance = appearance;
        setSize(appearance.descriptionWidth(), appearance.descriptionHeight());
        setHideOnButtonClick(true);
        setHeading(appearance.description());
        setPredefinedButtons(PredefinedButton.CANCEL);
    }

    /**
     * Displays the MetadataTemplateDescDlg with the specified template info's description
     * @param info
     */
    public void show(MetadataTemplateInfo info) {
        HTML desc = new HTML(info.getDescription());
        desc.setStylePrimaryName(appearance.background());
        add(desc);

        super.show();

        ensureDebugId(DiskResourceModule.Ids.METADATA_DESC_DLG);
    }

    @Override
    public void show() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("This method is not supported.  Use show(MetadataTemplateInfo) instead.");
    }
}
