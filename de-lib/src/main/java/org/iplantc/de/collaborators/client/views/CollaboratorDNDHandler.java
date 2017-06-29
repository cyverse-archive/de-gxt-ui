package org.iplantc.de.collaborators.client.views;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.collaborators.client.ManageCollaboratorsView;
import org.iplantc.de.collaborators.client.presenter.ManageCollaboratorsPresenter;

import com.google.common.base.Joiner;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

import com.sencha.gxt.dnd.core.client.DndDragEnterEvent;
import com.sencha.gxt.dnd.core.client.DndDragMoveEvent;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.StatusProxy;
import com.sencha.gxt.fx.client.DragMoveEvent;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author aramsey
 */
public class CollaboratorDNDHandler implements DndDragStartEvent.DndDragStartHandler,
                                               DndDropEvent.DndDropHandler,
                                               DndDragMoveEvent.DndDragMoveHandler,
                                               DndDragEnterEvent.DndDragEnterHandler {

    ManageCollaboratorsView.Appearance appearance;
    ManageCollaboratorsPresenter presenter;
    boolean moved;

    public CollaboratorDNDHandler(ManageCollaboratorsView.Appearance appearance,
                                  ManageCollaboratorsPresenter presenter) {
        this.appearance = appearance;
        this.presenter = presenter;
    }

    @Override
    public void onDragEnter(DndDragEnterEvent event) {
        moved = false;

        List<Subject> sourceSubjects = getDragSources();
        DragMoveEvent dragEnterEvent = event.getDragEnterEvent();
        EventTarget target = dragEnterEvent.getNativeEvent().getEventTarget();
        Subject targetSubject = getDropTargetSubject(Element.as(target));

        if (!validateDropStatus(targetSubject, sourceSubjects, event.getStatusProxy())) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onDragMove(DndDragMoveEvent event) {
        moved = true;

        List<Subject> sourceSubjects = getDragSources();
        EventTarget target = event.getDragMoveEvent().getNativeEvent().getEventTarget();
        Subject targetSubject = getDropTargetSubject(Element.as(target));

        if (!validateDropStatus(targetSubject, sourceSubjects, event.getStatusProxy())) {
            event.setCancelled(true);
        }
    }


    @Override
    public void onDragStart(DndDragStartEvent event) {
        moved = false;

        List<Subject> sourceSubjects = getDragSources();

        if (sourceSubjects == null) {
            // Cancel drag
            event.setCancelled(true);
        } else {
            event.setData(sourceSubjects);
            event.getStatusProxy().update((SafeHtml)() -> getSubjectNames(sourceSubjects));
            event.getStatusProxy().setStatus(true);
            event.setCancelled(false);
        }
    }

    @Override
    public void onDrop(DndDropEvent event) {
        if (!moved) return;

        List<Subject> sourceSubjects = getDragSources();
        EventTarget target = event.getDragEndEvent().getNativeEvent().getEventTarget();
        Subject targetSubject = getDropTargetSubject(Element.as(target));

        if (validateDropStatus(targetSubject, sourceSubjects, event.getStatusProxy())) {
            presenter.subjectsDNDToList(targetSubject, sourceSubjects);
        }

    }

    private List<Subject> getDragSources() {
        return presenter.getSelectedSubjects();
    }

    private Subject getDropTargetSubject(Element eventTarget) {
        return presenter.getSubjectFromElement(eventTarget);
    }

    private boolean validateDropStatus(final Subject targetSubject,
                                       final List<Subject> sourceSubjects,
                                       final StatusProxy status) {
        // Verify we have drag data.
        if (sourceSubjects == null || sourceSubjects.size() == 0) {
            status.setStatus(false);
            status.update(SafeHtmlUtils.fromString(""));
            return false;
        }

        String stringNames = getSubjectNames(sourceSubjects);

        // Verify we have a drop target.
        if (targetSubject == null) {
            status.setStatus(false);
            status.update(SafeHtmlUtils.fromString(appearance.onlyDNDToListSupported()));
            return false;
        }

        // Verify target is a collaborator list
        if (!Group.GROUP_IDENTIFIER.equals(targetSubject.getSourceId())) {
            status.setStatus(false);
            status.update(SafeHtmlUtils.fromString(appearance.onlyDNDToListSupported()));
            return false;
        }

        // Verify no self-adds
        List<Subject> matches = sourceSubjects.stream()
                                              .filter(subject -> targetSubject.getId()
                                                                              .equals(subject.getId()))
                                              .collect(Collectors.toList());
        if (matches != null && !matches.isEmpty()) {
            status.setStatus(false);
            status.update(SafeHtmlUtils.fromString(appearance.groupSelfAdd()));
            return false;
        }

        // Reset status message
        status.setStatus(true);
        status.update(SafeHtmlUtils.fromString(stringNames + " > " + targetSubject.getSubjectDisplayName()));
        return true;
    }

    private String getSubjectNames(List<Subject> subjects) {
        List<String> names = subjects.stream()
                                           .map(Subject::getSubjectDisplayName)
                                           .collect(Collectors.toList());
        return Joiner.on(", ").join(names);
    }
}
