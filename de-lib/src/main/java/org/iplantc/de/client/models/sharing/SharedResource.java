package org.iplantc.de.client.models.sharing;

import org.iplantc.de.client.models.HasQualifiedId;

import com.google.gwt.user.client.ui.HasName;

public class SharedResource implements HasQualifiedId, HasName {

    /**
     * id of the shared resource
     */
    private String id;

    /**
     * systemId of the shared resource
     */
    private String systemId;

    /**
     * name of the shared resource
     */
    private String name;

    public SharedResource(String id, String name) {
        this(null, id, name);
    }

    public SharedResource(String systemId, String id, String name) {
        this.systemId = systemId;
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }
}
