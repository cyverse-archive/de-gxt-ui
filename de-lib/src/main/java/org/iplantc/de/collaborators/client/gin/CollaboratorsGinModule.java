package org.iplantc.de.collaborators.client.gin;

import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.client.services.impl.GroupServiceFacadeImpl;
import org.iplantc.de.collaborators.client.CollaborationView;
import org.iplantc.de.collaborators.client.GroupDetailsView;
import org.iplantc.de.collaborators.client.ManageCollaboratorsView;
import org.iplantc.de.collaborators.client.presenter.CollaborationPresenterImpl;
import org.iplantc.de.collaborators.client.presenter.GroupDetailsPresenterImpl;
import org.iplantc.de.collaborators.client.presenter.ManageCollaboratorsPresenter;
import org.iplantc.de.collaborators.client.util.UserSearchField;
import org.iplantc.de.collaborators.client.views.CollaborationViewImpl;
import org.iplantc.de.collaborators.client.views.GroupDetailsViewImpl;
import org.iplantc.de.collaborators.client.views.ManageCollaboratorsViewImpl;
import org.iplantc.de.collaborators.client.views.dialogs.ChooseCollaboratorsDialog;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;

/**
 * @author aramsey
 */
public class CollaboratorsGinModule extends AbstractGinModule {
    @Override
    protected void configure() {
        install(new GinFactoryModuleBuilder().implement(CollaborationView.class,
                                                        CollaborationViewImpl.class)
                                             .build(CollaborationViewFactory.class));
        bind(CollaborationView.Presenter.class).to(CollaborationPresenterImpl.class);

        install(new GinFactoryModuleBuilder().implement(ManageCollaboratorsView.class,
                                                        ManageCollaboratorsViewImpl.class)
                                             .build(ManageCollaboratorsViewFactory.class));
        bind(ManageCollaboratorsView.Presenter.class).to(ManageCollaboratorsPresenter.class);
        bind(ChooseCollaboratorsDialog.class);
        bind(GroupServiceFacade.class).to(GroupServiceFacadeImpl.class);
        bind(GroupDetailsView.class).to(GroupDetailsViewImpl.class);
        bind(GroupDetailsView.Presenter.class).to(GroupDetailsPresenterImpl.class);
        bind(UserSearchField.class);
    }
}
