package org.iplantc.de.collaborators.client.events;

import org.iplantc.de.client.models.collaborators.Subject;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;


/**
 * A GWT event that fires when the SubjectDeleteCell is clicked
 */
public class SubjectDeleteCellClicked
        extends GwtEvent<SubjectDeleteCellClicked.SubjectDeleteCellClickedHandler> {
    public interface SubjectDeleteCellClickedHandler extends EventHandler {
        void onSubjectDeleteCellClicked(SubjectDeleteCellClicked event);
    }

    public interface HasSubjectDeleteCellClickedHandlers {
        HandlerRegistration addSubjectDeleteCellClickedHandler(SubjectDeleteCellClickedHandler handler);
    }
    public static Type<SubjectDeleteCellClickedHandler> TYPE = new Type<SubjectDeleteCellClickedHandler>();

    private Subject subject;

    public SubjectDeleteCellClicked(Subject subject) {
        this.subject = subject;
    }

    public Type<SubjectDeleteCellClickedHandler> getAssociatedType() {
        return TYPE;
    }

    protected void dispatch(SubjectDeleteCellClickedHandler handler) {
        handler.onSubjectDeleteCellClicked(this);
    }

    public Subject getSubject() {
        return subject;
    }
}
