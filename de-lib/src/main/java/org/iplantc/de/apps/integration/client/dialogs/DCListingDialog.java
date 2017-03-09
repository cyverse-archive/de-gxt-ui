/**
 *
 */
package org.iplantc.de.apps.integration.client.dialogs;

import static org.iplantc.de.apps.integration.shared.AppIntegrationModule.Ids.CANCEL;
import static org.iplantc.de.apps.integration.shared.AppIntegrationModule.Ids.INSTALLED_TOOLS_DLG;
import static org.iplantc.de.apps.integration.shared.AppIntegrationModule.Ids.OK;

import org.iplantc.de.apps.integration.client.view.tools.DeployedComponentsListingView;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;

import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

import java.util.List;

/**
 * @author sriram
 */
public class DCListingDialog extends IPlantDialog implements SelectionChangedHandler<Tool> {

    private Tool selectedComponent = null;

    @Inject
    DCListingDialog(DeployedComponentsListingView.Presenter toolsPresenter) {
        setPixelSize(600, 500);
        setResizable(false);
        setModal(true);
        setHeading("Installed Tools");
        setHideOnButtonClick(false);
        getOkButton().setEnabled(false);
        getButton(PredefinedButton.CANCEL).addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                selectedComponent = null;
                hide();
            }
        });
        getButton(PredefinedButton.OK).addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                hide();
            }
        });
        getButton(PredefinedButton.OK).ensureDebugId(INSTALLED_TOOLS_DLG + OK);
        getButton(PredefinedButton.CANCEL).ensureDebugId(INSTALLED_TOOLS_DLG + CANCEL);
        toolsPresenter.go(this);
        toolsPresenter.addSelectionChangedHandler(this);
    }

    public Tool getSelectedComponent() {
        return selectedComponent;
    }

    @Override
    public void onSelectionChanged(SelectionChangedEvent<Tool> event) {
        List<Tool> items = event.getSelection();
        if (items != null && items.size() > 0) {
            getButton(PredefinedButton.OK).setEnabled(true);
            selectedComponent = items.get(0);
        } else {
            getButton(PredefinedButton.OK).setEnabled(false);
            selectedComponent = null;
        }
    }
}
