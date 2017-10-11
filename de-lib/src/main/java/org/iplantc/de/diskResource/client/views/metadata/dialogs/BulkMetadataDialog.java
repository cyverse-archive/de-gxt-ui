package org.iplantc.de.diskResource.client.views.metadata.dialogs;

import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.diskResource.client.BulkMetadataView;
import org.iplantc.de.diskResource.client.gin.factory.BulkMetadataViewFactory;

import com.google.inject.Inject;

public class BulkMetadataDialog extends IPlantDialog {

    private final BulkMetadataView.BulkMetadataViewAppearance appearance;
    private BulkMetadataViewFactory factory;
    private BulkMetadataView view;

    @Inject
    public BulkMetadataDialog(BulkMetadataView.BulkMetadataViewAppearance appearance,
                              BulkMetadataViewFactory factory) {
        this.appearance = appearance;
        this.factory = factory;
        setHeading(appearance.heading());
        setSize(appearance.dialogWidth(), appearance.dialogHeight());
        setHideOnButtonClick(false);
        setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
        setModal(true);
    }

    public void show(BulkMetadataView.BULK_MODE mode) {
        this.view = factory.create(mode);
        setWidget(view);

        super.show();
    }

    public boolean isValid() {
        return view.isValid();
    }

    public String getSelectedPath() {
        return view.getSelectedPath();
    }
}
