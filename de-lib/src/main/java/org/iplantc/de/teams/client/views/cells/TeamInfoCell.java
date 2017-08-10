package org.iplantc.de.teams.client.views.cells;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;

import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.teams.client.events.TeamInfoButtonSelected;
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
public class TeamInfoCell extends AbstractCell<Group> implements TeamInfoButtonSelected.HasTeamInfoButtonSelectedHandlers{

    public interface TeamInfoCellAppearance {
        String CLICKABLE_ELEMENT_NAME = "teamName";

        void render(SafeHtmlBuilder sb, Group group, String debugId);
    }

    private final TeamInfoCellAppearance appearance;
    private String baseId;
    private HandlerManager handlerManager;


    public TeamInfoCell() {
        this(GWT.create(TeamInfoCellAppearance.class));
    }

    public TeamInfoCell(TeamInfoCellAppearance appearance) {
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
                                 .equalsIgnoreCase(TeamInfoCellAppearance.CLICKABLE_ELEMENT_NAME)) {
            handlerManager.fireEvent(new TeamInfoButtonSelected(value));
        }
    }

    public void setBaseDebugId(String baseDebugId) {
        this.baseId = baseDebugId;
    }

    @Override
    public HandlerRegistration addTeamInfoButtonSelectedHandler(TeamInfoButtonSelected.TeamInfoButtonSelectedHandler handler) {
        return ensureHandlers().addHandler(TeamInfoButtonSelected.TYPE, handler);
    }

    protected HandlerManager ensureHandlers() {
        if (handlerManager == null) {
            handlerManager = new HandlerManager(this);
        }
        return handlerManager;
    }
}
