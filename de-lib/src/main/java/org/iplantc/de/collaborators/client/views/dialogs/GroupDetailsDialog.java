package org.iplantc.de.collaborators.client.views.dialogs;

import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.collaborators.client.GroupView;
import org.iplantc.de.collaborators.client.events.SaveGroupSelected;
import org.iplantc.de.collaborators.client.views.GroupDetailsView;
import org.iplantc.de.collaborators.shared.CollaboratorsModule;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

/**
 * @author aramsey
 */
public class GroupDetailsDialog extends IPlantDialog implements SaveGroupSelected.HasSaveGroupSelectedHandlers {

    GroupDetailsView view;
    GroupView.GroupViewAppearance appearance;

    @Inject
    public GroupDetailsDialog(GroupDetailsView view,
                              GroupView.GroupViewAppearance appearance) {
        this.view = view;
        this.appearance = appearance;

        setResizable(true);
        setPixelSize(appearance.groupDetailsWidth(), appearance.groupDetailsHeight());
        setOnEsc(false);

        add(view);

        addOkButtonSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                fireEvent(new SaveGroupSelected(view.getGroup(), view.getCollaborators()));
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
    public HandlerRegistration addSaveGroupSelectedHandler(SaveGroupSelected.SaveGroupSelectedHandler handler) {
        return addHandler(handler, SaveGroupSelected.TYPE);
    }
}
