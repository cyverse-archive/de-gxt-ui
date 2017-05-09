package org.iplantc.de.collaborators.client.views.cells;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;

import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.collaborators.client.events.GroupNameSelected;
import org.iplantc.de.collaborators.shared.CollaboratorsModule;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Event;

/**
 * A clickable cell containing the Collaborator List name that will open up the GroupDetailsDialog
 * @author aramsey
 */
public class GroupNameCell extends AbstractCell<Group> implements GroupNameSelected.HasGroupNameSelectedHandlers {

    public interface GroupNameCellAppearance {
        String CLICKABLE_ELEMENT_NAME = "groupName";

        void render(SafeHtmlBuilder safeHtmlBuilder, Group group, String debugID);
    }

    private GroupNameCellAppearance appearance = GWT.create(GroupNameCellAppearance.class);
    private String baseDebugId;
    private HandlerManager handlerManager;

    public GroupNameCell() {
        super(CLICK);
    }

    @Override
    public void onBrowserEvent(Context context,
                               Element parent,
                               Group value,
                               NativeEvent event,
                               ValueUpdater<Group> valueUpdater) {
        if (value == null) {
            return;
        }

        Element eventTargetElement = Element.as(event.getEventTarget());
        if ((Event.as(event).getTypeInt() == Event.ONCLICK)
            && eventTargetElement.getAttribute("name").equalsIgnoreCase(GroupNameCellAppearance.CLICKABLE_ELEMENT_NAME)) {
            ensureHandlers().fireEvent(new GroupNameSelected(value));
        }
    }

    @Override
    public void render(Context context, Group value, SafeHtmlBuilder sb) {
        String debugID = baseDebugId + "." + value.getId() + CollaboratorsModule.Ids.GROUP_NAME_CELL;
        appearance.render(sb, value, debugID);
    }

    public void setBaseDebugId(String baseDebugId) {
        this.baseDebugId = baseDebugId;
    }

    @Override
    public HandlerRegistration addGroupNameSelectedHandler(GroupNameSelected.GroupNameSelectedHandler handler) {
        return ensureHandlers().addHandler(GroupNameSelected.TYPE, handler);
    }

    protected HandlerManager ensureHandlers() {
        if (handlerManager == null) {
            handlerManager = new HandlerManager(this);
        }
        return handlerManager;
    }
}
