package org.iplantc.de.communities.client.gin;

import org.iplantc.de.communities.client.ManageCommunitiesView;
import org.iplantc.de.communities.client.presenter.ManageCommunitiesPresenterImpl;
import org.iplantc.de.communities.client.views.ManageCommunitiesViewImpl;

import com.google.gwt.inject.client.AbstractGinModule;

/**
 * @author aramsey
 */
public class CommunitiesGinModule extends AbstractGinModule {
    @Override
    protected void configure() {
        bind(ManageCommunitiesView.class).to(ManageCommunitiesViewImpl.class);
        bind(ManageCommunitiesView.Presenter.class).to(ManageCommunitiesPresenterImpl.class);
    }
}
