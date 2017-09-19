package org.iplantc.de.client.models.dataLink;

import java.util.List;

/**
 * The autobean representation of a JSON object with a "tickets" key that has a list of strings as the value
 */
public interface HasTickets {

    List<String> getTickets();
    void setTickets(List<String> tickets);
}
