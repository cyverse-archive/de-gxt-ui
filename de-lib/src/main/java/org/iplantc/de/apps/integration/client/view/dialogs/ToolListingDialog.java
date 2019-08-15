/**
 *
 */
package org.iplantc.de.apps.integration.client.view.dialogs;

import static org.iplantc.de.apps.integration.shared.AppIntegrationModule.Ids.INSTALLED_TOOLS_DLG;
import static org.iplantc.de.apps.integration.shared.AppIntegrationModule.Ids.OK;

import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.tools.client.events.ToolSelectionChangedEvent;
import org.iplantc.de.tools.client.views.manage.ManageToolsView;

import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;

/**
 * @author sriram
 */
public class ToolListingDialog extends IPlantDialog implements ToolSelectionChangedEvent.ToolSelectionChangedEventHandler {


    public interface ToolsListingViewAppearance {
        String loadingMask();

        String searchEmptyText();

        int dcListingDialogWidth();

        int dcListingDialogHeight();

        String dcListingDialogHeading();
    }


    private ToolsListingViewAppearance appearance;

    ManageToolsView.Presenter toolsPresenter;

    Tool selectedTool;

    @Inject
    ToolListingDialog(ManageToolsView.Presenter toolsPresenter,
                      ToolsListingViewAppearance appearance) {
        this.appearance = appearance;
        this.toolsPresenter = toolsPresenter;
        setPixelSize(appearance.dcListingDialogWidth(), appearance.dcListingDialogHeight());
        setResizable(false);
        setModal(true);
        setHeading(appearance.dcListingDialogHeading());
        setHideOnButtonClick(false);
        getOkButton().setEnabled(false);
        getButton(PredefinedButton.CANCEL).addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
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
        toolsPresenter.go(this);
        toolsPresenter.addToolSelectionChangedEventHandler(this);
    }

    public Tool getSelectedTool() {
        return selectedTool;
    }

    @Override
    public void onToolSelectionChanged(ToolSelectionChangedEvent event) {
        getOkButton().enable();
        selectedTool = event.getTool();
    }

    @Override
    public void show() {
        super.show();
        ensureDebugId(INSTALLED_TOOLS_DLG);
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        toolsPresenter.setViewDebugId(baseID);
    }
}
