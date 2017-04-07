package org.iplantc.de.groups.client.model;

import org.iplantc.de.client.models.groups.Group;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

/**
 * @author jstroot
 * @author aramsey
 */
public interface GroupProperties extends PropertyAccess<Group> {

    ModelKeyProvider<Group> id();

    ValueProvider<Group, String> name();

    ValueProvider<Group, String> description();
}
