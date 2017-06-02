package org.iplantc.de.collaborators.client.models;

import org.iplantc.de.client.models.collaborators.Subject;

import java.util.Comparator;

/**
 * @author jstroot
 */
public final class SubjectNameComparator implements Comparator<Subject> {
    @Override
    public int compare(Subject o1, Subject o2) {

        if (o1.getFirstName() == null && o2.getFirstName() == null) {
            return 0;
        }

        if (o1.getFirstName() == null) {
            return -1;
        }

        if (o2.getFirstName() == null) {
            return 1;
        }

        return o1.getFirstName().compareTo(o2.getFirstName());
    }
}
