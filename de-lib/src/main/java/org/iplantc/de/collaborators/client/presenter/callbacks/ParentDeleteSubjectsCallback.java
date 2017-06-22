package org.iplantc.de.collaborators.client.presenter.callbacks;

import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.UpdateMemberResult;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author aramsey
 */
public abstract class ParentDeleteSubjectsCallback {
    int doneCount;
    List<UpdateMemberResult> successUsers = Lists.newArrayList();
    List<Group> successGroups = Lists.newArrayList();
    List<Throwable> failures = Lists.newArrayList();

    public void setCallbackCounter(int doneCount) {
        this.doneCount = doneCount;
    }

    public synchronized void updateCounter() {
        doneCount--;

        if (doneCount == 0) {
            whenDone(successUsers, successGroups, failures);
        }
    }

    public void done(Throwable caught) {
        failures.add(caught);

        updateCounter();
    }

    public void done(List<UpdateMemberResult> results) {
        if (results != null && !results.isEmpty()) {
            successUsers.addAll(results);
        }

        updateCounter();
    }

    public void done(Group group) {
        if (group != null) {
            successGroups.add(group);
        }

        updateCounter();
    }

    public abstract void whenDone(List<UpdateMemberResult> totalResults, List<Group> successGroups, List<Throwable> failures);
}
