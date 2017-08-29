package org.iplantc.de.teams.client.models;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Privilege;
import org.iplantc.de.client.models.groups.PrivilegeType;

import com.google.gwt.editor.client.Editor.Path;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface PrivilegeProperties extends PropertyAccess<Privilege> {

    ValueProvider<Privilege, PrivilegeType> privilegeType();

    ValueProvider<Privilege, String> type();

    @Path("subject")
    ValueProvider<Privilege, Subject> name();

    @Path("subject.getInstitution")
    ValueProvider<Privilege, String> institution();
}
