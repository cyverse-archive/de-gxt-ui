/**
 *
 *
 */
package org.iplantc.de.client.models.sharing;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.diskResources.PermissionValue;

/**
 * @author sriram
 */
public class Sharing {

    private String id;
    private String systemId;
    private PermissionValue displayPermission;
    private PermissionValue permission;
    private final Subject subject;
    private String name;


    public Sharing(final Subject c, final PermissionValue p, final String id, final String name) {
        this(c, p, null, id, name);
    }

    public Sharing(final Subject c, final PermissionValue p, final String systemId, final String id, final String name) {
        this.subject = c;
        this.systemId = systemId;
        setId(id);
        setName(name);
        if (p != null) {
            permission = p;
            displayPermission = permission;
        }

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isReadable() {
        return isWritable() || PermissionValue.read.equals(permission);
    }

    public boolean isWritable() {
        return isOwner() || PermissionValue.write.equals(permission);
    }

    public boolean isOwner() {
        return PermissionValue.own.equals(permission);
    }

    public String getId() {
        return id;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    //TODO REMOVE ME, I'M TEMPORARY
    public String getSourceId() {
        return "ldap";
    }

    public String getKey() {
        return getSubject().getId() + getSystemId() + getId();
    }

    public void setPermission(PermissionValue perm) {
        this.permission = perm;
    }

    public PermissionValue getPermission() {
        return permission;
    }

    public void setDisplayPermission(PermissionValue perm) {
        displayPermission = perm;
    }

    public PermissionValue getDisplayPermission() {
        return displayPermission;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Sharing)) {
            return false;
        }
        Sharing s = (Sharing)o;
        return getKey().equals(s.getKey()) && s.getDisplayPermission().equals(getDisplayPermission());
    }

    public Sharing copy() {
        return new Sharing(subject, permission, systemId, id, name);
    }

    public String getUserName() {
        return getSubject().getId();
    }

    public String getName() {
        return name;
    }

    public Subject getSubject() {
        return subject;
    }


    public String getCollaboratorName() {
        StringBuilder builder = new StringBuilder();
        if (getSubject().getFirstName() != null && !getSubject().getFirstName().isEmpty()) {
            builder.append(getSubject().getFirstName());
            if (getSubject().getLastName() != null && !getSubject().getLastName().isEmpty()) {
                builder.append(" ");
                builder.append(getSubject().getLastName());
            }
            return builder.toString();
        } else {
            return getSubject().getId();
        }
    }

}
