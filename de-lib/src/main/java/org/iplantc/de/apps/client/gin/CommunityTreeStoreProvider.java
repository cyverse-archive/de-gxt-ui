package org.iplantc.de.apps.client.gin;

import org.iplantc.de.client.models.HasId;
import org.iplantc.de.client.models.groups.Group;

import com.google.inject.Provider;

import com.sencha.gxt.data.shared.TreeStore;

/**
 * @author aramsey
 */
public class CommunityTreeStoreProvider implements Provider<TreeStore<Group>> {

    @Override
    public TreeStore<Group> get() {
        return new TreeStore<>(HasId::getId);
    }
}
