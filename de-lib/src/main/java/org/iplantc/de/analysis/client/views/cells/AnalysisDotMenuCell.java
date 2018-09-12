package org.iplantc.de.analysis.client.views.cells;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;

import org.iplantc.de.analysis.client.views.widget.AnalysisDotMenu;
import org.iplantc.de.analysis.shared.AnalysisModule;
import org.iplantc.de.client.models.analysis.Analysis;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Event;

/**
 * A cell that contains the "dot" menu (3 vertical dots).  When clicked the menu
 * containing the various Analysis buttons is revealed.
 * @author aramsey
 */
public class AnalysisDotMenuCell extends AbstractCell<Analysis> implements HasCell<Analysis, Analysis> {

    public interface AnalysisDotMenuAppearance  {
        void render(Context context, Analysis value, SafeHtmlBuilder sb, String debugId);

        String commentText();

        ImageResource commentIcon();

        String relaunchText();

        ImageResource relaunchIcon();

        String shareText();

        ImageResource shareIcon();

        String outputFolderText();

        ImageResource outputFolderIcon();

        String parametersText();

        ImageResource parametersIcon();

        String infoText();

        ImageResource infoIcon();

        String completeText();

        ImageResource completeIcon();

        String cancelText();

        ImageResource cancelIcon();

        String deleteText();

        ImageResource deleteIcon();

        String renameText();

        ImageResource renameIcon();
    }

    private AnalysisDotMenuAppearance appearance;
    private String baseDebugId;
    private HasHandlers hasHandlers;

    public AnalysisDotMenuCell() {
        this(GWT.create(AnalysisDotMenuAppearance.class));
    }

    public AnalysisDotMenuCell(final AnalysisDotMenuAppearance appearance) {
        super(CLICK);
        this.appearance = appearance;
    }

    @Override
    public void render(Context context, Analysis value, SafeHtmlBuilder sb) {
        String debugId = getDebugId(value);
        appearance.render(context, value, sb, debugId);
    }

    @Override
    public Cell<Analysis> getCell() {
        return this;
    }

    @Override
    public FieldUpdater<Analysis, Analysis> getFieldUpdater() {
        return null;
    }

    @Override
    public Analysis getValue(Analysis object) {
        return object;
    }

    @Override
    public void onBrowserEvent(Context context,
                               Element parent,
                               Analysis value,
                               NativeEvent event,
                               ValueUpdater<Analysis> valueUpdater) {
        Element eventTarget = Element.as(event.getEventTarget());
        if ((value == null) || !parent.isOrHasChild(eventTarget)) {
            return;
        }

        if (parent.isOrHasChild(eventTarget)) {
            switch (Event.as(event).getTypeInt()) {
                case Event.ONCLICK:
                    AnalysisDotMenu menu = new AnalysisDotMenu(appearance, value, hasHandlers);
                    menu.showAt(event.getClientX(), event.getClientY());
                    menu.ensureDebugId(getDebugId(value));
                    break;
                default:
                    break;
            }
        }
    }

    String getDebugId(Analysis value) {
        return baseDebugId + "." + value.getId() + AnalysisModule.Ids.ANALYSIS_DOT_MENU;
    }

    public void setHasHandlers(HasHandlers hasHandlers) {
        this.hasHandlers = hasHandlers;
    }

    public void setBaseDebugId(String baseDebugId) {
        this.baseDebugId = baseDebugId;
    }
}
