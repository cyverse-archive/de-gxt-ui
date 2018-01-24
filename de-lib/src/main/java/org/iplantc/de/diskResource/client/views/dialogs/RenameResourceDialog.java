package org.iplantc.de.diskResource.client.views.dialogs;

import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.commons.client.validators.DiskResourceNameValidator;
import org.iplantc.de.commons.client.validators.DiskResourceSameNameValidator;
import org.iplantc.de.commons.client.views.dialogs.IPlantPromptDialog;
import org.iplantc.de.diskResource.client.DiskResourceView;
import org.iplantc.de.diskResource.share.DiskResourceModule;

import com.google.inject.Inject;

/**
 * A dialog used for renaming any kind of disk resource
 * @author aramsey
 */
public class RenameResourceDialog extends IPlantPromptDialog {

    private DiskResourceView.Presenter.Appearance displayStrings;

    @Inject
    public RenameResourceDialog(DiskResourceView.Presenter.Appearance displayStrings) {
        this.displayStrings = displayStrings;
        setMaxLength(-1);
        setHeading(displayStrings.rename());
    }

    public void show(DiskResource resource) {
        setInitialText(resource.getName());
        String labelText;
        if (resource instanceof Folder) {
            labelText = displayStrings.folderName();
        } else {
            labelText = displayStrings.fileName();
        }
        setFieldLabelText(labelText);
        addValidator(new DiskResourceSameNameValidator(resource));
        addValidator(new DiskResourceNameValidator());

        super.show();

        ensureDebugId(DiskResourceModule.Ids.RENAME_RESOURCE_DLG);
    }

    @Override
    public void show() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("This method is not supported for this class. "
                                                + "Use show(DiskResource) instead.");
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        getTextField().ensureDebugId(baseID + DiskResourceModule.Ids.TEXT_FIELD);
        getButton(PredefinedButton.OK).ensureDebugId(baseID + DiskResourceModule.Ids.OK_BTN);

    }
}
