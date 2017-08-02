package org.iplantc.de.teams.client.gin;

import org.iplantc.de.teams.client.EditTeamView;
import org.iplantc.de.teams.client.TeamsView;
import org.iplantc.de.teams.client.presenter.EditTeamPresenterImpl;
import org.iplantc.de.teams.client.presenter.TeamsPresenterImpl;
import org.iplantc.de.teams.client.views.EditTeamViewImpl;
import org.iplantc.de.teams.client.views.TeamsViewImpl;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;

/**
 * The gin module for the Teams view
 *
 * @author aramsey
 */
public class TeamsGinModule extends AbstractGinModule {

    @Override
    protected void configure() {
        install(new GinFactoryModuleBuilder().implement(TeamsView.class, TeamsViewImpl.class)
                                             .build(TeamsViewFactory.class));
        bind(TeamsView.Presenter.class).to(TeamsPresenterImpl.class);
        bind(EditTeamView.class).to(EditTeamViewImpl.class);
        bind(EditTeamView.Presenter.class).to(EditTeamPresenterImpl.class);
    }
}
