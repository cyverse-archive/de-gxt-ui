package org.iplantc.de.teams.client.models;

import org.iplantc.de.client.models.groups.Privilege;

import com.google.gwt.editor.client.Editor.Path;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface PrivilegeProperties extends PropertyAccess<Privilege> {

    @Path("name")
    ValueProvider<Privilege, String> privilege();

    ValueProvider<Privilege, String> type();

    @Path("subject.getName")
    ValueProvider<Privilege, String> name();

}
