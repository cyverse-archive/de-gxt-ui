package org.iplantc.de.apps.client.presenter.communities;

import org.iplantc.de.client.models.groups.Group;

import java.util.Comparator;

/**
 * @author aramsey
 *
 * A comparator for sorting Groups by name
 */
public class GroupComparator implements Comparator<Group> {
    @Override
    public int compare(Group group1, Group group2) {
        return group1.getSubjectDisplayName().compareToIgnoreCase(group2.getSubjectDisplayName());
    }
}
