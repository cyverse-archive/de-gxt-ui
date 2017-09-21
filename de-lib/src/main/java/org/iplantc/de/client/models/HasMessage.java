package org.iplantc.de.client.models;

/**
 * Interface definition for POJOs which have a message property.
 *
 * This class is often used with autobeans.
 * @author aramsey
 */
public interface HasMessage {

    String getMessage();

    void setMessage(String message);
}
