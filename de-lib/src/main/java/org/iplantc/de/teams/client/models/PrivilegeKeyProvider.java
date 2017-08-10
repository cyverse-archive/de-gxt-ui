package org.iplantc.de.teams.client.models;

import org.iplantc.de.client.models.groups.Privilege;

import com.sencha.gxt.data.shared.ModelKeyProvider;

public class PrivilegeKeyProvider implements ModelKeyProvider<Privilege> {
    @Override
    public String getKey(Privilege privilege) {
        // Username + privilege
        return privilege.getSubject().getId() + privilege.getPrivilegeType();
    }
}
