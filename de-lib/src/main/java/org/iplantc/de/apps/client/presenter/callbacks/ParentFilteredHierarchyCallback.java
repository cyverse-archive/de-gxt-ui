package org.iplantc.de.apps.client.presenter.callbacks;

import org.iplantc.de.apps.client.presenter.hierarchies.OntologyHierarchiesPresenterImpl.FilteredHierarchyCallback;

import java.util.List;

/**
 * @author aramsey
 *
 * A class that will fire the handleSuccess method when all callbacks have reported back as
 * finished
 */
public abstract class ParentFilteredHierarchyCallback {

    int doneCount = 0;
    List<FilteredHierarchyCallback> childCallbacks;

    public ParentFilteredHierarchyCallback(List<FilteredHierarchyCallback> childCallbacks) {
        if (childCallbacks == null || childCallbacks.size() == 0) {
            return;
        }

        this.childCallbacks = childCallbacks;

        for(FilteredHierarchyCallback callback : childCallbacks) {
            callback.setParent(this);
        }
    }

    public synchronized void done() {
        doneCount++;

        if (doneCount == childCallbacks.size()) {
            handleSuccess();
        }
    }

    public abstract void handleSuccess();
}
