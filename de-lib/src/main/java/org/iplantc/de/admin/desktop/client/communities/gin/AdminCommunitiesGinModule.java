package org.iplantc.de.admin.desktop.client.communities.gin;

import org.iplantc.de.admin.desktop.client.communities.AdminCommunitiesView;
import org.iplantc.de.admin.desktop.client.communities.presenter.AdminCommunitiesPresenterImpl;
import org.iplantc.de.admin.desktop.client.communities.presenter.AdminManageCommunitiesPresenterImpl;
import org.iplantc.de.admin.desktop.client.communities.service.AdminCommunityServiceFacade;
import org.iplantc.de.admin.desktop.client.communities.service.impl.AdminCommunityServiceFacadeImpl;
import org.iplantc.de.admin.desktop.client.communities.views.AdminCommunitiesViewImpl;
import org.iplantc.de.apps.client.ManageCommunitiesView;
import org.iplantc.de.apps.client.gin.CommunityTreeStoreProvider;
import org.iplantc.de.apps.client.views.communities.ManageCommunitiesViewImpl;
import org.iplantc.de.client.models.groups.Group;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.assistedinject.GinFactoryModuleBuilder;
import com.google.inject.TypeLiteral;

import com.sencha.gxt.data.shared.TreeStore;

/**
 * @author aramsey
 */
public class AdminCommunitiesGinModule extends AbstractGinModule {
    @Override
    protected void configure() {
        bind(new TypeLiteral<TreeStore<Group>>(){}).toProvider(CommunityTreeStoreProvider.class);
        install(new GinFactoryModuleBuilder().implement(AdminCommunitiesView.class, AdminCommunitiesViewImpl.class).build(
                AdminCommunitiesViewFactory.class));
        bind(AdminCommunitiesView.Presenter.class).to(AdminCommunitiesPresenterImpl.class);
        bind(AdminCommunityServiceFacade.class).to(AdminCommunityServiceFacadeImpl.class);
        bind(ManageCommunitiesView.class).to(ManageCommunitiesViewImpl.class);
        bind(ManageCommunitiesView.Presenter.class).to(AdminManageCommunitiesPresenterImpl.class);
    }
}
