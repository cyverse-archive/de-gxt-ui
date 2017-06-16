package org.iplantc.de.collaborators.client.views.dialogs;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.collaborators.client.GroupDetailsView;
import org.iplantc.de.collaborators.client.GroupView;
import org.iplantc.de.collaborators.client.events.GroupSaved;
import org.iplantc.de.collaborators.shared.CollaboratorsModule;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

/**
 * A dialog for creating/editing a Collaborator List.
 * From this dialog, a user can update the details of a Collaborator List and add/remove members to it.
 * @author aramsey
 */
public class GroupDetailsDialog extends IPlantDialog implements GroupSaved.HasGroupSavedHandlers {

    GroupDetailsView.Presenter presenter;
    GroupView.GroupViewAppearance appearance;
    GroupDetailsView.MODE mode = GroupDetailsView.MODE.EDIT;

    @Inject
    public GroupDetailsDialog(GroupDetailsView.Presenter presenter,
                              GroupView.GroupViewAppearance appearance) {
        this.presenter = presenter;
        this.appearance = appearance;

        setResizable(true);
        setPixelSize(appearance.groupDetailsWidth(), appearance.groupDetailsHeight());
        setOnEsc(false);
        setHideOnButtonClick(false);

        addOkButtonSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                if (presenter.isViewValid()) {
                    presenter.saveGroupSelected();
                    hide();
                } else {
                    AlertMessageBox alertMsgBox =
                            new AlertMessageBox("Warning", appearance.completeRequiredFieldsError());
                    alertMsgBox.show();
                }
            }
        });

        addCancelButtonSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                hide();
            }
        });
    }

    /**
     * Used for displaying GroupDetailsView to edit an existing Group
     * @param group
     */
    public void show(Subject group) {
        presenter.go(this, group, mode);
        setHeading(appearance.groupDetailsHeading(group));
        super.show();

        ensureDebugId(CollaboratorsModule.Ids.GROUP_DETAILS_DLG);
    }

    /**
     * Used for displaying GroupDetailsView with a new Group
     */
    public void show() {
        mode = GroupDetailsView.MODE.ADD;
        show(null);
    }

    @Override
    public HandlerRegistration addGroupSavedHandler(GroupSaved.GroupSavedHandler handler) {
        return presenter.addGroupSavedHandler(handler);
    }

    @Override
    protected void onEnsureDebugId(String baseID) {
        super.onEnsureDebugId(baseID);

        presenter.setViewDebugId(baseID + CollaboratorsModule.Ids.GROUP_DETAILS_VIEW);
    }
}
