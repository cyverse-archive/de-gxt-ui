package org.iplantc.de.collaborators.client.views.cells;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.collaborators.client.events.GroupNameSelected;
import org.iplantc.de.collaborators.shared.CollaboratorsModule;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Event;

/**
 * @author jstroot
 */
public final class SubjectNameCell extends AbstractCell<Subject> {

    public interface SubjectNameCellAppearance {
        String CLICKABLE_ELEMENT_NAME = "groupName";

        void render(SafeHtmlBuilder safeHtmlBuilder, Subject subject, String debugID);
    }

    private final SubjectNameCellAppearance appearance = GWT.create(SubjectNameCellAppearance.class);
    private String baseDebugId;
    private HasHandlers hasHandlers;

    @Override
    public void onBrowserEvent(Context context,
                               Element parent,
                               Subject value,
                               NativeEvent event,
                               ValueUpdater<Subject> valueUpdater) {
        if (value == null || !Group.GROUP_IDENTIFIER.equals(value.getSourceId())) {
            return;
        }

        Element eventTargetElement = Element.as(event.getEventTarget());
        if ((Event.as(event).getTypeInt() == Event.ONCLICK)
            && eventTargetElement.getAttribute("name").equalsIgnoreCase(GroupNameCell.GroupNameCellAppearance.CLICKABLE_ELEMENT_NAME)) {
            hasHandlers.fireEvent(new GroupNameSelected(value));
        }
    }

    public SubjectNameCell() {
        super(CLICK);
    }

    @Override
    public void render(Context context,
                       Subject value,
                       SafeHtmlBuilder sb) {
        String debugID = baseDebugId + "." + value.getId() + CollaboratorsModule.Ids.SUBJECT_NAME_CELL;
        appearance.render(sb, value, debugID);
    }

    public void setBaseDebugId(String baseDebugId) {
        this.baseDebugId = baseDebugId;
    }

    public void setHasHandlers(HasHandlers hasHandlers) {
        this.hasHandlers = hasHandlers;
    }
}
