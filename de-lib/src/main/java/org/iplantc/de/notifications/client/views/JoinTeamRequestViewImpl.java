/**
 *
 */
package org.iplantc.de.notifications.client.views;

import static org.iplantc.de.notifications.client.ReactNotifications.JoinTeamRequestDialog;

import org.iplantc.de.commons.client.util.CyVerseReactComponents;
import org.iplantc.de.notifications.client.ReactNotifications;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.Splittable;

/**
 * The view to allow a team admin to approve or deny a join request from another user
 *
 * @author aramsey
 */
public class JoinTeamRequestViewImpl implements JoinTeamRequestView {

    @Inject
    public JoinTeamRequestViewImpl() {

    }

    @Override
    public void edit(Presenter presenter, Splittable payloadTeam) {
        Scheduler.get().scheduleFinally(() -> {
            ReactNotifications.JoinTeamProps props = new ReactNotifications.JoinTeamProps();
            props.presenter = presenter;
            props.request = payloadTeam;
            props.dialogOpen = true;
            CyVerseReactComponents.render(JoinTeamRequestDialog,
                                          props,
                                          new HTMLPanel("<div></div>").getElement());
        });
    }
}
