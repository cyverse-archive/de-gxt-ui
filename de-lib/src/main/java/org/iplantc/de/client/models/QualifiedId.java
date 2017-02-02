package org.iplantc.de.client.models;

/**
 * @author dennis
 */
public class QualifiedId  implements HasQualifiedId {

    private String systemId;
    private String id;

    @Override
    public String getSystemId() {
        return systemId;
    }

    @Override
    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public QualifiedId(final String systemId, final String id) {
        this.systemId = systemId;
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof QualifiedId) {
            QualifiedId other = (QualifiedId) obj;
            return other.getSystemId() == systemId && other.getId() == id;
        }
        return false;
    }
}
