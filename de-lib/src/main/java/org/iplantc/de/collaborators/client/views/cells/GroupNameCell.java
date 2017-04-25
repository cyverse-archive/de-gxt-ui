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
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Event;

/**
 * @author aramsey
 */
public class GroupNameCell extends AbstractCell<Group> {

    public interface GroupNameCellAppearance {
        String CLICKABLE_ELEMENT_NAME = "groupName";

        void render(SafeHtmlBuilder safeHtmlBuilder, Group group, String debugID);
    }

    private GroupNameCellAppearance appearance = GWT.create(GroupNameCellAppearance.class);
    private String baseDebugId;
    private GroupNameSelected.GroupNameSelectedHandler handler;

    public GroupNameCell(GroupNameSelected.GroupNameSelectedHandler handler) {
        super(CLICK);
        this.handler = handler;
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
            handler.onGroupNameSelected(new GroupNameSelected(value));
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
}
