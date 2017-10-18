package org.iplantc.de.diskResource.client.views.dialogs;

import org.iplantc.de.client.models.HasPath;
import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.diskResource.client.GridView;
import org.iplantc.de.diskResource.client.gin.factory.DiskResourceSelectorFieldFactory;
import org.iplantc.de.diskResource.client.views.widgets.MultiFileSelectorField;
import org.iplantc.de.diskResource.share.DiskResourceModule;

import com.google.gwt.user.client.TakesValue;
import com.google.inject.Inject;

import java.util.List;

public class MetadataCopyDialog extends IPlantDialog implements TakesValue<List<HasPath>> {

    MultiFileSelectorField multiFileFolderSelector;
    final GridView.Presenter.Appearance appearance;
    DiskResource srcDr;

    @Inject
    public MetadataCopyDialog(final GridView.Presenter.Appearance appearance,
                              DiskResourceSelectorFieldFactory selectionFieldFactory) {
        this.appearance = appearance;
        multiFileFolderSelector = selectionFieldFactory.createMultiFileSelector(true, appearance.copyMetadataPrompt());
        add(multiFileFolderSelector);
        setHideOnButtonClick(false);
        setSize(appearance.copyMetadataDlgWidth(), appearance.copyMetadataDlgHeight());
        setModal(false);
    }

    @Override
    public void setValue(List<HasPath> value) {
        multiFileFolderSelector.setValue(value);
    }

    @Override
    public List<HasPath> getValue() {
        return multiFileFolderSelector.getValue();
    }

    public DiskResource getSource() {
        return srcDr;
    }

    public void show(DiskResource selected) {
        this.srcDr = selected;
        setHeading(appearance.copyMetadata(selected.getPath()));

        super.show();

        ensureDebugId(DiskResourceModule.Ids.METADATA_COPY_DLG);
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        getOkButton().ensureDebugId(baseID + DiskResourceModule.Ids.OK_BTN);
        multiFileFolderSelector.ensureDebugId(baseID);
    }
}
