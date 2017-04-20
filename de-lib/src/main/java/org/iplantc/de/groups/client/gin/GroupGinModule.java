package org.iplantc.de.groups.client.gin;

import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.client.services.impl.GroupServiceFacadeImpl;
import org.iplantc.de.groups.client.GroupView;
import org.iplantc.de.groups.client.presenter.GroupPresenterImpl;
import org.iplantc.de.groups.client.views.GroupViewImpl;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;

/**
 * @author aramsey
 */
public class GroupGinModule extends AbstractGinModule {
    @Override
    protected void configure() {
        install(new GinFactoryModuleBuilder().implement(GroupView.class, GroupViewImpl.class)
                                             .build(GroupViewFactory.class));

        bind(GroupView.GroupPresenter.class).to(GroupPresenterImpl.class);
        bind(GroupServiceFacade.class).to(GroupServiceFacadeImpl.class);
    }
}
