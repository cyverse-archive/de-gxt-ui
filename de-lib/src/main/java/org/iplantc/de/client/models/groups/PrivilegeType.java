package org.iplantc.de.client.models.groups;

/**
 * Enum to manage grouper permissions with readable labels
 */
public enum PrivilegeType {
    admin("Can manage the team"),
    read("Can view the team and its members"),
    view("Can view the team"),
    readOptin("Can view the team, its members, and opt to join"),
    optin("Can view the team and opt to join"),
    optout("Can opt to leave the team");

    private String label;
    PrivilegeType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static PrivilegeType fromTypeString(String typeString) {
        if (typeString == null || typeString.isEmpty()) {
            return null;
        }
        try {
            return valueOf(typeString);
        } catch (Exception e) {
            return null;
        }
    }
}
