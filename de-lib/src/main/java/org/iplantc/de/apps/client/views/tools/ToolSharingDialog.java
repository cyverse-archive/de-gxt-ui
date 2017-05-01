/**
 * @author sriram
 */

package org.iplantc.de.apps.client.views.tools;

import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;

import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.event.SelectEvent;

import java.util.List;

public class ToolSharingDialog extends IPlantDialog {

    @Inject
    ToolSharingView sharingView;

    @Inject
    ToolSharingPresenter presenter;
    
    @Inject
    ToolSharingDialog() {
        super(false);
        setPixelSize(600, 500);
        setHideOnButtonClick(true);
        setModal(true);
        setResizable(false);
        setHeading("Manage Sharing");
        setOkButtonText("Done");
        getOkButton().addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
              presenter.processRequest();
            }
        });
    }

    public void show(final List<Tool> resourcesToShare) {
        sharingView.setSelectedTools(resourcesToShare);
        super.show();
    }

    @Override
    public void show() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("This method is not supported for this class.");
    }

}
