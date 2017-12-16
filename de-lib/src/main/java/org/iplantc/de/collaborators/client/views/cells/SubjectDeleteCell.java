package org.iplantc.de.collaborators.client.views.cells;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.collaborators.client.events.SubjectDeleteCellClicked;
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
 * A cell that contains just a "delete" icon used for deleting a subject
 * @author aramsey
 */
public final class SubjectDeleteCell extends AbstractCell<Subject> {

    public interface SubjectDeleteCellAppearance {
        String CLICKABLE_ELEMENT_NAME = "deleteSubject";

        void render(SafeHtmlBuilder safeHtmlBuilder, String debugID);
    }

    private final SubjectDeleteCellAppearance appearance = GWT.create(SubjectDeleteCellAppearance.class);
    private String baseDebugId;
    private HasHandlers hasHandlers;

    public SubjectDeleteCell() {
        super(CLICK);
    }

    @Override
    public void onBrowserEvent(Context context,
                               Element parent,
                               Subject value,
                               NativeEvent event,
                               ValueUpdater<Subject> valueUpdater) {
        Element eventTargetElement = Element.as(event.getEventTarget());
        if (Event.as(event).getTypeInt() == Event.ONCLICK
            && eventTargetElement.getAttribute("name").equalsIgnoreCase(SubjectDeleteCellAppearance.CLICKABLE_ELEMENT_NAME)
            && hasHandlers != null) {
            hasHandlers.fireEvent(new SubjectDeleteCellClicked(value));
        }
    }

    @Override
    public void render(Context context,
                       Subject value,
                       SafeHtmlBuilder sb) {
        String debugID = baseDebugId + "." + value.getId() + CollaboratorsModule.Ids.SUBJECT_NAME_CELL;
        appearance.render(sb, debugID);
    }

    public void setBaseDebugId(String baseDebugId) {
        this.baseDebugId = baseDebugId;
    }

    public void setHasHandlers(HasHandlers hasHandlers) {
        this.hasHandlers = hasHandlers;
    }
}
