package org.iplantc.de.collaborators.client.views.dialogs;

import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.GroupAutoBeanFactory;
import org.iplantc.de.collaborators.client.GroupView;
import org.iplantc.de.collaborators.client.events.SaveGroupSelected;
import org.iplantc.de.collaborators.client.views.GroupDetailsView;
import org.iplantc.de.collaborators.shared.CollaboratorsModule;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

/**
 * @author aramsey
 */
public class GroupDetailsDialog extends IPlantDialog implements SaveGroupSelected.HasSaveGroupSelectedHandlers {

    GroupDetailsView view;
    GroupView.GroupViewAppearance appearance;
    private GroupAutoBeanFactory factory;

    @Inject
    public GroupDetailsDialog(GroupDetailsView view,
                              GroupView.GroupViewAppearance appearance,
                              GroupAutoBeanFactory factory) {
        this.view = view;
        this.appearance = appearance;
        this.factory = factory;

        setResizable(true);
        setPixelSize(appearance.groupDetailsWidth(), appearance.groupDetailsHeight());
        setOnEsc(false);
        setHideOnButtonClick(false);

        add(view);

        addOkButtonSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                if (view.isValid()) {
                    fireEvent(new SaveGroupSelected(view.getGroup(), view.getCollaborators()));
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

        addHideHandler(new HideEvent.HideHandler() {
            @Override
            public void onHide(HideEvent event) {
                view.clearHandlers();
            }
        });
    }

    public void show(Group group) {
        view.edit(group);
        setHeading(appearance.groupDetailsHeading(group));
        super.show();

        ensureDebugId(CollaboratorsModule.Ids.GROUP_DETAILS_DLG);
    }

    @Override
    public void show() {
        Group group = factory.getGroup().as();
        show(group);
    }

    @Override
    public HandlerRegistration addSaveGroupSelectedHandler(SaveGroupSelected.SaveGroupSelectedHandler handler) {
        return addHandler(handler, SaveGroupSelected.TYPE);
    }
}
