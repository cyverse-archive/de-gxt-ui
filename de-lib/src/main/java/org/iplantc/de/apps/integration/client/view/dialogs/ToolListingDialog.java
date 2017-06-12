/**
 *
 */
package org.iplantc.de.apps.integration.client.view.dialogs;

import static org.iplantc.de.apps.integration.shared.AppIntegrationModule.Ids.CANCEL;
import static org.iplantc.de.apps.integration.shared.AppIntegrationModule.Ids.INSTALLED_TOOLS_DLG;
import static org.iplantc.de.apps.integration.shared.AppIntegrationModule.Ids.OK;

import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.tools.client.views.manage.ManageToolsView;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;

/**
 * @author sriram
 */
public class ToolListingDialog extends IPlantDialog implements SelectionChangedEvent.SelectionChangedHandler<Tool> {


    public interface ToolsListingViewAppearance {

        String nameColumnHeader();

        String versionColumnHeader();

        String pathColumnHeader();

        String attributionLabel();

        String descriptionLabel();

        String loadingMask();

        String searchEmptyText();

        SafeHtml detailsRenderer();

        String newToolReq();

        ImageResource add();

        String infoDialogWidth();

        String infoDialogHeight();

        int nameColumnWidth();

        int versionColumnWidth();

        int pathColumnWidth();

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
        getButton(PredefinedButton.CANCEL).ensureDebugId(INSTALLED_TOOLS_DLG + CANCEL);
        toolsPresenter.go(this);
        toolsPresenter.addSelectionChangedHandler(this);
    }

    public Tool getSelectedTool() {
        return selectedTool;
    }

    @Override
    public void onSelectionChanged(SelectionChangedEvent<Tool> event) {
        getOkButton().enable();
        selectedTool = event.getSelection().get(0);
    }
}
