package org.iplantc.de.teams.client.models;

import org.iplantc.de.client.models.groups.Group;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

/**
 * Properties interface to access values within a Group
 * @author aramsey
 */
public interface GroupProperties extends PropertyAccess<Group> {

    ModelKeyProvider<Group> id();

    ValueProvider<Group, String> name();

    ValueProvider<Group, String> description();

}
