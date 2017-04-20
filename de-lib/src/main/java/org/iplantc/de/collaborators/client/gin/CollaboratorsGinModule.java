package org.iplantc.de.collaborators.client.gin;

import org.iplantc.de.collaborators.client.presenter.ManageCollaboratorsPresenter;
import org.iplantc.de.collaborators.client.views.ManageCollaboratorsDialog;
import org.iplantc.de.collaborators.client.views.ManageCollaboratorsView;
import org.iplantc.de.collaborators.client.views.ManageCollaboratorsViewImpl;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;

/**
 * @author aramsey
 */
public class CollaboratorsGinModule extends AbstractGinModule {
    @Override
    protected void configure() {
        install(new GinFactoryModuleBuilder().implement(ManageCollaboratorsView.class,
                                                        ManageCollaboratorsViewImpl.class)
                                             .build(ManageCollaboratorsViewFactory.class));
        bind(ManageCollaboratorsView.Presenter.class).to(ManageCollaboratorsPresenter.class);
        bind(ManageCollaboratorsDialog.class);
    }
}
