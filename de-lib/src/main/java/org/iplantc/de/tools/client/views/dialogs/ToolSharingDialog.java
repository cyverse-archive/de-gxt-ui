/**
 * @author sriram
 */

package org.iplantc.de.tools.client.views.dialogs;

import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.tools.client.gin.factory.ToolSharingPresenterFactory;
import org.iplantc.de.tools.client.views.manage.ManageToolsView;
import org.iplantc.de.tools.client.views.manage.ToolSharingPresenter;
import org.iplantc.de.tools.shared.ToolsModule;

import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.event.SelectEvent;

import java.util.List;

public class ToolSharingDialog extends IPlantDialog {


    ToolSharingPresenter presenter;
    ToolSharingPresenterFactory factory;
    ManageToolsView.ManageToolsViewAppearance appearance;
    
    @Inject
    ToolSharingDialog(ToolSharingPresenterFactory factory, ManageToolsView.ManageToolsViewAppearance appearance) {
        super(false);
        this.factory = factory;
        this.appearance = appearance;
        setPixelSize(appearance.sharingDialogWidth(), appearance.sharingDialogHeight());
        setHideOnButtonClick(true);
        setModal(true);
        setResizable(false);
        setHeading(appearance.manageSharing());
        setOkButtonText(appearance.done());
        getOkButton().addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
              presenter.processRequest();
            }
        });
    }

    public void show(final List<Tool> resourcesToShare) {
        presenter = factory.create(resourcesToShare);
        presenter.go(this);
        super.show();

        onEnsureDebugId(ToolsModule.ToolIds.SHARING_DLG);
    }

    @Override
    public void show() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("This method is not supported for this class. ");
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        presenter.setViewDebugId(baseID);
        getOkButton().ensureDebugId(baseID + ToolsModule.ToolIds.DONE_BTN);
    }
}
