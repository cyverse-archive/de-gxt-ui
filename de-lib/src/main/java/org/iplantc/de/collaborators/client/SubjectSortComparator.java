package org.iplantc.de.collaborators.client;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;

import com.google.common.base.Strings;

import java.util.Comparator;

public class SubjectSortComparator implements Comparator<Subject> {

    @Override
    public int compare(Subject o1, Subject o2) {
        int compare;

        compare = compareGroups(o1, o2);
        if (compare != 0) return compare;

        String aLastName = o1.getLastName();
        String bLastName = o2.getLastName();
        compare = compareStrings(aLastName, bLastName);
        if (compare != 0) return compare;

        String aFirstName = o1.getFirstName();
        String bFirstName = o2.getFirstName();
        compare = compareStrings(aFirstName, bFirstName);
        if (compare != 0) return compare;

        String aName = o1.getName();
        String bName = o2.getName();
        compare = compareStrings(aName, bName);
        if (compare != 0) return compare;

        String aId = o1.getId();
        String bId = o2.getId();
        return compareStrings(aId, bId);
    }

    int compareGroups(Subject o1, Subject o2) {
        boolean firstIsGroup = Group.GROUP_IDENTIFIER.equals(o1.getSourceId());
        boolean secondIsGroup = Group.GROUP_IDENTIFIER.equals(o2.getSourceId());

        //If both are groups
        if (firstIsGroup && secondIsGroup) {
            return o1.getSubjectDisplayName().compareToIgnoreCase(o2.getSubjectDisplayName());
        }

        //If either is a group
        if (firstIsGroup || secondIsGroup) {
            return firstIsGroup ? -1 : 1;
        }

        return 0;
    }

    int compareStrings(String s1, String s2) {
        boolean isNullOrEmptyS1 = Strings.isNullOrEmpty(s1);
        boolean isNullOrEmptyS2 = Strings.isNullOrEmpty(s2);

        //If both strings have values
        if (!isNullOrEmptyS1 && !isNullOrEmptyS2) {
            return s1.compareToIgnoreCase(s2);
        }

        //If either string has a value, compare
        if (!isNullOrEmptyS1 || !isNullOrEmptyS2) {
            return isNullOrEmptyS1 ? -1 : 1;
        }

        return 0;
    }
}

