package org.iplantc.de.teams.client.views;

import org.iplantc.de.commons.client.util.CyVerseReactComponents;
import org.iplantc.de.teams.client.ReactTeamViews;
import org.iplantc.de.teams.client.TeamsView;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.autobean.shared.Splittable;

/**
 * @author aramsey
 */
public class TeamsViewImpl extends Composite implements TeamsView {

    HTMLPanel panel;
    ReactTeamViews.TeamProps props;

    @Inject
    public TeamsViewImpl(@Assisted ReactTeamViews.TeamProps baseProps) {
        panel = new HTMLPanel("<div></div>");
        props = baseProps;
    }

    @Override
    public void setBaseId(String baseId) {
        props.parentId = baseId;
        render();
    }

    @Override
    public void mask() {
        props.loading = true;
        render();
    }

    @Override
    public void unmask() {
        props.loading = false;
        render();
    }

    @Override
    public void showCheckBoxes() {
        props.isSelectable = true;
        render();
    }

    @Override
    public void setTeamList(Splittable teamListSpl) {
        props.teamListing = teamListSpl;
        render();
    }

    @Override
    public void setSelectedTeams(Splittable teamList) {
        props.selectedTeams = teamList;
        render();
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    void render() {
        CyVerseReactComponents.render(ReactTeamViews.Teams, props, panel.getElement());
    }
}
