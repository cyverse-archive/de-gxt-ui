/**
 * @author sriram
 */

package org.iplantc.de.tools.client.views.dialogs;

import org.iplantc.de.tools.client.gin.factory.ToolSharingPresenterFactory;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.tools.client.views.manage.ToolSharingPresenter;

import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.event.SelectEvent;

import java.util.List;

public class ToolSharingDialog extends IPlantDialog {


    ToolSharingPresenter presenter;
    ToolSharingPresenterFactory factory;
    
    @Inject
    ToolSharingDialog(ToolSharingPresenterFactory factory) {
        super(false);
        this.factory = factory;
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
        presenter = factory.create(resourcesToShare);
        presenter.go(this);
        super.show();
    }

    @Override
    public void show() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("This method is not supported for this class. ");
    }

}
