package org.iplantc.de.notifications.client.gin;

import org.iplantc.de.notifications.client.presenter.JoinTeamRequestPresenter;
import org.iplantc.de.notifications.client.presenter.NotificationPresenterImpl;
import org.iplantc.de.notifications.client.views.JoinTeamRequestView;
import org.iplantc.de.notifications.client.views.JoinTeamRequestViewImpl;
import org.iplantc.de.notifications.client.views.NotificationView;
import org.iplantc.de.notifications.client.views.NotificationViewImpl;

import com.google.gwt.inject.client.AbstractGinModule;

/**
 * @author aramsey
 */
public class NotificationGinModule extends AbstractGinModule {
    @Override
    protected void configure() {
        bind(NotificationView.class).to(NotificationViewImpl.class);
        bind(NotificationView.Presenter.class).to(NotificationPresenterImpl.class);
        bind(JoinTeamRequestView.class).to(JoinTeamRequestViewImpl.class);
        bind(JoinTeamRequestView.Presenter.class).to(JoinTeamRequestPresenter.class);
    }
}
