package org.iplantc.de.client.models.notifications.payload;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

/**
 * Autobean representation of the payload received from a team-related notification
 */
public interface PayloadTeam {

    @PropertyName("email_address")
    String getEmailAddress();

    @PropertyName("requester_id")
    String getRequesterId();

    @PropertyName("requester_name")
    String getRequesterName();

    @PropertyName("requester_email")
    String getRequesterEmail();

    @PropertyName("requester_message")
    String getRequesterMessage();

    @PropertyName("team_name")
    String getTeamName();

    String getAction();
    void setAction(String action);
}
