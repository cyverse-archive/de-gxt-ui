package org.iplantc.de.tools.client.views.dialogs;

import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.tools.client.views.manage.EditToolView;
import org.iplantc.de.tools.client.views.manage.ManageToolsView;
import org.iplantc.de.tools.shared.ToolsModule;

import com.google.common.base.Strings;
import com.google.gwt.user.client.Command;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.event.SelectEvent;

/**
 * Created by sriram on 4/27/17.
 */
public class EditToolDialog extends IPlantDialog {

    private final EditToolView.EditToolViewAppearance appearance;
    private ManageToolsView.Presenter presenter;

    @Inject
    EditToolView editToolView;

    @Inject
    public EditToolDialog(EditToolView.EditToolViewAppearance appearance) {
       this.appearance = appearance;
       setHeading(appearance.create());
       setHideOnButtonClick(false);
       getOkButton().addSelectHandler(new SelectEvent.SelectHandler() {
           @Override
           public void onSelect(SelectEvent event) {
               Tool t = editToolView.getTool();
               if(Strings.isNullOrEmpty(t.getId())) {
                   t.setId(null); //remove id field from splittable
                   presenter.addTool(t, new DialogCallbackCommand());
               } else {
                   presenter.updateTool(t, new DialogCallbackCommand());
               }
           }
       });
       getButton(PredefinedButton.CANCEL).addSelectHandler(new SelectEvent.SelectHandler() {
           @Override
           public void onSelect(SelectEvent event) {
               EditToolDialog.this.hide();
           }
       });

    }

    public void editTool(Tool t) {
        editToolView.editTool(t);
    }

    public void show(ManageToolsView.Presenter presenter) {
        this.presenter = presenter;
        add(editToolView);
        super.show();
        ensureDebugId(ToolsModule.EditToolIds.EDIT_DIALOG);
    }

    public void show() {
        throw new UnsupportedOperationException("Method not supported!");
    }

    private class DialogCallbackCommand implements Command {

        @Override
        public void execute() {
            EditToolDialog.this.hide();
        }
    }
    
    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);
        editToolView.asWidget().ensureDebugId(baseID + ToolsModule.EditToolIds.EDIT_VIEW);
    }

}
