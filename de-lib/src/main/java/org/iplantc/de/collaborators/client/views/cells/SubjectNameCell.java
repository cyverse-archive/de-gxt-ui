package org.iplantc.de.collaborators.client.views.cells;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.collaborators.shared.CollaboratorsModule;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * @author jstroot
 */
public final class SubjectNameCell extends AbstractCell<Subject> {

    public interface SubjectNameCellAppearance {
        void render(SafeHtmlBuilder safeHtmlBuilder, Subject subject, String debugID);
    }

    private final SubjectNameCellAppearance appearance = GWT.create(SubjectNameCellAppearance.class);
    private String baseDebugId;

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
}
