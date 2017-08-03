package org.iplantc.de.client.models.groups;

/**
 * Enum to manage grouper permissions with readable labels
 */
public enum PrivilegeType {
    admin("Can help manage the team"),
    read("Can view the team and its members"),
    view("Can view the team"),
    optin("Can opt to join the team"),
    optout("Can opt to leave the team");

    private String label;
    PrivilegeType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}