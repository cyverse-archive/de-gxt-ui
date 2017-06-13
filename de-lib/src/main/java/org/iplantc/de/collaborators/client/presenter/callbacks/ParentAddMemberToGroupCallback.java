package org.iplantc.de.collaborators.client.presenter.callbacks;

import org.iplantc.de.client.models.groups.UpdateMemberResult;
import org.iplantc.de.collaborators.client.presenter.ManageCollaboratorsPresenter.AddMemberToGroupCallback;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * A class that will fire the handleSuccess method when all callbacks have reported as finished
 * @author aramsey
 */
public abstract class ParentAddMemberToGroupCallback {
    int doneCount = 0;
    List<AddMemberToGroupCallback> childCallbacks;
    List<UpdateMemberResult> totalResults = Lists.newArrayList();

    public ParentAddMemberToGroupCallback(List<AddMemberToGroupCallback> childCallbacks) {
        if (childCallbacks == null || childCallbacks.size() == 0) {
            return;
        }

        this.childCallbacks = childCallbacks;

        for(AddMemberToGroupCallback callback : childCallbacks) {
            callback.setParent(this);
        }
    }

    public synchronized void done(List<UpdateMemberResult> results) {
        doneCount++;

        if (results != null && !results.isEmpty()) {
            totalResults.addAll(results);
        }

        if (doneCount == childCallbacks.size()) {
            handleSuccess(totalResults);
        }
    }

    public abstract void handleSuccess(List<UpdateMemberResult> totalResults);
}
