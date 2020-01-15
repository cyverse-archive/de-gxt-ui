package org.iplantc.de.teams.client.views;

import org.iplantc.de.commons.client.util.CyVerseReactComponents;
import org.iplantc.de.teams.client.EditTeamView;
import org.iplantc.de.teams.client.ReactTeamViews;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.autobean.shared.Splittable;

public class EditTeamViewImpl extends Composite implements EditTeamView {

    private final HTMLPanel panel;
    ReactTeamViews.EditTeamProps props;

    @Inject
    public EditTeamViewImpl(@Assisted ReactTeamViews.EditTeamProps baseProps) {
        this.props = baseProps;
        panel = new HTMLPanel("<div></div>");
    }

    @Override
    public void edit(Splittable team, Splittable privileges, Splittable members) {
        props.team = team;
        props.privileges = privileges;
        props.members = members;
        props.loading = false;
        render();
    }

    @Override
    public void updateTeam(Splittable team) {
        props.team = team;
        render();
    }

    @Override
    public void mask() {
        props.open = true;
        props.loading = true;
        render();
    }

    @Override
    public void unmask() {
        props.open = true;
        props.loading = false;
        render();
    }

    @Override
    public void close() {
        props.loading = false;
        props.open = false;
        render();
    }

    private void render() {
        CyVerseReactComponents.render(ReactTeamViews.EditTeamDialog, props, panel.getElement());
    }
}
