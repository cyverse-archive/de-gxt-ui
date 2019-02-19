package org.iplantc.de.apps.client.models;

import org.iplantc.de.client.models.groups.Group;

import com.sencha.gxt.core.client.ValueProvider;

/**
 * @author aramsey
 */
public class CommunityStringValueProvider implements ValueProvider<Group, String> {

    @Override
    public String getValue(Group object) {
        return object.getSubjectDisplayName();
    }

    @Override
    public void setValue(Group object, String value) {
        // do nothing intentionally
    }

    @Override
    public String getPath() {
        return null;
    }
}
