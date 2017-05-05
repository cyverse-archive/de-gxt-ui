package org.iplantc.de.tools.client.views.manage;

import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;

import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.event.SelectEvent;

/**
 * Created by sriram on 4/27/17.
 */
public class EditToolDialog extends IPlantDialog {

    private final EditToolView.EditToolViewAppearance appearance;
    private final ManageToolsView.Presenter presenter;

    @Inject
    EditToolView editToolView;

    @Inject
    public EditToolDialog(ManageToolsView.Presenter presenter, EditToolView.EditToolViewAppearance appearance) {
       this.appearance = appearance;
       this.presenter  = presenter;
       setHeading(appearance.create());
       setHideOnButtonClick(false);
       getOkButton().addSelectHandler(new SelectEvent.SelectHandler() {
           @Override
           public void onSelect(SelectEvent event) {
               Tool t = editToolView.getTool();
               presenter.addTool(t);
           }
       });
       getButton(PredefinedButton.CANCEL).addSelectHandler(new SelectEvent.SelectHandler() {
           @Override
           public void onSelect(SelectEvent event) {
               EditToolDialog.this.hide();
           }
       });

    }

    @Override
    public void show() {
        add(editToolView.asWidget());
        super.show();
    }
}
