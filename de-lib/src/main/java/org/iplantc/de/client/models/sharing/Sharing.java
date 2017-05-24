/**
 *
 *
 */
package org.iplantc.de.client.models.sharing;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;

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


    public Sharing(final Subject subject, final PermissionValue permission, final String id, final String name) {
        this(subject, permission, null, id, name);
    }

    public Sharing(final Subject subject, final PermissionValue permission, final String systemId, final String id, final String name) {
        this.subject = subject;
        this.systemId = systemId;
        setId(id);
        setName(name);
        if (permission != null) {
            this.permission = permission;
            displayPermission = this.permission;
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

    public String getSourceId() {
        if (subject instanceof Group) {
            return Subject.GROUP_IDENTIFIER;
        }
        return subject.getSourceId();
    }

    public String getKey() {
        return getSubject().getId();
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

    public String getSubjectName() {
        return subject.getSubjectDisplayName();
    }
}
