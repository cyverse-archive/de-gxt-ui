package org.iplantc.de.teams.client.views.cells;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;

import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.teams.client.events.TeamNameSelected;
import org.iplantc.de.teams.shared.Teams;

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
 * The cell within the Teams view that will show the user the info icon and display Team info once clicked
 * @author aramsey
 */
public class TeamNameCell extends AbstractCell<Group> implements TeamNameSelected.HasTeamNameSelectedHandlers {

    public interface TeamNameCellAppearance {
        String CLICKABLE_ELEMENT_NAME = "teamName";

        void render(SafeHtmlBuilder sb, Group group, String debugId);
    }

    private final TeamNameCellAppearance appearance;
    private String baseId;
    private HandlerManager handlerManager;


    public TeamNameCell() {
        this(GWT.create(TeamNameCellAppearance.class));
    }

    public TeamNameCell(TeamNameCellAppearance appearance) {
        super(CLICK);
        this.appearance = appearance;
    }

    @Override
    public void render(Context context, Group value, SafeHtmlBuilder sb) {
        String debugId = baseId + "." + value.getId() + Teams.Ids.TEAM_INFO_CELL;
        appearance.render(sb, value, debugId);
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
            && eventTargetElement.getAttribute("name")
                                 .equalsIgnoreCase(TeamNameCellAppearance.CLICKABLE_ELEMENT_NAME)) {
            handlerManager.fireEvent(new TeamNameSelected(value));
        }
    }

    public void setBaseDebugId(String baseDebugId) {
        this.baseId = baseDebugId;
    }

    @Override
    public HandlerRegistration addTeamNameSelectedHandler(TeamNameSelected.TeamNameSelectedHandler handler) {
        return ensureHandlers().addHandler(TeamNameSelected.TYPE, handler);
    }

    protected HandlerManager ensureHandlers() {
        if (handlerManager == null) {
            handlerManager = new HandlerManager(this);
        }
        return handlerManager;
    }
}
