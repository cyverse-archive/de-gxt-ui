package org.iplantc.de.communities.client.gin;

import org.iplantc.de.communities.client.CommunitiesView;
import org.iplantc.de.communities.client.presenter.CommunitiesPresenterImpl;
import org.iplantc.de.communities.client.views.CommunitiesViewImpl;

import com.google.gwt.inject.client.AbstractGinModule;

/**
 * @author aramsey
 */
public class CommunitiesGinModule extends AbstractGinModule {
    @Override
    protected void configure() {
        bind(CommunitiesView.class).to(CommunitiesViewImpl.class);
        bind(CommunitiesView.Presenter.class).to(CommunitiesPresenterImpl.class);
    }
}
