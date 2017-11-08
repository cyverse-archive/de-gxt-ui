package org.iplantc.de.diskResource.client.views.dialogs;

import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.commons.client.validators.DiskResourceNameValidator;
import org.iplantc.de.commons.client.views.dialogs.IPlantPromptDialog;
import org.iplantc.de.diskResource.client.events.selection.CreateNewFolderSelected;
import org.iplantc.de.diskResource.share.DiskResourceModule;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HTML;
import com.google.inject.Inject;

/**
 * @author jstroot
 */
public class CreateFolderDialog extends IPlantPromptDialog implements CreateNewFolderSelected.HasCreateNewFolderSelectedHandlers {

    public interface Appearance {

        String dialogWidth();

        String folderName();

        String newFolder();

        SafeHtml renderDestinationPathLabel(String destPath, String createIn);
    }

    private final Appearance appearance;

    private final DiskResourceUtil diskResourceUtil;


    @Inject
    public CreateFolderDialog(final Appearance appearance,
                              DiskResourceUtil diskResourceUtil) {
        super(appearance.folderName(), -1, "", new DiskResourceNameValidator());
        this.appearance = appearance;
        this.diskResourceUtil = diskResourceUtil;
        setWidth(appearance.dialogWidth());
        setHeading(appearance.newFolder());
    }

    public void show(Folder parentFolder) {
        String destPath = parentFolder.getPath();
        HTML htmlDestText = new HTML(appearance.renderDestinationPathLabel(destPath, diskResourceUtil.parseNameFromPath(destPath)));
        insert(htmlDestText, 0);

        super.show();
        ensureDebugId(DiskResourceModule.Ids.CREATE_FOLDER_DLG);
    }

    public String getFolderName() {
        return getFieldText();
    }

    @Override
    public void show() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("This method is not supported for this class. " +
                                                "Use show(Folder) instead.");
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        getTextField().ensureDebugId(baseID + DiskResourceModule.Ids.FOLDER_NAME);
        getButton(PredefinedButton.OK).ensureDebugId(baseID + DiskResourceModule.Ids.OK_BTN);
    }

    @Override
    public HandlerRegistration addCreateNewFolderSelectedHandler(CreateNewFolderSelected.CreateNewFolderSelectedHandler handler) {
        return addHandler(handler, CreateNewFolderSelected.TYPE);
    }
}
