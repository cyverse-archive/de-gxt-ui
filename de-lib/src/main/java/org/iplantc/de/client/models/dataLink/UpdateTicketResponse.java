package org.iplantc.de.client.models.dataLink;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

/**
 * The autobean representation of the JSON object that is returned when a data link ticket has been updated
 */
public interface UpdateTicketResponse extends HasTickets {

    @PropertyName("user")
    String getUserId();

    @PropertyName("user")
    void setUserId(String userId);

}
